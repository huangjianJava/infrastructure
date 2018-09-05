package org.infrastructure.common.web.spring;

import org.infrastructure.common.exception.ControllerException;
import org.infrastructure.common.exception.DaoException;
import org.infrastructure.common.exception.ServiceException;
import org.infrastructure.common.web.exception.MoreThrowables;
import org.infrastructure.common.web.exception.MyResourceNotFoundException;
import org.infrastructure.common.web.exception.UnauthorizedException;
import org.infrastructure.common.web.exception.ValidationErrorDTO;
import org.infrastructure.result.ResultData;
import org.infrastructure.result.ResultDataConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Locale;

/**
 * 异常处理父类
 * 该类会处理所有在执行标有@RequestMapping注解的方法时发生的异常.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 消息处理(入参校验)
     * ResourceBundleMessageSource 注入失败
     */
    @Autowired
    private MessageSource messageSource;

    public RestResponseEntityExceptionHandler() {
        super();
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> defaultErrorHandler(final Exception ex,
                                                      final WebRequest request) {
        log.error("500 Status Code", ex);
        final ResultData<?> apiError = message(ex);
        return handleExceptionInternal(ex, apiError, new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<Object> handleUnauthorization(final UnauthorizedException ex,
                                                        final WebRequest request) {
        logger.error("401 Status Code", ex);

        ResultData<String> authObj = ResultData.createAuthFailResult();
        if (ex.getCodeEx() != null) {
            if (ex.getCodeEx() == ResultDataConstants.TOKEN_LOST) {
                authObj = ResultData.createTokenLoseResult();
            }
            if (ex.getCodeEx() == ResultDataConstants.TOKEN_KICK) {
                authObj = ResultData.createTokenKickResult();
            }
        }

        return handleExceptionInternal(ex, authObj, new HttpHeaders(), HttpStatus.OK, request);
    }

    @ExceptionHandler({ServiceException.class})
    public ResponseEntity<Object> handleServiceException(final ServiceException ex,
                                                         final WebRequest request) {
        logger.error("handleServiceException", ex);

        return handleExceptionInternal(ex, message(ex), new HttpHeaders(), HttpStatus.OK, request);
    }

    @ExceptionHandler({ControllerException.class})
    public ResponseEntity<Object> handleControllerException(final ControllerException ex,
                                                            final WebRequest request) {
        logger.error("handleControllerException", ex);

        return handleExceptionInternal(ex, message(ex), new HttpHeaders(), HttpStatus.OK, request);
    }

    @ExceptionHandler({DaoException.class})
    public ResponseEntity<Object> handleDaoException(final DaoException ex,
                                                     final WebRequest request) {
        logger.error("handleDaoException", ex);

        return handleExceptionInternal(ex, message(ex), new HttpHeaders(), HttpStatus.OK, request);
    }

    // 500
    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class,
            IllegalStateException.class})
    public ResponseEntity<Object> handle500s(final RuntimeException ex, final WebRequest request) {
        log.error("500 Status Code", ex);

        final ResultData<?> apiError = message(ex);

        return handleExceptionInternal(ex, apiError, new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    // @ExceptionHandler(value = MyException.class)
    // 400
    @Override
    protected final ResponseEntity<Object> handleHttpMessageNotReadable(
            final HttpMessageNotReadableException ex, final HttpHeaders headers,
            final HttpStatus status, final WebRequest request) {
        log.error("handleHttpMessageNotReadable Bad Request: {}", ex.getMessage());
        log.debug("handleHttpMessageNotReadable Bad Request: ", ex);

        final ResultData<?> apiError = message(ex);
        return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected final ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex, final HttpHeaders headers,
            final HttpStatus status, final WebRequest request) {
        log.error("handleMethodArgumentNotValid Bad Request: {}", ex.getMessage());
        log.debug("handleMethodArgumentNotValid Bad Request: ", ex);

        final BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        final ValidationErrorDTO dto = processFieldErrors(fieldErrors);

        final ResultData<?> apiError = messageValidationError(ex, dto);
        return handleExceptionInternal(ex, apiError, headers, HttpStatus.OK, request);
    }

    // 422

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public final ResponseEntity<Object> handleBadRequest(final ConstraintViolationException ex,
                                                         final WebRequest request) {
        log.error("422 Status Code", ex);
        log.debug("Violation exception: {}", ex.getLocalizedMessage());

        final ValidationErrorDTO dto = processFieldErrors(ex);
        final ResultData<?> apiError = messageValidationError(ex, dto);
        return handleExceptionInternal(ex, apiError, new HttpHeaders(),
                HttpStatus.OK, request);
    }


    // 403
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleEverything(final AccessDeniedException ex,
                                                   final WebRequest request) {
        log.error("403 Status Code", ex);

        final ResultData<?> apiError = message(ex);
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.FORBIDDEN,
                request);
    }

    // 404
    @ExceptionHandler({MyResourceNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(final RuntimeException ex,
                                                    final WebRequest request) {
        log.error("Not Found: {}", ex.getMessage());

        final ResultData<?> apiError = message(ex);
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.NOT_FOUND,
                request);
    }

    // 409

    // @ExceptionHandler({ InvalidDataAccessApiUsageException.class, DataAccessException.class,
    // MyConflictException.class })
    // protected ResponseEntity<Object> handleConflict(final RuntimeException ex, final WebRequest
    // request) {
    // log.warn("Conflict: {}", ex.getMessage());
    //
    // final ErrorMessage apiError = message(HttpStatus.CONFLICT, ex);
    // return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.CONFLICT,
    // request);
    // }

    // 415

    @ExceptionHandler({InvalidMimeTypeException.class, InvalidMediaTypeException.class})
    protected ResponseEntity<Object> handleInvalidMimeTypeException(
            final IllegalArgumentException ex, final WebRequest request) {
        log.error("Unsupported Media Type: {}", ex.getMessage());

        final ResultData<?> apiError = message(ex);
        return handleExceptionInternal(ex, apiError, new HttpHeaders(),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE, request);
    }

    // UTIL
    private ValidationErrorDTO processFieldErrors(final List<FieldError> fieldErrors) {
        final ValidationErrorDTO dto = new ValidationErrorDTO();

        for (final FieldError fieldError : fieldErrors) {
            final String localizedErrorMessage = fieldError.getDefaultMessage();
            dto.addFieldError(fieldError.getField(), localizedErrorMessage);
        }

        return dto;
    }

    @SuppressWarnings("rawtypes")
    private ValidationErrorDTO processFieldErrors(ConstraintViolationException e) {
        final ValidationErrorDTO dto = new ValidationErrorDTO();
        Locale locale = LocaleContextHolder.getLocale();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            dto.addFieldError(violation.getPropertyPath().toString(),
                    messageSource.getMessage(violation.getMessage(), null, locale));
        }

        return dto;
    }

    protected ResultData<?> messageValidationError(final Exception ex, final ValidationErrorDTO validationError) {
        final String message =
                ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
        final String devMessage = MoreThrowables.getRootCauseStackTraceAsString(ex);

        return ResultData.createValidationError(validationError, message, devMessage);
    }

    protected ResultData<?> message(final Exception ex) {
        final String message =
                ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
        final String devMessage = MoreThrowables.getRootCauseStackTraceAsString(ex);

        return ResultData.createErrorResult(null, message, devMessage);
    }

    protected ResultData<?> message(final RuntimeException ex) {
        final String message =
                ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
        final String devMessage = MoreThrowables.getRootCauseStackTraceAsString(ex);

        return ResultData.createErrorResult(null, message, devMessage);
    }
}

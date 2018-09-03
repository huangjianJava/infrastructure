package org.infrastructure.common.aop;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.infrastructure.common.constants.ShareConstants;
import org.infrastructure.common.utils.ServletUtil;
import org.infrastructure.result.ResultData;
import org.infrastructure.result.enums.TokenResultEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author code
 * @version V1.0
 * @Title: 基础代码
 * @Description: 描述
 **/

public abstract class RequestProcessAop {

    
    private static Logger logger = LoggerFactory.getLogger(RequestProcessAop.class);
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    private static final String PRE_TAG = "";

    protected abstract boolean getCheckToken();

    protected abstract boolean checkLoginAuth(String tokenStr);

    protected abstract boolean checkPopedom(RequestProcess pjp);

    /**
     * 校验数据权限
     *
     * @param popedomCode 权限
     * @return boolean
     */
    protected boolean checkDataPopedom(String popedomCode) {
        return true;
    }

    /**
     * 是否日志记录
     */
    protected boolean checkEnableLog() {
        return true;
    }
    
    protected TokenResultEnum checkLoginAuthEx(String tokenStr, RequestProcess pjp) {
        return TokenResultEnum.TOKEN_OK;
    }

    @Pointcut("@annotation(org.infrastructure.common.aop.RequestProcess)")
    public void webLog() {}


    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        logger.debug(PRE_TAG + "doBefore");
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        logger.debug(PRE_TAG + "doAfterReturning");
    }


    @Around("webLog()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        startTime.set(System.currentTimeMillis());

        logger.debug(PRE_TAG + "around");
        Object ret;
        Map<String, Object> logObj = null;
        try {
            if (checkEnableLog()) {
                logger.debug("---");
                logObj = new HashMap<>(16);
    
                ServletRequestAttributes attributes =
                        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = attributes.getRequest();
    
                logObj.put("HEADER", ServletUtil.getHeadersInfo(request));
                logObj.put("URL", request.getRequestURL().toString());
                logObj.put("HTTP_METHOD", request.getMethod());
                logObj.put("IP", ServletUtil.getRemoteIp());
                logObj.put("CLASS_METHOD",
                        pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName());
    
                Object[] args = pjp.getArgs();
                if (args != null) {
                    try {
                        ArrayList<Object> newArgs = new ArrayList<>();
                        for (Object arg : args) {
                            if ((arg == null) || (arg instanceof HttpServletRequest)
                                    || (arg instanceof HttpServletResponse)
                                    || (arg instanceof MultipartFile)) {
                                continue;
                            }
    
                            newArgs.add(arg);
                        }
                        logObj.put("ARGS", newArgs);
    
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        logger.error(ex.getMessage());
                    }
                }
            }
            
            Signature signature = pjp.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method targetMethod = methodSignature.getMethod();
            // Method realMethod = pjp.getTarget().getClass().getDeclaredMethod(signature.getName(),
            // targetMethod.getParameterTypes());
            RequestProcess obj = targetMethod.getAnnotation(RequestProcess.class);
            ApiOperation apiOperObj = targetMethod.getAnnotation(ApiOperation.class);
            RequestMapping requestMapObj = targetMethod.getAnnotation(RequestMapping.class);
    
            if (checkEnableLog() && (null != logObj)) {
                if (apiOperObj != null) {
                    logObj.put("LOGNAME", apiOperObj.value());
                }
                if (requestMapObj != null) {
                    logObj.put("LOGURL", StringUtils.join(requestMapObj.value(), "/"));
                }
            }
    
            if (obj != null) {
                if (getCheckToken() && obj.checkLogin()) {
                    ServletRequestAttributes attributes =
                            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    HttpServletRequest request = attributes.getRequest();
                    
                    String tokenStr = getTokenStr(request);
                    
                    //用于兼容1.0.30-SNAPSHOT前的版本
                    if (!checkLoginAuth(tokenStr)) {
                        return ResultData.createAuthFailResult();
                    }
                    
                    //1.0.30-SNAPSHOT加入
                    TokenResultEnum tokenResult = checkLoginAuthEx(tokenStr, obj);
                    if (null == tokenResult) {
                        return ResultData.createAuthFailResult();
                    } else {
                       switch(tokenResult) {
                            case TOKEN_OK:
                               break;
                            case TOKEN_FAIL:
                                return ResultData.createAuthFailResult();
                            case TOKEN_LOSE:
                                return ResultData.createTokenLoseResult();
                            case TOKEN_KICK:
                                return ResultData.createTokenKickResult();
                            default:
                                return ResultData.createAuthFailResult();
                        }
                    } 
    
                    if (!checkPopedom(obj)) {
                        String message =
                                apiOperObj == null
                                        ? pjp.getSignature().getDeclaringTypeName() + "."
                                                + pjp.getSignature().getName()
                                        : apiOperObj.value();
                        return ResultData.createPopedomFailResult(message);
                    }
    
                    // 校验数据权限
                    if (obj.checkDataPopedom()) {
                        if (!checkDataPopedom(obj.popedomCode())) {
                            String message =
                                    apiOperObj == null
                                            ? pjp.getSignature().getDeclaringTypeName() + "."
                                                    + pjp.getSignature().getName()
                                            : apiOperObj.value();
                            return ResultData.createDataPopedomFailResult(message);
                        }
                    }
                }
            } else {
                return ResultData.createAuthFailResult();
            }

            ret = pjp.proceed();
        } finally {
            long execTimes = System.currentTimeMillis() - startTime.get();
            if (checkEnableLog() && (null != logObj)) {
                logObj.put("EXECTIMES", execTimes);
                if (processLog(logObj) == false) {
                    logger.info(JSON.toJSONString(logObj)); 
                }
            }
        }
        return ret;
    }
    
    /**
     * 日志数据处理
     * @param logData
     */
    protected boolean processLog(Map<String, Object> logData) {
        return false;
    }

    private String getTokenStr(HttpServletRequest request) {
        String tokenStr = request.getHeader(ShareConstants.TOKEN_HEADER_NAME);
        if (tokenStr == null) {
            tokenStr = request.getParameter(ShareConstants.TOKEN_GET_NAME);
        }

        return tokenStr;
    }
}

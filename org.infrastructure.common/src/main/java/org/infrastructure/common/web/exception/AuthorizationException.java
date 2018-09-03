package org.infrastructure.common.web.exception;


/**
 * Exception thrown if there is a problem during authorization (access control check).
 *
 */
public class AuthorizationException extends RuntimeException {

  private static final long serialVersionUID = -446771989361482085L;

  /**
   * Creates a new AuthorizationException.
   */
  public AuthorizationException() {
      super();
  }

  /**
   * Constructs a new AuthorizationException.
   *
   * @param message the reason for the exception
   */
  public AuthorizationException(String message) {
      super(message);
  }

  /**
   * Constructs a new AuthorizationException.
   *
   * @param cause the underlying Throwable that caused this exception to be thrown.
   */
  public AuthorizationException(Throwable cause) {
      super(cause);
  }

  /**
   * Constructs a new AuthorizationException.
   *
   * @param message the reason for the exception
   * @param cause   the underlying Throwable that caused this exception to be thrown.
   */
  public AuthorizationException(String message, Throwable cause) {
      super(message, cause);
  }
}

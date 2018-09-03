package org.infrastructure.common.web.exception;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p></p>
 *********************************************************************************************/


/**
 * Thrown to indicate a requested operation or access to a requested resource is not allowed.
 *
 */
public class UnauthorizedException extends AuthorizationException {

  private static final long serialVersionUID = 6620000563156827089L;

  private Integer codeEx;

  /**
   * Creates a new UnauthorizedException.
   */
  public UnauthorizedException() {
      super();
  }

  /**
   * Constructs a new UnauthorizedException.
   *
   * @param message the reason for the exception
   */
  public UnauthorizedException(String message) {
      super(message);
  }

    /**
     * Constructs a new UnauthorizedException.
     *
     */
    public UnauthorizedException(Integer codeEx) {
        super();
        this.codeEx = codeEx;
    }

  /**
   * Constructs a new UnauthorizedException.
   *
   * @param cause the underlying Throwable that caused this exception to be thrown.
   */
  public UnauthorizedException(Throwable cause) {
      super(cause);
  }

  /**
   * Constructs a new UnauthorizedException.
   *
   * @param message the reason for the exception
   * @param cause   the underlying Throwable that caused this exception to be thrown.
   */
  public UnauthorizedException(String message, Throwable cause) {
      super(message, cause);
  }

    public Integer getCodeEx() {
        return codeEx;
    }

    public void setCodeEx(Integer codeEx) {
        this.codeEx = codeEx;
    }
}


package org.infrastructure.common.exception;

import java.io.Serializable;

/**
 * @author code
 * @version V1.0
 * @Title: 基础代码
 * @Description: 描述
 **/

public class ServiceException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;
    
    protected  int errorCode = -1;
    
    public ServiceException() {

    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    protected ServiceException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    public void setCode(int errorCode){
        this.errorCode =  errorCode;
    }
    public int getCode(){
        return this.errorCode;
    }
}

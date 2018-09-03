
package org.infrastructure.common.exception;

import java.io.Serializable;

/**
 * @author code
 * @version V1.0
 * @Title: 基础代码
 * @Description: 描述
 **/

public class ControllerException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;
    
    protected  int errorCode = -1;
    
    public ControllerException() {

    }

    public ControllerException(String message) {
        super(message);
    }

    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControllerException(Throwable cause) {
        super(cause);
    }

    protected ControllerException(String message, Throwable cause,
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


package org.infrastructure.common.exception;

import java.io.Serializable;

/**
 * @author code
 * @version V1.0
 * @Title: 基础代码
 * @Description: 描述
 **/

public class DaoException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;
    
    protected  int errorCode = -1;
    
    public DaoException() {

    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }

    protected DaoException(String message, Throwable cause,
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
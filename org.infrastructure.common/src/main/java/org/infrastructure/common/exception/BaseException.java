package org.infrastructure.common.exception;

import java.io.Serializable;

/**
 * @author code
 * @version V1.0
 * @Title: 基础代码
 * @Description: 描述
 **/

public class BaseException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected  int errorCode = -1;
	
	public BaseException() {

	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseException(Throwable cause) {
		super(cause);
	}

	protected BaseException(String message, Throwable cause,
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

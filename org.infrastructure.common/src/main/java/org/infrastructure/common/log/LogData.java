package org.infrastructure.common.log;

import org.infrastructure.result.ResultData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogData implements Serializable {
	private static final long serialVersionUID = 3335849916351979411L;

	private String serviceName;
	
	@SuppressWarnings("unused")
    private Date date;
	
	private List<Object> data = new ArrayList<Object>();
	
	private Boolean isException =false;
	
	private String exceptionMsg;
	
	private StackTraceElement[] exceptionStackTrace;
	
	private Long executeTime;
	
	private String userId;
	
	private String userName;
	
	private String url;
	
	@SuppressWarnings("rawtypes")
    private ResultData resultData;
	
	
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Date getDate() {
		return new Date();
	}

	public List<Object> getData() {
		return data;
	}

	public void setData(Object data) {
		this.data.add(data);
	}

	public Boolean getIsException() {
		return isException;
	}

	public void setIsException(Boolean isException) {
		this.isException = isException;
	}

	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

	public StackTraceElement[] getExceptionStackTrace() {
		return exceptionStackTrace;
	}

	public void setExceptionStackTrace(StackTraceElement[] exceptionStackTrace) {
		this.exceptionStackTrace = exceptionStackTrace;
	}

	public Long getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Long executeTime) {
		this.executeTime = executeTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    @SuppressWarnings("rawtypes")
    public ResultData getResultData() {
        return resultData;
    }

    public void setResultData(@SuppressWarnings("rawtypes") ResultData resultData) {
        this.resultData = resultData;
    }
}

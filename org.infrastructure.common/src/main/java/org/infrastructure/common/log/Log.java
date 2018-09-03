package org.infrastructure.common.log;

import java.util.Map;

import org.infrastructure.common.aop.RequestProcess;
import org.infrastructure.common.utils.DateUtil;
import org.infrastructure.result.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Log {
	private static Logger logExec = LoggerFactory.getLogger("largeExecLog");
	private static Logger logError = LoggerFactory.getLogger("errorLog");
	
	public static void writeLargeExecInf(String str) {
		logExec.info(str);
	}
	
	public static void writeErrorInf(String str) {
		logError.error(str);
	}
	
	public static void writeErrorInf(String str, Throwable paramThrowable) {
		logError.error(str, paramThrowable);
	}
	
	public static boolean enableLogLargeExec() {
		return logExec.isInfoEnabled();
	}
	
	public static boolean enableLogError() {
		return logError.isErrorEnabled();
	}
	
	public static void writeStringBuilderByAry(StringBuilder strObj, Object[] args, String preStr, String sufStr) {
		if ((strObj != null) && (args != null)) {
			for(int i = 0; i< args.length; i++) {
				if (args[i] != null) {
					if ((args[i] instanceof Map)) {
					    
						strObj.append(preStr + JSON.toJSONString(args[i]) + sufStr);
			        } else if ((args[i] instanceof String)) {
			        	strObj.append(preStr + (String)args[i] + sufStr);
			        } else if (args[i].getClass().equals(Object.class)){
			        	strObj.append(preStr + args[i].getClass().toString() + sufStr);
			    	}
				}
			}
		}
	}
	
	public static void sysLog(Object[] args, RequestProcess pjp,
            Throwable e, Long sExecTime, @SuppressWarnings("rawtypes") ResultData returnResult) {
        try {
            LogData logData = new LogData();
            logData.setIsException(false);
            logData.setServiceName(pjp.logName());
            logData.setUrl(pjp.logName());
            
            Object obj[] = args;
            for (Object o : obj) {
                logData.setData(o);
            }
            if (e != null) {
                logData.setIsException(true);
                logData.setExceptionMsg(e.getMessage());
                logData.setExceptionStackTrace(e.getStackTrace());
                //e.printStackTrace();
            }
            
            if(returnResult!=null){
                logData.setResultData(returnResult);
            }
            
            logData.setExecuteTime(sExecTime);
              
            @SuppressWarnings("unused")
            String json = JSON.toJSONString(logData, SerializerFeature.WriteDateUseDateFormat);
            
           // MongoCollection<Document> mongoCollection=  MongoManager.getMongoDatabase().getCollection("syslogs");
           // Document document = Document.parse(json);
           // document.put("date", new Date());
            
           // mongoCollection.insertOne(document);
        } catch ( Exception ex) {
            try {
                if (Log.enableLogError()) {
                    StringBuilder errStr = new StringBuilder();
                    errStr.append("sysLog: " + DateUtil.getCurrentDateStr(DateUtil.TIME_PATTERN_DEFAULT));
                    Log.writeStringBuilderByAry(errStr, args, "", " \n");
                    Log.writeErrorInf(errStr.toString());
                }
            } catch(Exception eee) {  
            }
            
            Log.writeErrorInf("", ex);
        } finally {

        } 
	}
}

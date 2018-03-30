package wsn.b405.util;

import wsn.b405.bean.LoggerVO;

import java.util.Enumeration;
import java.util.Map;

/**
 * Created by @author cdlixu5 on 2018/1/11.
 */
public class Log4jFrameWrokImpl extends LogFrameWrok {

    private final static String logType ="org.slf4j.impl.SimpleLoggerFactory";

    @Override
    protected boolean isThisType(String type) {
        return logType.equals(type);
    }

    @Override
    public void changeLogLevel(String logType,Object object, String level) {
        if(!isThisType(logType)){
            if(null == getNextHandler()){
                return;
            }
            getNextHandler().changeLogLevel(logType,object,level);
        }

        org.apache.log4j.Logger targetLogger = (org.apache.log4j.Logger) object;
        org.apache.log4j.Level targetLevel = org.apache.log4j.Level.toLevel(level);
        targetLogger.setLevel(targetLevel);
    }

    @Override
    public void getAllLog(String type,String loggerPrefix,Map<String, Object> loggerMap) {
        if(!isThisType(type)){
            if(getNextHandler() == null){
                return; //处理不了，不知别的日志框架
            }
            getNextHandler().getAllLog(type,loggerPrefix,loggerMap);
        }

        Enumeration enumeration = org.apache.log4j.LogManager.getCurrentLoggers();
        while (enumeration.hasMoreElements()) {
            org.apache.log4j.Logger logger = (org.apache.log4j.Logger) enumeration.nextElement();
            if (match(loggerPrefix,logger.getName())) {
                loggerMap.put(logger.getName(), new LoggerVO(logger.getName(), logger.getEffectiveLevel().toString().toString(), logger));
            }
        }
        org.apache.log4j.Logger rootLogger = org.apache.log4j.LogManager.getRootLogger();
        loggerMap.put(rootLogger.getName(), new LoggerVO(rootLogger.getName(), rootLogger.getEffectiveLevel().toString(), rootLogger));

    }
}

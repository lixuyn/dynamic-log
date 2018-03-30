package wsn.b405.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wsn.b405.bean.LoggerVO;

import java.util.Map;

/**
 * Created by @author cdlixu5 on 2018/1/11.
 */
public class LogbackFrameWrokImpl extends LogFrameWrok {

    private final static String logType ="ch.qos.logback.classic.util.ContextSelectorStaticBinder";

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

        ch.qos.logback.classic.Logger targetLogger = (ch.qos.logback.classic.Logger) object;
        ch.qos.logback.classic.Level targetLevel = ch.qos.logback.classic.Level.toLevel(level);
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

        ch.qos.logback.classic.LoggerContext loggerContext = (ch.qos.logback.classic.LoggerContext) LoggerFactory.getILoggerFactory();
        for (ch.qos.logback.classic.Logger logger : loggerContext.getLoggerList()) {
            if (match(loggerPrefix,logger.getName())) {
                loggerMap.put(logger.getName(), new LoggerVO(logger.getName(), logger.getEffectiveLevel().toString(), logger));
            }
        }
        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        loggerMap.put(rootLogger.getName(), new LoggerVO(rootLogger.getName(), rootLogger.getEffectiveLevel().toString(), rootLogger));
    }
}

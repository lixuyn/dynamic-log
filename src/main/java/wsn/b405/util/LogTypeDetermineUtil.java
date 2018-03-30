package wsn.b405.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by @author cdlixu5 on 2017/12/10.
 */
public class LogTypeDetermineUtil {

    private Logger logger = LoggerFactory.getLogger(LogTypeDetermineUtil.class);

    private static String loggerPrefix = "";

    private String logFrameworkType;

    private static volatile LogTypeDetermineUtil util = new LogTypeDetermineUtil();
    private Map<String, Object> loggerMap = new ConcurrentHashMap<String, Object>(16);

    Log4j2FrameWrok log4j2FrameWrok = new Log4j2FrameWrok();
    Log4jFrameWrokImpl log4jFrameWrok = new Log4jFrameWrokImpl();
    LogbackFrameWrokImpl logbackFrameWrok  = new LogbackFrameWrokImpl();
    SimpleLogFrameWrokImpl simpleLogFrameWrok = new SimpleLogFrameWrokImpl();

    public static LogTypeDetermineUtil getInstance() {
        return util;
    }

    public static void setLoggerPrefix(String loggerPrefix) {
        LogTypeDetermineUtil.loggerPrefix = loggerPrefix;
    }

    private LogTypeDetermineUtil() {    }

    private void logType() {
        String type = StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr();
        logFrameworkType = type;

        logger.info("\n\n\n logType:{}" + type);

        //责任连模式
        log4jFrameWrok.setNextHandler(logbackFrameWrok);
        log4j2FrameWrok.setNextHandler(log4jFrameWrok);
        logbackFrameWrok.setNextHandler(simpleLogFrameWrok);

        //获取所有的日志对象
        log4j2FrameWrok.getAllLog(type,loggerPrefix,loggerMap);
    }

    public Map<String, Object> getLoggerMap() {
        if (loggerMap.isEmpty()) {
            logType();
        }
        return loggerMap;
    }

    public String getLogFrameworkType() {
        return logFrameworkType;
    }

    public void changeLogLevel(Object object,String level){
        log4j2FrameWrok.changeLogLevel(logFrameworkType,object,level);
    }

}

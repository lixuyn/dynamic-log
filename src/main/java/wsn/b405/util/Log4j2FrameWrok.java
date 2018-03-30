package wsn.b405.util;

import org.apache.commons.lang3.StringUtils;
import wsn.b405.bean.LoggerVO;

import java.util.Map;

/**
 * Created by @author cdlixu5 on 2018/1/11.
 */
public class Log4j2FrameWrok extends LogFrameWrok {

    private final static  String logType ="org.apache.logging.slf4j.Log4jLoggerFactory";

    @Override
    protected boolean isThisType(String type) {
        return  logType.equals(type);
    }

    @Override
    public void changeLogLevel(String logType,Object object, String level) {
        if(!isThisType(logType)){
            if(null == getNextHandler()){
                return;
            }
            getNextHandler().changeLogLevel(logType,object,level);
        }

        org.apache.logging.log4j.core.config.LoggerConfig loggerConfig = (org.apache.logging.log4j.core.config.LoggerConfig) object;
        org.apache.logging.log4j.Level targetLevel = org.apache.logging.log4j.Level.toLevel(level);
        loggerConfig.setLevel(targetLevel);
        org.apache.logging.log4j.core.LoggerContext ctx = (org.apache.logging.log4j.core.LoggerContext) org.apache.logging.log4j.LogManager.getContext(true);
        ctx.updateLoggers();
    }

    @Override
    public void getAllLog(String type,String loggerPrefix,Map<String, Object> loggerMap) {
        if(!isThisType(type)){
            if(getNextHandler() == null){
                return; //处理不了，不知别的日志框架
            }
            getNextHandler().getAllLog(type,loggerPrefix,loggerMap);
        }
        org.apache.logging.log4j.core.LoggerContext loggerContext = (org.apache.logging.log4j.core.LoggerContext) org.apache.logging.log4j.LogManager.getContext(true);
        Map<String, org.apache.logging.log4j.core.config.LoggerConfig> map = loggerContext.getConfiguration().getLoggers();
        for (org.apache.logging.log4j.core.config.LoggerConfig loggerConfig : map.values()) {
            String key = loggerConfig.getName();
            if (StringUtils.isBlank(key)) {
                key = "root";
            }
            //序列化有问题,因为config文件不序列化
            loggerMap.put(key, new LoggerVO(loggerConfig.getName(), loggerConfig.getLevel().toString(), loggerConfig));
        }
    }
}

package wsn.b405.util;

import org.slf4j.Logger;
import org.slf4j.impl.SimpleLoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;
import wsn.b405.bean.LoggerVO;
import wsn.b405.constant.LogLevelEnum;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by @author cdlixu5 on 2018/1/11.
 */
public class SimpleLogFrameWrokImpl extends LogFrameWrok {
    private final static  String logType = "org.slf4j.impl.SimpleLoggerFactory";
    @Override
    protected boolean isThisType(String type) {
        return logType.equals(type);
    }

    @Override
    public void changeLogLevel(String logType, Object obj, String level) {
        if(!isThisType(logType)){
            if(null == getNextHandler()){
                return;
            }
            getNextHandler().changeLogLevel(logType,obj,level);
        }

        if(null == obj){
            return;
        }
        Class<?> clazz  = obj.getClass();
        try {
            Field field = clazz.getDeclaredField("currentLogLevel");
            field.setAccessible(true);
            int lev = LogLevelEnum.getLevelValueByLevelString(level);
            field.setInt(obj,lev);
            field.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllLog(String type,String loggerPrefix, Map<String, Object> loggerMap) {
        if(!isThisType(type)){
            if(getNextHandler() == null){
                return; //处理不了，不知别的日志框架
            }
            getNextHandler().getAllLog(type,loggerPrefix,loggerMap);
        }

        StaticLoggerBinder loggerBinder = StaticLoggerBinder.getSingleton();
        SimpleLoggerFactory factory = (SimpleLoggerFactory) loggerBinder.getLoggerFactory();
        Class<?> clazz = factory.getClass();
        Field field = null;
        try {
            field = clazz.getDeclaredField("loggerMap");
            field.setAccessible(true);
            Map<String, Object> ms = (Map<String, Object>) field.get(factory);

            ms.entrySet().stream().filter(E -> match(loggerPrefix,E.getKey())).forEach(
                    E -> {
                        String name = E.getKey();
                        Logger log = (Logger) E.getValue();
                        loggerMap.put(name, new LoggerVO(name, level(log), log));
                    }
            );

            org.apache.log4j.Logger rootLogger = org.apache.log4j.LogManager.getRootLogger();
            loggerMap.put(rootLogger.getName(), new LoggerVO(rootLogger.getName(), rootLogger.getEffectiveLevel().toString(), rootLogger));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String level(Logger logger){
        Class<?> clazz  = logger.getClass();
        try {
            Field field = clazz.getDeclaredField("currentLogLevel");
            field.setAccessible(true);
            int lev = (int) field.get(logger);
            return LogLevelEnum.getLevelStringByLevelValue(lev);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return  "DEFAULT";
    }
}

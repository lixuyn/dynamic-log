package wsn.b405.util;

import java.util.Map;

/**
 * Created by @author cdlixu5 on 2018/1/11.
 */
public abstract class LogFrameWrok {

    private  LogFrameWrok nextHandler;

    public LogFrameWrok getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(LogFrameWrok nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * 判断日志框架的类型
     * @param type
     * @return
     */
   protected abstract boolean isThisType(String type);

    /**
     * 修改指定日志对象的，日志级别
     * @param type
     * @param obj
     * @param level
     */
    abstract void changeLogLevel(String type,Object obj,String level);

    /**
     * 获取工程下所有的日志对象
     * @param loggerMap
     * @param loggerPrefix
     */
    abstract void getAllLog(String type,String loggerPrefix,Map<String,Object> loggerMap);

    /**
     * log对象过滤规则
     * @param loggerPrefix
     * @return
     */
    public boolean match(String loggerPrefix,String loggerName){
        if(null == loggerName) {
            return Boolean.FALSE;
        }
        if(null == loggerPrefix || "".equals(loggerPrefix)) {
            return Boolean.TRUE;
        }else{
            return loggerName.contains(loggerPrefix);
        }
    }
}

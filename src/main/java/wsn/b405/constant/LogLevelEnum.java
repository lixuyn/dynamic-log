package wsn.b405.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by @author cdlixu5 on 2017/12/15.
 */
public enum LogLevelEnum {
    TRACE(0, "TRACE"),
    DEBUG(10, "DEBUG"),
    INFO(20, "INFO"),
    WARN(30, "WARN"),
    ERROR(40, "ERROR");

    private LogLevelEnum(int levelValue, String levelString) {
        this.levelName = levelString;
        this.levelValue = levelValue;
    }

    /**
     * SimpleLogger 级别和值之间的转换关系
     * @param value
     * @return
     */
    public static  String getLevelStringByLevelValue(int value){
        LogLevelEnum logLevelEnum[] = LogLevelEnum.values();
        for (LogLevelEnum i: logLevelEnum  ) {
            if(i.getLevelValue() == value){
                return i.getLevelName();
            }
        }
        return  "UNKNOWN";
    }

    public static int getLevelValueByLevelString(String levelName){
        LogLevelEnum logLevelEnum[] = LogLevelEnum.values();
        for (LogLevelEnum i: logLevelEnum  ) {
            if(StringUtils.isNotEmpty(levelName) && i.getLevelName().equals(levelName)){
                return i.getLevelValue();
            }
        }
        return  INFO.getLevelValue();
    }

    private int levelValue;
    private String levelName;

    public int getLevelValue() {
        return levelValue;
    }

    public void setLevelValue(int levelValue) {
        this.levelValue = levelValue;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}

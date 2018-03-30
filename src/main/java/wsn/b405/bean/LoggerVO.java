package wsn.b405.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by @author cdlixu5 on 2017/12/13.
 */
public class LoggerVO {
    private String name;
    private String level;

    @JSONField(serialize=false)
    private  transient  Object logerRef;

    public Object getLogerRef() {
        return logerRef;
    }

    public void setLogerRef(Object logerRef) {
        this.logerRef = logerRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "LoggerVO{" +
                "name='" + name + '\'' +
                ", level='" + level + '\'' +
                '}';
    }

    public LoggerVO(String name, String level, Object logerRef) {
        this.name = name;
        this.level = level;
        this.logerRef = logerRef;
    }
}

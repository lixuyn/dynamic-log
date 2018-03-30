package wsn.b405.constant;

/**
 * Created by @author cdlixu5 on 2017/12/10.
 */
public class LogFrameworkType {
    public final static  String LOG4J_LOGGER_FACTORY= "org.slf4j.impl.Log4jLoggerFactory";
    public final static  String LOGBACK_LOGGER_FACTORY ="ch.qos.logback.classic.util.ContextSelectorStaticBinder";
    public final static  String LOG4J2_LOGGER_FACTORY ="org.apache.logging.slf4j.Log4jLoggerFactory";
    public final  static  String LOG4J_FACTORY = "org.slf4j.impl.SimpleLoggerFactory";
    public static final String UNKNOWN = "unknown log type" ;
}

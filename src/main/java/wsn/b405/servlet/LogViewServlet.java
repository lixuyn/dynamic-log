package wsn.b405.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wsn.b405.service.LogStatService;
import wsn.b405.util.LogTypeDetermineUtil;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by @author cdlixu5 on 2017/12/10.
 */
public class LogViewServlet extends ResourceServlet {
    private Logger logger = LoggerFactory.getLogger(LogViewServlet.class);

    private String jmxUrl = null;
    private String jmxUsername = null;
    private String jmxPassword = null;
    private MBeanServerConnection conn = null;

    private LogStatService statService = LogStatService.getInstance();

    public LogViewServlet(){
        super("/support");
    }

    @Override
    public void init() throws ServletException {
        super.init();
        LogTypeDetermineUtil.setLoggerPrefix(this.getLoggerProfix);
    }

    @Override
    protected String process(String url) {
        String resp = null;
        if(this.jmxUrl == null) {
            resp = this.statService.service(url);
        } else if(this.conn == null) {
            try {
                this.initJmxConn();
            } catch (IOException var6) {
                logger.error("init jmx connection error", var6);
                resp = LogStatService.returnJSONResult(-1, "init jmx connection error" + var6.getMessage(),null);
            }

            if(this.conn != null) {
                try {
                    resp = this.getJmxResult(this.conn, url);
                } catch (Exception var5) {
                    logger.error("get jmx data error", var5);
                    resp =  LogStatService.returnJSONResult(-1, "get data error:" + var5.getMessage(),null);
                }
            }
        } else {
            try {
                resp = this.getJmxResult(this.conn, url);
            } catch (Exception var4) {
                logger.error("get jmx data error", var4);
                resp =  LogStatService.returnJSONResult(-1, "get data error" + var4.getMessage(),null);
            }
        }

        return resp;
    }

    private void initJmxConn() throws IOException {
        if(this.jmxUrl != null) {
            JMXServiceURL url = new JMXServiceURL(this.jmxUrl);
            HashMap env = null;
            if(this.jmxUsername != null) {
                env = new HashMap();
                String[] jmxc = new String[]{this.jmxUsername, this.jmxPassword};
                env.put("jmx.remote.credentials", jmxc);
            }

            JMXConnector jmxc1 = JMXConnectorFactory.connect(url, env);
            this.conn = jmxc1.getMBeanServerConnection();
        }

    }

    private String getJmxResult(MBeanServerConnection connetion, String url) throws Exception {
        ObjectName name = new ObjectName("wsn.b405.logType:type=LogStatService");
        String result = (String)this.conn.invoke(name, "service", new String[]{url}, new String[]{String.class.getName()});
        return result;
    }
}

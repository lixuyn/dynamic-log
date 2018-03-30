package wsn.b405.servlet;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by @author cdlixu5 on 2017/12/10.
 */
public class MonitorServlet extends ResourceServlet {
    private String resLocation = "/support";
    private Set<String> mapping = new HashSet<String>();

    public  MonitorServlet(){
        super("/support/");
        this.mapping.add("/css/bootstrap.min.css");
        this.mapping.add("/js/bootstrap.min.js");
        this.mapping.add("/js/jquery.min.js");
    }

    @Override
    protected String getFilePath(String fileName) {
        return this.mapping.contains(fileName) ? this.resLocation + fileName : super.getFilePath(fileName);
    }

    @Override
    protected String process(String var1) {
        return null;
    }
}

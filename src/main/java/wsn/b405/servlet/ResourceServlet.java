package wsn.b405.servlet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wsn.b405.bean.IPRange;
import wsn.b405.util.FileUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by @author cdlixu5 on 2017/12/10.
 */
public abstract class ResourceServlet extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(ResourceServlet.class);

    public static final String SESSION_USER_KEY = "dynamicLog-user";
    public static final String PARAM_NAME_USERNAME = "loginUsername";
    public static final String PARAM_NAME_PASSWORD = "loginPassword";
    public static  final  String LOGGER_PROFIX = "loggerProfix";
    //split by ,
    public static final String PARAM_NAME_ALLOW_LIST = "allow";
    public static final String PARAM_NAME_DENY_LIST = "deny";

    protected String username = null;
    protected String getLoggerProfix= null;
    protected String password = null;
    protected final String resourcePath;
    private List<IPRange> allow = new ArrayList<IPRange>();
    private List<IPRange> deny = new ArrayList<IPRange>();

    protected ResourceServlet(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public void init() throws ServletException {
        this.initAuth();
    }

    private void initAuth(){
        String logginName = this.getInitParameter(PARAM_NAME_USERNAME);
        if(StringUtils.isNotBlank(logginName)){
            this.username =  logginName;
        }

        String pass = this.getInitParameter(PARAM_NAME_PASSWORD);
        if(StringUtils.isNotBlank(pass)){
           this.password = pass;
        }

        String loggerFix = this.getInitParameter(LOGGER_PROFIX);
        if(StringUtils.isNotBlank(loggerFix)){
            this.getLoggerProfix = loggerFix;
        }

        try {
            String e = this.getInitParameter(PARAM_NAME_ALLOW_LIST);
            if(e != null && e.trim().length() != 0) {
                e = e.trim();
                String[] var13 = e.split(",");
                String[] var6 = var13;
                int var7 = var13.length;
                int var8 = 0;
                for(var8 = 0; var8 < var7; ++var8) {
                    String item = var6[var8];
                    if(item != null && item.length() != 0) {
                        IPRange ipRange = new IPRange(item);
                        this.allow.add(ipRange);
                    }
                }
            }
        } catch (Exception var12) {
            String msg = "initParameter config error, allow : " + this.getInitParameter(PARAM_NAME_ALLOW_LIST);
            logger.error(msg, var12);
        }

        try {
            String e = this.getInitParameter(PARAM_NAME_DENY_LIST);
            if(e != null && e.trim().length() != 0) {
                e = e.trim();
                String[] var13 = e.split(",");
                String[] var6 = var13;
                int var7 = var13.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String item = var6[var8];
                    if(item != null && item.length() != 0) {
                        IPRange ipRange = new IPRange(item);
                        this.deny.add(ipRange);
                    }
                }
            }
        } catch (Exception var11) {
            String msg = "initParameter config error, deny : " + this.getInitParameter(PARAM_NAME_DENY_LIST);
            logger.error(msg, var11);
        }
    }

    protected String getFilePath(String fileName) {
        return this.resourcePath + fileName;
    }

    protected void returnResourceFile(String fileName, String uri, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        String filePath = this.getFilePath(fileName);
       logger.debug("\n\n\n"+ filePath);
        if(filePath.endsWith(".html")) {
            response.setContentType("text/html; charset=utf-8");
        }

        if(fileName.endsWith(".jpg")) {
            byte[] text1 = FileUtil.readByteArrayFromResource(filePath);
            if(text1 != null) {
                response.getOutputStream().write(text1);
            }

        } else {
            String text = FileUtil.readFromResource(filePath);
            if(text == null) {
                response.sendRedirect(uri + "/login.html");
            } else {
                if(fileName.endsWith(".css")) {
                    response.setContentType("text/css;charset=utf-8");
                } else if(fileName.endsWith(".js")) {
                    response.setContentType("text/javascript;charset=utf-8");
                }

                response.getWriter().write(text);
            }
        }
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String requestURI = request.getRequestURI();
        response.setCharacterEncoding("utf-8");
        if(contextPath == null) {
            contextPath = "";
        }

        String uri = contextPath + servletPath;
        String path = requestURI.substring(contextPath.length() + servletPath.length());
        if(!this.isPermittedRequest(request)) {
            path = "/nopermit.html";
            this.returnResourceFile(path, uri, response);
        } else {
            String fullUrl;
            if("/submitLogin".equals(path)) {
                fullUrl = request.getParameter(PARAM_NAME_USERNAME);
                String passwordParam = request.getParameter(PARAM_NAME_PASSWORD);
                if(this.username.equals(fullUrl) && this.password.equals(passwordParam)) {
                    request.getSession().setAttribute(SESSION_USER_KEY, this.username);
                    response.getWriter().print("success");
                } else {
                    response.getWriter().print("error");
                }

            } else if(this.isRequireAuth() && !this.ContainsUser(request) && !this.checkLoginParam(request) && !"/login.html".equals(path) && !path.startsWith("/css") && !path.startsWith("/js") && !path.startsWith("/img")) {
                if(!contextPath.equals("") && !contextPath.equals("/")) {
                    if("".equals(path)) {
                        response.sendRedirect("log/login.html");
                    } else {
                        response.sendRedirect("login.html");
                    }
                } else {
                    response.sendRedirect("/log/login.html");
                }

            } else if(!"".equals(path)) {
                if("/".equals(path)) {
                    response.sendRedirect("login.html");
                } else if(path.contains(".json")) {
                    fullUrl = path;
                    if(request.getQueryString() != null && request.getQueryString().length() > 0) {
                        fullUrl = path + "?" + request.getQueryString();
                    }

                    response.getWriter().print(this.process(fullUrl));
                } else {
                    this.returnResourceFile(path, uri, response);
                }
            } else {
                if(!contextPath.equals("") && !contextPath.equals("/")) {
                    response.sendRedirect("log/login.html");
                } else {
                    response.sendRedirect("/log/login.html");
                }

            }
        }
    }

    public boolean ContainsUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(SESSION_USER_KEY) != null;
    }

    public boolean checkLoginParam(HttpServletRequest request) {
        String usernameParam = request.getParameter(PARAM_NAME_USERNAME);
        String passwordParam = request.getParameter(PARAM_NAME_PASSWORD);
        return null != this.username && null != this.password?this.username.equals(usernameParam) && this.password.equals(passwordParam):false;
    }

    public boolean isRequireAuth() {
        return this.username != null;
    }

    public boolean isPermittedRequest(HttpServletRequest request) {
        return  true;
    }

    protected abstract String process(String var1);


}

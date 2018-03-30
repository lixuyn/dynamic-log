package wsn.b405.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wsn.b405.bean.LogStatManagerBean;
import wsn.b405.bean.LoggerVO;
import wsn.b405.bean.PageBean;
import wsn.b405.util.BasicSystemInfoUtil;
import wsn.b405.util.LogTypeDetermineUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by @author cdlixu5 on 2017/12/11.
 */
public class LogStatService implements LogStatManagerBean {
    private static final Logger logger = LoggerFactory.getLogger(LogStatService.class);
    private  static  volatile LogStatService logStatService = new LogStatService();

    private static LogTypeDetermineUtil util = LogTypeDetermineUtil.getInstance();

    @Override
    public String service(String url) {
        Map parameters = getParameters(url);
        //所有的Logger对象，分页[搜索分页]
        if("/basicInfo.json".equals(url)){
            return returnJSONResult(1,BasicSystemInfoUtil.getBasicSystemInfo(),null);
        }
        if(url.startsWith("/logList.json")){
            return searchLogList(parameters);
        }
        //改变Logger的级别
        else if(url.startsWith("/changeLogLevel.json")){
            return returnJSONResult(1,this.changeLogLevel(parameters),null);
        }
        return null;
    }

    /**
     * 改变某一个日志的级别
     * @param parameters
     * @return
     */
    private Object changeLogLevel(Map parameters) {
        String logFrameworkType = util.getLogFrameworkType();
        if (null != parameters){
            String level = (String) parameters.get("level");
            String logClass = (String) parameters.get("logClass");
            Map<String,Object> map = getAllLogInstance();
            LoggerVO logger = ((LoggerVO)map.get(logClass));
            Object object = logger.getLogerRef();
            if (logger == null) {
                throw new RuntimeException("需要修改日志级别的Logger不存在");
            }
            //改变日志VO的级别【前端展示】
            logger.setLevel(level);
            //改变日志对象的级别【后台生效】
            util.changeLogLevel(object,level);
        }
        return "SUCCESS";
    }



    /**
     * 解析请求参数
     * @param url
     * @return
     */
    private Map getParameters(String url) {
        if(StringUtils.isNotEmpty(url) && url.contains("?")){
            Map<String,String> param = new HashMap<>();
            String paramStr = url.substring(url.indexOf("?") + 1 );
            if(StringUtils.isNotEmpty(paramStr)){
                String[] parm = paramStr.split("&");
                for(String str : parm){
                    String  arg[] = str.split("=");
                    if(arg.length ==2 ){//去除不合法的参数
                        param.put(arg[0],arg[1]);
                    }
                }
            }
            return param;
        }
        return null;
    }

    private String searchLogList(Map<String,String> parameters) {
        Map<String ,Object> loggerMap = util.getLoggerMap();
        PageBean pageBean = null;
        if(null != parameters && parameters.size() > 0){
            //search param ,filter
            if(parameters.containsKey("keyWord")){
                Map<String,Object> res = new HashMap<String, Object>();
                loggerMap.forEach((K,V) ->{
                    if(K.contains(parameters.get("keyWord").trim())){
                        res.put(K,V);
                    }
                });
                loggerMap = res;
            }

            // page param ,pageSize = 15;
            int pageSize = 15;
            if(parameters.containsKey("pageSize")){
                try{
                    pageSize = Integer.valueOf(parameters.get("pageSize"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            pageBean = new PageBean(loggerMap,pageSize);
            if(parameters.containsKey("pageNow")){
                int pageNow = 1;
                try{
                    pageNow = Integer.valueOf(parameters.get("pageNow"));
                }catch (Exception e){
                    e.printStackTrace();
                }
                loggerMap = pageBean.getPageDate(pageNow);
            }
        }

        String str = returnJSONResult(1,loggerMap,pageBean);
        return  str;
    }

    public static String returnJSONResult(int resultCode, Object content,PageBean pageBean) {
        HashMap dataMap = new HashMap();
        dataMap.put("ResultCode", Integer.valueOf(resultCode));
        dataMap.put("Content", content);
        if(null != pageBean ){
            dataMap.put("pageBean",pageBean);
        }
        //解决序列化出错问题，忽略配置文件
        return JSON.toJSONString(dataMap, new PropertyFilter() {
            @Override
            public boolean apply(Object o, String s, Object o1) {
                if("configuration".equals(s)){
                    return false;
                }
                return true;
            }
        });
    }

    private Map<String,Object> getAllLogInstance(){
        return  util.getLoggerMap();
    }

    public static LogStatService getInstance() {
        return logStatService;
    }
}

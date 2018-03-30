package wsn.b405.console;

import wsn.b405.service.LogStatService;
import wsn.b405.util.BasicSystemInfoUtil;
import wsn.b405.util.JsonStringPrintFormatUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Map;

/**
 * Created by @author cdlixu5 on 2017/12/30.
 */
public class Main {
    private static LogStatService logStatService = LogStatService.getInstance();

    public static void main(String[] args) {
        help(System.out);
        while(true){
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                String argsLine = reader.readLine();
                resolveArgs(argsLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static  void resolveArgs(String argsLine){
        if(null != argsLine) {
            switch (argsLine.trim().toLowerCase()) {
                case "exit":
                    System.exit(0);
                    break;
                case "help":
                    help(System.out);
                    break;
                case "dy-log list all":
                    handlerCommand("dy-log list all");
                    break;
                case "sys-info":
                    handlerCommand("sys-info");
                    break;
                default:
                    String[] arg = argsLine.split(" ");

                    if (argsLine.startsWith("dy-log search")) {
                        if (3 == arg.length && null != arg[2]) {
                            handlerCommand("dy-log search", arg[2]);
                        }else {
                            printHitMsg(System.out,"命令输入参数有误，请按照命令格式输入dy-log search LogName,命令详细信息请用help查看");
                        }
                    }

                    else if(argsLine.startsWith("dy-log page")){
                        String pageNow = "1";
                        String pageSize = "15";
                        if(3 <= arg.length){
                            pageNow = arg[2];
                        }if (4 == arg.length){
                            pageSize = arg[3];
                        }
                        handlerCommand("dy-log page",pageNow,pageSize);
                    }

                    else if (argsLine.startsWith("dy-log change all")) {
                        if(4 == arg.length && null != arg[3]){
                            handlerCommand("dy-log change all",arg[3]);
                        }else {
                            printHitMsg(System.out,"命令输入参数有误，请按照命令格式输入dy-log change all level,命令详细信息请用help查看");
                        }
                    }

                    else if (argsLine.startsWith("dy-log change")) {
                        if(4 == arg.length && null != arg[2]  && null != arg[3]) {
                            handlerCommand("dy-log change", arg[2], arg[3]);
                        }
                        else{
                            printHitMsg(System.out,"命令输入参数有误，请按照命令格式输入dy-log chanage logName level,命令详细信息请用help查看");
                        }
                    }
            }
        }
    }

    private static  void handlerCommand(String command,String ...args){
        switch (command){
            case "dy-log list all":
                print(System.out,logStatService.service("/logList.json"));
                break;
            case "dy-log search":
                print(System.out,logStatService.service("/logList.json?keyWord="+args[0]));
                break;
            case "dy-log change all":
                logStatService.service("/changeLogLevel.json?level="+args[1]+"&logClass="+ args[0]);
                print(System.out,logStatService.service("/logList.json"));
                break;
            case "dy-log change":
                logStatService.service("/changeLogLevel.json?level="+args[1]+"&logClass="+ args[0]);
                print(System.out,logStatService.service("/logList.json"));
                break;
            case "sys-info":
                print(System.out,BasicSystemInfoUtil.getBasicSystemInfo());
                break;
            case "dy-log page":
                print(System.out, logStatService.service("/logList.json?pageSize=" + args[1] + "&pageNow=" + args[0]));
        }
    }

    private static void print(PrintStream printStream,Object object){
        if (object instanceof Map){
            ((Map)object).entrySet().stream().forEach(E -> {printStream.print(E );printStream.println();});
        }else if(object instanceof  String){
            printStream.print(JsonStringPrintFormatUtil.formatJson((String) object));
        }else {
            printStream.print(object);
        }
    }

    private static void help(PrintStream printStream){
        printStream.print("动态日志帮助工具使用文档，指令格式如下：\n");

        printStream.print("dy-log -command args\n");
        printStream.print("eg: dy-log list all列出所有的日志\n");
        printStream.print("    help 显示帮助文档\n");
        printStream.print("    dy-log page pageNumber [pageSize = 15] 分页输出日志对象\n");
        printStream.print("    dy-log search Key 查找日志对象对象，精确匹配【慎重使用】\n");
        printStream.print("    dy-log change logName level修改指定日志对象的级别[DEBUG,INFO,WARN,ERROR]\n");
        printStream.print("    dy-log change all level修改所有日志对象的级别[DEBUG,INFO,WARN,ERROR]\n");
        printStream.print("    sys-info 显示操作系统基本信息\n");
        printStream.print("    exit 退出\n");
    }

    /**
     * 输入提示信息
     * @param printStream
     * @param msg
     */
    private static void printHitMsg(PrintStream printStream,String msg){
        printStream.print(msg);
    }
}

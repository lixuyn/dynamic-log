package wsn.b405.util;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by @author cdlixu5 on 2017/12/31.
 */
public class BasicSystemInfoUtil {

    private static volatile Map<String, Object> dataMap = new LinkedHashMap<>(24);

    public static Map<String, Object> getBasicSystemInfo() {
        baseInfo(dataMap);
        getSystemDiskInfo(dataMap);
        getSystemMemoryInfo(dataMap);
        return dataMap;
    }

    public static void baseInfo(Map<String, Object> dataMap) {
        dataMap.put("JavaVMName", System.getProperty("java.vm.name"));
        dataMap.put("JavaVersion", System.getProperty("java.version"));
        dataMap.put("JavaClassPath", System.getProperty("java.class.path"));
        dataMap.put("StartTime", ManagementFactory.getRuntimeMXBean().getUptime() +"S");
    }

    public static void getSystemMemoryInfo(Map<String, Object> dataMap) {
        com.sun.management.OperatingSystemMXBean osm1 = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double free = (osm1.getFreePhysicalMemorySize());
        double total = (osm1.getTotalPhysicalMemorySize());
        dataMap.put("内存使用率", (int)((total - free) / total * 100) + "%");
        dataMap.put("总内存", change((long)total,2)+"MB");
        dataMap.put("可用内存", change((long)free,2) +"MB");
    }

    public static void getSystemDiskInfo(Map<String, Object> dataMap) {
        File[] roots = File.listRoots();// 获取磁盘分区列表
        for (File file : roots) {
            long free = file.getFreeSpace();
            long total = file.getTotalSpace();
            long use = total - free;
            List<String> info = new ArrayList<String>(3);
            info.add("空闲未使用 = " + change(free,3) + "G");
            info.add("总容量 = " + change(total,3) + "G");
            info.add("使用百分比 = " + bfb(use, total));
            dataMap.put(file.getPath(), info);
        }
    }

    public static long change(long num,int turn) {
        for (int i = 0; i < turn; i++) {
            num/=1024;
        }
        return num;
    }

    public static String bfb(Object num1, Object num2) {
        double val1 = Double.valueOf(num1.toString());
        double val2 = Double.valueOf(num2.toString());
        if (val2 == 0) {
            return "0.0%";
        } else {
            DecimalFormat df = new DecimalFormat("#0.00");
            return df.format(val1 / val2 * 100) + "%";
        }
    }
}

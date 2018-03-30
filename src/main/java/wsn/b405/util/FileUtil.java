package wsn.b405.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;

/**
 * Created by @author cdlixu5 on 2017/12/10.
 */
public class FileUtil {
    /**
     * 读取js,css，html文件
     * @param url
     * @return
     */
    public static String readFromResource(String url){
        Resource resource = new ClassPathResource(url);
        BufferedReader reader = null;
        StringWriter ex = new StringWriter();
        try {
            reader = new BufferedReader(new InputStreamReader(resource.getInputStream(),"utf8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            char[] buffer = new char[4096];
            boolean n = false;

            int n1;
            while(-1 != (n1 = reader.read(buffer))) {
                ex.write(buffer, 0, n1);
            }

            return ex.toString();
        } catch (IOException var4) {
            throw new IllegalStateException("read error", var4);
        }
    }

    /**
     * 读取媒体文件
     * @param filePath
     * @return
     */
    public static byte[] readByteArrayFromResource(String filePath) {
        Resource resource = new ClassPathResource(filePath);
        BufferedInputStream reader = null;
        ByteArrayOutputStream  ex = new ByteArrayOutputStream();
        try {
            reader = new BufferedInputStream(resource.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            byte[] buffer = new byte[4096];
            boolean n = false;

            int n1;
            while(-1 != (n1 = reader.read(buffer))) {
                ex.write(buffer, 0, n1);
            }

            return ex.toByteArray();
        } catch (IOException var4) {
            throw new IllegalStateException("read error", var4);
        }
    }
}

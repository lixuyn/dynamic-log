package wsn.b405.bean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by @author cdlixu5 on 2017/12/12.
 */
public class PageBean {
    private Map<String,Object> data;
    private int pageSize = 10;
    private int pageNow = 1;
    private int totalCont = 0;
    private int pageCount = 0;

    public Map<String,Object> getData() {
        return data;
    }

    public PageBean(Map<String, Object> data,int pageSize) {
        this.pageSize = pageSize;
        this.data = data;

        getTotalCont();
        getPageCount();
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageNow(int pageNow) {
        this.pageNow = pageNow;
    }

    public int getPageNow() {
        return pageNow;
    }


    public int getTotalCont() {
        this.totalCont =  this.data.size();
        return  this.totalCont;
    }


    public int getPageCount() {
        int totalCount = getTotalCont();
        pageCount = (totalCount % pageSize == 0) ? totalCount / pageSize : (totalCount / pageSize) + 1;
        return pageCount;
    }

    /**
     * 分页
     * @param pageNow
     * @return
     */
    public Map<String,Object> getPageDate(int pageNow){
        this.setPageNow(pageNow);
        Map<String, Object> res = new HashMap<String, Object>();
        Set<Map.Entry<String,Object>> set= data.entrySet();
        Iterator<Map.Entry<String,Object>> iterator = set.iterator();

        for(int i=0; (i<pageSize*pageNow) && (pageNow<=pageCount) && i<totalCont ;i++){
            if(i< pageSize * (pageNow -1)){
                iterator.next();
                continue;
            }else {
                Map.Entry<String, Object> it = iterator.next();
                res.put(it.getKey(),it.getValue());
            }
        }
        return res;
    }
}

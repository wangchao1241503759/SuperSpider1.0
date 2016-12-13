package cn.com.infcn.superspider.utils;

import java.util.UUID;

public class StringUtil {

    /**
     * 生成一个UUID，一般用处是用来标识数据库唯一主键用
     * 
     * @return UUID
     */
    public static String generateUUID() {

        return UUID.randomUUID().toString().toLowerCase().replace("-", "");
    }
    
    /**
     * 判断字串是否为空
     * 
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        return (str != null && str.length() != 0) ? (str.trim().length() != 0 ? false : true) : true;
    }
}

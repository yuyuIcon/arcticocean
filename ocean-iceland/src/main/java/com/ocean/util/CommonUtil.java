package com.ocean.util;

import java.util.Map;
import java.util.UUID;

/**
 * Created by niu on 2018/1/22.
 */
public class CommonUtil {

    /**
     * 获得一个UUID
     *
     * @return String UUID
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    /**
     * 将爱回收推送参数进行排序重组
     * @param map
     * @param secret
     * @return
     */
    public static String getSortPrams(Map<String, Object> map, String secret) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            builder.append(entry.getKey()).append("=");
            if (entry.getValue() != null) {
                builder.append(entry.getValue());
            }
            builder.append("&");
        }
        builder.append("secret").append("=").append(secret);
        return builder.toString();
    }
    
}

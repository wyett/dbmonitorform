package com.wyett.agent.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.wyett.common.bean.InstanceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/14 22:45
 * @description: TODO
 */

public class ParseUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ParseUtil.class);

    //dbname 436G 1744M 985G 457G 478G 49% / 0 0 0 0 0 0 0
    public static InstanceBean bindInsInfoToObject(String str) {
        // cast string to array
        String[] arr = str.replaceAll("G|M","")
                .replaceAll("\\s+", " ")
                .split(" ");

        if (arr.length != 15) {
            LOG.error("str does not match InsInfoDto: " + str );
            return null;
        }

        return InstanceBean.builder()
                .dbname(arr[0])
                .dbSize(Integer.parseInt(arr[1]))
                .tbSize(Integer.parseInt(arr[2]))
                .diskSize(Integer.parseInt(arr[3]))
                .diskFreeSize(Integer.parseInt(arr[4]))
                .diskUsedSize(Integer.parseInt(arr[5]))
                .usedPct(arr[6])
                .path(arr[7])
                .binlogSize(Arrays.asList(
                        new String[]{arr[8],arr[9],arr[10],arr[11],arr[12],arr[13],arr[14]})
                        .stream()
                        .mapToInt(Integer::parseInt).toArray()).build();
    }

    /**
     * format json string
     * @param s
     * @return
     */
    private static String format(String s) {
        return s.replaceAll("G|M|\\s+", "");
    }

    /**
     * parse jsonString into T object
     * @param jsonString {"dbname":"tl_list_0","dbSize":"396G","tbSize":"792M","diskSize":"788G","diskSize":"411G",
     *         "diskFreeSize":"337G","usedPct":"55%","path":"/","binlogSize":[0,0,0,0,0,0,0]}
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T parseStringToObject(String jsonString, Class<T> t) {
        T result = null;
        try {
            result = JSON.parseObject(format(jsonString), t);
        } catch (JSONException e) {
            System.out.println("parse String " + jsonString + " failed");
        }
        return result;
    }
}

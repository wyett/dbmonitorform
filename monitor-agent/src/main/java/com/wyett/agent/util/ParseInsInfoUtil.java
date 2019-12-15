package com.wyett.agent.util;

import com.wyett.common.dto.InsInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/14 22:45
 * @description: TODO
 */

public class ParseInsInfoUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ParseInsInfoUtil.class);

    //dbname 436G 1744M 985G 457G 478G 49% / 0 0 0 0 0 0 0
    public static InsInfoDto bindInsInfoToObject(String str) {
        // cast string to array
        String[] arr = str.replaceAll("G|M","")
                .replaceAll("\\s+", " ")
                .split(" ");

        if (arr.length != 15) {
            LOG.error("str does not match InsInfoDto: " + str );
            return null;
        }

        return InsInfoDto.builder()
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
}

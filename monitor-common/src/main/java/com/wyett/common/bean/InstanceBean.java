package com.wyett.common.bean;

import lombok.*;

import java.io.Serializable;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/14 21:01
 * @description: TODO
 */

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstanceBean implements Serializable {
//    rela_pers_2 436G 1744M 985G 457G 478G 49% / 0 0 0 0 0 0 0
    private String hostIp;
    private String dbname;
    private int dbSize;
    private int tbSize;
    private int diskSize;
    private int diskUsedSize;
    private int diskFreeSize;
    private String usedPct;
    private String path;
    private int[] binlogSize;
}

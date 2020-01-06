package com.wyett.server;

import com.wyett.common.config.ZkServerConfiguration;
import com.wyett.common.core.CfgFactory;

/**
 * @author : wyettLei
 * @date : Created in 2020/1/6 15:24
 * @description: TODO
 */

public class ZkConfigTest {
    public static void main(String[] args) {
        ZkServerConfiguration zkc = CfgFactory.readProperties(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("conf/config.properties"),
                ZkServerConfiguration.class);

        System.out.println(zkc.getZkConnLimitSize());
    }
}

package com.wyett.server;

import com.wyett.common.config.Resource;
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
                Resource.getResource("conf/config.properties"),
                ZkServerConfiguration.class);

        // close inputstream
        Resource.release();

        System.out.println(zkc.getZkConnLimitSize());
    }
}

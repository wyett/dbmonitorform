package com.wyett.server;

import com.wyett.common.config.Resource;
import com.wyett.common.config.ZkServerConfiguration;
import com.wyett.common.core.CfgFactory;
import com.wyett.common.factory.ZkServerFactory;
import com.wyett.common.service.ZkServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/30 16:51
 * @description: TODO
 */

public class ServerUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ServerUtil.class);

    private static ZkServerService zkServerService = new ZkServerFactory().create();

    // read config
    private static ZkServerConfiguration zkServerConfiguration = CfgFactory.readProperties(
            Resource.getResource("conf/config.properties"),
            ZkServerConfiguration.class);

    private static ServerUtil serverUtil;

    // signleton
    public static ServerUtil getInstance() {
        if (serverUtil == null) {
            return new ServerUtil();
        }
        return serverUtil;
    }

    // contructor
    public ServerUtil() {
        zkServerService.createRootNode();

        for (String s : zkServerService.getClusterPathAsList(zkServerConfiguration.getZkRootPath())) {
            zkServerService.initListener(s);
        }
    }
}

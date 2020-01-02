package com.wyett.server;

import com.wyett.common.config.ZkServerConfiguration;
import com.wyett.common.service.ZkServerService;
import com.wyett.common.service.impl.ZkServerServiceImpl;
import com.wyett.common.util.ZkPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/30 16:51
 * @description: TODO
 */

public class ServerUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ServerUtil.class);

    private static ZkServerService zkServerService = new ZkServerServiceImpl();

    // read config
    private static ZkServerConfiguration zkServerConfiguration;
    private static String zkRootPath = zkServerConfiguration.getZkRootPath();

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

        for (String s : zkServerService.getClusterPathAsList(zkRootPath)) {
            zkServerService.initListener(s);
        }
    }
}

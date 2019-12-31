package com.wyett.server;

import com.wyett.common.config.ZkServerConfiguration;
import com.wyett.common.util.ZkPoolUtil;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.util.ZkPathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/30 16:51
 * @description: TODO
 */

public class ServerUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ServerUtil.class);

    private static ZkServerConfiguration zkServerConfiguration;
    private static ZkPoolUtil zkPool = ZkPoolUtil.getInstance();

    public ServerUtil() {

    }


}

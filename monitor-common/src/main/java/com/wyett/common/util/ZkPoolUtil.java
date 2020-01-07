package com.wyett.common.util;

import com.wyett.common.config.Resource;
import com.wyett.common.config.ZkServerConfiguration;
import com.wyett.common.core.CfgFactory;
import com.wyett.common.core.annotation.ReadConf;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


/**
 * @author : wyettLei
 * @date : Created in 2019/12/19 16:43
 * @description: TODO
 */

public class ZkPoolUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ZkPoolUtil.class);

    private static ZkServerConfiguration zkServerConfiguration = CfgFactory.readProperties(
            Resource.getResource("conf/config.properties"),
            ZkServerConfiguration.class);

    private static ZkPoolUtil instance = null;
    // pool
    private static Vector<ZkClient> zkClientPool = null;

    // pool size
    private static int zkPoolSize = zkServerConfiguration.getZkConnLimitSize();

    // init zk connection pool
    static {
        zkClientPool = new Vector<>(zkPoolSize);
        ZkClient zkClient = null;
        for (int i = 0; i < zkPoolSize; i++) {
            zkClient = new ZkClient(zkServerConfiguration.getZkServer(),
                    zkServerConfiguration.getZkSessionTimeOut(),
                    zkServerConfiguration.getZkConnTimeOut());
            zkClientPool.add(zkClient);
        }
    }

    // create ZkPoolUtil instance
    public synchronized static ZkPoolUtil getInstance() {
        if (instance != null) {
            return new ZkPoolUtil();
        }
        return instance;
    }

    public ZkPoolUtil() {}

    /**
     * get zkClient from zkConnectionPool
     */
    public synchronized ZkClient getZkConnection() {
        ZkClient zkc = null;
        if (zkClientPool.size() > 0) {
            zkc = zkClientPool.get(0);
            zkClientPool.remove(0);
            LOG.info("zk connection pool size is " + zkClientPool.size());
        } else {
            for (int i = 0; i < zkPoolSize; i++) {
                zkc = new ZkClient(zkServerConfiguration.getZkServer(),
                        zkServerConfiguration.getZkSessionTimeOut(),
                        zkServerConfiguration.getZkConnTimeOut());
                zkClientPool.add(zkc);
            }
            zkc = zkClientPool.get(0);
            zkClientPool.remove(0);
            LOG.info("zk connection pool size is " + zkClientPool.size());
        }
        return zkc;
    }

    /**
     * release zkclient in zkClientPool
     * @param zkc
     */
    public synchronized void release(ZkClient zkc) {
        if (zkClientPool != null && zkClientPool.size() < zkPoolSize) {
            zkClientPool.add(zkc);
        }
        LOG.info("release zkClient and pool size is " + (zkClientPool == null ? 0 : zkClientPool.size()));
    }
}

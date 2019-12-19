package com.wyett.registry.util;

import com.alibaba.fastjson.JSON;
import com.wyett.common.util.MonitorConfigUtils;
import com.wyett.registry.config.ZkServerConfiguration;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/19 16:43
 * @description: TODO
 */

public class ZkUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ZkUtil.class);

//    private static final String ZK_CONN = MonitorConfigUtils.getProperty("zk.servers");
//    private static final String ROOT_PATH = MonitorConfigUtils.getProperty("zk.root.path");
//    private static final int SESSION_TIMEOUT = MonitorConfigUtils.getIntProperty("zk.session.timeout");
//    private static final int CONN_TIMEOUT = MonitorConfigUtils.getIntProperty("zk.connection.timeout");
    private static ZkServerConfiguration zkServerConfiguration;
    private static ZkClient zkClient;

    static {
        zkClient = new ZkClient(zkServerConfiguration.getZkServer(),
                zkServerConfiguration.getZkConnTimeOut(),
                zkServerConfiguration.getZkSessionTimeOut());
    }

    /**
     * create root node
     */
    public static void createRootNode() {
        String zkRoot = zkServerConfiguration.getZkRootPath();
        if (!zkClient.exists(zkRoot)) {
            List<ACL> list = new ArrayList<>();
            int perm = ZooDefs.Perms.CREATE | ZooDefs.Perms.DELETE | ZooDefs.Perms.READ;
            ACL acl = new ACL(perm, new Id("world", "anyone"));
            list.add(acl);
            zkClient.createPersistent(zkRoot, "monitor root node", list);
            LOG.info("created root node: " + zkRoot);
        }
    }

    /**
     * save hearbeat info into server nodes
     */
    public static void createServerNode(String serverName, JSON json) {
        String serverNode = zkServerConfiguration.getZkRootPath() + "/" + serverName;
        if (!zkClient.exists(serverName)) {
            List<ACL> aclList = new ArrayList<>();
            int perm = ZooDefs.Perms.READ | ZooDefs.Perms.WRITE;
            ACL acl = new ACL(perm, new Id("world", "anyone"));
            aclList.add(acl);
            zkClient.createEphemeral(serverName, json, aclList);
            LOG.info("created service node: " + serverNode);
        }
    }

    /**
     * update server node
     */
    public static void updateServerNode(String serverName, JSON json) {
        String serverNode = zkServerConfiguration.getZkRootPath() + "/" + serverName;
        zkClient.writeData(serverNode, json);
        LOG.info("updated node " + serverNode);
    }


}

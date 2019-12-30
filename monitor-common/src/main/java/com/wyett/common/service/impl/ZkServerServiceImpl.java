package com.wyett.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.wyett.common.config.ZkServerConfiguration;
import com.wyett.common.service.ZkServerService;
import com.wyett.common.util.ZkPoolUtil;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/20 11:54
 * @description: TODO
 */

public class ZkServerServiceImpl implements ZkServerService {

    private static final Logger LOG = LoggerFactory.getLogger(ZkServerServiceImpl.class);

    private static ZkServerConfiguration zkServerConfiguration;
    private static ZkPoolUtil zkUtil = ZkPoolUtil.getInstance();

    /**
     * create root node
     */
    @Override
    public void createRootNode() {
        String zkRoot = zkServerConfiguration.getZkRootPath();
        ZkClient zc = zkUtil.getZkConnection();
        if (!zc.exists(zkRoot)) {
            List<ACL> list = new ArrayList<>();
            int perm = ZooDefs.Perms.CREATE | ZooDefs.Perms.DELETE | ZooDefs.Perms.READ;
            ACL acl = new ACL(perm, new Id("world", "anyone"));
            list.add(acl);
            zc.createPersistent(zkRoot, "monitor root node", list);
            LOG.info("created root node: " + zkRoot);
        }
    }

    /**
     * save hearbeat info into server nodes
     */
    @Override
    public void createServerNode(String serverName, JSON json) {
        String serverNode = zkServerConfiguration.getZkRootPath() + "/" + serverName;
        ZkClient zc = zkUtil.getZkConnection();
        if (!zc.exists(serverName)) {
            List<ACL> aclList = new ArrayList<>();
            int perm = ZooDefs.Perms.READ | ZooDefs.Perms.WRITE;
            ACL acl = new ACL(perm, new Id("world", "anyone"));
            aclList.add(acl);
            zc.createEphemeral(serverName, json, aclList);
            LOG.info("created service node: " + serverNode);
        }
    }

    /**
     * update server node
     */
    @Override
    public void updateServerNode(String serverName, JSON json) {
        String serverNode = zkServerConfiguration.getZkRootPath() + "/" + serverName;
        ZkClient zc = zkUtil.getZkConnection();
        zc.writeData(serverNode, json);
        LOG.info("updated node " + serverNode);
    }

    /**
     * read data from node
     * @param path
     * @return
     */
    @Override
    public String getData(String path) {
        return zkUtil.getZkConnection().readData(path);
    }

    /**
     * get child list
     * @param path
     * @return
     */
    @Override
    public List<String> getClusterPathAsList(String path) {
        return zkUtil.getZkConnection().getChildren(path).stream()
                .map(p -> path + "/" + p)
                .collect(Collectors.toList());
    }

    @Override
    public void initListener(String path) {
        zkUtil.getZkConnection().unsubscribeAll();
        zkUtil.getZkConnection().getChildren(path)
                .stream()
                .map(p -> path + "/" + p)
                .forEach(p -> zkUtil.getZkConnection().subscribeDataChanges(p, new DataListenerImpl()));

    }

/*
    private class DataChange implements IZkDataListener, IZkChildListener {

        @Override
        public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {

        }

        @Override
        public void handleDataChange(String dataPath, Object data) throws Exception {

        }

        @Override
        public void handleDataDeleted(String dataPath) throws Exception {

        }
    }

 */
}

package com.wyett.agent;

import com.wyett.agent.util.ExecUtil;
import com.wyett.agent.util.ParseUtil;
import com.wyett.common.dto.InsInfoDto;
import com.wyett.common.util.MonitorConfigUtils;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/9 15:08
 * @description: TODO
 */

public class MonitorAgent {
    private static final Logger LOG = LoggerFactory.getLogger(MonitorAgent.class);

    private static final String ZK_CONN = MonitorConfigUtils.getProperty("zk.servers");
    private static final String ROOT_PATH = MonitorConfigUtils.getProperty("zk.root.path");
    private static final int CONN_TIMEOUT = MonitorConfigUtils.getIntProperty("zk.session.timeout");
    private static final int SESSION_TIMEOUT = MonitorConfigUtils.getIntProperty("zk.connection.timeout");

    // presistent and sequence
    private static final String SERVICE_PATH = ROOT_PATH + "/service";
    // "/monitor/service0000000001"
    private String nodePath;

    private ZkClient zkClient;
    private Thread stateThread;

    // agent singleton
    private MonitorAgent() {}
    private static MonitorAgent agent = new MonitorAgent();
    public static MonitorAgent getInstance() {
        return agent;
    }

    /**
     * init agent
     * @param args
     * @param instrumentation
     */
    public static void premain(String args, Instrumentation instrumentation) {
        LOG.info("begin to create agent...");
        MonitorAgent.getInstance().init();
    }

    public void init() {
        zkClient = new ZkClient(ZK_CONN, SESSION_TIMEOUT, CONN_TIMEOUT);

        //create root node
        createRootNode();

        // create temp node
        createServiceNode();

        // thread
        stateThread = new Thread(() -> {
            while(true) {
                updateServiceNode();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread_db_monitor");
        stateThread.setDaemon(true);
        stateThread.start();
    }

    /**
     * create root node
     */
    public void createRootNode() {
        if (!zkClient.exists(ROOT_PATH)) {
            List<ACL> list = new ArrayList<>();
            int perm = ZooDefs.Perms.CREATE | ZooDefs.Perms.DELETE;
            ACL acl = new ACL(perm, new Id("world", "anyone"));
            list.add(acl);
            zkClient.createPersistent(ROOT_PATH, "monitor root node", list);
            LOG.info("created root node: " + ROOT_PATH);
        }
    }

    /**
     * create service node
     */
    public void createServiceNode() {
        List<ACL> aclList = new ArrayList<>();
        int perm = ZooDefs.Perms.READ | ZooDefs.Perms.WRITE;
        ACL acl = new ACL(perm, new Id("world", "anyone"));
        aclList.add(acl);
        nodePath = zkClient.createEphemeralSequential(SERVICE_PATH, getInsInfo(), aclList);
        LOG.info("created service node: " + nodePath);
    }

    /**
     * update service node
     */
    public void updateServiceNode() {
        zkClient.writeData(nodePath, getInsInfo());
        LOG.info("updated node " + nodePath);
    }

    /**
     * get local host ip
     * @return
     */
    private String getLocalIp() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return addr.getHostAddress();
    }

    /**
     * get os info
     * @return
     */
    public String getInsInfo() {
        InsInfoDto insInfoDto = new InsInfoDto();

        ExecUtil execUtil = new ExecUtil();

//        List<Map<String, Object>> lmso = new ArrayList<>();
//        lmso = execUtil.loadTask();

        for(Map<String, Object> mso : execUtil.loadTask()) {
            String cmd = execUtil.getCmd(mso);
            int internal = execUtil.getIntneralTime(mso);
            insInfoDto = ParseUtil.bindInsInfoToObject(execUtil.exec(cmd, 5000));
            insInfoDto.setHostIp(getLocalIp());
            // todo
        }

        return null;
    }
}

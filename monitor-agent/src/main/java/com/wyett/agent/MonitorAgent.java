package com.wyett.agent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wyett.agent.util.ExecUtil;
import com.wyett.agent.util.ParseUtil;
import com.wyett.common.dto.InsInfoDto;
import com.wyett.common.util.MonitorConfigUtils;
import com.wyett.common.util.PackageUtil;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/9 15:08
 * @description: TODO
 */

public class MonitorAgent {
    private static final Logger LOG = LoggerFactory.getLogger(MonitorAgent.class);

    private static final String DTO_PKG = "com.wyett.common.dto";

    private static final String ZK_CONN = MonitorConfigUtils.getProperty("zk.servers");
    private static final String ROOT_PATH = MonitorConfigUtils.getProperty("zk.root.path");
    private static final int SESSION_TIMEOUT = MonitorConfigUtils.getIntProperty("zk.session.timeout");
    private static final int CONN_TIMEOUT = MonitorConfigUtils.getIntProperty("zk.connection.timeout");
    private static final long SCHEDULER_INTERNAL = MonitorConfigUtils.getLongProperty("task.scheduler.internal");

    private static String hostIp;
    static {
        hostIp = getLocalIp();
    }

    // server node,
    private static final String SERVER_PATH = ROOT_PATH + "/" + hostIp;

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

        // create server node
        createServerNode();

        // thread
        stateThread = new Thread(() -> {
            while(true) {
                updateServerNode();
                try {
                    Thread.sleep(SCHEDULER_INTERNAL);
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
            int perm = ZooDefs.Perms.CREATE | ZooDefs.Perms.DELETE | ZooDefs.Perms.READ;
            ACL acl = new ACL(perm, new Id("world", "anyone"));
            list.add(acl);
            zkClient.createPersistent(ROOT_PATH, "monitor root node", list);
            LOG.info("created root node: " + ROOT_PATH);
        }
    }

    /**
     * save hearbeat info into server nodes
     */
    public void createServerNode() {
        List<ACL> aclList = new ArrayList<>();
        int perm = ZooDefs.Perms.READ | ZooDefs.Perms.WRITE;
        ACL acl = new ACL(perm, new Id("world", "anyone"));
        aclList.add(acl);
        zkClient.createEphemeral(SERVER_PATH, getHreatbeatInfo(), aclList);
        LOG.info("created service node: " + SERVER_PATH);
    }

    /**
     * update server node
     */
    public void updateServerNode() {
        zkClient.writeData(SERVER_PATH, getHreatbeatInfo());
        LOG.info("updated node " + SERVER_PATH);
    }

    /**
     * get local host ip
     * @return
     */
    private static String getLocalIp() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return addr.getHostAddress();
    }

    /**
     * get all dto
     * @return
     */
    private static Map<String, Class<?>> getAllDto() {

        List<Class<?>> classList = null;
        try {
            classList = PackageUtil.getClassListInPackage(DTO_PKG, false, ".class");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Map<String, Class<?>> msc = PackageUtil.convertClassToString(classList);

        return msc;

    }

    /**
     * create heartbeat service info
     * @return {service : timestamp}
     */
    public Map<String, Long> getHreatbeatInfo() {
        ExecUtil execUtil = new ExecUtil();
        Map<String, Class<?>> msc = getAllDto();

        Map<String, Long> msd = execUtil.loadTask().stream()
                .map(s -> "DTO_PKG" + "." + execUtil.getClassName(s))
                .filter(s -> msc.keySet().stream().anyMatch(k -> s.equals(k)))
                .collect(HashMap::new,
                        (m, s) -> m.put(s, System.currentTimeMillis()),
                        (m1, m2) -> m1.putAll(m2));

//        return JSON.toJSONString(msd);

        return msd;
    }

    /**
     * parse result as Class<?>, not used
     * @return
     */
    public Map<Class<?>, JSON> ParseResult() {
        // executil
        ExecUtil execUtil = new ExecUtil();

        // Dto Map
        Map<String, Class<?>> msc = getAllDto();

        // result of all scripts
        Map<Class<?>, JSON> mcj = new HashMap<>();

//        Map<Class<?>, ?> mc = new HashMap<>();

        for(Map<String, String> mso : execUtil.loadTask()) {
            String cmd = execUtil.getCmd(mso);

            String resultJson = execUtil.getClassName(mso);
            String className = DTO_PKG + "." + resultJson;

            Object o = null;

            // check if exist Object
            if (msc.keySet().stream().anyMatch(s -> className.equals(s))) {
                // get class
                Class<?> clazz = msc.get(className);

                // cast string json into Object clazz
                o = ParseUtil.parseStringToObject(execUtil.exec(cmd, 5000), clazz);

                // save
                mcj.put(clazz, (JSONObject) JSON.toJSON(o));

//                mc.put(clazz, (clazz) o);
            } else {
                msc.keySet().stream().forEach(System.out::println);
            }
        }

        return mcj;
    }
}

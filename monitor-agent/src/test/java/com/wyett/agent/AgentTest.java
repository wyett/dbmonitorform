package com.wyett.agent;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/18 11:21
 * @description: TODO
 */

public class AgentTest {

//    private static final String HOME_PATH = System.getProperties().getProperty("user.dir");
    public static void main(String[] args) {
        MonitorAgent.premain(null, null);
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

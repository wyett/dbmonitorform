package com.wyett.server;

/**
 * @author : wyettLei
 * @date : Created in 2020/1/2 17:17
 * @description: TODO
 */

public class ServerUtilTest {
    public static void main(String[] args) {
        ServerUtil serverUtil = ServerUtil.getInstance();
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

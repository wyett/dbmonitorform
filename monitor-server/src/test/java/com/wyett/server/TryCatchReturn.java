package com.wyett.server;

/**
 * @author : wyettLei
 * @date : Created in 2020/1/7 9:31
 * @description: TODO
 */

public class TryCatchReturn {
    public int test() {
        int x = 5;
        try {
            x = 10;
            return x;
        } catch (Exception e) {
            x = 11;
            return x;
        } finally {
            //x = 12;
            return x;
        }
    }

    public static void main(String[] args) {
        System.out.println(new TryCatchReturn().test());
    }
}

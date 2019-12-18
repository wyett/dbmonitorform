package com.wyett.agent;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/18 17:24
 * @description: TODO
 */

public class ConfigPathTest1 {
    @Test
    public void test() throws IOException {
        InputStream in = new FileInputStream("conf/config.properties");

        Properties props = new Properties();
        props.load(in);
        System.out.println(props.getProperty("user.dir"));

        ///G:/java/dbmonitorform/monitor-agent/target/test-classes/
        String str1 = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(str1);

        ///G:/java/dbmonitorform/monitor-agent/target/test-classes/
        String str2 = this.getClass().getResource("").getPath();
        System.out.println(str2);

        ///G:/java/dbmonitorform/monitor-agent/target/test-classes/
        String str3 = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println(str3);

        String str4 = Thread.currentThread().getClass().getResource("").getPath();
        System.out.println(str4);

    }
}

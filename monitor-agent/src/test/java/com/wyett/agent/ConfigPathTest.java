package com.wyett.agent;


import com.wyett.common.util.MonitorConfigUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/14 14:58
 * @description: read task scheduler, and exec as plan
 */

public class ConfigPathTest {

    private static final String taskFile = MonitorConfigUtils.getProperty("task.scheduler.conf");

    @Test
    public void test() throws IOException {
        System.out.println(taskFile);
        File file = new File(taskFile);
        if (file.exists()) {
            System.out.println("exist");
        }

//        MonitorConfigUtils mcu = new MonitorConfigUtils(taskFile);
//        mcu.
        Properties properties = new Properties();
//        properties.load(this.getClass().getClassLoader().getResourceAsStream(taskFile));
//        properties.load(this.getClass().getClassLoader().getResourceAsStream(
//                System.getProperties().getProperty("user.dir") + "/conf/taskCmd" +
//                ".properties"));
        URL url = this.getClass().getResource(taskFile);
        System.out.println("url:" + url);

        System.out.println(this.getClass().getResource("/").getPath());
        System.out.println(this.getClass().getResource("").getPath());

        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("conf/config.properties");
        System.out.println(in.read());
        properties.load(new FileInputStream(taskFile));
        System.out.println(properties.getProperty("cmd"));

    }

    @Test
    public void test1() throws IOException {
        Properties props = new Properties();

//        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("conf/config.properties");
        InputStream in = new FileInputStream("conf/config.properties");
        props.load(in);

        System.out.println(props.getProperty("task.scheduler.conf"));

    }

    @Test
    public void test2() {
        Properties props = System.getProperties();
        System.out.println(props);

//        Arrays.asList(props.keySet()).stream()
//                .map(k -> props.get(k))
//                .forEach(k -> System.out.println(k + ":" + props.get(k)));
    }
}

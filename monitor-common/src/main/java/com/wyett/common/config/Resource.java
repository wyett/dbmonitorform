package com.wyett.common.config;

import org.junit.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author : wyettLei
 * @date : Created in 2020/1/6 19:22
 * @description: TODO
 */

public class Resource {

    private static final Logger LOG = LoggerFactory.getLogger(Resource.class);

    public static InputStream in = null;

    public static InputStream getResource(String name) {
        String osName = System.getProperties().getProperty("os.name");
        try {
            if (osName.contains("Win") || osName.contains("Mac")) {
                in = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
            } else if(osName.contains("Linux")) {
                in = new FileInputStream(System.getProperties().getProperty("user.dir") + "/" + name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    public static void release() {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.wyett.common.config;

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

    private static InputStream getResource(String name) {
        InputStream in = null;
        String osName = System.getProperties().getProperty("os.name");
        try {
            if (osName.contains("Win") || osName.contains("Mac")) {
                in = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
            } else if(osName.contains("Linux")) {
                in = new FileInputStream(System.getProperties().getProperty("user.dir") + "/" + name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LOG.debug("config loaded sucessfully...");
        return in;
    }
}

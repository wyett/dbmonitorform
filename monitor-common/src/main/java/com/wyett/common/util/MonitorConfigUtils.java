package com.wyett.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/14 15:34
 * @description: TODO
 */

public class MonitorConfigUtils {
    private static final Logger LOG = LoggerFactory.getLogger(MonitorConfigUtils.class);
    private static Properties properties;
    private static String configName;

    static {
        properties = new Properties();
        getResource("conf/config.properties");
    }

    public MonitorConfigUtils() {
    }

    public MonitorConfigUtils(String configName) {
        properties = new Properties();
        getResource(configName);
    }

    /**
     * reload
     * @param name
     */
    private void reload(String name) {
        properties.clear();
        getResource(configName);
    }

    public static Properties getProperties() {
        return properties;
    }

    /**
     * read config
     * @param name
     */
    private static void getResource(String name) {
        String osName = System.getProperties().getProperty("os.name");
        try {
            /*if (osName.contains("Win") || osName.contains("Mac")) {
                properties.load(MonitorConfigUtils.class.getClassLoader().getResourceAsStream(name));
            } else if(osName.contains("Linux")) {
                properties.load(new FileInputStream(
                        System.getProperties().getProperty("user.dir") + "/" + name));
            }*/
            properties.load(new FileInputStream(System.getProperties().getProperty("user.dir") + "/" + name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG.debug("config loaded sucessfully...");

        if (LOG.isDebugEnabled()) {
            LOG.debug("config deubg like this...");

            String key = null;
            Enumeration<Object> keys = properties.keys();
            while(keys.hasMoreElements()) {
                key = (String) keys.nextElement();
                LOG.debug(key + " = " + properties.getProperty(key));
            }
        }
    }

    /**
     * get key value
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * get key value as array
     * @param key
     * @param value
     * @param splitStr
     * @return
     */
    public static String[] getPropertyArray(String key, String[] value, String splitStr) {
        String keyValue = properties.getProperty(key);
        if(keyValue == null || "".equals(keyValue)) {
            return value;
        }
        try {
            String[] defaultValue = keyValue.split(splitStr);
            return defaultValue;
        } catch (Exception e) {
            return value;
        }
    }

    /**
     * get key value as map
     * @param key
     * @return
     */
    public static Map<String, String> getPropertyMap(String key) {
        String[] maps = getPropertyArray(key, null, ",");
        Map<String, String> map = new HashMap<>();

        try {
            for (String m : maps) {
                String[] ms = m.split(":");
                if (ms.length > 1) {
                    map.put(ms[0], ms[1]);
                }
            }
        } catch(Exception e) {
            LOG.error("get property map error, key is " + key);
        }
        return map;
    }

    /**
     * return int value
     * @param key
     * @return
     */
    public static int getIntProperty(String key) {
        return getIntProperty(key, 0);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || "".equals(value)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static long getLongProperty(String key) {
        return getLongProperty(key, 0L);
    }

    public static long getLongProperty(String key, long defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || "".equals(value)) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static boolean getBooleanProperty(String key) {
        return getBooleanProperty(key, false);
    }

    /**
     * get boolean value
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || "".equals(value)) {
            return defaultValue;
        }
        return Boolean.valueOf(value).booleanValue();
    }
}

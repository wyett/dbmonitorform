package com.wyett.common.core;

import com.wyett.common.core.annotation.ReadConf;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author : wyettLei
 * @date : Created in 2019/9/30 17:10
 * @description: TODO
 */

public class PropertyInvocationHandler implements InvocationHandler {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyInvocationHandler.class);

    private Properties properties;
    public PropertyInvocationHandler(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] obj) {
        ReadConf readConf = method.getAnnotation(ReadConf.class);
        if(readConf == null) {
            return null;
        }
        String value = readConf.value();
        String property = properties.getProperty(value);
        if(StringUtils.isEmpty(property)) {
            return null;
        }

        Class<?> returnClass = method.getReturnType();
        if(returnClass.isPrimitive()) {
            if (returnClass.equals(int.class)) { return Integer.valueOf(property); }
            if (returnClass.equals(long.class)) { return Long.valueOf(property); }
            if (returnClass.equals(float.class)) { return Float.valueOf(property); }
            if (returnClass.equals(double.class)) { return Double.valueOf(property); }
            if (returnClass.equals(boolean.class)) { return Boolean.valueOf(property); }
            if (returnClass.equals(short.class)) { return Short.valueOf(property); }
//            if (returnClass.equals(char.class)) { return Character.valueOf(property); }
            if (returnClass.equals(byte.class)) { return Byte.valueOf(property); }
        } else {
            if (returnClass.equals(String.class)) {
                return getProperty(property);
            }
            if (returnClass.equals(String[].class)) {
                return getPropertyArray(property, new String[]{}, ",");
            }
            if (returnClass.equals(Map.class)) {
                return getPropertyMap(property);
            }
            if (returnClass.equals(Boolean.class)) {
                return getBooleanProperty(property);
            }
            if (returnClass.equals(Integer.class)) {
                return getIntProperty(property);
            }
            if (returnClass.equals(Long.class)) {
                return getLongProperty(property);
            }

        }
        return property;
    }

    /**
     * get key value
     * @param key
     * @return
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * get key value as array
     * @param key
     * @param value
     * @param splitStr
     * @return
     */
    public String[] getPropertyArray(String key, String[] value, String splitStr) {
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
    public Map<String, String> getPropertyMap(String key) {
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
    public int getIntProperty(String key) {
        return getIntProperty(key, 0);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public int getIntProperty(String key, int defaultValue) {
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

    public long getLongProperty(String key) {
        return getLongProperty(key, 0L);
    }

    public long getLongProperty(String key, long defaultValue) {
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

    public boolean getBooleanProperty(String key) {
        return getBooleanProperty(key, false);
    }

    /**
     * get boolean value
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || "".equals(value)) {
            return defaultValue;
        }
        return Boolean.valueOf(value).booleanValue();
    }
}

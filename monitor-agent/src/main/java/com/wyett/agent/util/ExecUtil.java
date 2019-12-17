package com.wyett.agent.util;

import com.wyett.common.util.MonitorConfigUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/14 18:37
 * @description: TODO
 */

public class ExecUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ExecUtil.class);

    private static String taskFile = MonitorConfigUtils.getProperty("task.scheduler.conf");

//    private static List<Map<String, Object>> taskList = null;

    public ExecUtil() {
    }

    /**
     * load task conf, and convert it to maps
     * @return
     */
    public List<Map<String, String>> loadTask() {
        List<Map<String, String>> cmdList = new ArrayList<>();
        MonitorConfigUtils mcu = new MonitorConfigUtils(taskFile);

        // method name
        Method method = null;

        try {
            // load taskCmd.conf
            method = mcu.getClass().getDeclaredMethod("getProperties");
            Properties props = (Properties) method.invoke(null);

            // get cmd list
            Map<String, String> map = (Map) props;
            return map.keySet().stream()
                    .map(p -> map.get(p))
                    .map(m -> convertConfToMap(m, ","))
                    .collect(Collectors.toList());

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * convert string to map
     * @param str
     * @param splitStr
     * @return
     */
    public Map<String, String> convertConfToMap(String str, String splitStr) {
        if (!str.contains(splitStr) && !str.contains(":")) {
            return null;
        }
        Map<String, String> maps =
                Arrays.asList(str.split(splitStr))
                        .stream().map(s -> s.split(":"))
                        .collect(HashMap::new,
                                (m, s) -> m.put(s[0], s[1]),
                                (m1, m2) -> m1.putAll(m2));

        return maps;
    }

    /**
     * get key value
     * @param map
     * @param key
     * @return
     */
    private String getKeyValue(Map<String, String> map, String key) {
        return map.containsKey(key) && map.get(key) != null ? map.get(key) : null;
    }

    /**
     * get command to execute
     * @param map
     * @return
     */
    public String getCmd(Map<String, String> map) {
        String scriptFile = System.getProperties().getProperty("user.dir") + "/"
                + (String) getKeyValue(map, "script");

        File file = new File(scriptFile);
        if (!file.exists()) {
            LOG.error("cmd file does not exists");
            return null;
        }
        return (String) getKeyValue(map, "env") + " " + scriptFile;
    }

    /**
     * get execute internal time
     * @param map
     * @return
     */
    /*
    public int getIntneralTime(Map<String, String> map) {
        return map.containsKey("internal") &&
                map.get("internal") != null ? Integer.parseInt(map.get("internal")) : 5;
    }
     */

    public String getClassName(Map<String, String> map) {
        return (String) getKeyValue(map, "resultJson");
    }

    /**
     * exec command
     * @param cmd
     * @param waitTime
     * @return
     */
    public String exec(String cmd, int waitTime) {
        Process process = null;
        InputStream in = null;
        String result = "";

        try {
            // exec cmd
            try {
                process = Runtime.getRuntime().exec(cmd);
                process.waitFor(waitTime, TimeUnit.MILLISECONDS);

            } catch (IOException | InterruptedException e) {
                LOG.error("exec command failed: " + cmd);
            }

            // get result
            in = process.getInputStream();
            Scanner scanner = new Scanner(in);
            while (scanner.hasNextLine()) {
                result = result + scanner.nextLine() + "\n";
            }
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            process.destroy();
        }

        return result;
    }

}

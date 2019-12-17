package com.wyett.agent.util;

import com.wyett.common.util.PackageUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/16 17:30
 * @description: TODO
 */

public class TaskConfRead {

    private static final String PKG = "com.wyett.common.dto";

    @Test
    public void test() throws ClassNotFoundException, IOException {
        ExecUtil eu = new ExecUtil();
        List<Map<String, String>> lmso = new ArrayList<>();

        List<Class<?>> lc = PackageUtil.getClassListInPackage(PKG, false, ".class");
        Map<String, Class<?>> ls = PackageUtil.convertClassToString(lc);

        lmso = eu.loadTask();

        for(Map<String, String> mso : lmso) {
            String cmd = eu.getCmd(mso);
            int internal = eu.getIntneralTime(mso);
            String resultJson = eu.getClassName(mso);

            // check if exist Object
            if (ls.keySet().stream().anyMatch(s -> (PKG + "." + resultJson).equals(s))) {
                Class<?> clazz = ls.get(PKG + "." + resultJson);
                Object c = ParseUtil.parseStringToObject(eu.exec(cmd, 5000), clazz);

                System.out.println(c);
            } else {
                ls.keySet().stream().forEach(System.out::println);
            }


        }

    }
}

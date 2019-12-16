package com.wyett.agent.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/16 17:30
 * @description: TODO
 */

public class TaskConf {
    @Test
    public void test() {
        ExecUtil eu = new ExecUtil();
        List<Map<String, Object>> lmso = new ArrayList<>();

        lmso = eu.loadTask();

        for(Map<String, Object> mso : lmso) {
            String cmd = eu.getCmd(mso);
            int internal = eu.getIntneralTime(mso);
            String resultJson = eu.getObjectClass(mso);

            String result = ParseUtil.parseStringToObject(eu.exec(cmd, 5000), resultJson);


        }

    }
}

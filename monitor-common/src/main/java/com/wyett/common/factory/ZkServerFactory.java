package com.wyett.common.factory;

import com.wyett.common.providor.ZkServerProvider;
import com.wyett.common.service.ZkServerService;
import com.wyett.common.service.impl.ZkServerServiceImpl;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/20 18:55
 * @description: TODO
 */

public class ZkServerFactory implements ZkServerProvider {
    @Override
    public ZkServerService create() {
        return new ZkServerServiceImpl();
    }
}

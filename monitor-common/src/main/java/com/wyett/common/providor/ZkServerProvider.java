package com.wyett.common.providor;

import com.wyett.common.service.ZkServerService;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/20 18:51
 * @description: TODO
 */

public interface ZkServerProvider {
    public ZkServerService create();
}

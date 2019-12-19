package com.wyett.registry.config;

import com.wyett.common.config.ReadConf;
import lombok.Data;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/19 17:20
 * @description: TODO
 */

@Data
public class ZkServerConfiguration {
    @ReadConf("{zk.servers}")
    private String zkServer;

    @ReadConf("{zk.root.path}")
    private String zkRootPath;

    @ReadConf("{zk.session.timeout")
    private int zkSessionTimeOut;

    @ReadConf("{zk.connection.timeout}")
    private int zkConnTimeOut;
}

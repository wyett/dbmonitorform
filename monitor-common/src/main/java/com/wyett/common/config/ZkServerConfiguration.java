package com.wyett.common.config;

import com.wyett.common.core.annotation.ReadConf;
import lombok.Data;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/19 17:20
 * @description: TODO
 */

public interface ZkServerConfiguration {
    @ReadConf(value = "zk.servers")
    String getZkServer();

    @ReadConf(value = "zk.root.path")
    String getZkRootPath();

    @ReadConf(value = "zk.session.timeout")
    int getZkSessionTimeOut();

    @ReadConf(value = "zk.connection.timeout")
    int getZkConnTimeOut();

    @ReadConf(value = "zk.connection.limit.size")
    int getZkConnLimitSize();

}

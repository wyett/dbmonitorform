package com.wyett.common.config;

import com.wyett.common.core.annotation.ReadConf;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/20 11:42
 * @description: TODO
 */

public class KafkaConfiguration {
    @ReadConf("{kafka.zk.configs}")
    private String kafkaZkConfig;

    @ReadConf("{kafka.zk.root.path}")
    private String kafkaRootPath;

    @ReadConf("{kafka.servers}")
    private String kafkaServers;

    @ReadConf("{kafka.zk.limit.size}")
    private String kafkaZkLimitSize;

    @ReadConf("{kafka.consumer.group.name}")
    private String kafkaConsumerGroup;
}

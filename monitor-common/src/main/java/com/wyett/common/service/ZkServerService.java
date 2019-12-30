package com.wyett.common.service;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/20 11:50
 * @description: TODO
 */

public interface ZkServerService {

    public void createRootNode();
    public void createServerNode(String serverName, JSON json);
    public void updateServerNode(String serverName, JSON json);
    public String getData(String path);
    public List<String> getClusterPathAsList(String path);
    public void initListener(String path);

}

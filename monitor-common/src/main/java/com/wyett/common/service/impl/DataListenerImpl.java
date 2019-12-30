package com.wyett.common.service.impl;

import org.I0Itec.zkclient.IZkDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/30 18:31
 * @description:
 */

public class DataListenerImpl implements IZkDataListener {

    private static final Logger LOG = LoggerFactory.getLogger(DataListenerImpl.class);


    @Override
    public void handleDataChange(String dataPath, Object data) throws Exception {
        LOG.info(dataPath + " content has changed " + data);
    }

    @Override
    public void handleDataDeleted(String dataPath) throws Exception {

    }
}

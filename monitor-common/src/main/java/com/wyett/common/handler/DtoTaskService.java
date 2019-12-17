package com.wyett.common.handler;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/17 18:17
 * @description: TODO
 */

public interface DtoTaskService {
    /**
     * get dto from
     * @param t
     * @param <T>
     * @return
     */
    public <T> T getDto(T t);
}

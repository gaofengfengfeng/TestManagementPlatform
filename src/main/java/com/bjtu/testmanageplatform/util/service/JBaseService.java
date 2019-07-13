package com.bjtu.testmanageplatform.util.service;

import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: gaofeng
 * @Date: 2019-07-13
 * @Description:
 */
@Data
public class JBaseService {
    private static ConcurrentHashMap<String, ConfObj> regConfs = new ConcurrentHashMap<>();

    private String conf;

    public JBaseService() {
    }

    protected static ConcurrentHashMap<String, ConfObj> getRegConfs() {
        return regConfs;
    }

    public static void registerConf(String conf, ConfObj confObj) {
        regConfs.put(conf, confObj);
    }

    public static class ConfObj {
        public String confName;
    }
}
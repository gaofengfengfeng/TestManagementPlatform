package com.bjtu.testmanageplatform;

import com.bjtu.testmanageplatform.util.JProperties;
import com.bjtu.testmanageplatform.util.service.JRedisPoolService;
import redis.clients.jedis.HostAndPort;

import java.util.HashSet;

/**
 * @Author: gaofeng
 * @Date: 2019-07-11
 * @Description: 项目启动前的初始话， 包括读取配置文件等
 */
public class InitConfig {

    public final static String REDIS_POOL = "redis_pool";
    public final static Integer ONE_DAY_EXPIRE = 24 * 60 * 60;
    public final static String LOGIN_TOKEN_PRE = "login_token_";

    public static void init() {
        // 注册单机redis服务
        JRedisPoolService.ConfObj redisPoolConfObj = new JRedisPoolService.ConfObj();
        redisPoolConfObj.confName = REDIS_POOL;
        redisPoolConfObj.nodes = new HashSet<HostAndPort>();
        String redisPort = JProperties.getValueByKey("redis", "redis.port");
        String redisHost = JProperties.getValueByKey("redis", "redis.host");
        redisPoolConfObj.nodes.add(new HostAndPort(redisHost, Integer.parseInt(redisPort)));
        JRedisPoolService.registerConf(REDIS_POOL, redisPoolConfObj);

        // 读取配置文件中的常量值
        Const.privateKey = JProperties.getValueByKey("conf", "privateKey");
        Const.publicKey = JProperties.getValueByKey("conf", "publicKey");
        Const.aesKey = JProperties.getValueByKey("conf", "aesKey");
    }

    public static class Const {
        public static String privateKey = "";
        public static String publicKey = "";
        public static String aesKey = "";
    }

}

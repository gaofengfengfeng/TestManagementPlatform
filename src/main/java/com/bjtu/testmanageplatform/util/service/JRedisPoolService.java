package com.bjtu.testmanageplatform.util.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: gaofeng
 * @Date: 2019-07-13
 * @Description:
 */
@Slf4j
public class JRedisPoolService extends JBaseService implements MethodInterceptor {
    private static ConcurrentHashMap<String, JRedisPoolService> instancePool =
            new ConcurrentHashMap<>();

    private List<JedisPool> jedisPools;

    private JRedisPoolService(String conf) {
        setConf(conf);

        ConfObj confObj = (JRedisPoolService.ConfObj) getRegConfs().get(conf);
        jedisPools = new ArrayList<>();
        Set<HostAndPort> nodes = confObj.nodes;

        for (HostAndPort node : nodes) {
            JedisPoolConfig jpc = new JedisPoolConfig();

            // JedisPool jedisPool = new JedisPool(jpc, node.getHost(), node.getPort());
            // jedisPools.add(proxyShell);

            Class[] argumentTypes = new Class[]{GenericObjectPoolConfig.class, String.class,
                    int.class};
            Object[] arguments = new Object[]{jpc, node.getHost(), node.getPort()};

            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(JedisPool.class);
            enhancer.setCallback(this);

            JedisPool proxyShell = (JedisPool) enhancer.create(argumentTypes, arguments);
            jedisPools.add(proxyShell);
        }
    }


    public static JedisPool getJedisPool(String conf) {
        JRedisPoolService instance;
        if (instancePool.containsKey(conf)) {
            instance = instancePool.get(conf);
        } else {
            synchronized (JRedisPoolService.class) {
                if (instancePool.containsKey(conf)) {
                    instance = instancePool.get(conf);
                } else {
                    instance = new JRedisPoolService(conf);
                    instancePool.put(conf, instance);
                }
            }
        }
        return instance.jedisPools.get(0);
    }

    public static Jedis getInstance(String conf) {
        return getJedisPool(conf).getResource();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        log.debug("Call JedisPool Method: " + method.getName());
        return methodProxy.invokeSuper(obj, args);
    }

    public static class ConfObj extends JBaseService.ConfObj {
        public Set<HostAndPort> nodes;
    }
}

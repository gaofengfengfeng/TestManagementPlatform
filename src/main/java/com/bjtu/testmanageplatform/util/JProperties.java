package com.bjtu.testmanageplatform.util;

import java.io.*;
import java.util.Properties;

/**
 * @Author: gaofeng
 * @Date: 2019-07-11
 * @Description: 配置读取文件
 */
public class JProperties {

    /**
     * 根据配置文件路径以及key值找到对应的value
     *
     * @param path
     * @param key
     *
     * @return
     */
    private static String parsePropertier(String path, String key) {
        File file = new File(path);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException ioE) {
            ioE.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties.getProperty(key);
    }

    /**
     * 获取当前的运行环境  dev、test、prod
     *
     * @return
     */
    public static String activeEnv() {
        String path = "src/main/resources/application.properties";
        String key = "spring.profiles.active";
        return parsePropertier(path, key);
    }

    /**
     * 通过key值取得对应的conf文件内的value值
     * 本项目中相关的配置均存放在src/main/resources下，文件名为"conf-" + 运行环境 + ".properties"
     *
     * @param key
     *
     * @return
     */
    public static String getValueByKey(String prefix, String key) {
        String confPath = "src/main/resources/";
        StringBuilder sb = new StringBuilder();
        sb.append(confPath).append(prefix).append("-").append(activeEnv()).append(".properties");
        return parsePropertier(sb.toString(), key);
    }
}

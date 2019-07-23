package com.bjtu.testmanageplatform.util;

import com.alibaba.fastjson.JSON;
import com.bjtu.testmanageplatform.InitConfig;
import com.bjtu.testmanageplatform.beans.base.TokenObject;
import lombok.Data;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @Author: gaofeng
 * @Date: 2019-07-09
 * @Description: 生成工具
 */
public class Generator {

    /**
     * 生成Long类型的id，生成规则： 当前毫秒级时间+三位随机数，生成一个16位数的随机id
     *
     * @return
     */
    public static Long generateLongId() {
        String prefix = String.valueOf(System.currentTimeMillis());
        String suffix = String.valueOf((int) (Math.random() * 1000));
        return Long.valueOf(prefix + suffix);
    }

    /**
     * 生成盐
     *
     * @return
     */
    public static String createSalt() {
        byte[] salt = new byte[16];
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.nextBytes(salt);
            return salt.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * 生成token
     *
     * @param username
     * @param UserId
     *
     * @return
     */
    public static String generateToken(String username, Long UserId, String phone) {
        // 构建一个实体对象
        TokenObject tokenObject = new TokenObject();
        tokenObject.setUsername(username);
        tokenObject.setUserId(UserId);
        tokenObject.setPhone(phone);

        // 实体对象转json字符串，生成token
        String jsonStr = JSON.toJSONString(tokenObject);
        return AESUtil.encrypt(InitConfig.Const.aesKey, jsonStr);
    }

    /**
     * 解析token
     *
     * @param token
     *
     * @return
     */
    public static TokenObject parseToken(String token) {
        String jsonStr = AESUtil.decrypt(InitConfig.Const.aesKey, token);
        return JSON.parseObject(jsonStr, TokenObject.class);
    }
}

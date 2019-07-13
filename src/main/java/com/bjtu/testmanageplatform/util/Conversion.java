package com.bjtu.testmanageplatform.util;


import java.math.BigInteger;
import java.security.MessageDigest;
import org.apache.commons.codec.binary.Base64;

/**
 * @Author: gaofeng
 * @Date: 2019-07-09
 * @Description: 转换工具
 */
public class Conversion {

    /**
     * BASE64字符串解码为二进制数据
     *
     * @param base64
     *
     * @return
     *
     * @throws Exception
     */
    public static byte[] Base64Decode(String base64){
        return Base64.decodeBase64(base64);
    }

    /**
     * 二进制数据编码为BASE64字符串
     *
     * @param bytes
     *
     * @return
     *
     * @throws Exception
     */
    public static String Base64Encode(byte[] bytes){
        return Base64.encodeBase64String(bytes);
    }


    /**
     * @param str
     *
     * @return
     */
    public static String getMD5(String str) {
        return getMD5(str.getBytes());
    }


    /**
     * @param bytes
     *
     * @return
     */
    public static String getMD5(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            BigInteger bigInteger = new BigInteger(1, md.digest());
            String strMd5 = bigInteger.toString(16);

            if (strMd5.length() < 32) {
                int size = 32 - strMd5.length();
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < size; i++) {
                    sb.append("0");
                }
                strMd5 = sb.toString() + strMd5;
            }
            return strMd5;
        } catch (Exception e) {
            return null;
        }
    }
}

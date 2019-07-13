package com.bjtu.testmanageplatform.util;


import lombok.extern.slf4j.Slf4j;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Author: gaofeng
 * @Date: 2019-07-13
 * @Description:
 */
@Slf4j
public class AESUtil {

    private static final String ALG = "AES";
    private static final String ENC = "UTF-8";
    private static final String SEC_NORMALIZE_ALG = "MD5";

    /**
     * 加密
     *
     * @param secret
     * @param plainBytes
     *
     * @return
     *
     * @throws Exception
     */
    public static byte[] doEncrypt(String secret, byte[] plainBytes) throws Exception {
        MessageDigest dig = MessageDigest.getInstance(SEC_NORMALIZE_ALG);
        byte[] key = dig.digest(secret.getBytes(ENC));
        SecretKeySpec secKey = new SecretKeySpec(key, ALG);

        Cipher aesCipher = Cipher.getInstance(ALG);

        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] cipherBytes = aesCipher.doFinal(plainBytes);
        return cipherBytes;
    }

    /**
     * 加密
     *
     * @param secret
     * @param plainText
     *
     * @return
     */
    public static String encrypt(String secret, String plainText) {
        String cipherText = null;
        try {
            byte[] cipherBytes = doEncrypt(secret, plainText.getBytes(ENC));
            cipherText = Base64.getEncoder().encodeToString(cipherBytes);
            cipherText = URLEncoder.encode(cipherText, ENC);
        } catch (Exception e) {
            log.warn("encrypt failed errorMsg=" + e.getMessage());
        }

        return cipherText;
    }

    /**
     * 解密
     *
     * @param secret
     * @param cipherBytes
     *
     * @return
     *
     * @throws Exception
     */
    public static byte[] doDecrypt(String secret, byte[] cipherBytes) throws Exception {
        MessageDigest dig = MessageDigest.getInstance(SEC_NORMALIZE_ALG);
        byte[] key = dig.digest(secret.getBytes(ENC));
        SecretKeySpec secKey = new SecretKeySpec(key, ALG);

        Cipher aesCipher = Cipher.getInstance(ALG);

        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] plainBytes = aesCipher.doFinal(cipherBytes);
        return plainBytes;
    }

    /**
     * 解密
     *
     * @param secret
     * @param cipherText
     *
     * @return
     */
    public static String decrypt(String secret, String cipherText) {
        String plainText = null;
        try {
            cipherText = cipherText.replace(' ', '+');

            int i = 5;
            while (cipherText.contains("%") && i-- > 0) {
                cipherText = URLDecoder.decode(cipherText, ENC);
            }
            log.info("URLDecoder.decode cipherText=" + cipherText);

            byte[] cipherBytes = Base64.getDecoder().decode(cipherText.getBytes());
            byte[] plainBytes = doDecrypt(secret, cipherBytes);
            plainText = new String(plainBytes, ENC);
        } catch (Exception e) {
            log.warn("decrypt failed errorMsg=" + e.getMessage());
        }

        return plainText;
    }


    public static void main(String[] args) {
        String encryptedStr = encrypt("abc", "gaofeng");
        System.out.println(encryptedStr);
        System.out.println(decrypt("abc", encryptedStr));
    }
}

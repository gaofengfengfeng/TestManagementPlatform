package com.bjtu.testmanageplatform.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @Author: gaofeng
 * @Date: 2019-07-11
 * @Description:
 */
public class ConversionTest {
    @Test
    public void getMD5(){
        //System.out.println(Conversion.getMD5("[B@6fdb1f78"));
        String salt = Generator.createSalt();
        Long timestamp = System.currentTimeMillis();
        String strBeforeMd5 = salt + timestamp;
        System.out.println(Conversion.getMD5(strBeforeMd5));
    }
}
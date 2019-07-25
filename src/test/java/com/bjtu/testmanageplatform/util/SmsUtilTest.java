package com.bjtu.testmanageplatform.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @Author: gaofeng
 * @Date: 2019-07-24
 * @Description:
 */
public class SmsUtilTest {

    @Test
    public void sendMsg() {
        SmsUtil.singleSendMsg("17801020789", 377481, "北京交通大学", "铁路测试项目");
    }
}
package com.bjtu.testmanageplatform.util;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;

/**
 * @Author: gaofeng
 * @Date: 2019-07-24
 * @Description: 短信服务
 */
public class SmsUtil {
    private static final int APP_ID = 1400232894;
    private static final String APP_KEY = "53a809a3a78cf81acba16bb262b0e4da";
    private static final String SMS_SIGN = "文案助理";

    public static boolean singleSendMsg(String phone, Integer templateId, String departmentName,
                                  String projectName) {
        String[] params = {departmentName, projectName};
        SmsSingleSender singleSender = new SmsSingleSender(APP_ID, APP_KEY);
        SmsSingleSenderResult result;
        try {
            result = singleSender.sendWithParam("86", phone, templateId, params, SMS_SIGN, "",
                    "");
        } catch (Exception e) {
            JLog.error("send msg exception phone=" + phone, 100022145);
            e.printStackTrace();
            return false;
        }
        if (result.result == 0) {
            JLog.info("send msg success phone=" + phone);
            return true;
        } else {
            JLog.error("send msg failed phone=" + phone + " errMsg=" + result.errMsg, 100022149);
            return false;
        }
    }
}

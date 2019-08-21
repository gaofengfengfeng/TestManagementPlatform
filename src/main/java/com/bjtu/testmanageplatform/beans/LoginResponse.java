package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JResponse;
import lombok.Data;

/**
 * @Author: gaofeng
 * @Date: 2019-07-13
 * @Description:
 */
@Data
public class LoginResponse extends JResponse {

    private LoginResData data;

    @Data
    public static class LoginResData {
        private int role;
        private String token;
    }
}

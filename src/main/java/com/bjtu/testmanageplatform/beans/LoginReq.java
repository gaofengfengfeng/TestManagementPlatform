package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: gaofeng
 * @Date: 2019-07-04
 * @Description: 登录请求实体类
 */
@Data
public class LoginReq extends JRequest {

    private LoginData data;

    @Data
    public static class LoginData {
        @NotNull
        private String username;
        @NotNull
        private String password;
    }
}

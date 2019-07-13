package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JRequest;
import lombok.Data;

/**
 * @Author: gaofeng
 * @Date: 2019-07-09
 * @Description: 分配用户账号请求实体
 */
@Data
public class AssignUserReq extends JRequest {

    private AssignUserData data;

    @Data
    public static class AssignUserData {
        private String username;
        private String password;
        private String name;
        private String phone;
        private String department;
        private Integer role;
    }
}

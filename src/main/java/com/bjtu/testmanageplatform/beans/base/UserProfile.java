package com.bjtu.testmanageplatform.beans.base;

import lombok.Data;

/**
 * @Author: gaofeng
 * @Date: 2019-07-04
 * @Description: 用户信息
 */
@Data
public class UserProfile {
    private Long user_id;
    private String username;
    private String phone;
}

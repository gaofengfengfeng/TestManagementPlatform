package com.bjtu.testmanageplatform.beans.base;

import lombok.Data;

/**
 * @Author: gaofeng
 * @Date: 2019-07-04
 * @Description: 定义通用请求参数
 */
@Data
public class JRequest {
    private Long request_time;
    private String token;
    private UserProfile user_profile;
}

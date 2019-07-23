package com.bjtu.testmanageplatform.beans.base;

import lombok.Data;

/**
 * @Author: gaofeng
 * @Date: 2019-07-13
 * @Description:
 */
@Data
public class TokenObject {
    private String username;
    private Long userId;
    private String phone;
    private Long loginTime = System.currentTimeMillis();
}

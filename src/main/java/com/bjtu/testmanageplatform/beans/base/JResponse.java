package com.bjtu.testmanageplatform.beans.base;

import lombok.Data;

/**
 * @Author: gaofeng
 * @Date: 2019-07-04
 * @Description: 通用响应参数定义
 */
@Data
public class JResponse {
    private Integer err_no = 0;
    private String err_msg = "success";
    private Long response_time;
    private Object data = null;
}

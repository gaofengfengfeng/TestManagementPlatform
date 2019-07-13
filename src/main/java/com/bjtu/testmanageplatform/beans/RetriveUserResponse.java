package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JResponse;
import lombok.Data;

import java.util.List;

/**
 * @Author: gaofeng
 * @Date: 2019-07-13
 * @Description:
 */
@Data
public class RetriveUserResponse extends JResponse {

    private List<RetriveUserResData> data;

    @Data
    public static class RetriveUserResData {
        private Long user_id;
        private String name;
        private String phone;
        private String department;
        private String portrait_url;
    }
}

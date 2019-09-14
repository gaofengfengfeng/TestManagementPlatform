package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JRequest;
import lombok.Data;

/**
 * @Author: gaofeng
 * @Date: 2019-07-15
 * @Description:
 */
@Data
public class CreateStandardReq extends JRequest {

    private CreateStandardData data;

    @Data
    public static class CreateStandardData {
        private Integer standard_rank = 0;
        private String headline = "";
        private Integer headline_rank = 0;
        private String secondary_headline = "";
        private String third_headline = "";
        private Integer secondary_headline_rank = 0;
        private String third_headline_rank = "";
        private String name = "";
        private String name_rank = "";
        private String rank = "";
        private String content = "";
    }
}

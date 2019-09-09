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
        private Integer standard_rank;
        private String headline;
        private Integer headline_rank;
        private String secondary_headline;
        private String third_headline;
        private Integer secondary_headline_rank;
        private Integer third_headline_rank;
        private String name;
        private Integer name_rank;
        private String rank;
        private String content;
    }
}

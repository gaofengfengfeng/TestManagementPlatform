package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JResponse;
import lombok.Data;

/**
 * @Author: gaofeng
 * @Date: 2019-07-23
 * @Description:
 */
@Data
public class TemplateResponse extends JResponse {

    private TemplateResData data;

    @Data
    public static class TemplateResData {
        private String content;
        private String test_leader_name;
        private Long test_leader_id;
        private String under_test_leader_name;
        private Long under_test_leader_id;
        private Integer status;
        private String project_location;
        private String rank;
        private Integer type;
        private String name;
    }

}

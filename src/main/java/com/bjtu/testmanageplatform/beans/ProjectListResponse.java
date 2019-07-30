package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JResponse;
import lombok.Data;

import java.util.List;

/**
 * @Author: gaofeng
 * @Date: 2019-07-20
 * @Description:
 */
@Data
public class ProjectListResponse extends JResponse {

    private List<Project> data;

    @Data
    public static class Project {
        private Long project_id;
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

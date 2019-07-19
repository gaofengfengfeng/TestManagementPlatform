package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JRequest;
import lombok.Data;

/**
 * @Author: gaofeng
 * @Date: 2019-07-18
 * @Description:
 */
@Data
public class CreateProjectReq extends JRequest {

    private CreateProjectData data;

    @Data
    public static class CreateProjectData {
        private String name;
        private String project_location;
        private String rank;
        private Integer type;
    }
}

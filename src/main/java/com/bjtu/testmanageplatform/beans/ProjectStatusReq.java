package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JRequest;
import lombok.Data;

/**
 * @Author: gaofeng
 * @Date: 2019-07-19
 * @Description:
 */
@Data
public class ProjectStatusReq extends JRequest {

    private ProjectStatusData data;

    @Data
    public static class ProjectStatusData {
        private Long project_id;
        private Integer status;
    }
}

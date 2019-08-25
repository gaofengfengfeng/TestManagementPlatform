package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JRequest;
import lombok.Data;

import java.util.List;

/**
 * @Author: gaofeng
 * @Date: 2019-07-19
 * @Description:
 */
@Data
public class AssignTesterReq extends JRequest {

    private AssignTesterData data;

    @Data
    public static class AssignTesterData {
        private Long project_id;
        private List<Long> testers;
    }
}

package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JResponse;
import lombok.Data;

/**
 * @Author: gaofeng
 * @Date: 2019-07-18
 * @Description:
 */
@Data
public class CreateProjectResponse extends JResponse {

    private CreateProjectResData data;

    @Data
    public static class CreateProjectResData {
        private Long project_id;
    }
}

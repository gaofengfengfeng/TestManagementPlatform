package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JRequest;
import lombok.Data;

/**
 * @Author: gaofeng
 * @Date: 2019-07-23
 * @Description: 项目报告模板请求
 */
@Data
public class TemplateReq extends JRequest {

    private TemplateData data;

    @Data
    public class TemplateData {
        private Long project_id;
    }
}

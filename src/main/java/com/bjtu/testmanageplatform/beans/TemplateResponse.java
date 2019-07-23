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
    }

}

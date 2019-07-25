package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JRequest;
import com.bjtu.testmanageplatform.beans.base.JResponse;
import lombok.Data;

/**
 * 7-23
 */
@Data
public class MaterialUploadReq extends JRequest {

    private MaterialUploadData data;

    @Data
    public static class MaterialUploadData{
        private Long project_id;
        private Integer type;
        private String remark;
        private String file_url;
        private String content;
    }
}

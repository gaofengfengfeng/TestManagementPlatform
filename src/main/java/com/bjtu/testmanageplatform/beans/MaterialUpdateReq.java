package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JRequest;
import lombok.Data;
import lombok.NonNull;


@Data
public class MaterialUpdateReq extends JRequest {

    private MaterialUpdateData data;

    @Data
    public static class MaterialUpdateData{
        private Long project_id;
        private Long material_id;
        private Integer type;
        private String remark;
        private String file_url;
        private String content;
    }
}

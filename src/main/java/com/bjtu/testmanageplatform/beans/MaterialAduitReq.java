package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JRequest;
import lombok.Data;
@Data
public class MaterialAduitReq extends JRequest {

    private MaterialAduitData data;

    @Data
    public static class MaterialAduitData{
        private Long material_id;
        private Integer result;
        private String discussion;

    }
}

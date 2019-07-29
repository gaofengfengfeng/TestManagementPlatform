package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JRequest;
import lombok.Data;


/**
 * 7-23
 */
@Data
public class MaterialListReq extends JRequest {

    private MaterialListData data;

    @Data
    public static class MaterialListData{
        private Long project_id;

    }
}

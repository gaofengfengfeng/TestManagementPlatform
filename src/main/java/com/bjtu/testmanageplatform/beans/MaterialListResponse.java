package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JResponse;
import lombok.Data;
import java.util.List;

@Data
public class MaterialListResponse extends JResponse {

    private List<Material> data;

    @Data
    public static class Material {
        private Long material_id;
        private Integer type;
        private Integer status;
        private String remark;
        private String content;
    }
}

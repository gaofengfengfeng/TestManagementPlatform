package com.bjtu.testmanageplatform.model;

import lombok.Data;

/**
 * @Author: gaofeng
 * @Date: 2019-07-07
 * @Description: 提测项目子任务材料
 */
@Data
public class ProjectMaterial {
    private Long material_id;
    private Long project_id;
    private Long committer_id;
    private Integer type;
    private Integer status;
    private Integer audit_status;
    private Integer version;
    private String remark;
    private String discussion;
    private String file_url;
    private String content;
    private Long create_time;

    public ProjectMaterial() {
        material_id = 0L;
        project_id = 0L;
        committer_id = 0L;
        type = Type.UNUSE;
        status = TestProject.Status.UNUSE;
        audit_status = Audit.UNUSE;
        version = 0;
        remark = "";
        discussion = "";
        file_url = "";
        content = "";
        create_time = System.currentTimeMillis();
    }

    /**
     * 子任务类
     */
    public static class Type {
        public final static Integer UNUSE = 0;
        public final static Integer FILING_MATERIAL = 1; // 备案资料
        public final static Integer OPINIONS_ON_RECTIFICATION_OF_MPS = 2; // 公安部整改意见
        public final static Integer GRADE_AUDIT_MATERIAL = 3; // 定级审核材料
        public final static Integer ASSESSMENT_REPORT = 4; // 测评报告
        public final static Integer PLAN_OF_RECTIFICATION = 5; // 整改方案
    }

    public static class Audit{
        public final static Integer UNUSE = 0;
        public final static Integer NOPASS = 1;
        public final static Integer PASS = 2;
    }
}

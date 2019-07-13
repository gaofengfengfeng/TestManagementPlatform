package com.bjtu.testmanageplatform.model;

import lombok.Data;

/**
 * @Author: gaofeng
 * @Date: 2019-07-07
 * @Description: 提测项目
 */
@Data
public class TestProject {

    private Long project_id;
    private Long test_leader_id;
    private Long under_test_leader_id;
    private Integer status;
    private Integer project_location_code;
    private String project_location;
    private String rank;
    private Integer type;
    private Long create_time;

    public TestProject() {
        project_id = 0L;
        test_leader_id = 0L;
        under_test_leader_id = 0L;
        status = Status.UNUSE;
        project_location_code = 0;
        project_location = "";
        rank = "";
        type = Type.UNUSE;
        create_time = System.currentTimeMillis();
    }

    /**
     * 项目进展状态
     */
    public static class Status {
        public final static Integer UNUSE = 0;
        public final static Integer GRADE_MATERIAL_AUDIT_ING = 1; // 定级材料审核中
        public final static Integer GRADE_MATERIAL_AUDIT_PASSED = 2; // 定级材料审核完成
        public final static Integer GRADE_MATERIAL_AUDIT_FAILED = 3; // 定级材料审核失败
        public final static Integer ON_FILE = 4; // 备案中
        public final static Integer FILING_PASSES = 5; // 备案成功
        public final static Integer FILING_FAILED = 6; // 备案失败
        public final static Integer WATING_FOR_CONFIRM = 7; // 测试待确认
        public final static Integer TESTING = 8; // 测试中
        public final static Integer REJECT_FOR_RECTIFICATION = 9; // 驳回整改
        public final static Integer TEST_PASSED = 10; // 测试通过
        public final static Integer PROJECT_CANCELED = 11; // 项目取消
    }

    /**
     * 项目类型
     */
    public static class Type {
        public final static Integer UNUSE = 0;
        public final static Integer LEVEL_PROTECTION_TEST = 1; // 等保测试
        public final static Integer RISK_ASSESSMENT_TEST = 2; // 风险评估测试
    }
}

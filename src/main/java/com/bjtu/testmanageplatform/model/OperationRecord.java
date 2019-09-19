package com.bjtu.testmanageplatform.model;

import lombok.Data;

/**
 * @Author: gaofeng
 * @Date: 2019-09-19
 * @Description:
 */
@Data
public class OperationRecord {
    private Long record_id;
    private Long project_id;
    private Long operator_id;
    private String operator;
    private Integer operate_type;
    private String tester;
    private Integer audit_type;
    private Integer audit_result;
    private Long create_time;

    public OperationRecord() {
        record_id = 0L;
        project_id = 0L;
        operator_id = 0L;
        operator = "";
        operate_type = 0;
        tester = "";
        audit_type = 0;
        audit_result = 0;
        create_time = System.currentTimeMillis();
    }


    public static class OperationType {
        public static final Integer UNUSE = 0;
        public static final Integer CREATE_PROJECT = 1;
        public static final Integer ASSIGN_TESTER = 2;
        public static final Integer UPLOAD_MATERIAL = 3;
        public static final Integer MATERIAL_AUDIT = 4;
    }
}

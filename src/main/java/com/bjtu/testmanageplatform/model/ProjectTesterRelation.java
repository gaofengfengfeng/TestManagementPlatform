package com.bjtu.testmanageplatform.model;

import lombok.Data;

import javax.validation.Valid;

/**
 * @Author: gaofeng
 * @Date: 2019-07-07
 * @Description: 项目与具体测试者关系
 */
@Data
public class ProjectTesterRelation {
    private Long relation_id;
    private Long project_id;
    private Long tester_id;
    private Long create_time;

    public ProjectTesterRelation() {
        relation_id = 0L;
        project_id = 0L;
        tester_id = 0L;
        create_time = System.currentTimeMillis();
    }
}

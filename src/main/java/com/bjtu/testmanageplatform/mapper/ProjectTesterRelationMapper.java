package com.bjtu.testmanageplatform.mapper;

import com.bjtu.testmanageplatform.model.ProjectTesterRelation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Author: gaofeng
 * @Date: 2019-07-19
 * @Description:
 */
@Component
@Mapper
public interface ProjectTesterRelationMapper {

    @Insert("INSERT INTO project_tester_relation(relation_id, project_id, tester_id, create_time)" +
            " VALUES(#{relation_id}, #{project_id}, #{tester_id}, #{create_time})")
    Integer insert(ProjectTesterRelation projectTesterRelation);
}

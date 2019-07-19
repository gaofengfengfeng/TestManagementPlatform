package com.bjtu.testmanageplatform.mapper;

import com.bjtu.testmanageplatform.model.TestProject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @Author: gaofeng
 * @Date: 2019-07-18
 * @Description:
 */
@Component
@Mapper
public interface TestProjectMapper {

    @Insert("INSERT INTO test_project(project_id, name, test_leader_id, under_test_leader_id, " +
            "status, project_location_code, project_location, rank, type, create_time) VALUES" +
            "(#{project_id}, #{name}, #{test_leader_id}, #{under_test_leader_id}, #{status}, " +
            "#{project_location_code}, #{project_location}, #{rank}, #{type}, #{create_time})")
    Integer insert(TestProject testProject);


    @Select("SELECT * FROM test_project WHERE name=#{name}")
    TestProject selectByName(String name);

}

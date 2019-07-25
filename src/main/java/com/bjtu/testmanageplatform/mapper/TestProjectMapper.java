package com.bjtu.testmanageplatform.mapper;

import com.bjtu.testmanageplatform.model.TestProject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

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

    @Select("SELECT * FROM test_project WHERE project_id=#{projectId}")
    TestProject selectByProjectId(Long projectId);

    @Update("UPDATE test_project SET status=#{1} WHERE project_id=#{0}")
    Integer updateStatusByProjectId(Long projectId, Integer status);

    @Select("SELECT * FROM test_project WHERE test_leader_id=#{testLeaderId}")
    List<TestProject> selectByTestLeaderId(Long testLeaderId);

    @Select("SELECT * FROM test_project WHERE under_test_leader_id=#{underTestLeaderId}")
    List<TestProject> selectByUnderTestLeaderId(Long underTestLeaderId);
}

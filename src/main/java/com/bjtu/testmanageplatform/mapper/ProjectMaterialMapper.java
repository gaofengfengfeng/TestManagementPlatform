package com.bjtu.testmanageplatform.mapper;

import com.bjtu.testmanageplatform.beans.MaterialUpdateReq;
import com.bjtu.testmanageplatform.model.ProjectMaterial;
import com.bjtu.testmanageplatform.model.TestProject;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface ProjectMaterialMapper {

    @Select("SELECT count(*) FROM project_material WHERE project_id=#{0} and type=#{1}")
    Integer selectByIdType(Long projectId , Integer type);

    @Insert("INSERT INTO project_material(material_id, project_id, committer_id, type, " +
            "status, audit_status, version, remark, discussion, file_url, content, create_time) VALUES" +
            "(#{material_id}, #{project_id}, #{committer_id}, #{type}, #{status}, " +
            "#{audit_status}, #{version}, #{remark}, #{discussion}, #{file_url}, #{content}, #{create_time})")
    Integer insert(ProjectMaterial projectMaterial);

    @Select("SELECT * FROM project_material where project_id=#{projectId}")
    List<ProjectMaterial> selectByProjectId(Long projectId);

    @Update("<script>UPDATE project_material " +
            "<set> " +
            "<if test='remark!= null'>remark=#{remark},</if>"+
            "<if test='file_url!= null'>file_url=#{file_url},</if>"+
            "<if test='content!= null'>content=#{content}</if>"+
            "</set> " +
            "WHERE material_id=#{0}</script>")
    Integer update(Long materialId , @Param("remark") String remark,@Param("file_url") String file_url ,@Param("content") String content);

    @Select("SELECT project_id FROM project_material where material_id=#{materialId}")
    Long selectPidByMid(Long materialId);

    @Select("SELECT type FROM project_material where material_id=#{materialId}")
    Integer selectTypeByMid(Long materialId);

    @Select("SELECT * FROM project_material where material_id=#{materialId}")
    ProjectMaterial selectMaterialById(Long material);

    @Select("SELECT * FROM test_project where project_id=#{projectId}")
    TestProject selectProjectById(Long projectId);

    @Update("UPDATE project_material SET audit_status=#{1} , discussion=#{2} where material_id=#{0}")
    Integer updateMaterial(Long materialId, Integer auditStatus, String discussion);

    @Update("UPDATE project_material SET audit_status=#{1} where material_id=#{0}")
    Integer updateMaterialStatus(Long materialId, Integer auditStatus);
}

package com.bjtu.testmanageplatform.service;

import com.bjtu.testmanageplatform.InitConfig;
import com.bjtu.testmanageplatform.beans.MaterialListResponse;
import com.bjtu.testmanageplatform.mapper.ProjectMaterialMapper;
import com.bjtu.testmanageplatform.mapper.UserMapper;
import com.bjtu.testmanageplatform.model.ProjectMaterial;
import com.bjtu.testmanageplatform.model.TestProject;
import com.bjtu.testmanageplatform.model.User;
import com.bjtu.testmanageplatform.util.Generator;
import com.bjtu.testmanageplatform.util.JLog;
import com.bjtu.testmanageplatform.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaterialService {

    private ProjectMaterialMapper projectMaterialMapper;
    private UserMapper userMapper;
    private StateMachineService stateMachineService;
    private TestProjectService testProjectService;

    @Autowired
    public MaterialService(ProjectMaterialMapper projectMaterialMapper, StateMachineService stateMachineService,
                           TestProjectService testProjectService, UserMapper userMapper) {
        this.projectMaterialMapper = projectMaterialMapper;
        this.stateMachineService = stateMachineService;
        this.testProjectService = testProjectService;
        this.userMapper = userMapper;
    }

    public ProjectMaterial getMaterialById(Long materialId) {
        return projectMaterialMapper.selectMaterialById(materialId);
    }

    public Integer getVersionByIdType(Long projectId, Integer type) {
        return projectMaterialMapper.selectByIdType(projectId, type);
    }

    public Integer updateMaterialAudit(Long material_id, Integer audit_status, String discussion) {
        return projectMaterialMapper.updateMaterial(material_id, audit_status, discussion);
    }

    public int upload(ProjectMaterial projectMaterial) {
        JLog.info(String.format("enter upload material projectid=%s type=%s"
                , projectMaterial.getProject_id(), projectMaterial.getType()));

        projectMaterial.setMaterial_id(Generator.generateLongId());
        Integer result = projectMaterialMapper.insert(projectMaterial);
        return result == 1 ? 1 : 0;
    }

    public List<MaterialListResponse.Material> getMaterialByProjectId(Long projectId) {
        JLog.info(String.format("enter getMaterialByProjectId projectid=%s", projectId));
        List<ProjectMaterial> materials = projectMaterialMapper.selectByProjectId(projectId);
        TestProject project = testProjectService.selectByProjectId(projectId);
        List<MaterialListResponse.Material> materialList = new ArrayList<>();

        for (ProjectMaterial projectMaterial : materials) {
            MaterialListResponse.Material material = new MaterialListResponse.Material();
            material.setMaterial_id(projectMaterial.getMaterial_id());
            material.setType(projectMaterial.getType());
            material.setStatus(projectMaterial.getAudit_status());
            material.setContent(projectMaterial.getContent());
            material.setRemark(projectMaterial.getRemark());
            material.setFile_url(projectMaterial.getFile_url());
            materialList.add(material);
        }

        return materialList;
    }

    public Boolean judgeContains(String tagetStringA, String tagetStringB) {
        if (tagetStringA == null && tagetStringB == null)
            return true;
        else
            return false;
    }

    public Boolean update(Long material_id, String remark, String file_url, String content) {
        if (remark.length() == 0)
            remark = null;
        JLog.info(String.format("update materialid=%s", material_id));
        Integer result = projectMaterialMapper.update(material_id, remark, file_url, content);
        return (result == 1 ? true : false);
    }

    public Boolean judgeMatch(Long material_id, Long project_id, Integer type) {
        if (projectMaterialMapper.selectPidByMid(material_id) == project_id
                && projectMaterialMapper.selectTypeByMid(material_id) == type)
            return true;
        else
            return false;
    }

    public Boolean judgeCanAudit(Integer type) {
        if (type != ProjectMaterial.Type.FILING_MATERIAL && type != ProjectMaterial.Type.GRADE_AUDIT_MATERIAL
                && type != ProjectMaterial.Type.ASSESSMENT_REPORT)
            return false;
        else
            return true;
    }

    public Boolean audit(Long material_id, Integer type, Integer audit_status, String discussion) {
        Boolean result = false;
        Integer resultA;
        Long project_id = projectMaterialMapper.selectPidByMid(material_id);
        TestProject testProject = projectMaterialMapper.selectProjectById(project_id);
        Long underTestLeaderId = testProject.getUnder_test_leader_id();
        String testProjectName = testProject.getName();
        JLog.info(String.format("change projectid=%s by materialid=%s", testProject.getProject_id(), material_id));

        if (type == ProjectMaterial.Type.GRADE_AUDIT_MATERIAL) {
            if (audit_status == ProjectMaterial.Audit.NOPASS)
                result = testProjectService.changeProjectStatus(testProject, TestProject.Status.GRADE_MATERIAL_AUDIT_FAILED);
            else
                result = testProjectService.changeProjectStatus(testProject, TestProject.Status.GRADE_MATERIAL_AUDIT_PASSED);
        } else if (type == ProjectMaterial.Type.FILING_MATERIAL) {
            if (audit_status == ProjectMaterial.Audit.NOPASS)
                result = testProjectService.changeProjectStatus(testProject, TestProject.Status.FILING_FAILED);
            else
                result = testProjectService.changeProjectStatus(testProject, TestProject.Status.FILING_PASSES);
        } else if (type == ProjectMaterial.Type.ASSESSMENT_REPORT) {
            if (audit_status == ProjectMaterial.Audit.NOPASS)
                result = testProjectService.changeProjectStatus(testProject, TestProject.Status.REJECT_FOR_RECTIFICATION);
            else
                result = testProjectService.changeProjectStatus(testProject, TestProject.Status.TEST_PASSED);
        }

        if (result == true) {
            resultA = updateMaterialAudit(material_id, audit_status, discussion);
            sendMessage(underTestLeaderId, type, audit_status, testProjectName);
            return (resultA == 1 ? true : false);
        } else
            return false;

    }

    public void sendMessage(Long underTestLeaderId, Integer type, Integer audit_status, String testProjectName) {
        User underTestLeader = userMapper.selectByUserId(underTestLeaderId);
        String userPhone = underTestLeader.getPhone();
        String gradeAuditMaterial = "定级审核材料";
        String filingMaterial = "备案材料";
        String assessmentReport = "测评报告";

        if (type == ProjectMaterial.Type.GRADE_AUDIT_MATERIAL) {
            if (audit_status == ProjectMaterial.Audit.NOPASS)
                SmsUtil.singleSendMsg(userPhone, InitConfig.NOTIFY_UNDER_TEST_LEADER_NOT_PASS_ID, testProjectName, gradeAuditMaterial);
            else
                SmsUtil.singleSendMsg(userPhone, InitConfig.NOTIFY_UNDER_TEST_LEADER_PASS_ID, testProjectName, gradeAuditMaterial);
        } else if (type == ProjectMaterial.Type.FILING_MATERIAL) {
            if (audit_status == ProjectMaterial.Audit.NOPASS)
                SmsUtil.singleSendMsg(userPhone, InitConfig.NOTIFY_UNDER_TEST_LEADER_NOT_PASS_ID, testProjectName, filingMaterial);
            else
                SmsUtil.singleSendMsg(userPhone, InitConfig.NOTIFY_UNDER_TEST_LEADER_PASS_ID, testProjectName, filingMaterial);
        } else if (type == ProjectMaterial.Type.ASSESSMENT_REPORT) {
            if (audit_status == ProjectMaterial.Audit.NOPASS)
                SmsUtil.singleSendMsg(userPhone, InitConfig.NOTIFY_UNDER_TEST_LEADER_NOT_PASS_ID, testProjectName, assessmentReport);
            else
                SmsUtil.singleSendMsg(userPhone, InitConfig.NOTIFY_UNDER_TEST_LEADER_PASS_ID, testProjectName, assessmentReport);
        }

    }
}

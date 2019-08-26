package com.bjtu.testmanageplatform.controller;

import com.bjtu.testmanageplatform.beans.*;
import com.bjtu.testmanageplatform.beans.base.JRequest;
import com.bjtu.testmanageplatform.beans.base.JResponse;
import com.bjtu.testmanageplatform.beans.base.TokenObject;
import com.bjtu.testmanageplatform.model.ProjectMaterial;
import com.bjtu.testmanageplatform.model.TestProject;
import com.bjtu.testmanageplatform.service.MaterialService;
import com.bjtu.testmanageplatform.service.TestProjectService;
import com.bjtu.testmanageplatform.util.Generator;
import com.bjtu.testmanageplatform.util.JLog;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/material")
public class MaterialController {

    private TestProjectService testProjectService;
    private MaterialService materialService;

    @Autowired
    public MaterialController(TestProjectService testProjectService, MaterialService materialService){
        this.testProjectService = testProjectService;
        this.materialService = materialService;
    }

    @RequestMapping(value = "/upload")
    public JResponse upload(HttpServletRequest request,
                            @Valid @RequestBody MaterialUploadReq materialUploadReq){
        TokenObject tokenObject = Generator.parseToken(materialUploadReq.getToken());
        MaterialUploadReq.MaterialUploadData materialUploadData = materialUploadReq.getData();
        JLog.info(String.format("upload material projectid=%s type=%s" , materialUploadData.getProject_id() , materialUploadData.getType()));
        JResponse jResponse = new JResponse();

        // 如果项目不存在返回报错信息
        TestProject testProject =
                testProjectService.selectByProjectId(materialUploadData.getProject_id());
        if (testProject == null) {
            jResponse.setErr_no(101191552);
            jResponse.setErr_msg("unknown test project");
            return jResponse;
        }

        //查询该项目该类型的文件版本号
        Integer countByIdType = materialService.getVersionByIdType(materialUploadData.getProject_id() ,materialUploadData.getType());
        Integer newVersion = new Integer(countByIdType.intValue() + 1);

        ProjectMaterial projectMaterial = new ProjectMaterial();
        BeanUtils.copyProperties(materialUploadData ,projectMaterial);
        projectMaterial.setVersion(newVersion);
        projectMaterial.setCommitter_id(tokenObject.getUserId());

        int result = materialService.upload(projectMaterial);
        if(result == 0){
            jResponse.setErr_no(101400001);
            jResponse.setErr_msg("db error");
            return jResponse;
        }

        return jResponse;
    }

    @RequestMapping(value = "/list")
    public MaterialListResponse list(HttpServletRequest request,
                          @Valid @RequestBody MaterialListReq materialListReq){
        MaterialListReq.MaterialListData materialListData = materialListReq.getData();
        TokenObject tokenObject = Generator.parseToken(materialListReq.getToken());
        JLog.info(String.format("material list projectid=%s userid=%s"
                ,materialListData.getProject_id(), tokenObject.getUserId()));
        MaterialListResponse materialListResponse = new MaterialListResponse();

        // 如果项目不存在返回报错信息
        TestProject testProject = testProjectService.selectByProjectId(materialListData.getProject_id());
        if (testProject == null) {
            materialListResponse.setErr_no(101191552);
            materialListResponse.setErr_msg("unknown test project");
            return materialListResponse;
        }

        List<MaterialListResponse.Material> materialList = materialService.getMaterialByProjectId(materialListData.getProject_id());
        materialListResponse.setData(materialList);

        return materialListResponse;
    }

    @RequestMapping(value = "/update")
    public JResponse update(HttpServletRequest request,
                         @Valid @RequestBody MaterialUpdateReq materialUpdateReq){
        MaterialUpdateReq.MaterialUpdateData materialUpdateData = materialUpdateReq.getData();
        TokenObject tokenObject = Generator.parseToken(materialUpdateReq.getToken());
        JLog.info(String.format("update material projectid={} materialid={} type={} userid={}"
                ,materialUpdateData.getProject_id(),materialUpdateData.getMaterial_id(),materialUpdateData.getType(),tokenObject.getUserId()));
        JResponse jResponse = new JResponse();

        ProjectMaterial projectMaterial = materialService.getMaterialById(materialUpdateData.getMaterial_id());
        if(projectMaterial == null){
            jResponse.setErr_no(101400005);
            jResponse.setErr_msg("unknown material");
            return jResponse;
        }

        if(materialService.judgeMatch(materialUpdateData.getMaterial_id(), materialUpdateData.getProject_id(), materialUpdateData.getType()) == false){
            jResponse.setErr_no(101400004);
            jResponse.setErr_msg("project_id material_id type not match");
            return jResponse;
        }

        Boolean result = materialService.judgeContains(materialUpdateData.getFile_url(),materialUpdateData.getContent());
        String remark = materialUpdateData.getRemark().trim();

        //无法判断remark是修改为空还是本身为空
        if(result && remark.length() == 0){
            jResponse.setErr_no(101400002);
            jResponse.setErr_msg("No change");
            return jResponse;
        }else{
            if(!materialService.update(materialUpdateData.getMaterial_id(), remark,
                    materialUpdateData.getFile_url(), materialUpdateData.getContent())){
                jResponse.setErr_no(101400003);
                jResponse.setErr_msg("update failed");
                return jResponse;
            }
        }
        return jResponse;

    }

    @RequestMapping(value = "/audit")
    public JResponse aduit(HttpServletRequest request,
                        @Valid @RequestBody MaterialAduitReq materialAduitReq){
        MaterialAduitReq.MaterialAduitData materialAduitData = materialAduitReq.getData();
        TokenObject tokenObject = Generator.parseToken(materialAduitReq.getToken());
        JLog.info(String.format("audit materialid=%s by userid=%s"
                , materialAduitData.getMaterial_id(), tokenObject.getUserId()));
        JResponse jResponse = new JResponse();

        ProjectMaterial projectMaterial = materialService.getMaterialById(materialAduitData.getMaterial_id());
        if(projectMaterial == null){
            jResponse.setErr_no(101400005);
            jResponse.setErr_msg("unknown material");
            return jResponse;
        }

        if(projectMaterial.getAudit_status() != ProjectMaterial.Audit.UNUSE){
            jResponse.setErr_no(101400006);
            jResponse.setErr_msg("The material has been aduited");
            return jResponse;
        }

        //判断是否可以audit，类型包括定级材料，审核材料，测评报告
        if(!materialService.judgeCanAudit(projectMaterial.getType())){
            jResponse.setErr_no(101400007);
            jResponse.setErr_msg("material can not be audited");
            return jResponse;
        }

        if(!materialService.audit(materialAduitData.getMaterial_id(),projectMaterial.getType()
                ,materialAduitData.getResult(),materialAduitData.getDiscussion())){
            jResponse.setErr_no(101400008);
            jResponse.setErr_msg("db error or test status can not change");
            JLog.error(String.format("user save failed userId=%s phone=%s errNo=101400008",
                    tokenObject.getUserId()), 101400008);
            return jResponse;
        }

        return jResponse;
    }

}

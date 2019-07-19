package com.bjtu.testmanageplatform.controller;

import com.bjtu.testmanageplatform.beans.CreateProjectReq;
import com.bjtu.testmanageplatform.beans.CreateProjectResponse;
import com.bjtu.testmanageplatform.model.TestProject;
import com.bjtu.testmanageplatform.service.TestProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @Author: gaofeng
 * @Date: 2019-07-18
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping(value = "/v1/project")
public class TestProjectController {

    private TestProjectService testProjectService;

    @Autowired
    public TestProjectController(TestProjectService testProjectService) {
        this.testProjectService = testProjectService;
    }


    /**
     * 创建一个测试项目
     *
     * @param request
     * @param CreateProjectReq
     *
     * @return
     */
    @RequestMapping(value = "/create")
    public CreateProjectResponse create(HttpServletRequest request,
                                        @RequestBody @Valid CreateProjectReq CreateProjectReq) {
        CreateProjectReq.CreateProjectData createProjectData = CreateProjectReq.getData();
        log.info("create project name={}", createProjectData.getName());
        CreateProjectResponse createProjectResponse = new CreateProjectResponse();

        // 查找该项目名称是否已经被占用了
        TestProject testProjectInDB = testProjectService.selectByName(createProjectData.getName());
        if (testProjectInDB != null) {
            createProjectResponse.setErr_no(101182058);
            createProjectResponse.setErr_msg("there is already a project with name.");
            return createProjectResponse;
        }

        // 创建TestProject并赋值
        TestProject testProject = new TestProject();
        BeanUtils.copyProperties(createProjectData, testProject);
        testProject.setName(createProjectData.getName().trim());
        testProject.setUnder_test_leader_id(CreateProjectReq.getUser_profile().getUser_id());

        // 调用业务逻辑，创建对象
        Long projectId = testProjectService.create(testProject);

        if (projectId.equals(0L)) {
            createProjectResponse.setErr_no(101190152);
            createProjectResponse.setErr_msg("no available test leader or db error");
        } else {
            CreateProjectResponse.CreateProjectResData createProjectResData =
                    new CreateProjectResponse.CreateProjectResData();
            createProjectResData.setProject_id(projectId);
            createProjectResponse.setData(createProjectResData);
        }
        return createProjectResponse;
    }
}

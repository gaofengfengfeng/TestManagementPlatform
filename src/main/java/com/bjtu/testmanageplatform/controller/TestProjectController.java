package com.bjtu.testmanageplatform.controller;

import com.bjtu.testmanageplatform.beans.AssignTesterReq;
import com.bjtu.testmanageplatform.beans.CreateProjectReq;
import com.bjtu.testmanageplatform.beans.CreateProjectResponse;
import com.bjtu.testmanageplatform.beans.ProjectStatusReq;
import com.bjtu.testmanageplatform.beans.base.JResponse;
import com.bjtu.testmanageplatform.model.TestProject;
import com.bjtu.testmanageplatform.model.User;
import com.bjtu.testmanageplatform.service.TestProjectService;
import com.bjtu.testmanageplatform.service.UserService;
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
    private UserService userService;

    @Autowired
    public TestProjectController(TestProjectService testProjectService,
                                 UserService userService) {
        this.testProjectService = testProjectService;
        this.userService = userService;
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


    /**
     * 更新项目状态
     *
     * @param request
     * @param projectStatusReq
     *
     * @return
     */
    @RequestMapping(value = "/updateStatus")
    public JResponse changeStatus(HttpServletRequest request,
                                  @RequestBody @Valid ProjectStatusReq projectStatusReq) {
        ProjectStatusReq.ProjectStatusData projectStatusData = projectStatusReq.getData();
        log.info("change project status prokectId={} new status={}",
                projectStatusData.getProject_id(), projectStatusData.getStatus());

        JResponse jResponse = new JResponse();
        // 如果待更改状态不在status状态范围内，则返回报错信息
        if (projectStatusData.getStatus() < TestProject.Status.GRADE_MATERIAL_AUDIT_ING ||
                projectStatusData.getStatus() > TestProject.Status.PROJECT_CANCELED) {
            jResponse.setErr_no(101191548);
            jResponse.setErr_msg("unknown status");
        }

        // 如果项目不存在返回报错信息
        TestProject testProject =
                testProjectService.selectByProjectId(projectStatusData.getProject_id());
        if (testProject == null) {
            jResponse.setErr_no(101191552);
            jResponse.setErr_msg("unknown test project");
            return jResponse;
        }

        // 进入业务流程，先判断该状态是否是可达状态，如果是更新数据库，如果不是返回错误
        Boolean result = testProjectService.changeProjectStatus(testProject,
                projectStatusData.getStatus());
        if (!result) {
            jResponse.setErr_no(101191637);
            jResponse.setErr_msg("unreachasble status or db error");
        }

        return jResponse;
    }

    /**
     * 为测试项目配置测试人员
     *
     * @param request
     * @param assignTesterReq
     *
     * @return
     */
    @RequestMapping(value = "/assignTester")
    public JResponse assignTester(HttpServletRequest request,
                                  @RequestBody @Valid AssignTesterReq assignTesterReq) {
        AssignTesterReq.AssignTesterData assignTesterData = assignTesterReq.getData();
        log.info("assignTester projectId={}", assignTesterData.getProject_id());
        JResponse jResponse = new JResponse();

        // 判断该项目是否存在
        TestProject testProject =
                testProjectService.selectByProjectId(assignTesterData.getProject_id());
        if (testProject == null) {
            jResponse.setErr_no(101192103);
            jResponse.setErr_msg("unknown test project");
            return jResponse;
        }

        // 判断这些用户是够都是测试人员
        Boolean isAllTesters = userService.checkUserRole(assignTesterData.getTesters(),
                User.Role.TESTER);
        if (!isAllTesters) {
            jResponse.setErr_no(101192110);
            jResponse.setErr_msg("not all users are tester");
            return jResponse;
        }

        // 创建测试项目和测试人员之间的关系表
        Boolean result = testProjectService.assignTester(assignTesterData.getProject_id(),
                assignTesterData.getTesters());

        if (!result) {
            jResponse.setErr_no(101192124);
            jResponse.setErr_msg("db error");
        }

        return jResponse;
    }
}

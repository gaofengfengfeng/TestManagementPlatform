package com.bjtu.testmanageplatform.controller;

import com.bjtu.testmanageplatform.beans.*;
import com.bjtu.testmanageplatform.beans.base.JRequest;
import com.bjtu.testmanageplatform.beans.base.JResponse;
import com.bjtu.testmanageplatform.beans.base.UserProfile;
import com.bjtu.testmanageplatform.model.TestProject;
import com.bjtu.testmanageplatform.model.User;
import com.bjtu.testmanageplatform.service.TestProjectService;
import com.bjtu.testmanageplatform.service.UserService;
import com.bjtu.testmanageplatform.util.JLog;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: gaofeng
 * @Date: 2019-07-18
 * @Description:
 */
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
        JLog.info(String.format("create project name=%s", createProjectData.getName()));
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
        JLog.info(String.format("change project status prokectId=%s new status=%s",
                projectStatusData.getProject_id(), projectStatusData.getStatus()));

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
        JLog.info(String.format("assignTester projectId=%s", assignTesterData.getProject_id()));
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


    /**
     * 拉取某个用户所负责的所有项目列表
     *
     * @param request
     * @param jRequest
     *
     * @return
     */
    @RequestMapping(value = "/list")
    public ProjectListResponse list(HttpServletRequest request,
                                    @Valid @RequestBody JRequest jRequest) {
        UserProfile userProfile = jRequest.getUser_profile();
        JLog.info(String.format("project list userId=%s", userProfile.getUser_id()));
        ProjectListResponse projectListResponse = new ProjectListResponse();

        // 需要根据不同的用户角色拉取相应的项目列表，比如 测试单位负责人、被测单位项目负责人的记录存在test_project表中
        // 而实际测试人员和项目的关系则存在project_tester_relation表中
        Integer role = userService.getRoleByUserId(userProfile.getUser_id());

        // TODO: 目前只支持测试单位负责人、被测单位项目负责人、实际测试人员拉取项目列表
        if (!role.equals(User.Role.UNDER_TEST_LEADER) &&
                !role.equals(User.Role.TEST_LEADER) &&
                !role.equals(User.Role.TESTER)) {
            projectListResponse.setErr_no(101200042);
            projectListResponse.setErr_msg("the role of this user is not supported at present");
            return projectListResponse;
        }

        List<TestProject> testProjects = testProjectService.list(userProfile.getUser_id(), role);
        List<ProjectListResponse.Project> projects = new ArrayList<>();
        for (TestProject testProject : testProjects) {
            ProjectListResponse.Project project = new ProjectListResponse.Project();
            BeanUtils.copyProperties(testProject, project);
            project.setUnder_test_leader_name(userService.getNameByUserId(testProject.getUnder_test_leader_id()));
            project.setTest_leader_name(userService.getNameByUserId(testProject.getTest_leader_id()));
            projects.add(project);
        }
        projectListResponse.setData(projects);
        return projectListResponse;
    }


    /**
     * 请求项目测试报告模板
     *
     * @param request
     * @param templateReq
     *
     * @return
     */
    @RequestMapping(value = "/template")
    public TemplateResponse template(HttpServletRequest request,
                                     @Valid @RequestBody TemplateReq templateReq) {
        TemplateReq.TemplateData templateData = templateReq.getData();
        JLog.info(String.format("template projectId=%s", templateData.getProject_id()));
        TemplateResponse templateResponse = new TemplateResponse();

        // 判断该项目是否存在
        TestProject testProject =
                testProjectService.selectByProjectId(templateData.getProject_id());
        if (testProject == null) {
            templateResponse.setErr_no(101230121);
            templateResponse.setErr_msg("unknown test project");
            return templateResponse;
        }

        String content = testProjectService.getTemplate(testProject);

        TemplateResponse.TemplateResData templateResData = new TemplateResponse.TemplateResData();
        templateResData.setContent(content);
        templateResponse.setData(templateResData);

        return templateResponse;
    }
}

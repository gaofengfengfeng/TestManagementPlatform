package com.bjtu.testmanageplatform.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjtu.testmanageplatform.mapper.ProjectTesterRelationMapper;
import com.bjtu.testmanageplatform.mapper.StandardLibraryMapper;
import com.bjtu.testmanageplatform.mapper.TestProjectMapper;
import com.bjtu.testmanageplatform.mapper.UserMapper;
import com.bjtu.testmanageplatform.model.ProjectTesterRelation;
import com.bjtu.testmanageplatform.model.StandardLibrary;
import com.bjtu.testmanageplatform.model.TestProject;
import com.bjtu.testmanageplatform.model.User;
import com.bjtu.testmanageplatform.util.Generator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: gaofeng
 * @Date: 2019-07-18
 * @Description:
 */
@Slf4j
@Service
public class TestProjectService {

    private TestProjectMapper testProjectMapper;
    private UserMapper userMapper;
    private StateMachineService stateMachineService;
    private ProjectTesterRelationMapper projectTesterRelationMapper;
    private StandardLibraryMapper standardLibraryMapper;

    @Autowired
    public TestProjectService(TestProjectMapper testProjectMapper,
                              UserMapper userMapper,
                              StateMachineService stateMachineService,
                              ProjectTesterRelationMapper projectTesterRelationMapper,
                              StandardLibraryMapper standardLibraryMapper) {
        this.testProjectMapper = testProjectMapper;
        this.userMapper = userMapper;
        this.stateMachineService = stateMachineService;
        this.projectTesterRelationMapper = projectTesterRelationMapper;
        this.standardLibraryMapper = standardLibraryMapper;
    }

    /**
     * 根据项目名称查找项目
     *
     * @param name
     *
     * @return
     */
    public TestProject selectByName(String name) {
        log.info("enter select project by name. name={}", name);
        return testProjectMapper.selectByName(name.trim());
    }

    /**
     * 根据项目id查找项目
     *
     * @param projectId
     *
     * @return
     */
    public TestProject selectByProjectId(Long projectId) {
        log.info("enter selectByProjectId projectId={}", projectId);
        return testProjectMapper.selectByProjectId(projectId);
    }


    /**
     * 测试项目创建
     *
     * @param testProject
     *
     * @return
     */
    public Long create(TestProject testProject) {
        log.info("enter create project name={}", testProject.getName());

        // 查找一个测试单位负责人，如果没有负责人则创建失败
        List<User> testLeaders = userMapper.selectByRole(User.Role.TEST_LEADER);
        if (testLeaders == null || testLeaders.size() == 0) {
            log.error("no test leader available");
            return 0L;
        }

        // 随机取一名测试负责人将任务分配给他
        // TODO: 后续可以写一个分配策略，目前直接分配给第一个人
        User testLeader = testLeaders.get(0);
        // TODO: 需要短信通知测试负责人

        // 完善testProject对象属性
        testProject.setProject_id(Generator.generateLongId());
        testProject.setTest_leader_id(testLeader.getUserId());
        testProject.setStatus(TestProject.Status.GRADE_MATERIAL_AUDIT_ING);
        // TODO: project_location_code保留字段，后续可以后端解析 省市区得到code，也可以前端传递，目前默认都设置成0
        testProject.setProject_location_code(0);
        Integer result = testProjectMapper.insert(testProject);
        return result == 1 ? testProject.getProject_id() : 0L;
    }


    /**
     * 更改测试项目状态
     *
     * @param testProject
     * @param newStatus
     *
     * @return
     */
    public Boolean changeProjectStatus(TestProject testProject, Integer newStatus) {

        Integer oldStatus = testProject.getStatus();
        log.info("changePojectStatus oldStatus={} newStatus={}", oldStatus, newStatus);

        // 判断新状态是否是可达状态
        if (!stateMachineService.isReachable(oldStatus, newStatus)) {
            log.info("unreachable status");
            return false;
        }

        // 更新数据库状态
        Integer affect = testProjectMapper.updateStatusByProjectId(testProject.getProject_id(),
                newStatus);
        if (affect.equals(1)) {
            return true;
        }
        return false;
    }

    /**
     * 为项目设置测试人员，该方法应为事务方法，只要有一条插入失败，全部回滚
     *
     * @param projectId
     * @param testIds
     *
     * @return
     */
    @Transactional
    public Boolean assignTester(Long projectId, List<Long> testIds) {
        log.info("assignTester projectId={}", projectId);

        for (Long testerId : testIds) {
            ProjectTesterRelation projectTesterRelation = new ProjectTesterRelation();
            projectTesterRelation.setRelation_id(Generator.generateLongId());
            projectTesterRelation.setProject_id(projectId);
            projectTesterRelation.setTester_id(testerId);
            Integer affect = projectTesterRelationMapper.insert(projectTesterRelation);
            if (!affect.equals(1)) {
                // 事务回滚 return false
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
        return true;
    }


    /**
     * 根据用户角色和用户id查找他所负责的测试项目
     *
     * @param userId
     * @param role
     *
     * @return
     */
    public List<TestProject> list(Long userId, Integer role) {
        log.info("enter project list userId={} role={}", userId, role);

        // 根据不同用户角色调用不同查找项目的方法
        if (role.equals(User.Role.UNDER_TEST_LEADER)) {
            return testProjectMapper.selectByUnderTestLeaderId(userId);
        } else if (role.equals(User.Role.TEST_LEADER)) {
            return testProjectMapper.selectByTestLeaderId(userId);
        } else if (role.equals(User.Role.TESTER)) {
            List<Long> projectIds = projectTesterRelationMapper.selectProjectIdsByTesterId(userId);
            List<TestProject> testProjects = new ArrayList<>();
            for (Long projectId : projectIds) {
                testProjects.add(testProjectMapper.selectByProjectId(projectId));
            }
            return testProjects;
        }
        return null;
    }


    /**
     * 根据项目的定级生成项目测试报告
     *
     * @param testProject
     *
     * @return
     */
    public String getTemplate(TestProject testProject) {
        log.info("enter getTemplate projectId={} rank={}", testProject.getProject_id(),
                testProject.getRank());

        // 拿到项目的定级并解析
        String rank = testProject.getRank();
        String[] rankAfterSplit = rank.split(",");

        // 首先拿到一级标题
        List<StandardLibrary> headlines = standardLibraryMapper.selectHeadlinesByRank();
        JSONObject firstHeadline = new JSONObject();
        for (StandardLibrary h : headlines) {
            List<StandardLibrary> secondaryHeadlines =
                    standardLibraryMapper.selectSecondaryHeadlinesByRankAndHeadline(h.getHeadline_rank());
            JSONObject secondHeadline = new JSONObject();
            for (StandardLibrary sh : secondaryHeadlines) {
                List<StandardLibrary> names =
                        standardLibraryMapper.selectNamesByRankAndHeadlineAndSecondaryHeadline
                                (rankAfterSplit, h.getHeadline_rank(),
                                        sh.getSecondary_headline_rank());
                JSONObject contents = new JSONObject();
                for (StandardLibrary name : names) {
                    JSONObject content = new JSONObject();
                    content.put("content", name.getContent());
                    contents.put(name.getName(), content);
                }
                secondHeadline.put(sh.getSecondary_headline(), contents);
            }
            firstHeadline.put(h.getHeadline(), secondHeadline);
        }
        String template = JSON.toJSONString(firstHeadline);
        System.out.println(template);
        return template;
    }
}

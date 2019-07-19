package com.bjtu.testmanageplatform.service;

import com.bjtu.testmanageplatform.mapper.ProjectTesterRelationMapper;
import com.bjtu.testmanageplatform.mapper.TestProjectMapper;
import com.bjtu.testmanageplatform.mapper.UserMapper;
import com.bjtu.testmanageplatform.model.ProjectTesterRelation;
import com.bjtu.testmanageplatform.model.TestProject;
import com.bjtu.testmanageplatform.model.User;
import com.bjtu.testmanageplatform.util.Generator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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

    @Autowired
    public TestProjectService(TestProjectMapper testProjectMapper,
                              UserMapper userMapper,
                              StateMachineService stateMachineService,
                              ProjectTesterRelationMapper projectTesterRelationMapper) {
        this.testProjectMapper = testProjectMapper;
        this.userMapper = userMapper;
        this.stateMachineService = stateMachineService;
        this.projectTesterRelationMapper = projectTesterRelationMapper;
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
}

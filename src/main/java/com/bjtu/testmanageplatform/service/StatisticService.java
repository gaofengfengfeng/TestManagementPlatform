package com.bjtu.testmanageplatform.service;

import com.bjtu.testmanageplatform.beans.statistic.ProjectNumEveryday;
import com.bjtu.testmanageplatform.beans.statistic.ProjectRankRelation;
import com.bjtu.testmanageplatform.beans.statistic.ProjectTypeRelation;
import com.bjtu.testmanageplatform.mapper.StandardLibraryMapper;
import com.bjtu.testmanageplatform.mapper.TestProjectMapper;
import com.bjtu.testmanageplatform.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * ${description}
 *
 * @author gaofeng
 * @version 1.0
 * @date 2019-10-24
 */
@Service
public class StatisticService {

    private static final Long ONE_DAY = 24 * 3600 * 1000L;

    @Resource
    private UserMapper userMapper;

    @Resource
    private TestProjectMapper testProjectMapper;

    @Resource
    private StandardLibraryMapper standardLibraryMapper;

    /**
     * 获取当前系统用户数量
     *
     * @return
     */
    public Integer getUserNum() {
        return userMapper.queryUserNum();
    }

    /**
     * 获取当前系统项目数量
     *
     * @return
     */
    public Integer getPojectNum() {
        return testProjectMapper.queryProjectNum();
    }

    /**
     * 获取当前系统标准数量
     *
     * @return
     */
    public Integer getStandardNum() {
        return standardLibraryMapper.queryStandardNum();
    }

    /**
     * 查询近七天项目数量
     *
     * @return
     */
    public List<ProjectNumEveryday> getProjectNum7days() {
        List<ProjectNumEveryday> projectNumEverydays = new ArrayList<>(7);
        Long nowTimestamp = getTodayZeroPointTimestamps();
        for (int i = 0; i < 7; i++) {
            ProjectNumEveryday everyday = new ProjectNumEveryday();
            Long currentTimestamp = nowTimestamp - i * ONE_DAY;
            Integer count = testProjectMapper.queryProjectNumByInterval(currentTimestamp, System.currentTimeMillis());
            everyday.setTimestamp(currentTimestamp);
            everyday.setCount(count);
            projectNumEverydays.add(everyday);
        }
        return projectNumEverydays;
    }

    /**
     * 查询不同类型的项目数量
     *
     * @return
     */
    public List<ProjectTypeRelation> getProjectTypeRelations() {
        return testProjectMapper.queryProjectTypeRelation();
    }

    /**
     * 查询不同定级的项目数量
     *
     * @return
     */
    public List<ProjectRankRelation> getProjectRankRelations() {
        return testProjectMapper.queryProjectRankRelation();
    }

    /**
     * 获得“今天”零点时间戳
     *
     * @return
     */
    public static Long getTodayZeroPointTimestamps() {
        Long currentTimestamps = System.currentTimeMillis();
        Long oneDayTimestamps = Long.valueOf(60 * 60 * 24 * 1000);
        return currentTimestamps - (currentTimestamps + 60 * 60 * 8 * 1000) % oneDayTimestamps;
    }

}

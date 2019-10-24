package com.bjtu.testmanageplatform.controller;

import com.bjtu.testmanageplatform.beans.StatisticResponse;
import com.bjtu.testmanageplatform.beans.statistic.StatisticData;
import com.bjtu.testmanageplatform.service.StatisticService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ${description}
 *
 * @author gaofeng
 * @version 1.0
 * @date 2019-10-24
 */
@RestController
@RequestMapping(value = "/v1/stats")
public class StatisticController {

    @Resource
    private StatisticService statisticService;

    @RequestMapping(value = "/overview")
    public StatisticResponse overview() {

        StatisticResponse statisticResponse = new StatisticResponse();

        StatisticData statisticData = new StatisticData();
        statisticData.setUserNum(statisticService.getUserNum());
        statisticData.setProjectNum(statisticService.getPojectNum());
        statisticData.setStandardNum(statisticService.getStandardNum());
        statisticData.setProject_day_count(statisticService.getProjectNum7days());
        statisticData.setProject_type_count(statisticService.getProjectTypeRelations());
        statisticData.setProject_pass_count(statisticService.getProjectRankRelations());

        statisticResponse.setData(statisticData);
        return statisticResponse;
    }
}

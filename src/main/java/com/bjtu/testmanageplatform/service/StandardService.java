package com.bjtu.testmanageplatform.service;

import com.bjtu.testmanageplatform.mapper.StandardLibraryMapper;
import com.bjtu.testmanageplatform.model.StandardLibrary;
import com.bjtu.testmanageplatform.util.Generator;
import com.bjtu.testmanageplatform.util.JLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: gaofeng
 * @Date: 2019-07-16
 * @Description:
 */
@Service
public class StandardService {

    private StandardLibraryMapper standardLibraryMapper;

    @Autowired
    public StandardService(StandardLibraryMapper standardLibraryMapper) {
        this.standardLibraryMapper = standardLibraryMapper;
    }

    /**
     * 根据一个条目的三个序号唯一确定一条记录
     *
     * @param headlineRank
     * @param secondaryHeadlineRank
     * @param nameRank
     *
     * @return
     */
    public StandardLibrary findByRank(Integer headlineRank, Integer secondaryHeadlineRank,
                                      Integer nameRank) {
        JLog.info(String.format("enter findByRank headlineRank={} secondaryHeadlineRank={} " +
                "nameRank={}", headlineRank, secondaryHeadlineRank, nameRank));
        return standardLibraryMapper.selectByRank(headlineRank, secondaryHeadlineRank, nameRank);
    }


    /**
     * 插入一条新的标准
     *
     * @param standardLibrary
     *
     * @return
     */
    public Boolean create(StandardLibrary standardLibrary) {
        JLog.info(String.format("enter create standardLibrary={}", standardLibrary));
        standardLibrary.setStandard_id(Generator.generateLongId());
        Integer result = standardLibraryMapper.create(standardLibrary);
        return (result == 1 ? true : false);
    }

    /**
     * 按标题等级返回list
     *
     * @param standardRank
     * @param headlineRank
     * @param secondaryHeadlineRank
     *
     * @return
     */
    public List<StandardLibrary> list(Integer standardRank, Integer headlineRank,
                                      Integer secondaryHeadlineRank) {
        JLog.info(String.format("enter list standardRank={} headline_rank={} " +
                "secondary_headline_rank={}", standardRank, headlineRank, secondaryHeadlineRank));
        if (standardRank.equals(1)) {
            return standardLibraryMapper.selectListByStandardRankFirst();
        } else if (standardRank.equals(2)) {
            return standardLibraryMapper.selectListByStandardRankSecond(headlineRank);
        } else {
            return standardLibraryMapper.selectListByStandardRankThird(headlineRank,
                    secondaryHeadlineRank);
        }
    }
}

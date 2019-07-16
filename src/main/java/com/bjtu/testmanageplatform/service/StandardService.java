package com.bjtu.testmanageplatform.service;

import com.bjtu.testmanageplatform.mapper.StandardLibraryMapper;
import com.bjtu.testmanageplatform.model.StandardLibrary;
import com.bjtu.testmanageplatform.util.Generator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: gaofeng
 * @Date: 2019-07-16
 * @Description:
 */
@Slf4j
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
        log.info("enter findByRank headlineRank={} secondaryHeadlineRank={} nameRank={}",
                headlineRank, secondaryHeadlineRank, nameRank);
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
        log.info("enter create standardLibrary={}", standardLibrary);
        standardLibrary.setStandard_id(Generator.generateLongId());
        Integer result = standardLibraryMapper.create(standardLibrary);
        return (result == 1 ? true : false);
    }
}

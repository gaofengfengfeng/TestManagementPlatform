package com.bjtu.testmanageplatform.controller;

import com.bjtu.testmanageplatform.beans.CreateStandardReq;
import com.bjtu.testmanageplatform.beans.base.JResponse;
import com.bjtu.testmanageplatform.model.StandardLibrary;
import com.bjtu.testmanageplatform.service.StandardService;
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
 * @Date: 2019-07-15
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping(value = "/v1/standard")
public class StandardController {

    private StandardService standardService;

    @Autowired
    public StandardController(StandardService standardService) {
        this.standardService = standardService;
    }

    @RequestMapping(value = "/add")
    public JResponse create(HttpServletRequest request,
                            @Valid @RequestBody CreateStandardReq createStandardReq) {
        CreateStandardReq.CreateStandardData createStandardData = createStandardReq.getData();
        log.info("create standard {}", createStandardData.toString());
        JResponse jResponse = new JResponse();

        // 判断该条记录是否已经存在，如果存在则更新，如果不存在则插入
        StandardLibrary standardLibrary =
                standardService.findByRank(createStandardData.getHeadline_rank(),
                        createStandardData.getSecondary_headline_rank(),
                        createStandardData.getName_rank());

        if (standardLibrary != null) {
            jResponse.setErr_no(101161801);
            jResponse.setErr_msg("There's already a record.");
            return jResponse;
        }

        // 执行插入操作
        standardLibrary = new StandardLibrary();
        BeanUtils.copyProperties(createStandardData, standardLibrary);
        Boolean result = standardService.create(standardLibrary);

        if (!result) {
            jResponse.setErr_no(101161814);
            jResponse.setErr_msg("db insert error");
        }
        return jResponse;
    }
}

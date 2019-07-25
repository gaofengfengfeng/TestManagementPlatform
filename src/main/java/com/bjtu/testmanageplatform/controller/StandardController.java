package com.bjtu.testmanageplatform.controller;

import com.bjtu.testmanageplatform.beans.CreateStandardReq;
import com.bjtu.testmanageplatform.beans.StandardListReq;
import com.bjtu.testmanageplatform.beans.StandardListResponse;
import com.bjtu.testmanageplatform.beans.base.JResponse;
import com.bjtu.testmanageplatform.model.StandardLibrary;
import com.bjtu.testmanageplatform.service.StandardService;
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
 * @Date: 2019-07-15
 * @Description:
 */
@RestController
@RequestMapping(value = "/v1/standard")
public class StandardController {

    private StandardService standardService;

    @Autowired
    public StandardController(StandardService standardService) {
        this.standardService = standardService;
    }

    /**
     * 插入一条标准库记录
     *
     * @param request
     * @param createStandardReq
     *
     * @return
     */
    @RequestMapping(value = "/add")
    public JResponse create(HttpServletRequest request,
                            @Valid @RequestBody CreateStandardReq createStandardReq) {
        CreateStandardReq.CreateStandardData createStandardData = createStandardReq.getData();
        JLog.info(String.format("create standard %s", createStandardData.toString()));
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


    /**
     * 查找指定标准库记录列表
     *
     * @param request
     * @param standardListReq
     *
     * @return
     */
    @RequestMapping(value = "/list")
    public StandardListResponse list(HttpServletRequest request,
                                     @RequestBody @Valid StandardListReq standardListReq) {
        StandardListReq.StandardListData standardListData = standardListReq.getData();
        JLog.info(String.format("standard list standardRank=%s headline_rank=%s " +
                        "secondary_headline_rank=%s", standardListData.getStandard_rank(),
                standardListData.getHeadline_rank(),
                standardListData.getSecondary_headline_rank()));
        StandardListResponse standardListResponse = new StandardListResponse();

        // 判断传来的传来的顺序是否合规
        if (standardListData.getStandard_rank() < 1 || standardListData.getStandard_rank() > 3) {
            standardListResponse.setErr_no(101180042);
            standardListResponse.setErr_msg("standard_rank should between 1, 3");
            return standardListResponse;
        } else if (standardListData.getStandard_rank().equals(1) &&
                (!standardListData.getHeadline_rank().equals(0) ||
                        !standardListData.getSecondary_headline_rank().equals(0))) {
            standardListResponse.setErr_no(101180043);
            standardListResponse.setErr_msg("headline_rank can't be 0 or secondary_headline_rank " +
                    "should be 0");
            return standardListResponse;
        } else if (standardListData.getStandard_rank().equals(2) &&
                (!standardListData.getSecondary_headline_rank().equals(0) ||
                        standardListData.getHeadline_rank().equals(0))) {
            standardListResponse.setErr_no(101180044);
            standardListResponse.setErr_msg("secondary_headline_rank should be 0 or headline_rank" +
                    " can't be 0");
            return standardListResponse;
        } else if (standardListData.getStandard_rank().equals(3) &&
                (standardListData.getSecondary_headline_rank().equals(0) ||
                        standardListData.getHeadline_rank().equals(0))) {
            standardListResponse.setErr_no(101180045);
            standardListResponse.setErr_msg("secondary_headline_rank or headline_rank can't be 0");
            return standardListResponse;
        }


        List<StandardLibrary> standardLibraryList =
                standardService.list(standardListData.getStandard_rank(),
                        standardListData.getHeadline_rank(),
                        standardListData.getSecondary_headline_rank());

        List<StandardListResponse.StandardListResData> standardListResDataList = new ArrayList<>();
        for (StandardLibrary standardLibrary : standardLibraryList) {
            StandardListResponse.StandardListResData standardListResData =
                    new StandardListResponse.StandardListResData();
            BeanUtils.copyProperties(standardLibrary, standardListResData);
            standardListResDataList.add(standardListResData);
        }

        standardListResponse.setData(standardListResDataList);
        return standardListResponse;
    }
}

package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JRequest;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;

/**
 * @Author: gaofeng
 * @Date: 2019-07-18
 * @Description:
 */
@Data
public class StandardListReq extends JRequest {

    private StandardListData data;

    @Data
    public static class StandardListData {
        @Range(min = 1, max = 3)
        private Integer standard_rank;
        @Range(min = 0)
        private Integer headline_rank;
        @Range(min = 0)
        private Integer secondary_headline_rank;
    }
}

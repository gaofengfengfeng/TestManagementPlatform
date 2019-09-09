package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JResponse;
import lombok.Data;

import java.util.List;

/**
 * @Author: gaofeng
 * @Date: 2019-07-18
 * @Description:
 */
@Data
public class StandardListResponse extends JResponse {

    private List<StandardListResData> data;

    @Data
    public static class StandardListResData {
        private String headline;
        private Integer headline_rank;
        private String secondary_headline;
        private Integer secondary_headline_rank;
        private String third_headline;
        private Integer third_headline_rank;
        private String name;
        private Integer name_rank;
        private String rank;
        private String content;
    }
}

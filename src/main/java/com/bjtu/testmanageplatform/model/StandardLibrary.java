package com.bjtu.testmanageplatform.model;

import lombok.Data;

/**
 * @Author: gaofeng
 * @Date: 2019-07-07
 * @Description: 标准库记录
 */
@Data
public class StandardLibrary {
    private Long standard_id;
    private Integer standard_rank;
    private String headline;
    private String secondary_headline;
    private String third_headline;
    private String name;
    private Integer headline_rank;
    private Integer secondary_headline_rank;
    private String third_headline_rank;
    private Integer name_rank;
    private String rank;
    private String content;
    private Long create_time;

    public StandardLibrary() {
        standard_id = 0L;
        standard_rank = StandardRank.UNUSE;
        headline = "";
        secondary_headline = "";
        third_headline = "";
        name = "";
        headline_rank = 0;
        secondary_headline_rank = 0;
        third_headline_rank = "";
        name_rank = 0;
        rank = "";
        content = "";
        create_time = System.currentTimeMillis();
    }

    public static class StandardRank {
        private final static Integer UNUSE = 0;
        private final static Integer LEVEL_ONE = 1; // 一级
        private final static Integer LEVEL_TWO = 2; // 二级
        private final static Integer LEVEL_THREE = 3; // 三级
        private final static Integer LEVEL_FOUR = 4; // 四级
    }
}

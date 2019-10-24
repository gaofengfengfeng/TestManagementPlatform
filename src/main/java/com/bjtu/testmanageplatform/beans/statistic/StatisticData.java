package com.bjtu.testmanageplatform.beans.statistic;

import lombok.Data;

import java.util.List;

/**
 * ${description}
 *
 * @author gaofeng
 * @version 1.0
 * @date 2019-10-24
 */
@Data
public class StatisticData {
    private Integer userNum;
    private Integer projectNum;
    private Integer standardNum;
    private List<ProjectNumEveryday> project_day_count;
    private List<ProjectTypeRelation> project_type_count;
    private List<ProjectRankRelation> project_pass_count;

}

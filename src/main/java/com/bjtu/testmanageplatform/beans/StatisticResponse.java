package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JResponse;
import com.bjtu.testmanageplatform.beans.statistic.StatisticData;
import lombok.Data;

/**
 * ${description}
 *
 * @author gaofeng
 * @version 1.0
 * @date 2019-10-24
 */
@Data
public class StatisticResponse extends JResponse {
    private StatisticData data;
}

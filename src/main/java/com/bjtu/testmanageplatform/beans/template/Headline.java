package com.bjtu.testmanageplatform.beans.template;

import lombok.Data;

import java.util.List;

/**
 * @Author: gaofeng
 * @Date: 2019-07-23
 * @Description:
 */
@Data
public class Headline {
    private String name;
    private List<SecondaryHeadline> secondaryHeadlines;
}

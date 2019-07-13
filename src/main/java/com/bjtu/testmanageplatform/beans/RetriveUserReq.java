package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JRequest;
import com.bjtu.testmanageplatform.beans.base.JResponse;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: gaofeng
 * @Date: 2019-07-13
 * @Description:
 */
@Data
public class RetriveUserReq extends JRequest {

    private RetriveUserData data;

    @Data
    public static class RetriveUserData {
        @NotNull
        private Integer role;
    }
}

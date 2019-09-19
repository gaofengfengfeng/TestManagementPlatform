package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JResponse;
import com.bjtu.testmanageplatform.model.OperationRecord;
import lombok.Data;

import java.util.List;

/**
 * @Author: gaofeng
 * @Date: 2019-09-19
 * @Description:
 */
@Data
public class OperationHistoryResponse extends JResponse {

    private List<OperationRecord> data;
}

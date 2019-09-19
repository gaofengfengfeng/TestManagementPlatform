package com.bjtu.testmanageplatform.mapper;

import com.bjtu.testmanageplatform.model.OperationRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Author: gaofeng
 * @Date: 2019-09-19
 * @Description:
 */
@Mapper
@Component
public interface OperationRecordMapper {
    @Insert("INSERT INTO operation_record(record_id, project_id, operator_id, operator, " +
            "operate_type, tester, audit_type, audit_result, create_time) VALUES" +
            "(#{record_id}, #{project_id}, #{operator_id}, #{operator}, #{operate_type}, " +
            "#{tester}, #{audit_type}, #{audit_result}, #{create_time})")
    Integer insert(OperationRecord operationRecord);
}

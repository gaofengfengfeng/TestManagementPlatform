package com.bjtu.testmanageplatform.service;

import com.alibaba.fastjson.JSON;
import com.bjtu.testmanageplatform.beans.Tester;
import com.bjtu.testmanageplatform.mapper.OperationRecordMapper;
import com.bjtu.testmanageplatform.model.OperationRecord;
import com.bjtu.testmanageplatform.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: gaofeng
 * @Date: 2019-09-19
 * @Description:
 */
@Service
public class OperationRecordService {

    @Autowired
    private UserService userService;

    @Autowired
    private OperationRecordMapper operationRecordMapper;


    public void createProject(Long projectId, Long operatorId) {
        OperationRecord operationRecord = new OperationRecord();
        operationRecord.setRecord_id(Generator.generateLongId());
        operationRecord.setProject_id(projectId);
        operationRecord.setOperator_id(operatorId);
        operationRecord.setOperate_type(OperationRecord.OperationType.CREATE_PROJECT);
        operationRecord.setOperator(userService.getNameByUserId(operatorId));
        operationRecordMapper.insert(operationRecord);
    }

    public void assignTester(Long projectId, Long operatorId, List<Long> testerIds) {
        OperationRecord operationRecord = new OperationRecord();
        operationRecord.setRecord_id(Generator.generateLongId());
        operationRecord.setProject_id(projectId);
        operationRecord.setOperator_id(operatorId);
        operationRecord.setOperate_type(OperationRecord.OperationType.ASSIGN_TESTER);
        operationRecord.setOperator(userService.getNameByUserId(operatorId));

        List<Tester> testers = new ArrayList<>();
        for (Long testerId : testerIds) {
            Tester tester = new Tester();
            tester.setTester_id(testerId);
            tester.setTester_name(userService.getNameByUserId(testerId));
            testers.add(tester);
        }
        operationRecord.setTester(JSON.toJSONString(testers));
        operationRecordMapper.insert(operationRecord);
    }

    public void uploadMaterial(Long projectId, Long operatorId, Integer auditType) {
        OperationRecord operationRecord = new OperationRecord();
        operationRecord.setRecord_id(Generator.generateLongId());
        operationRecord.setProject_id(projectId);
        operationRecord.setOperator_id(operatorId);
        operationRecord.setOperate_type(OperationRecord.OperationType.UPLOAD_MATERIAL);
        operationRecord.setAudit_type(auditType);
        operationRecord.setOperator(userService.getNameByUserId(operatorId));
        operationRecordMapper.insert(operationRecord);
    }

    public void materialAudit(Long projectId, Long operatorId, Integer auditResult) {
        OperationRecord operationRecord = new OperationRecord();
        operationRecord.setRecord_id(Generator.generateLongId());
        operationRecord.setProject_id(projectId);
        operationRecord.setOperator_id(operatorId);
        operationRecord.setOperate_type(OperationRecord.OperationType.MATERIAL_AUDIT);
        operationRecord.setAudit_result(auditResult);
        operationRecord.setOperator(userService.getNameByUserId(operatorId));
        operationRecordMapper.insert(operationRecord);
    }
}

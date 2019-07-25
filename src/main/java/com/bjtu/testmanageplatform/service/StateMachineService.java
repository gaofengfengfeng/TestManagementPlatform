package com.bjtu.testmanageplatform.service;

import com.bjtu.testmanageplatform.model.TestProject;
import com.bjtu.testmanageplatform.util.JLog;
import org.springframework.stereotype.Service;

/**
 * @Author: gaofeng
 * @Date: 2019-07-19
 * @Description: 状态机类，设计思路：项目中所有的状态流转都会先走到该类方法中，该类中可以有不同的重载方法，
 * 判断新旧状态是否是可达的
 */
@Service
public class StateMachineService {

    /**
     * 判断新状态是否可达
     *
     * @param oldStatus
     * @param newStatus
     *
     * @return
     */
    public Boolean isReachable(Integer oldStatus, Integer newStatus) {
        JLog.info(String.format("enter isReachable oldStatus=%s newStatus=%s", oldStatus,
                newStatus));

        // 如果项目已经达到终态，则不再支持状态改变，返回false
        if (isTerminated(oldStatus)) {
            JLog.info("already terminated. Can't change status anymore");
            return false;
        }

        // 如果旧状态是定级材料审核失败，则新状态一定得是，定级材料审核中、定级材料审核完成或者项目取消
        if (!isGradeMaterialAuditFailed(oldStatus, newStatus)) {
            JLog.info("new status should be GRADE_MATERIAL_AUDIT_ING or " +
                    "GRADE_MATERIAL_AUDIT_PASSED or PROJECT_CANCELED");
            return false;
        }

        //  如果旧状态是备案失败，则新状态一定得是，备案中、备案失败 或者 项目取消
        if (!isFilingFaild(oldStatus, newStatus)) {
            JLog.info("new status should be FILING_PASSES or FILING_PASSES or PROJECT_CANCELED");
            return false;
        }

        return true;
    }


    /**
     * 判断当前状态是否是终态
     *
     * @param status
     *
     * @return
     */
    private Boolean isTerminated(Integer status) {
        JLog.info(String.format("isTerminated status=%s", status));
        // 如果项目被取消，或者项目已经测试通过，则说明项目已经到达终态
        if (status.equals(TestProject.Status.TEST_PASSED) ||
                status.equals(TestProject.Status.PROJECT_CANCELED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断当前状态是否是定级材料审核失败，如果是，继续判断新状态是否是定级材料审核中或者定级材料审核完成
     *
     * @param oldStatus
     * @param newStatus
     *
     * @return
     */
    private Boolean isGradeMaterialAuditFailed(Integer oldStatus, Integer newStatus) {
        JLog.info(String.format("isGradeMaterialAuditFailed oldStatus=%s newStatus=%s", oldStatus
                , newStatus));
        if (oldStatus.equals(TestProject.Status.GRADE_MATERIAL_AUDIT_FAILED)) {
            if (newStatus.equals(TestProject.Status.GRADE_MATERIAL_AUDIT_ING) ||
                    newStatus.equals(TestProject.Status.GRADE_MATERIAL_AUDIT_PASSED) ||
                    newStatus.equals(TestProject.Status.PROJECT_CANCELED)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }


    /**
     * 判断是否是备案失败，如果是备案失败，则新状态必须是备案中或者备案成功
     *
     * @param oldStatus
     * @param newStatus
     *
     * @return
     */
    private Boolean isFilingFaild(Integer oldStatus, Integer newStatus) {
        JLog.info(String.format("isFilingFaild oldStatus=%s newStatus=%s", oldStatus, newStatus));
        if (oldStatus.equals(TestProject.Status.FILING_FAILED)) {
            if (newStatus.equals(TestProject.Status.ON_FILE) ||
                    newStatus.equals(TestProject.Status.FILING_PASSES) ||
                    newStatus.equals(TestProject.Status.PROJECT_CANCELED)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }


}

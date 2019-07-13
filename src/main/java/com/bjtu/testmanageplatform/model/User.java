package com.bjtu.testmanageplatform.model;

import lombok.Data;

/**
 * @Author: gaofeng
 * @Date: 2019-07-04
 * @Description: 用户实体
 */
@Data
public class User {
    private Long userId;
    private String username;
    private String password;
    private String salt;
    private String name;
    private String phone;
    private String department;
    private String portraitUrl;
    private Integer role;
    private Long createTime;

    public User() {
        userId = 0L;
        username = "";
        password = "";
        salt = "";
        name = "";
        phone = "";
        department = "";
        portraitUrl = "";
        role = Role.UNUSE;
        createTime = System.currentTimeMillis();
    }

    /**
     * 角色常量定义
     */
    public static class Role {
        public final static Integer UNUSE = 0;
        public final static Integer SYSTEM_ADMINISTRATOR = 1; // 系统管理员
        public final static Integer UNDER_TEST_LEADER = 2; // 被测单位负责人
        public final static Integer TEST_LEADER = 3; // 测评单位负责人
        public final static Integer TESTER = 4; // 测评单位测试人员
        public final static Integer LOCAL_ROAD_BUREAU = 5; // 地方路局
        public final static Integer GENERAL_ADMINISTRATION = 5; // 铁路总局
    }
}

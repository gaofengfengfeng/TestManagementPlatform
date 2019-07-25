package com.bjtu.testmanageplatform.util;

/**
 * @Author: gaofeng
 * @Date: 2017-09-04
 * @Description:
 */
public class CodeInfo {
    public static String getClassName(int stackIndex) {
        return Thread.currentThread().getStackTrace()[stackIndex].getClassName();
    }

    public static String getMethodName(int stackIndex) {
        return Thread.currentThread().getStackTrace()[stackIndex].getMethodName();
    }

    public static int getLineNumber(int stackIndex) {
        return Thread.currentThread().getStackTrace()[stackIndex].getLineNumber();
    }

    public static String getFileName(int stackIndex) {
        return Thread.currentThread().getStackTrace()[stackIndex].getFileName();
    }
}
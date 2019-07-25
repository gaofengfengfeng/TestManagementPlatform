package com.bjtu.testmanageplatform.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * @Author: gaofeng
 * @Date: 2017-09-04
 * @Description:
 */
public class JLog {
    private static ThreadLocal<Logger> mLogger = new ThreadLocal<>();
    private static ThreadLocal<String> mLogId = new ThreadLocal<>();
    private static ThreadLocal<ArrayList<String>> mNotices = new ThreadLocal<>();

    protected static Logger getLogger() {
        if (null == mLogger.get()) {
            Logger logger = LoggerFactory.getLogger("JLog");
            mLogger.set(logger);
        }
        return mLogger.get();
    }

    protected static String getLogId() {
        return mLogId.get();
    }

    protected static ArrayList<String> getNotices() {
        ArrayList<String> notices = mNotices.get();
        if (notices == null) {
            notices = new ArrayList<String>();
            mNotices.set(notices);
        }
        return notices;
    }

    protected static void resetNotices() {
        mNotices.set(null);
    }

    public static String resetLogId() {
        Long t = System.currentTimeMillis();
        int r = (int) (Math.random() * 1000000);
        String logId = String.format("%d%d", t, r);
        resetLogId(logId);
        return logId;
    }

    public static String resetLogId(String logId) {
        mLogId.set(logId);
        return logId;
    }

    public static void debug(String s) {
        String logId = getLogId();
        if (null == logId) {
            logId = resetLogId();
        }
        getLogger().debug(String.format("[caller=%s:%d][logId=%s] %s",
                CodeInfo.getClassName(3), CodeInfo.getLineNumber(3),
                logId, s));
    }

    public static void info(String s) {
        String logId = getLogId();
        if (null == logId) {
            logId = resetLogId();
        }
        CodeInfo.getLineNumber(3);
        getLogger().info(String.format("[caller=%s:%d][logId=%s] %s",
                CodeInfo.getClassName(3), CodeInfo.getLineNumber(3),
                logId, s));
    }

    public static void warn(String s) {
        String logId = getLogId();
        if (null == logId) {
            logId = resetLogId();
        }
        getLogger().warn(String.format("[caller=%s:%d][logId=%s] [errNo=0] %s",
                CodeInfo.getClassName(3), CodeInfo.getLineNumber(3),
                logId, s));
    }

    public static void warn(String s, int errNo) {
        String logId = getLogId();
        if (null == logId) {
            logId = resetLogId();
        }
        getLogger().warn(String.format("[caller=%s:%d][logId=%s] [errNo=%d] %s",
                CodeInfo.getClassName(3), CodeInfo.getLineNumber(3),
                logId, errNo, s));
    }

    public static void error(String s, int errNo) {
        String logId = getLogId();
        if (null == logId) {
            logId = resetLogId();
        }
        getLogger().error(String.format("[caller=%s:%d][logId=%s] [errNo=%d] %s",
                CodeInfo.getClassName(3), CodeInfo.getLineNumber(3),
                logId, errNo, s));
    }

    public static void addNotice(String s) {
        getNotices().add(s);
    }

    public static void notice() {
        String logId = getLogId();
        if (null == logId) {
            logId = resetLogId();
        }

        ArrayList<String> notices = getNotices();

        StringBuffer sb = new StringBuffer();
        for (String notice : notices) {
            sb.append("[").append(notice).append("]");
        }
        getLogger().info(String.format("[caller=%s:%d][logId=%s] [NOTICE]%s",
                CodeInfo.getClassName(3), CodeInfo.getLineNumber(3),
                logId, sb.toString()));

        resetNotices();
        resetLogId();
    }
}

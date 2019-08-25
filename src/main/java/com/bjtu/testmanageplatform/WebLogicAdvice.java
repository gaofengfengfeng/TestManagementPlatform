package com.bjtu.testmanageplatform;

import com.alibaba.fastjson.JSON;
import com.bjtu.testmanageplatform.beans.base.JResponse;
import com.bjtu.testmanageplatform.util.JLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: gaofeng
 * @Date: 2019-07-12
 * @Description: 异常处理类，捕获异常集中返回
 */
@RestControllerAdvice
public class WebLogicAdvice {

    @ExceptionHandler
    public JResponse errorHandler(HttpServletRequest request, Exception exception) {
        JResponse jResponse = new JResponse();

        if (exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class)) {
            jResponse.setErr_msg("param error");
            jResponse.setErr_no(101121021);
        } else {
            JLog.error(exception.getMessage(), 101121022);
            jResponse.setErr_no(101121022);
            jResponse.setErr_msg("server busy. exceptionMessage is " + exception.getMessage());
        }
        jResponse.setResponse_time(System.currentTimeMillis());
        JLog.info(JSON.toJSONString(jResponse));
        return jResponse;
    }
}

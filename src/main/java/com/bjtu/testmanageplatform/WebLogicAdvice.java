package com.bjtu.testmanageplatform;

import com.bjtu.testmanageplatform.beans.base.JResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: gaofeng
 * @Date: 2019-07-12
 * @Description: 异常处理类，捕获异常集中返回
 */
@Slf4j
@ControllerAdvice
public class WebLogicAdvice {


    public JResponse errorHandler(HttpServletRequest request, Exception exception) {
        JResponse jResponse = new JResponse();

        if (exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class)) {
            jResponse.setErr_msg("param error");
            jResponse.setErr_no(101121021);
        } else {
            log.error(exception.getMessage());
            jResponse.setErr_no(101121022);
            jResponse.setErr_msg("server busy");
        }
        jResponse.setResponse_time(System.currentTimeMillis());
        return jResponse;
    }
}

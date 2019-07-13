package com.bjtu.testmanageplatform.util;

import com.alibaba.fastjson.JSON;
import com.bjtu.testmanageplatform.beans.base.JResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: gaofeng
 * @Date: 2019-07-13
 * @Description:
 */
@Slf4j
public class JInterceptor extends HandlerInterceptorAdapter {
    private final Class<? extends JResponse> jResponseClass;

    public JInterceptor() {
        jResponseClass = JResponse.class;
    }

    public JInterceptor(Class<? extends JResponse> jResponseClass) {
        this.jResponseClass = jResponseClass;
    }

    /**
     * @Author:gaofeng
     * @Date:2018/5/24
     * @Description:在处理请求开始时，设置请求开始时间，初始化JResponse对象
     **/
    public static boolean doPreHandle(HttpServletRequest request,
                                      Class<? extends JResponse> jResponseClass) {

        request.setAttribute("beginReqUri", request.getRequestURI());

        Long reqStartTime = System.currentTimeMillis();
        request.setAttribute("reqStartTime", reqStartTime);

        try {
            JResponse jResponse = jResponseClass.newInstance();
            request.setAttribute("jResponse", jResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * @Author:gaofeng
     * @Date:2018/5/24
     * @Description: 在请求完成时，打印相应的时间、错误号、notice日志等
     **/
    public static void doAfterCompletion(HttpServletRequest request) {
        // Long reqStartTime = (Long) request.getAttribute("reqStartTime");
        // Long reqEndTime = System.currentTimeMillis();
        // Long reqCostTime = reqEndTime - reqStartTime;
        //
        // log.info("beginReqUri=" + request.getAttribute("beginReqUri"));
        // log.info("endReqUri=" + request.getRequestURI());
        // log.info("costTime=" + reqCostTime);
        //
        //
        // JResponse jResponse = (JResponse) request.getAttribute("jResponse");
        // log.info("errNo=" + jResponse.getErr_no());
        // log.info("serverTime=" + jResponse.getResponse_time());
        // if (jResponse.getResponse_time() == null || jResponse.getResponse_time().equals(0L)) {
        //     log.warn("JResponse common param serverTime=null");
        // }
        //
        // if (jResponse.getErr_no() != 0) {
        //     log.warn("request occur some error. endReqUri=" + request.getRequestURI() + " errMsg" +
        //             "=" + jResponse.getErr_msg(), jResponse.getErr_no());
        // }
        //
        //log.info("responseBody=" + JSON.toJSONString(jResponse));
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        return doPreHandle(request, jResponseClass);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        doAfterCompletion(request);
    }
}

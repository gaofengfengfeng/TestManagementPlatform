package com.bjtu.testmanageplatform;

import com.alibaba.fastjson.JSON;
import com.bjtu.testmanageplatform.beans.base.JRequest;
import com.bjtu.testmanageplatform.beans.base.JResponse;
import com.bjtu.testmanageplatform.beans.base.TokenObject;
import com.bjtu.testmanageplatform.beans.base.UserProfile;
import com.bjtu.testmanageplatform.util.Generator;
import com.bjtu.testmanageplatform.util.service.JRedisPoolService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @Author: gaofeng
 * @Date: 2019-07-12
 * @Description:
 */
@Slf4j
@Aspect
@Configuration
public class WebLogicAspect {

    @Pointcut("execution(public * com.bjtu.testmanageplatform.controller..*(..)) && @annotation" +
            "(org.springframework.web.bind.annotation.RequestMapping)")
    public void webLogic() {
    }

    @Around("webLogic()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 取到request请求
        RequestAttributes requestAttribute = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) requestAttribute;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        if (request.getRequestURI().equals("/v1/file/upload") ||
                request.getRequestURI().equals("/v1/user/createAdministrator") ||
                request.getRequestURI().equals("/v1/user/login")) {
            return proceedingJoinPoint.proceed();
        }

        // 取出在JMessageConverter中放置的 _requestBody属性，里面是请求体字符串
        String requestBody = (String) request.getAttribute("_requestBody");
        log.info(requestBody);
        JRequest jRequest = JSON.parseObject(requestBody, JRequest.class);

        // 取得拦截方法的返回类型
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();

        // 检查token是否与redis中的相同
        // 用户身份鉴权、token校验
        // TokenObject tokenObject = Generator.parseToken(jRequest.getToken());
        UserProfile userProfile = jRequest.getUser_profile();

        Jedis jedis = JRedisPoolService.getInstance(InitConfig.REDIS_POOL);
        String tokenInRedis = jedis.get(InitConfig.LOGIN_TOKEN_PRE + userProfile.getUser_id());

        if (!jRequest.getToken().equals(tokenInRedis)) {
            JResponse jResponse = (JResponse) targetMethod.getReturnType().newInstance();
            jResponse.setErr_no(101232110);
            jResponse.setErr_msg("wrong token");
            return jResponse;
        }


        // 判断返回类型是否是JResponse的子类
        // boolean isFather = JResponse.class.isAssignableFrom(targetMethod.getReturnType());

        // JResponse jResponse = (JResponse) targetMethod.getReturnType().newInstance();
        // jResponse.setErrNo(10000000);
        // jResponse.setErrMsg("aspect");
        // return jResponse;
        return proceedingJoinPoint.proceed();
    }

    @AfterReturning(returning = "jResponse", pointcut = "webLogic()")
    public void doAfterReturning(JResponse jResponse) throws Throwable {
        jResponse.setResponse_time(System.currentTimeMillis());
        log.info(JSON.toJSONString(jResponse));
    }

}

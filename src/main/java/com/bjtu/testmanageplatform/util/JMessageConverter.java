package com.bjtu.testmanageplatform.util;

import com.alibaba.fastjson.JSON;
import com.bjtu.testmanageplatform.beans.base.JRequest;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: gaofeng
 * @Date: 2019-07-13
 * @Description:
 */
public class JMessageConverter implements HttpMessageConverter {

    List<MediaType> supportedMediaTypes;

    public JMessageConverter() {
        supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.MULTIPART_FORM_DATA);
    }

    @Override
    public boolean canRead(Class aClass, MediaType mediaType) {
        return JRequest.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean canWrite(Class aClass, MediaType mediaType) {
        return false;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return supportedMediaTypes;
    }

    @Override
    public Object read(Class aClass, HttpInputMessage httpInputMessage) throws IOException,
            HttpMessageNotReadableException {
        JLog.info("calling JMessageConverter.read");
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        Object object = null;
        InputStream in = httpInputMessage.getBody();
        int len = 0;
        len = in.available();
        if (len > 0) {
            byte[] buffer = new byte[len];
            int pos = 0;
            int size = 0;

            while ((size = in.read(buffer, pos, len - pos)) > 0) {
                if (size < len - pos || len < 1) {
                    break;
                }
            }

            String body = new String(buffer, Charset.forName("utf-8"));
            JLog.info("reqBody is" + body);
            request.setAttribute("_requestBody", body);

            object = JSON.parseObject(body, aClass);
            if (null == object || !(object instanceof JRequest)) {
                object = new JRequest();
            }
        }
        return object;
    }

    @Override
    public void write(Object o, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {

    }
}

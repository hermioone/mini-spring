package org.hermione.minis.web.servlet;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hermione.minis.web.WebApplicationContext;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("FieldCanBeLocal")
@Slf4j
public class RequestMappingHandlerAdapter implements HandlerAdapter {
    private final WebApplicationContext wac;

    public RequestMappingHandlerAdapter(WebApplicationContext wac) {
        this.wac = wac;
    }

    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        handleInternal(request, response, (HandlerMethod) handler);
    }

    private void handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        Method method = handler.getMethod();
        Object obj = handler.getBean();
        Object objResult = null;
        try {
            objResult = method.invoke(obj);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        try {
            response.getWriter().append(Objects.requireNonNull(objResult).toString());
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }
}

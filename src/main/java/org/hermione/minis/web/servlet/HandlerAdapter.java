package org.hermione.minis.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通过反射调用相应的 controller 方法
 */
public interface HandlerAdapter {
    void handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}

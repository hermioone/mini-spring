package org.hermione.minis.web.servlet;


import javax.servlet.http.HttpServletRequest;

/**
 * 处理 URL 映射
 * 根据 URL 找到对应的方法和对象
 */
public interface HandlerMapping {
    HandlerMethod getHandler(HttpServletRequest request) throws Exception;
}




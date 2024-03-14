package org.hermione.minis.web;


import javax.servlet.http.HttpServletRequest;

public class WebDataBinderFactory {

    /**
     *
     * @param request
     * @param parameter         Controller 中 method 的一个参数对象
     * @return
     */
    public WebDataBinder createBinder(HttpServletRequest request, Object parameter) {
        WebDataBinder wbd = new WebDataBinder(parameter);
        initBinder(wbd, request);
        return wbd;
    }
    protected void initBinder(WebDataBinder dataBinder, HttpServletRequest request) {
    }
}
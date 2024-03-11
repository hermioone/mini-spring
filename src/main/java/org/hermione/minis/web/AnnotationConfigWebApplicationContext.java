package org.hermione.minis.web;


import org.hermione.minis.context.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;


public class AnnotationConfigWebApplicationContext extends ClassPathXmlApplicationContext implements WebApplicationContext {
    private ServletContext servletContext;

    /**
     * @param fileName applicationContext.xml，里面定义了 Spring IoC 的 Bean
     */
    public AnnotationConfigWebApplicationContext(String fileName) {
        // 调用 ClassPathXmlApplicationContext 的初始化函数
        super(fileName);
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}

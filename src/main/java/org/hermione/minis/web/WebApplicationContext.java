package org.hermione.minis.web;


import javax.servlet.ServletContext;
import org.hermione.minis.context.ApplicationContext;

public interface WebApplicationContext extends ApplicationContext {
    String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";
    String WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName();

    ServletContext getServletContext();
    void setServletContext(ServletContext servletContext);
}

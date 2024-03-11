package org.hermione.minis.web;


import lombok.Getter;
import lombok.Setter;
import org.hermione.minis.context.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;

public class XmlWebApplicationContext extends ClassPathXmlApplicationContext implements WebApplicationContext{

    @Getter
    @Setter
    private ServletContext servletContext;

    public XmlWebApplicationContext(String fileName) {
        super(fileName);
    }

}

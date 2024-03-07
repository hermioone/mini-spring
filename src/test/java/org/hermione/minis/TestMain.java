package org.hermione.minis;

import org.hermione.minis.beans.BeansException;
import org.hermione.minis.context.ClassPathXmlApplicationContext;
import org.junit.Test;

public class TestMain {

    @Test
    public void testClassPathXmlApplicationContext() throws BeansException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        AService aService = (AService) ctx.getBean("aservice");
        aService.sayHello();

        System.out.println("############");

        BaseService baseService = (BaseService) ctx.getBean("baseservice");
        baseService.sayHello();
    }
}

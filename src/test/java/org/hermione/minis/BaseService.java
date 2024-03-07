package org.hermione.minis;

import org.hermione.minis.beans.factory.annotation.Autowired;

public class BaseService {

    @Autowired(value = "basebaseservice")
    private BaseBaseService bbs;

    public void sayHello() {
        System.out.println("-------- BaseService start --------");
        System.out.println("Base Service says hello");
        bbs.sayHello();
        System.out.println("-------- BaseService end --------");
    }
}

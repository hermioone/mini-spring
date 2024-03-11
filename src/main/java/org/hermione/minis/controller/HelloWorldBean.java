package org.hermione.minis.controller;


import org.hermione.minis.beans.factory.annotation.Autowired;
import org.hermione.minis.web.RequestMapping;

public class HelloWorldBean {

    @Autowired(value = "helloWorldService")
    private HelloWorldService helloWorldService;

    /**
     * value 是 bean 的 id
     */
    @RequestMapping(value = "/test1")
    public String doGet() {
        return helloWorldService.hello();
    }

    @RequestMapping("/test2")
    public String doPost() {
        return "hello world!";
    }
}

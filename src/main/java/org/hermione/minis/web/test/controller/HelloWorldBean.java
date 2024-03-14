package org.hermione.minis.web.test.controller;


import org.hermione.minis.beans.factory.annotation.Autowired;
import org.hermione.minis.web.RequestMapping;
import org.hermione.minis.web.common.ModelAttribute;
import org.hermione.minis.web.common.RequestParam;
import org.hermione.minis.web.test.User;


public class HelloWorldBean {

    @Autowired(value = "helloWorldService")
    private HelloWorldService helloWorldService;

    /**
     * value 是 bean 的 id
     */
    @RequestMapping(value = "/test1")
    public String doGet2(String name, @RequestParam int age) {
        return "name: " + name + ", age: " + age + " -> " + helloWorldService.hello();
    }

    @RequestMapping("/test2")
    public String doGet2(@ModelAttribute User user) {
        return "hello world!: " + user.getName();
    }
}

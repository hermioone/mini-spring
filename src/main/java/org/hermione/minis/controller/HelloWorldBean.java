package org.hermione.minis.controller;


import org.hermione.minis.web.RequestMapping;

public class HelloWorldBean {

    @RequestMapping("/test1")
    public String doGet() {
        return "Lei Hou!";
    }

    @RequestMapping("/test2")
    public String doPost() {
        return "hello world!";
    }
}

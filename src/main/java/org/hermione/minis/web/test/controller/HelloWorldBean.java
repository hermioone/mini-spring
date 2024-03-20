package org.hermione.minis.web.test.controller;


import org.hermione.minis.beans.factory.annotation.Autowired;
import org.hermione.minis.web.RequestMapping;
import org.hermione.minis.web.common.ModelAttribute;
import org.hermione.minis.web.common.RequestParam;
import org.hermione.minis.web.common.ResponseBody;
import org.hermione.minis.web.test.User;
import org.hermione.minis.web.test.service.UserService;
import org.hermione.minis.web.view.ModelAndView;

import java.util.Date;
import java.util.List;


public class HelloWorldBean {

    @Autowired(value = "helloWorldService")
    private HelloWorldService helloWorldService;

    @Autowired(value = "userService")
    private UserService userService;

    /**
     * value 是 bean 的 id
     */
    @RequestMapping(value = "/test1")
    public ModelAndView doGet2(String name, @RequestParam int age) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("helloJsp");
        mav.addAttribute("name", name);
        mav.addAttribute("age", age);
        return mav;
    }

    @ResponseBody
    @RequestMapping("/test3")
    public User doGet3(@ModelAttribute User user) {
        user.setBirthday(new Date());
        return user;
    }

    @ResponseBody
    @RequestMapping(("/test4"))
    public List<User> doGet4(@RequestParam int id) {
        return userService.getUserInfo(id);
    }

    @ResponseBody
    @RequestMapping(("/test5"))
    public User doGet5(@RequestParam int id) {
        return userService.getUserInfo2(id);
    }
}

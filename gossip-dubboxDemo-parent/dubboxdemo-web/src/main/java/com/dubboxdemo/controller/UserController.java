package com.dubboxdemo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dubboxdemo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: WK
 * @Data: 2019/7/21 1:11
 * @Description: com.dubboxdemo.controller
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;
    @RequestMapping("/showName")
    @ResponseBody
    public String showName(){
        return userService.getName();
    }
}
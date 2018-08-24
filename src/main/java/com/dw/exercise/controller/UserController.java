package com.dw.exercise.controller;

import com.dw.exercise.vo.AuthUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/login")
    public String login(AuthUser user){
        return "xxx";
    }
    @RequestMapping("/register")
    public String register(AuthUser user) {
        return "";
    }
}

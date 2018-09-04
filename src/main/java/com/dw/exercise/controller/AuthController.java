package com.dw.exercise.controller;

import com.dw.exercise.entity.User;
import com.dw.exercise.service.AuthService;
import com.dw.exercise.vo.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;
    @RequestMapping("/signup")
    public User signup(@RequestBody AuthUser authUser, HttpServletResponse response){
        return authService.singUp(authUser);

    }
    @RequestMapping("/signin")
    public void signIn(@RequestBody AuthUser authUser, HttpServletResponse response) throws IOException {
        String token = authService.signIn(authUser.getUsername(), authUser.getPassword());
        response.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
        String result = "{\"token\":\"" + token+ "\"}";
        PrintWriter writer = response.getWriter();
        writer.write(result);
        writer.flush();
        writer.close();
    }
}

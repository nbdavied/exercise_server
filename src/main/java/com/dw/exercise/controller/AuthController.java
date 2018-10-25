package com.dw.exercise.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dw.exercise.dao.UserDAO;
import com.dw.exercise.entity.User;
import com.dw.exercise.manager.MiniAppManager;
import com.dw.exercise.model.dto.MiniAppOpenidSession;
import com.dw.exercise.service.AuthService;
import com.dw.exercise.vo.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.dw.exercise.security.SecurityConstants.EXPIRATION_TIME;
import static com.dw.exercise.security.SecurityConstants.SECRET;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;
    @Resource
    UserDAO userDAO;
    @Autowired
    MiniAppManager miniAppManager;

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
    @RequestMapping("/refresh")
    public void refreshToken(String token, HttpServletResponse response) throws IOException {
        DecodedJWT jwt =  JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token);
        String username = jwt.getSubject();
        User user = userDAO.getUserByUsername(username);
        String newtoken = JWT.create()
                .withSubject(username)
                .withClaim("uid", user.getId())
                .withClaim("nic", user.getNickname())
                .withArrayClaim("rol",  user.getRoles().toArray(new String[0]))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        response.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
        String result = "{\"token\":\"" + newtoken+ "\"}";
        PrintWriter writer = response.getWriter();
        writer.write(result);
        writer.flush();
        writer.close();
    }
    @PostMapping("/wxlogin")
    public void wxLogin(String code){
        MiniAppOpenidSession loginResult = miniAppManager.code2Session(code);

    }
}

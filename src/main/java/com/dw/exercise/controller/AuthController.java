package com.dw.exercise.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dw.exercise.dao.UserAuthDAO;
import com.dw.exercise.dao.UserDAO;
import com.dw.exercise.entity.User;
import com.dw.exercise.entity.UserAuth;
import com.dw.exercise.manager.MiniAppManager;
import com.dw.exercise.model.dto.MiniAppOpenidSession;
import com.dw.exercise.service.AuthService;
import com.dw.exercise.vo.AuthUser;
import com.dw.exercise.vo.MiniAppLogin;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
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
    @Resource
    UserAuthDAO userAuthDAO;
    @Autowired
    MiniAppManager miniAppManager;

    @RequestMapping("/signup")
    @Transactional
    public User signup(@RequestBody AuthUser authUser, HttpServletResponse response){
        User user = authUser.toUser();
        UserAuth auth = authUser.toUserAuth();
        auth.setIdentityType("username");
        return authService.signup(user, auth);

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
        Integer userid = Integer.parseInt(jwt.getSubject());
        User user = userDAO.getUserById(userid);
        String newtoken = JWT.create()
                .withSubject(userid.toString())
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
    //微信小程序登陆
    @PostMapping("/wxlogin")
    public void wxLogin(@RequestBody MiniAppLogin login, HttpServletResponse response) throws IOException {
        MiniAppOpenidSession loginResult = miniAppManager.code2Session(login.getCode());
        if(loginResult.getErrcode() != null && loginResult.getErrcode() != 0){
            throw new RuntimeException("微信登陆失败");
        }
        UserAuth auth = new UserAuth();
        auth.setIdentifier(loginResult.getOpenid());
        auth.setIdentityType("miniapp");
        auth = userAuthDAO.getUserAuthByIdentifierAndType(auth);
        User user = null;
        if(auth == null){
            //首次登陆
            auth = new UserAuth();
            auth.setIdentityType("miniapp");
            auth.setIdentifier(loginResult.getOpenid());
            auth.setToken(loginResult.getSessionKey());
            Date date = new Date();
            user = new User();
            user.setNickname(RandomStringUtils.randomAlphanumeric(16));
            auth.setGenTime(date);
            user.setRegTime(date);
            auth.setLastLoginTime(date);
            authService.signup(user, auth);
        }else{
            //更新sessionKey
            auth.setToken(loginResult.getSessionKey());
            Date date = new Date();
            auth.setLastLoginTime(date);
            userDAO.updateAuthTokenAndLoginTime(auth);
            user = userDAO.getUserById(auth.getUserId());
        }
        String token = JWT.create()
                .withSubject(String.valueOf(auth.getUserId()))
                .withClaim("nic", "")
                .withArrayClaim("rol",  user.getRoles().toArray(new String[0]))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        response.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
        String result = "{\"token\":\"" + token+ "\"}";
        PrintWriter writer = response.getWriter();
        writer.write(result);
        writer.flush();
        writer.close();
    }
}

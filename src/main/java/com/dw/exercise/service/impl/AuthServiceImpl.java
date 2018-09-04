package com.dw.exercise.service.impl;

import com.auth0.jwt.JWT;
import com.dw.exercise.dao.UserDAO;
import com.dw.exercise.entity.User;
import com.dw.exercise.entity.UserAuth;
import com.dw.exercise.security.JwtUser;
import com.dw.exercise.service.AuthService;
import com.dw.exercise.vo.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.dw.exercise.security.SecurityConstants.EXPIRATION_TIME;
import static com.dw.exercise.security.SecurityConstants.SECRET;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Resource
    private UserDAO userDAO;

    @Override
    public User singUp(AuthUser authUser) {
        if(StringUtils.isEmpty(authUser.getUsername())){
            throw new RuntimeException("用户名不能为空");
        }
        if(StringUtils.isEmpty(authUser.getPassword())){
            throw new RuntimeException("密码不能为空");
        }
        if(StringUtils.isEmpty(authUser.getNickname())){
            throw new RuntimeException("昵称不能为空");
        }
        if(userDAO.getUserByUsername(authUser.getUsername()) != null){
            throw new RuntimeException("用户名已存在");
        }
        User user = authUser.toUser();
        UserAuth auth = authUser.toUserAuth();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Date now = new Date();
        user.setRegTime(now);
        auth.setGenTime(now);
        auth.setToken(encoder.encode(auth.getToken()));
        auth.setServer("local");
        List<String> roles = new ArrayList<String>();
        roles.add("ROLE_USER");
        user.setRoles(roles);
        userDAO.createUser(user);
        auth.setUserId(user.getId());
        userDAO.createUserAuth(auth);
        return user;
    }

    @Override
    public String signIn(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userDAO.getUserByUsername(username);
        String token = JWT.create()
                .withSubject(username)
                .withClaim("uid", user.getId())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        return token;
    }

    @Override
    public String refresh(String oldToken) {
        return null;
    }
}

package com.dw.exercise.service.impl;

import com.auth0.jwt.JWT;
import com.dw.exercise.dao.UserAuthDAO;
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
    @Resource
    private UserAuthDAO userAuthDAO;

    @Override
    public User signup(User user, UserAuth auth) {
        if(userAuthDAO.getUserAuthByIdentifierAndType(auth) != null){
            throw new RuntimeException("用户已存在");
        }
        if("username".equals(auth.getIdentityType())) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            auth.setToken(encoder.encode(auth.getToken()));
        }
        Date now = new Date();
        user.setRegTime(now);
        auth.setGenTime(now);
        auth.setLastLoginTime(now);
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        user.setRoles(roles);
        userDAO.createUser(user);
        auth.setUserId(user.getId());
        userDAO.createUserAuth(auth);
        return user;
    }

    /**
     * 用户名密码登陆
     * @param username
     * @param password
     * @return
     */
    @Override
    public String signIn(String username, String password) {
//        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
//        final Authentication authentication = authenticationManager.authenticate(upToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        //User user = userDAO.getUserByUsername(username);
        UserAuth auth = new UserAuth();
        auth.setIdentifier(username);
        auth.setIdentityType("username");
        auth = userAuthDAO.getUserAuthByIdentifierAndType(auth);
        if(auth == null){
            throw new RuntimeException("用户名或密码错误");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if(!encoder.matches(password, auth.getToken())){
            throw new RuntimeException("用户名或密码错误");
        }
        User user = userDAO.getUserById(auth.getUserId());
        String token = JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withClaim("nic", user.getNickname())
                .withArrayClaim("rol",  user.getRoles().toArray(new String[0]))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        return token;
    }

    @Override
    public String refresh(String oldToken) {
        return null;
    }
}

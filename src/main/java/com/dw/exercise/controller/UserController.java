package com.dw.exercise.controller;

import com.dw.exercise.entity.User;
import com.dw.exercise.entity.UserAuth;
import com.dw.exercise.dao.UserDAO;
import com.dw.exercise.vo.AuthUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController()
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserDAO userDAO;

    @RequestMapping("/login")
    public String login(AuthUser user){
        return "xxx";
    }
    @PostMapping("/sign-up")
    public String signUp(@RequestBody AuthUser authUser, HttpServletResponse response) {
        if(StringUtils.isEmpty(authUser.getUsername())){
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return "fail";
        }
        if(StringUtils.isEmpty(authUser.getPassword())){
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return "fail";
        }
        User user = authUser.toUser();
        UserAuth auth = authUser.toUserAuth();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Date now = new Date();
        user.setRegTime(now);
        auth.setGenTime(now);
        auth.setToken(encoder.encode(auth.getToken()));
        auth.setServer("local");
        List<String> roles = new ArrayList<>();
        roles.add("user");
        user.setRoles(roles);
        userDAO.createUser(user);
        auth.setUserId(user.getId());
        userDAO.createUserAuth(auth);
        return "";
    }
    @GetMapping()
    User getUsers(){
        User user = userDAO.getUserByUsername("testuser2");
        return user;
    }
}

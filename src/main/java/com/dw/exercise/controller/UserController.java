package com.dw.exercise.controller;

import com.dw.exercise.entity.User;
import com.dw.exercise.entity.UserAuth;
import com.dw.exercise.dao.UserDAO;
import com.dw.exercise.vo.AuthUser;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    @Resource
    private UserDAO userDAO;


    @GetMapping("/all")
    List<User> getUsers(){
        return userDAO.getAllUsers();
    }
}

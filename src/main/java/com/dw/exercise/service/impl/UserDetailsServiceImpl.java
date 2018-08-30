package com.dw.exercise.service.impl;


import com.dw.exercise.entity.User;
import com.dw.exercise.entity.UserAuth;
import com.dw.exercise.dao.UserDAO;
import com.dw.exercise.security.JwtUserFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService{
    @Resource
    UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.getUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException(username);
        }
        UserAuth auth = new UserAuth();
        auth.setUserId(user.getId());
        auth.setServer("local");
        auth = userDAO.getUserAuthByIdAndServer(auth);
        return JwtUserFactory.create(user, auth);
    }
}

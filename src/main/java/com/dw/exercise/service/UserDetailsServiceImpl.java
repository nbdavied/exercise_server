package com.dw.exercise.service;


import com.dw.exercise.entity.UserAuth;
import com.dw.exercise.mapper.UserMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static java.util.Collections.emptyList;

@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService{
    @Resource
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.dw.exercise.entity.User appuser = userMapper.getUserByUsername(username);
        if(appuser == null){
            throw new UsernameNotFoundException(username);
        }
        UserAuth authinfo = new UserAuth();
        authinfo.setUserId(appuser.getId());
        authinfo.setServer("local");
        authinfo = userMapper.getUserAuthByIdAndServer(authinfo);
        return new User(appuser.getUsername(), authinfo.getToken(), emptyList());
    }
}

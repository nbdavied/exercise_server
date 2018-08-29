package com.dw.exercise.security;

import com.dw.exercise.entity.User;
import com.dw.exercise.entity.UserAuth;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JwtUserFactory {
    public JwtUserFactory() {
    }
    public static JwtUser create(User user, UserAuth auth){
        return new JwtUser(
                user.getId(),user.getUsername(),auth.getToken(),mapToGrantedAuthorities(user.getRoles())
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> roles) {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}

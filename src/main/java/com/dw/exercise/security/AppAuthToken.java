package com.dw.exercise.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AppAuthToken extends UsernamePasswordAuthenticationToken {
    private Integer userId;

    public AppAuthToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public AppAuthToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public AppAuthToken(Object principal, Object credentials, Integer userId, Collection<? extends GrantedAuthority> authorities ) {
        super(principal, credentials, authorities);
        this.userId = userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getUserId() {
        return userId;
    }

}

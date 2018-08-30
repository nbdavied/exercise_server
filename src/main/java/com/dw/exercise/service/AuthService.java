package com.dw.exercise.service;

import com.dw.exercise.entity.User;
import com.dw.exercise.vo.AuthUser;

public interface AuthService {
    User singUp(AuthUser user);
    String signIn(String username, String password);
    String refresh(String oldToken);
}

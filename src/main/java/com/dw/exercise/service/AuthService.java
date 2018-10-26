package com.dw.exercise.service;

import com.dw.exercise.entity.User;
import com.dw.exercise.entity.UserAuth;

public interface AuthService {
    User signup(User user, UserAuth auth);
    String signIn(String username, String password);
    String refresh(String oldToken);
}

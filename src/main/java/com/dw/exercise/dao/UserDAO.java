package com.dw.exercise.dao;

import com.dw.exercise.entity.User;
import com.dw.exercise.entity.UserAuth;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDAO {
    int createUser(User user);

    int createUserAuth(UserAuth auth);

    User getUserByUsername(String username);

    UserAuth getUserAuthByIdAndServer(UserAuth auth);
}

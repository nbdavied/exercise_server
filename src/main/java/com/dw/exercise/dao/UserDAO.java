package com.dw.exercise.dao;

import com.dw.exercise.entity.User;
import com.dw.exercise.entity.UserAuth;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDAO {
    int createUser(User user);

    User getUserByUsername(String username);

    List<User> getAllUsers();

    UserAuth getUserAuthByIdAndServer(UserAuth auth);

    int createUserAuth(UserAuth auth);
}

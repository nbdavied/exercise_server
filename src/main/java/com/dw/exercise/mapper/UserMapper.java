package com.dw.exercise.mapper;

import com.dw.exercise.entity.User;
import com.dw.exercise.entity.UserAuth;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public int createUser(User user);

    public int createUserAuth(UserAuth auth);

    public User getUserByUsername(String username);

    public UserAuth getUserAuthByIdAndServer(UserAuth auth);
}

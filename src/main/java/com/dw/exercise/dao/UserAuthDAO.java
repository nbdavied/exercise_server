package com.dw.exercise.dao;

import com.dw.exercise.entity.UserAuth;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAuthDAO {
    UserAuth getUserAuthByIdentifierAndType(UserAuth auth);
}

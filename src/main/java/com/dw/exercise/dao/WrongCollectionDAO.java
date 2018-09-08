package com.dw.exercise.dao;

import com.dw.exercise.entity.WrongCollection;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WrongCollectionDAO {
    int insert(WrongCollection collection);

    WrongCollection randomlySelect(WrongCollection collection);

    int deleteWrongCollection(WrongCollection collection);

    int deleteInBank(int bankId);
}

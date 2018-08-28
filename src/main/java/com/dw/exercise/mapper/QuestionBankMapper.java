package com.dw.exercise.mapper;

import com.dw.exercise.entity.QuestionBank;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper()
public interface QuestionBankMapper {
    List<QuestionBank> getBanks();
}

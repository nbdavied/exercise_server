package com.dw.exercise.dao;

import com.dw.exercise.entity.QuestionBank;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper()
public interface QuestionBankDAO {
    List<QuestionBank> getBanks();
    int createBank(QuestionBank bank);
}

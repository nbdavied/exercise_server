package com.dw.exercise.dao;

import com.dw.exercise.entity.Choice;
import com.dw.exercise.entity.Question;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionDAO {
    int createQuestion(Question q);

    Question getNextQuestionInBankId(Question q);

    Question getQuestionRandomlyInBankId(int bankId);

    Choice getChoiceById(int id);
}

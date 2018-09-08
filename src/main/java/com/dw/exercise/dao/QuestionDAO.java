package com.dw.exercise.dao;

import com.dw.exercise.entity.Choice;
import com.dw.exercise.entity.Question;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface QuestionDAO {
    int createQuestion(Question q);
    int insertChoice(Choice c);

    Question getNextQuestionInBankId(Question q);

    Question getNextQuestionInWrongCollection(Integer bankId, Integer lastId, Integer userId);

    Question getQuestionRandomlyInBankId(int bankId);

    Question getQuestionById(int id);

    Choice getChoiceById(int id);

    Integer countQuestionWithBankAndType(Question q);

    int deleteChoicesInBank(int bankId);

    int deleteQuestionsInBank(int bankId);
}

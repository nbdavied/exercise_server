package com.dw.exercise.dao;

import com.dw.exercise.entity.Choice;
import com.dw.exercise.entity.Question;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QuestionDAO {
    int createQuestion(Question q);
    int insertChoice(Choice c);

    Question getNextQuestionInBankId(Question q);

    /**
     * 从错题记录中选取下一题
     * @param map key: bankId, lastId, userId
     * @return
     */
    Question getNextQuestionInWrongCollection(Map<String, Object> map);

    Question getQuestionRandomlyInBankId(int bankId);

    Question getQuestionById(int id);

    Choice getChoiceById(int id);

    Integer countQuestionWithBankAndType(Question q);

    int deleteChoicesInBank(int bankId);

    int deleteQuestionsInBank(int bankId);

    /**
     * 获得题库中指定题型的所有题目id
     * @param q 传入bankId,type
     * @return
     */
    List<Integer> getQuestionIdsWithBankAndType(Question q);

    /**
     * 获取指定试卷的指定题型题目
     * @param map 传入paperId, type
     * @return
     */
    List<Question> getQuestionsInPaper(Map<String, Object> map);
}

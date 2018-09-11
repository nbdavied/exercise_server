package com.dw.exercise.dao;

import com.dw.exercise.entity.PaperQuestion;
import com.dw.exercise.entity.TestPaper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PaperDAO {
    int createPaper(TestPaper paper);

    /**
     * 获得某用户当天最大试卷号
     * @param map 传入 date, userId
     * @return
     */
    String getMaxPaperNoOfDate(Map<String, Object> map);

    /**
     * 批量插入题目
     * @param paperQuestions
     * @return
     */
    int batchInsertPaperQuestion(List<PaperQuestion> paperQuestions);
    List<TestPaper> getPaperOfUser(Integer userId);
}

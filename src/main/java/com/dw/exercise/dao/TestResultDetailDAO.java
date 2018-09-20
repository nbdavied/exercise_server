package com.dw.exercise.dao;

import com.dw.exercise.entity.QuestionWithAnswerAndSelect;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TestResultDetailDAO {
    void saveResultDetail(Integer resultId);
    List<QuestionWithAnswerAndSelect> getDetail(Map<String, Object> map);
}

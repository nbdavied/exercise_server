package com.dw.exercise.mapper;

import com.dw.exercise.entity.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionMapper {
    public int createQuestion(Question q);
}

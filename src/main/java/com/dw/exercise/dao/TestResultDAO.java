package com.dw.exercise.dao;

import com.dw.exercise.entity.TestResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestResultDAO {
    int insert(TestResult result);
    List<TestResult> getResultByPaperId(Integer paperId);
}

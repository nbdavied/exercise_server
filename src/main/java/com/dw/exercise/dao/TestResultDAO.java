package com.dw.exercise.dao;

import com.dw.exercise.entity.TestResult;

import java.util.List;

public interface TestResultDAO {
    int insert(TestResult result);
    List<TestResult> getResultByPaperId(Integer paperId);
}

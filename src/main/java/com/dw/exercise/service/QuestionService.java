package com.dw.exercise.service;

import com.dw.exercise.vo.QuestionWithAnswer;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

@Service
public class QuestionService {
    public List<QuestionWithAnswer> parseExcel(Workbook wb){
        return null;
    }
    public List<QuestionWithAnswer> parseDoc(){
        return null;
    }
    public List<QuestionWithAnswer> parse(File file){
        return null;
    }
    public List<QuestionWithAnswer> parse(InputStream is){
        return null;
    }
}

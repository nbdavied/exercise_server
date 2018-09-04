package com.dw.exercise.service;

import com.dw.exercise.entity.Choice;
import com.dw.exercise.vo.QuestionWithAnswer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    public List<QuestionWithAnswer> parseExcel(Workbook wb){
        Sheet singleSheet = wb.getSheet("单选题");
        List<QuestionWithAnswer> list = new ArrayList<>();
        for(Row row : singleSheet){
            QuestionWithAnswer q = parseSingleMultiRow(row);
            list.add(q);
        }
        Sheet multiSheet = wb.getSheet("多选题");
        for(Row row : multiSheet){
            QuestionWithAnswer q = parseSingleMultiRow(row);
            list.add(q);
        }
        Sheet tfSheet = wb.getSheet("判断题");
        for(Row row : tfSheet){
            QuestionWithAnswer q = parseTfRow(row);
            list.add(q);
        }

        return list;
    }

    public List<QuestionWithAnswer> parseDoc(){
        return null;
    }

    public List<QuestionWithAnswer> parse(File file){
        return null;
    }
    public List<QuestionWithAnswer> parse(InputStream is) throws IOException, InvalidFormatException {
        Workbook wb = WorkbookFactory.create(is);
        return parseExcel(wb);
    }
    private QuestionWithAnswer parseSingleMultiRow(Row row){
        final String question = row.getCell(0).getStringCellValue();
        String answerString = row.getCell(1).getStringCellValue();
        int[] rightIndex = parseAnswer(answerString);
        final String type = rightIndex.length > 1 ? "m" : "s";
        final List<Choice>  rightChoices = new ArrayList<>();
        for (int index : rightIndex){
            Choice choice = new Choice();
            String text = row.getCell(index + 2).getStringCellValue();
            choice.setText(text);
            rightChoices.add(choice);
        }
        final List<Choice> wrongChoices = new ArrayList<>();
        for(int i = 2; i <= row.getLastCellNum(); i++){
            //判断是否正确答案
            Cell cell = row.getCell(i);
            if(cell == null)
                break;
            if(!contains(rightIndex, i - 2)){
                String text = cell.getStringCellValue();
                Choice choice = new Choice();
                choice.setText(text);
                wrongChoices.add(choice);
            }
        }
        QuestionWithAnswer q = new QuestionWithAnswer();
        q.setQuestion(question);
        q.setType(type);
        q.setEditFlag("0");
        q.setRightChoices(rightChoices);
        q.setWrongChoices(wrongChoices);
        return q;
    }
    private QuestionWithAnswer parseTfRow(Row row) {
        QuestionWithAnswer q = new QuestionWithAnswer();
        List<Choice> rightChoices = new ArrayList<>();
        Choice right = new Choice();
        rightChoices.add(right);
        List<Choice> wrongChoices = new ArrayList<>();
        Choice wrong = new Choice();
        wrongChoices.add(wrong);
        q.setRightChoices(rightChoices);
        q.setWrongChoices(wrongChoices);
        String answer = row.getCell(1).getStringCellValue();
        if("1".equals(answer)) {
            right.setText("正确");
            wrong.setText("错误");
        }else{
            right.setText("错误");
            wrong.setText("正确");
        }
        q.setType("t");
        q.setEditFlag("0");
        return q;
    }
    private int[] parseAnswer(String answerString){
        answerString = answerString.toLowerCase();
        char[] answers = answerString.toCharArray();
        int[] result = new int[answers.length];
        for (int i = 0; i < answers.length; i++) {
            char answer = answers[i];
            int r = answer - 'a';
            result[i] = r;
        }
        return result;
    }
    private boolean contains(int[] array, int i){
        for(int a : array){
            if(a == i){
                return true;
            }
        }
        return false;
    }
}

package com.dw.exercise.controller;

import com.dw.exercise.entity.QuestionBank;
import com.dw.exercise.dao.QuestionBankDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class BankController {
    @Resource()
    private QuestionBankDAO bankMapper;

    @GetMapping("/banks")
    public List<QuestionBank> getBanks(){
        return bankMapper.getBanks();
    }

    @GetMapping("/bank/count/{id}")
    public Map<String, Integer> countQuestionInBank(@PathVariable Integer id){

    }
}

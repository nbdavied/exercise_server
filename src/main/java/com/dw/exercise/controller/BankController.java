package com.dw.exercise.controller;

import com.dw.exercise.entity.QuestionBank;
import com.dw.exercise.dao.QuestionBankDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class BankController {
    @Resource()
    private QuestionBankDAO bankMapper;

    @GetMapping("/banks")
    public List<QuestionBank> getBanks(){
        return bankMapper.getBanks();
    }
}

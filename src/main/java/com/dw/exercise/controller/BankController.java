package com.dw.exercise.controller;

import com.dw.exercise.dao.QuestionDAO;
import com.dw.exercise.entity.Question;
import com.dw.exercise.entity.QuestionBank;
import com.dw.exercise.dao.QuestionBankDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BankController {
    @Resource()
    private QuestionBankDAO bankDAO;
    @Resource()
    private QuestionDAO questionDAO;

    @GetMapping("/banks")
    public List<QuestionBank> getBanks(){
        return bankDAO.getBanks();
    }

    @GetMapping("/bank/count/{id}")
    public Map<String, Integer> countQuestionInBank(@PathVariable Integer id){
        Map<String, Integer> result = new HashMap<>();
        Question q = new Question();
        q.setBankId(id);
        q.setType("s");
        Integer snum = questionDAO.countQuestionWithBankAndType(q);
        result.put("snum", snum);
        q.setType("m");
        Integer mnum = questionDAO.countQuestionWithBankAndType(q);
        result.put("mnum", mnum);
        q.setType("t");
        Integer tnum = questionDAO.countQuestionWithBankAndType(q);
        result.put("tnum", tnum);
        return result;
    }
}

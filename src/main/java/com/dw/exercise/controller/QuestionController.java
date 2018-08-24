package com.dw.exercise.controller;

import com.dw.exercise.entity.Question;
import com.dw.exercise.mapper.QuestionMapper;
import com.dw.exercise.vo.QuestionNoAnswer;
import com.dw.exercise.vo.QuestionWithAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/question")
public class QuestionController {
    @Resource
    private QuestionMapper questionMapper;

    @RequestMapping("/{bankId}")
    public QuestionNoAnswer nextQuestion(@PathVariable("bankId") int bankId, String method){
        return null;
    }
    @RequestMapping(value = "/edit/{quesId}", method = RequestMethod.GET)
    public QuestionWithAnswer getQuestionForEdit(@PathVariable("quesId") int quesId){
        return null;
    }
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public void editQuestionAnswer(QuestionWithAnswer question){

    }
    @RequestMapping(value= "/confirmedit/{quesId}")
    public void confirmEditQuestionAnswer(@PathVariable("quesId") int quesId){

    }
    @RequestMapping(value="/canceledit/{quesId}")
    public void cancelEditQuestionAnswer(@PathVariable("quesId") int quesId){

    }
}

package com.dw.exercise.controller;

import com.dw.exercise.entity.Choice;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {
    @Resource
    private QuestionMapper questionMapper;

    @RequestMapping("/{bankId}")
    public QuestionNoAnswer nextQuestion(@PathVariable("bankId") int bankId, String method, Integer last){
        Question q = null;
        if(last == null){
            last = 0;
        }
        if("random".equals(method)){
            q = questionMapper.getQuestionRandomlyInBankId(bankId);
        }else{
            q = new Question();
            q.setBankId(bankId);
            q.setId(last);
            q = questionMapper.getNextQuestionInBankId(q);
        }
        String[] right = q.getRightChoices().split(",");
        String[] wrong = q.getWrongChoices().split(",");
        List<Integer> choiceIds = new ArrayList<Integer>();
        for(String id : right){
            choiceIds.add(Integer.parseInt(id));
        }
        for(String id : wrong){
            choiceIds.add(Integer.parseInt(id));
        }
        List<Choice> choices = new ArrayList<Choice>();
        for(int id : choiceIds){
            Choice choice = questionMapper.getChoiceById(id);
            choices.add(choice);
        }
        Collections.shuffle(choices);   //打乱顺序

        QuestionNoAnswer result = new QuestionNoAnswer();
        result.setId(q.getId());
        result.setQuestion(q.getQuestion());
        result.setChoices(choices);
        result.setType(q.getType());
        result.setEditFlag(q.getEditFlag());
        return result;
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

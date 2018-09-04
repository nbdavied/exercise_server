package com.dw.exercise.controller;

import com.dw.exercise.dao.UserDAO;
import com.dw.exercise.dao.WrongCollectionDAO;
import com.dw.exercise.entity.Choice;
import com.dw.exercise.entity.Question;
import com.dw.exercise.dao.QuestionDAO;
import com.dw.exercise.entity.User;
import com.dw.exercise.entity.WrongCollection;
import com.dw.exercise.service.QuestionService;
import com.dw.exercise.vo.QuestionNoAnswer;
import com.dw.exercise.vo.QuestionWithAnswer;
import com.dw.util.StringUtil;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {
    @Resource
    private QuestionDAO questionDAO;
    @Resource
    private UserDAO userDAO;
    @Resource
    private WrongCollectionDAO wrongCollectionDAO;
    @Resource
    private QuestionService questionService;

    /**
     * 顺序获取题库中下一题
     * @param bankId 题库号
     * @param last 当前题号
     * @return
     */
    @GetMapping("/next")
    public QuestionNoAnswer nextQuestion(Integer bankId, Integer last, String wrong){
        if(bankId == null){
            throw new RuntimeException("未指定题库编号");
        }
        if(last == null) {
            last = 0;
        }
        boolean onlyWrong = false;
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = null;
        if("1".equals(wrong)){
            user = userDAO.getUserByUsername(username);
            if(user != null){
                onlyWrong = true;
            }
        }
        Question question;
        if(onlyWrong){
            question = questionDAO.getNextQuestionInWrongCollection(bankId, last, user.getId());
        }else {
            question = new Question();
            question.setBankId(bankId);
            question.setId(last);
            question = questionDAO.getNextQuestionInBankId(question);
        }
        return prepareQuestion(question);
    }

    /**
     * 随机获取下一题
     * @param bankId 题库号
     * @return
     */
    @GetMapping("/random")
    public QuestionNoAnswer randomQuestion(Integer bankId, String wrong){
        if(bankId == null){
            throw new RuntimeException("未指定题库编号");
        }
        boolean onlyWrong = false;
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = null;
        if("1".equals(wrong)){
            user = userDAO.getUserByUsername(username);
            if(user != null){
                onlyWrong = true;
            }
        }
        Question question;
        if(onlyWrong){
            WrongCollection collection = new WrongCollection();
            collection.setBankId(bankId);
            collection.setUserId(user.getId());
            collection = wrongCollectionDAO.randomlySelect(collection);
            if(collection == null){
                question = null;
            }else{
                question = questionDAO.getQuestionById(collection.getQuestionId());
            }
        }else {
            question = questionDAO.getQuestionRandomlyInBankId(bankId);
        }
        return prepareQuestion(question);
    }

    /**
     * 提交并获取正确答案
     * @param id 题号
     * @param subs 提交的答案
     * @return
     */
    @PostMapping("/answer/{id}")
    public List<Integer> getAnswer(@PathVariable Integer id, @RequestBody List<Integer> subs){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Question q = questionDAO.getQuestionById(id);
        if(q == null){
            throw new RuntimeException("题目不存在");
        }
        List<Integer> answers = q.getRightChoices();
        //对登陆用户判断提交的答案是否正确
        User user = userDAO.getUserByUsername(username);
        if(user != null){
            boolean bingo = true;
            if(subs.size() != answers.size()){
                //判断答案数量一致
                bingo = false;
            }else {
                for (Integer sub : subs) {
                    if(!answers.contains(sub)){
                        bingo = false;
                        break;
                    }
                }
            }
            if(!bingo){
                WrongCollection wrong = new WrongCollection();
                wrong.setUserId(user.getId());
                wrong.setQuestionId(id);
                wrong.setBankId(q.getBankId());
                wrongCollectionDAO.insert(wrong);
            }
        }
        return answers;
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
    @DeleteMapping("/wrong/{id}")
    @PreAuthorize("hasRole('USER')")
    public void deleteWrongCollection(@PathVariable Integer id){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDAO.getUserByUsername(username);
        WrongCollection collection = new WrongCollection();
        collection.setUserId(user.getId());
        collection.setQuestionId(id);
        wrongCollectionDAO.deleteWrongCollection(collection);
    }
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void createQuestion(@RequestBody QuestionWithAnswer question){
        insertQuestionWithAnswer(question);
    }
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void createBank(MultipartFile file, String bankname) throws IOException {
        List<QuestionWithAnswer> qlist = questionService.parse(file.getInputStream());
        for(QuestionWithAnswer q : qlist){
            insertQuestionWithAnswer(q);
        }
    }
    private QuestionNoAnswer prepareQuestion(@Nullable Question q){
        if(q == null){
            return null;
        }
        List<Integer> choiceIds = new ArrayList<Integer>();
        choiceIds.addAll(q.getRightChoices());
        choiceIds.addAll(q.getWrongChoices());
        List<Choice> choices = new ArrayList<Choice>();
        for(int id : choiceIds){
            Choice choice = questionDAO.getChoiceById(id);
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
    private void insertQuestionWithAnswer(QuestionWithAnswer question){
        Question q = new Question();
        q.setQuestion(question.getQuestion());
        q.setBankId(question.getBankId());
        q.setEditFlag("0");
        q.setType(question.getType());
        List<Integer> choiceIds = new ArrayList<>();
        for (Choice right : question.getRightChoices()){
            questionDAO.insertChoice(right);
            choiceIds.add(right.getId());
        }
        q.setRightChoices(choiceIds);
        choiceIds = new ArrayList<>();
        for (Choice wrong : question.getWrongChoices()){
            questionDAO.insertChoice(wrong);
            choiceIds.add(wrong.getId());
        }
        q.setWrongChoices(choiceIds);
        questionDAO.createQuestion(q);
    }
}


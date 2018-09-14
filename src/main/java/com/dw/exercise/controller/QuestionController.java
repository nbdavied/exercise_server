package com.dw.exercise.controller;

import com.dw.exercise.dao.QuestionBankDAO;
import com.dw.exercise.dao.UserDAO;
import com.dw.exercise.dao.WrongCollectionDAO;
import com.dw.exercise.entity.*;
import com.dw.exercise.dao.QuestionDAO;
import com.dw.exercise.service.QuestionService;
import com.dw.exercise.vo.QuestionNoAnswer;
import com.dw.exercise.vo.QuestionNoAnswerWithSelected;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.util.*;

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
    @Resource
    private QuestionBankDAO questionBankDAO;

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
            Map<String, Object> map = new HashMap<>();
            map.put("bankId", bankId);
            map.put("lastId", last);
            map.put("userId", user.getId());
            question = questionDAO.getNextQuestionInWrongCollection(map);
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
        List<Choice> rightChoices = q.getRightChoices();
        //对登陆用户判断提交的答案是否正确
        User user = userDAO.getUserByUsername(username);
        if(user != null){
            boolean bingo = true;
            if(subs.size() != rightChoices.size()){
                //判断答案数量一致
                bingo = false;
            }else {
                for (Choice c : rightChoices) {
                    if(!subs.contains(c.getId())){
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
        List<Integer> answers = new ArrayList<>();
        for(Choice c: rightChoices){
            answers.add(c.getId());
        }
        return answers;
    }
    @GetMapping("")
    public List<QuestionNoAnswerWithSelected> getQuestionsInPaper(Integer paperId, String type){
        Map<String, Object> map = new HashMap<>();
        map.put("paperId", paperId);
        map.put("type", type);
        List<QuestionWithSelected> list = questionDAO.getQuestionsInPaper(map);
        List<QuestionNoAnswerWithSelected> result = new ArrayList<>();
        for(QuestionWithSelected q : list){
            result.add(prepareQuestionWithSelected(q));
        }
        return result;
    }
    @RequestMapping(value = "/edit/{quesId}", method = RequestMethod.GET)
    public Question getQuestionForEdit(@PathVariable("quesId") int quesId){
        return null;
    }
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public void editQuestionAnswer(Question question){

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
    public void createQuestion(@RequestBody Question question){
        insertQuestionWithAnswer(question, question.getBankId());
    }
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void createBank(MultipartFile file, String bankname) throws IOException, InvalidFormatException {
        List<Question> qlist = questionService.parse(file.getInputStream());
        QuestionBank bank = new QuestionBank(bankname);
        questionBankDAO.createBank(bank);
        for(Question q : qlist){
            insertQuestionWithAnswer(q, bank.getId());
        }
    }
    @DeleteMapping("/bank/{bankId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteQuestionOfBank(@PathVariable Integer bankId){
        wrongCollectionDAO.deleteInBank(bankId);
        questionDAO.deleteChoicesInBank(bankId);
        questionDAO.deleteQuestionsInBank(bankId);
        questionBankDAO.deleteBank(bankId);
    }
    private QuestionNoAnswer prepareQuestion(@Nullable Question q){
        if(q == null){
            return null;
        }
        List<Choice> choices = new ArrayList<>();
        choices.addAll(q.getRightChoices());
        choices.addAll(q.getWrongChoices());
        for(Choice c : choices){
            c.setRight(null);
        }
        //if(!"t".equals(q.getType())) {
            Collections.shuffle(choices);   //打乱顺序
        //}
        QuestionNoAnswer result = new QuestionNoAnswer();
        result.setId(q.getId());
        result.setQuestion(q.getQuestion());
        result.setChoices(choices);
        result.setType(q.getType());
        result.setEditFlag(q.getEditFlag());
        return result;
    }
    private QuestionNoAnswerWithSelected prepareQuestionWithSelected(@Nullable QuestionWithSelected q){
        QuestionNoAnswer qna = prepareQuestion(q);
        QuestionNoAnswerWithSelected result = new QuestionNoAnswerWithSelected(qna);
        result.setSelected(q.getSelected());
        return result;
    }
    private void insertQuestionWithAnswer(Question question, int bankId){
        question.setBankId(bankId);
        question.setEditFlag("0");
        questionDAO.createQuestion(question);
        List<Choice> choices = new ArrayList<>();
        choices.addAll(question.getRightChoices());
        choices.addAll(question.getWrongChoices());
        for(Choice c: choices){
            c.setQuestionId(question.getId());
            questionDAO.insertChoice(c);
        }
    }
}


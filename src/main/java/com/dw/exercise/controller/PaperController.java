package com.dw.exercise.controller;

import com.dw.exercise.dao.PaperDAO;
import com.dw.exercise.dao.QuestionDAO;
import com.dw.exercise.dao.TestResultDAO;
import com.dw.exercise.entity.*;
import com.dw.exercise.security.AppAuthToken;
import com.dw.util.StringUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.Format;
import java.util.*;

@RestController
@RequestMapping("/paper")
@PreAuthorize("hasRole('USER')")
public class PaperController {
    @Resource
    private PaperDAO paperDAO;
    @Resource
    private QuestionDAO questionDAO;
    @Resource
    private TestResultDAO resultDAO;

    @PostMapping("")
    @Transactional
    public void createPaper(@RequestBody TestPaper paper){
        Integer userId = ((AppAuthToken) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        paper.setUserId(userId);
        paper.setStatus("0");
        paper.setRestTime(paper.getTotalTime());
        //设置试卷号
        Map<String, Object> param = new HashMap<>();
        param.put("date", StringUtil.today());
        param.put("userId", userId);
        String maxNo = paperDAO.getMaxPaperNoOfDate(param);
        Integer no = 0;
        if(maxNo != null) {
            no = Integer.parseInt(maxNo.substring(8));
        }
        no++;
        maxNo = String.format("%s%02d", StringUtil.today(), no);
        paper.setPaperNo(maxNo);
        paperDAO.createPaper(paper);

        Question q = new Question();
        q.setBankId(paper.getBankId());
        q.setType("s");
        //取出所有单选题id
        List<Integer> sIds = questionDAO.getQuestionIdsWithBankAndType(q);
        //打乱顺序
        Collections.shuffle(sIds);
        //选择前面指定数量
        List<Integer> ids = sIds.subList(0, paper.getsNum());
        //多选题
        q.setType("m");
        List<Integer> mIds = questionDAO.getQuestionIdsWithBankAndType(q);
        Collections.shuffle(mIds);
        ids.addAll(mIds.subList(0, paper.getmNum()));
        //判断题
        q.setType("t");
        List<Integer> tIds = questionDAO.getQuestionIdsWithBankAndType(q);
        Collections.shuffle(tIds);
        ids.addAll(tIds.subList(0, paper.gettNum()));
        //插入试卷题目关联
        List<PaperQuestion> questionList = new ArrayList<>();
        int i = 1;
        for (Integer id: ids){
            PaperQuestion question = new PaperQuestion();
            question.setPaperId(paper.getId());
            question.setQuestionId(id);
            question.setNo(i ++);
            question.setRight(false);
            questionList.add(question);
        }
        paperDAO.batchInsertPaperQuestion(questionList);
    }
    @GetMapping()
    public List<TestPaper> getPaperList(){
        Integer userId = ((AppAuthToken) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        List<TestPaper> list = paperDAO.getPaperOfUser(userId);
        return list;
    }
    @PostMapping("/start")
    @Transactional
    public TestPaper startPaper(@RequestBody TestPaper paper){
        Integer paperId = paper.getId();
        paper = paperDAO.getPaperInfoWithId(paperId);
        if("0".equals(paper.getStatus())){
            paper.setRestTime(paper.getTotalTime());
            paper.setStatus("1");
            paperDAO.updateById(paper);
            paperDAO.clearSelectInPaper(paperId);
        }else if ("2".equals(paper.getStatus())){
            paper.setRestTime(paper.getTotalTime());
            paper.setStatus("1");
            paperDAO.updateById(paper);
            paperDAO.clearSelectInPaper(paperId);
        }
        return paper;
    }
    /**
     * 更改选项
     * 判断选择是否正确
     * @param paperQuestion
     */
    @PostMapping("/select")
    public void saveSelected(@RequestBody PaperQuestion paperQuestion){
        Question question = questionDAO.getQuestionById(paperQuestion.getQuestionId());
        paperQuestion.setRight(false);
        if(paperQuestion.getSelected().size() > 0
                && paperQuestion.getSelected().size() == question.getRightChoices().size()){
            boolean bingo = true;
            for(Choice rightChoice: question.getRightChoices()){
                if(!paperQuestion.getSelected().contains(rightChoice.getId())){
                    bingo = false;
                    break;
                }
            }
            paperQuestion.setRight(bingo);
        }
        paperDAO.updateSelectedOfPaperQuestion(paperQuestion);
    }
    @PostMapping("/finish")
    @Transactional
    public Integer finishPaper(@RequestBody TestPaper paper){
        int paperId = paper.getId();
        paper = paperDAO.getPaperInfoWithId(paperId);
        List<PaperQuestionCount> count = paperDAO.countRightQuestionInPaper(paperId);
        int sRight = 0, mRight= 0, tRight = 0;
        for(PaperQuestionCount c : count){
            if("s".equals(c.getType())){
                sRight = c.getCnt();
            }else if("m".equals(c.getType())){
                mRight = c.getCnt();
            }else if("t".equals(c.getType())){
                tRight = c.getCnt();
            }
        }
        int score = paper.getsScore() * sRight
                + paper.getmScore() * mRight
                + paper.gettScore() * tRight;
        TestResult result = new TestResult();
        result.setPaperId(paperId);
        result.setSubTime(new Date());
        result.setsRight(sRight);
        result.settRight(tRight);
        result.setmRight(mRight);
        result.setsTotal(paper.getsNum());
        result.setmTotal(paper.getmNum());
        result.settTotal(paper.gettNum());
        result.setScore(score);
        resultDAO.insert(result);
        paper.setStatus("2");
        paper.setRestTime(paper.getTotalTime());
        paperDAO.updateById(paper);
        return result.getId();
    }
    @PostMapping("/time")
    public void setRestTime(@RequestBody TestPaper paper){
        paper.setStatus("1");
        paperDAO.updateById(paper);
    }
    @GetMapping("/result")
    public List<TestResult> getResultsOfPaper(Integer paperId){
        List<TestResult> result =  resultDAO.getResultByPaperId(paperId);
        return result;
    }
}

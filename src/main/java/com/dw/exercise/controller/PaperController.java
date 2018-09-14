package com.dw.exercise.controller;

import com.dw.exercise.dao.PaperDAO;
import com.dw.exercise.dao.QuestionDAO;
import com.dw.exercise.entity.PaperQuestion;
import com.dw.exercise.entity.Question;
import com.dw.exercise.entity.TestPaper;
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

    @PostMapping("")
    @Transactional
    public void createPaper(@RequestBody TestPaper paper){
        Integer userId = ((AppAuthToken) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        paper.setUserId(userId);
        paper.setStatus("0");
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
    @PutMapping("/select")
    public void saveSelected(@RequestBody PaperQuestion paperQuestion){
        paperDAO.updateSelectedOfPaperQuestion(paperQuestion);
    }
}

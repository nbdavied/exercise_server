package com.dw.exercise.entity;

import java.util.List;

public class PaperQuestion {
    private Integer paperId;
    private Integer questionId;
    private Integer no;
    private List<Integer> selected;
    private Boolean right;

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public List<Integer> getSelected() {
        return selected;
    }

    public void setSelected(List<Integer> selected) {
        this.selected = selected;
    }

    public Boolean isRight() {
        return right;
    }

    public void setRight(Boolean right) {
        this.right = right;
    }
}

package com.dw.exercise.vo;

import java.util.List;

public class QuestionNoAnswerWithSelected extends QuestionNoAnswer {
    private List<Integer> selected;

    public QuestionNoAnswerWithSelected() {
        super();
    }
    public QuestionNoAnswerWithSelected(QuestionNoAnswer q){
        this.setId(q.getId());
        this.setQuestion(q.getQuestion());
        this.setChoices(q.getChoices());
        this.setType(q.getType());
        this.setEditFlag(q.getEditFlag());
        this.selected = null;
    }

    public List<Integer> getSelected() {
        return selected;
    }

    public void setSelected(List<Integer> selected) {
        this.selected = selected;
    }
}

package com.dw.exercise.entity;


public class Choice{
    private Integer id;
    private String text;
    private Integer questionId;
    private Boolean right;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Boolean isRight() {
        return right;
    }

    public void setRight(Boolean right) {
        this.right = right;
    }
}

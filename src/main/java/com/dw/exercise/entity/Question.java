package com.dw.exercise.entity;

import java.util.List;

public class Question {
    private int id;
    private String question;
    private List<Integer> rightChoices;
    private List<Integer> wrongChoices;
    private String type;
    private String editFlag;
    private int bankId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Integer> getRightChoices() {
        return rightChoices;
    }

    public void setRightChoices(List<Integer> rightChoices) {
        this.rightChoices = rightChoices;
    }

    public List<Integer> getWrongChoices() {
        return wrongChoices;
    }

    public void setWrongChoices(List<Integer> wrongChoices) {
        this.wrongChoices = wrongChoices;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEditFlag() {
        return editFlag;
    }

    public void setEditFlag(String editFlag) {
        this.editFlag = editFlag;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }
}

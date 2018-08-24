package com.dw.exercise.entity;

public class Question {
    private int id;
    private String question;
    private String rightChoices;
    private String wrongChoices;
    private String type;
    private String editFlag;

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

    public String getRightChoices() {
        return rightChoices;
    }

    public void setRightChoices(String rightChoices) {
        this.rightChoices = rightChoices;
    }

    public String getWrongChoices() {
        return wrongChoices;
    }

    public void setWrongChoices(String wrongChoices) {
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
}

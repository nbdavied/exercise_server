package com.dw.exercise.entity;


public class QuestionBank {
    private int id;
    private String name;
    private Boolean delete;

    public QuestionBank() {
    }

    public QuestionBank(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }
}

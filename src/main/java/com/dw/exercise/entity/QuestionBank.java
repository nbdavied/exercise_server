package com.dw.exercise.entity;

public class QuestionBank {
    private int id;
    private String name;

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
}

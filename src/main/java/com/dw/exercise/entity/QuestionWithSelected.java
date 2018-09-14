package com.dw.exercise.entity;

import java.util.List;

public class QuestionWithSelected extends Question {
    private List<Integer> selected;

    public List<Integer> getSelected() {
        return selected;
    }

    public void setSelected(List<Integer> selected) {
        this.selected = selected;
    }
}

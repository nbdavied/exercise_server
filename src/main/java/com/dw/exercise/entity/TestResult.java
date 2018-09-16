package com.dw.exercise.entity;

import java.util.Date;

public class TestResult {
    private Integer id;
    private Integer paperId;
    private Date subTime;
    private Integer score;
    private Integer sRight;
    private Integer mRight;
    private Integer tRight;
    private Integer sTotal;
    private Integer mTotal;
    private Integer tTotal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public Date getSubTime() {
        return subTime;
    }

    public void setSubTime(Date subTime) {
        this.subTime = subTime;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getsRight() {
        return sRight;
    }

    public void setsRight(Integer sRight) {
        this.sRight = sRight;
    }

    public Integer getmRight() {
        return mRight;
    }

    public void setmRight(Integer mRight) {
        this.mRight = mRight;
    }

    public Integer gettRight() {
        return tRight;
    }

    public void settRight(Integer tRight) {
        this.tRight = tRight;
    }

    public Integer getsTotal() {
        return sTotal;
    }

    public void setsTotal(Integer sTotal) {
        this.sTotal = sTotal;
    }

    public Integer getmTotal() {
        return mTotal;
    }

    public void setmTotal(Integer mTotal) {
        this.mTotal = mTotal;
    }

    public Integer gettTotal() {
        return tTotal;
    }

    public void settTotal(Integer tTotal) {
        this.tTotal = tTotal;
    }
}

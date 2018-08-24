package com.dw.exercise.entity;

import java.util.Date;

public class UserAuth {
    private int userId;
    private String server;
    private String token;
    private Date genTime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

}

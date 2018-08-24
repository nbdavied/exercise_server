package com.dw.exercise.vo;

import com.dw.exercise.entity.User;
import com.dw.exercise.entity.UserAuth;

import java.util.Date;

/**
 * 登陆用户对象
 */
public class AuthUser {
    private int id;
    private String username;
    private String nickname;
    private String gender;
    private String birthday;
    private String certid;
    private String mobile;
    private String email;
    private String address;
    private String avatar;
    private Date regTime;

    private String server;
    private String password;
    private Date genTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCertid() {
        return certid;
    }

    public void setCertid(String certid) {
        this.certid = certid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }


    public User toUser(){
        User user = new User();
        user.setId(this.getId());
        user.setUsername(this.username);
        user.setNickname(this.nickname);
        user.setGender(this.gender);
        user.setBirthday(this.birthday);
        user.setCertid(this.certid);
        user.setMobile(this.mobile);
        user.setEmail(this.email);
        user.setAddress(this.address);
        user.setAvatar(this.avatar);
        user.setRegTime(this.regTime);
        return user;
    }
    public UserAuth toUserAuth(){
        UserAuth auth = new UserAuth();
        auth.setUserId(this.id);
        auth.setServer(this.server);
        auth.setToken(this.password);
        auth.setGenTime(this.genTime);
        return auth;
    }
}

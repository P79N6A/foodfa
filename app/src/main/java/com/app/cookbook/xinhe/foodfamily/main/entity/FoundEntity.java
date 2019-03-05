package com.app.cookbook.xinhe.foodfamily.main.entity;

/**
 * Created by shiyujia02 on 2018/1/26.
 */

public class FoundEntity {
    private String id;
    private String title;
    private String path;
    private String desc;
    private String status;
    private String follow;
    private String questions;
    private String is_follow;
    private String is_follow_text;
    private Question_Date question_data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }

    public String getIs_follow_text() {
        return is_follow_text;
    }

    public void setIs_follow_text(String is_follow_text) {
        this.is_follow_text = is_follow_text;
    }

    public Question_Date getQuestion_data() {
        return question_data;
    }

    public void setQuestion_data(Question_Date question_data) {
        this.question_data = question_data;
    }

    public User_Date getUser_data() {
        return user_data;
    }

    public void setUser_data(User_Date user_data) {
        this.user_data = user_data;
    }

    private User_Date user_data;




}

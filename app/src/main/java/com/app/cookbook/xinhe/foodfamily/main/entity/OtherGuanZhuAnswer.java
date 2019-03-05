package com.app.cookbook.xinhe.foodfamily.main.entity;

/**
 * Created by shiyujia02 on 2018/1/24.
 */

public class OtherGuanZhuAnswer {
    private String id;
    private String title;
    private String content;
    private Object  answer;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getAnswer() {
        return answer;
    }

    public void setAnswer(Object answer) {
        this.answer = answer;
    }

    public String getCountUsers() {
        return countUsers;
    }

    public void setCountUsers(String countUsers) {
        this.countUsers = countUsers;
    }

    private String countUsers;

}

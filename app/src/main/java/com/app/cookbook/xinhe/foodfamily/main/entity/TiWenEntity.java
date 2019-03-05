package com.app.cookbook.xinhe.foodfamily.main.entity;

/**
 * Created by shiyujia02 on 2018/1/24.
 */

public class TiWenEntity {
    private String id;
    private String title;
    private String answer;
    private String name;
    private String avatar;
    private TiWenUsers users;
    private String countUsers;

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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public TiWenUsers getUsers() {
        return users;
    }

    public void setUsers(TiWenUsers users) {
        this.users = users;
    }

    public String getCountUsers() {
        return countUsers;
    }

    public void setCountUsers(String countUsers) {
        this.countUsers = countUsers;
    }

}

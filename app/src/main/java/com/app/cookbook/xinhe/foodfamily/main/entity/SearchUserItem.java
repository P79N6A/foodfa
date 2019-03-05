package com.app.cookbook.xinhe.foodfamily.main.entity;

/**
 * Created by 18030150 on 2018/3/22.
 */

public class SearchUserItem {


    /**
     * answer : 0
     * avatar : http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151695636321637105.jpg
     * id : 2
     * name : 徐小小
     * profession : 6666
     * questions : 1
     */

    private String answer;
    private String id;
    private String name;
    private String avatar;
    private String profession;
    private String questions;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }
}

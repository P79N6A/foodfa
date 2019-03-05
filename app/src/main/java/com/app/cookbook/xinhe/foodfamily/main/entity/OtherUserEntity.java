package com.app.cookbook.xinhe.foodfamily.main.entity;

/**
 * Created by shiyujia02 on 2018/1/24.
 */

public class OtherUserEntity {
    private String name;
    private String personal_signature;
    private String avatar;
    private String profession;
    private String sex;
    private String tel;
    private String user_status;
    private String follow_class;
    private String follow_users;
    private String followed_users;
    private String questions;
    private String answer;
    private String follow_questions;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getRelational() {
        return relational;
    }

    public void setRelational(String relational) {
        this.relational = relational;
    }

    private String relational;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonal_signature() {
        return personal_signature;
    }

    public void setPersonal_signature(String personal_signature) {
        this.personal_signature = personal_signature;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFollow_class() {
        return follow_class;
    }

    public void setFollow_class(String follow_class) {
        this.follow_class = follow_class;
    }

    public String getFollow_users() {
        return follow_users;
    }

    public void setFollow_users(String follow_users) {
        this.follow_users = follow_users;
    }

    public String getFollowed_users() {
        return followed_users;
    }

    public void setFollowed_users(String followed_users) {
        this.followed_users = followed_users;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getFollow_questions() {
        return follow_questions;
    }

    public void setFollow_questions(String follow_questions) {
        this.follow_questions = follow_questions;
    }

    public String getCollecton_answer() {
        return collecton_answer;
    }

    public void setCollecton_answer(String collecton_answer) {
        this.collecton_answer = collecton_answer;
    }

    private String collecton_answer;
}

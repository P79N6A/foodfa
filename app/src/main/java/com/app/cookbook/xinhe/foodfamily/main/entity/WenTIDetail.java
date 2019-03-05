package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

/**
 * Created by shiyujia02 on 2018/1/30.
 */

public class WenTIDetail {
    private String id;
    private String title;
    private String content;
    private String uid;
    private String created_id;


    private String type;
    private String follow;
    private String content_remove;
    private String answer;
    private String is_follow;
    private String is_follow_text;
    private String is_delete;
    private List<CategoryDateEn> category_data;
    private List<ImageEn> img_data;
    private AnswerEn answer_data;

    //    private String classification_id;
//    private String views;
    private boolean is_zhankai;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public boolean isIs_zhankai() {
        return is_zhankai;
    }

    public void setIs_zhankai(boolean is_zhankai) {
        this.is_zhankai = is_zhankai;
    }

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

    public String getContent_remove() {
        return content_remove;
    }

    public void setContent_remove(String content_remove) {
        this.content_remove = content_remove;
    }
//
//    public String getClassification_id() {
//        return classification_id;
//    }
//
//    public void setClassification_id(String classification_id) {
//        this.classification_id = classification_id;
//    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getCreated_id() {
        return created_id;
    }

    public void setCreated_id(String created_id) {
        this.created_id = created_id;
    }

//    public String getViews() {
//        return views;
//    }
//
//    public void setViews(String views) {
//        this.views = views;
//    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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

    public List<CategoryDateEn> getCategory_data() {
        return category_data;
    }

    public void setCategory_data(List<CategoryDateEn> category_data) {
        this.category_data = category_data;
    }

    public List<ImageEn> getImg_data() {
        return img_data;
    }

    public void setImg_data(List<ImageEn> img_data) {
        this.img_data = img_data;
    }

    public AnswerEn getAnswer_data() {
        return answer_data;
    }

    public void setAnswer_data(AnswerEn answer_data) {
        this.answer_data = answer_data;
    }


}

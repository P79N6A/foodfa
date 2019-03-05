package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.io.Serializable;

/**
 * Created by shiyujia02 on 2018/1/25.
 */

public class Location implements Serializable {


    /**
     * answer_number : 1
     * thumbs_count : 0
     * comment_count : 0
     * follow_count : 1
     * sys_count : 6
     */

    private int answer_number;
    private int thumbs_count;
    private int comment_count;
    private int follow_count;
    private int sys_count;

    public int getAnswer_number() {
        return answer_number;
    }

    public void setAnswer_number(int answer_number) {
        this.answer_number = answer_number;
    }

    public int getThumbs_count() {
        return thumbs_count;
    }

    public void setThumbs_count(int thumbs_count) {
        this.thumbs_count = thumbs_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getFollow_count() {
        return follow_count;
    }

    public void setFollow_count(int follow_count) {
        this.follow_count = follow_count;
    }

    public int getSys_count() {
        return sys_count;
    }

    public void setSys_count(int sys_count) {
        this.sys_count = sys_count;
    }



}

package com.app.cookbook.xinhe.foodfamily.shiyujia.entity;

public class LabelDetails {


    /**
     * title : 饮食
     * path : http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151702084492266887.jpg
     * desc : 民以食为天
     * id : 3
     * count_follow_users : 12
     * question_count : 378
     */

    private String title;
    private String path;
    private String desc;
    private String id;
    private String count_follow_users;
    private String question_count;
    private String is_follow;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCount_follow_users() {
        return count_follow_users;
    }

    public void setCount_follow_users(String count_follow_users) {
        this.count_follow_users = count_follow_users;
    }

    public String getQuestion_count() {
        return question_count;
    }

    public void setQuestion_count(String question_count) {
        this.question_count = question_count;
    }

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }
}

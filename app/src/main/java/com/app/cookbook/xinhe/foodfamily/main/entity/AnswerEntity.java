package com.app.cookbook.xinhe.foodfamily.main.entity;


/**
 * Created by shiyujia02 on 2018/1/24.
 */

public class AnswerEntity {
    private String id;
    private String pid;
    private String content;
    private String uid;
    private String status;
    private String type;
    private String created_at;
    private String thumbs;

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    private String comment_count;
    private String collection;
    private String is_existence;
    private String name;
    private String content_remove;
    private String users_avatar;
//    private String is_thumbs;
//    private String is_thumbs_text;
//    private String is_collect;
//    private String is_collect_text;
    private ImageDate img_data;
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumbs() {
        return thumbs;
    }

    public void setThumbs(String thumbs) {
        this.thumbs = thumbs;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getIs_existence() {
        return is_existence;
    }

    public void setIs_existence(String is_existence) {
        this.is_existence = is_existence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent_remove() {
        return content_remove;
    }

    public void setContent_remove(String content_remove) {
        this.content_remove = content_remove;
    }

    public String getUsers_avatar() {
        return users_avatar;
    }

    public void setUsers_avatar(String users_avatar) {
        this.users_avatar = users_avatar;
    }

//    public String getIs_thumbs() {
//        return is_thumbs;
//    }
//
//    public void setIs_thumbs(String is_thumbs) {
//        this.is_thumbs = is_thumbs;
//    }

//    public String getIs_thumbs_text() {
//        return is_thumbs_text;
//    }
//
//    public void setIs_thumbs_text(String is_thumbs_text) {
//        this.is_thumbs_text = is_thumbs_text;
//    }

//    public String getIs_collect() {
//        return is_collect;
//    }
//
//    public void setIs_collect(String is_collect) {
//        this.is_collect = is_collect;
//    }
//
//    public String getIs_collect_text() {
//        return is_collect_text;
//    }
//
//    public void setIs_collect_text(String is_collect_text) {
//        this.is_collect_text = is_collect_text;
//    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ImageDate getImg_data() {
        return img_data;
    }

    public void setImg_data(ImageDate img_data) {
        this.img_data = img_data;
    }



}

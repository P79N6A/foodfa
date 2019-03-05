package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

/**
 * Created by shiyujia02 on 2018/1/24.
 */

public class HuiDaEntity {
    private String content;
    private String content_remove;
    private String id;
    private String qid;
    private String title;
    private String thumbs;
    private String collection;
    private String created_at;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<ImageDate> getImg_data() {
        return img_data;
    }

    public void setImg_data(List<ImageDate> img_data) {
        this.img_data = img_data;
    }

    private List<ImageDate> img_data;
}

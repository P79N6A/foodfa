package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

/**
 * Created by shiyujia02 on 2018/2/5.
 */

public class MyShouCangEntity {
    private String content;
    private String id;
    private String thumbs;
    private String collection;
    private String content_remove;
    private String name;
    private String avatar;
    private String uid;
    private String title;
    private String pid;
    private List<ImageDate> img_data;
    private String is_thumbs;
    private String is_thumbs_text;
    private String is_collect;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getContent_remove() {
        return content_remove;
    }

    public void setContent_remove(String content_remove) {
        this.content_remove = content_remove;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public List<ImageDate> getImg_data() {
        return img_data;
    }

    public void setImg_data(List<ImageDate> img_data) {
        this.img_data = img_data;
    }

    public String getIs_thumbs() {
        return is_thumbs;
    }

    public void setIs_thumbs(String is_thumbs) {
        this.is_thumbs = is_thumbs;
    }

    public String getIs_thumbs_text() {
        return is_thumbs_text;
    }

    public void setIs_thumbs_text(String is_thumbs_text) {
        this.is_thumbs_text = is_thumbs_text;
    }

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    public String getIs_collect_text() {
        return is_collect_text;
    }

    public void setIs_collect_text(String is_collect_text) {
        this.is_collect_text = is_collect_text;
    }

    private String is_collect_text;
}

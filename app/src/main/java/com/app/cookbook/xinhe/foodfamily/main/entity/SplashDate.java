package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

/**
 * Created by shiyujia02 on 2018/1/25.
 */

public class SplashDate {
    private String id;
    private String title;
    private String start_at;
    private String end_at;

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

    public String getStart_at() {
        return start_at;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    public List<PathEntity> getPath() {
        return path;
    }

    public void setPath(List<PathEntity> path) {
        this.path = path;
    }

    private List<PathEntity> path;

}

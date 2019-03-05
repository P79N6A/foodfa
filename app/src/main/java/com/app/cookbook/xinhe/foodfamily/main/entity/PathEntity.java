package com.app.cookbook.xinhe.foodfamily.main.entity;

/**
 * Created by shiyujia02 on 2018/1/25.
 */

public class PathEntity {
    private String path;
    private String size_id;
    private String title;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize_id() {
        return size_id;
    }

    public void setSize_id(String size_id) {
        this.size_id = size_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    private String controller;
}

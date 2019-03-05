package com.app.cookbook.xinhe.foodfamily.shiyujia.entity;

public class BannerEntity {

    /**
     * id : 1
     * name : 小炒
     * nutrition_tips : 微信
     * face_path : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/4F18710E-1024-47A2-9044-D980B0162595.png
     */

    private int id;
    private String name;
    private String nutrition_tips;
    private String face_path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNutrition_tips() {
        return nutrition_tips;
    }

    public void setNutrition_tips(String nutrition_tips) {
        this.nutrition_tips = nutrition_tips;
    }

    public String getFace_path() {
        return face_path;
    }

    public void setFace_path(String face_path) {
        this.face_path = face_path;
    }
}

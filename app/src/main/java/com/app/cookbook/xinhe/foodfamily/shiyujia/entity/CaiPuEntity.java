package com.app.cookbook.xinhe.foodfamily.shiyujia.entity;

import java.util.List;

public class CaiPuEntity {

    /**
     * current_page : 1
     * data : [{"id":2,"name":"小炒","nutrition_tips":"微信","face_path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/4F18710E-1024-47A2-9044-D980B0162595.png"}]
     * first_page_url : http://syj.cn/answerApi/own/ownCollectRecipe?page=1
     * from : 1
     * last_page : 1
     * last_page_url : http://syj.cn/answerApi/own/ownCollectRecipe?page=1
     * next_page_url : null
     * path : http://syj.cn/answerApi/own/ownCollectRecipe
     * per_page : 10
     * prev_page_url : null
     * to : 1
     * total : 1
     */

    private int current_page;
    private String first_page_url;
    private int from;
    private int last_page;
    private String last_page_url;
    private Object next_page_url;
    private String path;
    private int per_page;
    private Object prev_page_url;
    private int to;
    private int total;
    private List<DataBean> data;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public String getFirst_page_url() {
        return first_page_url;
    }

    public void setFirst_page_url(String first_page_url) {
        this.first_page_url = first_page_url;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public String getLast_page_url() {
        return last_page_url;
    }

    public void setLast_page_url(String last_page_url) {
        this.last_page_url = last_page_url;
    }

    public Object getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(Object next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public Object getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(Object prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 2
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
}

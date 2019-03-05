package com.app.cookbook.xinhe.foodfamily.main.entity;

/**
 * Created by shiyujia02 on 2018/5/29.
 */

public class NewAnswerDate {

    /**
     * id : 2440
     * uid : 259
     * created_at : 1527577417
     * thumbs : 6
     * name : 风信子
     * content_remove :
     * users_avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/12723c383dca401897e6cdffc52215bc.jpg
     * comment_count : 0
     * img_data : {"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/32ed038e9169490c9245eca324171394.jpg"}
     */

    private String id;
    private String uid;
    private String created_at;
    private String thumbs;
    private String name;
    private String content_remove;
    private String users_avatar;
    private String comment_count;
    private ImgDataBean img_data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getThumbs() {
        return thumbs;
    }

    public void setThumbs(String thumbs) {
        this.thumbs = thumbs;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
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


    public ImgDataBean getImg_data() {
        return img_data;
    }

    public void setImg_data(ImgDataBean img_data) {
        this.img_data = img_data;
    }

    public static class ImgDataBean {
        /**
         * path : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/32ed038e9169490c9245eca324171394.jpg
         */

        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}

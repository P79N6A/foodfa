package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

/**
 * Created by 18030150 on 2018/3/26.
 */

public class AnswerListOne {


    /**
     * comment_content : 测试数据
     * comment_id : 1
     * comment_thumbs : 0
     * is_thumbs : 2
     * is_thumbs_type : 未点赞
     * original_id : 0
     * son_data : [{"comment_content":"测试数据","comment_crated_at":1521697685,"comment_id":3,"comment_thumbs":0,"is_thumbs":2,"is_thumbs_type":"未点赞","original_id":1,"parent_users_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png","parent_users_id":170,"parent_users_name":"Kevin","users_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png","users_id":170,"users_name":"Kevin"},{"comment_content":"测试数据","comment_crated_at":1521697673,"comment_id":2,"comment_thumbs":0,"is_thumbs":2,"is_thumbs_type":"未点赞","original_id":1,"parent_users_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png","parent_users_id":170,"parent_users_name":"Kevin","users_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png","users_id":170,"users_name":"Kevin"}]
     * user_avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png
     * user_id : 170
     * user_name : Kevin
     */

    private String comment_content;
    private String comment_id;
    private String comment_thumbs;
    private String created_at;
    private String is_delete;
    private String is_thumbs;
    private String is_thumbs_type;
    private String original_id;
    private String son_count;
    private String user_avatar;
    private String user_id;
    private String user_name;
    private String parent_user_id;
    private String parent_user_name;
    private String parent_user_avatar;
    private List<SonDataBean> son_data;


    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_thumbs() {
        return comment_thumbs;
    }

    public void setComment_thumbs(String comment_thumbs) {
        this.comment_thumbs = comment_thumbs;
    }

    public String getIs_thumbs() {
        return is_thumbs;
    }

    public void setIs_thumbs(String is_thumbs) {
        this.is_thumbs = is_thumbs;
    }

    public String getIs_thumbs_type() {
        return is_thumbs_type;
    }

    public void setIs_thumbs_type(String is_thumbs_type) {
        this.is_thumbs_type = is_thumbs_type;
    }

    public String getOriginal_id() {
        return original_id;
    }

    public void setOriginal_id(String original_id) {
        this.original_id = original_id;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getSon_count() {
        return son_count;
    }

    public void setSon_count(String son_count) {
        this.son_count = son_count;
    }

    public List<SonDataBean> getSon_data() {
        return son_data;
    }

    public void setSon_data(List<SonDataBean> son_data) {
        this.son_data = son_data;
    }

    public String getParent_user_id() {
        return parent_user_id;
    }

    public void setParent_user_id(String parent_user_id) {
        this.parent_user_id = parent_user_id;
    }

    public String getParent_user_name() {
        return parent_user_name;
    }

    public void setParent_user_name(String parent_user_name) {
        this.parent_user_name = parent_user_name;
    }

    public String getParent_user_avatar() {
        return parent_user_avatar;
    }

    public void setParent_user_avatar(String parent_user_avatar) {
        this.parent_user_avatar = parent_user_avatar;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }
}

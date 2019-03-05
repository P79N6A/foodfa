package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

/**
 * Created by 18030150 on 2018/3/28.
 */

public class CommentLeveTwo {


    /**
     * current_page : 1
     * data : [{"comment_content":"测试数据","comment_id":2,"comment_thumbs":0,"created_at":1521697673,"is_thumbs":2,"is_thumbs_type":"未点赞","parent_user_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png","parent_user_id":170,"parent_user_name":"Kevin","user_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png","user_id":170,"user_name":"Kevin"},{"comment_content":"测试数据","comment_id":3,"comment_thumbs":0,"created_at":1521697685,"is_thumbs":2,"is_thumbs_type":"未点赞","parent_user_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png","parent_user_id":170,"parent_user_name":"Kevin","user_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png","user_id":170,"user_name":"Kevin"},{"comment_content":"耳机数据","comment_id":26,"comment_thumbs":0,"created_at":1521697851,"is_thumbs":2,"is_thumbs_type":"未点赞","parent_user_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png","parent_user_id":170,"parent_user_name":"Kevin","user_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/3091F40D-A08D-4CFB-8AAB-584E10EB015A.png","user_id":199,"user_name":"张大大家的小花大家记得记得看得开"},{"comment_content":"测试数据","comment_id":37,"comment_thumbs":0,"created_at":1521697998,"is_thumbs":2,"is_thumbs_type":"未点赞","parent_user_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png","parent_user_id":170,"parent_user_name":"Kevin","user_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png","user_id":170,"user_name":"Kevin"},{"comment_content":"测试数据","comment_id":38,"comment_thumbs":0,"created_at":1521711002,"is_thumbs":2,"is_thumbs_type":"未点赞","parent_user_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png","parent_user_id":170,"parent_user_name":"Kevin","user_avatar":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151703509225783703.jpg","user_id":52,"user_name":"悬崖边的蔡文姬"},{"comment_content":"测试数据","comment_id":40,"comment_thumbs":0,"created_at":1521798185,"is_thumbs":2,"is_thumbs_type":"未点赞","parent_user_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png","parent_user_id":170,"parent_user_name":"Kevin","user_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/4A1FC0BC-A40B-4EBC-9CDE-4166B7EE0EDB.png","user_id":255,"user_name":"徐小小家的小女孩"}]
     * first_page_url : http://app1.shiyujia.com/answerApi/comment/getCommentLevelTwo?page=1
     * from : 1
     * last_page : 1
     * last_page_url : http://app1.shiyujia.com/answerApi/comment/getCommentLevelTwo?page=1
     * original_data : {"comment_content":"测试数据","comment_id":1,"comment_thumbs":1,"created_at":1521697443,"user_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png","user_id":170,"user_name":"Kevin"}
     * path : http://app1.shiyujia.com/answerApi/comment/getCommentLevelTwo
     * per_page : 20
     * to : 6
     * total : 6
     */

    private String current_page;
    private String first_page_url;
    private String from;
    private String last_page;
    private String last_page_url;
    private OriginalDataBean original_data;
    private String path;
    private String per_page;
    private String to;
    private String total;
    private List<CommentLeveTwoData> data;

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public String getFirst_page_url() {
        return first_page_url;
    }

    public void setFirst_page_url(String first_page_url) {
        this.first_page_url = first_page_url;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getLast_page() {
        return last_page;
    }

    public void setLast_page(String last_page) {
        this.last_page = last_page;
    }

    public String getLast_page_url() {
        return last_page_url;
    }

    public void setLast_page_url(String last_page_url) {
        this.last_page_url = last_page_url;
    }

    public OriginalDataBean getOriginal_data() {
        return original_data;
    }

    public void setOriginal_data(OriginalDataBean original_data) {
        this.original_data = original_data;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPer_page() {
        return per_page;
    }

    public void setPer_page(String per_page) {
        this.per_page = per_page;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<CommentLeveTwoData> getData() {
        return data;
    }

    public void setData(List<CommentLeveTwoData> data) {
        this.data = data;
    }
}

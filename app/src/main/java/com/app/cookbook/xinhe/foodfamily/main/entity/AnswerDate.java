package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

/**
 * Created by shiyujia02 on 2018/2/3.
 */

public class AnswerDate {
    /**
     * id : 1502
     * pid : 1571
     * content : <p>节日快乐瘦了十几斤哦哦哦哦哦你<br></br>哦婆婆我婆婆<br></br>二二我婆婆我朋友婆婆他她</p>
     * uid : 259
     * status : 1
     * type : 1
     * created_at : 1522209958
     * thumbs : 0
     * collection : 0
     * is_existence : 1
     * name : hxbxnxnxnxn
     * avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e55216a6aad64b28b56b77f227983c64.jpg
     * answer_content_remove : 节日快乐瘦了十几斤哦哦哦哦哦你
     * 哦婆婆我婆婆
     * 二二我婆婆我朋友婆婆他她
     * question_title : 我
     * question_answer : 14
     * users_personal_signature : null
     * comment : 0
     * hot_comment : []
     * img_data : [{"id":1594,"pid":1502,"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/152125247043046196.jpg","type":1,"is_existence":1,"deleted_at":null},{"id":1595,"pid":1502,"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/152125248657229009.jpg","type":1,"is_existence":1,"deleted_at":null},{"id":1596,"pid":1502,"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/15212525151319696.jpg","type":1,"is_existence":1,"deleted_at":null},{"id":1597,"pid":1502,"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/152125253875341459.jpg","type":1,"is_existence":1,"deleted_at":null},{"id":1598,"pid":1502,"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/152125255465891337.jpg","type":1,"is_existence":1,"deleted_at":null}]
     * is_follow : 2
     * is_follow_text : 未关注
     * is_thumbs : 2
     * is_thumbs_text : 未点赞
     * is_collect : 2
     * is_collect_text : 未收藏
     */

    private int id;
    private int pid;
    private String content;
    private String uid;
    private int status;
    private int type;
    private String created_at;
    private int thumbs;
    private int collection;
    private int is_existence;
    private String name;
    private String avatar;
    private String answer_content_remove;
    private String question_title;
    private int question_answer;
    private Object users_personal_signature;
    private int comment;
    private List<HotComment> hot_comment;
    private int is_follow;
    private String is_follow_text;
    private int is_thumbs;
    private String is_thumbs_text;
    private int is_collect;
    private String is_collect_text;
    private List<ImgDataBean> img_data;
    private String is_delete;

    private QuestionData question_data;

    public QuestionData getQuestion_data() {
        return question_data;
    }

    public void setQuestion_data(QuestionData question_data) {
        this.question_data = question_data;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getThumbs() {
        return thumbs;
    }

    public void setThumbs(int thumbs) {
        this.thumbs = thumbs;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public int getIs_existence() {
        return is_existence;
    }

    public void setIs_existence(int is_existence) {
        this.is_existence = is_existence;
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

    public String getAnswer_content_remove() {
        return answer_content_remove;
    }

    public void setAnswer_content_remove(String answer_content_remove) {
        this.answer_content_remove = answer_content_remove;
    }

    public String getQuestion_title() {
        return question_title;
    }

    public void setQuestion_title(String question_title) {
        this.question_title = question_title;
    }

    public int getQuestion_answer() {
        return question_answer;
    }

    public void setQuestion_answer(int question_answer) {
        this.question_answer = question_answer;
    }

    public Object getUsers_personal_signature() {
        return users_personal_signature;
    }

    public void setUsers_personal_signature(Object users_personal_signature) {
        this.users_personal_signature = users_personal_signature;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    public String getIs_follow_text() {
        return is_follow_text;
    }

    public void setIs_follow_text(String is_follow_text) {
        this.is_follow_text = is_follow_text;
    }

    public int getIs_thumbs() {
        return is_thumbs;
    }

    public void setIs_thumbs(int is_thumbs) {
        this.is_thumbs = is_thumbs;
    }

    public String getIs_thumbs_text() {
        return is_thumbs_text;
    }

    public void setIs_thumbs_text(String is_thumbs_text) {
        this.is_thumbs_text = is_thumbs_text;
    }

    public int getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(int is_collect) {
        this.is_collect = is_collect;
    }

    public String getIs_collect_text() {
        return is_collect_text;
    }

    public void setIs_collect_text(String is_collect_text) {
        this.is_collect_text = is_collect_text;
    }


    public List<ImgDataBean> getImg_data() {
        return img_data;
    }

    public void setImg_data(List<ImgDataBean> img_data) {
        this.img_data = img_data;
    }

    public List<HotComment> getHot_comment() {
        return hot_comment;
    }

    public void setHot_comment(List<HotComment> hot_comment) {
        this.hot_comment = hot_comment;
    }

    public static class ImgDataBean {
        /**
         * id : 1594
         * pid : 1502
         * path : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/152125247043046196.jpg
         * type : 1
         * is_existence : 1
         * deleted_at : null
         */

        private int id;
        private int pid;
        private String path;
        private int type;
        private int is_existence;
        private Object deleted_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getIs_existence() {
            return is_existence;
        }

        public void setIs_existence(int is_existence) {
            this.is_existence = is_existence;
        }

        public Object getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(Object deleted_at) {
            this.deleted_at = deleted_at;
        }
    }

    public static class HotComment {

        /**
         * name : 张大大家的小花大家记得记得看得开
         * avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/3091F40D-A08D-4CFB-8AAB-584E10EB015A.png
         * user_id : 199
         * comment_content : 问题是什么
         * comment_created_at : 1521697791
         * comment_thumbs : 0
         * comment_id : 20
         */

        private String name;
        private String avatar;
        private String user_id;
        private String comment_content;
        private String comment_created_at;
        private String comment_id;
        private String comment_thumbs;
        private String is_thumbs;
        private String is_thumbs_type;
        private String son_count;

        public String getSon_count() {
            return son_count;
        }

        public void setSon_count(String son_count) {
            this.son_count = son_count;
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

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getComment_content() {
            return comment_content;
        }

        public void setComment_content(String comment_content) {
            this.comment_content = comment_content;
        }

        public String getComment_created_at() {
            return comment_created_at;
        }

        public void setComment_created_at(String comment_created_at) {
            this.comment_created_at = comment_created_at;
        }

        public String getComment_thumbs() {
            return comment_thumbs;
        }

        public void setComment_thumbs(String comment_thumbs) {
            this.comment_thumbs = comment_thumbs;
        }

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
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
    }

    public static class QuestionData {
        private String question_title;
        private String is_follow;

        public String getQuestion_title() {
            return question_title;
        }

        public void setQuestion_title(String question_title) {
            this.question_title = question_title;
        }

        public String getIs_follow() {
            return is_follow;
        }

        public void setIs_follow(String is_follow) {
            this.is_follow = is_follow;
        }
    }

}

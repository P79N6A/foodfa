package com.app.cookbook.xinhe.foodfamily.main.entity;


/**
 * Created by shiyujia02 on 2018/1/27.
 */

public class QuesEn {
    private String question_content;
    private String question_content_remove;
    private String question_id;
    private String question_title;
    private String answer_id;

    private String answer_content;

    public String getAnswer_content_remove() {
        return answer_content_remove;
    }

    public void setAnswer_content_remove(String answer_content_remove) {
        this.answer_content_remove = answer_content_remove;
    }

    private String answer_content_remove;
    private String answer_thumbs;
    private String user_id;
    private String user_name;
    private String user_avatar;
    private String is_follow;
    private String is_follow_text;
    private String is_thumbs;
    private String is_thumbs_text;
    private String is_collect;
    private String is_collect_text;
    private String answers_users_name;
    private String answers_users_id;
    private AnswerDataBean answer_data;


    public AnswerDataBean getAnswer_data() {
        return answer_data;
    }

    public void setAnswer_data(AnswerDataBean answer_data) {
        this.answer_data = answer_data;
    }

    public String getQuestion_content() {
        return question_content;
    }

    public void setQuestion_content(String question_content) {
        this.question_content = question_content;
    }

    public String getQuestion_content_remove() {
        return question_content_remove;
    }

    public void setQuestion_content_remove(String question_content_remove) {
        this.question_content_remove = question_content_remove;
    }

    public String getAnswer_created_at() {
        return answer_created_at;
    }

    public void setAnswer_created_at(String answer_created_at) {
        this.answer_created_at = answer_created_at;
    }

    private String answer_created_at;

    public String getAnswers_users_name() {
        return answers_users_name;
    }

    public void setAnswers_users_name(String answers_users_name) {
        this.answers_users_name = answers_users_name;
    }

    public String getAnswers_users_id() {
        return answers_users_id;
    }

    public void setAnswers_users_id(String answers_users_id) {
        this.answers_users_id = answers_users_id;
    }

    public String getAnswers_users_avatar() {
        return answers_users_avatar;
    }

    public void setAnswers_users_avatar(String answers_users_avatar) {
        this.answers_users_avatar = answers_users_avatar;
    }

    private String answers_users_avatar;

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public String getAnswer_content() {
        return answer_content;
    }

    public void setAnswer_content(String answer_content) {
        this.answer_content = answer_content;
    }

    public String getAnswer_thumbs() {
        return answer_thumbs;
    }

    public void setAnswer_thumbs(String answer_thumbs) {
        this.answer_thumbs = answer_thumbs;
    }

    public String getQuestion_title() {
        return question_title;
    }

    public void setQuestion_title(String question_title) {
        this.question_title = question_title;
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

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }

    public String getIs_follow_text() {
        return is_follow_text;
    }

    public void setIs_follow_text(String is_follow_text) {
        this.is_follow_text = is_follow_text;
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

    public ImageDate getImg_data() {
        return img_data;
    }

    public void setImg_data(ImageDate img_data) {
        this.img_data = img_data;
    }

    private ImageDate img_data;

    public static class AnswerDataBean {
        /**
         * comment_count : 0
         * content : <p><span >我是***。我喜欢***。我不是****。我喜欢**all。</span></p><p><span >我是***。我喜欢***。我不是****。我喜欢**all。</span></p>
         * content_remove : 我是***。我喜欢***。我不是****。我喜欢**all。我是***。我喜欢***。我不是****。我喜欢**all。
         * created_at : 1527474200
         * id : 2404
         * name : 徐小小家的小女孩
         * thumbs : 12
         * uid : 255
         * users_avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/91139CA8-800B-42C0-9B07-69CD0B477BF8.png
         */

        private String comment_count;
        private String content;
        private String content_remove;
        private String created_at;
        private String id;
        private Attention.AttentionBean.AnswerDataBean.imgDataBean img_data;
        private String name;
        private String thumbs;
        private String uid;
        private String users_avatar;

        public Attention.AttentionBean.AnswerDataBean.imgDataBean getImg_data() {
            return img_data;
        }

        public void setImg_data(Attention.AttentionBean.AnswerDataBean.imgDataBean img_data) {
            this.img_data = img_data;
        }

        public String getComment_count() {
            return comment_count;
        }

        public void setComment_count(String comment_count) {
            this.comment_count = comment_count;
        }

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

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getThumbs() {
            return thumbs;
        }

        public void setThumbs(String thumbs) {
            this.thumbs = thumbs;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUsers_avatar() {
            return users_avatar;
        }

        public void setUsers_avatar(String users_avatar) {
            this.users_avatar = users_avatar;
        }

        public static class imgDataBean {
            private String path;

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }
        }
    }

}

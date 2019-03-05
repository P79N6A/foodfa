package com.app.cookbook.xinhe.foodfamily.main.entity;

/**
 * Created by 18030150 on 2018/3/21.
 */

public class AnswerBean {
    private String answer_id;
    private String answer_content;
    private String answers_users_name;
    private String answer_content_remove;
    private String answers_users_avatar;
    private String answer_created_at;
    private String answers_users_id;
    private String is_thumbs;
    private String is_thumbs_text;
    private String is_collect;
    private String is_collect_text;
    private ImgDataBean img_data;

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

    public String getAnswers_users_name() {
        return answers_users_name;
    }

    public void setAnswers_users_name(String answers_users_name) {
        this.answers_users_name = answers_users_name;
    }

    public String getAnswers_users_avatar() {
        return answers_users_avatar;
    }

    public void setAnswers_users_avatar(String answers_users_avatar) {
        this.answers_users_avatar = answers_users_avatar;
    }

    public String getAnswer_created_at() {
        return answer_created_at;
    }

    public void setAnswer_created_at(String answer_created_at) {
        this.answer_created_at = answer_created_at;
    }

    public String getAnswers_users_id() {
        return answers_users_id;
    }

    public void setAnswers_users_id(String answers_users_id) {
        this.answers_users_id = answers_users_id;
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

//            public List<ImgDataBean> getImg_data() {
//                return img_data;
//            }
//
//            public void setImg_data(List<ImgDataBean> img_data) {
//                this.img_data = img_data;
//            }


    public String getAnswer_content_remove() {
        return answer_content_remove;
    }

    public void setAnswer_content_remove(String answer_content_remove) {
        this.answer_content_remove = answer_content_remove;
    }

    public ImgDataBean getImg_data() {
        return img_data;
    }

    public void setImg_data(ImgDataBean img_data) {
        this.img_data = img_data;
    }

    public static class ImgDataBean {
        /**
         * id : 1393
         * pid : 1410
         * path : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/3a71928311c04608b082088fa25abe00.jpg
         * type : 1
         * is_existence : 1
         * deleted_at : null
         */

        private String id;
        private String pid;
        private String path;
        private String type;
        private String is_existence;
        //private Object deleted_at;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIs_existence() {
            return is_existence;
        }

        public void setIs_existence(String is_existence) {
            this.is_existence = is_existence;
        }
//
//                public Object getDeleted_at() {
//                    return deleted_at;
//                }
//
//                public void setDeleted_at(Object deleted_at) {
//                    this.deleted_at = deleted_at;
//                }
    }
}

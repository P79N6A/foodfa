package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

/**
 * Created by shiyujia02 on 2018/1/31.
 */

public class GuanZhuWentiDate {


    /**
     * current_page : 1
     * data : [{"id":8,"question_id":1570,"type":2,"created_at":1522056980,"title":"tsgsgs","user_name":"徐小小家的小女孩","user_id":255,"user_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/4A1FC0BC-A40B-4EBC-9CDE-4166B7EE0EDB.png"}]
     * first_page_url : http://app1.shiyujia.com/answerApi/message/followUsersQuestionMessage?page=1
     * from : 1
     * last_page : 1
     * last_page_url : http://app1.shiyujia.com/answerApi/message/followUsersQuestionMessage?page=1
     * next_page_url : null
     * path : http://app1.shiyujia.com/answerApi/message/followUsersQuestionMessage
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
    private List<Guanzhuen> data;

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

    public List<Guanzhuen> getData() {
        return data;
    }

    public void setData(List<Guanzhuen> data) {
        this.data = data;
    }

    public static class Guanzhuen {
        /**
         * id : 8
         * question_id : 1570
         * type : 2
         * created_at : 1522056980
         * title : tsgsgs
         * user_name : 徐小小家的小女孩
         * user_id : 255
         * user_avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/4A1FC0BC-A40B-4EBC-9CDE-4166B7EE0EDB.png
         */

        private int id;
        private int created_at;
        private int question_id;
        private String question_is_existence;
        private String title;
        private int type;
        private String user_avatar;
        private int user_id;
        private String user_name;

        public String getQuestion_is_existence() {
            return question_is_existence;
        }

        public void setQuestion_is_existence(String question_is_existence) {
            this.question_is_existence = question_is_existence;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getQuestion_id() {
            return question_id;
        }

        public void setQuestion_id(int question_id) {
            this.question_id = question_id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getCreated_at() {
            return created_at;
        }

        public void setCreated_at(int created_at) {
            this.created_at = created_at;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUser_avatar() {
            return user_avatar;
        }

        public void setUser_avatar(String user_avatar) {
            this.user_avatar = user_avatar;
        }
    }

}

package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

/**
 * Created by shiyujia02 on 2018/3/28.
 */

public class WoGuanZhuEntity {
    /**
     * current_page : 1
     * data : [{"id":267,"name":"鲁尧","uid":212,"avatar":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151797590532185131.jpg","status":1,"created_at":1521025497,"questions":{"id":698,"title":"昨晚宿醉了，今天上午准时上班了！头晕、恶心、全身无力......如何破？？？？？？","created_at":1517999486}},{"id":280,"name":"Sharon.S","uid":69,"avatar":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151703981957694071.jpg","status":1,"created_at":1521089434,"questions":{"id":120,"title":"你小时候吃过哪些至今难忘的零食，好吃的不好吃的都说说","created_at":1517282501}},{"id":281,"name":"徐小小家的猫","uid":1,"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/C38D1D67-D639-4B62-8C19-0D8496074628.png","status":1,"created_at":1521095492,"questions":{"id":601,"title":"寿司蘸芥末到底应该怎么蘸？","created_at":1517915412}},{"id":300,"name":"徐小小家的小女孩","uid":255,"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/4A1FC0BC-A40B-4EBC-9CDE-4166B7EE0EDB.png","status":2,"created_at":1522217446,"questions":{"id":1402,"title":"红枸杞和黑枸杞的区别？","created_at":1520300457}}]
     * first_page_url : http://app1.shiyujia.com/answerApi/users/ownFollowUsers?page=1
     * from : 1
     * last_page : 1
     * last_page_url : http://app1.shiyujia.com/answerApi/users/ownFollowUsers?page=1
     * next_page_url : null
     * path : http://app1.shiyujia.com/answerApi/users/ownFollowUsers
     * per_page : 10
     * prev_page_url : null
     * to : 4
     * total : 4
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
         * id : 267
         * name : 鲁尧
         * uid : 212
         * avatar : http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151797590532185131.jpg
         * status : 1
         * created_at : 1521025497
         * questions : {"id":698,"title":"昨晚宿醉了，今天上午准时上班了！头晕、恶心、全身无力......如何破？？？？？？","created_at":1517999486}
         */

        private String id;
        private String status;
        private String name;
        private String uid;
        private String avatar;
        private String created_at;
        private DataBean.QuestionsBean questions;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public QuestionsBean getQuestions() {
            return questions;
        }

        public void setQuestions(QuestionsBean questions) {
            this.questions = questions;
        }

        public static class QuestionsBean {
            /**
             * id : 698
             * title : 昨晚宿醉了，今天上午准时上班了！头晕、恶心、全身无力......如何破？？？？？？
             * created_at : 1517999486
             */

            private int id;
            private String title;
            private int created_at;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getCreated_at() {
                return created_at;
            }

            public void setCreated_at(int created_at) {
                this.created_at = created_at;
            }
        }
    }

}

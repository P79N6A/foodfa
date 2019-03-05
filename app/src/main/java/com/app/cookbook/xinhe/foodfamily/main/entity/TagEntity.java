package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

public class TagEntity {

    /**
     * is_existence : 1
     * derivatives : {"id":242,"path":null,"title":"美食"}
     * data : [{"id":242,"title":"美食","path":null,"desc":null,"follow":0,"questions":69},{"id":34,"title":"各地美食","path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151703188462994121.jpg","desc":"美食与旅行","follow":45,"questions":68},{"id":69,"title":"地方美食","path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151727987455638383.png","desc":"中国地大物博，你的家乡有什么特色美食吗？","follow":4,"questions":42},{"id":178,"title":"DIY美食","path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151790635882977712.jpg","desc":"中式、西式各类DIY美食","follow":13,"questions":56},{"id":293,"title":"美食探店","path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appProduction/152473361227640997.jpg","desc":"探索各地好吃的店","follow":0,"questions":3},{"id":331,"title":"美食摄影","path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appProduction/label/2018_07/15312877851701967.jpg","desc":"让手机先吃","follow":7,"questions":5}]
     * total : 6
     * participle : ["美食"]
     */

    private String is_existence;
    private DerivativesBean derivatives;
    private int total;
    private List<DataBean> data;
    private List<String> participle;

    public String getIs_existence() {
        return is_existence;
    }

    public void setIs_existence(String is_existence) {
        this.is_existence = is_existence;
    }

    public DerivativesBean getDerivatives() {
        return derivatives;
    }

    public void setDerivatives(DerivativesBean derivatives) {
        this.derivatives = derivatives;
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

    public List<String> getParticiple() {
        return participle;
    }

    public void setParticiple(List<String> participle) {
        this.participle = participle;
    }

    public static class DerivativesBean {
        /**
         * id : 242
         * path : null
         * title : 美食
         */

        private int id;
        private String path;
        private String title;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class DataBean {
        /**
         * id : 242
         * title : 美食
         * path : null
         * desc : null
         * follow : 0
         * questions : 69
         */

        private String id;
        private String title;
        private String path;
        private String desc;
        private String follow;
        private String questions;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getFollow() {
            return follow;
        }

        public void setFollow(String follow) {
            this.follow = follow;
        }

        public String getQuestions() {
            return questions;
        }

        public void setQuestions(String questions) {
            this.questions = questions;
        }
    }

}

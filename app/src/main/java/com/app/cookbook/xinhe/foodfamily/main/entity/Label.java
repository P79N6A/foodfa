package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 18030150 on 2018/4/1.
 */

public class Label {

    /**
     * data : [{"desc":"美食与旅行","follow":10,"id":34,"path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151703188462994121.jpg","questions":78,"title":"各地美食"},{"desc":"中国地大物博，你的家乡有什么特色美食吗？","follow":0,"id":69,"path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151727987455638383.png","questions":28,"title":"地方美食"},{"desc":"中式、西式各类DIY美食","follow":0,"id":178,"path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151790635882977712.jpg","questions":31,"title":"DIY美食"},{"follow":0,"id":204,"questions":26,"title":"美食"},{"follow":0,"id":261,"questions":1,"title":"各地美食"}]
     * derivatives : {"id":204,"title":"美食"}
     * is_existence : 1
     * participle : ["美食"]
     * total : 5
     */

    private DerivativesBean derivatives;
    private String is_existence;
    private String total;
    private List<DataBean> data;
    private List<String> participle;

    public DerivativesBean getDerivatives() {
        return derivatives;
    }

    public void setDerivatives(DerivativesBean derivatives) {
        this.derivatives = derivatives;
    }

    public String getIs_existence() {
        return is_existence;
    }

    public void setIs_existence(String is_existence) {
        this.is_existence = is_existence;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
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
         * id : 204
         * title : 美食
         */

        private String id;
        private String title;

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
    }

    public static class DataBean implements Serializable {
        /**
         * desc : 美食与旅行
         * follow : 10
         * id : 34
         * path : http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151703188462994121.jpg
         * questions : 78
         * title : 各地美食
         */

        private String desc;
        private String follow;
        private String id;
        private String path;
        private String questions;
        private String title;
        private boolean isCheck;

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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getQuestions() {
            return questions;
        }

        public void setQuestions(String questions) {
            this.questions = questions;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }
    }
}

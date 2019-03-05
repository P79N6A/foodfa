package com.app.cookbook.xinhe.foodfamily.shiyujia.entity;

import java.util.List;

public class NewHandpick {

    /**
     * current_page : 1
     * data : [{"id":2572,"is_follow":2,"title":"朋友万事如意"},{"id":2571,"is_follow":2,"title":"可以"},{"id":2570,"is_follow":2,"title":"哦里"},{"id":2569,"is_follow":2,"title":"自己"},{"id":2568,"is_follow":2,"title":"家里"},{"id":2567,"is_follow":2,"title":"提交"},{"id":2566,"is_follow":2,"title":"你你你"},{"id":2565,"is_follow":2,"title":"我去"},{"id":2564,"is_follow":2,"title":"路"},{"id":2563,"is_follow":2,"title":"磨破"},{"id":2562,"is_follow":2,"title":"弄"},{"id":2561,"is_follow":2,"title":"掏钱"},{"id":2560,"is_follow":2,"title":"噢泼泼"},{"id":2559,"is_follow":2,"title":"没啥意思"},{"id":2558,"is_follow":2,"title":"热体忒"},{"id":2557,"is_follow":2,"title":"下午"},{"id":2556,"is_follow":2,"title":"提交"},{"id":2555,"is_follow":2,"title":"磨砂"},{"id":2554,"is_follow":2,"title":"哦去你"},{"id":2553,"is_follow":2,"title":"哦去你"}]
     * first_page_url : http://app1.shiyujia.com/answerApi/interlocution/getQuestion?page=1
     * from : 1
     * last_page : 126
     * last_page_url : http://app1.shiyujia.com/answerApi/interlocution/getQuestion?page=126
     * next_page_url : http://app1.shiyujia.com/answerApi/interlocution/getQuestion?page=2
     * path : http://app1.shiyujia.com/answerApi/interlocution/getQuestion
     * per_page : 20
     * to : 20
     * total : 2502
     */

    private String current_page;
    private String first_page_url;
    private String from;
    private String last_page;
    private String last_page_url;
    private String next_page_url;
    private String path;
    private String per_page;
    private String to;
    private String total;
    private List<DataBean> data;

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

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 2572
         * is_follow : 2
         * title : 朋友万事如意
         */

        private String id;
        private String is_follow;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIs_follow() {
            return is_follow;
        }

        public void setIs_follow(String is_follow) {
            this.is_follow = is_follow;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}

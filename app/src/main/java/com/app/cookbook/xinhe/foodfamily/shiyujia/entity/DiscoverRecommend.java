package com.app.cookbook.xinhe.foodfamily.shiyujia.entity;

import java.util.List;

public class DiscoverRecommend {

    /**
     * current_page : 1
     * data : [{"id":34,"path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151703188462994121.jpg","title":"各地美食"},{"id":79,"path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151728444982737375.jpg","title":"旅行"},{"id":24,"path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/15172197365742237.jpg","title":"下饭神剧"},{"id":9,"path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151702178414456001.jpg","title":"鸡尾酒"},{"id":331,"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appProduction/label/2018_07/15312877851701967.jpg","title":"美食摄影"},{"id":334,"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appProduction/label/2018_08/153447270614549562.jpg","title":"脑洞"},{"id":19,"path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151702297165409964.jpg","title":"饮食文化"},{"id":218,"path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151946256857713590.jpg","title":"咖啡"},{"id":332,"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appProduction/other/2018_07/153127647011086428.jpg","title":"冰淇淋"},{"id":5,"path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151702095494128383.jpg","title":"烘焙"}]
     * first_page_url : http://app1.shiyujia.com/answerApi/find/recommendClass?page=1
     * from : 1
     * last_page : 3
     * last_page_url : http://app1.shiyujia.com/answerApi/find/recommendClass?page=3
     * next_page_url : http://app1.shiyujia.com/answerApi/find/recommendClass?page=2
     * path : http://app1.shiyujia.com/answerApi/find/recommendClass
     * per_page : 10
     * to : 10
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
         * id : 34
         * path : http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151703188462994121.jpg
         * title : 各地美食
         */

        private String id;
        private String path;
        private String title;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}

package com.app.cookbook.xinhe.foodfamily.shiyujia.entity;

import java.util.List;

public class MyDraftsPrint {

    /**
     * current_page : 1
     * data : [{"class_ids":"","content":"草稿","created_at":1539079370,"id":4,"image_data":[{"id":1,"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/answer/2018_09/562831f99fe14bd486cc609efad57d8c.jpg","size":"1:1","total_id":4}]},{"class_ids":"","content":"草稿","created_at":1539079302,"id":3},{"class_ids":"","content":"草稿","created_at":1539079300,"id":2},{"class_ids":"","content":"草稿","created_at":1539079292,"id":1}]
     * first_page_url : http://syj.cn/answerApi/draft/draftList?page=1
     * from : 1
     * last_page : 1
     * last_page_url : http://syj.cn/answerApi/draft/draftList?page=1
     * path : http://syj.cn/answerApi/draft/draftList
     * per_page : 20
     * to : 4
     * total : 4
     */

    private String current_page;
    private String first_page_url;
    private String from;
    private String last_page;
    private String last_page_url;
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
         * class_ids :
         * content : 草稿
         * created_at : 1539079370
         * id : 4
         * image_data : [{"id":1,"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/answer/2018_09/562831f99fe14bd486cc609efad57d8c.jpg","size":"1:1","total_id":4}]
         */

        private String class_ids;
        private String content;
        private String created_at;
        private String id;
        private List<ImageDataBean> image_data;

        public String getClass_ids() {
            return class_ids;
        }

        public void setClass_ids(String class_ids) {
            this.class_ids = class_ids;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public List<ImageDataBean> getImage_data() {
            return image_data;
        }

        public void setImage_data(List<ImageDataBean> image_data) {
            this.image_data = image_data;
        }

        public static class ImageDataBean {
            /**
             * id : 1
             * path : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/answer/2018_09/562831f99fe14bd486cc609efad57d8c.jpg
             * size : 1:1
             * total_id : 4
             */

            private String id;
            private String path;
            private String size;
            private String total_id;

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

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public String getTotal_id() {
                return total_id;
            }

            public void setTotal_id(String total_id) {
                this.total_id = total_id;
            }
        }
    }
}

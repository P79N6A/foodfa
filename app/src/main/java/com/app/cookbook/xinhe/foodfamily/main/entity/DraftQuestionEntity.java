package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

/**
 * Created by shiyujia02 on 2018/3/22.
 */

public class DraftQuestionEntity {

    /**
     * current_page : 1
     * data : [{"id":62,"title":"草稿2","content":"<p>vivo过了头刘谋<\/p><img src=\"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/eb9c1f0b52104bff9bf4f0e60734f3f0.jpg\"/><p> <\/p><p> <\/p>\n<\/br>","content_remove":"vivo过了头刘谋  \n","classification_id":"","updated_at":1521688746,"img_data":[{"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/eb9c1f0b52104bff9bf4f0e60734f3f0.jpg"}]},{"id":61,"title":"草稿1","content":"<p>吉利提议一下<\/p><img src=\"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/fcff501014724c66a0e57e35a4ffdea1.jpg\"/><img src=\"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/d5dbffd3be3e4a40915529a9f5d6bbf0.jpg\"/><p> <\/p><p> <\/p><p> <\/p>\n<\/br>","content_remove":"吉利提议一下   \n","classification_id":"","updated_at":1521688659,"img_data":[]}]
     * first_page_url : http://app1.shiyujia.com/answerApi/draftQuestion/getDraftQuestionList?page=1
     * from : 1
     * last_page : 1
     * last_page_url : http://app1.shiyujia.com/answerApi/draftQuestion/getDraftQuestionList?page=1
     * next_page_url : null
     * path : http://app1.shiyujia.com/answerApi/draftQuestion/getDraftQuestionList
     * per_page : 10
     * prev_page_url : null
     * to : 2
     * total : 2
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
    private List<DraftQuestionEn> data;

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

    public List<DraftQuestionEn> getData() {
        return data;
    }

    public void setData(List<DraftQuestionEn> data) {
        this.data = data;
    }

    public static class DraftQuestionEn {
        /**
         * id : 62
         * title : 草稿2
         * content : <p>vivo过了头刘谋</p><img src="https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/eb9c1f0b52104bff9bf4f0e60734f3f0.jpg"/><p> </p><p> </p>
         * </br>
         * content_remove : vivo过了头刘谋
         * <p>
         * classification_id :
         * updated_at : 1521688746
         * img_data : [{"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/eb9c1f0b52104bff9bf4f0e60734f3f0.jpg"}]
         */

        private int id;
        private String title;
        private String content;
        private String content_remove;
        private String classification_id;
        private int updated_at;
        private List<ImgDataBean> img_data;

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

        public String getClassification_id() {
            return classification_id;
        }

        public void setClassification_id(String classification_id) {
            this.classification_id = classification_id;
        }

        public int getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(int updated_at) {
            this.updated_at = updated_at;
        }

        public List<ImgDataBean> getImg_data() {
            return img_data;
        }

        public void setImg_data(List<ImgDataBean> img_data) {
            this.img_data = img_data;
        }

        public static class ImgDataBean {
            /**
             * path : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/eb9c1f0b52104bff9bf4f0e60734f3f0.jpg
             */

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

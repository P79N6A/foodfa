package com.app.cookbook.xinhe.foodfamily.shiyujia.entity;

import java.util.List;

public class FriendAttentionLabel {
    /**
     * current_page : 1
     * data : [{"class_id":8,"user_count":26,"content_count":88,"class_data":{"id":8,"title":"宝宝辅食","path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151788148580834728.jpg","desc":"宝宝从6个月开始就可以开始添加辅食，丰富多样化的辅食关系到宝宝一生的健康"}},{"class_id":15,"user_count":52,"content_count":246,"class_data":{"id":15,"title":"母婴","path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151702267663344515.jpg","desc":"一切有关妈妈宝宝"}},{"class_id":19,"user_count":63,"content_count":223,"class_data":{"id":19,"title":"饮食文化","path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151702297165409964.jpg","desc":"吃货不是一天练成的，他有上下五千年。"}},{"class_id":68,"user_count":59,"content_count":252,"class_data":{"id":68,"title":"酒","path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151727961733714458.png","desc":"白酒、黄酒、烧酒、清酒~各种酒~"}},{"class_id":5,"user_count":95,"content_count":161,"class_data":{"id":5,"title":"烘焙","path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151702095494128383.jpg","desc":"烘焙爱好者聚集地"}},{"class_id":6,"user_count":61,"content_count":211,"class_data":{"id":6,"title":"零食","path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151788113239493387.jpg","desc":"它是让你今天吃完嚷嚷要减肥，明天看见又忍不住吃的薯片虾条；是下午一小把，营养全拿下的坚果干果。它是网红，也是经典；是回忆，却很日常；是美味，也谈健康。它游离于三餐之外，时刻出现在你我身边，它是你的最爱零食。"}},{"class_id":81,"user_count":92,"content_count":162,"class_data":{"id":81,"title":"食谱","path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/15172909463043349.jpg","desc":"还原烹饪的备忘录"}},{"class_id":24,"user_count":10,"content_count":19,"class_data":{"id":24,"title":"下饭神剧","path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/15172197365742237.jpg","desc":"谁说下饭的只有好菜，神剧也是。"}},{"class_id":34,"user_count":71,"content_count":98,"class_data":{"id":34,"title":"各地美食","path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151703188462994121.jpg","desc":"美食与旅行"}},{"class_id":9,"user_count":25,"content_count":47,"class_data":{"id":9,"title":"鸡尾酒","path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151702178414456001.jpg","desc":"cocktail，激发无边灵感~Mixologist大欢迎~，一日之计在于晨，我们不知道到底无签名改为民服务范围么发我么发松岛枫第三方额我免费个说三道四冯绍峰身份而为了我翻了翻为风微风我威风威风饿了雷锋费我风微风违法违法为风微风风我微风给我个我的发的为风微风我额"}}]
     * first_page_url : http://app1.shiyujia.com/answerApi/other/otherFollowClass?page=1
     * from : 1
     * last_page : 1
     * last_page_url : http://app1.shiyujia.com/answerApi/other/otherFollowClass?page=1
     * next_page_url : null
     * path : http://app1.shiyujia.com/answerApi/other/otherFollowClass
     * per_page : 10
     * prev_page_url : null
     * to : 10
     * total : 10
     */

    private String current_page;
    private String first_page_url;
    private String from;
    private String last_page;
    private String last_page_url;
    private Object next_page_url;
    private String path;
    private String per_page;
    private Object prev_page_url;
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

    public String getPer_page() {
        return per_page;
    }

    public void setPer_page(String per_page) {
        this.per_page = per_page;
    }

    public Object getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(Object prev_page_url) {
        this.prev_page_url = prev_page_url;
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
         * class_id : 8
         * user_count : 26
         * content_count : 88
         * class_data : {"id":8,"title":"宝宝辅食","path":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151788148580834728.jpg","desc":"宝宝从6个月开始就可以开始添加辅食，丰富多样化的辅食关系到宝宝一生的健康"}
         */

        private String class_id;
        private String user_count;
        private String content_count;
        private ClassDataBean class_data;

        public String getClass_id() {
            return class_id;
        }

        public void setClass_id(String class_id) {
            this.class_id = class_id;
        }

        public String getUser_count() {
            return user_count;
        }

        public void setUser_count(String user_count) {
            this.user_count = user_count;
        }

        public String getContent_count() {
            return content_count;
        }

        public void setContent_count(String content_count) {
            this.content_count = content_count;
        }

        public ClassDataBean getClass_data() {
            return class_data;
        }

        public void setClass_data(ClassDataBean class_data) {
            this.class_data = class_data;
        }

        public static class ClassDataBean {
            /**
             * id : 8
             * title : 宝宝辅食
             * path : http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151788148580834728.jpg
             * desc : 宝宝从6个月开始就可以开始添加辅食，丰富多样化的辅食关系到宝宝一生的健康
             */

            private String id;
            private String title;
            private String path;
            private String desc;

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
        }
    }
}

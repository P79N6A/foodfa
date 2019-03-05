package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

public class SuggestedUsers {

    /**
     * current_page : 1
     * data : [{"sort":0,"user_data":{"answer_count":22,"id":255,"name":"徐小小家的小女孩","thumbs_count":17},"user_id":255},{"sort":1,"user_data":{"answer_count":5,"id":249,"name":"oplm","thumbs_count":2},"user_id":249},{"sort":5,"user_data":{"answer_count":1,"id":240,"name":"西风","thumbs_count":2},"user_id":240},{"sort":6,"user_data":{"answer_count":0,"id":222,"name":"爱吃猪肉的净坛使者","thumbs_count":0},"user_id":222}]
     * first_page_url : http://app1.shiyujia.com/answerApi/attention/getFollowUsers?page=1
     * from : 1
     * last_page : 1
     * last_page_url : http://app1.shiyujia.com/answerApi/attention/getFollowUsers?page=1
     * path : http://app1.shiyujia.com/answerApi/attention/getFollowUsers
     * per_page : 10
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
         * sort : 0
         * user_data : {"answer_count":22,"id":255,"name":"徐小小家的小女孩","thumbs_count":17}
         * user_id : 255
         */

        private String sort;
        private UserDataBean user_data;
        private String user_id;

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public UserDataBean getUser_data() {
            return user_data;
        }

        public void setUser_data(UserDataBean user_data) {
            this.user_data = user_data;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public static class UserDataBean {
            /**
             * answer_count : 22
             * id : 255
             * name : 徐小小家的小女孩
             * thumbs_count : 17
             */

            private String answer_count;
            private String id;
            private String name;
            private String thumbs_count;
            private String avatar;

            public String getAnswer_count() {
                return answer_count;
            }

            public void setAnswer_count(String answer_count) {
                this.answer_count = answer_count;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getThumbs_count() {
                return thumbs_count;
            }

            public void setThumbs_count(String thumbs_count) {
                this.thumbs_count = thumbs_count;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }
    }
}

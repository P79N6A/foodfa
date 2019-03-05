package com.app.cookbook.xinhe.foodfamily.shiyujia.entity;

import java.util.List;

public class ImageTextComment {


    /**
     * current_page : 1
     * data : [{"id":88,"content":"哈哈","user_id":263,"level":1,"thumbs":0,"created_at":1544164844,"type":1,"total_id":421,"is_thumbs":2,"user_data":{"id":263,"name":"醉","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/users/2018_11/ca378313d28e42d8b352610dedc66e23.jpg"},"is_existence":2,"son_data":null,"son_count":0},{"id":86,"content":"你好，3","user_id":249,"level":1,"thumbs":0,"created_at":1544161886,"type":1,"total_id":419,"is_thumbs":2,"user_data":{"id":249,"name":"oplm。","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/users/2018_09/de17bccf50da4966830f747425d3170c.jpg"},"is_existence":2,"son_data":null,"son_count":0},{"id":85,"content":"你好，2","user_id":249,"level":1,"thumbs":0,"created_at":1544161877,"type":1,"total_id":418,"is_thumbs":2,"user_data":{"id":249,"name":"oplm。","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/users/2018_09/de17bccf50da4966830f747425d3170c.jpg"},"is_existence":2,"son_data":null,"son_count":0},{"id":84,"content":"你好","user_id":249,"level":1,"thumbs":0,"created_at":1544161851,"type":1,"total_id":417,"is_thumbs":2,"user_data":{"id":249,"name":"oplm。","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/users/2018_09/de17bccf50da4966830f747425d3170c.jpg"},"is_existence":2,"son_data":{"id":87,"content":"爱","user_id":249,"level":2,"thumbs":0,"created_at":1544161923,"type":1,"total_id":420,"parent_uid":249,"parent_id":84,"original_id":84,"is_thumbs":2,"parent_data":{"id":249,"name":"oplm。","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/users/2018_09/de17bccf50da4966830f747425d3170c.jpg"},"user_data":{"id":249,"name":"oplm。","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/users/2018_09/de17bccf50da4966830f747425d3170c.jpg"}},"son_count":1}]
     * first_page_url : http://app1.shiyujia.com/answerApi/imageText/getImageTextComment?page=1
     * from : 1
     * last_page : 1
     * last_page_url : http://app1.shiyujia.com/answerApi/imageText/getImageTextComment?page=1
     * next_page_url : null
     * path : http://app1.shiyujia.com/answerApi/imageText/getImageTextComment
     * per_page : 10
     * prev_page_url : null
     * to : 4
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
         * id : 88
         * content : 哈哈
         * user_id : 263
         * level : 1
         * thumbs : 0
         * created_at : 1544164844
         * type : 1
         * total_id : 421
         * is_thumbs : 2
         * user_data : {"id":263,"name":"醉","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/users/2018_11/ca378313d28e42d8b352610dedc66e23.jpg"}
         * is_existence : 2
         * son_data : null
         * son_count : 0
         */

        private String id;
        private String content;
        private String user_id;
        private String level;
        private String thumbs;
        private String created_at;
        private String type;
        private String total_id;
        private String is_thumbs;
        private UserDataBean user_data;
        private String is_existence;
        private SonDataBean son_data;
        private String son_count;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getThumbs() {
            return thumbs;
        }

        public void setThumbs(String thumbs) {
            this.thumbs = thumbs;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTotal_id() {
            return total_id;
        }

        public void setTotal_id(String total_id) {
            this.total_id = total_id;
        }

        public String getIs_thumbs() {
            return is_thumbs;
        }

        public void setIs_thumbs(String is_thumbs) {
            this.is_thumbs = is_thumbs;
        }

        public UserDataBean getUser_data() {
            return user_data;
        }

        public void setUser_data(UserDataBean user_data) {
            this.user_data = user_data;
        }

        public String getIs_existence() {
            return is_existence;
        }

        public void setIs_existence(String is_existence) {
            this.is_existence = is_existence;
        }


        public String getSon_count() {
            return son_count;
        }

        public void setSon_count(String son_count) {
            this.son_count = son_count;
        }

        public SonDataBean getSon_data() {
            return son_data;
        }

        public void setSon_data(SonDataBean son_data) {
            this.son_data = son_data;
        }

        public static class UserDataBean {
            /**
             * id : 263
             * name : 醉
             * avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/users/2018_11/ca378313d28e42d8b352610dedc66e23.jpg
             */

            private String id;
            private String name;
            private String avatar;

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

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }

        public static class SonDataBean {


            /**
             * id : 2
             * content : 兔兔
             * user_id : 249
             * level : 2
             * thumbs : 0
             * created_at : 1544595815
             * type : 1
             * total_id : 9
             * parent_uid : 249
             * parent_id : 1
             * original_id : 1
             * is_thumbs : 2
             * parent_data : {"id":249,"name":"oplm","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/624586bd5f614d40bc77676846864a29.jpg"}
             * user_data : {"id":249,"name":"oplm","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/624586bd5f614d40bc77676846864a29.jpg"}
             */

            private String id;
            private String content;
            private String user_id;
            private String level;
            private String thumbs;
            private String created_at;
            private String type;
            private String total_id;
            private String parent_uid;
            private String parent_id;
            private String original_id;
            private String is_thumbs;
            private ParentDataBean parent_data;
            private UserDataBean user_data;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getThumbs() {
                return thumbs;
            }

            public void setThumbs(String thumbs) {
                this.thumbs = thumbs;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTotal_id() {
                return total_id;
            }

            public void setTotal_id(String total_id) {
                this.total_id = total_id;
            }

            public String getParent_uid() {
                return parent_uid;
            }

            public void setParent_uid(String parent_uid) {
                this.parent_uid = parent_uid;
            }

            public String getParent_id() {
                return parent_id;
            }

            public void setParent_id(String parent_id) {
                this.parent_id = parent_id;
            }

            public String getOriginal_id() {
                return original_id;
            }

            public void setOriginal_id(String original_id) {
                this.original_id = original_id;
            }

            public String getIs_thumbs() {
                return is_thumbs;
            }

            public void setIs_thumbs(String is_thumbs) {
                this.is_thumbs = is_thumbs;
            }

            public ParentDataBean getParent_data() {
                return parent_data;
            }

            public void setParent_data(ParentDataBean parent_data) {
                this.parent_data = parent_data;
            }

            public UserDataBean getUser_data() {
                return user_data;
            }

            public void setUser_data(UserDataBean user_data) {
                this.user_data = user_data;
            }

            public static class ParentDataBean {
                /**
                 * id : 249
                 * name : oplm
                 * avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/624586bd5f614d40bc77676846864a29.jpg
                 */

                private String id;
                private String name;
                private String avatar;

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

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }
            }

            public static class UserDataBean {
                /**
                 * id : 249
                 * name : oplm
                 * avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/624586bd5f614d40bc77676846864a29.jpg
                 */

                private String id;
                private String name;
                private String avatar;

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

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }
            }
        }
    }
}

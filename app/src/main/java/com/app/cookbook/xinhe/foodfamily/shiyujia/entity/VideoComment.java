package com.app.cookbook.xinhe.foodfamily.shiyujia.entity;

import java.util.List;

public class VideoComment {

    /**
     * current_page : 1
     * data : [{"content":"啦啦啦啦","created_at":1539827809,"id":37,"is_existence":2,"is_thumbs":2,"level":1,"son_count":0,"thumbs":0,"total_id":73,"type":1,"user_data":{"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg","id":263,"name":"霸王被坑"},"user_id":263},{"content":"啦啦啦啦","created_at":1539827809,"id":36,"is_existence":2,"is_thumbs":2,"level":1,"son_count":0,"thumbs":0,"total_id":72,"type":1,"user_data":{"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg","id":263,"name":"霸王被坑"},"user_id":263},{"content":"啦啦啦啦","created_at":1539827808,"id":35,"is_existence":2,"is_thumbs":2,"level":1,"son_count":0,"thumbs":0,"total_id":71,"type":1,"user_data":{"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg","id":263,"name":"霸王被坑"},"user_id":263},{"content":"啦啦啦啦","created_at":1539827808,"id":34,"is_existence":2,"is_thumbs":2,"level":1,"son_count":0,"thumbs":0,"total_id":70,"type":1,"user_data":{"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg","id":263,"name":"霸王被坑"},"user_id":263},{"content":"啦啦啦啦","created_at":1539827805,"id":33,"is_existence":2,"is_thumbs":2,"level":1,"son_count":0,"thumbs":0,"total_id":69,"type":1,"user_data":{"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg","id":263,"name":"霸王被坑"},"user_id":263},{"content":"啦啦啦啦","created_at":1539827805,"id":32,"is_existence":2,"is_thumbs":2,"level":1,"son_count":0,"thumbs":0,"total_id":68,"type":1,"user_data":{"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg","id":263,"name":"霸王被坑"},"user_id":263},{"content":"突突突突突突","created_at":1539765946,"id":28,"is_existence":2,"is_thumbs":2,"level":1,"son_count":2,"son_data":{"content":"啦啦啦","created_at":1539774175,"id":31,"is_thumbs":2,"level":2,"original_id":28,"parent_data":{"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg","id":263,"name":"霸王被坑"},"parent_id":28,"parent_uid":263,"thumbs":0,"total_id":67,"type":1,"user_data":{"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg","id":263,"name":"霸王被坑"},"user_id":263},"thumbs":0,"total_id":60,"type":1,"user_data":{"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg","id":263,"name":"霸王被坑"},"user_id":263},{"content":"突突突突突突","created_at":1539765925,"id":27,"is_existence":2,"is_thumbs":2,"level":1,"son_count":0,"thumbs":0,"total_id":59,"type":1,"user_data":{"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg","id":263,"name":"霸王被坑"},"user_id":263},{"content":"陌陌陌陌默默哦","created_at":1539748355,"id":26,"is_existence":2,"is_thumbs":2,"level":1,"son_count":0,"thumbs":0,"total_id":58,"type":1,"user_data":{"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg","id":263,"name":"霸王被坑"},"user_id":263},{"content":"陌陌陌陌默默哦","created_at":1539748355,"id":25,"is_existence":2,"is_thumbs":2,"level":1,"son_count":0,"thumbs":0,"total_id":57,"type":1,"user_data":{"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg","id":263,"name":"霸王被坑"},"user_id":263}]
     * first_page_url : http://app1.shiyujia.com/answerApi/video/getVideoComment?page=1
     * from : 1
     * last_page : 2
     * last_page_url : http://app1.shiyujia.com/answerApi/video/getVideoComment?page=2
     * next_page_url : http://app1.shiyujia.com/answerApi/video/getVideoComment?page=2
     * path : http://app1.shiyujia.com/answerApi/video/getVideoComment
     * per_page : 10
     * to : 10
     * total : 12
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
    private List<VideoComment.DataBean> data;

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

    public List<VideoComment.DataBean> getData() {
        return data;
    }

    public void setData(List<VideoComment.DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * content : 啦啦啦啦
         * created_at : 1539827809
         * id : 37
         * is_existence : 2
         * is_thumbs : 2
         * level : 1
         * son_count : 0
         * thumbs : 0
         * total_id : 73
         * type : 1
         * user_data : {"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg","id":263,"name":"霸王被坑"}
         * user_id : 263
         * son_data : {"content":"啦啦啦","created_at":1539774175,"id":31,"is_thumbs":2,"level":2,"original_id":28,"parent_data":{"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg","id":263,"name":"霸王被坑"},"parent_id":28,"parent_uid":263,"thumbs":0,"total_id":67,"type":1,"user_data":{"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg","id":263,"name":"霸王被坑"},"user_id":263}
         */

        private String content;
        private String created_at;
        private String id;
        private String is_existence;
        private String is_thumbs;
        private String level;
        private String son_count;
        private String thumbs;
        private String total_id;
        private String type;
        private UserDataBean user_data;
        private String user_id;
        private SonDataBean son_data;

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

        public String getIs_existence() {
            return is_existence;
        }

        public void setIs_existence(String is_existence) {
            this.is_existence = is_existence;
        }

        public String getIs_thumbs() {
            return is_thumbs;
        }

        public void setIs_thumbs(String is_thumbs) {
            this.is_thumbs = is_thumbs;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getSon_count() {
            return son_count;
        }

        public void setSon_count(String son_count) {
            this.son_count = son_count;
        }

        public String getThumbs() {
            return thumbs;
        }

        public void setThumbs(String thumbs) {
            this.thumbs = thumbs;
        }

        public String getTotal_id() {
            return total_id;
        }

        public void setTotal_id(String total_id) {
            this.total_id = total_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public SonDataBean getSon_data() {
            return son_data;
        }

        public void setSon_data(SonDataBean son_data) {
            this.son_data = son_data;
        }

        public static class UserDataBean {
            /**
             * avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg
             * id : 263
             * name : 霸王被坑
             */

            private String avatar;
            private String id;
            private String name;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
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
        }

        public static class SonDataBean {
            /**
             * content : 啦啦啦
             * created_at : 1539774175
             * id : 31
             * is_thumbs : 2
             * level : 2
             * original_id : 28
             * parent_data : {"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg","id":263,"name":"霸王被坑"}
             * parent_id : 28
             * parent_uid : 263
             * thumbs : 0
             * total_id : 67
             * type : 1
             * user_data : {"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg","id":263,"name":"霸王被坑"}
             * user_id : 263
             */

            private String content;
            private String created_at;
            private String id;
            private String is_thumbs;
            private String level;
            private String original_id;
            private ParentDataBean parent_data;
            private String parent_id;
            private String parent_uid;
            private String thumbs;
            private String total_id;
            private String type;
            private UserDataBeanX user_data;
            private String user_id;

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

            public String getIs_thumbs() {
                return is_thumbs;
            }

            public void setIs_thumbs(String is_thumbs) {
                this.is_thumbs = is_thumbs;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getOriginal_id() {
                return original_id;
            }

            public void setOriginal_id(String original_id) {
                this.original_id = original_id;
            }

            public SonDataBean.ParentDataBean getParent_data() {
                return parent_data;
            }

            public void setParent_data(SonDataBean.ParentDataBean parent_data) {
                this.parent_data = parent_data;
            }

            public String getParent_id() {
                return parent_id;
            }

            public void setParent_id(String parent_id) {
                this.parent_id = parent_id;
            }

            public String getParent_uid() {
                return parent_uid;
            }

            public void setParent_uid(String parent_uid) {
                this.parent_uid = parent_uid;
            }

            public String getThumbs() {
                return thumbs;
            }

            public void setThumbs(String thumbs) {
                this.thumbs = thumbs;
            }

            public String getTotal_id() {
                return total_id;
            }

            public void setTotal_id(String total_id) {
                this.total_id = total_id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public UserDataBeanX getUser_data() {
                return user_data;
            }

            public void setUser_data(UserDataBeanX user_data) {
                this.user_data = user_data;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public static class ParentDataBean {
                /**
                 * avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg
                 * id : 263
                 * name : 霸王被坑
                 */

                private String avatar;
                private String id;
                private String name;

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
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
            }

            public static class UserDataBeanX {
                /**
                 * avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg
                 * id : 263
                 * name : 霸王被坑
                 */

                private String avatar;
                private String id;
                private String name;

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
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
            }
        }
    }
}

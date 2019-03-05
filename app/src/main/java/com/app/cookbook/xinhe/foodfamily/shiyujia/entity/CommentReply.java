package com.app.cookbook.xinhe.foodfamily.shiyujia.entity;

import java.util.List;

public class CommentReply {

    /**
     * current_page : 1
     * data : [{"id":2,"content":"第N次评论","user_id":170,"level":2,"thumbs":0,"created_at":1538969319,"type":1,"total_id":8,"parent_uid":170,"parent_id":1,"is_thumbs":2,"is_existence":2,"user_data":{"id":170,"name":"null","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png"},"parent_data":{"id":170,"name":"null","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png"}},{"id":3,"content":"第N次评论","user_id":170,"level":2,"thumbs":0,"created_at":1538969342,"type":1,"total_id":9,"parent_uid":170,"parent_id":2,"is_thumbs":2,"is_existence":2,"user_data":{"id":170,"name":"null","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png"},"parent_data":{"id":170,"name":"null","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png"}}]
     * first_page_url : http://syj.cn/answerApi/imageText/getImageTextCommentList?page=1
     * from : 1
     * last_page : 1
     * last_page_url : http://syj.cn/answerApi/imageText/getImageTextCommentList?page=1
     * next_page_url : null
     * path : http://syj.cn/answerApi/imageText/getImageTextCommentList
     * per_page : 10
     * prev_page_url : null
     * to : 2
     * total : 2
     * original_data : {"id":1,"content":"第N次评论","user_id":170,"level":1,"thumbs":0,"created_at":1538969211,"type":1,"total_id":7,"parent_uid":0,"parent_id":0,"user_data":{"id":170,"name":"null","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png"}}
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
    private OriginalDataBean original_data;
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

    public OriginalDataBean getOriginal_data() {
        return original_data;
    }

    public void setOriginal_data(OriginalDataBean original_data) {
        this.original_data = original_data;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class OriginalDataBean {
        /**
         * id : 1
         * content : 第N次评论
         * user_id : 170
         * level : 1
         * thumbs : 0
         * created_at : 1538969211
         * type : 1
         * total_id : 7
         * parent_uid : 0
         * parent_id : 0
         * user_data : {"id":170,"name":"null","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png"}
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
        private UserDataBean user_data;
        private String is_thumbs;

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

        public UserDataBean getUser_data() {
            return user_data;
        }

        public void setUser_data(UserDataBean user_data) {
            this.user_data = user_data;
        }

        public String getIs_thumbs() {
            return is_thumbs;
        }

        public void setIs_thumbs(String is_thumbs) {
            this.is_thumbs = is_thumbs;
        }

        public static class UserDataBean {
            /**
             * id : 170
             * name : null
             * avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png
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

    public static class DataBean {
        /**
         * id : 2
         * content : 第N次评论
         * user_id : 170
         * level : 2
         * thumbs : 0
         * created_at : 1538969319
         * type : 1
         * total_id : 8
         * parent_uid : 170
         * parent_id : 1
         * is_thumbs : 2
         * is_existence : 2
         * user_data : {"id":170,"name":"null","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png"}
         * parent_data : {"id":170,"name":"null","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png"}
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
        private String is_thumbs;
        private String is_existence;
        private UserDataBeanX user_data;
        private ParentDataBean parent_data;

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

        public String getIs_thumbs() {
            return is_thumbs;
        }

        public void setIs_thumbs(String is_thumbs) {
            this.is_thumbs = is_thumbs;
        }

        public String getIs_existence() {
            return is_existence;
        }

        public void setIs_existence(String is_existence) {
            this.is_existence = is_existence;
        }

        public UserDataBeanX getUser_data() {
            return user_data;
        }

        public void setUser_data(UserDataBeanX user_data) {
            this.user_data = user_data;
        }

        public ParentDataBean getParent_data() {
            return parent_data;
        }

        public void setParent_data(ParentDataBean parent_data) {
            this.parent_data = parent_data;
        }

        public static class UserDataBeanX {
            /**
             * id : 170
             * name : null
             * avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png
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

        public static class ParentDataBean {
            /**
             * id : 170
             * name : null
             * avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png
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

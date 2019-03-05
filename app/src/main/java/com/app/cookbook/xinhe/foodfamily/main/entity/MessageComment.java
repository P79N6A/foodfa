package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

/**
 * Created by 18030150 on 2018/3/29.
 */

public class MessageComment {


    /**
     * current_page : 1
     * data : [{"id":4706,"answer_id":0,"question_id":0,"comment_id":0,"comment_content":"回复12121","level":1,"created_at":1541036959,"uid":14,"video_image_comment_id":21,"status":2,"video_image_id":4,"user_data":{"id":14,"name":"不想做好咖啡的厨娘只是个烘焙师","avatar":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151702343540814671.jpg"},"impriting_comment_data":{"id":21,"content":"这是个视频评论02","is_existence":1},"comment_is_existence":null},{"id":4705,"answer_id":0,"question_id":0,"comment_id":0,"comment_content":"回复111111","level":1,"created_at":1541036637,"uid":14,"video_image_comment_id":19,"status":2,"video_image_id":4,"user_data":{"id":14,"name":"不想做好咖啡的厨娘只是个烘焙师","avatar":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151702343540814671.jpg"},"impriting_comment_data":{"id":19,"content":"二级评论咯","is_existence":1},"comment_is_existence":null},{"id":4703,"answer_id":0,"question_id":0,"comment_id":0,"comment_content":"回复111","level":1,"created_at":1540978202,"uid":6,"video_image_comment_id":22,"status":2,"video_image_id":3,"user_data":{"id":6,"name":"一只树","avatar":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151702158825276771.jpg"},"impriting_comment_data":{"id":22,"content":"这是个视频评论02-01","is_existence":1},"comment_is_existence":null},{"id":4675,"answer_id":0,"question_id":0,"comment_id":0,"comment_content":"这是一个视频的评论喔","level":1,"created_at":1539844482,"uid":248,"video_image_comment_id":6,"status":3,"video_image_id":3,"user_data":{"id":248,"name":"9356_ocf6","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/1A7E7AF5-376E-4B6D-B910-CD6AD55BEA42.png"},"impriting_comment_data":{"id":6,"content":"第N次评论","is_existence":1},"comment_is_existence":null},{"id":4673,"answer_id":0,"question_id":0,"comment_id":0,"comment_content":"啦啦啦啦","level":1,"created_at":1539827809,"uid":263,"video_image_comment_id":36,"status":3,"video_image_id":7,"user_data":{"id":263,"name":"霸王被坑","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg"},"impriting_comment_data":null,"comment_is_existence":null},{"id":4674,"answer_id":0,"question_id":0,"comment_id":0,"comment_content":"啦啦啦啦","level":1,"created_at":1539827809,"uid":263,"video_image_comment_id":37,"status":3,"video_image_id":7,"user_data":{"id":263,"name":"霸王被坑","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg"},"impriting_comment_data":null,"comment_is_existence":null},{"id":4671,"answer_id":0,"question_id":0,"comment_id":0,"comment_content":"啦啦啦啦","level":1,"created_at":1539827808,"uid":263,"video_image_comment_id":35,"status":3,"video_image_id":7,"user_data":{"id":263,"name":"霸王被坑","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg"},"impriting_comment_data":null,"comment_is_existence":null},{"id":4672,"answer_id":0,"question_id":0,"comment_id":0,"comment_content":"啦啦啦啦","level":1,"created_at":1539827808,"uid":263,"video_image_comment_id":34,"status":3,"video_image_id":7,"user_data":{"id":263,"name":"霸王被坑","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg"},"impriting_comment_data":null,"comment_is_existence":null},{"id":4669,"answer_id":0,"question_id":0,"comment_id":0,"comment_content":"啦啦啦啦","level":1,"created_at":1539827805,"uid":263,"video_image_comment_id":32,"status":3,"video_image_id":7,"user_data":{"id":263,"name":"霸王被坑","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg"},"impriting_comment_data":null,"comment_is_existence":null},{"id":4670,"answer_id":0,"question_id":0,"comment_id":0,"comment_content":"啦啦啦啦","level":1,"created_at":1539827805,"uid":263,"video_image_comment_id":33,"status":3,"video_image_id":7,"user_data":{"id":263,"name":"霸王被坑","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e58b5544dfc643fe962511a2525c5b8f.jpg"},"impriting_comment_data":null,"comment_is_existence":null}]
     * first_page_url : http://syj.cn/answerApi/message/commentListV5?page=1
     * from : 1
     * last_page : 2
     * last_page_url : http://syj.cn/answerApi/message/commentListV5?page=2
     * next_page_url : http://syj.cn/answerApi/message/commentListV5?page=2
     * path : http://syj.cn/answerApi/message/commentListV5
     * per_page : 10
     * prev_page_url : null
     * to : 10
     * total : 19
     */

    private String current_page;
    private String first_page_url;
    private String from;
    private String last_page;
    private String last_page_url;
    private String next_page_url;
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
         * id : 4706
         * answer_id : 0
         * question_id : 0
         * comment_id : 0
         * comment_content : 回复12121
         * level : 1
         * created_at : 1541036959
         * uid : 14
         * video_image_comment_id : 21
         * status : 2
         * video_image_id : 4
         * user_data : {"id":14,"name":"不想做好咖啡的厨娘只是个烘焙师","avatar":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151702343540814671.jpg"}
         * impriting_comment_data : {"id":21,"content":"这是个视频评论02","is_existence":1}
         * comment_is_existence : null
         */

        private String id;
        private String answer_id;
        private String question_id;
        private String comment_id;
        private String comment_content;
        private String level;
        private String created_at;
        private String uid;
        private String video_image_comment_id;
        private String status;
        private String video_image_id;
        private UserDataBean user_data;
        private ImpritingCommentDataBean impriting_comment_data;
        private CommentIsExistenceBean comment_is_existence;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAnswer_id() {
            return answer_id;
        }

        public void setAnswer_id(String answer_id) {
            this.answer_id = answer_id;
        }

        public String getQuestion_id() {
            return question_id;
        }

        public void setQuestion_id(String question_id) {
            this.question_id = question_id;
        }

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }

        public String getComment_content() {
            return comment_content;
        }

        public void setComment_content(String comment_content) {
            this.comment_content = comment_content;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getVideo_image_comment_id() {
            return video_image_comment_id;
        }

        public void setVideo_image_comment_id(String video_image_comment_id) {
            this.video_image_comment_id = video_image_comment_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getVideo_image_id() {
            return video_image_id;
        }

        public void setVideo_image_id(String video_image_id) {
            this.video_image_id = video_image_id;
        }

        public UserDataBean getUser_data() {
            return user_data;
        }

        public void setUser_data(UserDataBean user_data) {
            this.user_data = user_data;
        }

        public ImpritingCommentDataBean getImpriting_comment_data() {
            return impriting_comment_data;
        }

        public void setImpriting_comment_data(ImpritingCommentDataBean impriting_comment_data) {
            this.impriting_comment_data = impriting_comment_data;
        }

        public CommentIsExistenceBean getComment_is_existence() {
            return comment_is_existence;
        }

        public void setComment_is_existence(CommentIsExistenceBean comment_is_existence) {
            this.comment_is_existence = comment_is_existence;
        }

        public static class CommentIsExistenceBean {
            private String comment_is_existence;

            public String getComment_is_existence() {
                return comment_is_existence;
            }

            public void setComment_is_existence(String comment_is_existence) {
                this.comment_is_existence = comment_is_existence;
            }
        }

        public static class UserDataBean {
            /**
             * id : 14
             * name : 不想做好咖啡的厨娘只是个烘焙师
             * avatar : http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151702343540814671.jpg
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

        public static class ImpritingCommentDataBean {
            /**
             * id : 21
             * content : 这是个视频评论02
             * is_existence : 1
             */

            private String id;
            private String content;
            private String is_existence;
            private String table_id;

            public String getTable_id() {
                return table_id;
            }

            public void setTable_id(String table_id) {
                this.table_id = table_id;
            }

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

            public String getIs_existence() {
                return is_existence;
            }

            public void setIs_existence(String is_existence) {
                this.is_existence = is_existence;
            }
        }
    }
}

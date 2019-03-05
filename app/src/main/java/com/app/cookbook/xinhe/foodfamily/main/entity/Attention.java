package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

/**
 * Created by 18030150 on 2018/5/29.
 */

public class Attention {

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
    private List<AttentionBean> data;


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

    public List<AttentionBean> getData() {
        return data;
    }

    public void setData(List<AttentionBean> data) {
        this.data = data;
    }

    public static class AttentionBean {


        /**
         * answer_data : {"comment_count":0,"content":"<p><span >我是***。我喜欢***。我不是****。我喜欢**all。<\/span><\/p><p><span >我是***。我喜欢***。我不是****。我喜欢**all。<\/span><\/p>","content_remove":"我是***。我喜欢***。我不是****。我喜欢**all。我是***。我喜欢***。我不是****。我喜欢**all。","created_at":1527474200,"id":2404,"name":"徐小小家的小女孩","thumbs":12,"uid":255,"users_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/91139CA8-800B-42C0-9B07-69CD0B477BF8.png"}
         * is_from : 3
         * question_content : <p><span >我是***。我喜欢***。我不是****。我喜欢**教徒</span></p>
         * question_content_remove : 我是***。我喜欢***。我不是****。我喜欢**教徒
         * question_id : 2201
         * question_title : 我是***。我喜欢***。我不是****。我喜欢**all。
         * user_avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/91139CA8-800B-42C0-9B07-69CD0B477BF8.png
         * user_id : 255
         * user_name : 徐小小家的小女孩
         */

        private AnswerDataBean answer_data;
        private String is_from;
        private String question_content;
        private String question_content_remove;
        private String question_id;
        private String question_title;
        private String user_avatar;
        private String user_id;
        private String user_name;

        public AnswerDataBean getAnswer_data() {
            return answer_data;
        }

        public void setAnswer_data(AnswerDataBean answer_data) {
            this.answer_data = answer_data;
        }

        public String getIs_from() {
            return is_from;
        }

        public void setIs_from(String is_from) {
            this.is_from = is_from;
        }

        public String getQuestion_content() {
            return question_content;
        }

        public void setQuestion_content(String question_content) {
            this.question_content = question_content;
        }

        public String getQuestion_content_remove() {
            return question_content_remove;
        }

        public void setQuestion_content_remove(String question_content_remove) {
            this.question_content_remove = question_content_remove;
        }

        public String getQuestion_id() {
            return question_id;
        }

        public void setQuestion_id(String question_id) {
            this.question_id = question_id;
        }

        public String getQuestion_title() {
            return question_title;
        }

        public void setQuestion_title(String question_title) {
            this.question_title = question_title;
        }

        public String getUser_avatar() {
            return user_avatar;
        }

        public void setUser_avatar(String user_avatar) {
            this.user_avatar = user_avatar;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public static class AnswerDataBean {
            /**
             * comment_count : 0
             * content : <p><span >我是***。我喜欢***。我不是****。我喜欢**all。</span></p><p><span >我是***。我喜欢***。我不是****。我喜欢**all。</span></p>
             * content_remove : 我是***。我喜欢***。我不是****。我喜欢**all。我是***。我喜欢***。我不是****。我喜欢**all。
             * created_at : 1527474200
             * id : 2404
             * name : 徐小小家的小女孩
             * thumbs : 12
             * uid : 255
             * users_avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/91139CA8-800B-42C0-9B07-69CD0B477BF8.png
             */

            private String comment_count;
            private String content;
            private String content_remove;
            private String created_at;
            private String id;
            private imgDataBean img_data;
            private String name;
            private String thumbs;
            private String uid;
            private String users_avatar;

            public imgDataBean getImg_data() {
                return img_data;
            }

            public void setImg_data(imgDataBean img_data) {
                this.img_data = img_data;
            }

            public String getComment_count() {
                return comment_count;
            }

            public void setComment_count(String comment_count) {
                this.comment_count = comment_count;
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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getThumbs() {
                return thumbs;
            }

            public void setThumbs(String thumbs) {
                this.thumbs = thumbs;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getUsers_avatar() {
                return users_avatar;
            }

            public void setUsers_avatar(String users_avatar) {
                this.users_avatar = users_avatar;
            }

            public static class imgDataBean {
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

}

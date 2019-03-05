package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

/**
 * Created by shiyujia02 on 2018/3/28.
 */

public class MineGuanZhuQuestionEntity {


    private int current_page;
    private String first_page_url;
    private int from;
    private int last_page;
    private String last_page_url;
    private String next_page_url;
    private String path;
    private String per_page;
    private int to;
    private int total;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int id;
        private String title;
        private String content;
        private int countUsers;
        private AnswerDataBean answer_data;

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


        public int getCountUsers() {
            return countUsers;
        }

        public void setCountUsers(int countUsers) {
            this.countUsers = countUsers;
        }

        public AnswerDataBean getAnswer_data() {
            return answer_data;
        }

        public void setAnswer_data(AnswerDataBean answer_data) {
            this.answer_data = answer_data;
        }

        public static class AnswerBean {
            /**
             * id : 1493
             * content : <p>特殊教育就</p>
             * pid : 1570
             * created_at : 1522205289
             * uid : 259
             * content_remove : 特殊教育就
             * img_data : [{"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/152118731441578583.jpg"}]
             * users : {"uname":"hxbxnxnxnxn","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e55216a6aad64b28b56b77f227983c64.jpg"}
             */

            private int id;
            private String content;
            private int pid;
            private int created_at;
            private int uid;
            private String content_remove;
            private UsersBean users;
            private List<ImgDataBean> img_data;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public int getCreated_at() {
                return created_at;
            }

            public void setCreated_at(int created_at) {
                this.created_at = created_at;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getContent_remove() {
                return content_remove;
            }

            public void setContent_remove(String content_remove) {
                this.content_remove = content_remove;
            }

            public UsersBean getUsers() {
                return users;
            }

            public void setUsers(UsersBean users) {
                this.users = users;
            }

            public List<ImgDataBean> getImg_data() {
                return img_data;
            }

            public void setImg_data(List<ImgDataBean> img_data) {
                this.img_data = img_data;
            }

            public static class UsersBean {
                /**
                 * uname : hxbxnxnxnxn
                 * avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e55216a6aad64b28b56b77f227983c64.jpg
                 */

                private String uname;
                private String avatar;

                public String getUname() {
                    return uname;
                }

                public void setUname(String uname) {
                    this.uname = uname;
                }

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }
            }

            public static class ImgDataBean {
                /**
                 * path : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/152118731441578583.jpg
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

        public static class AnswerDataBean {
            /**
             * id : 1493
             * content : <p>特殊教育就</p>
             * pid : 1570
             * created_at : 1522205289
             * uid : 259
             * content_remove : 特殊教育就
             * img_data : [{"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/152118731441578583.jpg"}]
             * users : {"uname":"hxbxnxnxnxn","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e55216a6aad64b28b56b77f227983c64.jpg"}
             */

            private int id;
            private String content;
            private int pid;
            private int created_at;
            private int uid;
            private String content_remove;
            private UsersBeanX users;
            private List<ImageDate> img_data;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public int getCreated_at() {
                return created_at;
            }

            public void setCreated_at(int created_at) {
                this.created_at = created_at;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getContent_remove() {
                return content_remove;
            }

            public void setContent_remove(String content_remove) {
                this.content_remove = content_remove;
            }

            public UsersBeanX getUsers() {
                return users;
            }

            public void setUsers(UsersBeanX users) {
                this.users = users;
            }

            public List<ImageDate> getImg_data() {
                return img_data;
            }

            public void setImg_data(List<ImageDate> img_data) {
                this.img_data = img_data;
            }

            public static class UsersBeanX {
                /**
                 * uname : hxbxnxnxnxn
                 * avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/e55216a6aad64b28b56b77f227983c64.jpg
                 */

                private String uname;
                private String avatar;

                public String getUname() {
                    return uname;
                }

                public void setUname(String uname) {
                    this.uname = uname;
                }

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }
            }

            public static class ImgDataBeanX {
                /**
                 * path : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/152118731441578583.jpg
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

}

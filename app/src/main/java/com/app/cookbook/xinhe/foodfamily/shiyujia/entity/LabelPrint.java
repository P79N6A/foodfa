package com.app.cookbook.xinhe.foodfamily.shiyujia.entity;

import java.util.List;

public class LabelPrint {

    /**
     * current_page : 1
     * data : [{"id":4,"status":2,"coment_number":5,"thumb_number":0,"collection_number":0,"user_id":170,"content":"比好","user_data":{"id":170,"name":"null","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png"},"image_data":[{"id":3,"total_id":4,"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/answer/2018_09/2415ae227cc7421980943ba6855a06b1.jpg","size":"1:1"}],"video_data":null,"is_collect":2,"is_thumbs":2,"is_update":2,"is_existence":2}]
     * first_page_url : http://syj.cn/answerApi/find/getClassImprinting?page=1
     * from : 1
     * last_page : 1
     * last_page_url : http://syj.cn/answerApi/find/getClassImprinting?page=1
     * next_page_url : null
     * path : http://syj.cn/answerApi/find/getClassImprinting
     * per_page : 15
     * prev_page_url : null
     * to : 1
     * total : 1
     * recommend_data : [{"id":4,"status":2,"coment_number":5,"thumb_number":0,"collection_number":0,"user_id":170,"content":"比好","user_data":{"id":170,"name":"null","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png"},"image_data":[{"id":3,"total_id":4,"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/answer/2018_09/2415ae227cc7421980943ba6855a06b1.jpg","size":"1:1"}],"video_data":null,"is_collect":2,"is_thumbs":2,"is_update":2,"is_existence":2},{"id":3,"status":1,"coment_number":4,"thumb_number":1,"collection_number":1,"user_id":170,"content":"野生动物园","user_data":{"id":170,"name":"null","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png"},"image_data":null,"video_data":{"id":2,"total_id":3,"video_path":"http://out-20170513094521332-xgmqlm9wuz.oss-cn-shanghai.aliyuncs.com/customerTrans/875d3a6a249c386af6fe717407252318/130b0753-1664d8b8f32-0000-0000-013-e3213.wmv","video_id":"b1ade772d727402ea6f2dd55811a941f","is_transcoding":2,"size":26246026,"face_path":"http://vod.cicas-edu.com/8d254501c5414099a8498a145cad9c67/snapshots/normal/2BE9B2E0-1664D862B02-0000-0000-208-5326700002.jpg?auth_key=1538902538-0-0-80b83ae58d0e6113b32238937724d133","transcoding_data":[{"transcoding_path":"http://vod.cicas-edu.com/b1ade772d727402ea6f2dd55811a941f/2797f128719147c7b41662382018c28d-253ef75a9f21048cf24b9ba45815c770-fd.m3u8","transcoding_type":"m3u8","video_id":"b1ade772d727402ea6f2dd55811a941f","Definition":"FD","Duration":30,"Height":"99","Width":"99"}]},"is_collect":2,"is_thumbs":2,"is_update":2,"is_existence":2}]
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
    private List<RecommendDataBean> recommend_data;

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

    public List<RecommendDataBean> getRecommend_data() {
        return recommend_data;
    }

    public void setRecommend_data(List<RecommendDataBean> recommend_data) {
        this.recommend_data = recommend_data;
    }

    public static class DataBean {
        /**
         * collection_number : 0
         * coment_number : 0
         * content : 123
         * id : 13
         * image_data : [{"id":6,"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/answer/2018_09/2415ae227cc7421980943ba6855a06b1.jpg","size":"1:1","total_id":13}]
         * is_collect : 2
         * is_existence : 2
         * is_thumbs : 2
         * is_update : 2
         * status : 2
         * thumb_number : 0
         * user_data : {"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/91139CA8-800B-42C0-9B07-69CD0B477BF8.png","id":255,"name":"徐小小家的小女孩"}
         * user_id : 255
         * video_data : {"id":10,"is_transcoding":2,"size":26246026,"total_id":7,"transcoding_data":[{"Definition":"FD","Duration":30,"Height":"200","Width":"500","transcoding_path":"http://vod.cicas-edu.com/5e390456a0204c628d560ce39bc0d8e6/e5ac9e05c8464896b8d9cc7b792f556d-4e77eb2dd59041c925dc18f0d0fcfeb3-fd.m3u8?auth_key=1538981723-5169365f814e430da0673276cc4a8688-0-13c5a602961fc5d621dfd12aa25bd1a7","transcoding_type":"m3u8","video_id":"5e390456a0204c628d560ce39bc0d8e6"}],"video_id":"5e390456a0204c628d560ce39bc0d8e6","video_path":"http://out-20170513094521332-xgmqlm9wuz.oss-cn-shanghai.aliyuncs.com/customerTrans/875d3a6a249c386af6fe717407252318/106bf93b-16652752c9f-0000-0000-013-e3213.wmv"}
         */

        private String collection_number;
        private String coment_number;
        private String content;
        private String is_recommend_class;
        private String id;
        private String is_collect;
        private String is_existence;
        private String is_thumbs;
        private String is_update;
        private int status;
        private String thumb_number;
        private HomeItem.DataBean.UserDataBean user_data;
        private String user_id;
        private HomeItem.DataBean.VideoDataBean video_data;
        private List<HomeItem.DataBean.ImageDataBean> image_data;
        private List<String> imageList;

        public String getIs_recommend_class() {
            return is_recommend_class;
        }

        public void setIs_recommend_class(String is_recommend_class) {
            this.is_recommend_class = is_recommend_class;
        }

        public String getCollection_number() {
            return collection_number;
        }

        public void setCollection_number(String collection_number) {
            this.collection_number = collection_number;
        }

        public String getComent_number() {
            return coment_number;
        }

        public void setComent_number(String coment_number) {
            this.coment_number = coment_number;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIs_collect() {
            return is_collect;
        }

        public void setIs_collect(String is_collect) {
            this.is_collect = is_collect;
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

        public String getIs_update() {
            return is_update;
        }

        public void setIs_update(String is_update) {
            this.is_update = is_update;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getThumb_number() {
            return thumb_number;
        }

        public void setThumb_number(String thumb_number) {
            this.thumb_number = thumb_number;
        }

        public HomeItem.DataBean.UserDataBean getUser_data() {
            return user_data;
        }

        public void setUser_data(HomeItem.DataBean.UserDataBean user_data) {
            this.user_data = user_data;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public HomeItem.DataBean.VideoDataBean getVideo_data() {
            return video_data;
        }

        public void setVideo_data(HomeItem.DataBean.VideoDataBean video_data) {
            this.video_data = video_data;
        }

        public List<HomeItem.DataBean.ImageDataBean> getImage_data() {
            return image_data;
        }

        public void setImage_data(List<HomeItem.DataBean.ImageDataBean> image_data) {
            this.image_data = image_data;
        }

        public List<String> getImageList() {
            return imageList;
        }

        public void setImageList(List<String> imageList) {
            this.imageList = imageList;
        }

        public static class UserDataBean {
            /**
             * avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/91139CA8-800B-42C0-9B07-69CD0B477BF8.png
             * id : 255
             * name : 徐小小家的小女孩
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

        public static class VideoDataBean {
            /**
             * id : 10
             * is_transcoding : 2
             * size : 26246026
             * total_id : 7
             * transcoding_data : [{"Definition":"FD","Duration":30,"Height":"200","Width":"500","transcoding_path":"http://vod.cicas-edu.com/5e390456a0204c628d560ce39bc0d8e6/e5ac9e05c8464896b8d9cc7b792f556d-4e77eb2dd59041c925dc18f0d0fcfeb3-fd.m3u8?auth_key=1538981723-5169365f814e430da0673276cc4a8688-0-13c5a602961fc5d621dfd12aa25bd1a7","transcoding_type":"m3u8","video_id":"5e390456a0204c628d560ce39bc0d8e6"}]
             * video_id : 5e390456a0204c628d560ce39bc0d8e6
             * video_path : http://out-20170513094521332-xgmqlm9wuz.oss-cn-shanghai.aliyuncs.com/customerTrans/875d3a6a249c386af6fe717407252318/106bf93b-16652752c9f-0000-0000-013-e3213.wmv
             */

            private String id;
            private String is_transcoding;
            private String size;
            private String face_path;
            private String total_id;
            private String video_id;
            private String video_path;
            private List<HomeItem.DataBean.VideoDataBean.TranscodingDataBean> transcoding_data;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getIs_transcoding() {
                return is_transcoding;
            }

            public void setIs_transcoding(String is_transcoding) {
                this.is_transcoding = is_transcoding;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public void setFace_path(String face_path) {
                this.face_path = face_path;
            }

            public String getFace_path() {
                return face_path;
            }

            public String getTotal_id() {
                return total_id;
            }

            public void setTotal_id(String total_id) {
                this.total_id = total_id;
            }

            public String getVideo_id() {
                return video_id;
            }

            public void setVideo_id(String video_id) {
                this.video_id = video_id;
            }

            public String getVideo_path() {
                return video_path;
            }

            public void setVideo_path(String video_path) {
                this.video_path = video_path;
            }

            public List<HomeItem.DataBean.VideoDataBean.TranscodingDataBean> getTranscoding_data() {
                return transcoding_data;
            }

            public void setTranscoding_data(List<HomeItem.DataBean.VideoDataBean.TranscodingDataBean> transcoding_data) {
                this.transcoding_data = transcoding_data;
            }

            public static class TranscodingDataBean {
                /**
                 * Definition : FD
                 * Duration : 30
                 * Height : 200
                 * Width : 500
                 * transcoding_path : http://vod.cicas-edu.com/5e390456a0204c628d560ce39bc0d8e6/e5ac9e05c8464896b8d9cc7b792f556d-4e77eb2dd59041c925dc18f0d0fcfeb3-fd.m3u8?auth_key=1538981723-5169365f814e430da0673276cc4a8688-0-13c5a602961fc5d621dfd12aa25bd1a7
                 * transcoding_type : m3u8
                 * video_id : 5e390456a0204c628d560ce39bc0d8e6
                 */

                private String Definition;
                private String Duration;
                private String Height;
                private String Width;
                private String transcoding_path;
                private String transcoding_type;
                private String video_id;

                public String getDefinition() {
                    return Definition;
                }

                public void setDefinition(String Definition) {
                    this.Definition = Definition;
                }

                public String getDuration() {
                    return Duration;
                }

                public void setDuration(String Duration) {
                    this.Duration = Duration;
                }

                public String getHeight() {
                    return Height;
                }

                public void setHeight(String Height) {
                    this.Height = Height;
                }

                public String getWidth() {
                    return Width;
                }

                public void setWidth(String Width) {
                    this.Width = Width;
                }

                public String getTranscoding_path() {
                    return transcoding_path;
                }

                public void setTranscoding_path(String transcoding_path) {
                    this.transcoding_path = transcoding_path;
                }

                public String getTranscoding_type() {
                    return transcoding_type;
                }

                public void setTranscoding_type(String transcoding_type) {
                    this.transcoding_type = transcoding_type;
                }

                public String getVideo_id() {
                    return video_id;
                }

                public void setVideo_id(String video_id) {
                    this.video_id = video_id;
                }
            }
        }

        public static class ImageDataBean {
            /**
             * id : 6
             * path : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/answer/2018_09/2415ae227cc7421980943ba6855a06b1.jpg
             * size : 1:1
             * total_id : 13
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
    public static class RecommendDataBean {
        /**
         * id : 4
         * status : 2
         * coment_number : 5
         * thumb_number : 0
         * collection_number : 0
         * user_id : 170
         * content : 比好
         * user_data : {"id":170,"name":"null","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png"}
         * image_data : [{"id":3,"total_id":4,"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/answer/2018_09/2415ae227cc7421980943ba6855a06b1.jpg","size":"1:1"}]
         * video_data : null
         * is_collect : 2
         * is_thumbs : 2
         * is_update : 2
         * is_existence : 2
         */

        private String id;
        private String status;
        private String coment_number;
        private String thumb_number;
        private String collection_number;
        private String user_id;
        private String content;
        private UserDataBeanX user_data;
        private Object video_data;
        private String is_collect;
        private String is_thumbs;
        private String is_update;
        private String is_existence;
        private List<ImageDataBeanX> image_data;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getComent_number() {
            return coment_number;
        }

        public void setComent_number(String coment_number) {
            this.coment_number = coment_number;
        }

        public String getThumb_number() {
            return thumb_number;
        }

        public void setThumb_number(String thumb_number) {
            this.thumb_number = thumb_number;
        }

        public String getCollection_number() {
            return collection_number;
        }

        public void setCollection_number(String collection_number) {
            this.collection_number = collection_number;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public UserDataBeanX getUser_data() {
            return user_data;
        }

        public void setUser_data(UserDataBeanX user_data) {
            this.user_data = user_data;
        }

        public Object getVideo_data() {
            return video_data;
        }

        public void setVideo_data(Object video_data) {
            this.video_data = video_data;
        }

        public String getIs_collect() {
            return is_collect;
        }

        public void setIs_collect(String is_collect) {
            this.is_collect = is_collect;
        }

        public String getIs_thumbs() {
            return is_thumbs;
        }

        public void setIs_thumbs(String is_thumbs) {
            this.is_thumbs = is_thumbs;
        }

        public String getIs_update() {
            return is_update;
        }

        public void setIs_update(String is_update) {
            this.is_update = is_update;
        }

        public String getIs_existence() {
            return is_existence;
        }

        public void setIs_existence(String is_existence) {
            this.is_existence = is_existence;
        }

        public List<ImageDataBeanX> getImage_data() {
            return image_data;
        }

        public void setImage_data(List<ImageDataBeanX> image_data) {
            this.image_data = image_data;
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

        public static class ImageDataBeanX {
            /**
             * id : 3
             * total_id : 4
             * path : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/answer/2018_09/2415ae227cc7421980943ba6855a06b1.jpg
             * size : 1:1
             */

            private String id;
            private String total_id;
            private String path;
            private String size;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTotal_id() {
                return total_id;
            }

            public void setTotal_id(String total_id) {
                this.total_id = total_id;
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
        }
    }
}

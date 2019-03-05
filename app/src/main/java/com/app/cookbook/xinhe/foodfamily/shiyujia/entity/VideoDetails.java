package com.app.cookbook.xinhe.foodfamily.shiyujia.entity;

import java.util.List;

public class VideoDetails {

    /**
     * collection_number : 0
     * coment_number : 0
     * content : 野生动物园
     * created_at : 1538981729
     * id : 7
     * is_class : 1
     * is_collect : 2
     * is_existence : 2
     * is_follow_user : 2
     * is_thumbs : 2
     * is_update : 2
     * thumb_number : 0
     * type : 1
     * user_data : {"avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png","id":170,"name":"null"}
     * user_id : 170
     * video_data : {"created_at":1538981714,"face_path":"http://vod.cicas-edu.com/8d254501c5414099a8498a145cad9c67/snapshots/normal/2BE9B2E0-1664D862B02-0000-0000-208-5326700002.jpg?auth_key=1538902538-0-0-80b83ae58d0e6113b32238937724d133","is_transcoding":2,"size":26246026,"total_id":7,"transcoding_data":[{"Definition":"FD","Duration":30,"Height":"200","Width":"500","transcoding_path":"http://vod.cicas-edu.com/5e390456a0204c628d560ce39bc0d8e6/e5ac9e05c8464896b8d9cc7b792f556d-4e77eb2dd59041c925dc18f0d0fcfeb3-fd.m3u8?auth_key=1538981723-5169365f814e430da0673276cc4a8688-0-13c5a602961fc5d621dfd12aa25bd1a7","transcoding_type":"m3u8","video_id":"5e390456a0204c628d560ce39bc0d8e6"}],"video_id":"5e390456a0204c628d560ce39bc0d8e6","video_path":"http://out-20170513094521332-xgmqlm9wuz.oss-cn-shanghai.aliyuncs.com/customerTrans/875d3a6a249c386af6fe717407252318/106bf93b-16652752c9f-0000-0000-013-e3213.wmv"}
     */

    private String collection_number;
    private String coment_number;
    private String content;
    private String created_at;
    private String id;
    private String is_class;
    private String is_collect;
    private String is_existence;
    private String is_follow_user;
    private String is_thumbs;
    private String is_update;
    private String thumb_number;
    private String type;
    private UserDataBean user_data;
    private String user_id;
    private VideoDataBean video_data;
    private List<ClassDataBean> class_data;
    private List<BannerEntity> result_recipe;

    public List<BannerEntity> getBanners() {
        return result_recipe;
    }

    public void setBanners(List<BannerEntity> banners) {
        this.result_recipe = banners;
    }
    public List<ClassDataBean> getClass_data() {
        return class_data;
    }

    public void setClass_data(List<ClassDataBean> class_data) {
        this.class_data = class_data;
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

    public String getIs_class() {
        return is_class;
    }

    public void setIs_class(String is_class) {
        this.is_class = is_class;
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

    public String getIs_follow_user() {
        return is_follow_user;
    }

    public void setIs_follow_user(String is_follow_user) {
        this.is_follow_user = is_follow_user;
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

    public String getThumb_number() {
        return thumb_number;
    }

    public void setThumb_number(String thumb_number) {
        this.thumb_number = thumb_number;
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

    public VideoDataBean getVideo_data() {
        return video_data;
    }

    public void setVideo_data(VideoDataBean video_data) {
        this.video_data = video_data;
    }

    public static class UserDataBean {
        /**
         * avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png
         * id : 170
         * name : null
         */

        private String avatar;
        private String id;
        private String name;
        private String personal_signature;

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

        public String getPersonal_signature() {
            return personal_signature;
        }

        public void setPersonal_signature(String personal_signature) {
            this.personal_signature = personal_signature;
        }
    }

    public static class VideoDataBean {
        /**
         * created_at : 1538981714
         * face_path : http://vod.cicas-edu.com/8d254501c5414099a8498a145cad9c67/snapshots/normal/2BE9B2E0-1664D862B02-0000-0000-208-5326700002.jpg?auth_key=1538902538-0-0-80b83ae58d0e6113b32238937724d133
         * is_transcoding : 2
         * size : 26246026
         * total_id : 7
         * transcoding_data : [{"Definition":"FD","Duration":30,"Height":"200","Width":"500","transcoding_path":"http://vod.cicas-edu.com/5e390456a0204c628d560ce39bc0d8e6/e5ac9e05c8464896b8d9cc7b792f556d-4e77eb2dd59041c925dc18f0d0fcfeb3-fd.m3u8?auth_key=1538981723-5169365f814e430da0673276cc4a8688-0-13c5a602961fc5d621dfd12aa25bd1a7","transcoding_type":"m3u8","video_id":"5e390456a0204c628d560ce39bc0d8e6"}]
         * video_id : 5e390456a0204c628d560ce39bc0d8e6
         * video_path : http://out-20170513094521332-xgmqlm9wuz.oss-cn-shanghai.aliyuncs.com/customerTrans/875d3a6a249c386af6fe717407252318/106bf93b-16652752c9f-0000-0000-013-e3213.wmv
         */

        private String created_at;
        private String face_path;
        private String is_transcoding;
        private String size;
        private String total_id;
        private String video_id;
        private String video_path;
        private List<TranscodingDataBean> transcoding_data;

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getFace_path() {
            return face_path;
        }

        public void setFace_path(String face_path) {
            this.face_path = face_path;
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

        public List<TranscodingDataBean> getTranscoding_data() {
            return transcoding_data;
        }

        public void setTranscoding_data(List<TranscodingDataBean> transcoding_data) {
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

    public static class ClassDataBean {
        private String id;
        private String title;
        private String path;

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
    }
}

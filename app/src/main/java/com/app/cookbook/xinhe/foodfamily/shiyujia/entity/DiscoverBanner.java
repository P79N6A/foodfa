package com.app.cookbook.xinhe.foodfamily.shiyujia.entity;

public class DiscoverBanner {

    /**
     * id : 3
     * type : 5
     * banner_path : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/30011C6B-043F-4807-B454-B9EEBE09B608.png
     * ascription_id : 177
     * release_user_id : 49
     * user_release_data : {"id":49,"name":"小甜甜有多甜","avatar":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151703482095619780.jpg","personal_signature":"爱美食爱烘焙"}
     * ascription_question_data : {"id":177,"title":"吃什么能淡化黑眼圈？"}
     */

    private String id;
    private String type;
    private String banner_path;
    private String ascription_id;
    private String release_user_id;
    private AscriptionClassDataBean ascription_class_data;
    private UserReleaseDataBean user_release_data;
    private AscriptionQuestionDataBean ascription_question_data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBanner_path() {
        return banner_path;
    }

    public void setBanner_path(String banner_path) {
        this.banner_path = banner_path;
    }

    public String getAscription_id() {
        return ascription_id;
    }

    public void setAscription_id(String ascription_id) {
        this.ascription_id = ascription_id;
    }

    public String getRelease_user_id() {
        return release_user_id;
    }

    public void setRelease_user_id(String release_user_id) {
        this.release_user_id = release_user_id;
    }

    public UserReleaseDataBean getUser_release_data() {
        return user_release_data;
    }

    public void setUser_release_data(UserReleaseDataBean user_release_data) {
        this.user_release_data = user_release_data;
    }

    public AscriptionQuestionDataBean getAscription_question_data() {
        return ascription_question_data;
    }

    public void setAscription_question_data(AscriptionQuestionDataBean ascription_question_data) {
        this.ascription_question_data = ascription_question_data;
    }

    public AscriptionClassDataBean getAscription_class_data() {
        return ascription_class_data;
    }

    public void setAscription_class_data(AscriptionClassDataBean ascription_class_data) {
        this.ascription_class_data = ascription_class_data;
    }

    public static class UserReleaseDataBean {
        /**
         * id : 49
         * name : 小甜甜有多甜
         * avatar : http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151703482095619780.jpg
         * personal_signature : 爱美食爱烘焙
         */

        private String id;
        private String name;
        private String avatar;
        private String personal_signature;

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

        public String getPersonal_signature() {
            return personal_signature;
        }

        public void setPersonal_signature(String personal_signature) {
            this.personal_signature = personal_signature;
        }
    }

    public static class AscriptionQuestionDataBean {
        /**
         * id : 177
         * title : 吃什么能淡化黑眼圈？
         */

        private String id;
        private String title;

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
    }

    public static class AscriptionClassDataBean {
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

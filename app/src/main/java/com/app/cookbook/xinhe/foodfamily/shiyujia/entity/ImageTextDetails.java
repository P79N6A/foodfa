package com.app.cookbook.xinhe.foodfamily.shiyujia.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class ImageTextDetails {


    /**
     * user_id : 170
     * content : 123
     * is_class : 1
     * coment_number : 0
     * thumb_number : 0
     * collection_number : 0
     * created_at : 1538963214
     * type : 3
     * img_data : [{"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/answer/2018_09/2415ae227cc7421980943ba6855a06b1.jpg","id":1,"size":"1:1"}]
     * is_update : 1
     * is_existence : 1
     * class_data : null
     * user_data : {"id":170,"name":"null","avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png"}
     */

    private String id;
    private String user_id;
    private String content;
    private String is_class;
    private String coment_number;
    private String thumb_number;
    private String collection_number;
    private String created_at;
    private String type;
    private String is_update;
    private String is_existence;
    private String is_thumbs;
    private String is_collect;
    private String is_follow_user;
    private List<ClassDataBean> class_data;
    private UserDataBean user_data;
    private List<ImgDataBean> img_data;
    private List<BannerEntity> result_recipe;

    public List<BannerEntity> getBanners() {
        return result_recipe;
    }

    public void setBanners(List<BannerEntity> banners) {
        this.result_recipe = banners;
    }

    public String getIs_thumbs() {
        return is_thumbs;
    }

    public void setIs_thumbs(String is_thumbs) {
        this.is_thumbs = is_thumbs;
    }

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    public String getIs_follow_user() {
        return is_follow_user;
    }

    public void setIs_follow_user(String is_follow_user) {
        this.is_follow_user = is_follow_user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIs_class() {
        return is_class;
    }

    public void setIs_class(String is_class) {
        this.is_class = is_class;
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

    public List<ClassDataBean> getClass_data() {
        return class_data;
    }

    public void setClass_data(List<ClassDataBean> class_data) {
        this.class_data = class_data;
    }

    public UserDataBean getUser_data() {
        return user_data;
    }

    public void setUser_data(UserDataBean user_data) {
        this.user_data = user_data;
    }

    public List<ImgDataBean> getImg_data() {
        return img_data;
    }

    public void setImg_data(List<ImgDataBean> img_data) {
        this.img_data = img_data;
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

    public static class ImgDataBean {
        /**
         * path : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/answer/2018_09/2415ae227cc7421980943ba6855a06b1.jpg
         * id : 1
         * size : 1:1
         */

        private String path;
        private String id;
        private String size;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }

    public static class ClassDataBean implements Parcelable {
        private String id;
        private String title;
        private String path;

        protected ClassDataBean(Parcel in) {
            id = in.readString();
            title = in.readString();
            path = in.readString();
        }

        public static final Creator<ClassDataBean> CREATOR = new Creator<ClassDataBean>() {
            @Override
            public ClassDataBean createFromParcel(Parcel in) {
                return new ClassDataBean(in);
            }

            @Override
            public ClassDataBean[] newArray(int size) {
                return new ClassDataBean[size];
            }
        };

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(title);
            dest.writeString(path);
        }


    }
}

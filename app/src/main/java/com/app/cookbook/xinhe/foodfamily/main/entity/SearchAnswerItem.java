package com.app.cookbook.xinhe.foodfamily.main.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 18030150 on 2018/3/22.
 */

public class SearchAnswerItem implements Parcelable {

    /**
     * content_remove : 就先不说为什么，先来看几个图先看动画片里的人吃完美食是什么反应，你就会觉得里面的食物特别好吃了吧？！再看看他们的吃相：要好吃成什么样才能有这种吃相啊？对不对，现实生活中哪有这样的？所以你就觉得动画漫画里的食物比较诱人啦~
     * id : 1374
     * img_data : {"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/15202402537323266.jpg"}
     * questions_id : 1378
     * questions_title : 为什么动漫里的食物常常看着比现实中的更诱人？
     * user_avatar : http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151703551182503900.png
     * user_id : 53
     * user_name : Clarence
     */

    private String content_remove;
    private String id;
    private ImgDataBean img_data;
    private String questions_id;
    private String questions_title;
    private String user_avatar;
    private String user_id;
    private String user_name;


    public SearchAnswerItem(Parcel in) {
        content_remove = in.readString();
        id = in.readString();
        questions_id = in.readString();
        questions_title = in.readString();
        user_avatar = in.readString();
        user_id = in.readString();
        user_name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(content_remove);
        parcel.writeString(id);
        parcel.writeString(questions_id);
        parcel.writeString(questions_title);
        parcel.writeString(user_avatar);
        parcel.writeString(user_id);
        parcel.writeString(user_name);

    }

    public String getContent_remove() {
        return content_remove;
    }

    public void setContent_remove(String content_remove) {
        this.content_remove = content_remove;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ImgDataBean getImg_data() {
        return img_data;
    }

    public void setImg_data(ImgDataBean img_data) {
        this.img_data = img_data;
    }

    public String getQuestions_id() {
        return questions_id;
    }

    public void setQuestions_id(String questions_id) {
        this.questions_id = questions_id;
    }

    public String getQuestions_title() {
        return questions_title;
    }

    public void setQuestions_title(String questions_title) {
        this.questions_title = questions_title;
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

    @Override
    public int describeContents() {
        return 0;
    }


    public static class ImgDataBean {
        /**
         * path : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/15202402537323266.jpg
         */

        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public static final Parcelable.Creator<SearchAnswerItem> CREATOR = new Parcelable.Creator<SearchAnswerItem>() {
        public SearchAnswerItem createFromParcel(Parcel in) {
            return new SearchAnswerItem(in);
        }

        public SearchAnswerItem[] newArray(int size) {
            return new SearchAnswerItem[size];
        }
    };
}

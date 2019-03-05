package com.app.cookbook.xinhe.foodfamily.main.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shiyujia02 on 2018/1/30.
 */

public class CategoryDateEn implements Parcelable {
    private String title;
    private String status;
    private String id;
    private String qid;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public CategoryDateEn(Parcel in) {
        title = in.readString();
        status = in.readString();
        id = in.readString();
        qid = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(status);
        parcel.writeString(id);
        parcel.writeString(qid);

    }

    public static final Parcelable.Creator<CategoryDateEn> CREATOR = new Parcelable.Creator<CategoryDateEn>() {
        public CategoryDateEn createFromParcel(Parcel in) {
            return new CategoryDateEn(in);
        }

        public CategoryDateEn[] newArray(int size) {
            return new CategoryDateEn[size];
        }
    };
}

package com.app.cookbook.xinhe.foodfamily.main.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shiyujia02 on 2018/1/29.
 */

public class UploadImageEntity implements Parcelable {
    private String iamge_path;
    private String upload_iamge_path;

    public UploadImageEntity(Parcel in) {
        iamge_path = in.readString();
        upload_iamge_path = in.readString();

    }
    public UploadImageEntity() {

    }
    public String getIamge_path() {
        return iamge_path;
    }

    public void setIamge_path(String iamge_path) {
        this.iamge_path = iamge_path;
    }

    public String getUpload_iamge_path() {
        return upload_iamge_path;
    }

    public void setUpload_iamge_path(String upload_iamge_path) {
        this.upload_iamge_path = upload_iamge_path;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(iamge_path);
        dest.writeString(upload_iamge_path);
    }
    public static final Parcelable.Creator<UploadImageEntity> CREATOR = new Parcelable.Creator<UploadImageEntity>() {
        public UploadImageEntity createFromParcel(Parcel in) {
            return new UploadImageEntity(in);
        }

        public UploadImageEntity[] newArray(int size) {
            return new UploadImageEntity[size];
        }
    };
}

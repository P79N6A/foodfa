package com.app.cookbook.xinhe.foodfamily.main.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shiyujia02 on 2018/1/24.
 */

public class ImageDate implements Parcelable {
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

//    public String getPid() {
//        return pid;
//    }
//
//    public void setPid(String pid) {
//        this.pid = pid;
//    }
//
//    private String pid;

    public ImageDate(Parcel in) {
        path = in.readString();
     //  pid = in.readString();
    }
    public ImageDate() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(path);
       // parcel.writeString(pid);
    }
    public static final Parcelable.Creator<ImageDate> CREATOR = new Parcelable.Creator<ImageDate>() {
        public ImageDate createFromParcel(Parcel in) {
            return new ImageDate(in);
        }

        public ImageDate[] newArray(int size) {
            return new ImageDate[size];
        }
    };
}

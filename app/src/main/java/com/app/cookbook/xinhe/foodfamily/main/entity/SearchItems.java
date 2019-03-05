package com.app.cookbook.xinhe.foodfamily.main.entity;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 18030150 on 2018/3/21.
 */

public class SearchItems implements Parcelable {
    private String question_id;
    private String question_title;
    private String question_content;
    private String question_content_remove;
    private AnswerBean answer;
    private String answer_count;
    private String is_follow;
    private String is_follow_text;

    public SearchItems(Parcel in) {
        question_id = in.readString();
        question_title = in.readString();
        question_content_remove = in.readString();
        is_follow = in.readString();
        is_follow_text = in.readString();
        question_content = in.readString();
        answer_count = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(question_id);
        parcel.writeString(question_title);
        parcel.writeString(question_content);
        parcel.writeString(question_content_remove);
        parcel.writeString(answer_count);
        parcel.writeString(is_follow);
        parcel.writeString(is_follow_text);

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

    public AnswerBean getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerBean answer) {
        this.answer = answer;
    }

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }

    public String getIs_follow_text() {
        return is_follow_text;
    }

    public void setIs_follow_text(String is_follow_text) {
        this.is_follow_text = is_follow_text;
    }

    public String getAnswer_count() {
        return answer_count;
    }

    public void setAnswer_count(String answer_count) {
        this.answer_count = answer_count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<SearchItems> CREATOR = new Parcelable.Creator<SearchItems>() {
        public SearchItems createFromParcel(Parcel in) {
            return new SearchItems(in);
        }

        public SearchItems[] newArray(int size) {
            return new SearchItems[size];
        }
    };

}


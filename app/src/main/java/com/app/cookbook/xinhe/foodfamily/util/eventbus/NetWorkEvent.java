package com.app.cookbook.xinhe.foodfamily.util.eventbus;

public class NetWorkEvent {
    private String string;
    public  NetWorkEvent(String str){
        this.string = str;
    }

    public String getMessage() {
        return string;
    }

    public void setMessage(String String) {
        this.string = String;
    }
}

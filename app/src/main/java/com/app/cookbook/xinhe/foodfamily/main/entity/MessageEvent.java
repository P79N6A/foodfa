package com.app.cookbook.xinhe.foodfamily.main.entity;

/**
 * Created by 18030150 on 2018/3/24.
 */

public class MessageEvent {
    private String message;

    public MessageEvent(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}

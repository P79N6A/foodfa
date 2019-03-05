package com.app.cookbook.xinhe.foodfamily.main.entity;

/**
 * Created by 18030150 on 2018/3/25.
 */

public class VersionMsgName {
    private String version;
    private String content;
    private String url="";
    private String is_force;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIs_force() {
        return is_force;
    }

    public void setIs_force(String is_force) {
        this.is_force = is_force;
    }
}

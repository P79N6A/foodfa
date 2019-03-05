package com.app.cookbook.xinhe.foodfamily.main.entity;

public class ImagePathEntity {
    private String local_path;

    public String getLocal_path() {
        return local_path;
    }

    public void setLocal_path(String local_path) {
        this.local_path = local_path;
    }

    public String getNet_path() {
        return net_path;
    }

    public void setNet_path(String net_path) {
        this.net_path = net_path;
    }

    private String net_path;

    public String getBili() {
        return bili;
    }

    public void setBili(String bili) {
        this.bili = bili;
    }

    private String bili;
}

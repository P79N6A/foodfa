package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

/**
 * Created by 18030150 on 2018/3/22.
 */

public class SearchUser {

    private List<SearchUserItem> data;
    private String total;
    private List<String> participle;

    public List<SearchUserItem> getData() {
        return data;
    }

    public void setData(List<SearchUserItem> data) {
        this.data = data;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<String> getParticiple() {
        return participle;
    }

    public void setParticiple(List<String> participle) {
        this.participle = participle;
    }
}

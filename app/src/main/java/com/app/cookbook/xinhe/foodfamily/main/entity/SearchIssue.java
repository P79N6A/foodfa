package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

/**
 * Created by 18030150 on 2018/3/20.
 */

public class SearchIssue {
    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    private String total;
    private List<SearchItems> data;
    private List<String> participle;

    public List<SearchItems> getData() {
        return data;
    }

    public void setData(List<SearchItems> data) {
        this.data = data;
    }

    public List<String> getParticiple() {
        return participle;
    }

    public void setParticiple(List<String> participle) {
        this.participle = participle;
    }
}

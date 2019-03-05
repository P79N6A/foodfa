package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

/**
 * Created by 18030150 on 2018/3/22.
 */

public class SearchAnswerData {

    private List<SearchAnswerItem> data;
    private List<String> participle;
    private String total;

    public List<SearchAnswerItem> getData() {
        return data;
    }

    public void setData(List<SearchAnswerItem> data) {
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

package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

/**
 * Created by 18030150 on 2018/5/8.
 */

public class UseLabel {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * title : dxjxndn
         * class_id : 321
         * path : null
         */

        private String title;
        private String id;
        private String path;
        private boolean isSelect;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }
    }
}

package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

/**
 * Created by 18030150 on 2018/5/8.
 */

public class HotLabel {


    /**
     * code : 1
     * msg : 热门标签列表
     * data : [{"title":"dxjxndn","class_id":321,"path":null},{"title":"Fadsf","class_id":320,"path":null},{"title":"弄","class_id":319,"path":null},{"title":"tgg","class_id":315,"path":null},{"title":"gg","class_id":326,"path":null},{"title":"您","class_id":325,"path":null}]
     */

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
        private String class_id;
        private String path;
        private boolean isSelect;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getClass_id() {
            return class_id;
        }

        public void setClass_id(String class_id) {
            this.class_id = class_id;
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

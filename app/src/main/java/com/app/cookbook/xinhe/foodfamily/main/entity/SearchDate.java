package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

/**
 * Created by shiyujia02 on 2018/1/28.
 */

public class SearchDate {

    /**
     * current_page : 1
     * data : [{"question_id":2220,"question_title":"123","answer_data":null},{"question_id":2219,"question_title":"The new version of a great game","answer_data":null},{"question_id":2213,"question_title":"新增问题1111","answer_data":{"id":2440,"uid":259,"created_at":1527577417,"thumbs":9,"name":"风信子","content_remove":"","users_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/12723c383dca401897e6cdffc52215bc.jpg","comment_count":0,"img_data":{"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/32ed038e9169490c9245eca324171394.jpg"}}},{"question_id":2212,"question_title":"我是***的人。我喜欢***。我爱法**。我喜欢**all的人。我爱法论。**。法**。","answer_data":{"id":2437,"uid":170,"created_at":1527573872,"thumbs":12,"name":"Kevin","content_remove":"公民你哦破名 哦破破哦破 in破h哈哈大笑破哦这是yppxszy也是一种所以wppwwwsxwq iszsysys^_^有朋友想随心所欲想随心所欲","users_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png","comment_count":0,"img_data":{"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/4E30E1EA-E7C8-46F0-B76D-15330F6378C8.png"}}},{"question_id":2211,"question_title":"我是***。我喜欢***。我很**all。我喜欢**all。","answer_data":{"id":2432,"uid":559,"created_at":1527559237,"thumbs":0,"name":"之外","content_remove":"我是***。我喜欢***。我很**all。我喜欢**all。我是***。我喜欢***。我很**all。我喜欢**all。我是***。我喜欢***。我很**all。我喜欢**all。我是***。我喜欢***。我很**all。我喜欢**all。我是***。我喜欢***。我很**all。我喜欢**all。","users_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appProduction/152654104429725081.jpg","comment_count":0,"img_data":{"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/15275592209669300.jpg"}}},{"question_id":2210,"question_title":"我是***。哈哈哈我喜欢**。**是**all中的**。我不是***的。","answer_data":{"id":2430,"uid":170,"created_at":1527502770,"thumbs":12,"name":"Kevin","content_remove":"","users_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png","comment_count":0,"img_data":{"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/A06DF2D2-50A8-49C2-8189-2D2E63D62FB0.png"}}},{"question_id":2209,"question_title":"xufdtgfi","answer_data":{"id":2420,"uid":560,"created_at":1527496589,"thumbs":0,"name":"212122的的的饿的不是东西\u2026\u2026好","content_remove":"************","users_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/152655290669299237.png","comment_count":0,"img_data":{"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/152749655798442465.jpg"}}},{"question_id":2208,"question_title":"jxjxjjx","answer_data":{"id":2438,"uid":170,"created_at":1527573918,"thumbs":12,"name":"Kevin","content_remove":"一嘻嘻想随心所欲Mr 你一样也是一种","users_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/6C311115-9B98-4D56-BA4D-8C6EE4AA7F5D.png","comment_count":0,"img_data":null}},{"question_id":2207,"question_title":"ch","answer_data":null},{"question_id":2206,"question_title":"xfb","answer_data":{"id":2423,"uid":559,"created_at":1527497946,"thumbs":0,"name":"之外","content_remove":"*********","users_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appProduction/152654104429725081.jpg","comment_count":0,"img_data":{"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appDebug/15274979219774076.jpg"}}}]
     * first_page_url : http://app1.shiyujia.com/answerApi/home/getNewQuestion?page=1
     * from : 1
     * last_page : 217
     * last_page_url : http://app1.shiyujia.com/answerApi/home/getNewQuestion?page=217
     * next_page_url : http://app1.shiyujia.com/answerApi/home/getNewQuestion?page=2
     * path : http://app1.shiyujia.com/answerApi/home/getNewQuestion
     * per_page : 10
     * prev_page_url : null
     * to : 10
     * total : 2162
     */

    private int current_page;
    private String first_page_url;
    private int from;
    private int last_page;
    private String last_page_url;
    private String next_page_url;
    private String path;
    private int per_page;
    private Object prev_page_url;
    private int to;
    private int total;
    private List<DataBean> data;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public String getFirst_page_url() {
        return first_page_url;
    }

    public void setFirst_page_url(String first_page_url) {
        this.first_page_url = first_page_url;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public String getLast_page_url() {
        return last_page_url;
    }

    public void setLast_page_url(String last_page_url) {
        this.last_page_url = last_page_url;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public Object getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(Object prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * question_id : 2220
         * question_title : 123
         * answer_data : null
         */

        private int question_id;
        private String question_title;
        private NewAnswerDate answer_data;
        private String is_follow;

        public String getIs_follow() {
            return is_follow;
        }

        public void setIs_follow(String is_follow) {
            this.is_follow = is_follow;
        }

        public int getQuestion_id() {
            return question_id;
        }

        public void setQuestion_id(int question_id) {
            this.question_id = question_id;
        }

        public String getQuestion_title() {
            return question_title;
        }

        public void setQuestion_title(String question_title) {
            this.question_title = question_title;
        }

        public NewAnswerDate getAnswer_data() {
            return answer_data;
        }

        public void setAnswer_data(NewAnswerDate answer_data) {
            this.answer_data = answer_data;
        }
    }

}

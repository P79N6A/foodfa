package com.app.cookbook.xinhe.foodfamily.main.entity;

import java.util.List;

/**
 * Created by shiyujia02 on 2018/1/25.
 */

public class LocationDate {


    /**
     * current_page : 1
     * data : [{"click":1,"content":"亲爱的醉，感谢您的举报，我们会尽快核实并处理相关内容。","created_at":1542684125,"id":31399,"image_text_id":68,"status":31,"video_id":0,"video_image_comment_id":0},{"click":2,"content":"【邀请您回答】babymasuly:#福豆大食话#深夜放毒，晒晒你的晚餐/宵夜吧~","created_at":1536142811,"id":31319,"image_text_id":0,"question_id":2514,"status":4,"video_id":0,"video_image_comment_id":0},{"click":2,"content":"【邀请您回答】大福豆:#福豆大食话# 分享你买过的最好吃/最难吃的旅游土特产！","created_at":1536052838,"id":30736,"image_text_id":0,"question_id":2513,"status":4,"video_id":0,"video_image_comment_id":0},{"click":2,"content":"【邀请您回答】大福豆:#福豆大食话# 旅游季来啦！一起说说哪个地方的美食让你最难忘吧~","created_at":1535961757,"id":30211,"image_text_id":0,"question_id":2508,"status":4,"video_id":0,"video_image_comment_id":0},{"click":2,"content":"【邀请您回答】大福豆:#福豆大食话# 谁才是白米饭的最佳CP？","created_at":1535706426,"id":29667,"image_text_id":0,"question_id":2506,"status":4,"video_id":0,"video_image_comment_id":0},{"click":2,"content":"【邀请您回答】大福豆:#福豆大食话# 你知道哪些吃了之后真的会让人变美/变瘦的食物呢？","created_at":1535600713,"id":29141,"image_text_id":0,"question_id":2501,"status":4,"video_id":0,"video_image_comment_id":0},{"click":2,"content":"【邀请您回答】马甲线CICI:那些年，我们追过的《樱桃小丸子》里的美食","created_at":1535536394,"id":28619,"image_text_id":0,"question_id":2497,"status":4,"video_id":0,"video_image_comment_id":0},{"click":2,"content":"【邀请您回答】风页:#福豆大食话# 中秋又要到了，说说你最喜欢吃什么月饼！","created_at":1535449552,"id":28108,"image_text_id":0,"question_id":2467,"status":4,"video_id":0,"video_image_comment_id":0},{"click":2,"content":"【邀请您回答】大福豆:#吃货爱养生# 秋燥来了，吃什么才能对抗ta？","created_at":1535342028,"id":27584,"image_text_id":0,"question_id":2483,"status":4,"video_id":0,"video_image_comment_id":0},{"click":2,"content":"【邀请您回答】咚咚咚:#福豆大食话# 懒人料理大比拼，你能在最短的时间内做出哪些好吃的？","created_at":1535097567,"id":26960,"image_text_id":0,"question_id":2477,"status":4,"video_id":0,"video_image_comment_id":0}]
     * first_page_url : http://app1.shiyujia.com/answerApi/message/systemListV5?page=1
     * from : 1
     * last_page : 8
     * last_page_url : http://app1.shiyujia.com/answerApi/message/systemListV5?page=8
     * next_page_url : http://app1.shiyujia.com/answerApi/message/systemListV5?page=2
     * path : http://app1.shiyujia.com/answerApi/message/systemListV5
     * per_page : 10
     * to : 10
     * total : 74
     */

    private String current_page;
    private String first_page_url;
    private String from;
    private String last_page;
    private String last_page_url;
    private String next_page_url;
    private String path;
    private String per_page;
    private String to;
    private String total;
    private List<DataBean> data;

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public String getFirst_page_url() {
        return first_page_url;
    }

    public void setFirst_page_url(String first_page_url) {
        this.first_page_url = first_page_url;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getLast_page() {
        return last_page;
    }

    public void setLast_page(String last_page) {
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

    public String getPer_page() {
        return per_page;
    }

    public void setPer_page(String per_page) {
        this.per_page = per_page;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
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
         * click : 1
         * content : 亲爱的醉，感谢您的举报，我们会尽快核实并处理相关内容。
         * created_at : 1542684125
         * id : 31399
         * image_text_id : 68
         * status : 31
         * video_id : 0
         * video_image_comment_id : 0
         * question_id : 2514
         */

        private String click;
        private String content;
        private String created_at;
        private String id;
        private String image_text_id;
        private String status;
        private String video_id;
        private String video_image_comment_id;
        private String question_id;
        private String answer_id;
        private String url="";

        public String getClick() {
            return click;
        }

        public void setClick(String click) {
            this.click = click;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImage_text_id() {
            return image_text_id;
        }

        public void setImage_text_id(String image_text_id) {
            this.image_text_id = image_text_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getVideo_id() {
            return video_id;
        }

        public void setVideo_id(String video_id) {
            this.video_id = video_id;
        }

        public String getVideo_image_comment_id() {
            return video_image_comment_id;
        }

        public void setVideo_image_comment_id(String video_image_comment_id) {
            this.video_image_comment_id = video_image_comment_id;
        }

        public String getQuestion_id() {
            return question_id;
        }

        public void setQuestion_id(String question_id) {
            this.question_id = question_id;
        }

        public String getAnswer_id() {
            return answer_id;
        }

        public void setAnswer_id(String answer_id) {
            this.answer_id = answer_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

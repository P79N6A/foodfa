package com.app.cookbook.xinhe.foodfamily.shiyujia.entity;

import java.util.List;

public class QuestionsAnswers {

    private String current_page;
    private String first_page_url;
    private String from;
    private String last_page;
    private String last_page_url;
    private String next_page_url;
    private String path;
    private String per_page;
    private Object prev_page_url;
    private String to;
    private String total;
    private RecommendDataBean recommend_data;
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

    public Object getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(Object prev_page_url) {
        this.prev_page_url = prev_page_url;
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

    public RecommendDataBean getRecommend_data() {
        return recommend_data;
    }

    public void setRecommend_data(RecommendDataBean recommend_data) {
        this.recommend_data = recommend_data;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class RecommendDataBean {
        /**
         * question_id : 1760
         * question_title : 高脂肪饮食如何搭配能有助于减少脂肪？
         * user_id : 44
         * answer_data : {"id":1817,"pid":1760,"created_at":1524461302,"uid":376,"content_remove":"吃一顿美味的高脂肪低碳水化合物美食，确实可以做到减少脂肪并让你健康情况得到改善。瑞典的一项研究显示，你摄入美味的富含脂肪和蛋白质的美食会让你变得更苗条，因为这两种物质结合起来会让你非常有饱腹感，并且降低饥饿感，而一切的关键是限制碳水化合物。在这个实验中，参与者是二型糖尿病患者，研究人员把他们分成了两组，男性每天摄入1800卡，女性1600卡，这两组的饮食供能比如下：（1）高脂肪低碳水化合物的饮食是由50%的脂肪、20%的碳水化合物和30%的蛋白质构成，碳水化合物均为低升糖指数的食材。（2）低脂肪饮食是由30%的脂肪、60%的碳水化合物和10%的蛋白质构成。结果显示，这两组人在六个月后减少了同样的重量（平均4公斤多），采用高脂肪低碳水化合物饮食可以改善血糖耐受程度，胰岛素水平降低了30%。高脂肪组的血压也更低了，他们的胆固醇水平得到改善，低密度脂蛋白水平显著降低。这个实验总共进行了两年，两年后，高脂肪组的参与者摄入的来自饱和脂肪的卡路里数减少了20%，他们的整体胆固醇水平都得到了改善。可是吃这么多脂肪，怎么可能让胆固醇水平更好呢，尤其是这些脂肪很多都是来自饱和脂肪？在营养与代谢期刊中，有这么一项研究中，科学家们发现胆固醇水平主要受以下两种情况影响：（1）当我们摄入高碳水化合物饮食，尤其是高升糖指数的食物，尤其是那种植物来源的脂肪，例如玉米、大豆、葵花籽和植物油，你的低密度脂蛋白水平一定会很高，这时罹患动脉粥样硬化的几率非常高。研究显示，用脂肪代替碳水化合物会降低甘油三酯水平，甘油三酯水平高了必然对身体不好，同时，高密度脂蛋白的水平升高了，也就是我们所说的好胆固醇水平升高了，这个理论非常符合我们之前所说的那个瑞典实验。（2）当我们摄入动物来源的饱和脂肪时，尤其是牛肉、鸡肉和猪肉，而且碳水化合物较少时，低密度脂蛋白并没有升高。那些摄入大量富含反式脂肪的加工肉类和植物来源脂肪的人，低密度脂蛋白水平呈升高状态。那么采用高脂肪饮食时，我们应该注意什么呢？（1）你的饮食最好包含大量优质的蛋白质，供能比可以控制在20%到30%。（2）将碳水化合物的供能比控制到25%以下，而且最好主要是植物来源的低升糖指数碳水化合物。如果想减脂，碳水化合物的量最多不能超过100克。（3）避免加工后的碳水化合物和糖，尽量减少谷物的摄入，管理好自己的血糖水平。（4）完全断绝反式脂肪和经过加工的脂肪。避免来自植物的脂肪，例如玉米、葵花籽和大豆。（5）最好摄入来自天然食物的脂肪，例如有机肉类、鸡蛋、坚果、牛油果和椰子，全脂奶制品也不错。","thumbs":17,"img_data":null,"user_data":{"name":"晃悠的老刘","users_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/D1AC9A4B-4B1C-44FC-9242-FEC521FC913C.png","uid":376}}
         * user_data : {"name":"章鱼丸子不要放章鱼","users_avatar":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151703432789012258.jpg","uid":44}
         */

        private String question_id;
        private String question_title;
        private String user_id;
        private AnswerDataBean answer_data;
        private UserDataBeanX user_data;

        public String getQuestion_id() {
            return question_id;
        }

        public void setQuestion_id(String question_id) {
            this.question_id = question_id;
        }

        public String getQuestion_title() {
            return question_title;
        }

        public void setQuestion_title(String question_title) {
            this.question_title = question_title;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public AnswerDataBean getAnswer_data() {
            return answer_data;
        }

        public void setAnswer_data(AnswerDataBean answer_data) {
            this.answer_data = answer_data;
        }

        public UserDataBeanX getUser_data() {
            return user_data;
        }

        public void setUser_data(UserDataBeanX user_data) {
            this.user_data = user_data;
        }

        public static class AnswerDataBean {
            /**
             * id : 1817
             * pid : 1760
             * created_at : 1524461302
             * uid : 376
             * content_remove : 吃一顿美味的高脂肪低碳水化合物美食，确实可以做到减少脂肪并让你健康情况得到改善。瑞典的一项研究显示，你摄入美味的富含脂肪和蛋白质的美食会让你变得更苗条，因为这两种物质结合起来会让你非常有饱腹感，并且降低饥饿感，而一切的关键是限制碳水化合物。在这个实验中，参与者是二型糖尿病患者，研究人员把他们分成了两组，男性每天摄入1800卡，女性1600卡，这两组的饮食供能比如下：（1）高脂肪低碳水化合物的饮食是由50%的脂肪、20%的碳水化合物和30%的蛋白质构成，碳水化合物均为低升糖指数的食材。（2）低脂肪饮食是由30%的脂肪、60%的碳水化合物和10%的蛋白质构成。结果显示，这两组人在六个月后减少了同样的重量（平均4公斤多），采用高脂肪低碳水化合物饮食可以改善血糖耐受程度，胰岛素水平降低了30%。高脂肪组的血压也更低了，他们的胆固醇水平得到改善，低密度脂蛋白水平显著降低。这个实验总共进行了两年，两年后，高脂肪组的参与者摄入的来自饱和脂肪的卡路里数减少了20%，他们的整体胆固醇水平都得到了改善。可是吃这么多脂肪，怎么可能让胆固醇水平更好呢，尤其是这些脂肪很多都是来自饱和脂肪？在营养与代谢期刊中，有这么一项研究中，科学家们发现胆固醇水平主要受以下两种情况影响：（1）当我们摄入高碳水化合物饮食，尤其是高升糖指数的食物，尤其是那种植物来源的脂肪，例如玉米、大豆、葵花籽和植物油，你的低密度脂蛋白水平一定会很高，这时罹患动脉粥样硬化的几率非常高。研究显示，用脂肪代替碳水化合物会降低甘油三酯水平，甘油三酯水平高了必然对身体不好，同时，高密度脂蛋白的水平升高了，也就是我们所说的好胆固醇水平升高了，这个理论非常符合我们之前所说的那个瑞典实验。（2）当我们摄入动物来源的饱和脂肪时，尤其是牛肉、鸡肉和猪肉，而且碳水化合物较少时，低密度脂蛋白并没有升高。那些摄入大量富含反式脂肪的加工肉类和植物来源脂肪的人，低密度脂蛋白水平呈升高状态。那么采用高脂肪饮食时，我们应该注意什么呢？（1）你的饮食最好包含大量优质的蛋白质，供能比可以控制在20%到30%。（2）将碳水化合物的供能比控制到25%以下，而且最好主要是植物来源的低升糖指数碳水化合物。如果想减脂，碳水化合物的量最多不能超过100克。（3）避免加工后的碳水化合物和糖，尽量减少谷物的摄入，管理好自己的血糖水平。（4）完全断绝反式脂肪和经过加工的脂肪。避免来自植物的脂肪，例如玉米、葵花籽和大豆。（5）最好摄入来自天然食物的脂肪，例如有机肉类、鸡蛋、坚果、牛油果和椰子，全脂奶制品也不错。
             * thumbs : 17
             * img_data : null
             * user_data : {"name":"晃悠的老刘","users_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/D1AC9A4B-4B1C-44FC-9242-FEC521FC913C.png","uid":376}
             */

            private String id;
            private String pid;
            private String created_at;
            private String uid;
            private String content_remove;
            private String thumbs;
            private Object img_data;
            private UserDataBean user_data;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getContent_remove() {
                return content_remove;
            }

            public void setContent_remove(String content_remove) {
                this.content_remove = content_remove;
            }

            public String getThumbs() {
                return thumbs;
            }

            public void setThumbs(String thumbs) {
                this.thumbs = thumbs;
            }

            public Object getImg_data() {
                return img_data;
            }

            public void setImg_data(Object img_data) {
                this.img_data = img_data;
            }

            public UserDataBean getUser_data() {
                return user_data;
            }

            public void setUser_data(UserDataBean user_data) {
                this.user_data = user_data;
            }

            public static class UserDataBean {
                /**
                 * name : 晃悠的老刘
                 * users_avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/YRCkAM/D1AC9A4B-4B1C-44FC-9242-FEC521FC913C.png
                 * uid : 376
                 */

                private String name;
                private String users_avatar;
                private String uid;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getUsers_avatar() {
                    return users_avatar;
                }

                public void setUsers_avatar(String users_avatar) {
                    this.users_avatar = users_avatar;
                }

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }
            }
        }

        public static class UserDataBeanX {
            /**
             * name : 章鱼丸子不要放章鱼
             * users_avatar : http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151703432789012258.jpg
             * uid : 44
             */

            private String name;
            private String users_avatar;
            private String uid;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUsers_avatar() {
                return users_avatar;
            }

            public void setUsers_avatar(String users_avatar) {
                this.users_avatar = users_avatar;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }
        }
    }

    public static class DataBean {
        /**
         * question_id : 2353
         * question_title : 今年世界咖啡师大赛冠军首次由女咖啡师夺取，大家分享下自己欣赏的饮食领域厉害的小姐姐？
         * user_id : 369
         * answer_data : {"id":3717,"pid":2353,"created_at":1532745245,"uid":79,"content_remove":"潇洒姐\u2014\u2014不仅仅属于饮食界，而是个斜杠姐姐。出版了不少书籍，被大家所熟悉的是《按自己的意愿过一生》除了趁早读书会以外，最近潇洒姐的事业着重在新书《和潇洒姐塑身100天》以及在各个社交媒体中受到热捧的女性塑身打卡活动中塑身100天不仅仅是健身、锻炼，更多是提醒大家关注自己的饮食结构，选择健康的饮食，配合你的锻炼，分享了健康向上的生活态度，非常励志也很期待潇洒姐能够在塑身饮食方面有更多展示","thumbs":0,"img_data":{"answer_id":3717,"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appProduction/answer/2018_07/153274523089452878.jpg"},"user_data":{"name":"马甲线CICI","users_avatar":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151704259352308464.jpg","uid":79}}
         * user_data : {"name":"盒盒盒","users_avatar":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appProduction/152444791827149934.jpg","uid":369}
         */

        private String question_id;
        private String question_title;
        private String user_id;
        private String is_recommend_class;
        private AnswerDataBeanX answer_data;
        private UserDataBeanXXX user_data;

        public String getIs_recommend_class() {
            return is_recommend_class;
        }

        public void setIs_recommend_class(String is_recommend_class) {
            this.is_recommend_class = is_recommend_class;
        }

        public String getQuestion_id() {
            return question_id;
        }

        public void setQuestion_id(String question_id) {
            this.question_id = question_id;
        }

        public String getQuestion_title() {
            return question_title;
        }

        public void setQuestion_title(String question_title) {
            this.question_title = question_title;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public AnswerDataBeanX getAnswer_data() {
            return answer_data;
        }

        public void setAnswer_data(AnswerDataBeanX answer_data) {
            this.answer_data = answer_data;
        }

        public UserDataBeanXXX getUser_data() {
            return user_data;
        }

        public void setUser_data(UserDataBeanXXX user_data) {
            this.user_data = user_data;
        }

        public static class AnswerDataBeanX {
            /**
             * id : 3717
             * pid : 2353
             * created_at : 1532745245
             * uid : 79
             * content_remove : 潇洒姐——不仅仅属于饮食界，而是个斜杠姐姐。出版了不少书籍，被大家所熟悉的是《按自己的意愿过一生》除了趁早读书会以外，最近潇洒姐的事业着重在新书《和潇洒姐塑身100天》以及在各个社交媒体中受到热捧的女性塑身打卡活动中塑身100天不仅仅是健身、锻炼，更多是提醒大家关注自己的饮食结构，选择健康的饮食，配合你的锻炼，分享了健康向上的生活态度，非常励志也很期待潇洒姐能够在塑身饮食方面有更多展示
             * thumbs : 0
             * img_data : {"answer_id":3717,"path":"https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appProduction/answer/2018_07/153274523089452878.jpg"}
             * user_data : {"name":"马甲线CICI","users_avatar":"http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151704259352308464.jpg","uid":79}
             */

            private String id;
            private String pid;
            private String created_at;
            private String uid;
            private String content_remove;
            private String thumbs;
            private ImgDataBean img_data;
            private UserDataBeanXX user_data;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getContent_remove() {
                return content_remove;
            }

            public void setContent_remove(String content_remove) {
                this.content_remove = content_remove;
            }

            public String getThumbs() {
                return thumbs;
            }

            public void setThumbs(String thumbs) {
                this.thumbs = thumbs;
            }

            public ImgDataBean getImg_data() {
                return img_data;
            }

            public void setImg_data(ImgDataBean img_data) {
                this.img_data = img_data;
            }

            public UserDataBeanXX getUser_data() {
                return user_data;
            }

            public void setUser_data(UserDataBeanXX user_data) {
                this.user_data = user_data;
            }

            public static class ImgDataBean {
                /**
                 * answer_id : 3717
                 * path : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appProduction/answer/2018_07/153274523089452878.jpg
                 */

                private String answer_id;
                private String path;

                public String getAnswer_id() {
                    return answer_id;
                }

                public void setAnswer_id(String answer_id) {
                    this.answer_id = answer_id;
                }

                public String getPath() {
                    return path;
                }

                public void setPath(String path) {
                    this.path = path;
                }
            }

            public static class UserDataBeanXX {
                /**
                 * name : 马甲线CICI
                 * users_avatar : http://syjapppic.oss-cn-hangzhou.aliyuncs.com/151704259352308464.jpg
                 * uid : 79
                 */

                private String name;
                private String users_avatar;
                private String uid;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getUsers_avatar() {
                    return users_avatar;
                }

                public void setUsers_avatar(String users_avatar) {
                    this.users_avatar = users_avatar;
                }

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }
            }
        }

        public static class UserDataBeanXXX {
            /**
             * name : 盒盒盒
             * users_avatar : https://syjapppic.oss-cn-hangzhou.aliyuncs.com/appProduction/152444791827149934.jpg
             * uid : 369
             */

            private String name;
            private String users_avatar;
            private String uid;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUsers_avatar() {
                return users_avatar;
            }

            public void setUsers_avatar(String users_avatar) {
                this.users_avatar = users_avatar;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }
        }
    }
}

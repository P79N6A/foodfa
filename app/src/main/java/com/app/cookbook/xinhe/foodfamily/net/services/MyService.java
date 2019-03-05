package com.app.cookbook.xinhe.foodfamily.net.services;


import com.app.cookbook.xinhe.foodfamily.main.entity.AdviceLeixing;
import com.app.cookbook.xinhe.foodfamily.main.entity.AliEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.AnswerListOneData;
import com.app.cookbook.xinhe.foodfamily.main.entity.CommentLeveTwo;
import com.app.cookbook.xinhe.foodfamily.main.entity.FenLeiBiaoQianEn;
import com.app.cookbook.xinhe.foodfamily.main.entity.HotSearch;
import com.app.cookbook.xinhe.foodfamily.main.entity.HuiDaDate;
import com.app.cookbook.xinhe.foodfamily.main.entity.JiexiLei;
import com.app.cookbook.xinhe.foodfamily.main.entity.Label;
import com.app.cookbook.xinhe.foodfamily.main.entity.LocationDate;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageComment;
import com.app.cookbook.xinhe.foodfamily.main.entity.OtherUserEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.ReportMsg;
import com.app.cookbook.xinhe.foodfamily.main.entity.ShouCangDate;
import com.app.cookbook.xinhe.foodfamily.main.entity.WoGuanZhuEntity;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.CollectAnswer;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.DiscoverAttention;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.DiscoverBanner;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.DiscoverConfiguration;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.DiscoverRecommend;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.DraftDelete;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.ImageTextComment;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.VideoComment;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by limengtao on 2017/3/17.
 */

public interface MyService {

    /**
     * 获取验证码
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answer/feed/add")
    Observable<Bean<List<String>>> tijiao(@FieldMap Map<String, String> map);

    /**
     * 新增问题
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/question/addQuestion")
    Observable<Bean<Object>> add_question(@FieldMap Map<String, String> map);

    /**
     * 保存问题草稿
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/draftQuestion/draftQuestion")
    Observable<Bean<Object>> save_caogao(@FieldMap Map<String, String> map);


    /**
     * 发布文本内容
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/imageText/addImageText")
    Observable<Bean<Object>> fabu_content(@FieldMap Map<String, Object> map);

    /**
     * 发布文本草稿
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/draft/saveDraft")
    Observable<Bean<Object>> fabu_content_caogao(@FieldMap Map<String, Object> map);

    /**
     * 编辑文本草稿
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/imageText/updateImageText")
    Observable<Bean<Object>> bianji_content_caogao(@FieldMap Map<String, Object> map);

    /**
     * 修改文本草稿
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/draft/updateDraft")
    Observable<Bean<Object>> save_content_caogao_bianji(@FieldMap Map<String, Object> map);

    /**
     * 发布文本草稿
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/draft/releaseDraft")
    Observable<Bean<Object>> fabu_caogao_content(@FieldMap Map<String, Object> map);

    /**
     * 发布文本草稿
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/video/addVideo")
    Observable<Bean<Object>> fabu_movie(@FieldMap Map<String, Object> map);

    /**
     * 保存编辑问题草稿
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/draftQuestion/updateDraftQuestion")
    Observable<Bean<Object>> save_edit_caogao(@FieldMap Map<String, String> map);

    /**
     * 发布问题草稿
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/draftQuestion/addDraftQuestion")
    Observable<Bean<Object>> fabu_edit_caogao(@FieldMap Map<String, String> map);

    /**
     * 发布问题草稿
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/draftAnswer/setDraftAnswer")
    Observable<Bean<Object>> fabu_edit_caogao_answer(@FieldMap Map<String, String> map);


    /**
     * 保存答案草稿
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/draftAnswer/draftAnswer")
    Observable<Bean<Object>> save_answer_caogao(@FieldMap Map<String, String> map);

    /**
     * 更新数据
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/twoExplore/updateQuestion")
    Observable<Bean<Object>> update_question(@FieldMap Map<String, String> map);

    /**
     * 新增回答
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/answer/addAnswerNew")
    Observable<Bean<Object>> new_answer(@FieldMap Map<String, String> map);

    /**
     * 修改回答
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/answer/updateAnswerV4")
    Observable<Bean<Object>> update_answer(@FieldMap Map<String, String> map);

    /**
     * 热词点击次数
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/home/searchClick")
    Observable<Bean<Object>> reci_click_number(@FieldMap Map<String, String> map);

    /**
     * 修改回答草稿
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/draftAnswer/updateDraftAnswer")
    Observable<Bean<Object>> update_answer_caogao(@FieldMap Map<String, String> map);


    /**
     * 解析
     *
     * @return
     */
    @GET("answer/feed/index")
    Observable<Bean<JiexiLei>> jiexi();

    /**
     * 获取验证码
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/login/getCode")
    Observable<Bean<Object>> get_yanzhengma(@FieldMap Map<String, String> map);

    /**
     * 获取验证码
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/login/getCodeLogin")
    Observable<Bean<Object>> get_login_yanzhengma(@FieldMap Map<String, String> map);

    /**
     * 获取验证码
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/login/getPhoneSetPassword")
    Observable<Bean<Object>> get_update_password_yanzhengma(@FieldMap Map<String, String> map);

    /**
     * 获取验证码
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/login/getLoginStatusCode")
    Observable<Bean<Object>> get_xiugai_password_yanzhengma(@FieldMap Map<String, String> map);

    /**
     * 获取验证码
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/login/updateTelCode")
    Observable<Bean<Object>> get_xiugai_phone_yanzhengma(@FieldMap Map<String, String> map);

    /**
     * 注册
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/login/register")
    Observable<Bean<Object>> fast_register(@FieldMap Map<String, String> map);

    /**
     * 注册
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/login/phoneSetPassword")
    Observable<Bean<Object>> update_password(@FieldMap Map<String, String> map);

    /**
     * 注册
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/users/updatePassword")
    Observable<Bean<Object>> update_password_two(@FieldMap Map<String, String> map);

    /**
     * 注册
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/users/updateUserTel")
    Observable<Bean<Object>> update_phone_number(@FieldMap Map<String, String> map);

    /**
     * 登录
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/login/login")
    Observable<Bean<Object>> fast_login(@FieldMap Map<String, String> map);


    /**
     * 登录
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/login/phoneLogin")
    Observable<Bean<Object>> yanzhengma_login(@FieldMap Map<String, String> map);

    /**
     * 登录
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/login/phoneLogin")
    Observable<Bean<Object>> yanzhengma_login2(@FieldMap Map<String, String> map);

    /**
     * 登录
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/login/login")
    Observable<Bean<List<String>>> fast_login2(@FieldMap Map<String, String> map);

    /**
     * 反馈
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/users/addFeedPathAndroid")
    Observable<Bean<Object>> fankui(@FieldMap Map<String, Object> map);

    /**
     * 获取启动页
     *
     * @return
     */
    @GET("answerApi/users/getFeedType")
    Observable<Bean<List<AdviceLeixing>>> get_user_leixing();

    /**
     * 获取用户协议
     *
     * @return
     */
    @GET("answerApi/protocol/index")
    Observable<Bean<Object>> get_user_xieyi();


    /**
     * 获取阿里云的信息
     *
     * @param url
     * @return
     */
    @GET
    Observable<AliEntity> get_ali(@Url String url);

    /**
     * 设置个人信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/users/updateUsers")
    Observable<Bean<Object>> set_information(@FieldMap Map<String, String> map);

    /**
     * 设置个人信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/users/index")
    Observable<Bean<Object>> get_user_information(@FieldMap Map<String, String> map);

    /**
     * 设置他人信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/other/index")
    Observable<Bean<OtherUserEntity>> get_other_user_information(@FieldMap Map<String, String> map);

    /**
     * 关注标签
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/other/getOtherFollowClass")
    Observable<Bean<Object>> get_guanzhu_biaoqian(@FieldMap Map<String, String> map);

    /**
     * 我关注标签领域
     *
     * @return
     */
    @GET("answerApi/users/getOwnFollowClass")
    Observable<Bean<Object>> get_my_guanzhu_biaoqian(@Query("page") String page);

    /**
     * Ta关注的标签
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/other/otherFollowClass")
    Observable<Bean<Object>> get_other_follow_class(@FieldMap Map<String, String> map);

    /**
     * 关注我的人
     *
     * @return
     */
    @GET("answerApi/users/getOwnPassiveFollowUsers")
    Observable<Bean<Object>> get_guanzhu_wo_ren(@Query("message_id") String page);


    /**
     * 关注的问题
     *
     * @return
     */
    @GET("answerApi/message/followUsersQuestionMessage")
    Observable<Bean<Object>> get_guanzhu_question(@Query("message_id") String page);

    /**
     * 我的关注的问题
     *
     * @return
     */
    @GET("answerApi/users/getOwnFollowQuestion")
    Observable<Bean<Object>> get_guanzhu_mine_question(@Query("page") String page);

    /**
     * ta关注的问题
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/other/getOtherFollowQuestion")
    Observable<Bean<Object>> get_ta_guanzhu_question(@FieldMap Map<String, String> map);

    /**
     * 获取启动页
     *
     * @return
     */
    @GET("answerApi/start/index")
    Observable<Bean<Object>> get_splash_page();

    /**
     * 获取启动页
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/startLogs/index")
    Observable<Bean<Object>> version_tongji(@FieldMap Map<String, String> map);

    /**
     * 发现美好
     *
     * @return
     */
    @GET("answerApi/twoClass/getList")
    Observable<Bean<Object>> found_beauty(@Query("page") String id);

//    /**
//     * 发现详情
//     *
//     * @return
//     */
//    @GET("answerApi/class/detail")
//    Observable<Bean<Object>> found_detail(@Query("id") String id, @Query("page") String page);

    /**
     * 发现详情
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/class/classDetails")
    Observable<Bean<Object>> found_detail(@FieldMap Map<String, String> map);

    /**
     * 获取消息首页
     *
     * @return
     */
    @GET("answerApi/message/messageV5")
    Observable<Bean<Object>> get_location_shouye();


    /**
     * 获取系统消息
     *
     * @return
     */
    @GET("answerApi/message/systemListV5")
    Observable<Bean<LocationDate>> get_xitong_xiaoxi(@Query("message_id") String message_id);

    /**
     * 我的收藏
     *
     * @return
     */
    @GET("answerApi/users/getOwnCollect")
    Observable<Bean<ShouCangDate>> my_shoucang_resource();


    /**
     * 消息
     *
     * @return
     */
    @GET("answerApi/message/detail")
    Observable<Bean<List<String>>> get_xitong_detail(@Query("type") String type, @Query("id") String id);

    /**
     * 消息
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/message/deleteSystemList")
    Observable<Bean<List<String>>> delete_xitong_message(@FieldMap Map<String, String> map);

    /**
     * 删除最新回答
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/message/deleteNewAnswer")
    Observable<Bean<List<String>>> delete_zuixin_answer(@FieldMap Map<String, String> map);

    /**
     * 删除点赞收藏
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/message/deleteThumbsCollectList")
    Observable<Bean<List<String>>> delete_dianzan_shoucang(@FieldMap Map<String, String> map);


    /**
     * 消息
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/message/deleteFollowUsersQuestionMessage")
    Observable<Bean<List<String>>> delete_guanzhuwo_question(@FieldMap Map<String, String> map);

    /**
     * 删除评论
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/message/deleteCommentList")
    Observable<Bean<List<String>>> delete_commentList_question(@FieldMap Map<String, String> map);


    /**
     * 最新回答
     *
     * @return
     */
    @GET("answerApi/message/getNewAnswerV5")
    Observable<Bean<Object>> zuixin_huida(@Query("message_id") String message_id);

    /**
     * 我的收藏
     *
     * @return
     */
    @GET("answerApi/users/getOwnCollectAnswer")
    Observable<Bean<Object>> get_my_shoucang(@Query("page") String page);


    /**
     * 点赞列表
     *
     * @return
     */
    @GET("answerApi/message/thumbsCollectListV5")
    Observable<Bean<Object>> get_dianzan_list(@Query("message_id") int message_id);

    /**
     * 获取自定义分类
     *
     * @return
     */
    @GET("answerApi/explore/getCategorylist")
    Observable<Bean<List<FenLeiBiaoQianEn>>> get_zidingyifenlei(@Query("is_system") String is_system);

    /**
     * 他的收藏
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/other/getOtherCollectAnswer")
    Observable<Bean<Object>> other_shoucang_resource(@FieldMap Map<String, String> map);

    /**
     * 问题详情
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/question/questionDetails")
    Observable<Bean<Object>> question_detail(@FieldMap Map<String, String> map);

    /**
     * 答案详情
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/answer/answerDetailsV4")
    Observable<Bean<Object>> answer_detail(@FieldMap Map<String, String> map);

    /**
     * 获取时间戳
     *
     * @return
     */
    @GET("answerApi/explore/getTimeStamp")
    Observable<Bean<String>> get_time();

    /**
     * 获取Ta的提问
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/users/getOwnReleaseQuestion")
    Observable<Bean<Object>> get_question(@FieldMap Map<String, String> map);

    /**
     * TAde 提问
     */
    @FormUrlEncoded
    @POST("answerApi/usersThree/getOtherQuestion")
    Observable<Bean<Object>> get_other_question(@FieldMap Map<String, String> map);
    /**
     * 获取我的提问
     *
     * @return
     */
    @GET("answerApi/usersThree/getQuestion")
    Observable<Bean<Object>> get_my_question(@Query("page") String page);

    /**
     * 获取回答
     *
     * @return
     */
    @GET("answerApi/usersThree/getOwnAnswer")
    Observable<Bean<HuiDaDate>> get_answer(@Query("page") String page);

    /**
     * 获取回答
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/usersThree/getOtherAnswer")
    Observable<Bean<HuiDaDate>> get_ta_answer(@FieldMap Map<String, String> map);

    /**
     * 获取提问列表
     *
     * @return
     */
    @GET("answerApi/explore/getLatestuplist")
    Observable<Bean<Object>> get_tiwen_liebiao(@Query("page") String page, @Query("keyword") String keyword, @Query("question_id") String question_id);


    /**
     * 获取最新列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/home/getNewQuestion")
    Observable<Bean<Object>> get_zuixin_liebiao(@FieldMap Map<String, String> map);

    /**
     * 获取精选列表
     *
     * @return
     */
    @GET("answerApi/home/getSelect")
    Observable<Bean<Object>> get_jingxuan_liebiao(@Query("page") String page);

    /**
     * 获取关注列表
     *
     * @return
     */
    @GET("answerApi/attention/attentionAnswer")
    Observable<Bean<Object>> attention_home_liebiao(@Query("page") String page);

    /**
     * 获取问题草稿列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/draftQuestion/getDraftQuestionList")
    Observable<Bean<Object>> get_question_caogao_liebiao(@FieldMap Map<String, String> map);

    /**
     * 获取问题草稿列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/draftAnswer/getDraftAnswerList")
    Observable<Bean<Object>> get_answer_caogao_liebiao(@FieldMap Map<String, String> map);

    /**
     * 我关注的人
     *
     * @return
     */
    @GET("answerApi/users/ownFollowUsers")
    Observable<Bean<WoGuanZhuEntity>> get_wo_guanzhu_ren(@Query("status") String status, @Query("page") String page, @Query("module") String module);


    /**
     * 获取热搜标签
     *
     * @return
     */
    @GET("answerApi/home/getSearchHot")
    Observable<Bean<List<HotSearch>>> get_resou_date();


    /**
     * TA关注的人
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/users/otherFollowUsers")
    Observable<Bean<WoGuanZhuEntity>> get_ta_guanzhu_ren(@FieldMap Map<String, String> map);

    /**
     * 关注TA的人
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/other/getOtherPassiveFollowUsers")
    Observable<Bean<WoGuanZhuEntity>> get_guanzhu_ta_ren(@FieldMap Map<String, String> map);


    /**
     * 关注分类
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/class/addClass")
    Observable<Bean<List<String>>> guanzhufenlei(@FieldMap Map<String, String> map);

    /**
     * 取消关注分类
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/class/addClass")
    Observable<Bean<List<String>>> quxiao_guanzhufenlei(@FieldMap Map<String, String> map);

    /**
     * 取消关注分类
     *
     * @return
     */
    @GET("answerApi/users/followUsers")
    Observable<Bean<List<String>>> mquxiao_guanzhufenlei(@Query("uuid") String uid);

    /**
     * 关注的人
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/users/followUsersV4")
    Observable<Bean<List<String>>> gaunzhuta(@FieldMap Map<String, String> map);


    /**
     * 举报答案
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/users/followUsers")
    Observable<Bean<List<String>>> answer_jubao(@FieldMap Map<String, String> map);


    /**
     * 取消关注他
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/users/concernUsers")
    Observable<Bean<List<String>>> quxiaogaunzhuta(@FieldMap Map<String, String> map);


    /**
     * 点赞
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/answer/thumbsV4")
    Observable<Bean<List<String>>> dianzan(@FieldMap Map<String, String> map);

    /**
     * 关注用户
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/users/followUsers")
    Observable<Bean<List<String>>> guanzhuyonghu(@FieldMap Map<String, String> map);

    /**
     * 取消点赞
     *
     * @return
     */
    @GET("answerApi/explore/unthumbs")
    Observable<Bean<List<String>>> undiatnzan(@Query("answer_id") String answer_id, @Query("question_id") String question_id);

    /**
     * 收藏
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/answer/collectV4")
    Observable<Bean<List<String>>> shoucang(@FieldMap Map<String, String> map);

    /**
     * 取消收藏
     *
     * @return
     */
    @GET("answerApi/explore/uncollect")
    Observable<Bean<List<String>>> unshoucang(@Query("answer_id") String answer_id, @Query("question_id") String question_id);

    /**
     * 关注问题
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/question/followQuestion")
    Observable<Bean<List<String>>> guanzhu_question(@FieldMap Map<String, String> map);

    /**
     * 关注问题
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/draftAnswer/deleteDraftAnswer")
    Observable<Bean<Object>> delete_answer_caozao(@FieldMap Map<String, String> map);

    /**
     * 关注问题
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/draftQuestion/deleteDraftQuestion")
    Observable<Bean<Object>> delete_question_caozao(@FieldMap Map<String, String> map);

    /**
     * 关注人
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/users/concernUsers")
    Observable<Bean<List<String>>> guanzhu_ren(@FieldMap Map<String, String> map);

    /**
     * 取消关注问题
     *
     * @return
     */
    @GET("answerApi/explore/unfollow")
    Observable<Bean<List<String>>> quxiao_guanzhu_question(@Query("question_id") String question_id);

    /***
     * 搜索提问
     */
    @FormUrlEncoded
    @POST("answerApi/search/searchQuestion")
    Observable<Bean<Object>> get_issue(@FieldMap Map<String, String> map);

    /***
     * 搜索回答
     */
    @FormUrlEncoded
    @POST("answerApi/search/searchAnswer")
    Observable<Bean<Object>> get_answer(@FieldMap Map<String, String> map);

    /***
     * 新搜索用户
     */
    @FormUrlEncoded
    @POST("answerApi/search/searchUsers")
    Observable<Bean<Object>> get_user(@FieldMap Map<String, String> map);


    /***
     * 新搜索标签
     */
    @FormUrlEncoded
    @POST("answerApi/search/searchClassAssociative")
    Observable<Bean<Object>> search_biaoqian_resources(@FieldMap Map<String, String> map);


    /**
     * 举报问题
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/report/reportQuestion")
    Observable<Bean<List<String>>> reports_request(@FieldMap Map<String, String> map);

    /**
     * 获取举报问题数据
     *
     * @return
     */
    @GET("answerApi/report/getReportQuestionType")
    Observable<Bean<List<ReportMsg>>> report_msgs_request();

    /**
     * 举报答案
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/report/reportAnswer")
    Observable<Bean<List<String>>> reports_Answer(@FieldMap Map<String, String> map);

    /**
     * 获取举报答案数据
     *
     * @return
     */
    @GET("answerApi/report/getReportAnswerType")
    Observable<Bean<List<ReportMsg>>> report_Answer_type();

    /**
     * 举报用户
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/report/reportUsers")
    Observable<Bean<List<String>>> reports_user(@FieldMap Map<String, String> map);

    /**
     * 获取举报用户数据
     *
     * @return
     */
    @GET("answerApi/report/getReportUsersType")
    Observable<Bean<List<ReportMsg>>> rswer_user_type();

    /**
     * 启动APP记录
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/startLogs/index")
    Observable<Bean<Object>> updata_index(@FieldMap Map<String, String> map);

    /**
     * 一级评论列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/comment/getCommentLevelOne")
    Observable<Bean<AnswerListOneData>> comment_level_one(@FieldMap Map<String, String> map);

    /**
     * 点赞评论
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/comment/thumbsComment")
    Observable<Bean<List<String>>> thumbs_comment(@FieldMap Map<String, String> map);

    /**
     * 取消点赞评论
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/comment/unThumbsComment")
    Observable<Bean<List<String>>> un_thumbs_comment(@FieldMap Map<String, String> map);

    /**
     * 二级评论列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/comment/getCommentLevelTwo")
    Observable<Bean<CommentLeveTwo>> comment_level_two(@FieldMap Map<String, String> map);

    /**
     * 添加
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/comment/addComment")
    Observable<Bean<List<String>>> add_comment(@FieldMap Map<String, String> map);

    /**
     * 消息评论列表
     *
     * @return
     */
    @GET("answerApi/message/commentListV5")
    Observable<Bean<MessageComment>> comment_list(@Query("message_id") String message_id);

    /**
     * 分享APP记录
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/share/addShareLogs")
    Observable<Bean<List<String>>> add_share_logs(@FieldMap Map<String, String> map);

    /**
     * 搜索标签
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/search/searchClassAssociative")
    Observable<Bean<Label>> search_class(@FieldMap Map<String, String> map);

    /**
     * 获取用户排名
     *
     * @return
     */
    @GET("answerApi/users/getUserRanking")
    Observable<Bean<Object>> get_user_ranking();

    /**
     * 录入信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/login/demoXin")
    Observable<Bean<Object>> get_demo_xin(@FieldMap Map<String, String> map);

    /**
     * 根据评论id 获取二轮评论列表页
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/comment/getCommentById")
    Observable<Bean<Object>> get_comment_byid(@FieldMap Map<String, String> map);

    /**
     * 验证邀请码
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/login/checkInvitationCode")
    Observable<Bean<Object>> check_invitation_code(@FieldMap Map<String, String> map);

    /**
     * 获取热门标签
     *
     * @return
     */
    @GET("answerApi/class/getHotClass")
    Observable<Bean<Object>> get_hot_class();

    /**
     * 获取热门标签
     *
     * @return
     */
    @GET("answerApi/class/getUsersUsedClass")
    Observable<Bean<Object>> get_users_used_class();

    /**
     * 立即登录
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/login/nowLogin")
    Observable<Bean<Object>> fast_new_login(@FieldMap Map<String, String> map);

    /**
     * 删除答案
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/answer/deleteAnswer")
    Observable<Bean<Object>> delete_daan(@FieldMap Map<String, String> map);

    /**
     * 删除问题
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/question/deleteQuestion")
    Observable<Bean<Object>> delete_wenti(@FieldMap Map<String, String> map);

    /**
     * 删除回复
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/comment/deleteComment")
    Observable<Bean<Object>> delete_comment(@FieldMap Map<String, String> map);

    /**
     * 删除回复
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/users/setUserData")
    Observable<Bean<Object>> user_data(@FieldMap Map<String, String> map);

    /**
     * 值得关注的人
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/attention/getFollowUsers")
    Observable<Bean<Object>> follow_users(@FieldMap Map<String, String> map);

    /**
     * 微信登陆
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/login/checkWechat")
    Observable<Bean<Object>> check_wechat(@FieldMap Map<String, String> map);

    /**
     * 微博登陆
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/login/checkWeibo")
    Observable<Bean<Object>> check_weibo(@FieldMap Map<String, String> map);

    /**
     * 微信登陆绑定手机号码
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/login/thirdPartyWechatLogin")
    Observable<Bean<Object>> wechat_bind_phone(@FieldMap Map<String, String> map);

    /**
     * 微博登陆绑定手机号码
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/login/thirdPartyWeiboLogin")
    Observable<Bean<Object>> weibo_bind_phone(@FieldMap Map<String, String> map);

    /**
     * 绑定手机号码验证码
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/login/bindingGetPhoneCode")
    Observable<Bean<Object>> code_bind_phone(@FieldMap Map<String, String> map);

    /**
     * 绑定微信
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/users/bindingWechat")
    Observable<Bean<Object>> bind_wechat(@FieldMap Map<String, String> map);

    /**
     * 绑定微博
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/users/bindingWeibo")
    Observable<Bean<Object>> bind_weibo(@FieldMap Map<String, String> map);

    /**
     * 解除微信绑定
     *
     * @return
     */
    @GET("answerApi/users/untieWechat")
    Observable<Bean<Object>> untie_wechat();

    /**
     * 解除微信绑定
     *
     * @return
     */
    @GET("answerApi/users/untieWeibo")
    Observable<Bean<Object>> untie_weibo();

    /**
     * 首页
     *
     * @return
     */
    @GET("answerApi/home/home")
    Observable<Bean<Object>> get_home(@Query("page") String page);

    /**
     * 首页图文点赞
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/imageText/thumbs")
    Observable<Bean<List<String>>> home_dianzan(@FieldMap Map<String, String> map);


    /**
     * 首页视频点赞
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/video/thumbs")
    Observable<Bean<List<String>>> home_video_dianzan(@FieldMap Map<String, String> map);

    /**
     * 首页取消点赞
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/imageText/unthumbs")
    Observable<Bean<List<String>>> home_undiatnzan(@FieldMap Map<String, String> map);

    /**
     * 首页视频点赞成功
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/video/thumbs")
    Observable<Bean<List<String>>> home_video_thumbs(@FieldMap Map<String, String> map);

    /**
     * 首页取消点赞
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/video/unthumbs")
    Observable<Bean<List<String>>> home_video_unthumbs(@FieldMap Map<String, String> map);

    /**
     * 首页收藏成功
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/imageText/collect")
    Observable<Bean<List<String>>> home_shoucang(@FieldMap Map<String, String> map);

    /**
     * 首页取消收藏
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/imageText/uncollect")
    Observable<Bean<List<String>>> home_unshoucang(@FieldMap Map<String, String> map);

    /**
     * 首页视频收藏成功
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/video/collect")
    Observable<Bean<List<String>>> home_videoCollect(@FieldMap Map<String, String> map);

    /**
     * 首页视频取消收藏
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/video/uncollect")
    Observable<Bean<List<String>>> home_videoUncollect(@FieldMap Map<String, String> map);

    /**
     * 图文详情
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/imageText/getImageTextDetail")
    Observable<Bean<Object>> Image_Text_Detail(@FieldMap Map<String, String> map);

    /**
     * 视频详情
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/video/videoDetails")
    Observable<Bean<Object>> video_details(@FieldMap Map<String, String> map);

    /**
     * 图文评论列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/imageText/getImageTextComment")
    Observable<Bean<ImageTextComment>> Image_Text_Comment(@FieldMap Map<String, String> map);

    /**
     * 视频评论列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/video/getVideoComment")
//    Observable<Bean<Object>> get_video_comment(@FieldMap Map<String, String> map);
    Observable<Bean<VideoComment>> get_video_comment(@FieldMap Map<String, String> map);

    /**
     * 举报图文
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/report/reportImage")
    Observable<Bean<List<String>>> reportImage(@FieldMap Map<String, String> map);

    /**
     * 举报視頻
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/report/reportVideo")
    Observable<Bean<List<String>>> reportVideo(@FieldMap Map<String, String> map);

    /**
     * 举报图文类型
     *
     * @return
     */
    @GET("answerApi/report/getReportImageType")
    Observable<Bean<List<ReportMsg>>> get_report_image_type();

    /**
     * 举报视频类型
     *
     * @return
     */
    @GET("answerApi/report/getReportVideoType")
    Observable<Bean<List<ReportMsg>>> reportVideoType();

    /**
     * 删除视频
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/video/deleteVideo")
    Observable<Bean<List<String>>> deleteVideo(@FieldMap Map<String, String> map);

    /**
     * 删除图文
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/imageText/deleteImage")
    Observable<Bean<List<String>>> deleteImage(@FieldMap Map<String, String> map);

    /**
     * 删除草稿
     */
    @FormUrlEncoded
    @POST("answerApi/draft/draftDelete")
    Observable<Bean<DraftDelete>> draftDelete(@FieldMap Map<String, String> map);

    /**
     * 添加图文评论
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/imageText/addComment")
    Observable<Bean<List<String>>> addComment(@FieldMap Map<String, String> map);

    /**
     * 添加视频评论
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/video/addCommentVideo")
    Observable<Bean<List<String>>> addCommentVideo(@FieldMap Map<String, String> map);

    /**
     * 删除图文评论
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/imageText/deleteComment")
    Observable<Bean<List<String>>> deleteComment(@FieldMap Map<String, String> map);

    /**
     * 取消点赞评论
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/imageText/unthumbsComment")
    Observable<Bean<List<String>>> unthumbsComment(@FieldMap Map<String, String> map);

    /**
     * 图文点赞评论
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/imageText/thumbsComment")
    Observable<Bean<List<String>>> thumbsComment(@FieldMap Map<String, String> map);

    /**
     * 增加视频播放次数
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/video/addVideoPlay")
    Observable<Bean<List<String>>> addVideoPlay(@FieldMap Map<String, String> map);

    /**
     * 取消视频点赞评论
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/video/unthumbsComment")
    Observable<Bean<List<String>>> unthumbsVideoComment(@FieldMap Map<String, String> map);

    /**
     * 视频点赞评论
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/video/thumbsCommentVideo")
    Observable<Bean<List<String>>> thumbsCommentVideo(@FieldMap Map<String, String> map);

    /**
     * 删除视频评论
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/video/deleteComment")
    Observable<Bean<List<String>>> deleteVideoComment(@FieldMap Map<String, String> map);

    /**
     * 删除图文评论
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/imageText/deleteComment")
    Observable<Bean<List<String>>> deleteImageComment(@FieldMap Map<String, String> map);

    /**
     * 发现轮播图
     *
     * @return
     */
    @GET("answerApi/find/getBannerList")
    Observable<Bean<List<DiscoverBanner>>> banner_list();

    /**
     * 发现名称 配置
     *
     * @return
     */
    @GET("answerApi/find/getFindName")
    Observable<Bean<List<DiscoverConfiguration>>> find_name();

    /**
     * 发现推荐标签
     *
     * @return
     */
    @GET("answerApi/find/recommendClass")
    Observable<Bean<Object>> recommend_class(@Query("page") String page);

    /**
     * 图文评论回复
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/imageText/getImageTextCommentList")
    Observable<Bean<Object>> image_text_commentList(@FieldMap Map<String, String> map);

    /**
     * 视频评论回复
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/video/getVideoCommentList")
    Observable<Bean<Object>> video_commentList(@FieldMap Map<String, String> map);

    /**
     * 发现详情
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/find/classDetail")
    Observable<Bean<Object>> class_detail(@FieldMap Map<String, String> map);

    /**
     * 标签下的印迹
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/find/getClassImprinting")
    Observable<Bean<Object>> class_imprinting(@FieldMap Map<String, String> map);

    /**
     * 标签下的问题
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/find/classQuestion")
    Observable<Bean<Object>> class_question(@FieldMap Map<String, String> map);

    /**
     * 获取登录者信息
     *
     * @return
     */
    @GET("answerApi/own/getOwnInformation")
    Observable<Bean<Object>> own_information();


    /**
     * 好友主页信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/other/getOtherInformation")
    Observable<Bean<Object>> other_information(@FieldMap Map<String, String> map);

    /**
     * 我发布的印迹
     *
     * @return
     */
    @GET("answerApi/own/ownImprinting")
    Observable<Bean<Object>> own_imprinting(@Query("page") String page);

    /**
     * 我的收藏问答
     *
     * @return
     */
    @GET("answerApi/own/ownCollectAnswer")
    Observable<Bean<Object>> own_collectAnswer(@Query("page") String page);

    /**
     * 我的收藏菜谱
     *
     * @return
     */
    @GET("answerApi/own/ownCollectRecipe")
    Observable<Bean<Object>> own_collectRecipe(@Query("page") String page);
    /**
     * 我的收藏问答
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/other/otherCollectAnswer")
    Observable<Bean<Object>> other_collect_answer(@FieldMap Map<String, String> map);

    /**
     * 我的收藏的印迹
     *
     * @return
     */
    @GET("answerApi/own/ownCollectImprinting")
    Observable<Bean<Object>> own_collect_imprinting(@Query("page") String page);

    /**
     * TA收藏的印迹
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/other/otherCollectImprinting")
    Observable<Bean<Object>> other_collect_imprinting(@FieldMap Map<String, String> map);


    /**
     * TA发布的印迹
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/other/otherImprinting")
    Observable<Bean<Object>> other_imprinting(@FieldMap Map<String, String> map);

    /**
     * 修改wifi状态
     *
     * @return
     */
    @FormUrlEncoded
    @POST("answerApi/own/updateWifi")
    Observable<Bean<List<String>>> update_wifi(@FieldMap Map<String, String> map);

    /**
     * 最新回答
     *
     * @return
     */
    @GET("answerApi/interlocution/getAnswer")
    Observable<Bean<Object>> get_answer_list(@Query("page") String page);

    /**
     * 最新问题
     *
     * @return
     */
    @GET("answerApi/interlocution/getQuestion")
    Observable<Bean<Object>> get_question_list(@Query("page") String page);


    /**
     * 草稿箱印迹
     *
     * @return
     */
    @GET("answerApi/draft/draftList")
    Observable<Bean<Object>> get_draft_list(@Query("page") String page);

}

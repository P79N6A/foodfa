package com.app.cookbook.xinhe.foodfamily.shiyujia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.callback.DialogCallBack;
import com.app.cookbook.xinhe.foodfamily.main.AnswerReplyByIdActivity;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.main.OtherUserActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.CommentLeveTwoData;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.CommentReplyAdapter;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.CommentReply;
import com.app.cookbook.xinhe.foodfamily.util.FormatCurrentData;
import com.app.cookbook.xinhe.foodfamily.util.PopWindowHelper;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.Textutil;
import com.app.cookbook.xinhe.foodfamily.util.Utils_android_P;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

public class CommentVideoReplyActivity extends BaseActivity implements TextView.OnEditorActionListener {


    @BindView(R.id.main_layout)
    RelativeLayout main_layout;
    //返回
    @BindView(R.id.iamge_back)
    ImageView iamge_back;
    //标题
    @BindView(R.id.title_tv)
    TextView title_tv;
    //发布问题者头像
    @BindView(R.id.user_logo)
    ImageView user_logo;
    //发布问题者名称
    @BindView(R.id.user_name)
    TextView user_name;
    //发布问题时间
    @BindView(R.id.tv_time)
    TextView tv_time;
    //删除评论
    @BindView(R.id.delete_btn)
    TextView delete_btn;
    //回复问题
    @BindView(R.id.reply_btn)
    ImageView reply_btn;
    //发布问题者点赞
    @BindView(R.id.zan_number)
    TextView zan_number;
    //发布问题者点赞状态
    @BindView(R.id.zan_state)
    ImageView zan_state;
    //发布问题者问题内容
    @BindView(R.id.answer_content)
    TextView answer_content;
    //发布问题者点赞布局
    @BindView(R.id.zan_alyout)
    LinearLayout zan_alyout;
    //输入框
    @BindView(R.id.et_edit)
    EditText et_edit;
    //发送
    @BindView(R.id.send_reply)
    TextView send_reply;
    //列表
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    //查看详情
    @BindView(R.id.cha_answer)
    TextView cha_answer;
    private LinearLayoutManager linearLayoutManager;
    //回复适配器
    private CommentReplyAdapter commentReplyAdapter;
    //回复数据
    private List<CommentReply.DataBean> dataBeans = new ArrayList<>();

    /**
     * Rxjava
     */
    protected Subscription subscription;
    //图文id
    private String id;
    //评论id
    private String comment_id;
    //
    private String total_id;
    private String is_thumbs;
    //上级评论id
    private String parent_id;
    //上级评论用户id
    private String parent_uid;
    //被回复评论用户名
    private String userName;
    //被回复评论用户id
    private String mainUserId;
    //被回复评论是否被点赞
    private String mainThumbs;
    //页数
    private int PAGE = 1;
    //评论数
    private String Comment_thumbs;
    //某一条评论数据
    private CommentReply.DataBean listTwo;
    //某一条评论id
    private String CommentId;
    //某一条评论UserId
    private String UserId;
    //某一条评论UserName
    private String UserName;
    //某一条评论回复内容
    private String replyContent;
    //判断对列表操作完是否删除集合数据
    private String CommentMsg = "0";
    //判断是否发送
    private boolean isSand = false;
    //跳转类型
    private String type;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            listTwo = dataBeans.get(msg.arg1);
            String is_thumbs = listTwo.getIs_thumbs();
            CommentId = listTwo.getId();
            UserId = listTwo.getUser_data().getId();
            UserName = listTwo.getUser_data().getName();
            parent_id = listTwo.getParent_id();
            parent_uid = listTwo.getUser_id();
            Intent intent;
            Bundle bundle;
            switch (msg.what) {
                case 400:
                    Log.e("123", "     点赞    ");
                    if ("".equals(SharedPreferencesHelper.get(CommentVideoReplyActivity.this, "login_token", ""))) {
                        intent = new Intent(CommentVideoReplyActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        if ("2".equals(is_thumbs)) {
                            //点赞
                            listTwo.setIs_thumbs("1");
                            int comment_number = Integer.valueOf(listTwo.getThumbs());
                            listTwo.setThumbs(String.valueOf(comment_number + 1));
                            commentReplyAdapter.notifyDataSetChanged();
                            requestThumbsComment(id, listTwo.getTotal_id(), "1");
                        } else if ("1".equals(is_thumbs)) {
                            //取消点赞
                            listTwo.setIs_thumbs("2");
                            int comment_number = Integer.valueOf(listTwo.getThumbs());
                            listTwo.setThumbs(String.valueOf(comment_number - 1));
                            commentReplyAdapter.notifyDataSetChanged();
                            requestUnthumbsComment(id, listTwo.getTotal_id(), "1");
                        }
                    }
                    break;
                case 401:
                    replyContent = "回复" + UserName + ":";
                    Log.e("123", "     回复     " + replyContent);
                    et_edit.setHint(replyContent);
                    et_edit.setFocusable(true);
                    et_edit.setFocusableInTouchMode(true);
                    et_edit.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    break;
                case 402:
                    Log.e("123", "            " + listTwo.getUser_id() + "           " + UserId);
                    intent = new Intent(CommentVideoReplyActivity.this, FriendPageActivity.class);
                    intent.putExtra("UserId", UserId);
                    startActivity(intent);
                    break;
                case 403:
                    Log.e("123", "            " + listTwo.getUser_id() + "           " + UserId);
                    if (dataBeans.size() >= 10) {
                        PAGE = PAGE + 1;
                        //填写加载更多的网络请求，一般page++
                        VideoCommentList_Request(String.valueOf(PAGE), "2");
                    }
                    break;
                case 404:
                    if (!listTwo.getUser_data().getId().equals(parent_uid)) {
                        Log.e("123", "  !=    parent_uid  " + parent_uid + "  listTwo.getUser_data().getId()  " + listTwo.getUser_data().getId());
                        intent = new Intent(CommentVideoReplyActivity.this, FriendPageActivity.class);
                        intent.putExtra("UserId", listTwo.getUser_data().getId());
                        startActivity(intent);
                    } else {
                        Log.e("123", "  ==    parent_uid  " + parent_uid + "  listTwo.getUser_data().getId()  " + listTwo.getUser_data().getId());
                        intent = new Intent(CommentVideoReplyActivity.this, FriendPageActivity.class);
                        intent.putExtra("UserId", parent_uid);
                        startActivity(intent);
                    }
                    break;
                case 405:
                    PopWindowHelper.public_tishi_pop(CommentVideoReplyActivity.this, "删除回复", "是否删除该回复？", "否", "是", new DialogCallBack() {
                        @Override
                        public void save() {
                            requestDeleteVideoComment(id, CommentId);
                        }

                        @Override
                        public void cancel() {

                        }
                    });
                    break;
            }
        }
    };


    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_commtent_reply);
        ButterKnife.bind(this);
        String UserBrand = SharedPreferencesHelper.get(getApplicationContext(), "UserBrand", "").toString();
        if (UserBrand.contains("MI") || UserBrand.contains("Xiaomi")) {
            main_layout.setFitsSystemWindows(true);
        } else {
            main_layout.setFitsSystemWindows(false);
        }

        /**动态设置距离状态栏高度*/
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) main_layout.getLayoutParams();
        if (Utils_android_P.hasNotchResult(getApplicationContext())) {//如果有刘海
            lp.setMargins(10, 40 + Utils_android_P.set_add_height(), 10, 0);
        } else if ("Xiaomi".equals(android.os.Build.BRAND)) {//小米手机获取特定型号
            Log.e("小米8的型号", Build.MODEL);
            if (Build.MODEL.equals("MI 8")) {
                lp.setMargins(10, 140, 10, 0);
            } else {
                lp.setMargins(10, 10, 10, 0);
            }
        } else {
            lp.setMargins(10, 70, 10, 0);
        }
        main_layout.setLayoutParams(lp);

        id = getIntent().getStringExtra("id");
        comment_id = getIntent().getStringExtra("comment_id");
        total_id = getIntent().getStringExtra("total_id");
//        mainThumbs = getIntent().getStringExtra("is_thumbs");
        type = getIntent().getStringExtra("type");
        Log.e("123", "  id " + id + "    comment_id   " + comment_id);

//        if ("1".equals(mainThumbs)) {
//            zan_state.setImageResource(R.drawable.icon_answer_collect_on);
//        } else if ("2".equals(mainThumbs)) {
//            zan_state.setImageResource(R.drawable.icon_answer_collect);
//        }

        if (!TextUtils.isEmpty(type)) {
            if ("2".equals(type) || "1".equals(type)) {
                cha_answer.setText("查看印迹");
                cha_answer.setVisibility(View.VISIBLE);
            }
        }

        user_name.setOnClickListener(this);
        iamge_back.setOnClickListener(this);
        zan_alyout.setOnClickListener(this);
        send_reply.setOnClickListener(this);
        user_logo.setOnClickListener(this);
        delete_btn.setOnClickListener(this);
        reply_btn.setOnClickListener(this);
        cha_answer.setOnClickListener(this);
        et_edit.setOnEditorActionListener(this);

        et_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 文字改变时 回掉此方法
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 文字改变之前 回掉此方法
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    send_reply.setTextColor(getResources().getColor(R.color.color_bbbbbb));
                } else {
                    send_reply.setTextColor(getResources().getColor(R.color.bb_black));
                }
                if (s.length() == 140) {
                    Textutil.ToastUtil(CommentVideoReplyActivity.this, "上限140个字");
                }

            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
    }


    private void textDistance() {
        LinearLayout.LayoutParams rlp = (LinearLayout.LayoutParams) zan_number.getLayoutParams();
        String comment_thumbs = Comment_thumbs;
        if (Comment_thumbs.length() == 1) {
            rlp.setMargins(0, 0, 35, 0);
            zan_number.setLayoutParams(rlp);
            zan_number.setText(comment_thumbs);
        } else if (Comment_thumbs.length() == 2) {
            rlp.setMargins(0, 0, 17, 0);
            zan_number.setLayoutParams(rlp);
            zan_number.setText(comment_thumbs);
        } else if (Comment_thumbs.length() == 3) {
            rlp.setMargins(0, 0, 0, 0);
            zan_number.setLayoutParams(rlp);
            zan_number.setText(comment_thumbs);
        } else if (Comment_thumbs.length() == 4) {
            rlp.setMargins(0, 0, 17, 0);
            zan_number.setLayoutParams(rlp);
            comment_thumbs = Comment_thumbs;
            comment_thumbs = comment_thumbs.substring(0, 1);
            zan_number.setText(comment_thumbs + "k");
        } else if (Comment_thumbs.length() == 5) {
            rlp.setMargins(0, 0, 0, 0);
            zan_number.setLayoutParams(rlp);
            comment_thumbs = Comment_thumbs;
            comment_thumbs = comment_thumbs.substring(0, 2);
            zan_number.setText(comment_thumbs + "k");
        }
    }

    @Override
    public void initonResume() {

    }

    @Override
    public void initonPause() {

    }

    @Override
    public void doBusiness(Context mContext) {
        VideoCommentList_Request(String.valueOf(PAGE), "1");
    }

    @Override
    public void widgetClick(View v) {
        Intent intent;
        Bundle bundle;
        InputMethodManager imm;
        switch (v.getId()) {
            case R.id.iamge_back:
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                finish();
                break;
            case R.id.delete_btn:
//                Toast.makeText(this, "删除", Toast.LENGTH_SHORT).show();
                break;
            case R.id.reply_btn:
                replyContent = "回复" + userName + ":";
                et_edit.setHint("回复评论:");
                et_edit.setFocusable(true);
                et_edit.setFocusableInTouchMode(true);
                et_edit.requestFocus();
                parent_id = commentReply.getOriginal_data().getId();
                parent_uid = commentReply.getOriginal_data().getUser_data().getId();
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.cha_answer:
                if ("2".equals(type)) {
                    intent = new Intent(CommentVideoReplyActivity.this, VideoDetailsActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                } else if ("1".equals(type)) {
                    intent = new Intent(CommentVideoReplyActivity.this, ImageTextDetailActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
                break;
            case R.id.user_name:
                // 隐藏软键盘
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                intent = new Intent(CommentVideoReplyActivity.this, FriendPageActivity.class);
                intent.putExtra("UserId", mainUserId);
                startActivity(intent);
                break;
            case R.id.user_logo:
                // 隐藏软键盘
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                intent = new Intent(CommentVideoReplyActivity.this, FriendPageActivity.class);
                intent.putExtra("UserId", mainUserId);
                startActivity(intent);
                break;
            case R.id.zan_alyout:
                if ("".equals(SharedPreferencesHelper.get(CommentVideoReplyActivity.this, "login_token", ""))) {
                    intent = new Intent(CommentVideoReplyActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if ("2".equals(mainThumbs)) {
                        //点赞
                        requestThumbsComment(id, total_id, "2");
                    } else if ("1".equals(mainThumbs)) {
                        //取消点赞
                        requestUnthumbsComment(id, total_id, "2");
                    }
                }
                break;
            case R.id.send_reply:
                if ("".equals(SharedPreferencesHelper.get(CommentVideoReplyActivity.this, "login_token", ""))) {
                    intent = new Intent(CommentVideoReplyActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (isSand == false) {
                        if (!TextUtils.isEmpty(et_edit.getText().toString())) {
                            String content = et_edit.getText().toString();
                            isSand = true;
                            requestAddComment(id, parent_id, parent_uid, content);
                        } else {
                            Toast.makeText(this, "请输入评论内容", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }

    /**
     * 图文回复评论列表
     * 1.2正序2.1倒序
     */
    private int listSize;
    private CommentReply commentReply;

    private void VideoCommentList_Request(String page, String type) {
        Map<String, String> params = new HashMap<>();
        params.put("video_id", id);
        params.put("comment_id", comment_id);
        params.put("page", page);
        params.put("sort", "1");
        Log.e("123", "回复评论列表" + params.toString());
        subscription = Network.getInstance("回复评论列表", this)
                .getVideoCommentList(params,
                        new ProgressSubscriberNew<>(CommentReply.class, new GsonSubscriberOnNextListener<CommentReply>() {
                            @Override
                            public void on_post_entity(CommentReply result) {
                                listSize = result.getData().size();
                                commentReply = result;
                                Log.e("123", "     回复评论列表      " + result + "   listSize  " + listSize);
                                parent_id = result.getOriginal_data().getId();
                                parent_uid = result.getOriginal_data().getUser_data().getId();
                                mainUserId = result.getOriginal_data().getUser_id();
                                Comment_thumbs = result.getOriginal_data().getThumbs();
                                userName = result.getOriginal_data().getUser_data().getName();
                                total_id = result.getOriginal_data().getTotal_id();
                                mainThumbs = result.getOriginal_data().getIs_thumbs();
                                if ("1".equals(mainThumbs)) {
                                    zan_state.setImageResource(R.drawable.icon_answer_collect_on);
                                } else if ("2".equals(mainThumbs)) {
                                    zan_state.setImageResource(R.drawable.icon_answer_collect);
                                }

                                et_edit.setHint("回复评论:");
                                user_name.setText(userName);
                                title_tv.setText(result.getTotal() + "条回复");
                                tv_time.setText(FormatCurrentData.getTimeRange(Long.valueOf(result.getOriginal_data().getCreated_at())));
                                answer_content.setText(result.getOriginal_data().getContent());

                                Glide.with(CommentVideoReplyActivity.this)
                                        .load(result.getOriginal_data().getUser_data().getAvatar())
                                        .error(R.drawable.touxiang)
                                        .into(user_logo);
                                if (!"0".equals(Comment_thumbs)) {
                                    textDistance();
                                }
                                if ("1".equals(CommentMsg)) {
                                    if (dataBeans.size() > 0) {
                                        dataBeans.clear();
                                    }
                                    CommentMsg = "0";
                                }
//                                for (CommentReply.DataBean data : result.getData()) {
//                                    dataBeans.add(data);
//                                }

                                if (PAGE == 1) {
                                    Collections.reverse(result.getData());
                                    for (CommentReply.DataBean data : result.getData()) {
                                        dataBeans.add(data);
                                    }
                                } else if (PAGE > 1) {
                                    for (CommentReply.DataBean data : result.getData()) {
                                        dataBeans.add(0, data);
                                    }
                                }
                                isSand = false;
                                set_list_resource(dataBeans, type);
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("回复评论列表错误:   ", error);
                            }
                        }, this, true));
    }


    //添加评论
    private void requestAddComment(String video_image_id, String parent_id, String
            parent_uid, String content) {
        Map<String, String> params = new HashMap<>();
        params.put("video_image_id", video_image_id);
        params.put("parent_id", parent_id);
        params.put("parent_uid", parent_uid);
        params.put("content", content);
        Log.e("123", "添加评论：" + params.toString());
        subscription = Network.getInstance("添加评论", this)
                .getAddCommentVideo(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "添加评论：" + result.getCode());
                                CommentMsg = "1";
                                et_edit.setText("");
                                VideoCommentList_Request("1", "1");
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Log.e("123", "添加评论报错：" + error);
                            }
                        }, this, false));
    }

    //取消点赞评论
    private void requestUnthumbsComment(String video_image_id, String comment_id,
                                        final String types) {
        Map<String, String> params = new HashMap<>();
        params.put("video_image_id", video_image_id);
        params.put("comment_id", comment_id);
        Log.e("123", "取消点赞评论：" + params.toString());
        subscription = Network.getInstance("取消点赞评论", this)
                .getUnthumbsVideoComment(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "取消点赞评论：" + result.getCode());
                                if ("1".equals(types)) {
//                                    listTwo.setIs_thumbs("2");
//                                    int comment_number = Integer.valueOf(listTwo.getThumbs());
//                                    listTwo.setThumbs(String.valueOf(comment_number - 1));
                                    commentReplyAdapter.notifyDataSetChanged();
                                } else if ("2".equals(types)) {
                                    mainThumbs = "2";
                                    Comment_thumbs = String.valueOf(Integer.valueOf(Comment_thumbs) - 1);
                                    if (!"0".equals(Comment_thumbs)) {
                                        zan_number.setText(Comment_thumbs);
                                    } else {
                                        zan_number.setText("0");
                                    }
                                    textDistance();
                                    zan_state.setImageResource(R.drawable.icon_answer_collect);
                                }
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
//                                Log.e("123", "取消点赞评论报错：" + error);
                            }
                        }, this, false));
    }

    //点赞评论
    private void requestThumbsComment(String video_image_id, String comment_id,
                                      final String types) {
        Map<String, String> params = new HashMap<>();
        params.put("video_image_id", video_image_id);
        params.put("comment_id", comment_id);
        Log.e("123", "点赞评论：" + params.toString());
        subscription = Network.getInstance("点赞评论", this)
                .getThumbsCommentVideo(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "点赞评论：" + result.getCode());
                                if ("1".equals(types)) {
//                                    listTwo.setIs_thumbs("1");
//                                    int comment_number = Integer.valueOf(listTwo.getThumbs());
//                                    listTwo.setThumbs(String.valueOf(comment_number + 1));
                                    commentReplyAdapter.notifyDataSetChanged();
                                } else if ("2".equals(types)) {
//                                    Comment_thumbs = String.valueOf(Comment_thumbs + 1);
                                    Comment_thumbs = String.valueOf(Integer.valueOf(Comment_thumbs) + 1);
                                    zan_number.setText(Comment_thumbs);
                                    textDistance();
                                    zan_state.setImageResource(R.drawable.icon_answer_collect_on);
                                    mainThumbs = "1";
                                }
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Log.e("123", "点赞评论报错：" + error);
                            }
                        }, this, false));
    }

    //删除回复
    private void requestDeleteVideoComment(String video_image_id, String comment_id) {
        Map<String, String> params = new HashMap<>();
        params.put("video_image_id", video_image_id);
        params.put("comment_id", comment_id);
        Log.e("123", "删除评论：" + params.toString());
        subscription = Network.getInstance("删除评论", this)
                .getDeleteVideoComment(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "删除评论：" + result.getCode());
                                CommentMsg = "1";
                                VideoCommentList_Request("1", "2");
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Log.e("123", "删除评论报错：" + error);
                            }
                        }, this, false));
    }


    private boolean ista = false;
    private int size = 0;

    private void set_list_resource(final List<CommentReply.DataBean> dates, String type) {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemAnimator(null);
        commentReplyAdapter = new CommentReplyAdapter(this, handler, ista);
        commentReplyAdapter.setCommentLeveTwos(dates);
        if (listSize >= 10) {
            commentReplyAdapter.isMore("1");
        } else if (listSize < 10) {
            commentReplyAdapter.isMore("2");
        }
        recycler_view.setAdapter(commentReplyAdapter);
        if (PAGE == 1) {
            recycler_view.scrollToPosition(dates.size() - 1);
        } else {
            if (listSize >= 1) {
                size = (dates.size() - (listSize - 1));
            }
            recycler_view.scrollToPosition(dates.size() - size);
        }
        commentReplyAdapter.notifyDataSetChanged();
        if (!"2".equals(type)) {
            et_edit.setFocusable(true);
            et_edit.setFocusableInTouchMode(true);
            et_edit.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 返回按键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (!TextUtils.isEmpty(Comment_thumbs) && !TextUtils.isEmpty(isthumbs)) {
//                Intent intent = new Intent();
//                intent.putExtra("Comment_thumbs", Comment_thumbs);
//                intent.putExtra("isthumbs", isthumbs);
//                setResult(100, intent);
//            }
            InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm2.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            finish();
        }
        return true;
    }


}

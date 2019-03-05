package com.app.cookbook.xinhe.foodfamily.main;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.callback.DialogCallBack;
import com.app.cookbook.xinhe.foodfamily.main.adapter.CommentLeveTwoAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.CommentLeveTwo;
import com.app.cookbook.xinhe.foodfamily.main.entity.CommentLeveTwoData;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
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

public class AnswerReplyActivity extends BaseActivity {

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
    TextView reply_btn;
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
    RelativeLayout zan_alyout;
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
    /**
     * Rxjava
     */
    protected Subscription subscription;
    private String original_id;
    //问题id
    private String answer_id;
    //页数
    private int PAGE = 1;
    //二级列表适配器
    private CommentLeveTwoAdapter commentLeveTwoAdapter;
    //二级列表集合
    private List<CommentLeveTwoData> commentLeveTwos = new ArrayList<>();
    //
    private CommentLeveTwoData listTwo;
    //评论数
    private String Comment_thumbs;
    //回复者评论id
    private String comment_id;
    //回复者id
    private String user_id;
    private String userName = " ";
    private String image;
    private String name;
    private String content;
    private String time;
    private String isthumbs;
    private String userId;
    private String replyContent = "";
    private String sendType = "0";
    private String type;
    //判断是是否消息评论页跳转
    private String mesComm;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            listTwo = commentLeveTwos.get(msg.arg1);
            String is_thumbs = listTwo.getIs_thumbs();
            comment_id = listTwo.getComment_id();
            user_id = listTwo.getUser_id();
            userName = listTwo.getUser_name();
            Intent intent;
            Bundle bundle;
            switch (msg.what) {
                case 400:
                    Log.e("123", "     点赞    ");
                    if ("".equals(SharedPreferencesHelper.get(AnswerReplyActivity.this, "login_token", ""))) {
                        intent = new Intent(AnswerReplyActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        if ("2".equals(is_thumbs)) {
                            //点赞
                            cthumbs_comment(answer_id, comment_id, "1");
                        } else if ("1".equals(is_thumbs)) {
                            //取消点赞
                            un_cthumbs_comment(answer_id, comment_id, "1");
                        }
                    }
                    break;
                case 401:
                    replyContent = "回复" + userName + ":";
                    Log.e("123", "     回复     " + replyContent);
                    et_edit.setHint(replyContent);
                    et_edit.setFocusable(true);
                    et_edit.setFocusableInTouchMode(true);
                    et_edit.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    break;
                case 402:
                    Log.e("123", "            " + listTwo.getUser_id() + "           " + userId);
                    intent = new Intent(AnswerReplyActivity.this, FriendPageActivity.class);
                    intent.putExtra("UserId", user_id);
                    startActivity(intent);
                    break;
                case 403:
                    Log.e("123", "            " + listTwo.getUser_id() + "           " + userId);
                    if (commentLeveTwos.size() >= 20) {
                        PAGE = PAGE + 1;
                        //填写加载更多的网络请求，一般page++
                        init_comment_two(answer_id, original_id, String.valueOf(PAGE), "2");
                    }
                    break;
                case 404:
                        if (!listTwo.getParent_user_name().equals(name)) {
                            intent = new Intent(AnswerReplyActivity.this, FriendPageActivity.class);
                            intent.putExtra("UserId", listTwo.getParent_user_id());
                            startActivity(intent);
                        } else {
                            intent = new Intent(AnswerReplyActivity.this, FriendPageActivity.class);
                            intent.putExtra("UserId", userId);
                            startActivity(intent);
                        }
                    break;
                case 405:
                    PopWindowHelper.public_tishi_pop(AnswerReplyActivity.this, "删除回复", "是否删除该回复？", "否", "是", new DialogCallBack() {
                        @Override
                        public void save() {
                            delete_daan_submit(comment_id);

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
        setContentView(R.layout.activity_answer_reply);
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
                lp.setMargins(10, 70, 10, 0);
            }
        } else {
            lp.setMargins(10, 70, 10, 0);
        }
        main_layout.setLayoutParams(lp);

        answer_id = getIntent().getStringExtra("answer_id");
        original_id = getIntent().getStringExtra("original_id");
        image = getIntent().getStringExtra("image");
        name = getIntent().getStringExtra("name");
        content = getIntent().getStringExtra("content");
        time = getIntent().getStringExtra("time");
        isthumbs = getIntent().getStringExtra("isthumbs");
        userId = getIntent().getStringExtra("user_id");
        Comment_thumbs = getIntent().getStringExtra("thumbs");
        type = getIntent().getStringExtra("type");
        mesComm = getIntent().getStringExtra("mesComm");
        String isCheck = getIntent().getStringExtra("isCheck");
        Log.e("123", "     answer_id    " + answer_id + "        original_id       " + original_id);
        if ("2".equals(isCheck)) {
            cha_answer.setVisibility(View.VISIBLE);
        } else {
            cha_answer.setVisibility(View.GONE);
        }

        et_edit.setHint("回复评论:");
        user_name.setText(name);
        title_tv.setText("0条回复");
        tv_time.setText(FormatCurrentData.getTimeRange(Long.valueOf(time)));
        answer_content.setText(content);

        textDistance();

        if ("1".equals(isthumbs)) {
            zan_state.setImageResource(R.drawable.icon_answer_collect_on);
        } else if ("2".equals(isthumbs)) {
            zan_state.setImageResource(R.drawable.icon_answer_collect);
        }
        if (commentLeveTwos.size() > 0) {
            commentLeveTwos.clear();
        }

        Glide.with(this)
                .load(image)
                .error(R.drawable.touxiang)
                .into(user_logo);

        user_name.setOnClickListener(this);
        iamge_back.setOnClickListener(this);
        zan_alyout.setOnClickListener(this);
        send_reply.setOnClickListener(this);
        user_logo.setOnClickListener(this);
        delete_btn.setOnClickListener(this);
        reply_btn.setOnClickListener(this);
        cha_answer.setOnClickListener(this);

        et_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });

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
                // 文字改变之后 回掉此方法
                if (TextUtils.isEmpty(s)) {
                    send_reply.setTextColor(getResources().getColor(R.color.color_bbbbbb));
                } else {
                    sendType = "0";
                    send_reply.setTextColor(getResources().getColor(R.color.bb_black));
                }
                if (s.length() == 140) {
                    Textutil.ToastUtil(AnswerReplyActivity.this, "上限140个字");
                }
            }
        });
    }

    private void textDistance() {
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) zan_number.getLayoutParams();
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
        if ("1".equals(mesComm)) {
            init_comment_two(answer_id, original_id, String.valueOf(PAGE), "2");
        } else if ("2".equals(mesComm)) {
            init_comment_ById(answer_id, original_id, String.valueOf(PAGE), "2");
        }
    }


    @Override
    public void widgetClick(View v) {
        Intent intent;
        Bundle bundle;
        InputMethodManager imm;
        switch (v.getId()) {
            case R.id.iamge_back:
                if (!TextUtils.isEmpty(Comment_thumbs) && !TextUtils.isEmpty(isthumbs)) {
                    intent = new Intent();
                    intent.putExtra("Comment_thumbs", Comment_thumbs);
                    intent.putExtra("isthumbs", isthumbs);
                    setResult(100, intent);
                }
                finish();
                break;
            case R.id.delete_btn:
                Toast.makeText(this, "删除", Toast.LENGTH_SHORT).show();
                break;
            case R.id.reply_btn:
                replyContent = "回复" + name + ":";
                et_edit.setHint("回复评论:");
                et_edit.setFocusable(true);
                et_edit.setFocusableInTouchMode(true);
                et_edit.requestFocus();
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.cha_answer:
                intent = new Intent(AnswerReplyActivity.this, AnserActivity.class);
                bundle = new Bundle();
                bundle.putString("answer_id", answer_id);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.user_name:
                // 隐藏软键盘
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                intent = new Intent(AnswerReplyActivity.this, FriendPageActivity.class);
                intent.putExtra("UserId", userId);
                startActivity(intent);
                break;
            case R.id.user_logo:
                // 隐藏软键盘
                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                intent = new Intent(AnswerReplyActivity.this, FriendPageActivity.class);
                intent.putExtra("UserId", userId);
                startActivity(intent);
                break;
            case R.id.zan_alyout:
                if ("".equals(SharedPreferencesHelper.get(AnswerReplyActivity.this, "login_token", ""))) {
                    intent = new Intent(AnswerReplyActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if ("2".equals(isthumbs)) {
                        //点赞
                        cthumbs_comment(answer_id, original_id, "2");
                    } else if ("1".equals(isthumbs)) {
                        //取消点赞
                        un_cthumbs_comment(answer_id, original_id, "2");
                    }
                }
                break;
            case R.id.send_reply:
                if ("".equals(SharedPreferencesHelper.get(AnswerReplyActivity.this, "login_token", ""))) {
                    intent = new Intent(AnswerReplyActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if ("0".equals(sendType)) {
                        if (replyContent != null && userName != null && replyContent.contains(userName)) {
                            String str = et_edit.getText().toString();
                            if (!TextUtils.isEmpty(str)) {
                                String content = str.replace(replyContent, "");
                                init_add_comment(answer_id, content, original_id, comment_id, user_id);
                            } else {
                                Toast.makeText(this, "您还没有填写回答内容", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String content = et_edit.getText().toString();
                            if (!TextUtils.isEmpty(content)) {
                                init_add_comment(answer_id, content, original_id, original_id, userId);
                            } else {
                                Toast.makeText(this, "您还没有填写回答内容", Toast.LENGTH_SHORT).show();
                            }
                        }
                        sendType = "1";
                    }
                }
                break;
        }
    }

    /**
     * 二级评论列表
     * 1.1正序2.2倒序
     */
    private int listSize;

    private void init_comment_two(String answer_id, String original_id, String page, String sort) {
        Map<String, String> params = new HashMap<>();
        params.put("answer_id", answer_id);
        params.put("original_id", original_id);
        params.put("page", page);
        params.put("sort", sort);
        Log.e("二级评论列表", params.toString());
        subscription = Network.getInstance("二级评论列表", this)
                .comment_level_two_request(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<CommentLeveTwo>>() {
                            @Override
                            public void onNext(Bean<CommentLeveTwo> result) {
                                Log.e("123", "      二级评论列表         " + result.getMsg() + "         " + result.getData().getData().size());
                                listSize = result.getData().getData().size();
                                title_tv.setText(result.getData().getTotal() + "条回复");
                                if ("2".equals(Commtype)) {
                                    if (commentLeveTwos.size() > 0) {
                                        commentLeveTwos.clear();
                                    }
                                }
                                if (PAGE == 1) {
                                    Collections.reverse(result.getData().getData());
                                    for (CommentLeveTwoData data : result.getData().getData()) {
                                        commentLeveTwos.add(data);
                                    }
                                } else if (PAGE > 1) {
                                    for (CommentLeveTwoData data : result.getData().getData()) {
                                        commentLeveTwos.add(0, data);
                                    }
                                }
                                set_list_resource(commentLeveTwos);
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("错误: -----   ", error);
                            }
                        }, this, false));
    }

    //获取二轮评论列表
    private void init_comment_ById(String answer_id, String comment_id, String page, String sort) {
        Map<String, String> params = new HashMap<>();
        params.put("answer_id", answer_id);
        params.put("comment_id", comment_id);
        params.put("page", page);
        params.put("sort", sort);
        Log.e("根据评论id 获取二轮评论列表页", params.toString());
        subscription = Network.getInstance("根据评论id获取二轮评论列表页", this)
                .get_byId_request(params,
                        new ProgressSubscriberNew<>(CommentLeveTwo.class, new GsonSubscriberOnNextListener<CommentLeveTwo>() {
                            @Override
                            public void on_post_entity(CommentLeveTwo result) {
                                listSize = result.getData().size();
                                title_tv.setText(result.getTotal() + "条回复");
                                if (commentLeveTwos.size() > 0) {
                                    commentLeveTwos.clear();
                                }
                                if (PAGE == 1) {
                                    Collections.reverse(result.getData());
                                    for (CommentLeveTwoData data : result.getData()) {
                                        commentLeveTwos.add(data);
                                    }
                                } else if (PAGE > 1) {
                                    for (CommentLeveTwoData data : result.getData()) {
                                        commentLeveTwos.add(0, data);
                                    }
                                }
                                set_list_resource(commentLeveTwos);
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Logger.e("错误：" + error);
                            }
                        }, this, false));
    }

    /**
     * 评论点赞
     */
    private void cthumbs_comment(String answer_id, String comment_id, final String types) {
        Map<String, String> params = new HashMap<>();
        params.put("answer_id", answer_id);
        params.put("comment_id", comment_id);
        Log.e("评论点赞", params.toString());
        subscription = Network.getInstance("评论点赞", this)
                .cthumbs_comment_request(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "      评论点赞         " + result.getMsg());
                                if ("1".equals(types)) {
                                    listTwo.setIs_thumbs("1");
                                    int comment_number = Integer.valueOf(listTwo.getComment_thumbs());
                                    listTwo.setComment_thumbs(String.valueOf(comment_number + 1));
                                    commentLeveTwoAdapter.notifyDataSetChanged();
                                } else if ("2".equals(types)) {
//                                    Comment_thumbs = String.valueOf(Comment_thumbs + 1);
                                    Comment_thumbs = String.valueOf(Integer.valueOf(Comment_thumbs) + 1);
//                                    zan_number.setText(Comment_thumbs);
                                    textDistance();
                                    zan_state.setImageResource(R.drawable.icon_answer_collect_on);
                                    isthumbs = "1";
                                }
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("错误:    ", error);
                            }
                        }, this, false));
    }

    /**
     * 取消评论点赞
     */
    private void un_cthumbs_comment(String answer_id, String comment_id, final String types) {
        Map<String, String> params = new HashMap<>();
        params.put("answer_id", answer_id);
        params.put("comment_id", comment_id);
        Log.e("取消评论点赞", params.toString());
        subscription = Network.getInstance("取消评论点赞", this)
                .un_thumbs_comment_request(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "      取消评论点赞         " + result.getMsg());
                                if ("1".equals(result.getCode())) {
                                    if ("1".equals(types)) {
                                        listTwo.setIs_thumbs("2");
                                        int comment_number = Integer.valueOf(listTwo.getComment_thumbs());
                                        listTwo.setComment_thumbs(String.valueOf(comment_number - 1));
                                        commentLeveTwoAdapter.notifyDataSetChanged();
                                    } else if ("2".equals(types)) {
                                        isthumbs = "2";
                                        Comment_thumbs = String.valueOf(Integer.valueOf(Comment_thumbs) - 1);
//                                        zan_number.setText(Comment_thumbs);
                                        textDistance();
                                        zan_state.setImageResource(R.drawable.icon_answer_collect);
                                    }
                                }
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("错误:    ", error);
                            }
                        }, this, false));
    }

    /**
     * 添加评论
     */
    private String Commtype = "1";

    private void init_add_comment(String answerId, String content, String originalId, String parent_id, String parent_uid) {
        Map<String, String> params = new HashMap<>();
        params.put("answer_id", answerId);
        params.put("content", content);
        params.put("original_id", originalId);
        params.put("parent_id", parent_id);
        params.put("parent_uid", parent_uid);
        Log.e("添加评论", params.toString());
        subscription = Network.getInstance("添加评论", this)
                .add_comment_request(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "      添加评论         " + result.getMsg() + result.getCode());
                                Toast.makeText(AnswerReplyActivity.this, "" + result.getMsg(), Toast.LENGTH_SHORT).show();
                                et_edit.setText("");
                                // 隐藏软键盘
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                                PAGE = 1;
                                Commtype = "2";
                                init_comment_two(answer_id, original_id, String.valueOf(PAGE), "2");
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("错误:    ", error);
                                // 隐藏软键盘
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                            }
                        }, this, false));
    }

    //删除回复
    private void delete_daan_submit(String comment_id) {
        Map<String, String> params = new HashMap<>();
        params.put("answer_id", answer_id);
        params.put("comment_id", comment_id);
        Log.e("删除回复：", params.toString());
        subscription = Network.getInstance("删除回复", this)
                .delete_comment_request(params,
                        new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                            @Override
                            public void on_post_entity(List mlocation) {
                                Log.e("123", "删除回复成功：");
                                PAGE = 1;
                                Commtype = "2";
                                init_comment_two(answer_id, original_id, String.valueOf(PAGE), "2");
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "删除回复报错：" + error);
                                Toast.makeText(AnswerReplyActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }, this, false));
    }


    private boolean ista = false;
    private int size = 0;

    private void set_list_resource(final List<CommentLeveTwoData> dates) {

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemAnimator(null);
        commentLeveTwoAdapter = new CommentLeveTwoAdapter(this, handler, ista, name);
        commentLeveTwoAdapter.setCommentLeveTwos(dates);
        if (listSize >= 20) {
            commentLeveTwoAdapter.isMore("1");
        } else if (listSize < 20) {
            commentLeveTwoAdapter.isMore("2");
        }
        recycler_view.setAdapter(commentLeveTwoAdapter);
        if (PAGE == 1) {
            recycler_view.scrollToPosition(dates.size() - 1);
        } else {
            if (listSize >= 1) {
                size = (dates.size() - (listSize - 1));
            }
            recycler_view.scrollToPosition(dates.size() - size);
        }
        commentLeveTwoAdapter.notifyDataSetChanged();

        if (type != null && "1".equals(type)) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            et_edit.setFocusable(true);
            et_edit.setFocusableInTouchMode(true);
            et_edit.requestFocus();
            type = "0";
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 返回按键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!TextUtils.isEmpty(Comment_thumbs) && !TextUtils.isEmpty(isthumbs)) {
                Intent intent = new Intent();
                intent.putExtra("Comment_thumbs", Comment_thumbs);
                intent.putExtra("isthumbs", isthumbs);
                setResult(100, intent);
            }
            finish();
        }
        return true;
    }


}

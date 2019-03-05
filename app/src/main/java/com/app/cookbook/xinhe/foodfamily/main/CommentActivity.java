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

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.main.adapter.CommentAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.AnswerListOne;
import com.app.cookbook.xinhe.foodfamily.main.entity.AnswerListOneData;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.PersonalHomepageActivity;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.Textutil;
import com.app.cookbook.xinhe.foodfamily.util.Utils_android_P;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentActivity extends BaseActivity {


    @BindView(R.id.main_layout)
    RelativeLayout main_layout;
    @BindView(R.id.iamge_back)
    ImageView iamge_back;
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.cha_answer)
    TextView cha_answer;
    @BindView(R.id.title_tv_number)
    TextView title_tv_number;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.et_edit)
    EditText et_edit;
    @BindView(R.id.send_reply)
    TextView send_reply;
    @BindView(R.id.rl_root)
    RelativeLayout mRootView;
    @BindView(R.id.edit_alyout)
    RelativeLayout edit_alyout;


    private CommentAdapter commentAdapter;

    private List<AnswerListOne> answerListOnes = new ArrayList<>();
    private AnswerListOne listOne;

    private LinearLayoutManager linearLayoutManager;

    private String answer_id;
    //页数
    private int PAGE = 1;
    //是否有更多
    private boolean is_not_more;
    //评论id
    private String comment_id;
    //回复者id
    private String user_id;
    //列表点赞状态
    private String is_thumbs;
    //评论数
    private String Comment_thumbs;
    private String sendType = "0";


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            listOne = answerListOnes.get(msg.arg1);
            is_thumbs = listOne.getIs_thumbs();
            comment_id = listOne.getComment_id();
            user_id = listOne.getUser_id();
            Comment_thumbs = listOne.getComment_thumbs();
            Intent intent;
            Bundle bundle;
            switch (msg.what) {
                case 400:
                    Log.e("123", "             " + is_thumbs + "             " + answer_id + "          " + comment_id);
                    if ("".equals(SharedPreferencesHelper.get(CommentActivity.this, "login_token", ""))) {
                        intent = new Intent(CommentActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        if ("2".equals(is_thumbs)) {
                            //点赞
                            cthumbs_comment(answer_id, comment_id);
                        } else if ("1".equals(is_thumbs)) {
                            //取消点赞按
                            un_cthumbs_comment(answer_id, comment_id);
                        }
                    }
                    break;
                case 401:
                    Log.e("123", "  ---answer_id          " + answer_id + "    ---- comment_id       " + comment_id);
                    intent = new Intent(CommentActivity.this, AnswerReplyActivity.class);
                    intent.putExtra("answer_id", answer_id);
                    intent.putExtra("original_id", comment_id);
                    intent.putExtra("image", listOne.getUser_avatar());
                    intent.putExtra("name", listOne.getUser_name());
                    intent.putExtra("content", listOne.getComment_content());
                    intent.putExtra("time", listOne.getCreated_at());
                    intent.putExtra("thumbs", listOne.getComment_thumbs());
                    intent.putExtra("isthumbs", listOne.getIs_thumbs());
                    intent.putExtra("user_id", listOne.getUser_id());
                    intent.putExtra("type", "1");
                    intent.putExtra("mesComm", "1");
                    intent.putExtra("isCheck", "1");
                    startActivityForResult(intent, 100);
                    break;
                case 402:
                    intent = new Intent(CommentActivity.this, FriendPageActivity.class);
                    intent.putExtra("UserId", user_id);
                    startActivity(intent);
                    break;
                case 403:
                    delete_daan_submit(comment_id);
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
        setContentView(R.layout.activity_comment);
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
                lp.setMargins(10, 40, 10, 0);
            }
        } else {
            lp.setMargins(10, 70, 10, 0);
        }
        main_layout.setLayoutParams(lp);

        answer_id = getIntent().getStringExtra("answer_id");
        String type = getIntent().getStringExtra("type");

        et_edit.setHint("写评论:");
        title_tv.setText("评论");
        title_tv_number.setText("(0)");
        if ("2".equals(type)) {
            cha_answer.setVisibility(View.GONE);
        } else {
            cha_answer.setVisibility(View.GONE);
        }

        iamge_back.setOnClickListener(this);
        send_reply.setOnClickListener(this);
        if (answerListOnes.size() > 0) {
            answerListOnes.clear();
        }

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
                    Textutil.ToastUtil(CommentActivity.this, "上限140个字");
                }
            }
        });

        et_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });

        cha_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommentActivity.this, AnserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("answer_id", answer_id);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initonResume() {
//        init_one_comment(answer_id);
    }

    @Override
    public void initonPause() {

    }

    @Override
    public void doBusiness(Context mContext) {
        init_one_comment(answer_id);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.iamge_back:
//                if (!TextUtils.isEmpty(Comment_thumbs) && !TextUtils.isEmpty(is_thumbs)) {
//                    Intent intent = new Intent();
//                    intent.putExtra("Comment_thumbs", Comment_thumbs);
//                    intent.putExtra("isthumbs", is_thumbs);
//                    setResult(100, intent);
//                }
                finish();
                break;
            case R.id.send_reply:
                if ("".equals(SharedPreferencesHelper.get(CommentActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(CommentActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    String content = et_edit.getText().toString();
                    if ("0".equals(sendType)) {
                        if (!TextUtils.isEmpty(content)) {
                            init_add_comment(answer_id, content, "", "", "");
                        } else {
                            Toast.makeText(this, "您还没有填写回答内容", Toast.LENGTH_SHORT).show();
                        }
                        sendType = "1";
                    }
                }
                break;
        }
    }

    /**
     * 一级评论列表
     */
    private void init_one_comment(String answer_id) {
        Map<String, String> params = new HashMap<>();
        params.put("answer_id", answer_id);
        params.put("page", String.valueOf(PAGE));
        Log.e("获取提问数据参数", params.toString());
        subscription = Network.getInstance("一级评论列表", this)
                .comment_level_one_request(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<AnswerListOneData>>() {
                            @Override
                            public void onNext(Bean<AnswerListOneData> result) {
                                Log.e("123", "      一级评论列表         " + result.getMsg());
//                                Log.e("123", "     " + result.getData().getData().size());
                                String Comment_count = result.getData().getComment_count();
                                title_tv.setText("评论");
                                title_tv_number.setText("(" + Comment_count + ")");
                                if (isComm == 1) {
                                    if (answerListOnes.size() > 0) {
                                        Log.e("123", "         answerListOnes.size()       " + answerListOnes.size());
                                        answerListOnes.clear();
                                    }
                                    isComm = 0;
                                }
                                Log.e("123", "               " + result.getData().getData().size());
                                if (answerListOnes.size() > 0 && PAGE > 1) {//这表示是"加载"
                                    if (result.getData().getData().size() == 0) {
                                        //表示没有更多数据了
                                        is_not_more = true;
                                        answerListOnes.addAll(result.getData().getData());
                                        commentAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        answerListOnes.addAll(result.getData().getData());
                                        commentAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    answerListOnes = result.getData().getData();
                                    set_list_resource(answerListOnes);
                                }
                            }

                            @Override
                            public void onError(String error) {
                                if (isComm == 1) {
                                    if (answerListOnes.size() > 0) {
                                        answerListOnes.clear();
                                    }
                                    title_tv_number.setText("(0)");
                                    set_list_resource(answerListOnes);
                                } else {
                                    if (answerListOnes.size() == 0) {
                                        set_list_resource(answerListOnes);
                                    }
                                }
                                Log.e("错误:   ", error);

                            }
                        }, this, false));
    }

    /**
     * 评论点赞
     */

    private void cthumbs_comment(String answer_id, String comment_id) {
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
//                                Log.e("123", "     " + result.getData().getData().size());
                                if ("1".equals(result.getCode())) {
                                    listOne.setIs_thumbs("1");
                                    int comment_number = Integer.valueOf(listOne.getComment_thumbs());
                                    listOne.setComment_thumbs(String.valueOf(comment_number + 1));
                                    commentAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("错误: ", error);
                            }
                        }, this, false));
    }

    /**
     * 取消评论点赞
     */

    private void un_cthumbs_comment(String answer_id, String comment_id) {
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
//                                Log.e("123", "     " + result.getData().getData().size());
                                if ("1".equals(result.getCode())) {
                                    listOne.setIs_thumbs("2");
                                    int comment_number = Integer.valueOf(listOne.getComment_thumbs());
                                    listOne.setComment_thumbs(String.valueOf(comment_number - 1));
                                    commentAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("错误:  ", error);
                            }
                        }, this, false));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String Comment_thumbs = data.getExtras().getString("Comment_thumbs");
            String isthumbs = data.getExtras().getString("isthumbs");
            listOne.setIs_thumbs(isthumbs);
            listOne.setComment_thumbs(Comment_thumbs);
            commentAdapter.notifyDataSetChanged();
            Log.e("123", "    isthumbs   " + isthumbs + "         " + Comment_thumbs + "       " + listOne.getIs_thumbs() + "    " + listOne.getComment_thumbs());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 添加评论
     */
    private int isComm = 0;

    private void init_add_comment(final String answer_id, String content, String original_id, String parent_id, String parent_uid) {
        Map<String, String> params = new HashMap<>();
        params.put("answer_id", answer_id);
        params.put("content", content);
        params.put("original_id", original_id);
        params.put("parent_id", parent_id);
        params.put("parent_uid", parent_uid);
        Log.e("添加评论", params.toString());
        subscription = Network.getInstance("添加评论", this)
                .add_comment_request(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "      添加评论         " + result.getMsg());
                                Toast.makeText(CommentActivity.this, "" + result.getMsg(), Toast.LENGTH_SHORT).show();
                                et_edit.setText("");
                                // 隐藏软键盘
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                                et_edit.setHint("写评论:");
                                isComm = 1;
                                init_one_comment(answer_id);
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
        Log.e("删除評論：", params.toString());
        subscription = Network.getInstance("删除評論", this)
                .delete_comment_request(params,
                        new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                            @Override
                            public void on_post_entity(List mlocation) {
                                Log.e("123", "删除評論成功：");
                                PAGE = 1;
                                isComm = 1;
                                init_one_comment(answer_id);
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "删除評論报错：" + error);
                                Toast.makeText(CommentActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }, this, false));
    }


    private boolean ista = false;

    //
    private void set_list_resource(final List<AnswerListOne> dates) {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setItemAnimator(null);
        recycler_view.setHasFixedSize(true);

        commentAdapter = new CommentAdapter(CommentActivity.this, this, handler, ista, answer_id, dates);
        recycler_view.setAdapter(commentAdapter);

        // 设置静默加载模式
        // 设置静默加载模式
        xrefreshview.setSilenceLoadMore(true);
        //设置刷新完成以后，headerview固定的时间
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);
        xrefreshview.setPullRefreshEnable(true);
        xrefreshview.setPullLoadEnable(true);
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.enableRecyclerViewPullUp(true);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
        //给recycler_view设置底部加载布局
        xrefreshview.enableReleaseToLoadMore(true);
        xrefreshview.enableRecyclerViewPullUp(true);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
        xrefreshview.setPreLoadCount(10);
        if (dates.size() > 7) {
            xrefreshview.enableReleaseToLoadMore(true);
            xrefreshview.setLoadComplete(false);//显示底部
        } else {
            xrefreshview.enableReleaseToLoadMore(false);
            xrefreshview.setLoadComplete(true);//隐藏底部
        }
        xrefreshview.setOnRecyclerViewScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PAGE = 1;
                        isComm = 1;
                        //填写刷新数据的网络请求，一般page=1，List集合清空操作
                        init_one_comment(answer_id);
                        xrefreshview.stopRefresh();//刷新停止

                    }
                }, 1000);//2000是刷新的延时，使得有个动画效果
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        PAGE = PAGE + 1;
                        //填写加载更多的网络请求，一般page++
                        init_one_comment(answer_id);
                        //没有更多数据时候
                        if (is_not_more) {
                            xrefreshview.setLoadComplete(true);
                        } else {
                            //刷新完成必须调用此方法停止加载
                            xrefreshview.stopLoadMore(true);
                        }
                    }
                }, 1000);//1000是加载的延时，使得有个动画效果
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 返回按键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (!TextUtils.isEmpty(Comment_thumbs) && !TextUtils.isEmpty(is_thumbs)) {
//                Intent intent = new Intent();
//                intent.putExtra("Comment_thumbs", Comment_thumbs);
//                intent.putExtra("isthumbs", is_thumbs);
//                setResult(100, intent);
//            }
            finish();
        }
        return true;
    }

}

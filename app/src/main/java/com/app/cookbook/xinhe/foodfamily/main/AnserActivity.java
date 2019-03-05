package com.app.cookbook.xinhe.foodfamily.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.callback.DialogCallBack;
import com.app.cookbook.xinhe.foodfamily.main.adapter.AnswerFunctionAdapter;
import com.app.cookbook.xinhe.foodfamily.main.adapter.HotCommentAdapter;
import com.app.cookbook.xinhe.foodfamily.main.adapter.ReportMsgAdapter;
import com.app.cookbook.xinhe.foodfamily.main.adapter.ShareImageItemAdapter;
import com.app.cookbook.xinhe.foodfamily.main.db.NoteDao;
import com.app.cookbook.xinhe.foodfamily.main.db.entity.Note;
import com.app.cookbook.xinhe.foodfamily.main.entity.AnswerDate;
import com.app.cookbook.xinhe.foodfamily.main.entity.FunctionItem;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageEvent;
import com.app.cookbook.xinhe.foodfamily.main.entity.ReportMsg;
import com.app.cookbook.xinhe.foodfamily.main.entity.ShareImageItem;
import com.app.cookbook.xinhe.foodfamily.main.webview.MJavascriptInterface;
import com.app.cookbook.xinhe.foodfamily.main.webview.MyWebViewClient;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.update.PermisionUtils;
import com.app.cookbook.xinhe.foodfamily.util.DateUtils;
import com.app.cookbook.xinhe.foodfamily.util.FormatCurrentData;
import com.app.cookbook.xinhe.foodfamily.util.PopWindowHelper;
import com.app.cookbook.xinhe.foodfamily.util.ShareUtil;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.StringUtils;
import com.app.cookbook.xinhe.foodfamily.util.otherUi.ObservableScrollView;
import com.app.cookbook.xinhe.foodfamily.util.snackbar.BaseTransientBottomBar;
import com.app.cookbook.xinhe.foodfamily.util.snackbar.TopSnackBar;
import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.orhanobut.logger.Logger;
import com.tencent.stat.StatService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class AnserActivity extends BaseActivity implements ObservableScrollView.OnScrollListener, DialogInterface.OnCancelListener {
    //标题
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.issue_title_tv)
    TextView issue_title_tv;
    @BindView(R.id.zan_relayout)
    LinearLayout zan_relayout;
    //收藏
    @BindView(R.id.shoucang_layout)
    LinearLayout shoucang_layout;
    //分享
    @BindView(R.id.shaer_layout)
    LinearLayout shaer_layout;
    @BindView(R.id.zan_image)
    LikeButton zan_image;
    @BindView(R.id.dianzan_number)
    TextView dianzan_number;
    @BindView(R.id.shoucang_number)
    TextView shoucang_number;
    //收藏
    @BindView(R.id.shoucang_image)
    LikeButton shoucang_image;
    //关注问题
    @BindView(R.id.attention_image)
    LikeButton attention_image;
    //关注文字
    @BindView(R.id.attention_tv)
    TextView attention_tv;
    @BindView(R.id.more_btn)
    ImageView more_btn;
    @BindView(R.id.back_image)
    LinearLayout back_image;
    @BindView(R.id.coom_recyclerView)
    RecyclerView recyclerViews;
    @BindView(R.id.more_recyclerView)
    TextView more_recyclerView;
    @BindView(R.id.comment_layout)
    LinearLayout comment_layout;
    @BindView(R.id.comm_number)
    TextView comm_number;
    @BindView(R.id.scrollView)
    ObservableScrollView mScrollView;
    @BindView(R.id.childView_layout)
    RelativeLayout childView_layout;
    @BindView(R.id.pinglun_layout)
    LinearLayout pinglun_layout;
    //关注问题
    @BindView(R.id.attention_layout)
    RelativeLayout attention_layout;
    //回答问题
    @BindView(R.id.answer_layout)
    LinearLayout answer_layout;
    //个人信息布局
    @BindView(R.id.touxiang_layout)
    RelativeLayout touxiang_layout;
    //个人信息布局
    @BindView(R.id.touxiang_layout_two)
    RelativeLayout touxiang_layout_two;
    //用户信息布局
    @BindView(R.id.user_click)
    LinearLayout user_click;
    //用户信息布局
    @BindView(R.id.user_click_two)
    LinearLayout user_click_two;
    //用户头像
    @BindView(R.id.select_head_image)
    CircleImageView select_head_image;
    //用户头像
    @BindView(R.id.select_head_image_two)
    CircleImageView select_head_image_two;
    //用户名
    @BindView(R.id.user_name)
    TextView user_name;
    //用户名
    @BindView(R.id.user_name_two)
    TextView user_name_two;
    //用户签名
    @BindView(R.id.user_qianming)
    TextView user_qianming;
    //用户签名
    @BindView(R.id.user_qianming_two)
    TextView user_qianming_two;
    //已关注
    @BindView(R.id.huxiang_layout_top)
    LinearLayout huxiang_layout_top;
    //已关注
    @BindView(R.id.huxiang_layout_top_two)
    LinearLayout huxiang_layout_top_two;
    //关注
    @BindView(R.id.guanzhu_layout_top)
    LinearLayout guanzhu_layout_top;
    //关注
    @BindView(R.id.guanzhu_layout_top_two)
    LinearLayout guanzhu_layout_top_two;
    //点赞，评论，收藏布局
    @BindView(R.id.btn_layout)
    LinearLayout btn_layout;
    //热门评论数
    @BindView(R.id.pinglun_number)
    TextView pinglun_number;
    @BindView(R.id.comment_time_layout)
    RelativeLayout comment_time_layout;
    @BindView(R.id.comment_time)
    TextView comment_time;
    @BindView(R.id.container)
    RelativeLayout rl;
    @BindView(R.id.rel_quanju)
    RelativeLayout rel_quanju;
    LinearLayoutManager layoutManager;
    //答案id
    private String answer_id = "";
    //问题ID
    private String question_id = "";
    private AnswerDate answerDate = new AnswerDate();
    @BindView(R.id.web_show)
    WebView web_show;
    //隐藏的webView
    private NoteDao noteDao;
    //评论适配器
    private HotCommentAdapter hotCommentAdapter;
    //评论列表
    private List<AnswerDate.HotComment> answerListOnes = new ArrayList<>();
    //评论列表造作实体类
    private AnswerDate.HotComment listOne;
    private String comment_id;

    //是否删除
    private String is_delete;

    private Animation showAnim;
    private Animation dismissAnim;
    private Animation showAnimTwo;
    private Animation dismissAnimTwo;
    //分享数据
    private int[] moreImageids = {R.drawable.icon_create_image, R.drawable.icon_wechat, R.drawable.icon_wechat_moments, R.drawable.icon_weibo, R.drawable.icon_qq, R.drawable.icon_qqzone};
    private String[] moreName = {"生成长图", "微信", "朋友圈", "新浪微博", "QQ", "QQ空间"};
    //分享数据集合
    private List<ShareImageItem> moreListem = new ArrayList<>();
    //删除，编辑，举报
    private int[] functionImageids = {R.drawable.bianji, R.drawable.more_shanchu, R.drawable.jubao};
    private String[] functionName = {"编辑", "删除", "举报"};
    private List<FunctionItem> functionListem = new ArrayList<>();


    //添加点赞，收藏，关注音效
    private SoundPool sp;//声明一个SoundPool
    private int music;//定义一个整型用load（）；来设置suondID

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            listOne = answerListOnes.get(msg.arg1);
            String is_thumbs = listOne.getIs_thumbs();
            comment_id = listOne.getComment_id();
            String user_id = listOne.getUser_id();
            Intent intent;
            Bundle bundle;
            switch (msg.what) {
                case 400:
                    Properties prop = new Properties();
                    prop.setProperty("name", "dianzan");
                    StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_praise", prop);
                    if ("2".equals(is_thumbs)) {
                        //点赞
                        cthumbs_comment(answer_id, comment_id);
                    } else if ("1".equals(is_thumbs)) {
                        //取消点赞
                        un_cthumbs_comment(answer_id, comment_id);
                    }
                    break;
                case 401:
                    Properties prop1 = new Properties();
                    prop1.setProperty("name", "pinglun");
                    StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_comment", prop1);

                    Log.e("123", "  ---answer_id          " + answer_id + "    ---- comment_id       " + comment_id);
                    intent = new Intent(AnserActivity.this, AnswerReplyActivity.class);
                    intent.putExtra("answer_id", answer_id);
                    intent.putExtra("original_id", comment_id);
                    intent.putExtra("image", listOne.getAvatar());
                    intent.putExtra("name", listOne.getName());
                    intent.putExtra("content", listOne.getComment_content());
                    intent.putExtra("time", listOne.getComment_created_at());
                    intent.putExtra("thumbs", listOne.getComment_thumbs());
                    intent.putExtra("isthumbs", listOne.getIs_thumbs());
                    intent.putExtra("user_id", listOne.getUser_id());
                    intent.putExtra("type", "1");
                    intent.putExtra("mesComm", "1");
                    intent.putExtra("isCheck", "1");
                    startActivityForResult(intent, 100);
                    break;
                case 402:
                    intent = new Intent(AnserActivity.this, FriendPageActivity.class);
                    intent.putExtra("UserId", user_id);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {
        if (parms != null) {
            if (null != parms.getString("answer_id")) {
                answer_id = parms.getString("answer_id");
                Log.e("123", "     answer_id     " + answer_id);
            }
            if (null != parms.getString("question_id")) {
                question_id = parms.getString("question_id");
            }
            Log.e("123", "     question_id     " + question_id);
        } else {
            gethost();
        }
    }

    @Override
    public void initView() {
        setContentLayout(R.layout.activity_anser);
        EventBus.getDefault().register(this);
        //初始化黄牛刀
        ButterKnife.bind(this);
        isShowTitle(false);
        initfind();

        sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(this, R.raw.dianzan, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级

        mScrollView.setOnScrollListener(this);
        showAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_show);
        dismissAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_dismiss);
        showAnimTwo = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.title_show);
        dismissAnimTwo = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.title_dismiss);
        mScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 监听滑动到头部与底部
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE: {
                        // 获取ScrollView中包裹的View的高度！！！
                        int measuredHeight = mScrollView.getChildAt(0)
                                .getMeasuredHeight();
                        int scrollY = mScrollView.getScrollY();
                        int height = mScrollView.getHeight();
                        if (measuredHeight <= scrollY + height + recyclerViews.getHeight()) {
                            // 已经滑动的距离+在屏幕上显示的高度>=控件真实高度。说明已经滑动到底部
                            btn_layout.clearAnimation();
                            btn_layout.startAnimation(showAnim);
                            btn_layout.setVisibility(View.VISIBLE);
                            isstop = "1";
                        }
                        break;
                    }
                }
                return false;
            }
        });

        mScrollView.setOnScollChangedListener(new ObservableScrollView.OnScollChangedListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (590 < y) {
                    title_tv.setVisibility(View.VISIBLE);
                    touxiang_layout_two.setVisibility(View.VISIBLE);
                } else {
                    title_tv.setVisibility(View.GONE);
                    touxiang_layout_two.setVisibility(View.GONE);
                }
            }
        });
    }

    private void gethost() {
        Intent intent = getIntent();
        if (intent != null) {
            Uri uri = intent.getData();
            if (uri != null) {
                String query = uri.getQuery();
                String querynub = query.substring(3);
                answer_id = querynub;
            }
        }
    }

    private String isstop = "0";

    @Override
    public void initonResume() {
        Properties prop = new Properties();
        prop.setProperty("look_answer_detai", "浏览答案详情页次数");
        StatService.trackCustomBeginKVEvent(this, "Browse_details_answer", prop);

        if (answerListOnes.size() > 0) {
            answerListOnes.clear();
        }
        init_answer_detail();
    }

    @Override
    public void initonPause() {
        Properties prop = new Properties();
        prop.setProperty("look_answer_detai", "浏览答案详情页次数");
        StatService.trackCustomEndKVEvent(this, "Browse_details_answer", prop);
    }

    private void initfind() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViews.setNestedScrollingEnabled(false);
        recyclerViews.setLayoutManager(linearLayoutManager);
        recyclerViews.setFocusable(false);

        hotCommentAdapter = new HotCommentAdapter(AnserActivity.this, handler);
        hotCommentAdapter.setAnswerListOnes(answerListOnes);
        recyclerViews.setAdapter(hotCommentAdapter);
        hotCommentAdapter.setOnItemClickListener(new HotCommentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AnswerDate.HotComment listOne = answerListOnes.get(position);
                Intent intent = new Intent(AnserActivity.this, AnswerReplyActivity.class);
                intent.putExtra("answer_id", answer_id);
                intent.putExtra("original_id", listOne.getComment_id());
                intent.putExtra("image", listOne.getAvatar());
                intent.putExtra("name", listOne.getName());
                intent.putExtra("content", listOne.getComment_content());
                intent.putExtra("time", listOne.getComment_created_at());
                intent.putExtra("thumbs", listOne.getComment_thumbs());
                intent.putExtra("isthumbs", listOne.getIs_thumbs());
                intent.putExtra("user_id", listOne.getUser_id());
                intent.putExtra("mesComm", "1");
                intent.putExtra("isCheck", "1");
                startActivity(intent);
            }
        });
    }

    @Override
    public void doBusiness(Context mContext) {
        noteDao = new NoteDao(this);

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "back_image");
                StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_return", prop);
                finish();
            }
        });
        //跳转评论界面
        more_recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop1 = new Properties();
                prop1.setProperty("name", "pinglun");
                StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_comment", prop1);

                Intent intent = new Intent(AnserActivity.this, CommentActivity.class);
                intent.putExtra("answer_id", answer_id);
                intent.putExtra("original_id", "");
                intent.putExtra("user_id", answerDate.getUid());
                intent.putExtra("isCheck", "1");
                startActivityForResult(intent, 100);
            }
        });
        //跳转评论界面
        comment_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop1 = new Properties();
                prop1.setProperty("name", "pinglun");
                StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_comment", prop1);

                Intent intent = new Intent(AnserActivity.this, CommentActivity.class);
                intent.putExtra("answer_id", answer_id);
                intent.putExtra("isCheck", "1");
                startActivityForResult(intent, 100);
            }
        });

        user_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Properties prop = new Properties();
                prop.setProperty("name", "user_click");
                StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_personal_home_page", prop);

                Intent intent = new Intent(AnserActivity.this, FriendPageActivity.class);
                intent.putExtra("UserId", answerDate.getUid() + "");
                startActivity(intent);
            }
        });

        user_click_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Properties prop = new Properties();
                prop.setProperty("name", "user_click");
                StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_personal_home_page", prop);
                Intent intent = new Intent(AnserActivity.this, FriendPageActivity.class);
                intent.putExtra("UserId", answerDate.getUid() + "");
                startActivity(intent);
            }
        });

        title_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //问题详情
                if (answerDate.getPid() != 0) {
                    Intent intent = new Intent(AnserActivity.this, FenLeiQuestionDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("wenti_id", answerDate.getPid() + "");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        issue_title_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //问题详情
                if (answerDate.getPid() != 0) {
                    Intent intent = new Intent(AnserActivity.this, FenLeiQuestionDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("wenti_id", answerDate.getPid() + "");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("share_time", "分享后返回时长");
                StatService.trackCustomBeginKVEvent(AnserActivity.this, "Share_back_time", prop);
                if (functionListem.size() > 0) {
                    functionListem.clear();
                }
                select_more_popwindow("2");
            }
        });
    }

    public static Dialog dialog;
    //微博分享标题
    private String content;
    //分享类型
    private String ShareType;

    private void select_more_popwindow(String type) {
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.more_popwindow, null);

        //分享列表
        RecyclerView more_recyclerView = view.findViewById(R.id.more_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        more_recyclerView.setLayoutManager(layoutManager);
        more_recyclerView.setHasFixedSize(true);

        //刪除，編輯，舉報功能列表
        RecyclerView function_recyclerView = view.findViewById(R.id.function_recyclerView);
        LinearLayoutManager function_layoutManager = new LinearLayoutManager(this);
        function_layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        function_recyclerView.setLayoutManager(function_layoutManager);
        function_recyclerView.setHasFixedSize(true);

        //取消
        LinearLayout select_dismiss = view.findViewById(R.id.select_dismiss);

        dialog = new Dialog(this, R.style.transparentFrameWindowStyle2);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        if ("1".equals(type)) {
            function_recyclerView.setVisibility(View.GONE);
        } else {
            function_recyclerView.setVisibility(View.VISIBLE);
        }

        if (moreListem.size() == 0) {
            for (int i = 0; i < moreName.length; i++) {
                ShareImageItem item = new ShareImageItem();
                item.setShareImage(moreImageids[i]);
                item.setShareNamr(moreName[i]);
                moreListem.add(item);
            }
        }
        //分享填充适配器
        ShareImageItemAdapter shareImageItemAdapter = new ShareImageItemAdapter(this);
        shareImageItemAdapter.setShareImageItems(moreListem);
        more_recyclerView.setAdapter(shareImageItemAdapter);

        //设置逻辑交互
        //编辑
        if (functionListem.size() == 0) {
            if (SharedPreferencesHelper.get(getApplicationContext(), "user_id", "").toString().equals(answerDate.getUid())) {
                FunctionItem item = new FunctionItem();
                item.setFunctionImage(functionImageids[0]);
                item.setFunctionName(functionName[0]);
                functionListem.add(item);
                FunctionItem item2 = new FunctionItem();
                item2.setFunctionImage(functionImageids[2]);
                item2.setFunctionName(functionName[2]);
                functionListem.add(item2);
                if ("1".equals(is_delete)) {
                    FunctionItem item1 = new FunctionItem();
                    item1.setFunctionImage(functionImageids[1]);
                    item1.setFunctionName(functionName[1]);
                    functionListem.add(item1);
                }
            } else {
                FunctionItem item2 = new FunctionItem();
                item2.setFunctionImage(functionImageids[2]);
                item2.setFunctionName(functionName[2]);
                functionListem.add(item2);
                if ("1".equals(is_delete)) {
                    FunctionItem item1 = new FunctionItem();
                    item1.setFunctionImage(functionImageids[1]);
                    item1.setFunctionName(functionName[1]);
                    functionListem.add(item1);
                }
            }
        }
        //功能填充适配器
        AnswerFunctionAdapter answerFunctionAdapter = new AnswerFunctionAdapter(this);
        answerFunctionAdapter.setShareImageItems(functionListem);
        function_recyclerView.setAdapter(answerFunctionAdapter);

        shareImageItemAdapter.setOnItemClickListener(new ShareImageItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Properties prop = new Properties();
                switch (moreListem.get(position).getShareNamr()) {
                    case "微信":
                        prop.setProperty("name", "wechat_layout");
                        StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_more_share_wechat", prop);

                        if (answerDate.getImg_data() != null && answerDate.getImg_data().size() > 0) {
                            ShareUtil.ShareWechat(AnserActivity.this, answer.getQuestion_title(), answer.getAnswer_content_remove(),
                                    answerDate.getImg_data().get(0).getPath(),
                                    Network.ShareUrl + "answer?id=" + answer_id, "2");
                        } else {
                            ShareUtil.ShareWechat(AnserActivity.this, answer.getQuestion_title(), answer.getAnswer_content_remove(),
                                    Network.ShareImage,
                                    Network.ShareUrl + "answer?id=" + answer_id, "2");
                        }
                        ShareType = "2";
                        dialog.dismiss();
                        break;
                    case "朋友圈":
                        prop.setProperty("name", "wechat_moments_layout");
                        StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_more_share_wechat_circle", prop);
                        if (answerDate.getImg_data() != null && answerDate.getImg_data().size() > 0) {
                            ShareUtil.ShareWechatMoments(AnserActivity.this, answer.getQuestion_title(), answer.getAnswer_content_remove(),
                                    answerDate.getImg_data().get(0).getPath(),
                                    Network.ShareUrl + "answer?id=" + answer_id, "2");
                        } else {
                            ShareUtil.ShareWechatMoments(AnserActivity.this, answer.getQuestion_title(), answer.getAnswer_content_remove(),
                                    Network.ShareImage,
                                    Network.ShareUrl + "answer?id=" + answer_id, "2");
                        }
                        ShareType = "1";
                        dialog.dismiss();
                        break;
                    case "新浪微博":
                        prop.setProperty("name", "sinaWeibo_layout");
                        StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_more_share_sina", prop);
                        if (answerDate.getImg_data() != null && answerDate.getImg_data().size() > 0) {
                            if (content.length() > 40) {
                                content = content.substring(0, 40);
                                ShareUtil.ShareWeiBo(AnserActivity.this, content +
                                                "...-回答作者信息:" + answer.getName(), answer.getAnswer_content_remove(),
                                        answerDate.getImg_data().get(0).getPath(),
                                        Network.ShareUrl + "answer?id=" + answer_id, "2");
                            } else {
                                ShareUtil.ShareWeiBo(AnserActivity.this, content +
                                                "-回答作者信息:" + answer.getName(), answer.getAnswer_content_remove(),
                                        answerDate.getImg_data().get(0).getPath(),
                                        Network.ShareUrl + "answer?id=" + answer_id, "2");
                            }
                        } else {
                            if (content.length() > 40) {
                                content = content.substring(0, 40);
                                ShareUtil.ShareWeiBo(AnserActivity.this, content +
                                                "...-回答作者信息:" + answer.getName(), answer.getAnswer_content_remove(),
                                        Network.ShareImage,
                                        Network.ShareUrl + "answer?id=" + answer_id, "2");
                            } else {
                                ShareUtil.ShareWeiBo(AnserActivity.this, content +
                                                "-回答作者信息:" + answer.getName(), answer.getAnswer_content_remove(),
                                        Network.ShareImage,
                                        Network.ShareUrl + "answer?id=" + answer_id, "2");
                            }
                        }
                        ShareType = "3";
                        dialog.dismiss();
                        break;
                    case "QQ":
                        prop.setProperty("name", "QQ_layout");
                        StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_more_share_qq", prop);
                        if (answerDate.getImg_data() != null && answerDate.getImg_data().size() > 0) {
                            ShareUtil.ShareQQ(AnserActivity.this, answer.getQuestion_title(), answer.getAnswer_content_remove(), answerDate.getImg_data().get(0).getPath(),
                                    Network.ShareUrl + "answer?id=" + answer_id, "2");
                        } else {
                            ShareUtil.ShareQQ(AnserActivity.this, answer.getQuestion_title(), answer.getAnswer_content_remove(),
                                    Network.ShareImage,
                                    Network.ShareUrl + "answer?id=" + answer_id, "2");
                        }
                        ShareType = "4";
                        dialog.dismiss();
                        break;
                    case "QQ空间":
                        prop.setProperty("name", "QQzone_layout");
                        StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_more_share_qqzone", prop);
                        if (answerDate.getImg_data() != null && answerDate.getImg_data().size() > 0) {
                            ShareUtil.ShareQZone(AnserActivity.this, answer.getQuestion_title(), answer.getAnswer_content_remove(), answerDate.getImg_data().get(0).getPath(),
                                    Network.ShareUrl + "answer?id=" + answer_id, "2");
                        } else {
                            ShareUtil.ShareQZone(AnserActivity.this, answer.getQuestion_title(), answer.getAnswer_content_remove(),
                                    Network.ShareImage,
                                    Network.ShareUrl + "answer?id=" + answer_id, "2");
                        }
                        ShareType = "5";
                        dialog.dismiss();
                        break;
                    case "生成长图":
                        PermisionUtils.verifyStoragePermissions(AnserActivity.this);
                        Intent intent1 = new Intent(AnserActivity.this, PreviewShareImageActivity.class);
                        intent1.putExtra("imagePath", "");
                        intent1.putExtra("title", answer.getQuestion_title());
                        intent1.putExtra("type", "2");
                        intent1.putExtra("id", answer_id);
                        startActivity(intent1);
                        dialog.dismiss();
                        break;
                }
            }
        });

        answerFunctionAdapter.setOnItemClickListener(new AnswerFunctionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Properties prop = new Properties();
                switch (functionListem.get(position).getFunctionName()) {
                    case "编辑":
                        prop.setProperty("name", "bianji_layout");
                        StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_more_edite", prop);
                        go_to_edit();
                        break;
                    case "举报":
                        prop.setProperty("name", "jubao_layout");
                        StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_more_report", prop);
                        if ("".equals(SharedPreferencesHelper.get(AnserActivity.this, "login_token", ""))) {
                            Intent intent = new Intent(AnserActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            if (data.size() > 0) {
                                data.clear();
                            }
                            report_msg_question();
                            dialog.dismiss();
                        }
                        break;
                    case "删除":
                        dialog.dismiss();
                        prop.setProperty("name", "delete_layout");
                        StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_more_delete", prop);
                        PopWindowHelper.public_tishi_pop(AnserActivity.this, "删除回答", "是否删除该回答？", "否", "是", new DialogCallBack() {
                            @Override
                            public void save() {
                                delete_daan_submit();
                            }

                            @Override
                            public void cancel() {

                            }
                        });
                        break;
                }
            }
        });

        //取消
        select_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "select_dismiss");
                StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_cancel", prop);
                dialog.dismiss();
            }
        });
    }


    //订阅方法，当接收到事件的时候，会调用该方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
//        Log.e("123", "      messageEvent.getMessage()           " + messageEvent.getMessage());
        switch (messageEvent.getMessage()) {
            case "122":
                ShareUtil.add_share_logs_question(AnserActivity.this, "", ShareType, answer_id);
                TopSnackBar.make(more_btn, "分享成功", BaseTransientBottomBar.LENGTH_SHORT, AnserActivity.this).show();

                Properties prop = new Properties();
                prop.setProperty("share_time", "分享后返回时长");
                StatService.trackCustomEndKVEvent(this, "Share_back_time", prop);
                break;
            case "600":
                if (answerDate == null) {
                    init_answer_detail();
                }
                break;
        }
    }

    /**
     * 保存数据,=0销毁当前界面，=1不销毁界面，为了防止在后台时保存笔记并销毁，应该只保存笔记
     */

    private void saveNoteData(Note note, String answer_id, String content, String mcontent) {
        //以问题的ID作为分类
        note.setTitle("回答");
        note.setContent(content);
        note.setMcontent(mcontent);
        note.setGroupId(Integer.valueOf(answer_id));
        note.setGroupName(answer_id);
        note.setType(2);
        note.setBgColor("#FFFFFF");
        note.setIsEncrypt(0);
        note.setCreateTime(DateUtils.date2string(new Date()));
        //新建笔记
        long noteId = noteDao.insertNote(note);
        Log.i("", "新建立note答案Id: " + noteId);
    }

    /**
     * 去往编辑的页面
     */
    private void go_to_edit() {
        Note note = new Note();
        saveNoteData(note, answerDate.getId() + "", answerDate.getAnswer_content_remove(), answerDate.getContent());
        Intent intent = new Intent(AnserActivity.this, QuestionAnswerDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("flag", "1");
        bundle.putString("question_id", answerDate.getPid() + "");
        bundle.putString("question_title", answerDate.getQuestion_title());
        bundle.putString("answer_id", answerDate.getId() + "");
        intent.putExtras(bundle);
        startActivity(intent);
        dialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void quxiao_guanzhu_submit() {
        Map<String, String> params = new HashMap<>();
        params.put("uuid", answerDate.getUid() + "");
        Log.e("取消关注他：", params.toString());
        subscription = Network.getInstance("取消关注他", this)
                .quxiaoguanzhuta(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Logger.e("取消关注他成功：" + result.getCode());
                                //切换布局
                                huxiang_layout_top.setVisibility(View.GONE);
                                guanzhu_layout_top.setVisibility(View.VISIBLE);
                                huxiang_layout_top_two.setVisibility(View.GONE);
                                guanzhu_layout_top_two.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError(String error) {
                                Logger.e("取消关注他报错：" + error);
                            }
                        }, this, false));
    }

    private void guanzhu_submit() {
        Map<String, String> params = new HashMap<>();
        params.put("uuid", answerDate.getUid() + "");
        Log.e("关注他：", params.toString());
        subscription = Network.getInstance("关注他", this)
                .guanzhuta(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Logger.e("关注他成功：" + result.getCode());
                                //切换布局
                                huxiang_layout_top.setVisibility(View.VISIBLE);
                                guanzhu_layout_top.setVisibility(View.GONE);
                                huxiang_layout_top_two.setVisibility(View.VISIBLE);
                                guanzhu_layout_top_two.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Logger.e("关注他报错：" + error);
                            }
                        }, this, false));
    }

    private void UnShouCangNet(String answer_id, String question_id) {
        if (null == answer_id || "".equals(answer_id)) {
            Toast.makeText(this, "答案ID为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (null == question_id || "".equals(question_id)) {
            Toast.makeText(this, "问题ID为空！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("answer_id", answer_id);
            params.put("question_id", question_id);
            Log.e("取消收藏：", params.toString());
            subscription = Network.getInstance("取消收藏", this)
                    .unshoucang(answer_id, question_id,
                            new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                                @Override
                                public void onNext(Bean<List<String>> result) {
                                    Logger.e("取消收藏成功：" + result.getCode());
                                    shoucang_number.setText((answerDate.getCollection()) - 1 + "收藏");
                                    answerDate.setIs_collect(2);
                                    answerDate.setCollection((answerDate.getCollection() - 1));
                                    shoucang_image.setLiked(false);
                                    shoucang_image.setUnlikeDrawableRes(R.drawable.home_collect_off);
                                    answerDate.setIs_collect_text("未收藏");
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(AnserActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }, this, false));
        }

    }

    private void ShouCangNet(String answer_id, String question_id) {
        if (null == answer_id || "".equals(answer_id)) {
            Toast.makeText(this, "答案ID为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (null == question_id || "".equals(question_id)) {
            Toast.makeText(this, "问题ID为空！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("answer_id", answer_id);
            params.put("question_id", question_id);
            Log.e("收藏：", params.toString());
            subscription = Network.getInstance("收藏", this)
                    .shoucang(params,
                            new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                                @Override
                                public void onNext(Bean<List<String>> result) {
                                    Logger.e("收藏成功：" + result.getCode());
                                    shoucang_image.setLiked(true);
                                    shoucang_image.setLikeDrawableRes(R.drawable.home_collect_on);
                                    shoucang_number.setText((answerDate.getCollection()) + 1 + "收藏");
                                    answerDate.setCollection((answerDate.getCollection()) + 1);
                                    answerDate.setIs_collect(1);
                                    answerDate.setIs_collect_text("已收藏");

                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(AnserActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }, this, false));
        }
    }

    private void UnDianZanNet(String answer_id, String question_id) {
        if (null == answer_id || "".equals(answer_id)) {
            Toast.makeText(this, "答案ID为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (null == question_id || "".equals(question_id)) {
            Toast.makeText(this, "问题ID为空！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("answer_id", answer_id);
            params.put("question_id", question_id);
            Log.e("取消点赞：", params.toString());
            subscription = Network.getInstance("取消点赞", this)
                    .undianzan(answer_id, question_id,
                            new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                                @Override
                                public void onNext(Bean<List<String>> result) {
                                    Logger.e("取消点赞成功：" + result.getCode());
                                    dianzan_number.setText(answerDate.getThumbs() - 1 + "赞");
                                    zan_image.setLiked(false);
                                    zan_image.setUnlikeDrawableRes(R.drawable.zan_off);
                                    answerDate.setThumbs(answerDate.getThumbs() - 1);
                                    answerDate.setIs_thumbs(2);
                                    answerDate.setIs_thumbs_text("未点赞");
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(AnserActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }, this, false));
        }
    }

    private void DianZanNet(String answer_id, String question_id) {
        if (null == answer_id || "".equals(answer_id)) {
            Toast.makeText(this, "答案ID为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (null == question_id || "".equals(question_id)) {
            Toast.makeText(this, "问题ID为空！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("answer_id", answer_id);
            params.put("question_id", question_id);
            Log.e("点赞：", params.toString());
            subscription = Network.getInstance("点赞", this)
                    .dianzan(params,
                            new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                                @Override
                                public void onNext(Bean<List<String>> result) {
                                    Logger.e("点赞成功：" + result.getCode());
                                    zan_image.setLiked(true);
                                    zan_image.setLikeDrawableRes(R.drawable.zan_on);
                                    dianzan_number.setText(answerDate.getThumbs() + 1 + "赞");
                                    answerDate.setThumbs(answerDate.getThumbs() + 1);
                                    answerDate.setIs_thumbs(1);
                                    answerDate.setIs_thumbs_text("已点赞");
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(AnserActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }, this, false));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String Comment_thumbs = data.getExtras().getString("Comment_thumbs");
            String isthumbs = data.getExtras().getString("isthumbs");
            listOne.setIs_thumbs(isthumbs);
            listOne.setComment_thumbs(Comment_thumbs);
            hotCommentAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init_answer_detail() {
        Map<String, String> params = new HashMap<>();
        params.put("answer_id", answer_id);
        subscription = Network.getInstance("答案详情", getApplicationContext())
                .answer_detail(params,
                        new ProgressSubscriberNew<>(AnswerDate.class, new GsonSubscriberOnNextListener<AnswerDate>() {
                            @Override
                            public void on_post_entity(AnswerDate result) {
//                                Logger.e("答案详情第三方士大夫：", "" + result);

                                //设置页面的信息
                                answerDate = result;
                                //是否显示删除按钮
                                is_delete = result.getIs_delete();
                                question_id = String.valueOf(result.getPid());
                                SetInformation(result);
                                int comment = result.getComment();
//                                comm_number.setText(comment + "");
                                pinglun_number.setText("最热评论(" + comment + ")");

                                if (!TextUtils.isEmpty(result.getCreated_at())) {
                                    String created_at = result.getCreated_at();
                                    comment_time_layout.setVisibility(View.VISIBLE);
                                    comment_time.setText("回答于" + FormatCurrentData.getTimeRange(Long.valueOf(created_at)));
                                } else {
                                    comment_time_layout.setVisibility(View.GONE);
                                }

                                //是否显示右上角3个点
                                more_btn.setVisibility(View.VISIBLE);
                                if (result.getHot_comment() != null && result.getHot_comment().size() > 0) {
                                    pinglun_layout.setVisibility(View.VISIBLE);
                                    if (result.getHot_comment().size() == 1) {
                                        recyclerViews.setMinimumHeight(220);
                                    } else if (result.getHot_comment().size() == 2) {
                                        recyclerViews.setMinimumHeight(408);
                                    } else {
                                        recyclerViews.setMinimumHeight(816);
                                    }
                                    for (AnswerDate.HotComment item : result.getHot_comment()) {
                                        answerListOnes.add(item);
                                    }
                                    hotCommentAdapter.setAnswerListOnes(answerListOnes);
                                    recyclerViews.setAdapter(hotCommentAdapter);
                                } else {
                                    pinglun_layout.setVisibility(View.GONE);
                                }
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Logger.e("答案详情报错：" + error);

                                if ("timeout".equals(error)) {
                                    Toast.makeText(getApplicationContext(), "服务器请求超时请重新点击", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), error + "", Toast.LENGTH_SHORT).show();
                                }
                                finish();
                            }
                        }, this, true));

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
                                if ("1".equals(result.getCode())) {
                                    listOne.setIs_thumbs("1");
                                    int comment_number = Integer.valueOf(listOne.getComment_thumbs());
                                    listOne.setComment_thumbs(String.valueOf(comment_number + 1));
                                    hotCommentAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("错误", error);
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
                                if ("1".equals(result.getCode())) {
                                    listOne.setIs_thumbs("2");
                                    int comment_number = Integer.valueOf(listOne.getComment_thumbs());
                                    listOne.setComment_thumbs(String.valueOf(comment_number - 1));
                                    hotCommentAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("错误", error);
                            }
                        }, this, false));
    }

    //删除答案
    private void delete_daan_submit() {
        Map<String, String> params = new HashMap<>();
        params.put("answer_id", answer_id);
        Log.e("删除答案：", params.toString());
        subscription = Network.getInstance("删除答案", this)
                .delete_daan_request(params,
                        new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                            @Override
                            public void on_post_entity(List mlocation) {
                                Log.e("123", "删除答案成功：");
                                finish();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Logger.e("删除答案报错：" + error);
                                Toast.makeText(AnserActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }, this, false));
    }


    /**
     * 设置页面的跳转
     *
     * @param answerDate
     */
    AnswerDate answer;

    private void SetInformation(final AnswerDate answerDate) {
        //设置。如果回答的用户是自己就影藏关注按钮
        answer = answerDate;
        String user_id = SharedPreferencesHelper.get(getApplicationContext(), "user_id", "").toString();
        if (!TextUtils.isEmpty(user_id)) {
            if (user_id.equals(String.valueOf(answerDate.getUid()))) {
                guanzhu_layout_top.setVisibility(View.GONE);
                huxiang_layout_top.setVisibility(View.GONE);
                guanzhu_layout_top_two.setVisibility(View.GONE);
                huxiang_layout_top_two.setVisibility(View.GONE);
            } else {
                //设置关注状态
                if (answerDate.getIs_follow() == 1) {//已经关注
                    huxiang_layout_top.setVisibility(View.VISIBLE);
                    guanzhu_layout_top.setVisibility(View.GONE);
                    huxiang_layout_top_two.setVisibility(View.VISIBLE);
                    guanzhu_layout_top_two.setVisibility(View.GONE);
                } else if (answerDate.getIs_follow() == 2) {//未关注
                    huxiang_layout_top.setVisibility(View.GONE);
                    guanzhu_layout_top.setVisibility(View.VISIBLE);
                    huxiang_layout_top_two.setVisibility(View.GONE);
                    guanzhu_layout_top_two.setVisibility(View.VISIBLE);
                }
            }
        }
        content = answerDate.getQuestion_data().getQuestion_title();
        title_tv.setText(answerDate.getQuestion_data().getQuestion_title());
        issue_title_tv.setText(answerDate.getQuestion_data().getQuestion_title());
        title_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnserActivity.this, FenLeiQuestionDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("wenti_id", answerDate.getPid() + "");
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        Glide.with(getApplicationContext()).load(answerDate.getAvatar())
                .error(R.drawable.touxiang)
                .into(select_head_image);

        Glide.with(getApplicationContext()).load(answerDate.getAvatar())
                .error(R.drawable.touxiang)
                .into(select_head_image_two);

        user_name.setText(answerDate.getName());
        user_name_two.setText(answerDate.getName());
        if (null != answerDate.getUsers_personal_signature()) {
            user_qianming.setText(answerDate.getUsers_personal_signature() + "");
            user_qianming_two.setText(answerDate.getUsers_personal_signature() + "");

        } else {
            user_qianming.setText("懒懒路人");
            user_qianming_two.setText("懒懒路人");
        }

        //设置点赞的状态
        Log.e("123", "    设置点赞的状态      " + answerDate.getIs_thumbs());
        if (answerDate.getIs_thumbs() == 1) {
            zan_image.setLiked(true);
            zan_image.setLikeDrawableRes(R.drawable.zan_on);
        } else {
            zan_image.setLiked(false);
            zan_image.setUnlikeDrawableRes(R.drawable.zan_off);
        }
        if (answerDate.getThumbs() > 0) {
            dianzan_number.setText(answerDate.getThumbs() + "赞");
        }

        //设置关注状态
        Log.e("123", "    设置关注状态      " + answerDate.getQuestion_data().getIs_follow());
        if ("1".equals(answerDate.getQuestion_data().getIs_follow())) {
            attention_tv.setText("已关注");
            attention_tv.setTextColor(getResources().getColor(R.color.color_0EB39B));
            attention_image.setLiked(true);
            attention_image.setLikeDrawableRes(R.drawable.guanzhu_on);
        } else if ("2".equals(answerDate.getQuestion_data().getIs_follow())) {
            attention_tv.setText("关注问题");
            attention_tv.setTextColor(getResources().getColor(R.color.color_999999));
            attention_image.setLiked(false);
            attention_image.setUnlikeDrawableRes(R.drawable.guanzhu_off);
        }

        //设置收藏状态
        Log.e("123", "    设置收藏状态      " + answerDate.getIs_collect());
        if (answerDate.getIs_collect() == 1) {
            shoucang_image.setLiked(true);
            shoucang_image.setLikeDrawableRes(R.drawable.home_collect_on);
        } else {
            shoucang_image.setLiked(false);
            shoucang_image.setUnlikeDrawableRes(R.drawable.home_collect_off);
        }
        if (answerDate.getCollection() > 0) {
            shoucang_number.setText(answerDate.getCollection() + "收藏");
        }

        //回答问题
        answer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(AnserActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(AnserActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("123", "  question_id   " + question_id);
                    Intent intent = new Intent(AnserActivity.this, FastAnswerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("question_id", question_id + "");
                    bundle.putString("is_go_to_detai", "true");
                    bundle.putString("question_title", content);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        //关注用户
        guanzhu_layout_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(AnserActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(AnserActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Properties prop = new Properties();
                    prop.setProperty("name", "guanzhu_layout_top");
                    StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_follow", prop);
                    guanzhu_submit();
                }
            }
        });

        //取消关注
        huxiang_layout_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(AnserActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(AnserActivity.this, LoginActivity.class);
                    //按钮提示音
                    startActivity(intent);
                } else {
                    //按钮提示音
                    quxiao_guanzhu_submit();
                }
            }
        });
        //关注用户
        guanzhu_layout_top_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //按钮提示音
                if ("".equals(SharedPreferencesHelper.get(AnserActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(AnserActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Properties prop = new Properties();
                    prop.setProperty("name", "guanzhu_layout_top");
                    StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_follow", prop);
                    guanzhu_submit();
                }
            }
        });
        //取消关注
        huxiang_layout_top_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //按钮提示音
                if ("".equals(SharedPreferencesHelper.get(AnserActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(AnserActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    quxiao_guanzhu_submit();
                }

            }
        });

        //关注

        attention_image.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if ("".equals(SharedPreferencesHelper.get(AnserActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(AnserActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    guanzhu_question(question_id + "");
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                if ("".equals(SharedPreferencesHelper.get(AnserActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(AnserActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    quxiao_guanzhu_question(question_id + "");
                }
            }
        });

        //点赞
        zan_image.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                //按钮提示音
                sp.play(music, 0.2f, 0.2f, 0, 0, 1);
                if ("".equals(SharedPreferencesHelper.get(AnserActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(AnserActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    DianZanNet(answerDate.getId() + "", answerDate.getPid() + "");
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                //按钮提示音
                sp.play(music, 0.2f, 0.2f, 0, 0, 1);
                if ("".equals(SharedPreferencesHelper.get(AnserActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(AnserActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    UnDianZanNet(answerDate.getId() + "", answerDate.getPid() + "");
                }
            }
        });
        //收藏
        shoucang_image.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                //按钮提示音
                sp.play(music, 0.2f, 0.2f, 0, 0, 1);
                Properties prop = new Properties();
                prop.setProperty("name", "shoucang_layout");
                StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_collection", prop);
                if ("".equals(SharedPreferencesHelper.get(AnserActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(AnserActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    ShouCangNet(answerDate.getId() + "", answerDate.getPid() + "");
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                //按钮提示音
                sp.play(music, 0.2f, 0.2f, 0, 0, 1);
                Properties prop = new Properties();
                prop.setProperty("name", "shoucang_layout");
                StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_collection", prop);
                if ("".equals(SharedPreferencesHelper.get(AnserActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(AnserActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    UnShouCangNet(answerDate.getId() + "", answerDate.getPid() + "");
                }
            }
        });

        //分享
        shaer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //按钮提示音
                sp.play(music, 0.2f, 0.2f, 0, 0, 1);
                Properties prop = new Properties();
                prop.setProperty("name", "shaer_layout");
                StatService.trackCustomKVEvent(AnserActivity.this, "Details_answer_collection", prop);
                if (functionListem.size() > 0) {
                    functionListem.clear();
                }
                select_more_popwindow("2");
            }
        });

        //设置下方的列表
        final String linkCss = "<style type=\"text/css\"> " +
                "img {" +
                "max-width:100%;" +
                "max-height:100%;" +
                "margin:5px auto;" +
                "display:block;" +
                "}" +
                "p {" +
                "font-size:18px;" +
                "line-height:25px " +
                "}" +
                "body {" +
                "word-wrap:break-word;" +
                "font-family:Arial;" +
                "margin-right:15px;" +
                "margin-left:15px;" +
                "margin-top:5px;" +
                "font-size:15px;" +
                "}" + "</style>";
        String html = "<html><header>" + linkCss + "</header><body>" + answerDate.getContent().replace("\\'", "'") + "</body></html>";
//        Log.e("123","       答案详情内容      "+ answerDate.getContent());
        web_show.getSettings().setJavaScriptEnabled(true);
        web_show.getSettings().setAppCacheEnabled(true);
        web_show.getSettings().setDatabaseEnabled(true);
        web_show.getSettings().setDomStorageEnabled(true);

        web_show.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        //点击图片查看大图
        String[] imageUrls = StringUtils.returnImageUrlsFromHtml(html);
        web_show.addJavascriptInterface(new MJavascriptInterface(this, imageUrls), "imagelistener");
        web_show.setWebViewClient(new MyWebViewClient());
    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    protected void onDestroy() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(AnserActivity.this).clearDiskCache();//清理磁盘缓存需要在子线程中执行
            }
        }).start();
        super.onDestroy();
    }

    /***
     * 举报
     */
    private LinearLayoutManager linearLayoutManager;
    private ReportMsgAdapter reportMsgAdapter;
    private List<ReportMsg> data = new ArrayList<>();
    private TextView tv_report;
    private TextView report_title;

    private void select_report_popwindow(List<ReportMsg> msgs) {
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.report_popwindow, null);
        //举报信息
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayout report_btn = view.findViewById(R.id.report_btn);
        tv_report = view.findViewById(R.id.tv_report);
        report_title = view.findViewById(R.id.report_title);
        report_title.setText("举报回答");

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        reportMsgAdapter = new ReportMsgAdapter(this, handler1);
        reportMsgAdapter.setData(msgs);
        recyclerView.setAdapter(reportMsgAdapter);

        WindowManager wm1 = this.getWindowManager();
        int height1 = wm1.getDefaultDisplay().getHeight();

        dialog = new Dialog(this, R.style.transparentFrameWindowStyle2);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.height = height1 - (height1 / 3);
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                //处理监听事件
                Contacts.typeMsg.clear();
            }
        });
        //设置逻辑交互
        //编辑
        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = Contacts.typeMsg.toString();
                int len = str.length() - 1;
                String ids = str.substring(1, len).replace(" ", "");//"keyids":”1,2,3”
                if (!TextUtils.isEmpty(ids)) {
                    report_question(answerDate.getId() + "", ids);
                } else {
                    Toast.makeText(AnserActivity.this, "请选择举报内容", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Handler handler1 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 500:
                    tv_report.setTextColor(getResources().getColor(R.color.color_51B55C));
                    break;
                case 501:
                    tv_report.setTextColor(getResources().getColor(R.color.color_51B55C));
                    break;
                case 502:
                    tv_report.setTextColor(getResources().getColor(R.color.meishititle));
                    break;
            }

        }
    };

    /**
     * 举报
     *
     * @param question_id
     */
    private void report_question(String question_id, String type) {
        Map<String, String> params = new HashMap<>();
        params.put("answer_id", question_id);
        params.put("type", type);
        Log.e("举报问题：", params.toString());
        subscription = Network.getInstance("举报问题", this)
                .reports_Answer_request(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Logger.e("举报问题：" + result.getCode());
                                Toast.makeText(AnserActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                Contacts.typeMsg.clear();
                                dialog.dismiss();
                            }

                            @Override
                            public void onError(String error) {
                                Logger.e("举报问题异常：" + error.toString());
                                Toast.makeText(AnserActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }, this, false));
    }

    /**
     * 获取举报数据
     */
    private void report_msg_question() {
        subscription = Network.getInstance("获取举报问题类型", this)
                .report_Answer_type_request(new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<ReportMsg>>>() {
                    @Override
                    public void onNext(Bean<List<ReportMsg>> result) {

                        for (ReportMsg msg : result.getData()) {
//                            Logger.e("获取举报问题类型：" + msg.getId() + "            " + msg.getName());
                            data.add(msg);
                        }
                        select_report_popwindow(data);
                    }

                    @Override
                    public void onError(String error) {
                        Logger.e("获取举报问题类型异常：" + error.toString());
                    }
                }, this, false));
    }

    //关注问题
    private void guanzhu_question(String question_id) {
        Map<String, String> params = new HashMap<>();
        params.put("question_id", question_id);
        Log.e("关注问题：", params.toString());
        subscription = Network.getInstance("关注问题", AnserActivity.this)
                .guanzhu_question(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "关注问题成功：" + result.getCode());
                                attention_image.setLiked(true);
                                answerDate.getQuestion_data().setIs_follow("1");
                                attention_image.setLikeDrawableRes(R.drawable.guanzhu_on);
                                attention_tv.setText("已关注");
                                attention_tv.setTextColor(getResources().getColor(R.color.color_0EB39B));
                                EventBus.getDefault().post(new MessageEvent("500"));
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "  关注问题成功报错   " + error);
                            }
                        }, AnserActivity.this, false));
    }

    //取消关注问题
    private void quxiao_guanzhu_question(String question_id) {
        Map<String, String> params = new HashMap<>();
        params.put("question_id", question_id);
        Log.e("取消关注问题：", params.toString());
        subscription = Network.getInstance("取消关注问题", AnserActivity.this)
                .quxiao_question(question_id,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "取消关注问题成功：" + result.getCode());
                                attention_image.setLiked(false);
                                answerDate.getQuestion_data().setIs_follow("2");
                                attention_image.setUnlikeDrawableRes(R.drawable.guanzhu_off);
                                attention_tv.setText("关注问题");
                                attention_tv.setTextColor(getResources().getColor(R.color.color_999999));
                                EventBus.getDefault().post(new MessageEvent("501"));
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "  取消关注问题成功报错   " + error);
                            }
                        }, AnserActivity.this, false));
    }

    @Override
    public void onScroll(int scrollY) {
    }

    @Override
    public void onScrollToTop() {
        if (!btn_layout.isShown()) {
            btn_layout.clearAnimation();
            btn_layout.startAnimation(showAnim);
            btn_layout.setVisibility(View.VISIBLE);
        }
        isstop = "0";
    }

    @Override
    public void onScrollToBottom() {
        if ("0".equals(isstop)) {
            if (btn_layout.isShown()) {
                btn_layout.clearAnimation();
                btn_layout.startAnimation(dismissAnim);
                btn_layout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
    }
}

package com.app.cookbook.xinhe.foodfamily.shiyujia.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.callback.DialogCallBack;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.main.adapter.AnswerFunctionAdapter;
import com.app.cookbook.xinhe.foodfamily.main.adapter.ReportMsgAdapter;
import com.app.cookbook.xinhe.foodfamily.main.adapter.ShareImageItemAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.FunctionItem;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageEvent;
import com.app.cookbook.xinhe.foodfamily.main.entity.ReportMsg;
import com.app.cookbook.xinhe.foodfamily.main.entity.ShareImageItem;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.VideoCommentAdapter;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.ZhiZhenAdapter;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.BannerEntity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.VideoComment;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.VideoDetails;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.JudgeWifi;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.MD5Utils;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.shiyujia.view.LabelsView;
import com.app.cookbook.xinhe.foodfamily.util.CircleImageView;
import com.app.cookbook.xinhe.foodfamily.util.FormatCurrentData;
import com.app.cookbook.xinhe.foodfamily.util.PopWindowHelper;
import com.app.cookbook.xinhe.foodfamily.util.ShareUtil;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.Textutil;
import com.app.cookbook.xinhe.foodfamily.util.snackbar.BaseTransientBottomBar;
import com.app.cookbook.xinhe.foodfamily.util.snackbar.TopSnackBar;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.orhanobut.logger.Logger;
import com.shizhefei.view.indicator.BannerComponent;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.LayoutBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar;
import com.squareup.picasso.Picasso;
import com.tencent.stat.StatService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import chuangyuan.ycj.videolibrary.listener.VideoInfoListener;
import chuangyuan.ycj.videolibrary.listener.VideoWindowListener;
import chuangyuan.ycj.videolibrary.video.ExoUserPlayer;
import chuangyuan.ycj.videolibrary.video.VideoPlayerManager;
import chuangyuan.ycj.videolibrary.widget.VideoPlayerView;
import rx.Subscription;

public class VideoDetailsActivity extends Activity implements View.OnClickListener, TextView.OnEditorActionListener {

    @BindView(R.id.title_layout)
    RelativeLayout title_layout;
    //视频控件
    @BindView(R.id.home_videoView)
    VideoPlayerView home_videoView;
    ExoUserPlayer userPlayer;
    //返回按钮
    @BindView(R.id.back_im)
    LinearLayout back_im;
    //更多
    @BindView(R.id.more_im)
    ImageView more_im;
    //评论列表
    @BindView(R.id.recyclerView)
    XRecyclerView recyclerView;
    //
    @BindView(R.id.edit_alyout)
    RelativeLayout edit_alyout;
    //评论输入框
    @BindView(R.id.et_edit)
    EditText et_edit;
    //发送
    @BindView(R.id.send_reply)
    TextView send_reply;
    @BindView(R.id.comment_zan_layout)
    LinearLayout comment_zan_layout;
    //进入评论按钮
    @BindView(R.id.comment_layout)
    LinearLayout comment_layout;
    //点赞按钮
    @BindView(R.id.zan_image)
    LikeButton zan_image;
    //收藏按钮
    @BindView(R.id.shoucang_image)
    LikeButton shoucang_image;
    //分享按钮
    @BindView(R.id.shaer_layout)
    LinearLayout shaer_layout;
    //视频图标
    @BindView(R.id.play_btn)
    ImageView play_btn;
    //视频预加载图片
    @BindView(R.id.preview_image)
    ImageView preview_image;

    //详情作者布局
    private RelativeLayout author_msg_layout;
    private RelativeLayout user_msg_layout;
    private CircleImageView user_head_cir;
    private TextView user_name_tv;
    private TextView user_signature;
    private TextView user_attention;
    private TextView user_attention_off;
    private TextView details_content;
    private LabelsView labels;
    public TextView zan_layout;
    public TextView comm_layout;
    public TextView coll_layout;
    private TextView details_time_layout;
    private TextView comm_number;
    private RelativeLayout content_layout;
    private RelativeLayout user_llayout;

    //空状态
    private RelativeLayout empty_layout;


    private LinearLayoutManager linearLayoutManagerComment;
    //获取详情数据
    private VideoDetails imageTextDetails;
    //评论集合
    private List<VideoComment.DataBean> imageTextComments = new ArrayList<>();
    //评论适配器
    private VideoCommentAdapter videoCommentAdapter;
    //请求方法
    protected Subscription subscription;
    /***
     * 举报
     */
    private LinearLayoutManager linearLayoutManager;
    private ReportMsgAdapter reportMsgAdapter;
    private List<ReportMsg> data = new ArrayList<>();
    private TextView tv_report;
    private TextView report_title;

    //视频连接
    private String url = "";
    //视频图
    private String handImage;
    //图文ID
    private String id;

    //分享数据
    private int[] moreImageids = {R.drawable.icon_wechat, R.drawable.icon_wechat_moments, R.drawable.icon_weibo, R.drawable.icon_qq, R.drawable.icon_qqzone};
    private String[] moreName = {"微信", "朋友圈", "新浪微博", "QQ", "QQ空间"};
    //分享数据集合
    private List<ShareImageItem> moreListem = new ArrayList<>();
    //删除，编辑，举报
    private int[] functionImageids = {R.drawable.bianji, R.drawable.more_shanchu, R.drawable.jubao};
    private String[] functionName = {"编辑", "删除", "举报"};
    private List<FunctionItem> functionListem = new ArrayList<>();
    //是否可以删除
    private String is_delete;
    //是否可以编辑
    private String is_update;
    //是否点赞
    private String is_thumbs;
    //用户id
    private String Uid;
    //点赞数
    private int thumbNumber;
    //收藏数
    private int collectionNumber;
    //评论页数
    private int PAGE = 1;
    //评论某一条数据
    private VideoComment.DataBean textComment;
    //评论某一条点赞数
    private String zanNumber;
    //判断是否弹出键盘
    private String tan;
    //播放结束
    private String PlayEnd = "0";

    private String sendType = "0";

    private ExoUserPlayer exoPlayerManager;
    private int pos;
    private String is_follow_user;
    private String contentTv;
    private String avatar;
    private String name;
    private String personal_signature;
    private String thumb_number;
    private int coment_number;
    private String collection_number;
    private String created_at;
    private List<VideoDetails.ClassDataBean> class_data = new ArrayList<>();

    //判断是否释放资源
    private boolean isBack;

    //当前播放进度
    private long currPosition;
    //判断是否跳转页面
    private String IntentPage = "0";
    List<BannerEntity> bannerEntities = new ArrayList<>();
    ZhiZhenAdapter zhiZhenAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            textComment = imageTextComments.get(msg.arg1);
            zanNumber = textComment.getThumbs();
            pos = msg.arg1;
            Intent intent = null;
            switch (msg.what) {
                case 400:
                    intent = new Intent(VideoDetailsActivity.this, FriendPageActivity.class);
                    intent.putExtra("UserId", textComment.getUser_data().getId());
                    startActivity(intent);
                    break;
                case 401:
                    if ("".equals(SharedPreferencesHelper.get(VideoDetailsActivity.this, "login_token", ""))) {
                        intent = new Intent(VideoDetailsActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        IntentPage = "1";
                        intent = new Intent(VideoDetailsActivity.this, CommentVideoReplyActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("comment_id", textComment.getId());
                        intent.putExtra("total_id", textComment.getTotal_id());
                        intent.putExtra("is_thumbs", textComment.getIs_thumbs());
                        startActivity(intent);
                    }
                    break;
                case 402:
                    is_thumbs = textComment.getIs_thumbs();
                    if ("".equals(SharedPreferencesHelper.get(VideoDetailsActivity.this, "login_token", ""))) {
                        intent = new Intent(VideoDetailsActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        if ("2".equals(is_thumbs)) {
                            //点赞
                            textComment.setIs_thumbs("1");
                            textComment.setThumbs(String.valueOf(Integer.valueOf(zanNumber) + 1));
                            videoCommentAdapter.notifyDataSetChanged();
                            requestThumbsCommentVideo(id, textComment.getTotal_id());
                        } else if ("1".equals(is_thumbs)) {
                            //取消点赞
                            textComment.setIs_thumbs("2");
                            textComment.setThumbs(String.valueOf(Integer.valueOf(zanNumber) - 1));
                            videoCommentAdapter.notifyDataSetChanged();
                            requestVideo_image_id(id, textComment.getTotal_id());
                        }
                    }
                    break;
                case 403:
                    IntentPage = "1";
                    intent = new Intent(VideoDetailsActivity.this, CommentVideoReplyActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("total_id", textComment.getTotal_id());
                    intent.putExtra("comment_id", textComment.getId());
                    intent.putExtra("is_thumbs", textComment.getIs_thumbs());
                    startActivity(intent);
                    break;
                case 404:
                    PopWindowHelper.public_tishi_pop(VideoDetailsActivity.this, "删除评论", "是否删除该评论？", "否", "是", new DialogCallBack() {
                        @Override
                        public void save() {
                            requestDeleteVideoComment(id, textComment.getId());
                        }

                        @Override
                        public void cancel() {

                        }
                    });
                    break;
                case 405:
                    guanzhu_submit();
                    break;
                case 4051:
                    quxiao_guanzhu_submit();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        steepStatusBar();
        setContentView(R.layout.activity_video_details);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        initJingXuanDate(true);
        getCommentList("1", "1");
        getOnClick();
    }

    private void steepStatusBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);
    }

    private void initView() {
        id = getIntent().getStringExtra("id");
        url = getIntent().getStringExtra("url");
        handImage = getIntent().getStringExtra("handImage");
        currPosition = getIntent().getLongExtra("currPosition", 0);
        tan = getIntent().getStringExtra("tan");
        Log.e("123", "     图文ID    " + id);

        play_btn.setVisibility(View.GONE);
        userPlayer = new VideoPlayerManager.Builder(VideoPlayerManager.TYPE_PLAY_USER, home_videoView).create();
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) home_videoView.getLayoutParams();
        lp.height = width;
        home_videoView.setLayoutParams(lp);
        if (!TextUtils.isEmpty(url)) {
//            Log.e("123", "     url   " + url);
            home_videoView.setWGh(false);
            home_videoView.setOpenProgress2(true);
            exoPlayerManager = new VideoPlayerManager
                    .Builder(VideoPlayerManager.TYPE_PLAY_USER, home_videoView)
                    //设置视频标题
                    .setTitle("")
                    .setPosition(currPosition)
                    //设置水印图
                    .setPlayerGestureOnTouch(false)
                    .addMediaUri(Uri.parse(url))
                    //如果视频需要自己实现该回调 视频切换回调处理，进行布局处理，控制布局显示
                    .addVideoInfoListener(new VideoInfoListener() {
                        @Override
                        public void onPlayStart(long currPosition) {
                            Contacts.isPlay = true;
                            PlayEnd = "0";
                        }

                        @Override
                        public void onLoadingChanged() {
                        }

                        @Override
                        public void onPlayerError(@Nullable ExoPlaybackException e) {
                            exoPlayerManager.startPlayer();
                        }

                        @Override
                        public void onPlayEnd() {
                            PlayEnd = "1";
                            Log.e("123", "    PlayEnd    ");
                        }

                        @Override
                        public void isPlaying(boolean playWhenReady) {
                        }
                    })
                    .create();

            if (Contacts.isPlay == true || currPosition > 0) {
                exoPlayerManager.startPlayer();
            } else if (JudgeWifi.isWifiAvailable(this) == true) {
                requestPlayNumber(id);
                exoPlayerManager.startPlayer();
                back_im.setVisibility(View.VISIBLE);
            } else if ("2".equals(SharedPreferencesHelper.get(this, "isWiFI", ""))) {
                if (!exoPlayerManager.isPlaying()) {
                    exoPlayerManager.startPlayer();
                }
            } else {
                exoPlayerManager.setOnPlayClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //处理业务操作 完成后
                        if (Contacts.isPlay == false) {
                            exoPlayerManager.startPlayer();//开始播放
                        }
                    }
                });
            }
        }
        if (!TextUtils.isEmpty(handImage)) {
            Picasso.with(this)
                    .load(handImage)
                    .into(home_videoView.getPreviewImage());

            Picasso.with(this)
                    .load(handImage)
                    .into(preview_image);
        }

        linearLayoutManagerComment = new LinearLayoutManager(this);
        linearLayoutManagerComment.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManagerComment);

        getRefresh(recyclerView);

        videoCommentAdapter = new VideoCommentAdapter(this, handler);
        videoCommentAdapter.setImageTextComments(imageTextComments);
        recyclerView.setAdapter(videoCommentAdapter);

        back_im.setOnClickListener(this);
        more_im.setOnClickListener(this);
        comment_layout.setOnClickListener(this);
        shaer_layout.setOnClickListener(this);
        send_reply.setOnClickListener(this);
        et_edit.setOnEditorActionListener(this);
        //键盘显示监听
        et_edit.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            //当键盘弹出隐藏的时候会 调用此方法。
            @Override
            public void onGlobalLayout() {
                final Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                final int screenHeight = getWindow().getDecorView().getRootView().getHeight();
                final int heightDifference = screenHeight - rect.bottom;
                boolean visible = heightDifference > screenHeight / 3;
                if (visible) {
                    comment_zan_layout.setVisibility(View.GONE);
                    edit_alyout.setVisibility(View.VISIBLE);
                    if (edit_alyout.getVisibility() == View.VISIBLE) {
                        edit_alyout.requestFocus();
                    }
                } else {
                    comment_zan_layout.setVisibility(View.VISIBLE);
                    edit_alyout.setVisibility(View.GONE);
                }
            }
        });
        et_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
//        Log.e("123", "      messageEvent.getMessage()           " + messageEvent.getMessage());
        switch (messageEvent.getMessage()) {
            case "121":
                TopSnackBar.make(title_layout, "分享成功", BaseTransientBottomBar.LENGTH_SHORT, VideoDetailsActivity.this).show();
                Properties prop = new Properties();
                prop.setProperty("share_time", "分享后返回时长");
                StatService.trackCustomEndKVEvent(this, "Share_back_time", prop);
                break;
            case "1":
                initJingXuanDate(true);
                break;
        }
    }

    private void getItemClick() {
        videoCommentAdapter.setOnItemClickListener(new VideoCommentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!TextUtils.isEmpty(imageTextComments.get(position).getId())) {
                    IntentPage = "1";
                    Intent intent = new Intent(VideoDetailsActivity.this, CommentVideoReplyActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("comment_id", imageTextComments.get(position).getId());
                    intent.putExtra("total_id", imageTextComments.get(position).getTotal_id());
                    intent.putExtra("is_thumbs", imageTextComments.get(position).getIs_thumbs());
                    startActivity(intent);
                }
            }
        });
    }

    private void setBannerMsg() {
        /**设置食谱数据*/
        Log.e("123", "     ------------->   ");
        if (null != imageTextDetails.getBanners() && imageTextDetails.getBanners().size() > 0) {
            fram.setVisibility(View.VISIBLE);
            bannerEntities = imageTextDetails.getBanners();
            //设置间隔指针
            zhiZhenAdapter = new ZhiZhenAdapter(bannerEntities, VideoDetailsActivity.this);
            zhibiao_view.setAdapter(zhiZhenAdapter);
            //设置banner
            indicator.setScrollBar(new LayoutBar(VideoDetailsActivity.this, R.layout.banner_item, ScrollBar.Gravity.CENTENT_BACKGROUND));
            viewPager.setOffscreenPageLimit(2);
            bannerComponent = new BannerComponent(indicator, viewPager, false);
            bannerComponent.setAdapter(adapter);
            bannerComponent.setAutoPlayTime(2500);
            bannerComponent.startAutoPlay();
        } else {
            fram.setVisibility(View.GONE);
        }
    }

    private void getOnClick() {
        if (!TextUtils.isEmpty(tan)) {
            et_edit.setFocusable(true);
            et_edit.setFocusableInTouchMode(true);
            et_edit.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            tan = "";
        }
        //点赞
        zan_image.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                //按钮提示音
                if ("".equals(SharedPreferencesHelper.get(VideoDetailsActivity.this, "login_token", ""))) {
                    zan_image.setLiked(false);
                    zan_image.setUnlikeDrawableRes(R.drawable.home_like_off);
                    Intent intent = new Intent(VideoDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    DianZanNet(id, "1");
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                //按钮提示音
                if ("".equals(SharedPreferencesHelper.get(VideoDetailsActivity.this, "login_token", ""))) {
                    zan_image.setLiked(true);
                    zan_image.setUnlikeDrawableRes(R.drawable.home_like_on);
                    Intent intent = new Intent(VideoDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    UnDianZanNet(id);
                }
            }
        });
        //收藏
        shoucang_image.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                //按钮提示音
                Properties prop = new Properties();
                prop.setProperty("name", "shoucang_layout");
                StatService.trackCustomKVEvent(VideoDetailsActivity.this, "Details_answer_collection", prop);
                if ("".equals(SharedPreferencesHelper.get(VideoDetailsActivity.this, "login_token", ""))) {
                    shoucang_image.setLiked(false);
                    shoucang_image.setUnlikeDrawableRes(R.drawable.home_collect_off);
                    Intent intent = new Intent(VideoDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    ShouCangNet(id);
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                //按钮提示音
                Properties prop = new Properties();
                prop.setProperty("name", "shoucang_layout");
                StatService.trackCustomKVEvent(VideoDetailsActivity.this, "Details_answer_collection", prop);
                if ("".equals(SharedPreferencesHelper.get(VideoDetailsActivity.this, "login_token", ""))) {
                    shoucang_image.setLiked(true);
                    shoucang_image.setUnlikeDrawableRes(R.drawable.home_collect_on);
                    Intent intent = new Intent(VideoDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    UnShouCangNet(id);
                }
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
                if (!TextUtils.isEmpty(s.toString())) {
                    sendType = "0";
                    send_reply.setTextColor(getResources().getColor(R.color.color_292c31));
                } else {
                    send_reply.setTextColor(getResources().getColor(R.color.color_bbbbbb));
                }
                if (s.length() == 140) {
                    Textutil.ToastUtil(VideoDetailsActivity.this, "上限140个字");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.back_im:
                if (exoPlayerManager.onBackPressed()) {
                    isBack = true;
                    ActivityCompat.finishAfterTransition(this);
                } else {
                    ExoUserPlayer manualPlayer = VideoPlayerManager.getInstance().getVideoPlayer();
                    if (manualPlayer != null) {
                        Contacts.currPosition = manualPlayer.getCurrentPosition();
                    }
                }
                break;
            case R.id.more_im:
                if (functionListem.size() > 0) {
                    functionListem.clear();
                }
                select_more_popwindow();
                break;
            case R.id.comment_layout:
                comment_zan_layout.setVisibility(View.GONE);
                edit_alyout.setVisibility(View.VISIBLE);
                et_edit.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.shaer_layout:
                if (functionListem.size() > 0) {
                    functionListem.clear();
                }
                select_more_popwindow();
                break;
            case R.id.send_reply:
                if ("".equals(SharedPreferencesHelper.get(VideoDetailsActivity.this, "login_token", ""))) {
                    intent = new Intent(VideoDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if ("0".equals(sendType)) {
                        String content = et_edit.getText().toString();
                        if (!TextUtils.isEmpty(content)) {
                            sendType = "1";
                            requestAddComment(id, "", "", content);
                        } else {
                            Toast.makeText(this, "您还没有填写评论内容", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(url)) {
            exoPlayerManager.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        if ("1".equals(IntentPage)) {
//            if (!isBack || VideoPlayerManager.getInstance().getVideoPlayer() == null) {
//                Log.e("123", "      --------->  pause  ");
//                if ("0".equals(PlayEnd)) {
//                    userPlayer.onPause();
//                }
//            }
//        } else if ("0".equals(IntentPage)) {
            if (!isBack || VideoPlayerManager.getInstance().getVideoPlayer() == null) {
//                Log.e("123", "      ++++++++++++>  pause  ");
                if ("0".equals(PlayEnd)) {
                    exoPlayerManager.onPause();
                }
            }
//        }
    }

    @Override
    protected void onDestroy() {
        if ("1".equals(IntentPage)) {
            if (!isBack || VideoPlayerManager.getInstance().getVideoPlayer() == null) {
                userPlayer.onDestroy();
            }
        } else if ("0".equals(IntentPage)) {
            if (!isBack || VideoPlayerManager.getInstance().getVideoPlayer() == null) {
                exoPlayerManager.onDestroy();
            }
        }
        super.onDestroy();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        exoPlayerManager.onConfigurationChanged(newConfig);//横竖屏切换
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (exoPlayerManager.onBackPressed()) {
            isBack = true;
            ActivityCompat.finishAfterTransition(this);
        } else {
            ExoUserPlayer manualPlayer = VideoPlayerManager.getInstance().getVideoPlayer();
            if (manualPlayer != null) {
                Contacts.currPosition = manualPlayer.getCurrentPosition();
            }
//            isBack = true;
//            finish();
        }
    }


    private void initJingXuanDate(boolean is_true) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        Log.e("123", "视频详情：" + params.toString());
        subscription = Network.getInstance("视频详情", this)
                .getVideoDetails(params,
                        new ProgressSubscriberNew<>(VideoDetails.class, new GsonSubscriberOnNextListener<VideoDetails>() {
                            @Override
                            public void on_post_entity(VideoDetails result) {
                                Log.e("123", "     视频详情      " + result);
                                imageTextDetails = result;
                                initDate(result);
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "     视频详情报错      " + error);
                            }
                        }, this, is_true));
    }

    private void initDate(VideoDetails result) {
        if (class_data.size() > 0 && PAGE == 1) {
            class_data.clear();
        }
        is_delete = result.getIs_existence();
        is_update = result.getIs_update();
        Uid = result.getUser_id();
        is_follow_user = result.getIs_follow_user();
        contentTv = result.getContent();
        avatar = result.getUser_data().getAvatar();
        name = result.getUser_data().getName();
        personal_signature = result.getUser_data().getPersonal_signature();
        thumb_number = result.getThumb_number();

        collection_number = result.getCollection_number();
        created_at = result.getCreated_at();
        if (result.getClass_data() != null) {
            for (VideoDetails.ClassDataBean dataBean : result.getClass_data()) {
                class_data.add(dataBean);
            }
        }
        if (!TextUtils.isEmpty(result.getComent_number())) {
            coment_number = Integer.valueOf(result.getComent_number());
        }

        if (!TextUtils.isEmpty(result.getThumb_number())) {
            thumbNumber = Integer.valueOf(result.getThumb_number());
        }
        if (!TextUtils.isEmpty(result.getCollection_number())) {
            collectionNumber = Integer.valueOf(result.getCollection_number());
        }

        if ("1".equals(imageTextDetails.getIs_thumbs())) {
            zan_image.setLiked(true);
            zan_image.setLikeDrawableRes(R.drawable.home_like_on);
        } else if ("2".equals(imageTextDetails.getIs_thumbs())) {
            zan_image.setLiked(false);
            zan_image.setUnlikeDrawableRes(R.drawable.home_like_off);
        }
        if ("1".equals(imageTextDetails.getIs_collect())) {
            shoucang_image.setLiked(true);
            shoucang_image.setLikeDrawableRes(R.drawable.home_collect_on);
        } else if ("2".equals(imageTextDetails.getIs_collect())) {
            shoucang_image.setLiked(false);
            shoucang_image.setUnlikeDrawableRes(R.drawable.home_collect_off);
        }

        String user_id = SharedPreferencesHelper.get(VideoDetailsActivity.this, "user_id", "").toString();
        if (!TextUtils.isEmpty(user_id)) {
            if (user_id.equals(Uid)) {
                user_attention.setVisibility(View.GONE);
                user_attention_off.setVisibility(View.GONE);
            } else {
                if ("1".equals(is_follow_user)) {
                    user_attention_off.setVisibility(View.VISIBLE);
                    user_attention.setVisibility(View.GONE);
                } else if ("2".equals(is_follow_user)) {
                    user_attention.setVisibility(View.VISIBLE);
                    user_attention_off.setVisibility(View.GONE);
                }
            }
        }

        if (!TextUtils.isEmpty(contentTv)) {
            details_content.setVisibility(View.VISIBLE);
            details_content.setText(contentTv);
        } else {
            details_content.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(avatar)) {
            Glide.with(VideoDetailsActivity.this)
                    .load(avatar)
                    .error(R.drawable.touxiang)
                    .into(user_head_cir);
        }
        user_name_tv.setText(name);
        user_signature.setText(personal_signature);

        zan_layout.setText(thumbNumber + "点赞 ");
        comm_layout.setText(coment_number + "评论 ");
        comm_number.setText("全部评论(" + coment_number + ")");
        coll_layout.setText(collectionNumber + "收藏");
        if (!TextUtils.isEmpty(created_at)) {
            details_time_layout.setText(FormatCurrentData.getTimeRange(Long.valueOf(created_at)));
        }

        if (class_data.size() > 0) {
            labels.setVisibility(View.VISIBLE);
            labels.setLabels(class_data, new LabelsView.LabelTextProvider<VideoDetails.ClassDataBean>() {
                @Override
                public CharSequence getLabelText(TextView label, int position, VideoDetails.ClassDataBean data) {
                    return "# " + data.getTitle();
                }
            });
            labels.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
                @Override
                public void onLabelClick(TextView label, Object data, int position) {
                    VideoDetails.ClassDataBean item = class_data.get(position);
                    Intent intent = new Intent(VideoDetailsActivity.this, LabelDetailsActivity.class);
                    intent.putExtra("id", item.getId());
                    startActivity(intent);
                }
            });
        } else {
            labels.setVisibility(View.GONE);
        }

        if (bannerEntities.size() > 0) {
            bannerEntities.clear();
        }
        if (imageTextDetails.getBanners() != null && imageTextDetails.getBanners().size() > 0) {
            setBannerMsg();
        }

        if (TextUtils.isEmpty(url)) {
            if (result.getVideo_data() != null) {
                if ("1".equals(result.getVideo_data().getIs_transcoding())) {
                    url = result.getVideo_data().getVideo_path();
                    home_videoView.setWGh(false);
                    home_videoView.setOpenProgress2(true);
                    exoPlayerManager = new VideoPlayerManager
                            .Builder(VideoPlayerManager.TYPE_PLAY_USER, home_videoView)
                            //设置视频标题
                            .setTitle("")
                            //设置水印图
                            .setPlayerGestureOnTouch(false)
                            .addMediaUri(Uri.parse(url))
                            //如果视频需要自己实现该回调 视频切换回调处理，进行布局处理，控制布局显示
                            .addOnWindowListener(new VideoWindowListener() {
                                @Override
                                public void onCurrentIndex(int currentIndex, int windowCount) {
                                    Toast.makeText(getApplication(), "currentIndex::" + currentIndex, Toast.LENGTH_SHORT).show();
                                }
                            })
                            .create();
                } else if ("2".equals(result.getVideo_data().getIs_transcoding())) {
                    String path = result.getVideo_data().getTranscoding_data().get(0).getTranscoding_path();
                    //链接头部
                    String urlHead = path.substring(0, path.indexOf(".com")) + ".com";
                    //链接中间
                    String urlMiddle = path.substring(25, path.indexOf("?"));
                    //獲取PHP時間戳
                    String time = String.valueOf(Calendar.getInstance().getTimeInMillis());
                    String newTime = time.substring(0, 10);
                    String newUrl = MD5Utils.encode(urlMiddle + "-" + newTime + "-0-0-SHoXEVJALKeRij7JBnLgAIEDMPPKwgL2");
                    url = urlHead + urlMiddle + "?auth_key=" + newTime + "-0-0-" + newUrl;

                    home_videoView.setWGh(false);
                    home_videoView.setOpenProgress2(true);
                    exoPlayerManager = new VideoPlayerManager
                            .Builder(VideoPlayerManager.TYPE_PLAY_USER, home_videoView)
                            //设置视频标题
                            .setTitle("")
                            //设置水印图
                            .setPlayerGestureOnTouch(false)
                            .addMediaUri(Uri.parse(url))
                            //如果视频需要自己实现该回调 视频切换回调处理，进行布局处理，控制布局显示
                            .addVideoInfoListener(new VideoInfoListener() {
                                @Override
                                public void onPlayStart(long currPosition) {
                                    Contacts.isPlay = true;
                                    PlayEnd = "0";
                                }

                                @Override
                                public void onLoadingChanged() {
                                }

                                @Override
                                public void onPlayerError(@Nullable ExoPlaybackException e) {
                                    userPlayer.startPlayer();
                                }

                                @Override
                                public void onPlayEnd() {
                                    PlayEnd = "1";
                                }

                                @Override
                                public void isPlaying(boolean playWhenReady) {
                                }
                            })
                            .create();
                }
                Picasso.with(VideoDetailsActivity.this)
                        .load(result.getVideo_data().getFace_path())
                        .into(home_videoView.getPreviewImage());
                Picasso.with(VideoDetailsActivity.this)
                        .load(handImage)
                        .into(preview_image);
                if (JudgeWifi.isWifiAvailable(VideoDetailsActivity.this) == true) {
                    requestPlayNumber(id);
                    exoPlayerManager.startPlayer();
                    back_im.setVisibility(View.VISIBLE);
                } else if ("2".equals(SharedPreferencesHelper.get(VideoDetailsActivity.this, "isWiFI", ""))) {
                    if (!exoPlayerManager.isPlaying()) {
                        exoPlayerManager.startPlayer();
                    }
                } else {
                    userPlayer.setOnPlayClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //处理业务操作 完成后
                            userPlayer.startPlayer();//开始播放
                        }
                    });
                }
            }
        }
    }

    //评论列表
    private void getCommentList(String type, String start) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("page", String.valueOf(PAGE));
        Log.e("123", "图文评论" + params.toString());
        subscription = Network.getInstance("图文评论", this)
                .getVideoComment(params, new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<VideoComment>>() {
                    @Override
                    public void onNext(Bean<VideoComment> result) {
                        Log.e("123", "    视频评论result.getData().size()   " + result.getData().getData().size());
                        if (imageTextComments.size() > 0 && PAGE == 1) {
                            imageTextComments.clear();
                        }
                        if (result.getData().getData().size() == 0) {
                            recyclerView.setNoMore(true);
                        } else {
                            recyclerView.loadMoreComplete();
                        }
                        if (result.getData().getData().size() == 0 && PAGE == 1) {
                            empty_layout.setVisibility(View.VISIBLE);
                        } else {
                            empty_layout.setVisibility(View.GONE);
                        }
                        if ("2".equals(start)) {
                            coment_number = coment_number + 1;
                            comm_layout.setText(coment_number + "评论 ");
                            comm_number.setText("全部评论(" + coment_number + ")");
                        }

                        if (imageTextComments.size() > 0) {//这表示是"加载"
                            if (result.getData().getData().size() == 0) {
                                imageTextComments.addAll(result.getData().getData());
                                videoCommentAdapter.notifyDataSetChanged();
                            } else {
                                imageTextComments.addAll(result.getData().getData());
                                videoCommentAdapter.notifyDataSetChanged();
                            }
                        } else {
                            imageTextComments = result.getData().getData();
                            if (imageTextComments.size() > 0) {
                                videoCommentAdapter.setImageTextComments(imageTextComments);
                                recyclerView.setAdapter(videoCommentAdapter);
                            }
                        }
                        if (imageTextComments.size() > 0) {
                            getItemClick();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("123", "图文评论异常：" + error.toString());
                    }
                }, this, false));
    }


    //增加视频播放次数
    private void requestPlayNumber(String video_id) {
        Map<String, String> params = new HashMap<>();
        params.put("video_id", video_id);
        Log.e("123", "增加视频播放次数：" + params.toString());
        subscription = Network.getInstance("增加视频播放次数", this)
                .getAddVideoPlay(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "增加视频播放次数：" + result.getCode());
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "增加视频播放次数报错：" + error);
                            }
                        }, this, false));
    }

    //取消点赞评论
    private void requestVideo_image_id(String video_image_id, String comment_id) {
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
                                Toast.makeText(VideoDetailsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
//                                textComment.setIs_thumbs("2");
//                                textComment.setThumbs(String.valueOf(Integer.valueOf(zanNumber) - 1));
//                                videoCommentAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
//                                Log.e("123", "取消点赞评论报错：" + error);
                            }
                        }, this, false));
    }

    //点赞评论
    private void requestThumbsCommentVideo(String video_image_id, String comment_id) {
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
                                Toast.makeText(VideoDetailsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
//                                textComment.setIs_thumbs("1");
//                                textComment.setThumbs(String.valueOf(Integer.valueOf(zanNumber) + 1));
//                                videoCommentAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Log.e("123", "点赞评论报错：" + error);
                            }
                        }, this, false));
    }

    //删除评论
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
                                Toast.makeText(VideoDetailsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                imageTextComments.remove(pos);
                                videoCommentAdapter.notifyItemRemoved(pos);
                                if (imageTextComments.size() == 0) {
                                    if (class_data.size() > 0) {
                                        class_data.clear();
                                    }
                                    getCommentList("2", "1");
//                                    initJingXuanDate(false);
                                }
                                videoCommentAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Log.e("123", "删除评论报错：" + error);
                            }
                        }, this, false));
    }

    //添加评论
    private void requestAddComment(String video_image_id, String parent_id, String parent_uid, String content) {
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
                                Log.e("123", "添加评论：" + result.getMsg());
                                Toast.makeText(VideoDetailsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                sendType = "0";
                                PAGE = 1;
                                getCommentList("2", "2");
                                et_edit.setText("");
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Log.e("123", "添加评论报错：" + error);
                            }
                        }, this, false));
    }

    //取消关注
    private void quxiao_guanzhu_submit() {
        Map<String, String> params = new HashMap<>();
        if (null != Uid) {
            params.put("uuid", Uid);
        }
        Log.e("123", "取消关注他：" + params.toString());
        subscription = Network.getInstance("取消关注他", this)
                .quxiaoguanzhuta(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "取消关注他成功：" + result.getCode());
                                Toast.makeText(VideoDetailsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                //切换布局
                                user_attention.setVisibility(View.VISIBLE);
                                user_attention_off.setVisibility(View.GONE);
//                                imageTextDetails.setIs_follow_user("2");
//                                videoCommentAdapter.setIs_follow_user("2");
//                                recyclerView.setAdapter(videoCommentAdapter);
//                                videoCommentAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Logger.e("取消关注他报错：" + error);
                            }
                        }, this, false));
    }

    //关注
    private void guanzhu_submit() {
        Map<String, String> params = new HashMap<>();
        if (null != Uid) {
            params.put("uuid", Uid);
        }
        Log.e("123", "关注他：" + params.toString());
        subscription = Network.getInstance("关注他", this)
                .guanzhuta(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "关注他成功：" + result.getCode());
                                Toast.makeText(VideoDetailsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                //切换布局
                                user_attention_off.setVisibility(View.VISIBLE);
                                user_attention.setVisibility(View.GONE);
//                                imageTextDetails.setIs_follow_user("1");
//                                videoCommentAdapter.setIs_follow_user("1");
//                                recyclerView.setAdapter(videoCommentAdapter);
//                                videoCommentAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Logger.e("关注他报错：" + error);
                            }
                        }, this, false));
    }

    //视频点赞
    private void DianZanNet(String video_id, final String type) {
        Map<String, String> params = new HashMap<>();
        params.put("video_id", video_id);
        Log.e("123", "点赞：" + params.toString());
        subscription = Network.getInstance("点赞成功", VideoDetailsActivity.this)
                .home_videoThumbs(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "       点赞成功        ");
                                Toast.makeText(VideoDetailsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                thumbNumber = thumbNumber + 1;
                                zan_layout.setText(thumbNumber + "点赞 ");
//                                zan_and_ping_layout.setText(thumbNumber + "点赞 " + comentNumber + "评论 "
//                                        + collectionNumber + "收藏");
//                                imageTextDetails.setIs_thumbs("1");
//                                zan_image.setLiked(true);
//                                zan_image.setUnlikeDrawableRes(R.drawable.home_like_on);
//                                videoCommentAdapter.setThumb_number(thumbNumber + " ");
//                                recyclerView.setAdapter(videoCommentAdapter);
//                                videoCommentAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                if ("1".equals(type)) {
                                    Toast.makeText(VideoDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, VideoDetailsActivity.this, false));
    }

    //视频取消点赞
    private void UnDianZanNet(String video_id) {
        Map<String, String> params = new HashMap<>();
        params.put("video_id", video_id);
        Log.e("123", "取消点赞：" + params.toString());
        subscription = Network.getInstance("取消点赞", VideoDetailsActivity.this)
                .home_videoUnthumbs(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "取消点赞成功：" + result.getCode());
                                Toast.makeText(VideoDetailsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                thumbNumber = thumbNumber - 1;
                                zan_layout.setText(thumbNumber + "点赞 ");
//                                zan_and_ping_layout.setText(thumbNumber + "点赞 " + comentNumber + "评论 "
//                                        + collectionNumber + "收藏");
//                                imageTextDetails.setIs_thumbs("2");
//                                zan_image.setLiked(false);
//                                zan_image.setUnlikeDrawableRes(R.drawable.home_like_off);
//                                videoCommentAdapter.setThumb_number(thumbNumber + " ");
//                                recyclerView.setAdapter(videoCommentAdapter);
//                                videoCommentAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(VideoDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }, VideoDetailsActivity.this, false));
    }

    //视频收藏
    private void ShouCangNet(String video_id) {
        Map<String, String> params = new HashMap<>();
        params.put("video_id", video_id);
        Log.e("123", "收藏：" + params.toString());
        subscription = Network.getInstance("收藏", VideoDetailsActivity.this)
                .home_videoCollect(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "收藏成功：" + result.getCode());
                                Toast.makeText(VideoDetailsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                collectionNumber = collectionNumber + 1;
                                coll_layout.setText(collectionNumber + "收藏");
//                                imageTextDetails.setIs_collect("1");
//                                shoucang_image.setLiked(true);
//                                shoucang_image.setUnlikeDrawableRes(R.drawable.home_collect_on);
//                                videoCommentAdapter.setCollection_number(collectionNumber + " ");
//                                recyclerView.setAdapter(videoCommentAdapter);
//                                videoCommentAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(VideoDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }, VideoDetailsActivity.this, false));
    }

    //视频取消收藏
    private void UnShouCangNet(String video_id) {
        Map<String, String> params = new HashMap<>();
        params.put("video_id", video_id);
        Log.e("123", "取消收藏：" + params.toString());
        subscription = Network.getInstance("取消收藏", VideoDetailsActivity.this)
                .home_videoUncollect(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "取消收藏成功：" + result.getCode());
                                Toast.makeText(VideoDetailsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                collectionNumber = collectionNumber - 1;
                                coll_layout.setText(collectionNumber + "收藏");
//                                zan_and_ping_layout.setText(thumbNumber + "点赞 " + comentNumber + "评论 "
//                                        + collectionNumber + "收藏");
//                                imageTextDetails.setIs_collect("2");
//                                shoucang_image.setLiked(false);
//                                shoucang_image.setUnlikeDrawableRes(R.drawable.home_collect_off);
//                                videoCommentAdapter.setCollection_number(collectionNumber + " ");
//                                recyclerView.setAdapter(videoCommentAdapter);
//                                videoCommentAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(VideoDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }, VideoDetailsActivity.this, false));
    }

    private BannerComponent bannerComponent;
    ViewPager viewPager;
    Indicator indicator;
    RecyclerView zhibiao_view;
    FrameLayout fram;

    private void getRefresh(XRecyclerView recyclerView) {
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerView.setArrowImageView(R.drawable.xrefreshview_arrow);
        recyclerView
                .getDefaultRefreshHeaderView()
                .setRefreshTimeVisible(true);
        View header = LayoutInflater.from(this).inflate(R.layout.video_details_hand_layout, (ViewGroup) findViewById(android.R.id.content), false);

        user_llayout = header.findViewById(R.id.user_llayout);
        author_msg_layout = header.findViewById(R.id.author_msg_layout);
        user_msg_layout = header.findViewById(R.id.user_msg_layout);
        user_head_cir = header.findViewById(R.id.user_head_cir);
        user_name_tv = header.findViewById(R.id.user_name_tv);
        user_signature = header.findViewById(R.id.user_signature);
        user_attention = header.findViewById(R.id.user_attention);
        user_attention_off = header.findViewById(R.id.user_attention_off);
        details_content = header.findViewById(R.id.details_content);
        labels = header.findViewById(R.id.labels);
        zan_layout = header.findViewById(R.id.zan_layout);
        comm_layout = header.findViewById(R.id.comm_layout);
        coll_layout = header.findViewById(R.id.coll_layout);
        details_time_layout = header.findViewById(R.id.details_time_layout);
        comm_number = header.findViewById(R.id.comm_number);
        content_layout = header.findViewById(R.id.content_layout);
        //空状态
        empty_layout = header.findViewById(R.id.empty_layout);


        user_msg_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoDetailsActivity.this, FriendPageActivity.class);
                intent.putExtra("UserId", Uid);
                startActivity(intent);
            }
        });

        user_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(VideoDetailsActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(VideoDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    guanzhu_submit();
                }
            }
        });

        user_attention_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(VideoDetailsActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(VideoDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    quxiao_guanzhu_submit();
                }
            }
        });
        /**食谱*/
        viewPager = header.findViewById(R.id.banner_viewPager);
        viewPager.setFocusable(false);
        indicator = header.findViewById(R.id.banner_indicator);
        zhibiao_view = header.findViewById(R.id.zhibiao_view);
        zhibiao_view.setFocusable(false);
        fram = header.findViewById(R.id.fram);
        LinearLayoutManager ms = new LinearLayoutManager(header.getContext());
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置 recyclerview 布局方式为横向布局
        zhibiao_view.setLayoutManager(ms);  //给RecyClerView 添加设置好的布局样式
        /**食谱*/
        recyclerView.addHeaderView(header);
        recyclerView.getDefaultFootView().setLoadingHint("加载中...");
        recyclerView.getDefaultFootView().setNoMoreHint("加载完毕");

        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                PAGE = 1;
                if (null != bannerComponent) {
                    bannerComponent.stopAutoPlay();
                    bannerComponent = null;
                }
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        initJingXuanDate(false);
                        getCommentList("2", "1");
                        if (recyclerView != null) {
                            recyclerView.refreshComplete();
                        }
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                PAGE++;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        getCommentList("2", "1");
                    }
                }, 1000);
            }
        });
    }

    private IndicatorViewPager.IndicatorViewPagerAdapter adapter = new IndicatorViewPager.IndicatorViewPagerAdapter() {
        RoundedImageView banner_img;
        TextView shipu_name, shipu_tips;
        LinearLayout content_layout;

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = new View(container.getContext());
            }
            return convertView;

        }

        @Override
        public View getViewForPage(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = LayoutInflater.from(VideoDetailsActivity.this).inflate(R.layout.banner_item_layout, container, false);
            }

            banner_img = convertView.findViewById(R.id.banner_img);
            shipu_name = convertView.findViewById(R.id.shipu_name);
            shipu_tips = convertView.findViewById(R.id.shipu_tips);
            content_layout = convertView.findViewById(R.id.content_layout);
            //设置用户头像
            Glide.with(VideoDetailsActivity.this).load(bannerEntities.get(position).getFace_path())
                    .asBitmap()
                    .error(R.drawable.touxiang)
                    .into(banner_img);
            shipu_name.setText(bannerEntities.get(position).getName());
            shipu_tips.setText(bannerEntities.get(position).getNutrition_tips());
            content_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //去往食谱详情页
                    IntentPage = "1";
                    Intent intent = new Intent(VideoDetailsActivity.this, BannerDetailWebActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("detail_id", bannerEntities.get(position).getId() + "");
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            });
            return convertView;

        }


        @Override
        public int getCount() {
            return bannerEntities.size();
        }

    };


    public static Dialog dialog;
    //微博分享标题
    private String content;
    //分享类型
    private String ShareType;

    private void select_more_popwindow() {
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
        wl.y = this.getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        function_recyclerView.setVisibility(View.VISIBLE);

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
            if (SharedPreferencesHelper.get(this, "user_id", "").toString().equals(Uid)) {
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
                        StatService.trackCustomKVEvent(VideoDetailsActivity.this, "Details_answer_more_share_wechat", prop);
                        //视频分享
                        if (!TextUtils.isEmpty(imageTextDetails.getContent())) {
                            if (!TextUtils.isEmpty(imageTextDetails.getVideo_data().getFace_path())) {
                                ShareUtil.ShareWechat(VideoDetailsActivity.this, imageTextDetails.getUser_data().getName() + "发布了视频", imageTextDetails.getContent(),
                                        imageTextDetails.getVideo_data().getFace_path(),
                                        Network.VideoUrl + id, "1");
                            } else {
                                ShareUtil.ShareWechat(VideoDetailsActivity.this, imageTextDetails.getUser_data().getName() + "发布了视频", imageTextDetails.getContent(),
                                        Network.ShareImage,
                                        Network.VideoUrl + id, "1");
                            }
                        } else {
                            if (!TextUtils.isEmpty(imageTextDetails.getVideo_data().getFace_path())) {
                                ShareUtil.ShareWechat(VideoDetailsActivity.this, imageTextDetails.getUser_data().getName() + "发布了视频", "快去分享您的印迹吧",
                                        imageTextDetails.getVideo_data().getFace_path(),
                                        Network.VideoUrl + id, "1");
                            } else {
                                ShareUtil.ShareWechat(VideoDetailsActivity.this, imageTextDetails.getUser_data().getName() + "发布了视频", "快去分享您的印迹吧",
                                        Network.ShareImage,
                                        Network.VideoUrl + id, "1");
                            }
                        }
                        ShareType = "2";
                        dialog.dismiss();
                        break;
                    case "朋友圈":
                        prop.setProperty("name", "wechat_moments_layout");
                        StatService.trackCustomKVEvent(VideoDetailsActivity.this, "Details_answer_more_share_wechat_circle", prop);
                        //视频分享
                        if (!TextUtils.isEmpty(imageTextDetails.getContent())) {
                            if (!TextUtils.isEmpty(imageTextDetails.getVideo_data().getFace_path())) {
                                ShareUtil.ShareWechatMoments(VideoDetailsActivity.this, imageTextDetails.getUser_data().getName() + "发布了视频", imageTextDetails.getContent(),
                                        imageTextDetails.getVideo_data().getFace_path(),
                                        Network.VideoUrl + id, "1");
                            } else {
                                ShareUtil.ShareWechatMoments(VideoDetailsActivity.this, imageTextDetails.getUser_data().getName() + "发布了视频", imageTextDetails.getContent(),
                                        Network.ShareImage,
                                        Network.VideoUrl + id, "1");
                            }
                        } else {
                            if (!TextUtils.isEmpty(imageTextDetails.getVideo_data().getFace_path())) {
                                ShareUtil.ShareWechatMoments(VideoDetailsActivity.this, imageTextDetails.getUser_data().getName() + "发布了视频", "快去分享您的印迹吧",
                                        imageTextDetails.getVideo_data().getFace_path(),
                                        Network.VideoUrl + id, "1");
                            } else {
                                ShareUtil.ShareWechatMoments(VideoDetailsActivity.this, imageTextDetails.getUser_data().getName() + "发布了视频", "快去分享您的印迹吧",
                                        Network.ShareImage,
                                        Network.VideoUrl + id, "1");
                            }
                        }
                        ShareType = "1";
                        dialog.dismiss();
                        break;
                    case "新浪微博":
                        prop.setProperty("name", "sinaWeibo_layout");
                        StatService.trackCustomKVEvent(VideoDetailsActivity.this, "Details_answer_more_share_sina", prop);
                        //视频分享
                        if (!TextUtils.isEmpty(imageTextDetails.getContent())) {
                            if (!TextUtils.isEmpty(imageTextDetails.getVideo_data().getFace_path())) {
                                ShareUtil.ShareWeiBo(VideoDetailsActivity.this, imageTextDetails.getUser_data().getName() + "发布了视频", imageTextDetails.getContent(),
                                        imageTextDetails.getVideo_data().getFace_path(),
                                        Network.VideoUrl + id, "1");
                            } else {
                                ShareUtil.ShareWeiBo(VideoDetailsActivity.this, imageTextDetails.getUser_data().getName() + "发布了视频", imageTextDetails.getContent(),
                                        Network.ShareImage,
                                        Network.VideoUrl + id, "1");
                            }
                        } else {
                            if (!TextUtils.isEmpty(imageTextDetails.getVideo_data().getFace_path())) {
                                ShareUtil.ShareWeiBo(VideoDetailsActivity.this, imageTextDetails.getUser_data().getName() + "发布了视频", "快去分享您的印迹吧",
                                        imageTextDetails.getVideo_data().getFace_path(),
                                        Network.VideoUrl + id, "1");
                            } else {
                                ShareUtil.ShareWeiBo(VideoDetailsActivity.this, imageTextDetails.getUser_data().getName() + "发布了视频", "快去分享您的印迹吧",
                                        Network.ShareImage,
                                        Network.VideoUrl + id, "1");
                            }
                        }
                        ShareType = "3";
                        dialog.dismiss();
                        break;
                    case "QQ":
                        prop.setProperty("name", "QQ_layout");
                        StatService.trackCustomKVEvent(VideoDetailsActivity.this, "Details_answer_more_share_qq", prop);
                        //视频分享
                        if (!TextUtils.isEmpty(imageTextDetails.getContent())) {
                            if (!TextUtils.isEmpty(imageTextDetails.getVideo_data().getFace_path())) {
                                ShareUtil.ShareQQ(VideoDetailsActivity.this, imageTextDetails.getUser_data().getName() + "发布了视频", imageTextDetails.getContent(),
                                        imageTextDetails.getVideo_data().getFace_path(),
                                        Network.VideoUrl + id, "1");
                            } else {
                                ShareUtil.ShareQQ(VideoDetailsActivity.this, imageTextDetails.getUser_data().getName() + "发布了视频", imageTextDetails.getContent(),
                                        Network.ShareImage,
                                        Network.VideoUrl + id, "1");
                            }
                        } else {
                            if (!TextUtils.isEmpty(imageTextDetails.getVideo_data().getFace_path())) {
                                ShareUtil.ShareQQ(VideoDetailsActivity.this, imageTextDetails.getUser_data().getName() + "发布了视频", "快去分享您的印迹吧",
                                        imageTextDetails.getVideo_data().getFace_path(),
                                        Network.VideoUrl + id, "1");
                            } else {
                                ShareUtil.ShareQQ(VideoDetailsActivity.this, imageTextDetails.getUser_data().getName() + "发布了视频", "快去分享您的印迹吧",
                                        Network.ShareImage,
                                        Network.VideoUrl + id, "1");
                            }
                        }
                        ShareType = "4";
                        dialog.dismiss();
                        break;
                    case "QQ空间":
                        prop.setProperty("name", "QQzone_layout");
                        StatService.trackCustomKVEvent(VideoDetailsActivity.this, "Details_answer_more_share_qqzone", prop);
                        //视频分享
                        if (!TextUtils.isEmpty(imageTextDetails.getContent())) {
                            if (!TextUtils.isEmpty(imageTextDetails.getVideo_data().getFace_path())) {
                                ShareUtil.ShareQZone(VideoDetailsActivity.this, imageTextDetails.getUser_data().getName() + "发布了视频", imageTextDetails.getContent(),
                                        imageTextDetails.getVideo_data().getFace_path(),
                                        Network.VideoUrl + id, "1");
                            } else {
                                ShareUtil.ShareQZone(VideoDetailsActivity.this, imageTextDetails.getUser_data().getName() + "发布了视频", imageTextDetails.getContent(),
                                        Network.ShareImage,
                                        Network.VideoUrl + id, "1");
                            }
                        } else {
                            if (!TextUtils.isEmpty(imageTextDetails.getVideo_data().getFace_path())) {
                                ShareUtil.ShareQZone(VideoDetailsActivity.this, imageTextDetails.getUser_data().getName() + "发布了视频", "快去分享您的印迹吧",
                                        imageTextDetails.getVideo_data().getFace_path(),
                                        Network.VideoUrl + id, "1");
                            } else {
                                ShareUtil.ShareQZone(VideoDetailsActivity.this, imageTextDetails.getUser_data().getName() + "发布了视频", "快去分享您的印迹吧",
                                        Network.ShareImage,
                                        Network.VideoUrl + id, "1");
                            }
                        }
                        ShareType = "5";
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
                        if ("".equals(SharedPreferencesHelper.get(VideoDetailsActivity.this, "login_token", ""))) {
                            Intent intent = new Intent(VideoDetailsActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            prop.setProperty("name", "bianji_layout");
                            StatService.trackCustomKVEvent(VideoDetailsActivity.this, "Details_answer_more_edite", prop);
                        }
                        dialog.dismiss();
                        break;
                    case "举报":
                        prop.setProperty("name", "jubao_layout");
                        StatService.trackCustomKVEvent(VideoDetailsActivity.this, "Details_answer_more_report", prop);
                        if ("".equals(SharedPreferencesHelper.get(VideoDetailsActivity.this, "login_token", ""))) {
                            Intent intent = new Intent(VideoDetailsActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            report_msg_question();
                        }
                        dialog.dismiss();
                        break;
                    case "删除":
                        dialog.dismiss();
                        if ("".equals(SharedPreferencesHelper.get(VideoDetailsActivity.this, "login_token", ""))) {
                            Intent intent = new Intent(VideoDetailsActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            prop.setProperty("name", "delete_layout");
                            StatService.trackCustomKVEvent(VideoDetailsActivity.this, "Details_answer_more_delete", prop);
                            PopWindowHelper.public_tishi_pop(VideoDetailsActivity.this, "删除印迹", "是否删除该印迹？", "否", "是", new DialogCallBack() {
                                @Override
                                public void save() {
                                    deleteVideo();
                                }

                                @Override
                                public void cancel() {

                                }
                            });
                        }
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
                StatService.trackCustomKVEvent(VideoDetailsActivity.this, "Details_answer_cancel", prop);
                dialog.dismiss();
            }
        });
    }

    private void select_report_popwindow(List<ReportMsg> msgs) {
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.report_popwindow, null);
        //举报信息
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayout report_btn = view.findViewById(R.id.report_btn);
        tv_report = view.findViewById(R.id.tv_report);
        report_title = view.findViewById(R.id.report_title);
        report_title.setText("举报视频");

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
//                    report_question(answerDate.getId() + "", ids);
                    reportImage(ids);
                } else {
                    Toast.makeText(VideoDetailsActivity.this, "请选择举报内容", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Handler handler1 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 500:
                    tv_report.setTextColor(getResources().getColor(R.color.bottom_color));
                    break;
                case 501:
                    tv_report.setTextColor(getResources().getColor(R.color.bottom_color));
                    break;
                case 502:
                    tv_report.setTextColor(getResources().getColor(R.color.meishititle));
                    break;
            }

        }
    };

    /**
     * 获取举报视频数据
     */
    private void report_msg_question() {
        subscription = Network.getInstance("获取举报问题类型", this)
                .getReportVideoType(new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<ReportMsg>>>() {
                    @Override
                    public void onNext(Bean<List<ReportMsg>> result) {
                        if (data.size() > 0) {
                            data.clear();
                        }
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

    //举报视频
    private void reportImage(String ids) {
        Map<String, String> params = new HashMap<>();
        params.put("video_id", id);
        params.put("type", ids);
        Log.e("舉報视频：", params.toString());
        subscription = Network.getInstance("舉報视频", this)
                .getReportVideo(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "舉報视频成功" + result.getCode());
                                Toast.makeText(VideoDetailsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Logger.e("舉報视频报错：" + error);
                                dialog.dismiss();
                            }
                        }, this, false));
    }

    //刪除视频
    private void deleteVideo() {
        Map<String, String> params = new HashMap<>();
        params.put("video_id", id);
        Log.e("刪除视频：", params.toString());
        subscription = Network.getInstance("刪除视频", this)
                .getDeleteVideo(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "刪除视频成功" + result.getCode());
                                Toast.makeText(VideoDetailsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Logger.e("刪除视频报错：" + error);
                            }
                        }, this, false));
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Log.e("123","   exoPlayerManager.onBackPressed()       "+exoPlayerManager.onBackPressed());
//                ExoUserPlayer manualPlayer = VideoPlayerManager.getInstance().getVideoPlayer();
//                if (manualPlayer != null) {
//                    Contacts.currPosition = manualPlayer.getCurrentPosition();
//                }
//                isBack = true;
//                finish();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}

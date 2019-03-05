package com.app.cookbook.xinhe.foodfamily.shiyujia.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.app.cookbook.xinhe.foodfamily.main.PhotoBrowserActivity;
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
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.ImageTextDetailAdapter;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.ZhiZhenAdapter;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.BannerEntity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.ImageTextComment;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.ImageTextDetails;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.RecyclerUtil;
import com.app.cookbook.xinhe.foodfamily.shiyujia.view.GlideImageLoader;
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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
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
import com.tencent.stat.StatService;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;
import rx.Subscription;

public class ImageTextDetailActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    protected Subscription subscription;
    //标题布局
    @BindView(R.id.ll_header_content)
    RelativeLayout mHeaderContent;
    @BindView(R.id.title_user_alyout)
    LinearLayout title_user_alyout;
    //返回按钮
    @BindView(R.id.back_im)
    ImageView back_im;
    //头像
    @BindView(R.id.writer_user_head)
    CircleImageView writer_user_head;
    //名称
    @BindView(R.id.writer_user_name)
    TextView writer_user_name;
    //更多
    @BindView(R.id.more_im)
    ImageView more_im;
    @BindView(R.id.recyclerView)
    XRecyclerView recyclerView;

    //轮播图
    //评论输入框
    @BindView(R.id.edit_alyout)
    RelativeLayout edit_alyout;
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

    //详情作者布局
    private RelativeLayout hand_layout;
    private RelativeLayout imageText_msg_layout;
    private Banner home_details_kanner;
    private RelativeLayout user_msg_layout;
    private CircleImageView user_head;
    private TextView user_name;
    private TextView user_signature;
    private TextView user_attention;
    private TextView user_attention_off;
    private TextView details_content;
    private LabelsView labels;
    private TextView zan_layout;
    private TextView comm_layout;
    private TextView coll_layout;
    private TextView details_time_layout;
    private TextView comm_number;
    private RelativeLayout content_layout;
    private RelativeLayout empty_layout;
    //菜谱

    //获取详情数据
    private ImageTextDetails imageTextDetails;
    //图片集合
    private List<String> imageList = new ArrayList<>();
    //图片比例集合
    private List<String> imageRatio = new ArrayList<>();
    //评论集合
    private List<ImageTextComment.DataBean> imageTextComments = new ArrayList<>();
    //评论适配器
    private ImageTextDetailAdapter imageTextDetailAdapter;
    private LinearLayoutManager linearLayoutManagerComment;

    //图文ID
    private String id;
    private int mHeight;
    private int mheight;
    private int mWidth;
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
    //是否收藏
    private String is_collect;
    //关注用户
    private String is_follow_user;
    //点赞数
    private int thumbNumber;
    //评论数
    private int comentNumber;
    //收藏数
    private int collectionNumber;
    //用户id
    private String Uid;
    //头像地址
    private String avatar;
    //用户名称
    private String name;
    //评论页数
    private int PAGE = 1;
    //是否有更多
    private boolean is_not_more;
    //评论某一条数据
    private ImageTextComment.DataBean textComment;
    //评论某一条点赞数
    private String zanNumber;
    private int pos;
    //判断是否弹出键盘
    private String tan;

    private String sendType = "0";

    private List<ImageTextDetails.ClassDataBean> class_data = new ArrayList<>();
    List<BannerEntity> bannerEntities = new ArrayList<>();
    ZhiZhenAdapter zhiZhenAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            textComment = imageTextComments.get(msg.arg1);
            String is_thumbs = textComment.getIs_thumbs();
            zanNumber = textComment.getThumbs();
            pos = msg.arg1;
            Intent intent = null;
            switch (msg.what) {
                case 400:
                    intent = new Intent(ImageTextDetailActivity.this, FriendPageActivity.class);
                    intent.putExtra("UserId", textComment.getUser_data().getId());
                    startActivity(intent);
                    break;
                case 401:
                    intent = new Intent(ImageTextDetailActivity.this, CommtentReplyActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("comment_id", textComment.getId());
                    intent.putExtra("total_id", textComment.getTotal_id());
                    intent.putExtra("is_thumbs", textComment.getIs_thumbs());
                    startActivity(intent);
                    break;
                case 402:
//                    if ("".equals(SharedPreferencesHelper.get(ImageTextDetailActivity.this, "login_token", ""))) {
//                        intent = new Intent(ImageTextDetailActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                    } else {
                        if ("2".equals(is_thumbs)) {
                            //点赞
                            textComment.setIs_thumbs("1");
                            textComment.setThumbs(String.valueOf(Integer.valueOf(zanNumber) + 1));
                            imageTextDetailAdapter.notifyDataSetChanged();
                            requestThumbsComment(id, textComment.getTotal_id());
                        } else if ("1".equals(is_thumbs)) {
                            //取消点赞
                            textComment.setIs_thumbs("2");
                            textComment.setThumbs(String.valueOf(Integer.valueOf(zanNumber) - 1));
                            imageTextDetailAdapter.notifyDataSetChanged();
                            requestUnthumbsComment(id, textComment.getTotal_id());
                        }
//                    }
                    break;
                case 403:
                    intent = new Intent(ImageTextDetailActivity.this, CommtentReplyActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("comment_id", textComment.getId());
                    intent.putExtra("total_id", textComment.getTotal_id());
                    intent.putExtra("is_thumbs", textComment.getIs_thumbs());
                    startActivity(intent);
                    break;
                case 404:
                    PopWindowHelper.public_tishi_pop(ImageTextDetailActivity.this, "删除评论", "是否删除该评论？", "否", "是", new DialogCallBack() {
                        @Override
                        public void save() {
                            requestDeleteVideoComment(id, textComment.getId());
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_text_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        /**设置食谱数据*/
        imageTextDetailAdapter = new ImageTextDetailAdapter(this, handler);
        imageTextDetailAdapter.setImageTextComments(imageTextComments);
        recyclerView.setAdapter(imageTextDetailAdapter);
        initJingXuanDate();
        getCommentList("0");
        getScrollListener();
    }


    private void initView() {
        id = getIntent().getStringExtra("id");
        tan = getIntent().getStringExtra("tan");

        linearLayoutManagerComment = new LinearLayoutManager(this);
        linearLayoutManagerComment.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManagerComment);
        recyclerView.setFocusable(true);
        getRefresh(recyclerView);


        mHeaderContent.setOnClickListener(this);
        title_user_alyout.setOnClickListener(this);
        back_im.setOnClickListener(this);
        more_im.setOnClickListener(this);
        comment_layout.setOnClickListener(this);
        send_reply.setOnClickListener(this);
        shaer_layout.setOnClickListener(this);
        user_attention_off.setOnClickListener(this);
        user_attention.setOnClickListener(this);
        user_msg_layout.setOnClickListener(this);
        et_edit.setOnEditorActionListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        switch (messageEvent.getMessage()) {
            case "121":
                TopSnackBar.make(mHeaderContent, "分享成功", BaseTransientBottomBar.LENGTH_SHORT, ImageTextDetailActivity.this).show();
                Properties prop = new Properties();
                prop.setProperty("share_time", "分享后返回时长");
                StatService.trackCustomEndKVEvent(this, "Share_back_time", prop);
                break;
            case "1":
                initJingXuanDate();
                getCommentList("0");
                break;
        }
    }

    private void getOnClick() {
        //点赞
        zan_image.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                //按钮提示音
                if ("".equals(SharedPreferencesHelper.get(ImageTextDetailActivity.this, "login_token", ""))) {
                    zan_image.setLiked(false);
                    zan_image.setUnlikeDrawableRes(R.drawable.home_like_off);
                    Intent intent = new Intent(ImageTextDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    DianZanNet(id, "1");
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                //按钮提示音
                if ("".equals(SharedPreferencesHelper.get(ImageTextDetailActivity.this, "login_token", ""))) {
                    zan_image.setLiked(true);
                    zan_image.setUnlikeDrawableRes(R.drawable.home_like_on);
                    Intent intent = new Intent(ImageTextDetailActivity.this, LoginActivity.class);
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
                StatService.trackCustomKVEvent(ImageTextDetailActivity.this, "Details_answer_collection", prop);
                if ("".equals(SharedPreferencesHelper.get(ImageTextDetailActivity.this, "login_token", ""))) {
                    shoucang_image.setLiked(false);
                    shoucang_image.setUnlikeDrawableRes(R.drawable.home_collect_off);
                    Intent intent = new Intent(ImageTextDetailActivity.this, LoginActivity.class);
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
                StatService.trackCustomKVEvent(ImageTextDetailActivity.this, "Details_answer_collection", prop);
                if ("".equals(SharedPreferencesHelper.get(ImageTextDetailActivity.this, "login_token", ""))) {
                    shoucang_image.setLiked(true);
                    shoucang_image.setUnlikeDrawableRes(R.drawable.home_collect_on);
                    Intent intent = new Intent(ImageTextDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    UnShouCangNet(id);
                }
            }
        });
        et_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    send_reply.setTextColor(getResources().getColor(R.color.color_292c31));
                } else {
                    send_reply.setTextColor(getResources().getColor(R.color.color_bbbbbb));
                }
                if (s.length() == 140) {
                    Textutil.ToastUtil(ImageTextDetailActivity.this, "上限140个字");
                }
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
    }

    private void getItemClick() {
        imageTextDetailAdapter.setOnItemClickListener(new ImageTextDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!TextUtils.isEmpty(imageTextComments.get(position).getId())) {
                    Intent intent = new Intent(ImageTextDetailActivity.this, CommtentReplyActivity.class);
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
        if (bannerEntities.size() > 0) {
            bannerEntities.clear();
        }
        if (null != imageTextDetails.getBanners() && imageTextDetails.getBanners().size() > 0) {
            fram.setVisibility(View.VISIBLE);
            bannerEntities = imageTextDetails.getBanners();
            //设置间隔指针
            zhiZhenAdapter = new ZhiZhenAdapter(bannerEntities, ImageTextDetailActivity.this);
            zhibiao_view.setAdapter(zhiZhenAdapter);
            //设置banner
            indicator.setScrollBar(new LayoutBar(ImageTextDetailActivity.this, R.layout.banner_item, ScrollBar.Gravity.CENTENT_BACKGROUND));
            viewPager.setOffscreenPageLimit(2);
            bannerComponent = new BannerComponent(indicator, viewPager, false);
            bannerComponent.setAdapter(adapter);
            bannerComponent.setAutoPlayTime(2500);
            bannerComponent.startAutoPlay();
        } else {
            fram.setVisibility(View.GONE);
        }
    }

    private void getScrollListener() {
        if (!TextUtils.isEmpty(tan)) {
            et_edit.setFocusable(true);
            et_edit.setFocusableInTouchMode(true);
            et_edit.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }

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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int height = RecyclerUtil.getDistance(recyclerView) - recyclerView.getMeasuredHeight();
                ViewTreeObserver vto = hand_layout.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mHeight = hand_layout.getMeasuredHeight();
                    }
                });

                if (RecyclerUtil.getDistance(recyclerView) > mHeight) {
                    if (RecyclerUtil.getDistance(recyclerView) == 0 && height < 0) {
                        mHeaderContent.setBackgroundResource(R.drawable.image_text_bg);
                        writer_user_head.setVisibility(View.GONE);
                        writer_user_name.setVisibility(View.GONE);
                        mHeaderContent.setBackgroundColor(Color.argb(0, 48, 63, 159));
                    } else if (dy < 0) {
                        //向上滑动
//                        Log.e("123", "      " + RecyclerUtil.getDistance(recyclerView) + "      " + mheight + "       " + RecyclerUtil.getDistanceTwo(recyclerView) + "   " + hand_layout.getVisibility());
                        if (RecyclerUtil.getDistance(recyclerView) < mHeight) {
//                            Log.e("123", "      +++++>     ");
                            mHeaderContent.setBackgroundResource(R.drawable.image_text_bg);
                            writer_user_head.setVisibility(View.GONE);
                            writer_user_name.setVisibility(View.GONE);
                            mHeaderContent.setBackgroundColor(Color.argb(0, 48, 63, 159));
                        } else if (RecyclerUtil.getDistance(recyclerView) - mHeight < mWidth) {
//                            Log.e("123", "      ----->     ");
                            mHeaderContent.setBackgroundResource(R.drawable.image_text_bg);
                            writer_user_head.setVisibility(View.GONE);
                            writer_user_name.setVisibility(View.GONE);
                            mHeaderContent.setBackgroundColor(Color.argb(0, 48, 63, 159));
                        }
                    } else if (dy > 0) {
                        //向下滑动
                        if ((RecyclerUtil.getDistance(recyclerView) - mHeight) > mWidth) {
                            writer_user_head.setVisibility(View.VISIBLE);
                            writer_user_name.setVisibility(View.VISIBLE);
                            if (!TextUtils.isEmpty(avatar)) {
                                Glide.with(ImageTextDetailActivity.this)
                                        .load(avatar)
                                        .error(R.drawable.touxiang)
                                        .into(writer_user_head);
                                writer_user_name.setText(name);
                                Glide.with(ImageTextDetailActivity.this).load(avatar)
                                        .bitmapTransform(new BlurTransformation(ImageTextDetailActivity.this, 30), new CenterCrop(ImageTextDetailActivity.this))
                                        .into(new ViewTarget<View, GlideDrawable>(mHeaderContent) {
                                            //括号里为需要加载的控件
                                            @Override
                                            public void onResourceReady(GlideDrawable resource,
                                                                        GlideAnimation<? super GlideDrawable> glideAnimation) {
                                                this.view.setBackground(resource.getCurrent());
                                            }
                                        });
                            }
                        } else if (height < 0 && RecyclerUtil.getDistance(recyclerView) < mHeight) {
                            writer_user_head.setVisibility(View.VISIBLE);
                            writer_user_name.setVisibility(View.VISIBLE);
                            if (!TextUtils.isEmpty(avatar)) {
                                Glide.with(ImageTextDetailActivity.this)
                                        .load(avatar)
                                        .error(R.drawable.touxiang)
                                        .into(writer_user_head);
                                writer_user_name.setText(name);
                                Glide.with(ImageTextDetailActivity.this)
                                        .load(avatar)
                                        .crossFade(100)
                                        .bitmapTransform(new BlurTransformation(ImageTextDetailActivity.this, 23, 4))
                                        .into(new ViewTarget<View, GlideDrawable>(mHeaderContent) {
                                            //括号里为需要加载的控件
                                            @Override
                                            public void onResourceReady(GlideDrawable resource,
                                                                        GlideAnimation<? super GlideDrawable> glideAnimation) {
                                                this.view.setBackground(resource.getCurrent());
                                            }
                                        });
                                Glide.with(ImageTextDetailActivity.this)
                                        .load(avatar)
                                        .crossFade(100)
                                        .bitmapTransform(new BlurTransformation(ImageTextDetailActivity.this, 23, 4))
                                        .into(new ViewTarget<View, GlideDrawable>(mHeaderContent) {
                                            //括号里为需要加载的控件
                                            @Override
                                            public void onResourceReady(GlideDrawable resource,
                                                                        GlideAnimation<? super GlideDrawable> glideAnimation) {
                                                this.view.setBackground(resource.getCurrent());
                                            }
                                        });
                            }
                        }
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_header_content:
                break;
            case R.id.title_user_alyout:
                intent = new Intent(ImageTextDetailActivity.this, FriendPageActivity.class);
                intent.putExtra("UserId", Uid);
                startActivity(intent);
                break;
            case R.id.back_im:
                finish();
                break;
            case R.id.more_im:
                if (functionListem.size() > 0) {
                    functionListem.clear();
                }
                select_more_popwindow();
                break;
            case R.id.user_msg_layout:
                intent = new Intent(ImageTextDetailActivity.this, FriendPageActivity.class);
                intent.putExtra("UserId", Uid);
                startActivity(intent);
                break;
            case R.id.user_attention:
                if ("".equals(SharedPreferencesHelper.get(ImageTextDetailActivity.this, "login_token", ""))) {
                    intent = new Intent(ImageTextDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    guanzhu_submit();
                }
                break;
            case R.id.user_attention_off:
                if ("".equals(SharedPreferencesHelper.get(ImageTextDetailActivity.this, "login_token", ""))) {
                    intent = new Intent(ImageTextDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    quxiao_guanzhu_submit();
                }
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
                if ("".equals(SharedPreferencesHelper.get(ImageTextDetailActivity.this, "login_token", ""))) {
                    intent = new Intent(ImageTextDetailActivity.this, LoginActivity.class);
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

    private void initJingXuanDate() {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        Log.e("图文详情：", id);
        subscription = Network.getInstance("图文详情", this)
                .getImageTextDetail(params,
                        new ProgressSubscriberNew<>(ImageTextDetails.class, new GsonSubscriberOnNextListener<ImageTextDetails>() {
                            @Override
                            public void on_post_entity(ImageTextDetails result) {
                                Log.e("123", "     图文详情      " + result);
                                imageTextDetails = result;
                                initDate(result);
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "     图文详情报错" + error);
                            }
                        }, this, true));
    }

    private void initDate(ImageTextDetails result) {
        is_delete = imageTextDetails.getIs_existence();
        is_update = imageTextDetails.getIs_update();
        Uid = imageTextDetails.getUser_id();
        avatar = result.getUser_data().getAvatar();
        name = result.getUser_data().getName();
        if (!TextUtils.isEmpty(result.getThumb_number())) {
            thumbNumber = Integer.valueOf(result.getThumb_number());
        }
        //评论数
        if (!TextUtils.isEmpty(result.getComent_number())) {
            comentNumber = Integer.valueOf(result.getComent_number());
        }
        if (!TextUtils.isEmpty(result.getCollection_number())) {
            collectionNumber = Integer.valueOf(result.getCollection_number());
        }

        is_follow_user = imageTextDetails.getIs_follow_user();
        String user_id = SharedPreferencesHelper.get(ImageTextDetailActivity.this, "user_id", "").toString();
        if (!TextUtils.isEmpty(user_id)) {
            Log.e("123", "  user_id  " + user_id + "    Uid  " + Uid);
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

        if ("1".equals(result.getIs_thumbs())) {
            zan_image.setLiked(true);
            zan_image.setLikeDrawableRes(R.drawable.home_like_on);
        } else if ("2".equals(result.getIs_thumbs())) {
            zan_image.setLiked(false);
            zan_image.setUnlikeDrawableRes(R.drawable.home_like_off);
        }
        if ("1".equals(result.getIs_collect())) {
            shoucang_image.setLiked(true);
            shoucang_image.setLikeDrawableRes(R.drawable.home_collect_on);
        } else if ("2".equals(result.getIs_collect())) {
            shoucang_image.setLiked(false);
            shoucang_image.setUnlikeDrawableRes(R.drawable.home_collect_off);
        }

        if (imageList.size() > 0 && PAGE == 1) {
            imageList.clear();
            imageRatio.clear();
        }
        if (result.getImg_data() != null) {
            for (int i = 0; i < result.getImg_data().size(); i++) {
                imageList.add(result.getImg_data().get(i).getPath());
                imageRatio.add(result.getImg_data().get(i).getSize());
            }
        }
        if ("1:1".equals(result.getImg_data().get(0).getSize()) || "1,1".equals(result.getImg_data().get(0).getSize())) {
            Resources resources = ImageTextDetailActivity.this.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) home_details_kanner.getLayoutParams();
            lp.height = width;
            home_details_kanner.setLayoutParams(lp);
        } else if ("3:4".equals(result.getImg_data().get(0).getSize()) || "3,4".equals(result.getImg_data().get(0).getSize())) {
            Resources resources = ImageTextDetailActivity.this.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) home_details_kanner.getLayoutParams();
            lp.height = width + 240;
            home_details_kanner.setLayoutParams(lp);
        }
        home_details_kanner.setImages(imageList)
                .setBannerTitles(null)
                .setImageLoader(new GlideImageLoader())
                .start();
        home_details_kanner.updateBannerStyle(BannerConfig.NUM_INDICATOR);
        initbanner();
        details_content.setText(result.getContent());
        home_details_kanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (imageList != null) {
                    String strings[] = new String[imageList.size()];
                    for (int i = 0; i < imageList.size(); i++) {
                        strings[i] = imageList.get(i);
                    }
                    if (strings.length > 0) {
                        Intent intent = new Intent();
                        intent.putExtra("imageUrls", strings);
                        intent.putExtra("imageType", "2");
                        intent.setClass(ImageTextDetailActivity.this, PhotoBrowserActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        Glide.with(ImageTextDetailActivity.this)
                .load(result.getUser_data().getAvatar())
                .error(R.drawable.touxiang)
                .into(user_head);

        user_name.setText(result.getUser_data().getName());
        user_signature.setText(result.getUser_data().getPersonal_signature());

        zan_layout.setText(result.getThumb_number() + "点赞 ");
        comm_layout.setText(result.getComent_number() + "评论 ");
        coll_layout.setText(result.getCollection_number() + "收藏");
        details_time_layout.setText(FormatCurrentData.getTimeRange(Long.valueOf(result.getCreated_at())));
        comm_number.setText("全部评论(" + result.getComent_number() + ")");
        if (result.getClass_data() != null) {
            labels.setLabels(result.getClass_data(), new LabelsView.LabelTextProvider<ImageTextDetails.ClassDataBean>() {
                @Override
                public CharSequence getLabelText(TextView label, int position, ImageTextDetails.ClassDataBean data) {
                    return "# " + data.getTitle();
                }
            });
            labels.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
                @Override
                public void onLabelClick(TextView label, Object data, int position) {
                    ImageTextDetails.ClassDataBean item = result.getClass_data().get(position);
                    Intent intent = new Intent(ImageTextDetailActivity.this, LabelDetailsActivity.class);
                    intent.putExtra("id", item.getId());
                    startActivity(intent);
                }
            });
        } else {
            labels.setVisibility(View.GONE);
        }
        if (bannerEntities.size() == 0 && imageTextDetails.getBanners() != null && imageTextDetails.getBanners().size() > 0 && PAGE == 1) {
            setBannerMsg();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // release memory
        if (recyclerView != null) {
            recyclerView.destroy();
            recyclerView = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

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
        View header = LayoutInflater.from(this).inflate(R.layout.image_text_hand_layout, (ViewGroup) findViewById(android.R.id.content), false);

        hand_layout = header.findViewById(R.id.hand_layout);
        hand_layout.setFocusable(false);
        imageText_msg_layout = header.findViewById(R.id.imageText_msg_layout);
        home_details_kanner = header.findViewById(R.id.home_details_kanner);
        home_details_kanner.setFocusable(false);
        user_msg_layout = header.findViewById(R.id.user_msg_layout);
        user_head = header.findViewById(R.id.user_head);
        user_name = header.findViewById(R.id.user_name);
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
        empty_layout = header.findViewById(R.id.empty_layout);

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

        Resources resources = ImageTextDetailActivity.this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        mheight = height;
        mWidth = width;

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
                        initJingXuanDate();
                        getCommentList("0");

                        if (recyclerView != null) {
                            recyclerView.refreshComplete();
                        }
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                PAGE++;
//                if(null!=bannerComponent){
//                    bannerComponent.stopAutoPlay();
//                    bannerComponent = null;
//                }
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        getCommentList("0");
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
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.banner_item_layout, container, false);
            }

            banner_img = convertView.findViewById(R.id.banner_img);
            shipu_name = convertView.findViewById(R.id.shipu_name);
            shipu_tips = convertView.findViewById(R.id.shipu_tips);
            content_layout = convertView.findViewById(R.id.content_layout);
            //设置用户头像
            Glide.with(getApplicationContext()).load(bannerEntities.get(position).getFace_path())
                    .asBitmap()
                    .error(R.drawable.touxiang)
                    .into(banner_img);
            shipu_name.setText(bannerEntities.get(position).getName());
            shipu_tips.setText(bannerEntities.get(position).getNutrition_tips());
            content_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //去往食谱详情页
                    Intent intent = new Intent(ImageTextDetailActivity.this, BannerDetailWebActivity.class);
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


    private void initbanner() {
        home_details_kanner.setOnPageChangeListeners(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.e("123", "     position     " + position);
                if ("1:1".equals(imageRatio.get(position)) || "1,1".equals(imageRatio.get(position))) {
                    Resources resources = ImageTextDetailActivity.this.getResources();
                    DisplayMetrics dm = resources.getDisplayMetrics();
                    int width = dm.widthPixels;
//                    int height = dm.heightPixels;
//                    mHeight = height;
//                    mWidth = width;
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) home_details_kanner.getLayoutParams();
                    lp.height = width;
                    home_details_kanner.setLayoutParams(lp);
                } else if ("3:4".equals(imageRatio.get(position)) || "3,4".equals(imageRatio.get(position))) {
                    Resources resources = ImageTextDetailActivity.this.getResources();
                    DisplayMetrics dm = resources.getDisplayMetrics();
                    int width = dm.widthPixels;
//                    int height = dm.heightPixels;
//                    mHeight = height;
//                    mWidth = width;
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) home_details_kanner.getLayoutParams();
                    lp.height = width + 240;
                    home_details_kanner.setLayoutParams(lp);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
        });
    }


    //评论列表
    private String NoMsg = "0";

    private void getCommentList(String start) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("page", String.valueOf(PAGE));
        Log.e("123", "图文评论" + params.toString());
        subscription = Network.getInstance("图文评论", this)
                .getImageTextComment(params, new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<ImageTextComment>>() {
                    @Override
                    public void onNext(Bean<ImageTextComment> result) {
                        Log.e("123", "    图文评论result.getData().size()   " + result.getData().getData().size());
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
                        if (imageTextComments.size() > 0 && PAGE == 1) {
                            imageTextComments.clear();
                        }
                        if ("2".equals(start)) {
                            comentNumber = comentNumber + 1;
                            comm_layout.setText(comentNumber + "评论 ");
                            comm_number.setText("全部评论(" + comentNumber + ")");
                        }

                        if (imageTextComments.size() > 0) {//这表示是"加载"
                            if (result.getData().getData().size() == 0) {
                                is_not_more = true;
                                imageTextComments.addAll(result.getData().getData());
                                imageTextDetailAdapter.notifyDataSetChanged();
                            } else {
                                is_not_more = false;
                                imageTextComments.addAll(result.getData().getData());
                                imageTextDetailAdapter.notifyDataSetChanged();
                            }
                        } else {
                            imageTextComments = result.getData().getData();
                            if (imageTextComments.size() > 0) {
                                imageTextDetailAdapter.setImageTextComments(imageTextComments);
                                recyclerView.setAdapter(imageTextDetailAdapter);
                            }
                        }
                        if (imageTextComments.size() > 0) {
                            getOnClick();
                            getItemClick();
                        }

//                        if (imageTextDetails.getBanners() != null && imageTextDetails.getBanners().size() > 0 && PAGE == 1 && bannerEntities.size() == 0) {
//                            fram.setVisibility(View.VISIBLE);
//                            setBannerMsg();
//                        } else if (imageTextDetails.getBanners() == null || imageTextDetails.getBanners().size() == 0 && PAGE == 1) {
//                            fram.setVisibility(View.GONE);
//                        }


//                        else if (imageTextDetails.getBanners() == null || imageTextDetails.getBanners().size() == 0 && PAGE == 1) {
//                            fram.setVisibility(View.GONE);
//                        }
                    }

                    @Override
                    public void onError(String error) {
                        if ("无数据".equals(error)) {
                            NoMsg = "1";
                        }
                        Logger.e("图文评论异常：" + error.toString());
                    }
                }, this, false));
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
                .getAddComment(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "添加评论：" + result.getCode());
                                Toast.makeText(ImageTextDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                PAGE = 1;
                                recyclerView.setNoMore(false);
                                getCommentList("2");
                                sendType = "0";
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

    //取消点赞评论
    private void requestUnthumbsComment(String video_image_id, String comment_id) {
        Map<String, String> params = new HashMap<>();
        params.put("video_image_id", video_image_id);
        params.put("comment_id", comment_id);
        Log.e("123", "取消点赞评论：" + params.toString());
        subscription = Network.getInstance("取消点赞评论", this)
                .getUnthumbsComment(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "取消点赞评论：" + result.getCode());
                                Toast.makeText(ImageTextDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
//                                textComment.setIs_thumbs("2");
//                                textComment.setThumbs(String.valueOf(Integer.valueOf(zanNumber) - 1));
//                                imageTextDetailAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
//                                Log.e("123", "取消点赞评论报错：" + error);
                            }
                        }, this, false));
    }

    //点赞评论
    private void requestThumbsComment(String video_image_id, String comment_id) {
        Map<String, String> params = new HashMap<>();
        params.put("video_image_id", video_image_id);
        params.put("comment_id", comment_id);
        Log.e("123", "点赞评论：" + params.toString());
        subscription = Network.getInstance("点赞评论", this)
                .getThumbsComment(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "点赞评论：" + result.getCode());
                                Toast.makeText(ImageTextDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
//                                textComment.setIs_thumbs("1");
//                                textComment.setThumbs(String.valueOf(Integer.valueOf(zanNumber) + 1));
//                                imageTextDetailAdapter.notifyDataSetChanged();
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
                .getDeleteImageComment(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "删除评论：" + result.getCode());
                                Toast.makeText(ImageTextDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                imageTextComments.remove(pos);
                                imageTextDetailAdapter.notifyItemRemoved(pos);
                                if (imageTextComments.size() == 0) {
                                    if (class_data.size() > 0) {
                                        class_data.clear();
                                    }
                                    if (imageList.size() > 0) {
                                        imageList.clear();
                                    }
                                    getCommentList("0");
//                                    initJingXuanDate();
                                }
                                imageTextDetailAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Log.e("123", "删除评论报错：" + error);
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
                                Toast.makeText(ImageTextDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                //切换布局
                                user_attention.setVisibility(View.VISIBLE);
                                user_attention_off.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Log.e("123", "取消关注他报错：" + error);
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
                                Toast.makeText(ImageTextDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                //切换布局
                                user_attention.setVisibility(View.GONE);
                                user_attention_off.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Log.e("123", "关注他报错：" + error);
                            }
                        }, this, false));
    }

    //点赞
    private void DianZanNet(String image_text_id, final String type) {
        Map<String, String> params = new HashMap<>();
        params.put("image_text_id", image_text_id);
        Log.e("123", "点赞：" + params.toString());
        subscription = Network.getInstance("点赞成功", ImageTextDetailActivity.this)
                .home_dianzan(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "       点赞成功        ");
                                Toast.makeText(ImageTextDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                thumbNumber = thumbNumber + 1;
                                zan_layout.setText(thumbNumber + "点赞 ");

                                imageTextDetails.setIs_thumbs("1");
                                zan_image.setLiked(true);
                                zan_image.setUnlikeDrawableRes(R.drawable.home_like_on);
                            }

                            @Override
                            public void onError(String error) {
                                if ("1".equals(type)) {
                                    Toast.makeText(ImageTextDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, ImageTextDetailActivity.this, false));
    }

    //取消点赞
    private void UnDianZanNet(String image_text_id) {
        Map<String, String> params = new HashMap<>();
        params.put("image_text_id", image_text_id);
        Log.e("123", "取消点赞：" + params.toString());
        subscription = Network.getInstance("取消点赞", ImageTextDetailActivity.this)
                .home_undianzan(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "取消点赞：" + result.getCode());
                                Toast.makeText(ImageTextDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                thumbNumber = thumbNumber - 1;
                                zan_layout.setText(thumbNumber + "点赞 ");
                                imageTextDetails.setIs_thumbs("2");
                                zan_image.setLiked(false);
                                zan_image.setUnlikeDrawableRes(R.drawable.home_like_off);
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(ImageTextDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }, ImageTextDetailActivity.this, false));
    }

    //收藏
    private void ShouCangNet(String image_text_id) {
        Map<String, String> params = new HashMap<>();
        params.put("image_text_id", image_text_id);
        Log.e("123", "收藏：" + params.toString());
        subscription = Network.getInstance("收藏", ImageTextDetailActivity.this)
                .home_shoucang(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "收藏成功：" + result.getCode());
                                Toast.makeText(ImageTextDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                collectionNumber = collectionNumber + 1;
                                coll_layout.setText(collectionNumber + "收藏");
                                imageTextDetails.setIs_collect("1");
                                shoucang_image.setLiked(true);
                                shoucang_image.setUnlikeDrawableRes(R.drawable.home_collect_on);
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(ImageTextDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }, ImageTextDetailActivity.this, false));
    }

    //取消收藏
    private void UnShouCangNet(String image_text_id) {
        Map<String, String> params = new HashMap<>();
        params.put("image_text_id", image_text_id);
        Log.e("123", "取消收藏：" + params.toString());
        subscription = Network.getInstance("取消收藏", ImageTextDetailActivity.this)
                .home_unshoucang(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "取消收藏：" + result.getCode());
                                Toast.makeText(ImageTextDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                collectionNumber = collectionNumber - 1;
                                coll_layout.setText(collectionNumber + "收藏");
                                imageTextDetails.setIs_collect("2");
                                shoucang_image.setLiked(false);
                                shoucang_image.setUnlikeDrawableRes(R.drawable.home_collect_off);
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(ImageTextDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }, ImageTextDetailActivity.this, false));
    }


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
                if ("1".equals(is_update)) {
                    FunctionItem item = new FunctionItem();
                    item.setFunctionImage(functionImageids[0]);
                    item.setFunctionName(functionName[0]);
                    functionListem.add(item);
                }
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
                        StatService.trackCustomKVEvent(ImageTextDetailActivity.this, "Details_answer_more_share_wechat", prop);
                        //视频分享
                        if (!TextUtils.isEmpty(imageTextDetails.getContent())) {
                            if (!TextUtils.isEmpty(imageList.get(0))) {
                                ShareUtil.ShareWechat(ImageTextDetailActivity.this, imageTextDetails.getUser_data().getName() + "发布了图文", imageTextDetails.getContent(),
                                        imageList.get(0),
                                        Network.ImageTextUrl + id, "1");
                            } else {
                                ShareUtil.ShareWechat(ImageTextDetailActivity.this, imageTextDetails.getUser_data().getName() + "发布了图文", imageTextDetails.getContent(),
                                        Network.ShareImage,
                                        Network.ImageTextUrl + id, "1");
                            }
                        } else {
                            if (!TextUtils.isEmpty(imageList.get(0))) {
                                ShareUtil.ShareWechat(ImageTextDetailActivity.this, imageTextDetails.getUser_data().getName() + "发布了图文", "快去分享您的印迹吧",
                                        imageList.get(0),
                                        Network.ImageTextUrl + id, "1");
                            } else {
                                ShareUtil.ShareWechat(ImageTextDetailActivity.this, imageTextDetails.getUser_data().getName() + "发布了图文", "快去分享您的印迹吧",
                                        Network.ShareImage,
                                        Network.ImageTextUrl + id, "1");
                            }
                        }
                        ShareType = "2";
                        dialog.dismiss();
                        break;
                    case "朋友圈":
                        prop.setProperty("name", "wechat_moments_layout");
                        StatService.trackCustomKVEvent(ImageTextDetailActivity.this, "Details_answer_more_share_wechat_circle", prop);
                        //图文分享
                        if (!TextUtils.isEmpty(imageTextDetails.getContent())) {
                            if (!TextUtils.isEmpty(imageList.get(0))) {
                                ShareUtil.ShareWechatMoments(ImageTextDetailActivity.this, imageTextDetails.getUser_data().getName() + "发布了图文", imageTextDetails.getContent(),
                                        imageList.get(0),
                                        Network.ImageTextUrl + id, "1");
                            } else {
                                ShareUtil.ShareWechatMoments(ImageTextDetailActivity.this, imageTextDetails.getUser_data().getName() + "发布了图文", imageTextDetails.getContent(),
                                        Network.ShareImage,
                                        Network.ImageTextUrl + id, "1");
                            }
                        } else {
                            if (!TextUtils.isEmpty(imageList.get(0))) {
                                ShareUtil.ShareWechatMoments(ImageTextDetailActivity.this, imageTextDetails.getUser_data().getName() + "发布了图文", "快去分享您的印迹吧",
                                        imageList.get(0),
                                        Network.ImageTextUrl + id, "1");
                            } else {
                                ShareUtil.ShareWechatMoments(ImageTextDetailActivity.this, imageTextDetails.getUser_data().getName() + "发布了图文", "快去分享您的印迹吧",
                                        Network.ShareImage,
                                        Network.ImageTextUrl + id, "1");
                            }
                        }
                        ShareType = "1";
                        dialog.dismiss();
                        break;
                    case "新浪微博":
                        prop.setProperty("name", "sinaWeibo_layout");
                        StatService.trackCustomKVEvent(ImageTextDetailActivity.this, "Details_answer_more_share_sina", prop);
                        //图文分享
                        if (!TextUtils.isEmpty(imageTextDetails.getContent())) {
                            if (!TextUtils.isEmpty(imageList.get(0))) {
                                ShareUtil.ShareWeiBo(ImageTextDetailActivity.this, imageTextDetails.getUser_data().getName() + "发布了图文", imageTextDetails.getContent(),
                                        imageList.get(0),
                                        Network.ImageTextUrl + id, "1");
                            } else {
                                ShareUtil.ShareWeiBo(ImageTextDetailActivity.this, imageTextDetails.getUser_data().getName() + "发布了图文", imageTextDetails.getContent(),
                                        Network.ShareImage,
                                        Network.ImageTextUrl + id, "1");
                            }
                        } else {
                            if (!TextUtils.isEmpty(imageList.get(0))) {
                                ShareUtil.ShareWeiBo(ImageTextDetailActivity.this, imageTextDetails.getUser_data().getName() + "发布了图文", "快去分享您的印迹吧",
                                        imageList.get(0),
                                        Network.ImageTextUrl + id, "1");
                            } else {
                                ShareUtil.ShareWeiBo(ImageTextDetailActivity.this, imageTextDetails.getUser_data().getName() + "发布了图文", "快去分享您的印迹吧",
                                        Network.ShareImage,
                                        Network.ImageTextUrl + id, "1");
                            }
                        }
                        ShareType = "3";
                        dialog.dismiss();
                        break;
                    case "QQ":
                        prop.setProperty("name", "QQ_layout");
                        StatService.trackCustomKVEvent(ImageTextDetailActivity.this, "Details_answer_more_share_qq", prop);
                        //图文分享
                        if (!TextUtils.isEmpty(imageTextDetails.getContent())) {
                            if (!TextUtils.isEmpty(imageList.get(0))) {
                                ShareUtil.ShareQQ(ImageTextDetailActivity.this, imageTextDetails.getUser_data().getName() + "发布了图文", imageTextDetails.getContent(),
                                        imageList.get(0),
                                        Network.ImageTextUrl + id, "1");
                            } else {
                                ShareUtil.ShareQQ(ImageTextDetailActivity.this, imageTextDetails.getUser_data().getName() + "发布了图文", imageTextDetails.getContent(),
                                        Network.ShareImage,
                                        Network.ImageTextUrl + id, "1");
                            }
                        } else {
                            if (!TextUtils.isEmpty(imageList.get(0))) {
                                ShareUtil.ShareQQ(ImageTextDetailActivity.this, imageTextDetails.getUser_data().getName() + "发布了图文", "快去分享您的印迹吧",
                                        imageList.get(0),
                                        Network.ImageTextUrl + id, "1");
                            } else {
                                ShareUtil.ShareQQ(ImageTextDetailActivity.this, imageTextDetails.getUser_data().getName() + "发布了图文", "快去分享您的印迹吧",
                                        Network.ShareImage,
                                        Network.ImageTextUrl + id, "1");
                            }
                        }
                        ShareType = "4";
                        dialog.dismiss();
                        break;
                    case "QQ空间":
                        prop.setProperty("name", "QQzone_layout");
                        StatService.trackCustomKVEvent(ImageTextDetailActivity.this, "Details_answer_more_share_qqzone", prop);
                        //图文分享
                        if (!TextUtils.isEmpty(imageTextDetails.getContent())) {
                            if (!TextUtils.isEmpty(imageList.get(0))) {
                                ShareUtil.ShareQZone(ImageTextDetailActivity.this, imageTextDetails.getUser_data().getName() + "发布了图文", imageTextDetails.getContent(),
                                        imageList.get(0),
                                        Network.ImageTextUrl + id, "1");
                            } else {
                                ShareUtil.ShareQZone(ImageTextDetailActivity.this, imageTextDetails.getUser_data().getName() + "发布了图文", imageTextDetails.getContent(),
                                        Network.ShareImage,
                                        Network.ImageTextUrl + id, "1");
                            }
                        } else {
                            if (!TextUtils.isEmpty(imageList.get(0))) {
                                ShareUtil.ShareQZone(ImageTextDetailActivity.this, imageTextDetails.getUser_data().getName() + "发布了图文", "快去分享您的印迹吧",
                                        imageList.get(0),
                                        Network.ImageTextUrl + id, "1");
                            } else {
                                ShareUtil.ShareQZone(ImageTextDetailActivity.this, imageTextDetails.getUser_data().getName() + "发布了图文", "快去分享您的印迹吧",
                                        Network.ShareImage,
                                        Network.ImageTextUrl + id, "1");
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
                        if ("".equals(SharedPreferencesHelper.get(ImageTextDetailActivity.this, "login_token", ""))) {
                            Intent intent = new Intent(ImageTextDetailActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            prop.setProperty("name", "bianji_layout");
                            StatService.trackCustomKVEvent(ImageTextDetailActivity.this, "Details_answer_more_edite", prop);
                            //编辑
                            Intent intent = new Intent(ImageTextDetailActivity.this, AddImagesActivity.class);
                            Bundle bundle = new Bundle();
                            //传递必要数据(图片,id,内容,标签)
                            ArrayList<String> list = new ArrayList<>();
                            for (int i = 0; i < imageTextDetails.getImg_data().size(); i++) {
                                list.add(imageTextDetails.getImg_data().get(i).getPath());
                            }
                            if (null != imageTextDetails.getId()) {
                                intent.putExtra("update_id", imageTextDetails.getId());
                            }
                            if (null != imageTextDetails.getContent()) {
                                intent.putExtra("update_content", imageTextDetails.getContent());
                            }
                            if (null != imageTextDetails.getClass_data() && imageTextDetails.getClass_data().size() > 0) {
                                bundle.putParcelableArrayList("biaoqians", (ArrayList<? extends Parcelable>) imageTextDetails.getClass_data());
                            }
                            intent.putStringArrayListExtra("update_image_paths", list);
                            intent.putExtra("flag", "1");
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }
                        dialog.dismiss();
                        break;
                    case "举报":
                        prop.setProperty("name", "jubao_layout");
                        StatService.trackCustomKVEvent(ImageTextDetailActivity.this, "Details_answer_more_report", prop);
                        if ("".equals(SharedPreferencesHelper.get(ImageTextDetailActivity.this, "login_token", ""))) {
                            Intent intent = new Intent(ImageTextDetailActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            report_msg_question();
                        }
                        dialog.dismiss();
                        break;
                    case "删除":
                        dialog.dismiss();
                        prop.setProperty("name", "delete_layout");
                        StatService.trackCustomKVEvent(ImageTextDetailActivity.this, "Details_answer_more_delete", prop);
                        PopWindowHelper.public_tishi_pop(ImageTextDetailActivity.this, "删除图文", "是否删除该图文？", "否", "是", new DialogCallBack() {
                            @Override
                            public void save() {
                                deleteVideo();
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
                StatService.trackCustomKVEvent(ImageTextDetailActivity.this, "Details_answer_cancel", prop);
                dialog.dismiss();
            }
        });
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
        report_title.setText("举报图文");

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
                    reportImage(ids);
                } else {
                    Toast.makeText(ImageTextDetailActivity.this, "请选择举报内容", Toast.LENGTH_SHORT).show();
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
     * 获取举报图文数据
     */
    private void report_msg_question() {
        subscription = Network.getInstance("获取举报问题类型", this)
                .getReportImageType(new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<ReportMsg>>>() {
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

    //舉報圖文
    private void reportImage(String ids) {
        Map<String, String> params = new HashMap<>();
        params.put("image_text_id", id);
        params.put("type", ids);
        Log.e("举报图文：", params.toString());
        subscription = Network.getInstance("举报图文", this)
                .getReportImage(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "举报图文成功" + result.getCode());
                                Toast.makeText(ImageTextDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Logger.e("举报图文报错：" + error);
                                dialog.dismiss();
                            }
                        }, this, false));
    }

    //刪除圖文
    private void deleteVideo() {
        Map<String, String> params = new HashMap<>();
        params.put("image_text_id", id);
        Log.e("刪除圖文：", params.toString());
        subscription = Network.getInstance("刪除圖文", this)
                .getDeleteImage(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "刪除圖文成功" + result.getCode());
                                Toast.makeText(ImageTextDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Logger.e("刪除圖文报错：" + error);
                            }
                        }, this, false));
    }


//    /**
//     * 获取ObservableScrollView的滑动数据
//     */
//    @Override
//    public void onObservableScrollViewListener(int l, int t, int oldl, int oldt) {
//        if (t <= 0) {
//            //顶部图处于最顶部，标题栏透明
//            writer_user_head.setVisibility(View.GONE);
//            writer_user_name.setVisibility(View.GONE);
//            mHeaderContent.setBackgroundColor(Color.argb(0, 48, 63, 159));
//        } else if (t > 0 && t < mHeight) {
//            //滑动过程中，渐变
//            float scale = (float) t / mHeight;//算出滑动距离比例
//            float alpha = (150 * scale);//得到透明度
//            mHeaderContent.setBackgroundColor(Color.argb((int) alpha, 48, 59, 59));
//            if ((int) alpha == 131) {
//                writer_user_head.setVisibility(View.VISIBLE);
//                writer_user_name.setVisibility(View.VISIBLE);
//                Glide.with(ImageTextDetailActivity.this)
//                        .load(avatar)
//                        .error(R.drawable.wodetouxiang)
//                        .into(writer_user_head);
//                writer_user_name.setText(name);
//                Glide.with(this).load(imageList.get(0))
//                        .bitmapTransform(new BlurTransformation(this, 60), new CenterCrop(this))
//                        .into(new ViewTarget<View, GlideDrawable>(mHeaderContent) {
//                            //括号里为需要加载的控件
//                            @Override
//                            public void onResourceReady(GlideDrawable resource,
//                                                        GlideAnimation<? super GlideDrawable> glideAnimation) {
//                                this.view.setBackground(resource.getCurrent());
//                            }
//                        });
//            }
//        } else {
//            writer_user_head.setVisibility(View.VISIBLE);
//            writer_user_name.setVisibility(View.VISIBLE);
//            Glide.with(ImageTextDetailActivity.this)
//                    .load(avatar)
//                    .error(R.drawable.wodetouxiang)
//                    .into(writer_user_head);
//            writer_user_name.setText(name);
//            //过顶部图区域，标题栏定色
//            Glide.with(this).load(imageList.get(0))
//                    .bitmapTransform(new BlurTransformation(this, 60), new CenterCrop(this))
//                    .into(new ViewTarget<View, GlideDrawable>(mHeaderContent) {
//                        //括号里为需要加载的控件
//                        @Override
//                        public void onResourceReady(GlideDrawable resource,
//                                                    GlideAnimation<? super GlideDrawable> glideAnimation) {
//                            this.view.setBackground(resource.getCurrent());
//                        }
//                    });
//        }
//    }
}

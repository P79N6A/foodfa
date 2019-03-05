package com.app.cookbook.xinhe.foodfamily.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.callback.DialogCallBack;
import com.app.cookbook.xinhe.foodfamily.callback.ImageBiggerCallBack;
import com.app.cookbook.xinhe.foodfamily.main.adapter.AnswerFunctionAdapter;
import com.app.cookbook.xinhe.foodfamily.main.adapter.ReportMsgAdapter;
import com.app.cookbook.xinhe.foodfamily.main.adapter.ShareImageItemAdapter;
import com.app.cookbook.xinhe.foodfamily.main.adapter.SimpleAdapter;
import com.app.cookbook.xinhe.foodfamily.main.db.NoteDao;
import com.app.cookbook.xinhe.foodfamily.main.db.entity.Note;
import com.app.cookbook.xinhe.foodfamily.main.entity.AnswerEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.CategoryDateEn;
import com.app.cookbook.xinhe.foodfamily.main.entity.FunctionItem;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageEvent;
import com.app.cookbook.xinhe.foodfamily.main.entity.ReportMsg;
import com.app.cookbook.xinhe.foodfamily.main.entity.ShareImageItem;
import com.app.cookbook.xinhe.foodfamily.main.entity.UploadImageEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.WenTIDetail;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.update.PermisionUtils;
import com.app.cookbook.xinhe.foodfamily.util.BianjiToolbarControl;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.DateUtils;
import com.app.cookbook.xinhe.foodfamily.util.PopWindowHelper;
import com.app.cookbook.xinhe.foodfamily.util.ShareUtil;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.snackbar.BaseTransientBottomBar;
import com.app.cookbook.xinhe.foodfamily.util.snackbar.TopSnackBar;
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
import cz.msebera.android.httpclient.util.TextUtils;

public class FenLeiQuestionDetailActivity extends BaseActivity implements DialogInterface.OnCancelListener {
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.toolbar)
    BianjiToolbarControl mToolbar;
    @BindView(R.id.liji_answer_btn_two)
    LinearLayout liji_answer_btn_two;
    @BindView(R.id.attention_image)
    LikeButton attention_image;
    @BindView(R.id.attention_tv)
    TextView attention_tv;

    private String wenti_id;
    private NoteDao noteDao;
    private WenTIDetail wenTIDetail;
    private int PAGE = 1;
    private boolean is_not_more;
    private String flag;
    private LinearLayoutManager linearLayoutManager;
    private ReportMsgAdapter reportMsgAdapter;
    private List<ReportMsg> data = new ArrayList<>();
    private ArrayList<UploadImageEntity> huancun_detail_image = new ArrayList<>();
    //分享数据
    private int[] moreImageids = {R.drawable.icon_create_image, R.drawable.icon_wechat, R.drawable.icon_wechat_moments, R.drawable.icon_weibo, R.drawable.icon_qq, R.drawable.icon_qqzone};
    private String[] moreName = {"生成长图", "微信", "朋友圈", "新浪微博", "QQ", "QQ空间"};
    //分享数据集合
    private List<ShareImageItem> moreListem = new ArrayList<>();
    //删除，编辑，举报
    private int[] functionImageids = {R.drawable.bianji, R.drawable.more_shanchu, R.drawable.jubao};
    private String[] functionName = {"编辑", "删除", "举报"};
    private List<FunctionItem> functionListem = new ArrayList<>();

    //是否删除
    private String is_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {
        if (null != parms) {
            if (null != parms.getString("wenti_id")) {
                wenti_id = parms.getString("wenti_id");
            } else {
                wenti_id = "-1";
            }
            if (null != parms.getString("flag")) {
                flag = parms.getString("flag");
            } else {
                flag = "-1";
            }
            Log.e("123", "    问题ID    " + wenti_id);
            //需要缓存到详情页的图片
            if (null != parms.getParcelableArrayList("huancun_detail_image")) {
                huancun_detail_image = parms.getParcelableArrayList("huancun_detail_image");
            }
        } else {
            gethost();
        }
    }

    //获取网页点击事件消息数据
    private void gethost() {
        Intent intent = getIntent();
        if (intent != null) {
            Uri uri = intent.getData();
            if (uri != null) {
                String dataString = intent.getDataString();
                String scheme = uri.getScheme();
                String host = uri.getHost();
                String query = uri.getQuery();
                String querynub = query.substring(3);
//                Log.e("123", "dataString-----   " + dataString + "   ----- scheme ------    "
//                        + scheme + "   --- host ---   " + host + "   ----- query ----    " + query);
//                Log.e("123", "             " + querynub);
                wenti_id = querynub;
            }
        }
    }


    List<AnswerEntity> answerEntities = new ArrayList<>();

    private void init_question_detail(boolean is_show_dialog) {
        Map<String, String> params = new HashMap<>();
        params.put("question_id", wenti_id);
        params.put("page", String.valueOf(PAGE));
        subscription = Network.getInstance("消息页面获取通知界面", this)
                .question_detail(params,
                        new ProgressSubscriberNew<>(WenTIDetail.class, new GsonSubscriberOnNextListener<WenTIDetail>() {
                            @Override
                            public void on_post_entity(WenTIDetail result) {
                                Log.e("123", "获取问题详情成功：" + result);
                                is_delete = result.getIs_delete();

                                wenTIDetail = result;

                                if ("1".equals(wenTIDetail.getIs_follow())) {
                                    attention_tv.setText("已关注");
                                    attention_tv.setTextColor(getResources().getColor(R.color.color_0EB39B));
                                    attention_image.setLiked(true);
                                    attention_image.setLikeDrawableRes(R.drawable.icon_answer_collect_on);
                                } else if ("2".equals(wenTIDetail.getIs_follow())) {
                                    attention_tv.setText("关注问题");
                                    attention_tv.setTextColor(getResources().getColor(R.color.color_999999));
                                    attention_image.setLiked(false);
                                    attention_image.setUnlikeDrawableRes(R.drawable.icon_answer_collect);
                                }

                                mToolbar.showRight();

                                //设置页面的信息
                                if (answerEntities.size() > 0 && PAGE > 1) {//这表示是"加载"
                                    if (result.getAnswer_data().getData().size() == 0) {
                                        //表示没有更多数据了
                                        is_not_more = true;
                                        answerEntities.addAll(result.getAnswer_data().getData());
                                        if (null != adapter) {
                                            adapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        is_not_more = false;
                                        answerEntities.addAll(result.getAnswer_data().getData());
                                        if (null != adapter) {
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                } else {
                                    answerEntities = result.getAnswer_data().getData();
                                    set_answer_list2(result.getAnswer_data().getData());
                                }
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "获取问题详情报错：" + error);
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                finish();
                                if (error.equals("断网")) {
                                    Toast.makeText(getApplicationContext(), "Opps,网络走丢啦~", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (answerEntities.size() == 0) {
                                        set_answer_list2(answerEntities);
                                    }
                                }
                            }
                        }, this, is_show_dialog));
    }


    @Override
    public void initView() {
        setContentLayout(R.layout.activity_fen_lei_question_detail);
        EventBus.getDefault().register(this);
        //初始化黄牛刀
        ButterKnife.bind(this);
        isShowTitle(false);
        // recycler_view.setFocusable(false);
        init_question_detail(true);
    }


    @Override
    public void initonResume() {
        Properties prop = new Properties();
        prop.setProperty("look_wenti_detai", "浏览问题详情页次数");
        StatService.trackCustomBeginKVEvent(this, "Browse_details_question", prop);
    }

    @Override
    public void initonPause() {
        Properties prop = new Properties();
        prop.setProperty("look_wenti_detai", "浏览问题详情页次数");
        StatService.trackCustomEndKVEvent(this, "Browse_details_question", prop);
    }

    @Override
    public void doBusiness(Context mContext) {
        noteDao = new NoteDao(this);
        mToolbar.setBackButtonOnClickListerner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "mToolbar");
                StatService.trackCustomKVEvent(getApplicationContext(), "Details_problem_return", prop);

                finish();
            }
        });

        mToolbar.setRightButtonOnClickListerner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "mToolbar");
                StatService.trackCustomKVEvent(getApplicationContext(), "Details_problem_more", prop);
                if (functionListem.size() > 0) {
                    functionListem.clear();
                }
                select_more_popwindow();

            }
        });


        //立即回答
        liji_answer_btn_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "liji_answer_btn");
                StatService.trackCustomKVEvent(getApplicationContext(), "Details_problem_answer_immediately", prop);
                if ("".equals(SharedPreferencesHelper.get(getApplicationContext(), "login_token", ""))) {
                    Intent intent = new Intent(FenLeiQuestionDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(FenLeiQuestionDetailActivity.this, FastAnswerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("question_id", wenTIDetail.getId());
                    bundle.putString("is_go_to_detai", "true");
                    bundle.putString("question_title", wenTIDetail.getTitle());
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        //关注问题
        attention_image.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                //按钮提示音
                Properties prop = new Properties();
                prop.setProperty("name", "weiguanzhu_btn");
                StatService.trackCustomKVEvent(getApplicationContext(), "Details_problem_pay_attention_problem", prop);
                if ("".equals(SharedPreferencesHelper.get(getApplicationContext(), "login_token", ""))) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    guanzhu_question(wenTIDetail.getId());
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                //按钮提示音
                if ("".equals(SharedPreferencesHelper.get(getApplicationContext(), "login_token", ""))) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    quxiao_guanzhu_question(wenTIDetail.getId());
                }
            }
        });
    }

    private SimpleAdapter adapter;
    public LinearLayout layout_top;

    private void set_answer_list2(final List<AnswerEntity> answerEntities) {
        xrefreshview.setPullLoadEnable(true);
        // 设置静默加载模式
        linearLayoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setItemAnimator(null);
        if (wenTIDetail != null) {
            adapter = new SimpleAdapter(answerEntities, recycler_view, wenTIDetail, FenLeiQuestionDetailActivity.this, new ImageBiggerCallBack() {
                @Override
                public void image_url(View view, String url) {
                    Rect frame = new Rect();
                    getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                    int statusBarHeight = frame.top;

                    int[] location = new int[2];
                    view.getLocationOnScreen(location);
                    location[1] += statusBarHeight;
                    if (!TextUtils.isBlank(url)) {
                        String[] imageUrls = new String[]{url};
                        Intent intent = new Intent();
                        intent.putExtra("imageUrls", imageUrls);
//                        intent.putExtra("curImageUrl", url);
                        intent.putExtra("imageType", "2");
                        intent.setClass(FenLeiQuestionDetailActivity.this, PhotoBrowserActivity.class);
                        startActivity(intent);
                    }
                }
            });
            recycler_view.setAdapter(adapter);
        }
        //初始化headview的信息
        //initHeadViewResource();
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);
        xrefreshview.setPullLoadEnable(true);
        if (answerEntities.size() > 7) {
            xrefreshview.enableReleaseToLoadMore(true);
            adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
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
                View headerView = null;
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();
                if (firstVisibleItem == 0) {
                    headerView = recyclerView.getChildAt(firstVisibleItem);
                }
                float alpha = 0;
                if (headerView == null) {
                    alpha = 1;// 如果headerView 为null ,说明已经到达header滑动的最大高度了
                } else {
                    alpha = Math.abs(headerView.getTop()) * 1.0f / headerView.getHeight();
                    if (alpha > 0 && alpha < 0.5) {
                        //隐藏旁边两个分享按钮
                    } else if (alpha > 0.5 && alpha < 1) {
                        mToolbar.setTitle(wenTIDetail.getTitle());
                        mToolbar.showLeft();//显示左边的图片

                        mToolbar.setQuanJuColor(Color.parseColor("#ffffff"));
                    } else {
                        mToolbar.setTitle("");
                        mToolbar.showLeft();

                        mToolbar.setQuanJuColor(Color.parseColor("#ffffff"));
                    }
                }


            }
        });
        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PAGE = 1;
                        //填写刷新数据的网络请求，一般page=1，List集合清空操作
                        init_question_detail(false);
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
                        init_question_detail(false);

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


    private static Dialog dialog;
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
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

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
            if (!TextUtils.isEmpty(wenTIDetail.getUid())) {
                if (SharedPreferencesHelper.get(getApplicationContext(), "user_id", "").toString().equals(wenTIDetail.getUid())) {
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
                        StatService.trackCustomKVEvent(FenLeiQuestionDetailActivity.this, "Details_problem_more_share_wechat", prop);
                        if (answerEntities.size() > 0) {
                            if (wenTIDetail.getImg_data() != null && wenTIDetail.getImg_data().size() > 0) {
                                ShareUtil.ShareWechat(FenLeiQuestionDetailActivity.this, wenTIDetail.getTitle(),
                                        wenTIDetail.getContent_remove(),
                                        wenTIDetail.getImg_data().get(0).getPath(),
                                        Network.ShareUrl + "question?id=" + wenti_id, "1");
                            } else {
                                ShareUtil.ShareWechat(FenLeiQuestionDetailActivity.this, wenTIDetail.getTitle(),
                                        wenTIDetail.getContent_remove(),
                                        Network.ShareImage,
                                        Network.ShareUrl + "question?id=" + wenti_id, "1");
                            }
                        } else {
                            if (wenTIDetail.getImg_data() != null && wenTIDetail.getImg_data().size() > 0) {
                                ShareUtil.ShareWechat(FenLeiQuestionDetailActivity.this, wenTIDetail.getTitle(),
                                        "快去分享你的知识吧",
                                        wenTIDetail.getImg_data().get(0).getPath(),
                                        Network.ShareUrl + "question?id=" + wenti_id, "1");
                            } else {
                                ShareUtil.ShareWechat(FenLeiQuestionDetailActivity.this, wenTIDetail.getTitle(),
                                        "快去分享你的知识吧",
                                        Network.ShareImage,
                                        Network.ShareUrl + "question?id=" + wenti_id, "1");
                            }
                        }
                        ShareType = "2";
                        dialog.dismiss();
                        break;
                    case "朋友圈":
                        prop.setProperty("name", "wechat_moments_layout");
                        StatService.trackCustomKVEvent(FenLeiQuestionDetailActivity.this, "Details_problem_more_share_wechat_circle", prop);

                        if (answerEntities.size() > 0) {
                            if (wenTIDetail.getImg_data() != null && wenTIDetail.getImg_data().size() > 0) {
                                ShareUtil.ShareWechatMoments(FenLeiQuestionDetailActivity.this, wenTIDetail.getTitle(),
                                        wenTIDetail.getContent_remove(),
                                        wenTIDetail.getImg_data().get(0).getPath(),
                                        Network.ShareUrl + "question?id=" + wenti_id, "1");
                            } else {
                                ShareUtil.ShareWechatMoments(FenLeiQuestionDetailActivity.this, wenTIDetail.getTitle(),
                                        wenTIDetail.getContent_remove(),
                                        Network.ShareImage,
                                        Network.ShareUrl + "question?id=" + wenti_id, "1");
                            }
                        } else {
                            if (wenTIDetail.getImg_data() != null && wenTIDetail.getImg_data().size() > 0) {
                                ShareUtil.ShareWechatMoments(FenLeiQuestionDetailActivity.this, wenTIDetail.getTitle(),
                                        "快去分享你的知识吧",
                                        wenTIDetail.getImg_data().get(0).getPath(),
                                        Network.ShareUrl + "question?id=" + wenti_id, "1");
                            } else {
                                ShareUtil.ShareWechatMoments(FenLeiQuestionDetailActivity.this, wenTIDetail.getTitle(),
                                        "快去分享你的知识吧",
                                        Network.ShareImage,
                                        Network.ShareUrl + "question?id=" + wenti_id, "1");
                            }
                        }
                        ShareType = "1";
                        dialog.dismiss();
                        break;
                    case "新浪微博":
                        prop.setProperty("name", "sinaWeibo_layout");
                        StatService.trackCustomKVEvent(FenLeiQuestionDetailActivity.this, "Details_problem_more_share_sina", prop);

                        if (answerEntities.size() > 0) {
                            if (wenTIDetail.getImg_data() != null && wenTIDetail.getImg_data().size() > 0) {
                                ShareUtil.ShareWeiBo(FenLeiQuestionDetailActivity.this, wenTIDetail.getTitle(),
                                        wenTIDetail.getContent_remove(),
                                        wenTIDetail.getImg_data().get(0).getPath(),
                                        Network.ShareUrl + "question?id=" + wenti_id, "1");
                            } else {
                                ShareUtil.ShareWeiBo(FenLeiQuestionDetailActivity.this, wenTIDetail.getTitle(),
                                        wenTIDetail.getContent_remove(),
                                        Network.ShareImage,
                                        Network.ShareUrl + "question?id=" + wenti_id, "1");
                            }
                        } else {
                            if (wenTIDetail.getImg_data() != null && wenTIDetail.getImg_data().size() > 0) {
                                ShareUtil.ShareWeiBo(FenLeiQuestionDetailActivity.this, wenTIDetail.getTitle(),
                                        "快去分享你的知识吧",
                                        wenTIDetail.getImg_data().get(0).getPath(),
                                        Network.ShareUrl + "question?id=" + wenti_id, "1");
                            } else {
                                ShareUtil.ShareWeiBo(FenLeiQuestionDetailActivity.this, wenTIDetail.getTitle(),
                                        "快去分享你的知识吧",
                                        Network.ShareImage,
                                        Network.ShareUrl + "question?id=" + wenti_id, "1");
                            }
                        }
                        ShareType = "3";
                        dialog.dismiss();
                        break;
                    case "QQ":
                        prop.setProperty("name", "QQ_layout");
                        StatService.trackCustomKVEvent(FenLeiQuestionDetailActivity.this, "Details_problem_more_share_qq", prop);
                        if (answerEntities.size() > 0) {
                            if (wenTIDetail.getImg_data() != null && wenTIDetail.getImg_data().size() > 0) {
                                ShareUtil.ShareQQ(FenLeiQuestionDetailActivity.this, wenTIDetail.getTitle(),
                                        wenTIDetail.getContent_remove(),
                                        wenTIDetail.getImg_data().get(0).getPath(),
                                        Network.ShareUrl + "question?id=" + wenti_id, "1");
                            } else {
                                ShareUtil.ShareQQ(FenLeiQuestionDetailActivity.this, wenTIDetail.getTitle(),
                                        wenTIDetail.getContent_remove(),
                                        Network.ShareImage,
                                        Network.ShareUrl + "question?id=" + wenti_id, "1");
                            }
                        } else {
                            if (wenTIDetail.getImg_data() != null && wenTIDetail.getImg_data().size() > 0) {
                                ShareUtil.ShareQQ(FenLeiQuestionDetailActivity.this, wenTIDetail.getTitle(),
                                        "快去分享你的知识吧",
                                        wenTIDetail.getImg_data().get(0).getPath(),
                                        Network.ShareUrl + "question?id=" + wenti_id, "1");
                            } else {
                                ShareUtil.ShareQQ(FenLeiQuestionDetailActivity.this, wenTIDetail.getTitle(),
                                        "快去分享你的知识吧",
                                        Network.ShareImage,
                                        Network.ShareUrl + "question?id=" + wenti_id, "1");
                            }
                        }
                        ShareType = "4";
                        dialog.dismiss();
                        break;
                    case "QQ空间":
                        prop.setProperty("name", "QQzone_layout");
                        StatService.trackCustomKVEvent(FenLeiQuestionDetailActivity.this, "Details_problem_more_share_qqzone", prop);
                        if (answerEntities.size() > 0) {
                            if (wenTIDetail.getImg_data() != null && wenTIDetail.getImg_data().size() > 0) {
                                ShareUtil.ShareQZone(FenLeiQuestionDetailActivity.this, wenTIDetail.getTitle(),
                                        wenTIDetail.getContent_remove(),
                                        wenTIDetail.getImg_data().get(0).getPath(),
                                        Network.ShareUrl + "question?id=" + wenti_id, "1");
                            } else {
                                ShareUtil.ShareQZone(FenLeiQuestionDetailActivity.this, wenTIDetail.getTitle(),
                                        wenTIDetail.getContent_remove(),
                                        Network.ShareImage,
                                        Network.ShareUrl + "question?id=" + wenti_id, "1");
                            }
                        } else {
                            if (wenTIDetail.getImg_data() != null && wenTIDetail.getImg_data().size() > 0) {
                                ShareUtil.ShareQZone(FenLeiQuestionDetailActivity.this, wenTIDetail.getTitle(),
                                        "快去分享你的知识吧",
                                        wenTIDetail.getImg_data().get(0).getPath(),
                                        Network.ShareUrl + "question?id=" + wenti_id, "1");
                            } else {
                                ShareUtil.ShareQZone(FenLeiQuestionDetailActivity.this, wenTIDetail.getTitle(),
                                        "快去分享你的知识吧",
                                        Network.ShareImage,
                                        Network.ShareUrl + "question?id=" + wenti_id, "1");
                            }
                        }
                        ShareType = "5";
                        dialog.dismiss();
                        break;
                    case "生成长图":
                        PermisionUtils.verifyStoragePermissions(FenLeiQuestionDetailActivity.this);
                        Intent intent1 = new Intent(FenLeiQuestionDetailActivity.this, PreviewShareImageActivity.class);
                        intent1.putExtra("imagePath", "");
                        intent1.putExtra("type", "1");
                        intent1.putExtra("title", wenTIDetail.getTitle());
                        intent1.putExtra("id", wenti_id);
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
                        StatService.trackCustomKVEvent(FenLeiQuestionDetailActivity.this, "Details_problem_more_edite", prop);
                        //编辑之前先保存信息
                        go_to_edit();
                        dialog.dismiss();
                        break;
                    case "举报":
                        prop.setProperty("name", "jubao_layout");
                        StatService.trackCustomKVEvent(FenLeiQuestionDetailActivity.this, "Details_problem_more_report", prop);
                        if ("".equals(SharedPreferencesHelper.get(FenLeiQuestionDetailActivity.this, "login_token", ""))) {
                            Intent intent = new Intent(FenLeiQuestionDetailActivity.this, LoginActivity.class);
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
                        StatService.trackCustomKVEvent(FenLeiQuestionDetailActivity.this, "Details_answer_more_delete", prop);

                        PopWindowHelper.public_tishi_pop(FenLeiQuestionDetailActivity.this, "删除问题", "是否删除该问题？", "否", "是", new DialogCallBack() {
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
                StatService.trackCustomKVEvent(getApplicationContext(), "Details_problem_more_cancel", prop);
                dialog.dismiss();
            }
        });
    }


    //订阅方法，当接收到事件的时候，会调用该方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        switch (messageEvent.getMessage()) {
            case "121":
                ShareUtil.add_share_logs_question(FenLeiQuestionDetailActivity.this, wenti_id, ShareType, "");
                TopSnackBar.make(mToolbar, "分享成功", BaseTransientBottomBar.LENGTH_SHORT, FenLeiQuestionDetailActivity.this).show();

                Properties prop = new Properties();
                prop.setProperty("share_time", "分享后返回时长");
                StatService.trackCustomEndKVEvent(this, "Share_back_time", prop);
                break;
            case "600":
                if (wenTIDetail == null) {
                    init_question_detail(false);
                }
                break;
        }
    }

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
        report_title.setText("举报问题");

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        reportMsgAdapter = new ReportMsgAdapter(FenLeiQuestionDetailActivity.this, handler);
        reportMsgAdapter.setData(msgs);
        recyclerView.setAdapter(reportMsgAdapter);

        dialog = new Dialog(this, R.style.transparentFrameWindowStyle2);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();

        WindowManager wm1 = this.getWindowManager();
        int height1 = wm1.getDefaultDisplay().getHeight();
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
        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = Contacts.typeMsg.toString();
                int len = str.length() - 1;
                String ids = str.substring(1, len).replace(" ", "");//"keyids":”1,2,3”
                Log.e("123", "               " + ids);
                if (!TextUtils.isEmpty(ids)) {
                    report_question(wenTIDetail.getId(), ids);
                } else {
                    Toast.makeText(FenLeiQuestionDetailActivity.this, "请选择举报内容", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Handler handler = new Handler() {

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


    public void go_to_edit() {
        Note note = new Note();
        saveNoteData(note, wenTIDetail.getTitle(), wenTIDetail.getId(), wenTIDetail.getContent_remove(), wenTIDetail.getContent());
        Intent intent = new Intent(FenLeiQuestionDetailActivity.this, AddQuestionActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("flag", 1);//编辑笔记
        intent.putExtra("question_id", wenTIDetail.getId());
        //把标签这个集合传过去
        List<CategoryDateEn> categoryDateEns = new ArrayList<CategoryDateEn>();
        if (null != wenTIDetail.getCategory_data()) {
            categoryDateEns = wenTIDetail.getCategory_data();
        }
        bundle.putParcelableArrayList("biaoqian_list", (ArrayList<? extends Parcelable>) categoryDateEns);
        intent.putExtra("data", bundle);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 保存数据,=0销毁当前界面，=1不销毁界面，为了防止在后台时保存笔记并销毁，应该只保存笔记
     */
    private void saveNoteData(Note note, String question_title, String question_id, String content, String mcontent) {
        if (null != noteDao.queryNoteOne(Integer.valueOf(question_id))) {//如果之前有插入数据
            noteDao.deleteNote(Integer.valueOf(question_id));
            //以问题的ID作为分类
            note.setTitle(question_title);
            note.setContent(content);
            note.setMcontent(mcontent);
            note.setGroupId(Integer.valueOf(question_id));
            note.setGroupName(question_id);
            note.setType(2);
            note.setBgColor("#FFFFFF");
            note.setIsEncrypt(0);
            note.setCreateTime(DateUtils.date2string(new Date()));
            //新建笔记
            noteDao.insertNote(note);
        } else {//之前没有插入过数据
            note.setTitle(question_title);
            note.setContent(content);
            note.setMcontent(mcontent);
            note.setGroupId(Integer.valueOf(question_id));
            note.setGroupName(question_id);
            note.setType(2);
            note.setBgColor("#FFFFFF");
            note.setIsEncrypt(0);
            note.setCreateTime(DateUtils.date2string(new Date()));
            //新建笔记
            noteDao.insertNote(note);
        }
    }


    private void quxiao_guanzhu_question(String question_id) {
        Map<String, String> params = new HashMap<>();
        params.put("question_id", question_id);
        Log.e("取消关注问题：", params.toString());
        subscription = Network.getInstance("取消关注问题", this)
                .quxiao_question(question_id,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "取消关注问题成功：" + result.getCode());
                                wenTIDetail.setIs_follow("2");
//                                yiguanzhu_tv_btn.setVisibility(View.GONE);
//                                weiguanzhu_btn_two.setVisibility(View.VISIBLE);
                                attention_tv.setText("关注问题");
                                attention_tv.setTextColor(getResources().getColor(R.color.color_999999));
                                attention_image.setLiked(false);
                                attention_image.setLikeDrawableRes(R.drawable.icon_answer_collect);
                                EventBus.getDefault().post(new MessageEvent("501"));

                            }

                            @Override
                            public void onError(String error) {

                            }
                        }, this, false));

    }

    private void guanzhu_question(String question_id) {
        Map<String, String> params = new HashMap<>();
        params.put("question_id", question_id);
        Log.e("关注问题：", params.toString());
        subscription = Network.getInstance("关注问题", this)
                .guanzhu_question(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "关注问题成功：" + result.getCode());
                                wenTIDetail.setIs_follow("1");
//                                yiguanzhu_tv_btn.setVisibility(View.VISIBLE);
//                                weiguanzhu_btn_two.setVisibility(View.GONE);
                                attention_tv.setText("已关注");
                                attention_tv.setTextColor(getResources().getColor(R.color.color_0EB39B));
                                attention_image.setLiked(true);
                                attention_image.setLikeDrawableRes(R.drawable.icon_answer_collect_on);
                                EventBus.getDefault().post(new MessageEvent("500"));

                            }

                            @Override
                            public void onError(String error) {
                            }
                        }, this, false));

    }

    /**
     * 举报
     *
     * @param question_id
     */
    private void report_question(String question_id, String type) {
        Map<String, String> params = new HashMap<>();
        params.put("question_id", question_id);
        params.put("type", type);
        Log.e("关注问题：", params.toString());
        subscription = Network.getInstance("举报问题", this)
                .report_question(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "举报问题：" + result.getCode());
                                Toast.makeText(FenLeiQuestionDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                Contacts.typeMsg.clear();
                                dialog.dismiss();
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "举报问题异常：" + error.toString());
                                Toast.makeText(FenLeiQuestionDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }, this, false));
    }

    /**
     * 获取举报数据
     */
    private void report_msg_question() {
        subscription = Network.getInstance("获取举报问题类型", this)
                .report_msg_request(new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<ReportMsg>>>() {
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
                        Log.e("123", "获取举报问题类型异常：" + error.toString());
                    }
                }, this, false));
    }


    //删除答案
    private void delete_daan_submit() {
        Map<String, String> params = new HashMap<>();
        params.put("question_id", wenti_id);
        Log.e("删除问题：", params.toString());
        subscription = Network.getInstance("删除问题", this)
                .delete_wenti_request(params,
                        new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                            @Override
                            public void on_post_entity(List mlocation) {
                                Log.e("123", "删除问题成功：");
                                finish();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "删除问题报错：" + error);
                                Toast.makeText(FenLeiQuestionDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }, this, false));
    }


    @Override
    public void widgetClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}

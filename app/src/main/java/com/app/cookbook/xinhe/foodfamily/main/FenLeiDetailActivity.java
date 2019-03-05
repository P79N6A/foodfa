package com.app.cookbook.xinhe.foodfamily.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.callback.ImageBiggerCallBack;
import com.app.cookbook.xinhe.foodfamily.main.adapter.FenLeiDetailAdapter;
import com.app.cookbook.xinhe.foodfamily.main.adapter.SimpleAdapter2;
import com.app.cookbook.xinhe.foodfamily.main.entity.FenLeiDetailDate;
import com.app.cookbook.xinhe.foodfamily.main.entity.QuesEn;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.net.rx.RxUtils;
import com.app.cookbook.xinhe.foodfamily.util.BianjiToolbarControl2;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.functions.Action1;

public class FenLeiDetailActivity extends BaseActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.toolbar)
    BianjiToolbarControl2 mToolbar;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.empty_layout)
    RelativeLayout empty_layout;

    private FenLeiDetailAdapter jingXuanAdapter;
    private String fenlei_id;
    private int PAGE = 1;
    private boolean is_not_more;

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
            if (null != parms.getString("fenlei_id")) {
                fenlei_id = parms.getString("fenlei_id");
            } else {
                fenlei_id = "-1";
            }
        }
        Log.e("传过来的分类ID", fenlei_id);
    }

    @Override
    public void initView() {
        setContentLayout(R.layout.activity_fen_lei_detail);
        //初始化黄牛刀
        ButterKnife.bind(this);
        isShowTitle(false);

    }

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
                Log.e("123", "dataString-----   " + dataString + "   ----- scheme ------    "
                        + scheme + "   --- host ---   " + host + "   ----- query ----    " + query);
                Log.e("123", "             " + querynub);
            }
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

        mToolbar.setBackButtonOnClickListerner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //下方问题列表的数据
        initNetResource(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    List<QuesEn> quesEnList = new ArrayList<>();

    FenLeiDetailDate fenLeiDetailDate = new FenLeiDetailDate();


    //标签详情
    private void initNetResource(boolean is_show_dialog) {
        Map<String, String> params = new HashMap<>();
        params.put("class_id", fenlei_id);
        params.put("page", String.valueOf(PAGE));
        Log.e("标签详情：", params.toString());
        subscription = Network.getInstance("标签详情", FenLeiDetailActivity.this)
                .found_detail(params,
                        new ProgressSubscriberNew<>(FenLeiDetailDate.class, new GsonSubscriberOnNextListener<FenLeiDetailDate>() {
                            @Override
                            public void on_post_entity(FenLeiDetailDate result) {
//                        //设置页面的信息
                                fenLeiDetailDate = result;
//                                Log.e("标签详情成功", result.getQuestion_data().getData().size() + "");
                                if (result.getQuestion_data().getData().size() > 0) {
                                    empty_layout.setVisibility(View.GONE);
                                    if (quesEnList.size() > 0 && PAGE > 1) {//这表示是"加载"
                                        if (result.getQuestion_data().getData().size() == 0) {
                                            //表示没有更多数据了
                                            is_not_more = true;
                                            quesEnList.addAll(result.getQuestion_data().getData());
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            is_not_more = false;
                                            quesEnList.addAll(result.getQuestion_data().getData());
                                            adapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        quesEnList = result.getQuestion_data().getData();
                                        set_answer_list2(result.getQuestion_data().getData());
                                    }
                                } else {
                                    if (quesEnList.size() == 0) {
                                        empty_layout.setVisibility(View.VISIBLE);
                                        set_answer_list2(quesEnList);
                                    }
                                }
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Logger.e("标签详情报错：" + error);
                            }
                        }, FenLeiDetailActivity.this, is_show_dialog));
    }


    private LinearLayoutManager linearLayoutManager;
    private int tvContentHeight;
    private int tvBackHeight;
    private View headerView;
    SimpleAdapter2 adapter;
    public TextView tv_content, tv_back, question_number;
    public ImageView iv_arrow;
    public CircleImageView user_head_image;
    public TextView guanzhu_number, wenti_number, guanzhu_title;
    public LinearLayout guanzhu_layout_top, huxiangguanzhu_tv, miaoshu_layout;

    private void set_answer_list2(List<QuesEn> mquesEnList) {
        xrefreshview.setPullLoadEnable(true);
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemAnimator(null);
        // 设置静默加载模式
//		xRefreshView1.setSilenceLoadMore();
        linearLayoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(linearLayoutManager);
        adapter = new SimpleAdapter2(mquesEnList, this, new ImageBiggerCallBack() {
            @Override
            public void image_url(View view, String url) {
                Rect frame = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int statusBarHeight = frame.top;

                int[] location = new int[2];
                view.getLocationOnScreen(location);
                location[1] += statusBarHeight;

                int width = view.getWidth();
                int height = view.getHeight();
                navigateToImagesDetail(
                        url,
                        location[0],
                        location[1],
                        width,
                        height);
            }
        });
        headerView = adapter.setHeaderView(R.layout.item_header_layout, recycler_view);
        miaoshu_layout = (LinearLayout) headerView.findViewById(R.id.miaoshu_layout);
        user_head_image = (CircleImageView) headerView.findViewById(R.id.user_head_image);
        guanzhu_number = (TextView) headerView.findViewById(R.id.guanzhu_number);
        tv_content = (TextView) headerView.findViewById(R.id.tv_content);
        tv_back = (TextView) headerView.findViewById(R.id.tv_back);
        iv_arrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
        wenti_number = (TextView) headerView.findViewById(R.id.wenti_number);
        guanzhu_title = (TextView) headerView.findViewById(R.id.guanzhu_title);
        huxiangguanzhu_tv = (LinearLayout) headerView.findViewById(R.id.huxiangguanzhu_tv);
        guanzhu_layout_top = (LinearLayout) headerView.findViewById(R.id.guanzhu_layout_top);
        question_number = (TextView) headerView.findViewById(R.id.question_number);

        //初始化headview的信息
        initHeadViewResource();

        xrefreshview.setSilenceLoadMore(true);
        recycler_view.setAdapter(adapter);
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);
        xrefreshview.setPullLoadEnable(true);
        xrefreshview.setPreLoadCount(10);
        if (mquesEnList.size() > 7) {
            xrefreshview.enableReleaseToLoadMore(true);
//            adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
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
                    //Log.e(TAG, "alpha:" + alpha + "top :" + headerView.getTop() + " height: " + headerView.getHeight());
                    if (alpha > 0 && alpha < 0.5) {
                        //隐藏旁边两个分享按钮
                    } else if (alpha > 0.5 && alpha < 1) {
                        mToolbar.setTitle(fenLeiDetailDate.getTitle());
                        mToolbar.showLeft();//显示左边的图片
                        mToolbar.showRight();
                        mToolbar.setRightNumbers(fenLeiDetailDate.getQuestion_count() + "问题");
                        mToolbar.setQuanJuColor(Color.parseColor("#ffffff"));
                    } else {
                        mToolbar.setTitle("");
                        mToolbar.showLeft();
                        mToolbar.hideRight();

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
                        initNetResource(false);
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
                        initNetResource(false);

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

    private void navigateToImagesDetail(String url, int x, int y, int width, int height) {
        Bundle extras = new Bundle();
        extras.putString(ImagesDetailActivity.INTENT_IMAGE_URL_TAG, url);
        extras.putInt(ImagesDetailActivity.INTENT_IMAGE_X_TAG, x);
        extras.putInt(ImagesDetailActivity.INTENT_IMAGE_Y_TAG, y);
        extras.putInt(ImagesDetailActivity.INTENT_IMAGE_W_TAG, width);
        extras.putInt(ImagesDetailActivity.INTENT_IMAGE_H_TAG, height);
        readyGo(ImagesDetailActivity.class, extras);
        overridePendingTransition(0, 0);
    }


    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    private void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(FenLeiDetailActivity.this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    private void initHeadViewResource() {
        //添加描述
        if (null != fenLeiDetailDate.getDesc()) {
            if (fenLeiDetailDate.getDesc().length() == 0) {
                miaoshu_layout.setVisibility(View.GONE);
                tv_content.setVisibility(View.GONE);
                tv_back.setVisibility(View.GONE);
                iv_arrow.setVisibility(View.GONE);
            } else {
                miaoshu_layout.setVisibility(View.VISIBLE);
                tv_content.setText(fenLeiDetailDate.getDesc());
                tv_back.setText(fenLeiDetailDate.getDesc());
                //测量tv_content的高度
                ViewTreeObserver vto1 = tv_content.getViewTreeObserver();
                vto1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        tvContentHeight = tv_content.getHeight();
                        //获得高度之后，移除监听
                        tv_content.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    }
                });
                //测量tv_back的高度
                ViewTreeObserver vto = tv_back.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        tvBackHeight = tv_back.getHeight();
                        tv_back.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        //比较高度：
                        if (tvBackHeight > tvContentHeight) {
                            //说明有展开的内容：
                            //默认是关闭状态：
                            tv_content.setTag(true);
                            iv_arrow.setVisibility(View.VISIBLE);

                        } else {
                            iv_arrow.setVisibility(View.GONE);
                            tv_content.setTag(false);
                        }
                        tv_back.setVisibility(View.GONE);

                    }
                });
                //点击文字展开
                tv_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean tag = (boolean) tv_content.getTag();
                        if (tag) {
                            //如果是true,点击则展开：
                            tv_content.setTag(false);
                            tv_content.setMaxLines(Integer.MAX_VALUE);
                            iv_arrow.setImageResource(R.drawable.shangla);

                        } else {
                            iv_arrow.setImageResource(R.drawable.xiala);
                            tv_content.setTag(true);
                            tv_content.setMaxLines(3);
                        }
                    }
                });
                //点击图标展开
                iv_arrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean tag = (boolean) tv_content.getTag();
                        if (tag) {
                            //如果是true,点击则展开：
                            tv_content.setTag(false);
                            tv_content.setMaxLines(Integer.MAX_VALUE);
                            iv_arrow.setImageResource(R.drawable.shangla);
                        } else {
                            iv_arrow.setImageResource(R.drawable.xiala);
                            tv_content.setTag(true);
                            tv_content.setMaxLines(3);
                        }
                    }
                });
            }

        } else {
            miaoshu_layout.setVisibility(View.GONE);
            tv_content.setVisibility(View.GONE);
            tv_back.setVisibility(View.GONE);
            iv_arrow.setVisibility(View.GONE);
        }
        //设置右上角头像
        Glide.with(this).load(fenLeiDetailDate.getPath())
                .error(R.drawable.touxiang)
                .into(user_head_image);

        //设置关注数量
        guanzhu_number.setText(fenLeiDetailDate.getCount_follow_users() + "关注");

        question_number.setText(fenLeiDetailDate.getQuestion_count() + "问题");

        //分类标题
        guanzhu_title.setText(fenLeiDetailDate.getTitle());

        //设置关注
        RxUtils.clickView(guanzhu_layout_top)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if ("".equals(SharedPreferencesHelper.get(FenLeiDetailActivity.this, "login_token", ""))) {
                            Intent intent = new Intent(FenLeiDetailActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            guanzhu_submit(fenLeiDetailDate.getId());
                        }
                    }
                });
        //取消关注
        RxUtils.clickView(huxiangguanzhu_tv)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if ("".equals(SharedPreferencesHelper.get(FenLeiDetailActivity.this, "login_token", ""))) {
                            Intent intent = new Intent(FenLeiDetailActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            quxiao_guanzhu_submit(fenLeiDetailDate.getId());
                        }
                    }
                });

        //设置关注按钮状态
        if (fenLeiDetailDate.getIs_follow().equals("1")) {
            guanzhu_layout_top.setVisibility(View.GONE);
            huxiangguanzhu_tv.setVisibility(View.VISIBLE);
        } else {
            guanzhu_layout_top.setVisibility(View.VISIBLE);
            huxiangguanzhu_tv.setVisibility(View.GONE);
        }


    }

    /**
     * 取消关注
     *
     * @param user_id
     */
    private void quxiao_guanzhu_submit(String user_id) {
        Map<String, String> params = new HashMap<>();
        if (null != user_id) {
            params.put("id", user_id);
        }
        Log.e("取消关注分类ID：", params.toString());
        subscription = Network.getInstance("取消关注分类", this)
                .quxiao_guanzhu(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Logger.e("取消关注分类成功：" + result.getCode());
                                fenLeiDetailDate.setIs_follow("2");
                                guanzhu_layout_top.setVisibility(View.VISIBLE);
                                huxiangguanzhu_tv.setVisibility(View.GONE);
                                fenLeiDetailDate.setFollow(Integer.valueOf(fenLeiDetailDate.getFollow()) - 1 + "");
                                guanzhu_number.setText(fenLeiDetailDate.getFollow() + "关注");
                            }

                            @Override
                            public void onError(String error) {
                                Logger.e("取消关注分类报错：" + error);
                            }
                        }, this, false));

    }

    private void guanzhu_submit(String user_id) {
        Map<String, String> params = new HashMap<>();
        if (null != user_id) {
            params.put("id", user_id);
        }
        Log.e("关注分类ID：", params.toString());
        subscription = Network.getInstance("关注分类", this)
                .guanzhufenlei(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Logger.e("关注分类成功：" + result.getCode());
                                fenLeiDetailDate.setIs_follow("1");
                                guanzhu_layout_top.setVisibility(View.GONE);
                                huxiangguanzhu_tv.setVisibility(View.VISIBLE);
                                fenLeiDetailDate.setFollow(Integer.valueOf(fenLeiDetailDate.getFollow()) + 1 + "");
                                guanzhu_number.setText(fenLeiDetailDate.getFollow() + "关注");

                            }

                            @Override
                            public void onError(String error) {
                                Logger.e("关注分类报错：" + error);
                            }
                        }, this, false));

    }

    private void set_source(FenLeiDetailDate questionDate) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);
        jingXuanAdapter = new FenLeiDetailAdapter(FenLeiDetailActivity.this, questionDate);
        // 静默加载模式不能设置footerview
        recycler_view.setAdapter(jingXuanAdapter);

        recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
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
                    Log.e(TAG, "alpha:" + alpha + "top :" + headerView.getTop() + " height: " + headerView.getHeight());
                    if (alpha > 0 && alpha < 0.5) {
                        //隐藏旁边两个分享按钮
                    } else if (alpha > 0.5 && alpha < 1) {
                        mToolbar.setQuanJuColor(Color.parseColor("#ffffff"));
                        mToolbar.setTitle(fenLeiDetailDate.getTitle());
                        mToolbar.setRightNumbers(fenLeiDetailDate.getQuestions() + "问题");
                        mToolbar.showLeft();//显示左边的图片
                        mToolbar.showRight();
                    } else {
                        mToolbar.setQuanJuColor(Color.parseColor("#00000000"));
                        mToolbar.setTitle("");
                        mToolbar.hideLeft();
                        mToolbar.hideRight();
                    }
                }

            }
        });
    }

    @Override
    public void widgetClick(View v) {

    }


}

package com.app.cookbook.xinhe.foodfamily.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.main.adapter.JingXuanAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.HuiDaDate;
import com.app.cookbook.xinhe.foodfamily.main.entity.HuiDaEntity;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HuiDaActivity extends BaseActivity {
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.iamge_back)
    ImageView iamge_back;
    @BindView(R.id.back_layout)
    LinearLayout back_layout;
    @BindView(R.id.title_two)
    TextView title_two;
    private JingXuanAdapter jingXuanAdapter;
    private int PAGE = 1;
    private boolean is_not_more;
    private LinearLayoutManager layoutManager;
    private boolean is_unable_loadmore;
    private List<HuiDaEntity> daEntities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void RightImgOnclick() {


    }

    @Override
    public void initParms(Bundle parms) {
    }

    @Override
    public void initView() {
        setContentLayout(R.layout.activity_hui_da);
        //初始化黄牛刀
        ButterKnife.bind(this);
        isShowTitle(false);
        setGestureListener();
        init_net_resources(true);
    }

    private int y;

    private void setGestureListener() {
        recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                switch (newState) {
                    case 0:
                        if (!String.valueOf(y).contains("-") && !"0".equals(String.valueOf(y))) {
                            if (getScollYDistance() >= 200) {
                                title_two.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (firstVisibleItem == 0) {
                                title_two.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case 1:
                        if (!String.valueOf(y).contains("-") && !"0".equals(String.valueOf(y))) {
                            if (getScollYDistance() >= 200) {
                                title_two.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (firstVisibleItem == 0) {
                                title_two.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case 2:
                        if (!String.valueOf(y).contains("-") && !"0".equals(String.valueOf(y))) {
                            if (getScollYDistance() >= 200) {
                                title_two.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (firstVisibleItem == 0) {
                                title_two.setVisibility(View.GONE);
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                y = dy;
            }
        });
    }

    public int getScollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recycler_view.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    @Override
    public void initonResume() {
    }

    @Override
    public void initonPause() {

    }

    @Override
    public void doBusiness(Context mContext) {

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init_net_resources(boolean is_show_dialog) {
        Log.e("获取回答的问题页数", PAGE + "");
        subscription = Network.getInstance("获取回答的问题", this)
                .get_answer(String.valueOf(PAGE),
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<HuiDaDate>>() {
                            @Override
                            public void onNext(Bean<HuiDaDate> result) {
                                Logger.e("获取回答的问题成功：" + result.getCode());
                                if (daEntities.size() > 0 && PAGE > 1) {//这表示是"加载"
                                    if (result.getData().getData().size() == 0) {
                                        //表示没有更多数据了
                                        is_not_more = true;
                                        daEntities.addAll(result.getData().getData());
                                        jingXuanAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        daEntities.addAll(result.getData().getData());
                                        jingXuanAdapter.notifyDataSetChanged();
                                    }

                                } else {
                                    daEntities = result.getData().getData();
                                    set_list_resource(result.getData().getData());
                                }
                            }

                            @Override
                            public void onError(String error) {
                                is_not_more = true;
                                if (error.equals("无数据")) {
                                    Log.e("123", "暂时没有数据！");
                                } else {
                                    Logger.e("获取提问报错：" + error);
                                }
                                if (daEntities.size() == 0) {
                                    set_list_resource(daEntities);
                                }

                            }
                        }, this, is_show_dialog));
    }

    private boolean ista = false;

    private void set_list_resource(List<HuiDaEntity> daEntities) {
        //设置上拉刷新下拉加载
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemAnimator(null);
        layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.smoothScrollToPosition(0);
        jingXuanAdapter = new JingXuanAdapter(daEntities, getApplicationContext(), ista);
        jingXuanAdapter.setTitleName("我的回答");
        jingXuanAdapter.setType("1");
        recycler_view.setAdapter(jingXuanAdapter);
        // 静默加载模式不能设置footerview
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
        xrefreshview.enableRecyclerViewPullUp(true);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
        //设置静默加载时提前加载的item个数
        xrefreshview.setPreLoadCount(10);
        if (daEntities.size() > 7) {
            xrefreshview.enableReleaseToLoadMore(true);
//            jingXuanAdapter.setCustomLoadMoreView(new XRefreshViewFooter(this));//加载更多
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
                        //填写刷新数据的网络请求，一般page=1，List集合清空操作
                        init_net_resources(false);
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
                        init_net_resources(false);

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
    public void widgetClick(View v) {

    }
}
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
import com.app.cookbook.xinhe.foodfamily.main.adapter.GuanZhuWentiAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.MineGuanZhuQuestionEntity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GuanZhuQuestionActivity extends BaseActivity {
    @BindView(R.id.iamge_back)
    ImageView iamge_back;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.back_layout)
    LinearLayout back_layout;
    private GuanZhuWentiAdapter guanZhuWentiAdapter;
    private int PAGE = 1;
    private boolean is_not_more;
    private LinearLayoutManager layoutManager;
    private List<MineGuanZhuQuestionEntity.DataBean> dates = new ArrayList<>();
    @BindView(R.id.title_two)
    TextView title_two;

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
        setContentLayout(R.layout.activity_guan_zhu_question);
        //初始化黄牛刀
        ButterKnife.bind(this);
        isShowTitle(false);
        setGestureListener();
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
        //获取网络数据
        init_net_source(true);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    private void init_net_source(boolean is_show_dialog) {
        subscription = Network.getInstance("关注问题", this)
                .get_guanzhu_mine_question(String.valueOf(PAGE),
                        new ProgressSubscriberNew<>(MineGuanZhuQuestionEntity.class, new GsonSubscriberOnNextListener<MineGuanZhuQuestionEntity>() {
                            @Override
                            public void on_post_entity(MineGuanZhuQuestionEntity result) {
                                Log.e("可令肌肤莱克斯顿","精神科龙卷风是");
                                if (dates.size() > 0 && PAGE > 1) {//这表示是"加载"
                                    if (result.getData().size() == 0) {
                                        //表示没有更多数据了
                                        is_not_more = true;
                                        dates.addAll(result.getData());
                                        guanZhuWentiAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        dates.addAll(result.getData());
                                        guanZhuWentiAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    dates = result.getData();
                                    set_list_resource(result.getData());
                                }
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                is_not_more = true;
                                if (error.equals("无数据")) {
                                    Log.e("123","暂时没有数据！");

                                } else {
                                    Log.e("123","获取关注问题报错：" + error);
                                }
                                if (dates.size() == 0) {
                                    set_list_resource(dates);
                                }
                            }
                        }, this, is_show_dialog));
    }


    private boolean ista = false;

    private void set_list_resource(List<MineGuanZhuQuestionEntity.DataBean> dates) {
        //设置上拉刷新下拉加载
        recycler_view.setItemAnimator(null);
        recycler_view.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.smoothScrollToPosition(0);
        guanZhuWentiAdapter = new GuanZhuWentiAdapter(dates, getApplicationContext(), ista);
        guanZhuWentiAdapter.setTitleName("我关注的问题");
        recycler_view.setAdapter(guanZhuWentiAdapter);
        // 静默加载模式不能设置footerview
        // 设置静默加载模式
        xrefreshview.setSilenceLoadMore(true);
        //设置刷新完成以后，headerview固定的时间
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);
        xrefreshview.setPullRefreshEnable(true);
        xrefreshview.setPullLoadEnable(true);
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
        //给recycler_view设置底部加载布局
        xrefreshview.enableRecyclerViewPullUp(true);
        //设置静默加载时提前加载的item个数
        xrefreshview.setPreLoadCount(10);
        if (dates.size() > 7) {
            xrefreshview.enableReleaseToLoadMore(true);
//            guanZhuWentiAdapter.setCustomLoadMoreView(new XRefreshViewFooter(this));//加载更多
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
                        init_net_source(false);
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
                        init_net_source(false);

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

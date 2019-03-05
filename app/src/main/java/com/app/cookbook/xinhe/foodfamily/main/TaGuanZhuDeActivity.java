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
import com.app.cookbook.xinhe.foodfamily.main.adapter.TaGuanZhuRenAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.WoGuanZhuEntity;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaGuanZhuDeActivity extends BaseActivity {
    @BindView(R.id.iamge_back)
    ImageView iamge_back;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.back_layout)
    LinearLayout back_layout;
    @BindView(R.id.title_two)
    TextView title_two;
    TaGuanZhuRenAdapter woGuanZhuRenAdapter;
    String id;
    private int PAGE = 1;
    private boolean is_not_more;
    LinearLayoutManager layoutManager;
    List<WoGuanZhuEntity.DataBean> dates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {
        id = parms.getString("user_id");
    }

    @Override
    public void initView() {
        setContentLayout(R.layout.activity_ta_guan_zhu_de);
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
        iamge_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //获取网络数据
        init_net_source(true);
    }

    private void init_net_source(boolean is_show_dialog) {
        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(PAGE));
        params.put("uuid", id);
//        params.put("status", "1");
        Log.e("关注ta的人参数：", params.toString());
        subscription = Network.getInstance("关注ta的人", this)
                .get_ta_guanzhu_ren(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<WoGuanZhuEntity>>() {
                            @Override
                            public void onNext(Bean<WoGuanZhuEntity> result) {
                                Log.e("123", "获取ta关注的的人成功：" + result.getCode());
                                if (dates.size() > 0 && PAGE > 1) {//这表示是"加载"
                                    if (result.getData().getData().size() == 0) {
                                        //表示没有更多数据了
                                        is_not_more = true;
                                        dates.addAll(result.getData().getData());
                                        woGuanZhuRenAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        dates.addAll(result.getData().getData());
                                        woGuanZhuRenAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    dates = result.getData().getData();
                                    set_list_resource(result.getData().getData());
                                }
                            }

                            @Override
                            public void onError(String error) {
                                is_not_more = true;
                                if (error.equals("无数据")) {
                                    Log.e("123", "暂时没有数据！");
                                } else {
                                    Log.e("123", "获取ta关注的的人报错：" + error);
                                }

                                if (dates.size() == 0) {
                                    set_list_resource(dates);
                                }
                            }

                        }, this, is_show_dialog));

    }

    private boolean ista = true;

    private void set_list_resource(List<WoGuanZhuEntity.DataBean> dates) {
        //设置上拉刷新下拉加载
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemAnimator(null);
        layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        woGuanZhuRenAdapter = new TaGuanZhuRenAdapter(dates, getApplicationContext(), ista);
        woGuanZhuRenAdapter.setTitleName("TA关注的人");
        recycler_view.setAdapter(woGuanZhuRenAdapter);
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
        //给recycler_view设置底部加载布局
        xrefreshview.enableReleaseToLoadMore(true);
        xrefreshview.enableRecyclerViewPullUp(true);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
        xrefreshview.setPreLoadCount(10);
        if (dates.size() > 7) {
            xrefreshview.enableReleaseToLoadMore(true);
//            woGuanZhuRenAdapter.setCustomLoadMoreView(new XRefreshViewFooter(this));//加载更多
            xrefreshview.setLoadComplete(false);//显示底部
        } else {
            xrefreshview.enableReleaseToLoadMore(false);
            xrefreshview.setLoadComplete(true);//隐藏底部
        }
        //设置静默加载时提前加载的item个数
//        xefreshView1.setPreLoadCount(4);

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

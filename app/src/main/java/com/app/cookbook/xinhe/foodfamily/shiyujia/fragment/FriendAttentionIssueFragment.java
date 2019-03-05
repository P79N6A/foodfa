package com.app.cookbook.xinhe.foodfamily.shiyujia.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.adapter.GuanZhuWentiAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.MineGuanZhuQuestionEntity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

public class FriendAttentionIssueFragment extends Fragment {

    protected Subscription subscription;
    private View view;
    @BindView(R.id.amount_tv)
    TextView amount_tv;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private GuanZhuWentiAdapter myAttentionIssueAdapter;
    private int PAGE = 1;
    private boolean is_not_more;
    private LinearLayoutManager layoutManager;
    private List<MineGuanZhuQuestionEntity.DataBean> dates = new ArrayList<>();

    private String id;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.recyclerview_list, container, false);
            ButterKnife.bind(this, view);
            initView();
            init_net_source(false);
        }
        return view;
    }

    private void initView() {
        id = getActivity().getIntent().getStringExtra("user_id");
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    private void init_net_source(boolean is_show_dialog) {
        Log.e("123", "              " + id + "         " + PAGE);
        Map<String, String> params = new HashMap<>();
        params.put("uid", id);
        params.put("page", String.valueOf(PAGE));
        subscription = Network.getInstance("ta关注问题", getActivity())
                .get_ta_guanzhu_question(params,
                        new ProgressSubscriberNew<>(MineGuanZhuQuestionEntity.class, new GsonSubscriberOnNextListener<MineGuanZhuQuestionEntity>() {
                            @Override
                            public void on_post_entity(MineGuanZhuQuestionEntity result) {
                                Log.e("123", "            " + result.getData().size());
                                amount_tv.setVisibility(View.VISIBLE);
                                amount_tv.setText("共关注" + result.getTotal() + "个问题");
                                if (dates.size() > 0 && PAGE > 1) {//这表示是"加载"
                                    if (result.getData().size() == 0) {
                                        //表示没有更多数据了
                                        is_not_more = true;
                                        dates.addAll(result.getData());
                                        myAttentionIssueAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        dates.addAll(result.getData());
                                        myAttentionIssueAdapter.notifyDataSetChanged();
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
                                    Log.e("123", "暂时没有数据！");

                                } else {
                                    Logger.e("获取关注问题报错：" + error);
                                }
                                if (dates.size() == 0) {
                                    set_list_resource(dates);
                                }
                            }
                        }, getActivity(), is_show_dialog));
    }


    private boolean ista = true;

    private void set_list_resource(List<MineGuanZhuQuestionEntity.DataBean> dates) {
        //设置上拉刷新下拉加载
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(0);
        myAttentionIssueAdapter = new GuanZhuWentiAdapter(dates, getActivity(), ista);
        myAttentionIssueAdapter.setTitleName("TA关注的问题");
        recyclerView.setAdapter(myAttentionIssueAdapter);
        // 静默加载模式不能设置footerview
        // 设置静默加载模式
        xrefreshview.setSilenceLoadMore(true);
        //设置刷新完成以后，headerview固定的时间
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);
        xrefreshview.setPullRefreshEnable(true);
        xrefreshview.setPullLoadEnable(true);
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.enableReleaseToLoadMore(true);
        xrefreshview.enableRecyclerViewPullUp(true);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
        //给recyclerView设置底部加载布局
//        myAttentionIssueAdapter.setCustomLoadMoreView(new XRefreshViewFooter(this));//加载更多
        xrefreshview.enableReleaseToLoadMore(true);
        xrefreshview.enableRecyclerViewPullUp(true);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
        xrefreshview.setLoadComplete(false);//隐藏底部
        //设置静默加载时提前加载的item个数
        xrefreshview.setPreLoadCount(10);

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


}

package com.app.cookbook.xinhe.foodfamily.shiyujia.fragment;

import android.content.Intent;
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
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.adapter.GuanZhuBiaoQianAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.BiaoQianEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.GuanZhuBiaoQianEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.MineGuanZhuQuestionEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.WoGuanZhuEntity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.LabelDetailsActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.MyAttentionIssueAdapter;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.MyAttentionLabelAdapter;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.MyAttentionUserAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

public class MyAttentionLabelFragment extends Fragment {

    protected Subscription subscription;
    private View view;
    @BindView(R.id.amount_tv)
    TextView amount_tv;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private MyAttentionLabelAdapter myAttentionLabelAdapter;
    private int PAGE = 1;
    private boolean is_not_more;
    private String NoMsg = "0";
    private List<BiaoQianEntity> dates = new ArrayList<>();
    private LinearLayoutManager layoutManager;


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
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getItemClick() {
        myAttentionLabelAdapter.setOnItemClickListener(new MyAttentionLabelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BiaoQianEntity item = dates.get(position);
                Intent intent = new Intent(getActivity(), LabelDetailsActivity.class);
                intent.putExtra("id", item.getClass_id());
                startActivity(intent);
            }
        });
    }

    private void init_net_source(boolean is_show_dialog) {
        subscription = Network.getInstance("关注的标签", getActivity())
                .get_my_guanzhu_biaoqian(String.valueOf(PAGE),
                        new ProgressSubscriberNew<>(GuanZhuBiaoQianEntity.class, new GsonSubscriberOnNextListener<GuanZhuBiaoQianEntity>() {
                            @Override
                            public void on_post_entity(GuanZhuBiaoQianEntity result) {
                                Log.e("123", "    关注的标签参数  ");
                                amount_tv.setVisibility(View.VISIBLE);
                                amount_tv.setText("共关注" + result.getTotal() + "个标签");
                                if (result.getData().size() == 0 && PAGE == 1) {
                                    set_list_resource(dates);
                                }
                                if (dates.size() > 0 && PAGE > 1) {//这表示是"加载"
                                    if (result.getData().size() == 0) {
                                        //表示没有更多数据了
                                        is_not_more = true;
                                        dates.addAll(result.getData());
                                        myAttentionLabelAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = true;
                                        dates.addAll(result.getData());
                                        myAttentionLabelAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    dates = result.getData();
                                    set_list_resource(dates);
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
                                    NoMsg = "1";
                                } else {
                                    Logger.e("获取关注我的人报错：" + error);
                                }
                                if (dates.size() == 0) {
                                    set_list_resource(dates);
                                }
                            }
                        }, getActivity(), is_show_dialog));
    }

    private void set_list_resource(final List<BiaoQianEntity> dates) {
        //设置上拉刷新下拉加载
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        myAttentionLabelAdapter = new MyAttentionLabelAdapter(getActivity());
        myAttentionLabelAdapter.setList(dates);
        recyclerView.setAdapter(myAttentionLabelAdapter);
        getItemClick();
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
        xrefreshview.enableRecyclerViewPullUp(true);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
        //设置静默加载时提前加载的item个数
        xrefreshview.setPreLoadCount(10);
        if (dates.size() > 7) {
            xrefreshview.enableReleaseToLoadMore(true);
//            guanZhuWoRenAdapter.setCustomLoadMoreView(new XRefreshViewFooter(this));//加载更多
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


}

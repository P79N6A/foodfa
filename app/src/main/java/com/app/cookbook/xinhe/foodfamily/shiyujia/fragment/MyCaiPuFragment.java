package com.app.cookbook.xinhe.foodfamily.shiyujia.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.BannerDetailWebActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.ImageTextDetailActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.CaiPuEntity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.MyCollectCaipuAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;


public class MyCaiPuFragment extends Fragment {
    protected Subscription subscription;
    private View mView;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.recyclerView)
    RecyclerView recycler_view;
    @BindView(R.id.amount_tv)
    TextView amount_tv;
    @BindView(R.id.empty_layout)
    RelativeLayout empty_layout;
    @BindView(R.id.image_empty)
    ImageView image_empty;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    //
    private LinearLayoutManager layoutManager;

    //回答集合
    private List<CaiPuEntity.DataBean> daEntities = new ArrayList<>();
    //回答适配器
    private MyCollectCaipuAdapter collectCaipuAdapter;

    //当前页
    private int PAGE = 1;
    //是否加载更多
    private boolean is_not_more;
    private String id;

    public MyCaiPuFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_my_cai_pu, container, false);
            ButterKnife.bind(this, mView);
            initView();
            requestHuiDa(true);
        }
        return mView;
    }

    private void initView() {
        id = getActivity().getIntent().getStringExtra("user_id");
        layoutManager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(layoutManager);

    }

    private void requestHuiDa(boolean is_show_dialog) {
        subscription = Network.getInstance("菜谱", getActivity())
                .getOwnCollectCaipu(String.valueOf(PAGE),
                        new ProgressSubscriberNew<>(CaiPuEntity.class, new GsonSubscriberOnNextListener<CaiPuEntity>() {
                            @Override
                            public void on_post_entity(CaiPuEntity result) {
                                Log.e("123", "获取菜谱成功：" + result.getData().size());
                                amount_tv.setVisibility(View.VISIBLE);
                                amount_tv.setText("共收藏" + result.getTotal() + "个菜谱");
                                if (result.getData().size() == 0 && PAGE == 1) {
                                    image_empty.setImageResource(R.drawable.caipu_empty);
                                    tv_empty.setText("还没有收藏菜谱呦～");
                                    empty_layout.setVisibility(View.VISIBLE);
                                } else {
                                    empty_layout.setVisibility(View.GONE);
                                }
                                if (daEntities.size() > 0 && PAGE > 1) {//这表示是"加载"
                                    if (result.getData().size() == 0) {
                                        //表示没有更多数据了
                                        is_not_more = true;
                                        daEntities.addAll(result.getData());
                                        collectCaipuAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        daEntities.addAll(result.getData());
                                        collectCaipuAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    daEntities = result.getData();
                                    set_list_resource(daEntities);
                                }
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                if (daEntities.size() == 0 && error.contains("无") || "断网".equals(error)) {
                                    amount_tv.setVisibility(View.VISIBLE);
                                    amount_tv.setText("共收藏0个菜谱");
                                    image_empty.setImageResource(R.drawable.caipu_empty);
                                    tv_empty.setText("还没有收藏菜谱呦～");
                                    empty_layout.setVisibility(View.VISIBLE);
                                }else if(PAGE==1&&error.contains("无数据")){
                                    daEntities.clear();
                                    collectCaipuAdapter.notifyDataSetChanged();
                                    amount_tv.setVisibility(View.VISIBLE);
                                    amount_tv.setText("共收藏0个菜谱");
                                    image_empty.setImageResource(R.drawable.caipu_empty);
                                    tv_empty.setText("还没有收藏菜谱呦～");
                                    empty_layout.setVisibility(View.VISIBLE);
                                }
                                xrefreshview.stopRefresh();//刷新停止
                                Log.e("123", "   菜谱报错  " + error);
                            }
                        }, getActivity(), is_show_dialog));
    }


    private void set_list_resource(final List<CaiPuEntity.DataBean> dates) {
        //设置上拉刷新下拉加载
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemAnimator(null);
        layoutManager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(layoutManager);
        collectCaipuAdapter = new MyCollectCaipuAdapter(getActivity());
        collectCaipuAdapter.setDaEntities(dates);
        recycler_view.setAdapter(collectCaipuAdapter);
        collectCaipuAdapter.notifyDataSetChanged();
        collectCaipuAdapter.setOnItemClickListener(new MyCollectCaipuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.e("今飞凯达螺蛳粉","今飞凯达螺蛳粉");
                Intent intent = new Intent(getActivity(), BannerDetailWebActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("detail_id", dates.get(position).getId()+"");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
                        requestHuiDa(false);
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
                        requestHuiDa(false);

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

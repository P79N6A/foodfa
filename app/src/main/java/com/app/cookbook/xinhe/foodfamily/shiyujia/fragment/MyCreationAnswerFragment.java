package com.app.cookbook.xinhe.foodfamily.shiyujia.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.adapter.GuanZhuWoRenAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.GuanZhuwoEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.HuiDaDate;
import com.app.cookbook.xinhe.foodfamily.main.entity.HuiDaEntity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.UserAnswerAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

public class MyCreationAnswerFragment extends Fragment {

    protected Subscription subscription;
    private View mView;
    @BindView(R.id.amount_tv)
    TextView amount_tv;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.recyclerView)
    RecyclerView recycler_view;
    @BindView(R.id.empty_layout)
    RelativeLayout empty_layout;
    @BindView(R.id.image_empty)
    ImageView image_empty;
    @BindView(R.id.tv_empty)
    TextView tv_empty;

    private LinearLayoutManager layoutManager;

    //回答集合
    private List<HuiDaEntity> daEntities = new ArrayList<>();
    //回答适配器
    private UserAnswerAdapter userAnswerAdapter;

    //当前页
    private int PAGE = 1;
    //是否加载更多
    private boolean is_not_more;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.recyclerview_list, container, false);
            ButterKnife.bind(this, mView);
            requestHuiDa(true);
        }
        return mView;
    }

    private void getItemClick() {
//        userAnswerAdapter.setOnItemClickListener(new UserAnswerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                HuiDaEntity item = daEntities.get(position);
//                Intent intent = new Intent(getActivity(), FenLeiQuestionDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("wenti_id", item.getId());
//                bundle.putString("flag", "0");
//                intent.putExtras(bundle);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
//        });
    }

    private void requestHuiDa(boolean is_show_dialog) {
        Log.e("获取回答的问题页数", PAGE + "");
        subscription = Network.getInstance("回答", getActivity())
                .get_answer(String.valueOf(PAGE),
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<HuiDaDate>>() {
                            @Override
                            public void onNext(Bean<HuiDaDate> result) {
                                Log.e("123", "回答成功：" + result.getMsg());
                                amount_tv.setVisibility(View.VISIBLE);
                                amount_tv.setText("共" + result.getData().getTotal() + "条答案");
                                if (PAGE == 1 && result.getData().getData().size() == 0) {
                                    image_empty.setImageResource(R.drawable.huida_empty);
                                    tv_empty.setText("还没有创作问题呦～");
                                    empty_layout.setVisibility(View.VISIBLE);
                                }

//                                empty_layout
                                if (daEntities.size() > 0 && PAGE > 1) {//这表示是"加载"
                                    if (result.getData().getData().size() == 0) {
                                        //表示没有更多数据了
                                        is_not_more = true;
                                        daEntities.addAll(result.getData().getData());
                                        userAnswerAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        daEntities.addAll(result.getData().getData());
                                        userAnswerAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    daEntities = result.getData().getData();
                                    set_list_resource(daEntities);
                                }
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "      回答报错       " + error);
                                if (daEntities.size() ==0 &&error.contains("无") || "断网".equals(error)) {
                                    image_empty.setImageResource(R.drawable.huida_empty);
                                    tv_empty.setText("还没有创作问题呦～");
                                    empty_layout.setVisibility(View.VISIBLE);
                                }
                            }
                        }, getActivity(), is_show_dialog));
    }

    private void set_list_resource(final List<HuiDaEntity> dates) {
        //设置上拉刷新下拉加载
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemAnimator(null);
        layoutManager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(layoutManager);
        userAnswerAdapter = new UserAnswerAdapter(getActivity());
        userAnswerAdapter.setDaEntities(daEntities);
        recycler_view.setAdapter(userAnswerAdapter);
        userAnswerAdapter.notifyDataSetChanged();
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


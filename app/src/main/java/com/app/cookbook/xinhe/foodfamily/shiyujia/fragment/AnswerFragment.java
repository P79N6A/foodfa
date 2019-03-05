package com.app.cookbook.xinhe.foodfamily.shiyujia.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageEvent;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.NewAnswerAdapter;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.NewAnswer;
import com.app.cookbook.xinhe.foodfamily.util.ui.ViewPagerFragment;
import com.tencent.stat.StatService;

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
import rx.Subscription;

public class AnswerFragment extends ViewPagerFragment {

    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    private NewAnswerAdapter newIssueAdapter;
    private LinearLayoutManager layoutManager;
    private List<NewAnswer.DataBean> searchEns = new ArrayList<>();
    /**
     * Rxjava
     */
    protected Subscription subscription;
    private int PAGE = 1;
    private boolean is_not_more;

    private View view;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            NewAnswer.DataBean dataBean = searchEns.get(msg.arg1);
            switch (msg.what) {
                case 400:
                    quxiao_guanzhu_question(dataBean.getId());
                    break;
                case 401:
                    guanzhu_question(dataBean.getId());
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_hot, container, false);
            ButterKnife.bind(this, view);//绑定黄牛刀
            EventBus.getDefault().register(this);
            search_net_list(true);
        } else {
            if (searchEns.size() > 0) {
                searchEns.clear();
            }
            search_net_list(true);
        }
        return view;
    }

    private int listSize;

    @Override
    public void onResume() {
        super.onResume();
        Properties prop = new Properties();
        prop.setProperty("jingxuan_time", "精选页面停留时长");
        StatService.trackCustomBeginKVEvent(getActivity(), "Selected_time", prop);
    }

    @Override
    public void onPause() {
        super.onPause();
        Properties prop = new Properties();
        prop.setProperty("jingxuan_time", "精选页面停留时长");
        StatService.trackCustomEndKVEvent(getActivity(), "Selected_time", prop);

    }

    private String question_id = "";

    //获取最新答案
    private void search_net_list(boolean is_show_dialog) {
        Log.e("123", "    最新答案    " + PAGE);
        subscription = Network.getInstance("最新答案", getActivity())
                .get_answer_list(String.valueOf(PAGE),
                        new ProgressSubscriberNew<>(NewAnswer.class, new GsonSubscriberOnNextListener<NewAnswer>() {
                            @Override
                            public void on_post_entity(NewAnswer result) {
                                listSize = result.getData().size();
                                Log.e("123", "    最新答案    " + result.getData());
                                if (searchEns.size() > 0) {//这表示是"加载"
                                    if (question_id.equals("")) {//表示刷新
                                        searchEns.clear();
                                    }

                                    if (result.getData().size() == 0) {
                                        is_not_more = true;
                                        searchEns.addAll(result.getData());
                                        newIssueAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        searchEns.addAll(result.getData());
                                        newIssueAdapter.notifyDataSetChanged();
                                        question_id = searchEns.get(searchEns.size() - 1).getId() + "";
                                    }
                                } else {
                                    searchEns = result.getData();
                                    init_new_source(result.getData());
                                    question_id = searchEns.get(searchEns.size() - 1).getId() + "";
                                }
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                if (error.equals("无数据")) {
                                    Log.e("123", "暂时没有数据！");
                                } else {
                                    Log.e("123", "获取最新内容报错：" + error);
                                    if (searchEns.size() == 0) {
                                        search_net_list(false);
                                    }
                                }
                                xrefreshview.stopRefresh();//刷新停止
                            }
                        }, getActivity(), is_show_dialog));
    }

    //关注问题
    private void guanzhu_question(String question_id) {
        Map<String, String> params = new HashMap<>();
        params.put("question_id", question_id);
        Log.e("关注问题：", params.toString());
        subscription = Network.getInstance("关注问题", getActivity())
                .guanzhu_question(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "关注问题成功：" + result.getCode());
                            }

                            @Override
                            public void onError(String error) {

                            }
                        }, getActivity(), false));
    }

    //取消关注问题
    private void quxiao_guanzhu_question(String question_id) {
        Map<String, String> params = new HashMap<>();
        params.put("question_id", question_id);
        Log.e("取消关注问题：", params.toString());
        subscription = Network.getInstance("取消关注问题", getActivity())
                .quxiao_question(question_id,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "取消关注问题成功：" + result.getCode());
                            }

                            @Override
                            public void onError(String error) {
                            }
                        }, getActivity(), false));

    }

    private int size = 0;

    private void init_new_source(List<NewAnswer.DataBean> searchEns) {
        //设置上拉刷新下拉加载
        recycler_view.setHasFixedSize(true);
        ((DefaultItemAnimator) recycler_view.getItemAnimator()).setSupportsChangeAnimations(false);

        layoutManager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(layoutManager);

        newIssueAdapter = new NewAnswerAdapter(searchEns, getActivity(), true, handler);
        recycler_view.setAdapter(newIssueAdapter);
        if (question_id.length() > 0) {
            if (listSize >= 1) {
                size = (searchEns.size() - (listSize - 1));
            }
            recycler_view.scrollToPosition(searchEns.size() - size);
        }
        newIssueAdapter.notifyDataSetChanged();

        // 静默加载模式不能设置footerview
        // 设置静默加载模式
        xrefreshview.setSilenceLoadMore(true);
        //设置刷新完成以后，headerview固定的时间
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);
        xrefreshview.setPullRefreshEnable(true);
        xrefreshview.setPullLoadEnable(true);
        xrefreshview.setAutoLoadMore(false);
        //给recycler_view设置底部加载布局
        xrefreshview.enableRecyclerViewPullUp(true);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
        //设置静默加载时提前加载的item个数
        xrefreshview.setPreLoadCount(10);
        if (searchEns.size() > 7) {
            xrefreshview.enableReleaseToLoadMore(true);
//            jingXuanAdapter.setCustomLoadMoreView(new XRefreshViewFooter(getActivity()));//加载更多
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
                        question_id = "";
                        PAGE = 1;
                        //填写刷新数据的网络请求，一般page=1，List集合清空操作
                        search_net_list(false);
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
                        search_net_list(false);
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

    //订阅方法，当接收到事件的时候，会调用该方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        switch (messageEvent.getMessage()) {
            case "1":
                search_net_list(false);
                break;
        }
    }


    @Override
    public void fetchData() {

    }
}
package com.app.cookbook.xinhe.foodfamily.main.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.adapter.SearchAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageEvent;
import com.app.cookbook.xinhe.foodfamily.main.entity.SearchDate;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.ui.ViewPagerFragment;
import com.tencent.stat.StatService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;


public class HotFragment extends ViewPagerFragment {

    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    private SearchAdapter jingXuanAdapter;
    private LinearLayoutManager layoutManager;
    private List<SearchDate.DataBean> searchEns = new ArrayList<>();
    /**
     * Rxjava
     */
    protected Subscription subscription;
    private int PAGE = 1;
    private boolean is_not_more;

    public static HotFragment newInstance(String title) {
        HotFragment fragment = new HotFragment();
        Bundle args = new Bundle();
        args.putString("home_fragment_title", title);
        fragment.setArguments(args);
        return fragment;
    }

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_hot, container, false);
            ButterKnife.bind(this, view);//绑定黄牛刀
            EventBus.getDefault().register(this);
            initJingXuanDate(false);
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

    private void initJingXuanDate(boolean is_show_dialog) {
//        Log.e("123", "     Hot  PAGE        " + PAGE);
        subscription = Network.getInstance("获取精选列表", getActivity())
                .get_jingxuan_liebiao(String.valueOf(PAGE),
                        new ProgressSubscriberNew<>(SearchDate.class, new GsonSubscriberOnNextListener<SearchDate>() {
                            @Override
                            public void on_post_entity(SearchDate result) {
                                listSize = result.getData().size();
//                                Log.e("123", "     获取精选列表成功      ");
                                if (searchEns.size() > 0) {//这表示是"加载"
                                    if (question_id.equals("")) {//表示刷新
                                        searchEns.clear();
                                    }
                                    if (result.getData().size() == 0) {
                                        is_not_more = true;
                                        searchEns.addAll(result.getData());
                                        jingXuanAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        searchEns.addAll(result.getData());
                                        jingXuanAdapter.notifyDataSetChanged();
                                        question_id = searchEns.get(searchEns.size() - 1).getQuestion_id() + "";
                                    }
                                } else {
                                    searchEns = result.getData();
                                    init_new_source(result.getData());
                                    question_id = searchEns.get(searchEns.size() - 1).getQuestion_id() + "";
                                }
                                setGestureListener();
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
                                    Log.e("123", "获取精选列表报错：" + error);
//                                    if ("timeout".endsWith(error) || "SSL handshake timed out".endsWith(error)) {
                                    if (searchEns.size() == 0) {
                                        initJingXuanDate(false);
                                    }
//                                    }
                                }
                                xrefreshview.stopRefresh();//刷新停止
                            }
                        }, getActivity(), is_show_dialog));
    }

    private int size = 0;

    private void init_new_source(List<SearchDate.DataBean> searchEns) {
        //设置上拉刷新下拉加载
        recycler_view.setHasFixedSize(true);
        ((DefaultItemAnimator) recycler_view.getItemAnimator()).setSupportsChangeAnimations(false);

        layoutManager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(layoutManager);

        jingXuanAdapter = new SearchAdapter(searchEns, getActivity(), true);
        recycler_view.setAdapter(jingXuanAdapter);
        if (question_id.length() > 0) {
            if (listSize >= 1) {
                size = (searchEns.size() - (listSize - 1));
            }
            recycler_view.scrollToPosition(searchEns.size() - size);
        }
        jingXuanAdapter.notifyDataSetChanged();

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
                        initJingXuanDate(false);
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
                        initJingXuanDate(true);
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

    /**
     * 设置上下滑动作监听器
     *
     * @author jczmdeveloper
     */
    List<String> list = new ArrayList<>();
    private int y;
    int itemNum = 0;

    private void setGestureListener() {
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        if (firstVisibleItemPosition < 8) {
            Contacts.hotType = "101";
        } else {
            Contacts.hotType = "102";
        }

        recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case 0:
                        if (!String.valueOf(y).contains("-") && !"0".equals(String.valueOf(y))) {
                            EventBus.getDefault().post(new MessageEvent("400"));
                        } else {
                            if (String.valueOf(y).contains("-")) {
                                EventBus.getDefault().post(new MessageEvent("401"));
                            }
                        }
                        break;
                    case 1:
                        if (!String.valueOf(y).contains("-") && !"0".equals(String.valueOf(y))) {
                            EventBus.getDefault().post(new MessageEvent("400"));
                        } else {
                            if (String.valueOf(y).contains("-")) {
                                EventBus.getDefault().post(new MessageEvent("401"));
                            }
                        }
                        break;
                    case 2:
                        if (!String.valueOf(y).contains("-") && !"0".equals(String.valueOf(y))) {
                            EventBus.getDefault().post(new MessageEvent("400"));
                        } else {
                            if (String.valueOf(y).contains("-")) {
                                EventBus.getDefault().post(new MessageEvent("401"));
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                y = dy;
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition <= 5) {
                    Contacts.hotType = "101";
                    EventBus.getDefault().post(new MessageEvent("101"));
                } else if (firstVisibleItemPosition >= 8) {
                    if (!String.valueOf(y).contains("-")) {
                        itemNum = firstVisibleItemPosition;
                        Contacts.hotType = "101";
                        EventBus.getDefault().post(new MessageEvent("101"));
                    } else {
                        if (itemNum - firstVisibleItemPosition >= 3) {
                            Contacts.hotType = "102";
                            EventBus.getDefault().post(new MessageEvent("102"));
                        }
                    }
                }
            }
        });
    }


    //订阅方法，当接收到事件的时候，会调用该方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        switch (messageEvent.getMessage()) {
            case "301":
                recycler_view.scrollToPosition(0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PAGE = PAGE + 1;
                        //填写刷新数据的网络请求，一般page=1，List集合清空操作
                        initJingXuanDate(false);
                        xrefreshview.stopRefresh();//刷新停止
                    }
                }, 1000);//2000是刷新的延时，使得有个动画效果
                break;
            case "600":
                if (searchEns.size() == 0) {
                    initJingXuanDate(false);
                }
                break;
        }
    }


    @Override
    public void fetchData() {

    }
}

package com.app.cookbook.xinhe.foodfamily.main.fragment;

/**
 * 关注页面
 */

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.callback.ImageBiggerCallBack;
import com.app.cookbook.xinhe.foodfamily.main.adapter.AttentionAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.AttentionList;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageEvent;
import com.app.cookbook.xinhe.foodfamily.main.entity.SuggestedUsers;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
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


public class AttentionFragment extends ViewPagerFragment {

    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    //空数据布局
    @BindView(R.id.empty_layout)
    RelativeLayout empty_layout;
    //关注问题，答案列表
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    private AttentionAdapter attentionAdapter;
    private LinearLayoutManager layoutManager;
    private List<AttentionList.DataBean> searchEns = new ArrayList<>();
    //推荐值得关注的人
    private List<SuggestedUsers.DataBean> suggestedUsers = new ArrayList<>();

    /**
     * Rxjava
     */
    protected Subscription subscription;
    private int PAGE = 1;
    private boolean is_not_more;


    public static AttentionFragment newInstance(String title) {
        AttentionFragment fragment = new AttentionFragment();
        Bundle args = new Bundle();
        args.putString("home_fragment_title", title);
        fragment.setArguments(args);
        return fragment;
    }
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_attention, container, false);
            ButterKnife.bind(this, view);//绑定黄牛刀
            EventBus.getDefault().register(this);
//            user_ranking_question();
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
        if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
            if (searchEns.size() > 0) {
                searchEns.clear();
            }
            if (suggestedUsers.size() > 0) {
                suggestedUsers.clear();
            }
            recycler_view.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
        } else {
            recycler_view.setVisibility(View.VISIBLE);
            empty_layout.setVisibility(View.GONE);
            if (suggestedUsers.size() == 0) {
                user_ranking_question();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Properties prop = new Properties();
        prop.setProperty("jingxuan_time", "精选页面停留时长");
        StatService.trackCustomEndKVEvent(getActivity(), "Selected_time", prop);
    }

    /**
     * 值得关注的用户
     */
    private void user_ranking_question() {
        Map<String, String> params = new HashMap<>();
        params.put("sort", "");
        Log.e("123", "        " + params);
        subscription = Network.getInstance("值得关注的用户", getActivity())
                .follow_users_request(params,
                        new ProgressSubscriberNew<>(SuggestedUsers.class, new GsonSubscriberOnNextListener<SuggestedUsers>() {
                            @Override
                            public void on_post_entity(SuggestedUsers result) {
                                Log.e("123", "值得关注的人：" + result.getData().size());
                                if (suggestedUsers.size() > 0) {
                                    suggestedUsers.clear();
                                }
                                suggestedUsers.addAll(result.getData());
                                initGuanzhuDate(false);
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "值得关注的用户：error   " + error);
                            }
                        }, getActivity(), false));
    }

    private void initGuanzhuDate(boolean is_show_dialog) {
//        Log.e("123", "         Attention             " + PAGE);
        subscription = Network.getInstance("获取关注列表", getActivity())
                .get_attention_home_liebiao(String.valueOf(PAGE),
                        new ProgressSubscriberNew<>(AttentionList.class, new GsonSubscriberOnNextListener<AttentionList>() {
                            @Override
                            public void on_post_entity(AttentionList result) {
                                Log.e("123", "          获取关注列表成功             ");
                                listSize = result.getData().size();
                                if (PAGE == 1) {
                                    if (searchEns.size() > 0) {
                                        searchEns.clear();
                                    }
                                }
                                if (searchEns.size() > 0 && PAGE > 1) {//这表示是"加载"
                                    if (result.getData().size() == 0) {
                                        //表示没有更多数据了
                                        is_not_more = true;
                                        searchEns.addAll(result.getData());
                                        attentionAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        searchEns.addAll(result.getData());
                                        attentionAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    searchEns = result.getData();
                                    init_new_source(result.getData());
                                }
                                setGestureListener();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "        error    " + error);
                                if (error.equals("无数据") || error.equals("无关注数据")) {
//                                    PAGE = PAGE - 1;
                                    if (PAGE == 1) {
                                        if (searchEns.size() > 0) {
                                            searchEns.clear();
                                        }
                                        init_new_source(searchEns);
                                    }
                                } else {
//                                    if ("timeout".endsWith(error) || "SSL handshake timed out".endsWith(error)) {
                                    if (searchEns.size() == 0) {
                                        initGuanzhuDate(false);
                                    }
//                                    }
                                    Log.e("123", "获取关注列表报错：" + error);
                                }
                                xrefreshview.stopRefresh();//刷新停止
                            }
                        }, getActivity(), is_show_dialog));
    }

    private int size = 0;

    private void init_new_source(List<AttentionList.DataBean> searchEnss) {
        //设置上拉刷新下拉加载
        recycler_view.setHasFixedSize(true);
        ((DefaultItemAnimator) recycler_view.getItemAnimator()).setSupportsChangeAnimations(false);
        layoutManager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(layoutManager);

        attentionAdapter = new AttentionAdapter(searchEnss, getActivity(), new ImageBiggerCallBack() {
            @Override
            public void image_url(View view, String url) {
            }
        });
        attentionAdapter.setSuggestedUsers(suggestedUsers);

        recycler_view.setAdapter(attentionAdapter);
        if (PAGE > 1) {
            if (listSize >= 1) {
                size = (searchEnss.size() - (listSize - 1));
            }
            recycler_view.scrollToPosition(searchEnss.size() - size);
        }
        attentionAdapter.notifyDataSetChanged();

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
        if (searchEnss.size() > 7) {
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
                        PAGE = 1;
                        //填写刷新数据的网络请求，一般page=1，List集合清空操作
                        user_ranking_question();
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
                        initGuanzhuDate(false);
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
            Contacts.attentionType = "105";
        } else {
            Contacts.attentionType = "106";
        }

        recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case 0:
                        if (!String.valueOf(y).contains("-") && !"0".equals(String.valueOf(y))) {
                            EventBus.getDefault().post(new MessageEvent("404"));
                        } else {
                            if (String.valueOf(y).contains("-")) {
                                EventBus.getDefault().post(new MessageEvent("405"));
                            }
                        }
                        break;
                    case 1:
                        if (!String.valueOf(y).contains("-") && !"0".equals(String.valueOf(y))) {
                            EventBus.getDefault().post(new MessageEvent("404"));
                        } else {
                            if (String.valueOf(y).contains("-")) {
                                EventBus.getDefault().post(new MessageEvent("405"));
                            }
                        }
                        break;
                    case 2:
                        if (!String.valueOf(y).contains("-") && !"0".equals(String.valueOf(y))) {
                            EventBus.getDefault().post(new MessageEvent("404"));
                        } else {
                            if (String.valueOf(y).contains("-")) {
                                EventBus.getDefault().post(new MessageEvent("405"));
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
                    Contacts.attentionType = "105";
                    EventBus.getDefault().post(new MessageEvent("105"));
                } else if (firstVisibleItemPosition >= 8) {
                    if (!String.valueOf(y).contains("-")) {
                        itemNum = firstVisibleItemPosition;
                        Contacts.attentionType = "105";
                        EventBus.getDefault().post(new MessageEvent("105"));
                    } else {
                        if (itemNum - firstVisibleItemPosition >= 3) {
                            Contacts.attentionType = "106";
                            EventBus.getDefault().post(new MessageEvent("106"));
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
            case "303":
                recycler_view.scrollToPosition(0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PAGE = PAGE + 1;
                        //填写刷新数据的网络请求，一般page=1，List集合清空操作
//                        initGuanzhuDate(false);
//                        xrefreshview.stopRefresh();//刷新停止
                        user_ranking_question();
                    }
                }, 1000);//2000是刷新的延时，使得有个动画效果
                break;
        }
    }


    @Override
    public void fetchData() {

    }


}


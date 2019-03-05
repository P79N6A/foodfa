package com.app.cookbook.xinhe.foodfamily.main.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.adapter.FoundAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.FoundDate;
import com.app.cookbook.xinhe.foodfamily.main.entity.FoundEntity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.util.ui.ViewPagerFragment;
import com.tencent.stat.StatService;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;


public class FoundFragment extends ViewPagerFragment {
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    private FoundAdapter jingXuanAdapter;
    LinearLayoutManager layoutManager;
    @BindView(R.id.back_layout_btn)
    RelativeLayout back_layout_btn;
    @BindView(R.id.title_two)
    TextView title_two;
    private int PAGE = 1;
    private boolean is_not_more;
    List<FoundEntity> daEntities = new ArrayList<>();
    /**
     * Rxjava
     */
    protected Subscription subscription;
    View view;

    public static FoundFragment newInstance(String title) {
        FoundFragment fragment = new FoundFragment();
        Bundle args = new Bundle();
        args.putString("home_fragment_title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_faxianmeihao, null);
            ButterKnife.bind(this, view);//绑定黄牛刀
            setGestureListener();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Properties prop = new Properties();
        prop.setProperty("found_ime", "发现页面停留时长");
        StatService.trackCustomBeginKVEvent(getActivity(), "Found_time", prop);

        get_net_resources(true);

    }

    @Override
    public void onPause() {
        super.onPause();
        Properties prop = new Properties();
        prop.setProperty("found_ime", "发现页面停留时长");
        StatService.trackCustomEndKVEvent(getActivity(), "Found_time", prop);
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
                            if (getScollYDistance() >= 260) {
                                back_layout_btn.setVisibility(View.VISIBLE);
                                title_two.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (firstVisibleItem == 0) {
                                back_layout_btn.setVisibility(View.GONE);
                                title_two.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case 1:
                        if (!String.valueOf(y).contains("-") && !"0".equals(String.valueOf(y))) {
                            if (getScollYDistance() >= 260) {
                                back_layout_btn.setVisibility(View.VISIBLE);
                                title_two.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (firstVisibleItem == 0) {
                                back_layout_btn.setVisibility(View.GONE);
                                title_two.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case 2:
                        if (!String.valueOf(y).contains("-") && !"0".equals(String.valueOf(y))) {
                            if (getScollYDistance() >= 260) {
                                back_layout_btn.setVisibility(View.VISIBLE);
                                title_two.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (firstVisibleItem == 0) {
                                back_layout_btn.setVisibility(View.GONE);
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
    private int listSize;

    //发现请求
    private void get_net_resources(boolean is_show_dialog) {
        subscription = Network.getInstance("发现美好", getActivity())
                .found_beauty_list(String.valueOf(PAGE),
                        new ProgressSubscriberNew<>(FoundDate.class, new GsonSubscriberOnNextListener<FoundDate>() {
                            @Override
                            public void on_post_entity(FoundDate result) {
                                listSize = result.getData().size();
                                if (daEntities.size() > 0 && PAGE > 1) {//这表示是"加载"
                                    if (result.getData().size() == 0) {
                                        //表示没有更多数据了
                                        is_not_more = true;
                                        daEntities.addAll(result.getData());
                                        jingXuanAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        daEntities.addAll(result.getData());
                                        jingXuanAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    daEntities = result.getData();
                                    set_list_resource(result.getData());
                                }
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "获取发现美好报错：" + error);
                            }
                        }, getActivity(), is_show_dialog));
    }

    private int size = 0;

    private void set_list_resource(List<FoundEntity> daEntities) {
        //设置上拉刷新下拉加载
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemAnimator(null);
        layoutManager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(layoutManager);

        jingXuanAdapter = new FoundAdapter(daEntities, getActivity());
        jingXuanAdapter.setmOnItemClickListener(new FoundAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });

        recycler_view.setAdapter(jingXuanAdapter);
        if (PAGE > 1) {
            if (daEntities.size() >= 1) {
                size = (daEntities.size() - (listSize - 1));
            }
            recycler_view.scrollToPosition(daEntities.size() - size);
        }
        jingXuanAdapter.notifyDataSetChanged();
        // 静默加载模式不能设置footerview
        xrefreshview.setSilenceLoadMore(true);
        // 设置静默加载模式
        // xRefreshView1.setSilenceLoadMore();
        //设置刷新完成以后，headerview固定的时间
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);
        xrefreshview.setPullRefreshEnable(true);
        xrefreshview.setPullLoadEnable(true);
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.enableRecyclerViewPullUp(true);
        //设置静默加载时提前加载的item个数
        xrefreshview.setPreLoadCount(10);
        //给recycler_view设置底部加载布局
//        jingXuanAdapter.setCustomLoadMoreView(new XRefreshViewFooter(getActivity()));//加载更多
        xrefreshview.enableReleaseToLoadMore(true);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
        xrefreshview.setLoadComplete(false);//隐藏底部
        if (daEntities.size() > 7) {
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
                        get_net_resources(false);
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
                        get_net_resources(false);

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
    public void fetchData() {

    }
}

package com.app.cookbook.xinhe.foodfamily.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.MainSearchActivity;
import com.app.cookbook.xinhe.foodfamily.main.OtherUserActivity;
import com.app.cookbook.xinhe.foodfamily.main.adapter.SearchTagsAdapter;
import com.app.cookbook.xinhe.foodfamily.main.adapter.SearchUserAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.SearchUser;
import com.app.cookbook.xinhe.foodfamily.main.entity.SearchUserItem;
import com.app.cookbook.xinhe.foodfamily.main.entity.TagEntity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.LabelDetailsActivity;
import com.app.cookbook.xinhe.foodfamily.util.ui.ViewPagerFragment;
import com.tencent.stat.StatService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import rx.Subscription;

/**
 * Created by 18030150 on 2018/3/20.
 */

public class SearchTagFragment extends ViewPagerFragment {

    private View roomView;
    XRefreshView xrefreshview;
    private RelativeLayout image_layout;
    private ImageView image_null;
    private TextView tv_null;
    //列表控件
    private RecyclerView recycler_view;
    //布局控制器
    private LinearLayoutManager layoutManager;
    /**
     * Rxjava
     */
    protected Subscription subscription;
    //页数
    private int PAGE = 1;
    //是否有更多
    private boolean is_not_more;
    //搜索key
    private String searchkey;
    private SearchTagsAdapter searchUserAdapter;
    private List<TagEntity.DataBean> searchItems = new ArrayList<>();
    private List<String> participle = new ArrayList<>();

    public static SearchTagFragment newInstance(String title) {
        SearchTagFragment fragment = new SearchTagFragment();
        Bundle args = new Bundle();
        args.putString("home_fragment_title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (roomView == null) {
            roomView = inflater.inflate(R.layout.search_tag_fragment, null);
            initView();
        }
        if (TextUtils.isEmpty(searchkey)) {
            searchkey = MainSearchActivity.keyword;
            init_net_issue(true);
        } else {
            if (!searchkey.equals(MainSearchActivity.keyword)) {
                searchkey = MainSearchActivity.keyword;
                if (searchItems.size() > 0) {
                    searchItems.clear();
                }
                PAGE = 1;
                init_net_issue(true);
            }
        }
        return roomView;
    }

    private void initView() {
        xrefreshview = roomView.findViewById(R.id.xrefreshview);
        recycler_view = roomView.findViewById(R.id.recycler_view);
        image_layout = roomView.findViewById(R.id.image_layout);
        image_null = roomView.findViewById(R.id.image_null);
        tv_null = roomView.findViewById(R.id.tv_null);
    }

    private void setOnItemListener() {
        searchUserAdapter.setOnItemClickListener(new SearchTagsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //跳转到标签详情
                Intent intent = new Intent(getActivity(), LabelDetailsActivity.class);
                intent.putExtra("id", searchItems.get(position).getId());
                startActivity(intent);

            }
        });
    }


    private void init_net_issue(boolean is_show_dialog) {
        final Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(PAGE));
        params.put("keyword", MainSearchActivity.keyword);
        Log.e("获取标签参数", params.toString());
        subscription = Network.getInstance("获取标签数据", getActivity())
                .search_biaoqian_resources(params,
                        new ProgressSubscriberNew<>(TagEntity.class, new GsonSubscriberOnNextListener<TagEntity>() {
                            @Override
                            public void on_post_entity(TagEntity result) {
                                Log.e("123", "      获取用户数据         ");
                                image_layout.setVisibility(View.GONE);
                                xrefreshview.setVisibility(View.VISIBLE);

                                Log.e("123", "            " + searchItems.size());
                                if (searchItems.size() > 0 && PAGE > 0) {//这表示是"加载"
                                    if (result.getData().size() == 0) {
                                        //表示没有更多数据了
                                        is_not_more = true;
                                        searchItems.addAll(result.getData());
                                        participle.addAll(result.getParticiple());
                                        searchUserAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        searchItems.addAll(result.getData());
                                        participle.addAll(result.getParticiple());
                                        searchUserAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    searchItems = result.getData();
                                    participle = result.getParticiple();
                                    set_list_resource(result.getData());
                                }
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Log.e("错误:", error);
                                if (searchItems.size() == 0) {
                                    xrefreshview.setVisibility(View.GONE);
                                    image_null.setImageResource(R.drawable.biaoqian_empty);
                                    tv_null.setText("未找到相关标签");
                                    image_layout.setVisibility(View.VISIBLE);
                                    set_list_resource(searchItems);
                                }
                            }
                        }, getActivity()));
    }


    private boolean ista = false;

    private void set_list_resource(final List<TagEntity.DataBean> dates) {

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setHasFixedSize(true);

        searchUserAdapter = new SearchTagsAdapter(getActivity(), ista);
        searchUserAdapter.setSearchIssues(searchItems);
        searchUserAdapter.setParticiple(participle);
        recycler_view.setAdapter(searchUserAdapter);
        searchUserAdapter.notifyDataSetChanged();
        setOnItemListener();

        //设置上拉刷新下拉加载
//        recycler_view.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(getActivity());
//        recycler_view.setLayoutManager(layoutManager);
//        searchUserAdapter.setSearchIssues(searchItems);
//        recycler_view.setAdapter(searchUserAdapter);
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
        //设置静默加载时提前加载的item个数
        xrefreshview.setPreLoadCount(10);
        if (dates.size() > 7) {
            xrefreshview.enableReleaseToLoadMore(true);
//            searchUserAdapter.setCustomLoadMoreView(new XRefreshViewFooter(getActivity()));//加载更多
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
                        PAGE = 0;
                        //填写刷新数据的网络请求，一般page=1，List集合清空操作
                        if (searchItems.size() > 0) {
                            searchItems.clear();
                        }
                        init_net_issue(false);
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
                        init_net_issue(false);

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

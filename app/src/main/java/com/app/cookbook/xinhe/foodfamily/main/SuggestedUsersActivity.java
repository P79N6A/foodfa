package com.app.cookbook.xinhe.foodfamily.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.main.adapter.SuggestedUserListAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.SuggestedUsers;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import chuangyuan.ycj.videolibrary.video.ExoUserPlayer;
import chuangyuan.ycj.videolibrary.video.VideoPlayerManager;

public class SuggestedUsersActivity extends BaseActivity {
    //返回
    @BindView(R.id.iamge_back)
    ImageView iamge_back;
    //返回
    @BindView(R.id.back_layout)
    LinearLayout back_layout;
    //标题
    @BindView(R.id.title_two)
    TextView title_two;
    //列表
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    //刷新加载
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    private SuggestedUserListAdapter suggestedUserListAdapter;
    private LinearLayoutManager layoutManager;

    private int PAGE = 1;
    private boolean is_not_more;
    private List<SuggestedUsers.DataBean> dates = new ArrayList<>();
    private String flag = "";

    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {
        if (parms != null) {
            flag = parms.getString("flag");
        }
    }

    @Override
    public void initView() {
        setContentLayout(R.layout.activity_suggested_users);
        //初始化黄牛刀
        ButterKnife.bind(this);
        isShowTitle(false);
        user_ranking_question(true);
        setGestureListener();
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getOnItemClick() {
        suggestedUserListAdapter.setmOnItemClickListener(new SuggestedUserListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SuggestedUsersActivity.this, FriendPageActivity.class);
                intent.putExtra("UserId", dates.get(position).getUser_data().getId());
                startActivity(intent);
            }
        });
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

    private String titleName;

    @Override
    public void doBusiness(Context mContext) {
        title_two.setText("值得关注的人");
        titleName = "值得关注的人";
    }

    /**
     * 值得关注的用户
     */
    private String sort;
    private boolean isLoad = false;

    private void user_ranking_question(boolean is_show_dialog) {
        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(PAGE));
        if (!TextUtils.isEmpty(sort)) {
            params.put("sort", sort);
        }
        Log.e("123", "   值得关注的用户     " + params);
        subscription = Network.getInstance("值得关注的用户", this)
                .follow_users_request(params,
                        new ProgressSubscriberNew<>(SuggestedUsers.class, new GsonSubscriberOnNextListener<SuggestedUsers>() {
                            @Override
                            public void on_post_entity(SuggestedUsers result) {
                                Log.e("123", "值得關注的人：" + result.getData().get(0).getUser_id());
                                if (isLoad == true) {
                                    if (result.getData().size() > 0) {
                                        sort = result.getData().get(result.getData().size() - 1).getSort();
                                    }
                                    isLoad = false;
                                }
                                if (dates.size() > 0 && PAGE > 1) {//这表示是"加载"
                                    if (result.getData().size() == 0) {
                                        //表示没有更多数据了
                                        is_not_more = true;
                                        dates.addAll(result.getData());
                                        suggestedUserListAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        dates.addAll(result.getData());
                                        suggestedUserListAdapter.notifyDataSetChanged();
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
                                Log.e("123", "值得关注的用户：" + error);
                                is_not_more = true;
                                if (error.equals("无数据")) {
                                    Log.e("123", "暂时没有数据！");
                                } else {
                                    Log.e("123", "获取系统消息报错：" + error);
                                }
                                if (dates.size() == 0) {
                                    set_list_resource(dates);
                                }
                            }
                        }, this, is_show_dialog));
    }

    private void set_list_resource(final List<SuggestedUsers.DataBean> dates) {
        //设置上拉刷新下拉加载
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemAnimator(null);
        layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        suggestedUserListAdapter = new SuggestedUserListAdapter(dates, this);
        suggestedUserListAdapter.setTitleName(titleName);
        recycler_view.setAdapter(suggestedUserListAdapter);
        getOnItemClick();

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
                        sort = "";
                        user_ranking_question(false);
                        xrefreshview.stopRefresh();//刷新停止

                    }
                }, 1000);//2000是刷新的延时，使得有个动画效果
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        PAGE = PAGE + 1;
                        isLoad = true;
                        //填写加载更多的网络请求，一般page++
                        user_ranking_question(false);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

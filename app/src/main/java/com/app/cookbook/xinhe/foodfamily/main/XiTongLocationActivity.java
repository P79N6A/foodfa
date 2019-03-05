package com.app.cookbook.xinhe.foodfamily.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.callback.DialogCallBack;
import com.app.cookbook.xinhe.foodfamily.main.adapter.XiTongAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.LocationDate;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.util.PopWindowHelper;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XiTongLocationActivity extends BaseActivity {
    @BindView(R.id.iamge_back)
    ImageView iamge_back;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.back_layout)
    LinearLayout back_layout;
    @BindView(R.id.title_two)
    TextView title_two;
    private XiTongAdapter guanZhuWoRenAdapter;
    private boolean is_not_more;
    private LinearLayoutManager layoutManager;
    private List<LocationDate.DataBean> dates = new ArrayList<>();
    private String message_id = "";
    private int PAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public void initView() {
        setContentLayout(R.layout.activity_xi_tong_location);
        //初始化黄牛刀
        ButterKnife.bind(this);
        isShowTitle(false);
        setGestureListener();
        init_net_source(true);
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

    @Override
    public void doBusiness(Context mContext) {
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //根据推送过来的逻辑，跳转FoodFamily再进消息首页
                finish();
            }
        });
    }

    private void in() {
        Intent intent = new Intent(XiTongLocationActivity.this, FoodFamilyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("index_page", "xiaoxi3");
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.finish_right, R.anim.finish_left);
    }

    private void out() {
        Intent intent = new Intent(XiTongLocationActivity.this, FoodFamilyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("index_page", "xiaoxi3");
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.finish_left, R.anim.finish_right);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //获取网络数据
    }

    private void init_net_source(boolean is_show_dialog) {
        Log.e("传过去的messID", "传过去的messID" + message_id);
        subscription = Network.getInstance("系统消息", this)
                .get_xitong_message(message_id,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<LocationDate>>() {
                            @Override
                            public void onNext(Bean<LocationDate> result) {
                                Log.e("123", "获取系统消息成功：" + result.getCode());
                                if (PAGE == 1) {
                                    if (dates.size() > 0) {
                                        dates.clear();
                                    }
                                }
                                if (dates.size() > 0) {//如果还有数据,表示加载
                                    if (result.getData().getData().size() == 0) {
                                        //表示没有更多数据了
                                        is_not_more = true;
                                        dates.addAll(result.getData().getData());
                                        guanZhuWoRenAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        dates.addAll(result.getData().getData());
                                        guanZhuWoRenAdapter.notifyDataSetChanged();
                                        message_id = dates.get(dates.size() - 1).getId() + "";
                                    }

                                } else {
                                    dates = result.getData().getData();
                                    set_list_resource();
                                    message_id = dates.get(dates.size() - 1).getId() + "";

                                }
                            }

                            @Override
                            public void onError(String error) {
                                is_not_more = true;
                                if (error.equals("无数据")) {
                                    Log.e("123", "暂时没有数据！");
                                } else {
                                    Log.e("123", "获取系统消息报错：" + error);
                                }
                                if (dates.size() == 0) {
                                    set_list_resource();
                                }
                            }
                        }, this, is_show_dialog));
    }


    private void jilu_xiaoxi_list(String xiaoxi_id) {
        subscription = Network.getInstance("标记阅读", this)
                .get_xitong_detail("4", xiaoxi_id + "",
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "标记阅读成功：" + result.getCode());

                            }

                            @Override
                            public void onError(String error) {
                                if (error.equals("无数据")) {
                                    Toast.makeText(getApplicationContext(), "暂时没有数据！", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e("123", "标记阅读报错：" + error);
                                }
                            }
                        }, this, false));
    }

    private void go_to_detail(String content) {
        Intent intent = new Intent(XiTongLocationActivity.this, LocationDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("detail_string", content);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void set_list_resource() {
        //设置上拉刷新下拉加载
        layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setItemAnimator(null);
        recycler_view.setHasFixedSize(true);
        guanZhuWoRenAdapter = new XiTongAdapter(dates, getApplicationContext());
        recycler_view.setAdapter(guanZhuWoRenAdapter);

        guanZhuWoRenAdapter.setOnItemClickListener(new XiTongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LocationDate.DataBean le = dates.get(position);
                //标记已经阅读过了
                jilu_xiaoxi_list(le.getId());
                switch (le.getStatus()) {
                    case "1"://消息推送首页
//                        go_to_detail(le.getContent());
                        break;
                    case "2"://答案禁用
                        Intent intent = new Intent(XiTongLocationActivity.this, AnserActivity.class);
                        Bundle bundle = new Bundle();
                        String str = le.getAnswer_id() + "";
//                        str = str.substring(0, str.length() - 2);
                        bundle.putString("answer_id", str);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case "3"://问题禁用
                        Intent intent2 = new Intent(XiTongLocationActivity.this, FenLeiQuestionDetailActivity.class);
                        Bundle bundle2 = new Bundle();
                        String str2 = le.getQuestion_id() + "";
//                        str2 = str2.substring(0, str2.length() - 2);
                        bundle2.putString("wenti_id", str2);
                        intent2.putExtras(bundle2);
                        startActivity(intent2);
                        break;
                    case "4"://邀请回答
                        Intent intent7 = new Intent(XiTongLocationActivity.this, FenLeiQuestionDetailActivity.class);
                        Bundle bundle7 = new Bundle();
                        String str3 = le.getQuestion_id() + "";
//                        str3 = str3.substring(0, str3.length() - 2);
                        bundle7.putString("wenti_id", str3);
                        intent7.putExtras(bundle7);
                        startActivity(intent7);
                        break;
                    case "5"://用户反馈
                        break;
                    case "6"://首次进入APP欢迎方案
                        break;
                    case "7"://修改手机号
                        break;
                    case "8"://修改密码
                        break;
                    case "9"://举报问题处理
                        break;
                    case "10"://被举报问题处理
//                        Intent intent3 = new Intent(XiTongLocationActivity.this, FenLeiQuestionDetailActivity.class);
//                        Bundle bundle3 = new Bundle();
//                        String str4 = le.getQuestion_id() + "";
////                        str4 = str4.substring(0, str4.length() - 2);
//                        bundle3.putString("wenti_id", str4);
//                        intent3.putExtras(bundle3);
//                        startActivity(intent3);
                        break;
                    case "11"://举报通知
                        break;
                    case "12"://官方答案修改
                        Intent intent6 = new Intent(XiTongLocationActivity.this, AnserActivity.class);
                        Bundle bundle6 = new Bundle();
                        String str5 = le.getAnswer_id() + "";
                        str5 = str5.substring(0, str5.length() - 2);
                        bundle6.putString("answer_id", str5);
                        intent6.putExtras(bundle6);
                        startActivity(intent6);
                        break;
                    case "13": //APP内消息推送
                        break;
                    case "14"://被举报答案反馈
//                        Intent intent4 = new Intent(XiTongLocationActivity.this, AnserActivity.class);
//                        Bundle bundle4 = new Bundle();
//                        String str6 = le.getAnswer_id() + "";
////                        str6 = str6.substring(0, str6.length() - 2);
//                        bundle4.putString("answer_id", str6);
//                        intent4.putExtras(bundle4);
//                        startActivity(intent4);
                        break;
                    case "15"://举报答案反馈
                        break;
                    case "16"://消息推送问题
//                        go_to_detail(le.getContent());
                        Intent intent16 = new Intent(XiTongLocationActivity.this, FenLeiQuestionDetailActivity.class);
                        Bundle bundle16 = new Bundle();
                        String str16 = le.getQuestion_id() + "";
//                        str16 = str16.substring(0, str16.length() - 2);
                        bundle16.putString("wenti_id", str16);
                        intent16.putExtras(bundle16);
                        startActivity(intent16);
                        break;
                    case "17"://消息推送外链地址
                        Intent intent10 = new Intent(XiTongLocationActivity.this, JiGuangNetActivity.class);
                        Bundle bundle10 = new Bundle();
                        if (null != le.getUrl()) {
                            bundle10.putString("net_url", le.getUrl());
                            intent10.putExtras(bundle10);
                            startActivity(intent10);
                        }
                        break;
                    case "18"://官方问题修改
                        Intent intent5 = new Intent(XiTongLocationActivity.this, FenLeiQuestionDetailActivity.class);
                        Bundle bundle5 = new Bundle();
                        String str7 = le.getQuestion_id() + "";
//                        str7 = str7.substring(0, str7.length() - 2);
                        bundle5.putString("wenti_id", str7);
                        intent5.putExtras(bundle5);
                        startActivity(intent5);
                        break;
                    case "19":

                        break;
                    case "20":

                        break;
                    case "21":

                        break;
                    case "22":

                        break;
                    case "23":

                        break;
                    //问题推送上精选
                    case "24":
                        Intent intent24 = new Intent(XiTongLocationActivity.this, FenLeiQuestionDetailActivity.class);
                        Bundle bundle24 = new Bundle();
                        String str24 = le.getQuestion_id() + "";
//                        str24 = str24.substring(0, str24.length() - 2);
                        bundle24.putString("wenti_id", str24);
                        intent24.putExtras(bundle24);
                        startActivity(intent24);
                        break;
                    //答案推送上精选
                    case "25":
                        Intent intent25 = new Intent(XiTongLocationActivity.this, AnserActivity.class);
                        Bundle bundle25 = new Bundle();
                        String str25 = le.getAnswer_id() + "";
//                        str25 = str25.substring(0, str25.length() - 2);
                        bundle25.putString("answer_id", str25);
                        intent25.putExtras(bundle25);
                        startActivity(intent25);
                        break;
                }
            }
        });

        guanZhuWoRenAdapter.setOnItemLongClickListener(new XiTongAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                PopWindowHelper.public_tishi_pop(XiTongLocationActivity.this, "食与家提示", "是否删除？", "取消", "确定", new DialogCallBack() {
                    @Override
                    public void save() {
                        delete_message(position);
                    }

                    @Override
                    public void cancel() {

                    }
                });
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
        if (dates.size() >= 9) {
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
                        message_id = "";
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

    private void delete_message(final int position) {
        Map<String, String> params = new HashMap<>();
        params.put("id", dates.get(position).getId() + "");
        subscription = Network.getInstance("删除系统消息", this)
                .delete_xitong_message(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "删除系统消息成功：" + result.getCode());
                                Toast.makeText(getApplicationContext(), "该内容已被删除", Toast.LENGTH_SHORT).show();

                                //删除第一页数据之后自动加载第二页数据
                                if (dates.size() <= 8) {
                                    dates.remove(position);
                                    init_net_source(false);
                                } else {
                                    dates.remove(position);
                                    guanZhuWoRenAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onError(String error) {
                                if (error.equals("无数据")) {
                                    Log.e("123", "暂时没有数据！");
                                } else {
                                    Log.e("123", "删除系统消息报错：" + error);
                                }
                            }
                        }, this, false));
    }


    @Override
    public void widgetClick(View v) {

    }
}

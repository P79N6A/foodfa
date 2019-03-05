package com.app.cookbook.xinhe.foodfamily.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.app.cookbook.xinhe.foodfamily.main.adapter.ZuiXinHuidaAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.ZuiZinAnswerEntity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
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

public class NewQuestionActivity extends BaseActivity {
    @BindView(R.id.iamge_back)
    ImageView iamge_back;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.back_layout)
    LinearLayout back_layout;
    @BindView(R.id.title_two)
    TextView title_two;
    private boolean is_not_more;

    private ZuiXinHuidaAdapter jingXuanAdapter;
    LinearLayoutManager layoutManager;
    private List<ZuiZinAnswerEntity.DataBean> shouCangEntities = new ArrayList<>();
    private String message_id = "";

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
        setContentLayout(R.layout.activity_new_question);
        //初始化黄牛刀
        ButterKnife.bind(this);
        isShowTitle(false);
        setGestureListener();
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
                finish();
            }
        });
        initNetDate(true);
    }

    private void initNetDate(boolean is_show_dialog) {
        subscription = Network.getInstance("最新回答", this)
                .get_zuixin_answer(message_id,
                        new ProgressSubscriberNew<>(ZuiZinAnswerEntity.class, new GsonSubscriberOnNextListener<ZuiZinAnswerEntity>() {
                            @Override
                            public void on_post_entity(ZuiZinAnswerEntity result) {
                                if (shouCangEntities.size() > 0) {//这表示是"加载"
                                    if (result.getData().size() == 0) {
                                        //表示没有更多数据了
                                        is_not_more = true;
                                        shouCangEntities.addAll(result.getData());
                                        jingXuanAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        shouCangEntities.addAll(result.getData());
                                        jingXuanAdapter.notifyDataSetChanged();
                                        message_id = shouCangEntities.get(shouCangEntities.size() - 1).getId() + "";
                                    }

                                } else {
                                    shouCangEntities = result.getData();
                                    set_date_view(result.getData());
                                    message_id = shouCangEntities.get(shouCangEntities.size() - 1).getId() + "";

                                }
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123","获取最新回答报错：" + error);
                                is_not_more = true;
                                if (error.equals("无数据")) {
                                    Log.e("123", "暂时没有数据！");
                                } else {
                                    Log.e("123","获取系统消息报错：" + error);
                                }
                                if (shouCangEntities.size() == 0) {
                                    set_date_view(shouCangEntities);
                                }
                            }
                        }, this, is_show_dialog));
    }

    private void set_date_view(final List<ZuiZinAnswerEntity.DataBean> shouCangEntities) {
        //设置上拉刷新下拉加载
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemAnimator(null);
        layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        jingXuanAdapter = new ZuiXinHuidaAdapter(shouCangEntities, getApplicationContext());
        jingXuanAdapter.setmOnItemClickListener(new ZuiXinHuidaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(NewQuestionActivity.this, AnserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("answer_id", shouCangEntities.get(position).getId() + "");
                intent.putExtras(bundle);
                startActivity(intent);
                //记录此答案已经被点击过
                jilu_answer_list(shouCangEntities.get(position).getId() + "");
            }
        });
        jingXuanAdapter.setOnItemLongClickListener(new XiTongAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                PopWindowHelper.public_tishi_pop(NewQuestionActivity.this, "食与家提示", "是否删除？", "取消", "确定", new DialogCallBack() {
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
        recycler_view.setAdapter(jingXuanAdapter);
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
        xrefreshview.enableRecyclerViewPullUp(true);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
        xrefreshview.setPreLoadCount(10);
        if (shouCangEntities.size() > 7) {
            xrefreshview.enableReleaseToLoadMore(true);
//            jingXuanAdapter.setCustomLoadMoreView(new XRefreshViewFooter(this));//加载更多
            xrefreshview.setLoadComplete(false);//显示底部
        } else {
            xrefreshview.enableReleaseToLoadMore(false);
            xrefreshview.setLoadComplete(true);//隐藏底部
        }        //设置静默加载时提前加载的item个数
//        xefreshView1.setPreLoadCount(4);

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
                        //填写刷新数据的网络请求，一般page=1，List集合清空操作
                        initNetDate(false);
                        xrefreshview.stopRefresh();//刷新停止

                    }
                }, 1000);//2000是刷新的延时，使得有个动画效果
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //填写加载更多的网络请求，一般page++
                        initNetDate(false);

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
        params.put("answer_id", shouCangEntities.get(position).getId() + "");
        subscription = Network.getInstance("删除最新回答", this)
                .delete_new_answer(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Toast.makeText(getApplicationContext(), "该内容已被删除", Toast.LENGTH_SHORT).show();
                                Log.e("123","删除最新回答成功：" + result.getCode());
                                if (shouCangEntities.size() <= 8) {
                                    shouCangEntities.remove(position);
                                    initNetDate(false);
                                } else {
                                    shouCangEntities.remove(position);
                                    jingXuanAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onError(String error) {
                                if (error.equals("无数据")) {
                                    Log.e("123", "暂时没有数据！");
                                } else {
                                    Log.e("123","删除最新回答报错：" + error);
                                }
                            }
                        }, this, false));
    }


    private void jilu_answer_list(String answer_id) {
        subscription = Network.getInstance("最新回答记录", this)
                .get_xitong_detail("1", answer_id,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123","获取最新回答成功：" + result.getCode());

                            }

                            @Override
                            public void onError(String error) {
                                if (error.equals("无数据")) {
                                    Log.e("123", "暂时没有数据！");
                                } else {
                                    Log.e("123","获取最新回答报错：" + error);
                                }
                            }
                        }, this, false));
    }

    @Override
    public void widgetClick(View v) {

    }
}

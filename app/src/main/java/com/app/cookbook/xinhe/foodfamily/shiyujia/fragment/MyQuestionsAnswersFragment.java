package com.app.cookbook.xinhe.foodfamily.shiyujia.fragment;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.HuiDaDate;
import com.app.cookbook.xinhe.foodfamily.main.entity.HuiDaEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageEvent;
import com.app.cookbook.xinhe.foodfamily.main.entity.TiWenDate;
import com.app.cookbook.xinhe.foodfamily.main.entity.TiWenEntity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.HomeAdapter;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.UserAnswerAdapter;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.UserTopicAdapter;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.HomeItem;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Created by wenlaisu on 2018/4/12.
 */
@SuppressLint("ValidFragment")
public class MyQuestionsAnswersFragment extends Fragment {

    protected Subscription subscription;
    private View mView;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.recyclerView)
    RecyclerView recycler_view;
    @BindView(R.id.amount_tv)
    TextView amount_tv;
    @BindView(R.id.image_empty)
    ImageView image_empty;
    @BindView(R.id.tv_empty)
    TextView tv_empty;

    //提问集合
    private List<TiWenEntity> tiWenEntities = new ArrayList<>();
    //提问适配器
    private UserTopicAdapter userTopicAdapter;
    //
    private LinearLayoutManager layoutManager;

    //回答集合
    private List<HuiDaEntity> huiDaEntities = new ArrayList<>();
    //回答适配器
    private UserAnswerAdapter userAnswerAdapter;


    //问题当前页
    private int PAGE = 1;
    //提问当前页
    private int PAGETWO = 1;
    //是否加载更多
    private boolean is_not_more;

    private String huiDaType;
    private String tiWenType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.my_print_fragment, container, false);
            ButterKnife.bind(this, mView);
            EventBus.getDefault().register(this);
            huiDaType = "1";
            tiWenType = "0";
            initView();
            requestHuiDa(true);
        } else {
            initView();
        }
        return mView;
    }

    private void initView() {
        userTopicAdapter = new UserTopicAdapter(getActivity());
        userAnswerAdapter = new UserAnswerAdapter(getActivity());
    }

    private void getItemClick() {
        userTopicAdapter.setOnItemClickListener(new UserTopicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TiWenEntity item = tiWenEntities.get(position);
                Intent intent = new Intent(getActivity(), FenLeiQuestionDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("wenti_id", item.getId());
                bundle.putString("flag", "0");
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        switch (messageEvent.getMessage()) {
            case "201":
                if (huiDaEntities.size() > 0) {
                    huiDaEntities.clear();
                }
                if (tiWenEntities.size() > 0) {
                    tiWenEntities.clear();
                }
                huiDaType = "0";
                tiWenType = "1";
                requestTiWen(true);
                //修改对应的empty_view
                image_empty.setImageResource(R.drawable.wodetiwen_empty);
                tv_empty.setText("暂未提出任何问题，快去说出你的好奇吧");
                break;
            case "202":
                if (tiWenEntities.size() > 0) {
                    tiWenEntities.clear();
                }
                if (huiDaEntities.size() > 0) {
                    huiDaEntities.clear();
                }
                tiWenType = "0";
                huiDaType = "1";
                requestHuiDa(true);
                //修改对应的empty_view
                image_empty.setImageResource(R.drawable.huida_empty);
                tv_empty.setText("暂未作出任何回答，快去分享你的知识吧");
                break;
        }
    }

    private void requestTiWen(boolean is_show_dialog) {
        subscription = Network.getInstance("获取提问", getActivity())
                .get_my_question(String.valueOf(PAGETWO),
                        new ProgressSubscriberNew<>(TiWenDate.class, new GsonSubscriberOnNextListener<TiWenDate>() {
                            @Override
                            public void on_post_entity(TiWenDate result) {
                                Log.e("123", "提问成功：");
                                amount_tv.setVisibility(View.VISIBLE);
                                amount_tv.setText("共" + result.getTotal() + "条提问");
                                if (tiWenEntities.size() > 0 && PAGETWO > 1) {//这表示是"加载"
                                    if (result.getData().size() == 0) {
                                        //表示没有更多数据了
                                        is_not_more = true;
                                        tiWenEntities.addAll(result.getData());
                                        userTopicAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        tiWenEntities.addAll(result.getData());
                                        userTopicAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    tiWenEntities = result.getData();
                                    set_list_resource(huiDaEntities, tiWenEntities);
                                }
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "     提问报错    " + error);
                            }
                        }, getActivity(), is_show_dialog));
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
                                amount_tv.setText("共" + result.getData().getTotal() + "条回答");
                                if (huiDaEntities.size() > 0 && PAGE > 1) {//这表示是"加载"
                                    if (result.getData().getData().size() == 0) {
                                        //表示没有更多数据了
                                        is_not_more = true;
                                        huiDaEntities.addAll(result.getData().getData());
                                        userAnswerAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        huiDaEntities.addAll(result.getData().getData());
                                        userAnswerAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    huiDaEntities = result.getData().getData();
                                    set_list_resource(huiDaEntities, tiWenEntities);
                                }
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "      回答报错       " + error);
                            }
                        }, getActivity(), is_show_dialog));
    }

    private void set_list_resource(final List<HuiDaEntity> dates, List<TiWenEntity> tiWenDates) {
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);
        if ("1".equals(tiWenType)) {
            if (getActivity() != null) {
                userTopicAdapter = new UserTopicAdapter(getActivity());
                userTopicAdapter.setTiWenEntities(tiWenDates);
                recycler_view.setAdapter(userTopicAdapter);
            }
        }

        if ("1".equals(huiDaType)) {
            if (getActivity() != null) {
                userAnswerAdapter = new UserAnswerAdapter(getActivity());
                userAnswerAdapter.setDaEntities(dates);
                recycler_view.setAdapter(userAnswerAdapter);
            }
        }
        getItemClick();

        // 设置静默加载模式
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
        xrefreshview.enableReleaseToLoadMore(true);
        xrefreshview.enableRecyclerViewPullUp(true);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
        xrefreshview.setPreLoadCount(10);
        xrefreshview.enableReleaseToLoadMore(false);
        xrefreshview.setLoadComplete(true);//隐藏底部
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
                        PAGE = 1;
                        if ("1".equals(tiWenType)) {
                            requestTiWen(true);
                        } else if ("1".equals(huiDaType)) {
                            requestHuiDa(true);
                        }
                        xrefreshview.stopRefresh();//刷新停止

                    }
                }, 1000);//2000是刷新的延时，使得有个动画效果
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        PAGE++;
                        //填写加载更多的网络请求，一般page++
                        if ("1".equals(tiWenType)) {
                            requestTiWen(true);
                        } else if ("1".equals(huiDaType)) {
                            requestHuiDa(true);
                        }
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

package com.app.cookbook.xinhe.foodfamily.shiyujia.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.LabelPrintAdapter;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.QuestionsAnswersAdapter;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.LabelPrint;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.QuestionsAnswers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Created by wenlaisu on 2018/4/12.
 */
@SuppressLint("ValidFragment")
public class QuestionsAnswersFragment extends Fragment {

    protected Subscription subscription;
    @BindView(R.id.title_layout)
    TextView title_layout;
    @BindView(R.id.amount_tv)
    TextView amount_tv;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    RelativeLayout empty_layout;
    private static final String KEY = "key";
    private String title = "测试";

    private LinearLayoutManager layoutManager;

    List<String> mDatas = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mView;
    private Context mContext;
    //问答适配器
    private QuestionsAnswersAdapter questionsAnswersAdapter;
    //问答集合
    private List<QuestionsAnswers.DataBean> questionsAnswers = new ArrayList<>();
    private QuestionsAnswers dataBean;
    private boolean is_not_more;

    //宿主Activity Id
    private String labelId;

    private int PAGE = 1;
    //列表
    private String NoMsg = "0";
    //记录滑动位置
    private boolean itemPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mContext = getActivity();
            mView = inflater.inflate(R.layout.my_print_fragment, container, false);
            ButterKnife.bind(this, mView);//绑定黄牛刀
            initView(mView);
            initJingXuanDate(true);
        }
        return mView;
    }


    protected void initView(View view) {
        labelId = getActivity().getIntent().getStringExtra("id");
        Log.e("123", "      ---->印迹Id    " + labelId);
    }


    private void initJingXuanDate(boolean is_true) {
        Map<String, String> params = new HashMap<>();
        params.put("class_id", labelId);
        params.put("page", String.valueOf(PAGE));
        subscription = Network.getInstance("问答", getActivity())
                .getClassQuestion(params,
                        new ProgressSubscriberNew<>(QuestionsAnswers.class, new GsonSubscriberOnNextListener<QuestionsAnswers>() {
                            @Override
                            public void on_post_entity(QuestionsAnswers result) {
                                dataBean = result;
                                Log.e("123", "     问答      " + result.getData().size());
                                amount_tv.setVisibility(View.VISIBLE);
                                amount_tv.setText("共" + result.getTotal() + "条提问");
                                if (result.getData().size() == 0 && PAGE > 1) {
                                    PAGE = PAGE - 1;
                                }
                                if (questionsAnswers.size() > 0 && PAGE > 1) {//这表示是"加载"
                                    if (result.getData().size() == 0) {
                                        is_not_more = true;
                                        questionsAnswers.addAll(result.getData());
                                        questionsAnswersAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        questionsAnswers.addAll(result.getData());
                                        questionsAnswersAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    questionsAnswers = result.getData();
                                    set_list_resource(questionsAnswers);
                                }
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                if ("无数据".equals(error)) {
                                    NoMsg = "1";
                                    PAGE = PAGE - 1;
                                }
                                Log.e("123", "   问答报错  " + error);
                            }
                        }, getActivity(), is_true));
    }


    private void set_list_resource(final List<QuestionsAnswers.DataBean> dates) {
        //设置上拉刷新下拉加载
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        questionsAnswersAdapter = new QuestionsAnswersAdapter(getActivity());
        questionsAnswersAdapter.setList(questionsAnswers);
        mRecyclerView.setAdapter(questionsAnswersAdapter);
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


}

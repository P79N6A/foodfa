package com.app.cookbook.xinhe.foodfamily.shiyujia.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.callback.DialogCallBack;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.AddImagesActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.MyDraftsPrintAdapter;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.DraftDelete;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.MyDraftsPrint;
import com.app.cookbook.xinhe.foodfamily.util.PopWindowHelper;

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
public class MyDraftsPrintFragment extends Fragment {

    protected Subscription subscription;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.empty_layout)
    RelativeLayout empty_layout;
    @BindView(R.id.tv_empty)
    TextView tv_empty;

    private View mView;
    private Context mContext;
    private LinearLayout ll;
    private LinearLayoutManager layoutManager;

    private MyDraftsPrintAdapter myDraftsPrintAdapter;
    private List<MyDraftsPrint.DataBean> myDraftsPrint = new ArrayList<>();
    private int PAGE = 1;
    //是否有更多
    private boolean is_not_more;
    private String draftsId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mContext = getActivity();
            mView = View.inflate(mContext, R.layout.recyclerview_list, null);
            ButterKnife.bind(this, mView);//绑定黄牛刀
            initView();
        }
        return mView;
    }

    @Override
    public void onResume() {
        draftsId = "";
        initJingXuanDate(true);
        super.onResume();
    }

    public LinearLayout getLl() {
        return ll;
    }


    private void initView() {
        tv_empty.setText("还没有保存印记呦～");
        ll = mView.findViewById(R.id.ll);
    }

    private void getOnItemClick() {
        myDraftsPrintAdapter.setOnItemClickListener(new MyDraftsPrintAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), AddImagesActivity.class);
                //传递必要数据(图片,id,内容,标签)
                ArrayList<String> list = new ArrayList<>();
                if (null != myDraftsPrint.get(position).getImage_data()) {
                    for (int i = 0; i < myDraftsPrint.get(position).getImage_data().size(); i++) {
                        list.add(myDraftsPrint.get(position).getImage_data().get(i).getPath());
                    }
                }
                if (null != myDraftsPrint.get(position).getId()) {
                    intent.putExtra("update_id", myDraftsPrint.get(position).getId());
                }
                if (null != myDraftsPrint.get(position).getContent()) {
                    intent.putExtra("update_content", myDraftsPrint.get(position).getContent());
                }

                if (null != myDraftsPrint.get(position).getClass_ids()&&myDraftsPrint.get(position).getClass_ids().length()>0) {
                    intent.putExtra("biaoqian_string", myDraftsPrint.get(position).getClass_ids());
                    Log.e("传过去的标签：", "1"+myDraftsPrint.get(position).getClass_ids()+"1");
                }
                intent.putStringArrayListExtra("update_image_paths", list);
                intent.putExtra("flag", "5");
                startActivity(intent);
            }
        });

        myDraftsPrintAdapter.setOnLongClickListener(new MyDraftsPrintAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                PopWindowHelper.public_tishi_pop(getActivity(), "删除印迹", "是否删除该印迹？", "否", "是", new DialogCallBack() {
                    @Override
                    public void save() {
                        deleteImage(myDraftsPrint.get(position).getId());
                    }

                    @Override
                    public void cancel() {

                    }
                });

            }
        });
    }

    private void initJingXuanDate(boolean is_true) {
//        Map<String, String> params = new HashMap<>();
//        params.put("page", String.valueOf(PAGE));
        subscription = Network.getInstance("草稿箱印迹", getActivity())
                .get_draft_list(String.valueOf(PAGE),
                        new ProgressSubscriberNew<>(MyDraftsPrint.class, new GsonSubscriberOnNextListener<MyDraftsPrint>() {
                            @Override
                            public void on_post_entity(MyDraftsPrint result) {
                                Log.e("123", "     草稿箱印迹      " + result);
                                if (PAGE == 1 && result.getData().size() > 0) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    empty_layout.setVisibility(View.GONE);
                                } else if (PAGE == 1 && result.getData().size() == 0) {
                                    empty_layout.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                                if (myDraftsPrint.size() > 0 && PAGE > 1) {//这表示是"加载"
                                    if (String.valueOf(draftsId).equals("")) {//表示刷新
                                        myDraftsPrint.clear();
                                    }
                                    if (result.getData().size() == 0) {
                                        is_not_more = true;
                                        myDraftsPrint.addAll(result.getData());
                                        myDraftsPrintAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        myDraftsPrint.addAll(result.getData());
                                        myDraftsPrintAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    myDraftsPrint = result.getData();
                                    set_list_resource(myDraftsPrint);
                                }
                                if (myDraftsPrint.size() > 0) {
                                    draftsId = myDraftsPrint.get(myDraftsPrint.size() - 1).getId();
                                }
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                if ("无数据".equals(error)) {
                                }
                                if (myDraftsPrint.size() == 0) {
                                    set_list_resource(myDraftsPrint);
                                }
                                Log.e("123", "   草稿箱印迹报错  " + error);
                            }
                        }, getActivity(), is_true));
    }


    //刪除圖文
    private void deleteImage(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("draft_id", id);
        Log.e("123：", params.toString());
        subscription = Network.getInstance("刪除圖文", getActivity())
                .getDraftDelete(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<DraftDelete>>() {
                            @Override
                            public void onNext(Bean<DraftDelete> result) {
                                Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
                                Log.e("123", "刪除圖文成功" + result.getCode());
                                initJingXuanDate(false);
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "刪除圖文报错：" + error);
                                if (error.contains("BEGIN_OBJECT")) {
                                    initJingXuanDate(true);
                                }
                            }
                        }, getActivity(), false));
    }


    private void set_list_resource(final List<MyDraftsPrint.DataBean> dates) {
        //设置上拉刷新下拉加载
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        myDraftsPrintAdapter = new MyDraftsPrintAdapter(getActivity());
        myDraftsPrintAdapter.setMyDraftsPrint(dates);
        recyclerView.setAdapter(myDraftsPrintAdapter);
        recyclerView.setHasFixedSize(true);
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
//                        PAGE = PAGE + 1;
                        //填写加载更多的网络请求，一般page++
                        PAGE = Integer.valueOf(draftsId);
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

package com.app.cookbook.xinhe.foodfamily.main;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.callback.DialogCallBack;
import com.app.cookbook.xinhe.foodfamily.main.adapter.MessageCommentAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageComment;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.CommentVideoReplyActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.CommtentReplyActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.PersonalHomepageActivity;
import com.app.cookbook.xinhe.foodfamily.util.PopWindowHelper;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageCommentActivity extends BaseActivity {
    @BindView(R.id.iamge_back)
    ImageView iamge_back;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.back_layout)
    LinearLayout back_layout;
    //整体布局
    @BindView(R.id.main_layout)
    RelativeLayout main_layout;
    //控布局
    @BindView(R.id.null_layout)
    RelativeLayout null_layout;
    @BindView(R.id.title_two)
    TextView title_two;
    @BindView(R.id.back_layout_btn)
    RelativeLayout back_layout_btn;
    private int PAGE = 1;
    private boolean is_not_more;
    private LinearLayoutManager layoutManager;
    private MessageCommentAdapter messageCommentAdapter;
    private List<MessageComment.DataBean> dates = new ArrayList<>();

    //消息ID
    private String message_id;
    private MessageComment.DataBean dataItem;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            dataItem = dates.get(msg.arg1);
            String comment_id = dataItem.getComment_id();
            String user_id = dataItem.getUid();
            Intent intent;
            Bundle bundle;
            switch (msg.what) {
                case 400:
                    intent = new Intent(MessageCommentActivity.this, FriendPageActivity.class);
                    intent.putExtra("UserId", user_id);
                    startActivity(intent);
                    break;
                case 401:
//                    if ("1".equals(dataItem.getStatus())) {
//                        if ("1".equals(dataItem.getComment_is_existence())) {
//                            intent = new Intent(MessageCommentActivity.this, AnswerReplyByIdActivity.class);
//                            bundle = new Bundle();
//                            intent.putExtra("answer_id", dataItem.getAnswer_id());
//                            intent.putExtra("original_id", dataItem.getComment_id());
//                            intent.putExtras(bundle);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        } else if ("2".equals(dataItem.getComment_is_existence())) {
//                            Toast.makeText(MessageCommentActivity.this, "评论已经删除", Toast.LENGTH_SHORT).show();
//                        }
//                    } else if ("2".equals(dataItem.getStatus())) {
//                        if ("1".equals(dataItem.getImpriting_comment_data().getIs_existence())) {
//                            intent = new Intent(MessageCommentActivity.this, CommtentReplyActivity.class);
//                            intent.putExtra("id", dataItem.getVideo_image_id());
//                            intent.putExtra("comment_id", dataItem.getImpriting_comment_data().getTable_id());
//                            intent.putExtra("type", "1");
//                            startActivity(intent);
//                        } else if ("2".equals(dataItem.getComment_is_existence())) {
//                            Toast.makeText(MessageCommentActivity.this, "评论已经删除", Toast.LENGTH_SHORT).show();
//                        }
//                    } else if ("3".equals(dataItem.getStatus())) {
//                        if ("1".equals(dataItem.getImpriting_comment_data().getIs_existence())) {
//                            intent = new Intent(MessageCommentActivity.this, CommentVideoReplyActivity.class);
//                            intent.putExtra("id", dataItem.getVideo_image_id());
//                            intent.putExtra("comment_id", dataItem.getImpriting_comment_data().getTable_id());
//                            intent.putExtra("type", "2");
//                            startActivity(intent);
//                        } else if ("2".equals(dataItem.getComment_is_existence())) {
//                            Toast.makeText(MessageCommentActivity.this, "评论已经删除", Toast.LENGTH_SHORT).show();
//                        }
//                    }


                    break;
            }
        }
    };


    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_message_comment);
        ButterKnife.bind(this);
        isShowTitle(false);
        init_net_source(true);
        setGestureListener();
    }


    @Override
    public void initonResume() {

    }

    @Override
    public void initonPause() {

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
    public void doBusiness(Context mContext) {
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void widgetClick(View v) {

    }

    //评论列表
    private void init_net_source(boolean is_show_dialog) {
        Log.e("关注问题PAGE：", PAGE + "");
        subscription = Network.getInstance("评论列表", this)
                .comment_list_request(message_id,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<MessageComment>>() {
                            @Override
                            public void onNext(Bean<MessageComment> result) {
                                Log.e("123", "评论列表：" + result.getCode() + result.getMsg() + "     result.getData().getDat        " + result.getData().getData().size());
                                result.getData().getData().get(0).getId();
                                if (result.getData().getData().size() > 0) {
                                    int idSize = result.getData().getData().size();
                                    for (int i = 0; i < idSize; i++) {
                                        if (idSize == i + 1) {
                                            message_id = result.getData().getData().get(i).getId();
                                        }
                                    }
                                }
                                if (dates.size() > 0 && PAGE > 1) {//这表示是"加载"
                                    if (result.getData().getData().size() == 0) {
                                        //表示没有更多数据了
                                        is_not_more = true;
                                        dates.addAll(result.getData().getData());
                                        messageCommentAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        dates.addAll(result.getData().getData());
                                        messageCommentAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    dates = result.getData().getData();
                                    set_list_resource(result.getData().getData());
                                }
                                Log.e("123", "       message_id       " + message_id);
                            }

                            @Override
                            public void onError(String error) {
                                is_not_more = true;
                                if (error.equals("无数据")) {
                                    Log.e("123", "暂时没有数据！");
                                } else {
                                    Log.e("123", "错误：" + error);
                                }
                                if (dates.size() == 0) {
                                    set_list_resource(dates);
                                }
                            }
                        }, this, is_show_dialog));
    }

    private boolean ista = false;

    private void set_list_resource(List<MessageComment.DataBean> dates) {
        //设置上拉刷新下拉加载
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemAnimator(null);
        layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setHasFixedSize(true);

        messageCommentAdapter = new MessageCommentAdapter(dates, getApplicationContext(), ista, handler);
        recycler_view.setAdapter(messageCommentAdapter);
        setOnClickLongListener();
        // 静默加载模式不能设置footerview
        // 设置静默加载模式
        xrefreshview.setSilenceLoadMore(true);
        //设置刷新完成以后，headerview固定的时间
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);
        xrefreshview.setPullRefreshEnable(true);
        xrefreshview.setPullLoadEnable(true);
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
        //给recycler_view设置底部加载布局
        xrefreshview.enableRecyclerViewPullUp(true);
        //设置静默加载时提前加载的item个数
        xrefreshview.setPreLoadCount(10);
        if (dates.size() > 7) {
            xrefreshview.enableReleaseToLoadMore(true);
//            messageCommentAdapter.setCustomLoadMoreView(new XRefreshViewFooter(this));//加载更多
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
                        message_id = "";
                        //填写刷新数据的网络请求，一般page=1，List集合清空操作
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

    private MessageComment.DataBean dataBean;

    private void setOnClickLongListener() {
        messageCommentAdapter.setOnItemLongClickListener(new MessageCommentAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                dataBean = dates.get(position);
                PopWindowHelper.public_tishi_pop(MessageCommentActivity.this, "食与家提示", "是否删除？", "取消", "确定", new DialogCallBack() {
                    @Override
                    public void save() {
                        delete_message(dataBean.getId(), position);
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        });

        messageCommentAdapter.setOnItemClickListener(new MessageCommentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MessageComment.DataBean dataBean = dates.get(position);
                Intent intent;
                Bundle bundle;
                if ("1".equals(dataBean.getStatus())) {
                    if ("1".equals(dataBean.getComment_is_existence().getComment_is_existence())) {
                        intent = new Intent(MessageCommentActivity.this, AnswerReplyByIdActivity.class);
                        bundle = new Bundle();
                        intent.putExtra("answer_id", dataBean.getAnswer_id());
                        intent.putExtra("original_id", dataBean.getComment_id());
                        intent.putExtras(bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MessageCommentActivity.this, "评论已经删除", Toast.LENGTH_SHORT).show();
                    }
                } else if ("2".equals(dataBean.getStatus())) {
                    if ("1".equals(dataBean.getImpriting_comment_data().getIs_existence())) {
                        intent = new Intent(MessageCommentActivity.this, CommtentReplyActivity.class);
                        intent.putExtra("id", dataBean.getVideo_image_id());
                        intent.putExtra("comment_id", dataBean.getImpriting_comment_data().getTable_id());
                        intent.putExtra("type", "1");
                        startActivity(intent);
                    } else {
                        Toast.makeText(MessageCommentActivity.this, "评论已经删除", Toast.LENGTH_SHORT).show();
                    }
                } else if ("3".equals(dataBean.getStatus())) {
                    if ("1".equals(dataBean.getImpriting_comment_data().getIs_existence())) {
                        intent = new Intent(MessageCommentActivity.this, CommentVideoReplyActivity.class);
                        intent.putExtra("id", dataBean.getVideo_image_id());
                        intent.putExtra("comment_id", dataBean.getImpriting_comment_data().getTable_id());
                        intent.putExtra("type", "2");
                        startActivity(intent);
                    } else {
                        Toast.makeText(MessageCommentActivity.this, "评论已经删除", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void delete_message(final String id, final int position) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        Log.e("123", "          " + position);
        subscription = Network.getInstance("删除评论", this)
                .delete_commentList_question(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Toast.makeText(getApplicationContext(), "该内容已被删除", Toast.LENGTH_SHORT).show();
                                Log.e("123", "删除评论成功：" + result.getCode());
                                //删除第一页数据之后自动加载第二页数据
                                if (dates.size() <= 9) {
                                    dates.remove(position);
                                    init_net_source(false);
                                    messageCommentAdapter.notifyDataSetChanged();
                                } else {
                                    dates.remove(position);
                                    messageCommentAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onError(String error) {
                                if (error.equals("无数据")) {
                                    Toast.makeText(getApplicationContext(), "暂时没有数据！", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e("123", "删除点赞收藏报错：" + error);
                                }
                            }
                        }, this, false));
    }

}

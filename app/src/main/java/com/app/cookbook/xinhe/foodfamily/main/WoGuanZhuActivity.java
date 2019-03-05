package com.app.cookbook.xinhe.foodfamily.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.WoGuanZhuEntity;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;

import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.PersonalHomepageActivity;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class WoGuanZhuActivity extends BaseActivity {
    //返回
    @BindView(R.id.iamge_back)
    ImageView iamge_back;
    //返回
    @BindView(R.id.back_layout)
    LinearLayout back_layout;
    //标题
    @BindView(R.id.title_two)
    TextView title_two;
    //推荐关注人
    @BindView(R.id.suggestedUsers)
    TextView suggestedUsers;
    //列表
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    //刷新加载
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;

    WoGuanZhuRenAdapter woGuanZhuRenAdapter;

    List<WoGuanZhuEntity.DataBean> dates = new ArrayList<>();
    private int PAGE = 1;
    private boolean is_not_more;
    LinearLayoutManager layoutManager;

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
        setContentLayout(R.layout.activity_wo_guan_zhu);
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
        suggestedUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WoGuanZhuActivity.this, SuggestedUsersActivity.class);
                startActivity(intent);
            }
        });
        //获取网络数据
        init_net_source(true);
    }

    public static List<WoGuanZhuEntity.DataBean> guanZhuwoEntities = new ArrayList<>();


    private void init_net_source(boolean is_show_dialog) {
        Map<String, String> params = new HashMap<>();
        params.put("status", "1");
        params.put("page", String.valueOf(PAGE));
        params.put("module", "message");
        Log.e("关注的人参数：", params.toString());
        subscription = Network.getInstance("关注的人", this)
                .get_wo_guanzhu_ren("1", String.valueOf(PAGE), "message",
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<WoGuanZhuEntity>>() {
                            @Override
                            public void onNext(Bean<WoGuanZhuEntity> result) {
                                Log.e("123", "获取关注的人成功：" + result.getCode());
                                if (dates.size() > 0 && PAGE > 1) {//这表示是"加载"
                                    if (result.getData().getData().size() == 0) {
                                        //表示没有更多数据了
                                        is_not_more = true;
                                        dates.addAll(result.getData().getData());
                                        woGuanZhuRenAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        dates.addAll(result.getData().getData());
                                        woGuanZhuRenAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    dates = result.getData().getData();
                                    set_list_resource(result.getData().getData());
                                    guanZhuwoEntities = result.getData().getData();
                                }
                            }

                            @Override
                            public void onError(String error) {
                                is_not_more = true;
                                if (error.equals("无数据")) {
                                    Log.e("123", "暂时没有数据！");
                                } else {
                                    Log.e("123", "获取提问报错：" + error);
                                }
                                if (dates.size() == 0) {
                                    set_list_resource(dates);
                                }
                            }
                        }, this, is_show_dialog));
    }


    private void set_list_resource(final List<WoGuanZhuEntity.DataBean> dates) {
        //设置上拉刷新下拉加载
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemAnimator(null);
        layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        woGuanZhuRenAdapter = new WoGuanZhuRenAdapter(dates, getApplicationContext());
        recycler_view.setAdapter(woGuanZhuRenAdapter);
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
//            woGuanZhuRenAdapter.setCustomLoadMoreView(new XRefreshViewFooter(this));//加载更多
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

    @Override
    public void widgetClick(View v) {

    }

    public class WoGuanZhuRenAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
        private List<WoGuanZhuEntity.DataBean> list;
        private Context context;
        private static final int EMPTY_VIEW = 2;
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private String status = "";

        public WoGuanZhuRenAdapter(List<WoGuanZhuEntity.DataBean> mlist, Context mcontext) {
            list = mlist;
            context = mcontext;
        }


        @Override
        public RecyclerView.ViewHolder getViewHolder(View view) {
            PurchaseViewHolder purchaseViewHolder = new PurchaseViewHolder(view, false);
            return purchaseViewHolder;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
            RecyclerView.ViewHolder holder = getViewHolderByViewType(viewType, parent);
            return holder;
        }

        private RecyclerView.ViewHolder getViewHolderByViewType(int viewType, ViewGroup parent) {
            View empty_view = LayoutInflater.from(context).inflate(R.layout.guanzhu_empty_layout, parent, false);
            View shidan_view = LayoutInflater.from(context).inflate(R.layout.woguanzhuren_item_new, parent, false);
            RecyclerView.ViewHolder holder = null;
            if (viewType == EMPTY_VIEW) {
                holder = new EmptyViewHolder(empty_view, true);
            } else {
                holder = new PurchaseViewHolder(shidan_view, true);
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder view_holder, final int position, boolean isItem) {
            if (view_holder instanceof WoGuanZhuRenAdapter.PurchaseViewHolder) {
                WoGuanZhuRenAdapter.PurchaseViewHolder holder = (WoGuanZhuRenAdapter.PurchaseViewHolder) view_holder;
                if (position == 0) {
                    holder.title.setVisibility(View.VISIBLE);
                } else {
                    holder.title.setVisibility(View.GONE);
                }

                ((WoGuanZhuRenAdapter.PurchaseViewHolder) view_holder).view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(v, position);
                        }
                    }
                });

                holder.user_name.setText(list.get(position).getName());

                Glide.with(context.getApplicationContext()).load(list.get(position).getAvatar())
                        .error(R.drawable.touxiang)
                        .into(holder.select_head_image);
                //去往个人中心
                holder.select_head_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(WoGuanZhuActivity.this, FriendPageActivity.class);
                        intent.putExtra("UserId", list.get(position).getUid() + "");
                        startActivity(intent);
                    }
                });
                holder.name_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(WoGuanZhuActivity.this, FriendPageActivity.class);
                        intent.putExtra("UserId", list.get(position).getUid() + "");
                        startActivity(intent);
                    }
                });

                if (null != list.get(position).getQuestions()) {
                    holder.content_tv.setVisibility(View.VISIBLE);
                    holder.quiz_type.setText("最新问题");
                    holder.content_tv.setText(list.get(position).getQuestions().getTitle());
                } else {
                    holder.content_tv.setVisibility(View.GONE);
                    holder.quiz_type.setText("暂无问题");
                }
                holder.content_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, FenLeiQuestionDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("wenti_id", list.get(position).getQuestions().getId() + "");
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });

            }
        }


        @Override
        public int getAdapterItemViewType(int position) {
            if (list.size() == 0) {
                return EMPTY_VIEW;
            }
            return TYPE_ITEM;
        }

        @Override
        public int getAdapterItemCount() {
            return list.size() > 0 ? list.size() : 1;
        }


        private OnItemClickListener mOnItemClickListener;

        public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }

        public void setData(List<WoGuanZhuEntity.DataBean> list) {
            this.list = list;
            notifyDataSetChanged();
        }


        public class EmptyViewHolder extends RecyclerView.ViewHolder {
            public ImageView img_top;
            public View view;

            public EmptyViewHolder(View item_view, boolean isItem) {
                super(item_view);
                if (isItem) {
                    this.view = item_view;
                    img_top = (ImageView) item_view
                            .findViewById(R.id.img_top);
                }
            }
        }

        public class PurchaseViewHolder extends RecyclerView.ViewHolder {
            public View view;
            public TextView user_name, content_tv;
            public CircleImageView select_head_image;
            private TextView title;
            private TextView quiz_type;
            private LinearLayout name_layout;


            public PurchaseViewHolder(View itemView, boolean isItem) {
                super(itemView);
                if (isItem) {
                    this.view = itemView;
                    user_name = (TextView) itemView.findViewById(R.id.user_name);
                    select_head_image = (CircleImageView) itemView.findViewById(R.id.select_head_image);
                    content_tv = (TextView) itemView.findViewById(R.id.content_tv);
                    title = itemView.findViewById(R.id.title);
                    quiz_type = itemView.findViewById(R.id.quiz_type);
                    name_layout = itemView.findViewById(R.id.name_layout);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}

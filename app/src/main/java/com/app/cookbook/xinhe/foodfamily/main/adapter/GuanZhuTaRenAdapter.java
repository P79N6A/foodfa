package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.main.OtherUserActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.WoGuanZhuEntity;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;

/**
 * Created by shiyujia02 on 2018/2/11.
 */

public class GuanZhuTaRenAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<WoGuanZhuEntity.DataBean> list;
    private Context context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    /**
     * Rxjava
     */
    protected Subscription subscription;
    private boolean ista;

    public GuanZhuTaRenAdapter(List<WoGuanZhuEntity.DataBean> mlist, Context mcontext, boolean mista) {
        list = mlist;
        ista = mista;
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

    RecyclerView.ViewHolder holder = null;

    private RecyclerView.ViewHolder getViewHolderByViewType(int viewType, ViewGroup parent) {
        View empty_view;
        if (ista) {
            empty_view = LayoutInflater.from(context).inflate(R.layout.taa_guanzhuworen_empty, parent, false);

        } else {
            empty_view = LayoutInflater.from(context).inflate(R.layout.guanzhuworen_empty, parent, false);

        }
        View shidan_view = LayoutInflater.from(context).inflate(R.layout.woguanzhuren_item, parent, false);

        if (viewType == EMPTY_VIEW) {
            holder = new EmptyViewHolder(empty_view, true);

        } else if (viewType == TYPE_ITEM) {
            holder = new PurchaseViewHolder(shidan_view, true);

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, final int position, boolean isItem) {
        if (view_holder instanceof PurchaseViewHolder) {
            PurchaseViewHolder holder = (PurchaseViewHolder) view_holder;
            if ("1".equals(list.get(position).getStatus())) {//未互相关注
                holder.quxiaoguanzhu_layout_top.setVisibility(View.GONE);
                holder.guanzhu_layout_top.setVisibility(View.VISIBLE);
            } else {//互相关注
                holder.quxiaoguanzhu_layout_top.setVisibility(View.VISIBLE);
                holder.guanzhu_layout_top.setVisibility(View.GONE);
            }


            holder.user_name.setText(list.get(position).getName());
            if (null != list.get(position).getCreated_at() || "".equals(list.get(position).getCreated_at())) {
                Long time = Long.valueOf(list.get(position).getCreated_at());
                String month = "";
                String date = "";
                if (transForDate(time).getMonth() < 10) {
                    month = "0" + (Integer.valueOf(transForDate(time).getMonth()) + 1);
                } else {
                    month = (Integer.valueOf(transForDate(time).getMonth()) + 1) + "";
                }
                if (transForDate(time).getDate() < 10) {
                    date = "0" + transForDate(time).getDate();
                } else {
                    date = transForDate(time).getDate() + "";
                }
                holder.time_tv.setText(month + "-" + date);
            } else {
                holder.time_tv.setText("");
            }

            Glide.with(context).load(list.get(position).getAvatar())
                    .centerCrop()
                    .error(R.drawable.touxiang)
                    .into(holder.select_head_image);
            //关注按钮
            holder.guanzhu_layout_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    } else {
                        guanzhu_submit(list.get(position).getUid() + "", position);
                    }
                }
            });
            //取消关注
            holder.quxiaoguanzhu_layout_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    } else {
                        quxiao_guanzhu_submit(list.get(position).getUid() + "", position);
                    }
                }
            });

            //去往用户主页
            holder.user_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", list.get(position).getUid() + "");
                    context.startActivity(intent);
                }
            });
            //去往用户主页
            holder.user_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", list.get(position).getUid() + "");
                    context.startActivity(intent);
                }
            });

        }
    }

    /* 时间戳转日期
     * @param ms
     * @return
     */
    public static Date transForDate(Long ms) {
        if (ms == null) {
            ms = (long) 0;
        }
        long msl = (long) ms * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date temp = null;
        if (ms != null) {
            try {
                String str = sdf.format(msl);
                temp = sdf.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    /**
     * 取消关注
     *
     * @param user_id
     */
    private void quxiao_guanzhu_submit(String user_id, final int position) {
        Map<String, String> params = new HashMap<>();
        if (null != user_id) {
            params.put("uuid", user_id);
        }
        Log.e("取消关注分类ID：", params.toString());
        subscription = Network.getInstance("取消关注用户", context)
                .quxiaoguanzhuta(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "取消关注分类成功：" + result.getCode());
                                list.get(position).setStatus("1");
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "取消关注分类报错：" + error);
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                            }
                        }, context, false));

    }

    private void guanzhu_submit(String id, final int position) {
        Map<String, String> params = new HashMap<>();
        params.put("uuid", id);
        Log.e("关注他：", params.toString());
        subscription = Network.getInstance("关注他", context)
                .guanzhuta(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "关注他成功：" + result.getCode());
                                list.get(position).setStatus("2");
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                                Log.e("123", "关注他报错：" + error);
                            }
                        }, context, false));
    }

    @Override
    public int getAdapterItemViewType(int position) {
        if (list.size() == 0) {
            return EMPTY_VIEW;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getAdapterItemCount() {
        return list.size() > 0 ? list.size() : 1;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
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
        public TextView user_name, time_tv;
        public CircleImageView select_head_image;
        public LinearLayout guanzhu_layout_top, quxiaoguanzhu_layout_top, user_click;
        private TextView title;

        public PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                user_name = (TextView) itemView.findViewById(R.id.user_name);
                time_tv = (TextView) itemView.findViewById(R.id.time_tv);
                select_head_image = (CircleImageView) itemView.findViewById(R.id.select_head_image);
                guanzhu_layout_top = (LinearLayout) itemView.findViewById(R.id.guanzhu_layout_top);
                user_click = (LinearLayout) itemView.findViewById(R.id.user_click);
                quxiaoguanzhu_layout_top = (LinearLayout) itemView.findViewById(R.id.quxiaoguanzhu_layout_top);
                title = itemView.findViewById(R.id.title);
            }
        }
    }
}

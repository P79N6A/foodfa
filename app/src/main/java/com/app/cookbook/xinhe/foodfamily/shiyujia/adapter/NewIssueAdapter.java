package com.app.cookbook.xinhe.foodfamily.shiyujia.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.FastAnswerActivity;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.NewHandpick;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.tencent.stat.StatService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class NewIssueAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<NewHandpick.DataBean> list;
    private Context context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_ITEM = 1;
    private Boolean is_new_page;
    private Handler handler;

    public NewIssueAdapter(List<NewHandpick.DataBean> mlist, Context mcontext, boolean mis_new_page, Handler handler) {
        list = mlist;
        this.handler = handler;
        is_new_page = mis_new_page;
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
        View empty_view = LayoutInflater.from(context).inflate(R.layout.search_empty_layout, parent, false);
        View shidan_view = LayoutInflater.from(context).inflate(R.layout.new_handpick_item, parent, false);
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
        if (view_holder instanceof PurchaseViewHolder) {
            final PurchaseViewHolder holder = (PurchaseViewHolder) view_holder;

            ((PurchaseViewHolder) view_holder).view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });
            final NewHandpick.DataBean dataBean = list.get(position);

            holder.question_content.setText(dataBean.getTitle());

            if ("1".equals(dataBean.getIs_follow())) {
                holder.attention_image.setLiked(true);
                holder.attention_tv.setText("已关注");
                holder.attention_image.setLikeDrawableRes(R.drawable.icon_answer_collect_on);
            } else if ("2".equals(dataBean.getIs_follow())) {
                holder.attention_image.setLiked(false);
                holder.attention_tv.setText("关注问题");
                holder.attention_image.setUnlikeDrawableRes(R.drawable.icon_answer_collect);
            }

            //跳转问题详情页
            holder.question_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Properties prop = new Properties();
                    prop.setProperty("name", "question_content");
                    StatService.trackCustomKVEvent(context, "Newest_details_problem", prop);

                    Intent intent = new Intent(context, FenLeiQuestionDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("wenti_id", dataBean.getId());
                    bundle.putString("flag", "0");
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            holder.attention_image.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                        holder.attention_image.setLiked(false);
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        dataBean.setIs_follow("2");
                    } else {
                        Message msg = new Message();
                        msg.what = 401;
                        msg.arg1 = position;
                        handler.sendMessage(msg);
                        dataBean.setIs_follow("1");
                        holder.attention_tv.setText("已关注");
                    }
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        dataBean.setIs_follow("1");
                    } else {
                        Message msg = new Message();
                        msg.what = 400;
                        msg.arg1 = position;
                        handler.sendMessage(msg);
                        dataBean.setIs_follow("2");
                        holder.attention_tv.setText("关注问题");
                    }
                }
            });
            //回答问题
            holder.answer_issue_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, FastAnswerActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("question_id", dataBean.getId());
                        bundle.putString("is_go_to_detai", "true");
                        bundle.putString("question_title", dataBean.getTitle());
                        intent.putExtras(bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    /**
     * 时间戳转日期
     *
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

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
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
        public TextView question_content;

        //关注问题
        private RelativeLayout attention_layout;
        private LikeButton attention_image;
        private TextView attention_tv;
        //回答问题
        private TextView answer_issue_btn;
        private LinearLayout answer_layout;
        private ImageView answer_image;

        public PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                question_content = (TextView) itemView.findViewById(R.id.question_content);
                attention_layout = itemView.findViewById(R.id.attention_layout);
                attention_image = itemView.findViewById(R.id.attention_image);
                attention_tv = itemView.findViewById(R.id.attention_tv);
                answer_issue_btn = itemView.findViewById(R.id.answer_issue_btn);
                answer_layout = itemView.findViewById(R.id.answer_layout);
                answer_image = itemView.findViewById(R.id.answer_image);
            }
        }
    }
}
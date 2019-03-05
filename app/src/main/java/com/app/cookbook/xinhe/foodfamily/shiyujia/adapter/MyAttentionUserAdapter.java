package com.app.cookbook.xinhe.foodfamily.shiyujia.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.OtherUserActivity;
import com.app.cookbook.xinhe.foodfamily.main.WoGuanZhuActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.WoGuanZhuEntity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAttentionUserAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<WoGuanZhuEntity.DataBean> list;
    private Context context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public MyAttentionUserAdapter(Context mcontext) {
        this.context = mcontext;
    }

    public void setList(List<WoGuanZhuEntity.DataBean> list) {
        this.list = list;
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
        if (view_holder instanceof PurchaseViewHolder) {
            final PurchaseViewHolder holder = (PurchaseViewHolder) view_holder;
            holder.title.setVisibility(View.GONE);

            holder.user_name.setText(list.get(position).getName());

            Glide.with(context.getApplicationContext()).load(list.get(position).getAvatar())
                    .error(R.drawable.touxiang)
                    .into(holder.select_head_image);
            //去往个人中心
            holder.select_head_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", list.get(position).getUid() + "");
                    context.startActivity(intent);
                }
            });
            holder.name_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", list.get(position).getUid() + "");
                    context.startActivity(intent);
                }
            });

            holder.content_tv.setVisibility(View.GONE);
//            if (null != list.get(position).getQuestions()) {
//                holder.content_tv.setVisibility(View.VISIBLE);
////                holder.quiz_type.setText("最新问题");
//                holder.quiz_type.setVisibility(View.GONE);
//                holder.content_tv.setText(list.get(position).getQuestions().getTitle());
//            } else {
//                holder.content_tv.setVisibility(View.GONE);
//                holder.quiz_type.setVisibility(View.GONE);
////                holder.quiz_type.setText("暂无问题");
//            }

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
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getPosition();
                        mOnItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
            }
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

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


}

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.GuanZhuBiaoQianEntity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.LabelDetailsActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.FriendAttentionLabel;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by shiyujia02 on 2018/1/24.
 */

public class FriendAttentionLabelAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<FriendAttentionLabel.DataBean> list;
    private Context context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private boolean ista;

    private String titleName;

    public FriendAttentionLabelAdapter(List<FriendAttentionLabel.DataBean> mlist, Context mcontext, boolean mista) {
        list = mlist;
        ista = mista;
        context = mcontext;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
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
        View empty_view;
        if (ista) {
            empty_view = LayoutInflater.from(context).inflate(R.layout.ta_biaoqian_empty_layout, parent, false);

        } else {
            empty_view = LayoutInflater.from(context).inflate(R.layout.biaoqian_empty_layout, parent, false);

        }
        View shidan_view = LayoutInflater.from(context).inflate(R.layout.gaunzhubiaoqian_item, parent, false);
        RecyclerView.ViewHolder holder = null;
        if (viewType == EMPTY_VIEW) {
            holder = new EmptyViewHolder(empty_view, false);
        } else {
            holder = new PurchaseViewHolder(shidan_view, true);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, final int position, boolean isItem) {
        if (view_holder instanceof PurchaseViewHolder) {
            PurchaseViewHolder holder = (PurchaseViewHolder) view_holder;
            Glide.with(context).load(list.get(position).getClass_data().getPath())
                    .error(R.drawable.touxiang)
                    .into(holder.touxiang_image);

            if (position != list.size()) {
                ((PurchaseViewHolder) view_holder).view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(v, position);
                        }
                    }
                });
                holder.biaoqian_name.setText(list.get(position).getClass_data().getTitle());

                holder.touxiang_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, LabelDetailsActivity.class);
                        intent.putExtra("id", list.get(position).getClass_id());
                        context.startActivity(intent);
                    }
                });
                holder.guanzhu_number.setText(list.get(position).getUser_count() + "关注");

                holder.wenti_numbers.setText(list.get(position).getContent_count() + "内容");

                if (null != list.get(position).getClass_data()) {
                    if (null != list.get(position).getClass_data().getTitle()) {
                        holder.question_title.setText(list.get(position).getClass_data().getTitle());
                    } else {
                        holder.question_title.setText("");
                    }
                } else {
                    holder.question_title.setText("");
                }

                //跳转分类
                holder.fenlei_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, LabelDetailsActivity.class);
                        intent.putExtra("id", list.get(position).getClass_id());
                        context.startActivity(intent);
                    }
                });

                //跳转问题详情
                holder.question_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list.get(position).getClass_id() != null) {
                            Intent intent = new Intent(context, FenLeiQuestionDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("wenti_id", list.get(position).getClass_data().getId());
                            intent.putExtras(bundle);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
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

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setData(List<FriendAttentionLabel.DataBean> list) {
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
        public TextView biaoqian_name, guanzhu_number, wenti_numbers, question_title;
        public CircleImageView touxiang_image;
        public RelativeLayout fenlei_layout;
        public LinearLayout question_layout;

        public PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                biaoqian_name = (TextView) itemView.findViewById(R.id.biaoqian_name);
                touxiang_image = (CircleImageView) itemView.findViewById(R.id.touxiang_image);
                guanzhu_number = (TextView) itemView.findViewById(R.id.guanzhu_number);
                wenti_numbers = (TextView) itemView.findViewById(R.id.wenti_numbers);
                question_title = (TextView) itemView.findViewById(R.id.question_title);
                fenlei_layout = (RelativeLayout) itemView.findViewById(R.id.fenlei_layout);
                question_layout = (LinearLayout) itemView.findViewById(R.id.question_layout);
            }
        }
    }

}

package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.AnserActivity;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.OtherUserActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.MineGuanZhuQuestionEntity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.util.DataTypeAdaptor;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by shiyujia02 on 2018/1/16.
 */

public class GuanZhuWentiAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<MineGuanZhuQuestionEntity.DataBean> list;
    private Context context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private boolean ista;

    private String titleName;

    public GuanZhuWentiAdapter(List<MineGuanZhuQuestionEntity.DataBean> mlist, Context mcontext, boolean mista) {
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
            empty_view = LayoutInflater.from(context).inflate(R.layout.mine_ta_guanzhuwenti_empty_layout, parent, false);
        } else {
            empty_view = LayoutInflater.from(context).inflate(R.layout.mine_guanzhuwenti_empty_layout, parent, false);
        }
        View shidan_view = LayoutInflater.from(context).inflate(R.layout.wenti_layout_item_two, parent, false);
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
            PurchaseViewHolder holder = (PurchaseViewHolder) view_holder;

            ((PurchaseViewHolder) view_holder).view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });

            if (list.get(position).getCountUsers() == 0) {
                holder.user_name.setVisibility(View.GONE);
                holder.select_head_image.setVisibility(View.GONE);
            } else {
                holder.user_name.setVisibility(View.VISIBLE);
                holder.select_head_image.setVisibility(View.VISIBLE);
            }
            holder.question_title.setVisibility(View.GONE);

            if (null == list.get(position).getTitle() || "".equals(list.get(position).getTitle())) {
                holder.question_content.setVisibility(View.GONE);
            } else {
                holder.question_content.setVisibility(View.VISIBLE);
                holder.question_content.setText(list.get(position).getTitle());
            }


            if (null != list.get(position).getAnswer_data()) {
                if (list.get(position).getAnswer_data().getImg_data().size() > 0) {
                    holder.answer_img.setVisibility(View.VISIBLE);
                    //默认只显示头一张图片
                    Glide.with(context)
                            .load(list.get(position).getAnswer_data().getImg_data().get(0).getPath())
                            .asBitmap()
                            .error(R.drawable.touxiang)
                            .into(holder.answer_img);


                } else {
                    holder.answer_img.setVisibility(View.GONE);
                }

                if (null == list.get(position).getAnswer_data().getContent_remove()
                        || "".equals(list.get(position).getAnswer_data().getContent_remove())) {
                    holder.question_answer.setVisibility(View.GONE);
                } else {
                    holder.question_answer.setVisibility(View.VISIBLE);
                    Log.e("数据库附近开始第六届", list.get(position).getAnswer_data().getContent_remove());
                    holder.question_answer.setText(
                            Html.fromHtml(list.get(position).getAnswer_data().getContent_remove()));
                }
                Glide.with(context).load(list.get(position).getAnswer_data().getUsers().getAvatar())
                        .asBitmap()
                        .error(R.drawable.touxiang)
                        .into(holder.select_head_image);
            } else {
                holder.question_answer.setVisibility(View.GONE);
                holder.answer_img.setVisibility(View.GONE);
            }

            if (list.get(position).getCountUsers() == 0) {
                holder.answer_numbers.setText("暂无回答");
                holder.select_head_image.setVisibility(View.INVISIBLE);
                holder.user_name.setVisibility(View.INVISIBLE);
            } else {

                holder.answer_numbers.setText(list.get(position).getCountUsers() + "回答");
                holder.select_head_image.setVisibility(View.VISIBLE);
                holder.user_name.setVisibility(View.VISIBLE);
            }

            if (null != list.get(position).getAnswer_data()) {
                if (null != list.get(position).getAnswer_data().getUsers()) {
                    if (null != list.get(position).getAnswer_data().getUsers().getUname()) {
                        holder.user_name.setText(list.get(position).getAnswer_data().getUsers().getUname());

                    }
                }
            }

            holder.answer_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != list.get(position).getAnswer_data()) {
                        Intent intent = new Intent(context, AnserActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("answer_id", list.get(position).getAnswer_data().getId() + "");
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);


                    }
                }
            });
            //跳转问题详情
            holder.question_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FenLeiQuestionDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("wenti_id", list.get(position).getId() + "");
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });
            //跳转答案详情
            holder.question_answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != list.get(position).getAnswer_data()) {
                        Intent intent = new Intent(context, AnserActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("answer_id", list.get(position).getAnswer_data().getId() + "");
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }
                }
            });

            //跳转他人主页
            holder.user_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != list.get(position).getAnswer_data()) {
                        Intent intent = new Intent(context, FriendPageActivity.class);
                        intent.putExtra("UserId", list.get(position).getAnswer_data().getUid() + "");
                        context.startActivity(intent);
                    }


                }
            });
        } else if (view_holder instanceof EmptyViewHolder) {
            EmptyViewHolder holder = (EmptyViewHolder) view_holder;
        }
    }

    private static Gson buildGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapterFactory(DataTypeAdaptor.FACTORY);
        return gsonBuilder.create();
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

    public void setData(List<MineGuanZhuQuestionEntity.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_top;
        public View view;
        private TextView title;

        public EmptyViewHolder(View item_view, boolean isItem) {
            super(item_view);
            if (isItem) {
                this.view = item_view;
                img_top = (ImageView) item_view
                        .findViewById(R.id.img_top);
                title = item_view.findViewById(R.id.title);
            }
        }
    }

    public class PurchaseViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView question_title, question_content, question_answer, user_name, answer_numbers;
        public CircleImageView select_head_image;
        public RelativeLayout touxiang_layout;
        public LinearLayout user_layout;
        private TextView title;
        private RoundedImageView answer_img;

        public PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                user_layout = (LinearLayout) itemView.findViewById(R.id.user_layout);
                question_title = (TextView) itemView.findViewById(R.id.question_title);
                question_content = (TextView) itemView.findViewById(R.id.question_content);
                question_answer = (TextView) itemView.findViewById(R.id.question_answer);
                select_head_image = (CircleImageView) itemView.findViewById(R.id.select_head_image);
                user_name = (TextView) itemView.findViewById(R.id.user_name);
                answer_numbers = (TextView) itemView.findViewById(R.id.answer_numbers);
                touxiang_layout = (RelativeLayout) itemView.findViewById(R.id.touxiang_layout);
                title = itemView.findViewById(R.id.title);
                answer_img = itemView.findViewById(R.id.answer_img);
            }
        }
    }
}

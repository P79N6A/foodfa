package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.AnserActivity;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.OtherUserActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.ZuiZinAnswerEntity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.util.DateUtils;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by shiyujia02 on 2018/1/25.
 */

public class ZuiXinHuidaAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<ZuiZinAnswerEntity.DataBean> list;
    private Context context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_ITEM = 1;


    public ZuiXinHuidaAdapter(List<ZuiZinAnswerEntity.DataBean> mlist, Context mcontext) {
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
        View empty_view = LayoutInflater.from(context).inflate(R.layout.new_answer_empty_layout, parent, false);
        View shidan_view = LayoutInflater.from(context).inflate(R.layout.new_question_answer_item, parent, false);
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
        PurchaseViewHolder holder;
        if (view_holder instanceof PurchaseViewHolder) {
            holder = (PurchaseViewHolder) view_holder;
            if (position == 0) {
                holder.title.setVisibility(View.VISIBLE);
            } else {
                holder.title.setVisibility(View.GONE);
            }
            ((PurchaseViewHolder) view_holder).view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });

            ((PurchaseViewHolder) view_holder).view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            Glide.with(context.getApplicationContext()).load(list.get(position).getUser_data().getAvatar())
                    .error(R.drawable.touxiang)
                    .into(holder.select_head_image);


            //个人主页
            holder.select_head_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", list.get(position).getUid() + "");
                    context.startActivity(intent);
                }
            });

            if (!TextUtils.isEmpty(list.get(position).getContent_remove())) {
                holder.answer_content_tv.setVisibility(View.VISIBLE);
                holder.answer_content_tv.setText(list.get(position).getContent_remove());
            } else {
                holder.answer_content_tv.setVisibility(View.GONE);
            }

            holder.user_name.setText(list.get(position).getUser_data().getName());

            if (!"0".equals(list.get(position).getImg_count())) {
                holder.image_tv.setVisibility(View.VISIBLE);
            } else {
                holder.image_tv.setVisibility(View.GONE);
            }

            holder.question_content_tv.setText(list.get(position).getQuestion_data().getTitle());

            //给视图添加长按事件
            holder.image_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            holder.question_content_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            holder.question_content_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FenLeiQuestionDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("wenti_id", list.get(position).getPid() + "");
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            holder.answer_content_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            holder.answer_content_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AnserActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("answer_id", list.get(position).getId() + "");
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            holder.user_name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            if (list.get(position).getCreated_at() != null) {
                holder.time_tv.setText(DateUtils.getTimeFormatText(transForDate(Long.valueOf(list.get(position).getCreated_at()))));
            }

        }


    }

    private XiTongAdapter.OnItemLongClickListener onItemLongClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemLongClickListener(XiTongAdapter.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
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

    public void setData(List<ZuiZinAnswerEntity.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public EmptyViewHolder(View item_view, boolean isItem) {
            super(item_view);
            if (isItem) {
                this.view = item_view;

            }
        }
    }

    public class PurchaseViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView question_content_tv, image_tv, answer_content_tv, user_name, time_tv;
        public CircleImageView select_head_image;
        private TextView title;

        public PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                question_content_tv = (TextView) itemView.findViewById(R.id.question_content_tv);
                user_name = (TextView) itemView.findViewById(R.id.user_name);
                select_head_image = (CircleImageView) itemView.findViewById(R.id.select_head_image);
                image_tv = (TextView) itemView.findViewById(R.id.image_tv);
                answer_content_tv = (TextView) itemView.findViewById(R.id.answer_content_tv);
                time_tv = (TextView) itemView.findViewById(R.id.time_tv);
                title = itemView.findViewById(R.id.title);
            }
        }
    }
}

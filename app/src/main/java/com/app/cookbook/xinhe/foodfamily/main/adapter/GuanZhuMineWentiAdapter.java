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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.OtherUserActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.GuanZhuWentiDate;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.util.DateUtils;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by shiyujia02 on 2018/3/24.
 */

public class GuanZhuMineWentiAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<GuanZhuWentiDate.Guanzhuen> list;
    private Context context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_QUESTION = 3;

    private boolean ista;

    public GuanZhuMineWentiAdapter(List<GuanZhuWentiDate.Guanzhuen> mlist, Context mcontext, boolean mista) {
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

    private RecyclerView.ViewHolder getViewHolderByViewType(int viewType, ViewGroup parent) {
        View empty_view;
        if (ista) {
            empty_view = LayoutInflater.from(context).inflate(R.layout.ta_guanzhuwenti_empty_layout, parent, false);
        } else {
            empty_view = LayoutInflater.from(context).inflate(R.layout.guanzhuwenti_empty_layout, parent, false);
        }
        View shidan_view = LayoutInflater.from(context).inflate(R.layout.guanzhu_mine_item, parent, false);
        View question_view = LayoutInflater.from(context).inflate(R.layout.guanzhu_mine_question_item, parent, false);
        RecyclerView.ViewHolder holder = null;
        if (viewType == EMPTY_VIEW) {
            holder = new EmptyViewHolder(empty_view, true);
        } else if (viewType == TYPE_QUESTION) {
            holder = new QuestionPurchaseViewHolder(question_view, true);
        } else {
            holder = new PurchaseViewHolder(shidan_view, true);
        }
        return holder;
    }

    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, final int position, boolean isItem) {
        if (getAdapterItemViewType(position) == EMPTY_VIEW) {

        } else if (getAdapterItemViewType(position) == TYPE_ITEM) {//关注我的人
            PurchaseViewHolder holder = (PurchaseViewHolder) view_holder;

            final GuanZhuWentiDate.Guanzhuen item = list.get(position);


            if (position == 0) {
                holder.title.setVisibility(View.VISIBLE);
            } else {
                holder.title.setVisibility(View.GONE);
            }
            ((PurchaseViewHolder) view_holder).view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });

            if (null != item.getUser_name()) {
                if (null != item.getUser_name()) {
                    holder.user_name.setText(item.getUser_name());
                }
            }

            Glide.with(context).load(item.getUser_avatar())
                    .error(R.drawable.touxiang)
                    .into(holder.select_head_image);

            holder.time_tv.setText(DateUtils.getTimeFormatText(transForDate(Long.valueOf(item.getCreated_at()))));

            //去往个人主页
            holder.select_head_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", item.getUser_id() + "");
                    context.startActivity(intent);
                }
            });
            //去往个人主页
            holder.top_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", item.getUser_id() + "");
                    context.startActivity(intent);
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
            holder.time_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
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

        } else {//关注我的问题
            Log.e("sfskdf", "jsklfd");
            QuestionPurchaseViewHolder questionPurchaseViewHolder = (QuestionPurchaseViewHolder) view_holder;

            final GuanZhuWentiDate.Guanzhuen item = list.get(position);

            if (position == 0) {
                questionPurchaseViewHolder.title.setVisibility(View.VISIBLE);
            } else {
                questionPurchaseViewHolder.title.setVisibility(View.GONE);
            }
            ((QuestionPurchaseViewHolder) view_holder).view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            if (null != item.getUser_name()) {
                if (null != item.getUser_name()) {
                    questionPurchaseViewHolder.user_name.setText(item.getUser_name());
                }
            }
            Glide.with(context).load(item.getUser_avatar())
                    // .centerCrop()
                    //.placeholder(R.drawable.touxiang)
                    .error(R.drawable.touxiang)
                    .into(questionPurchaseViewHolder.select_head_image);

            questionPurchaseViewHolder.time_tv.setText(DateUtils.getTimeFormatText(transForDate(Long.valueOf(item.getCreated_at()))));

            questionPurchaseViewHolder.content_tv.setText(item.getTitle());
            //去往问题详情页
            questionPurchaseViewHolder.content_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(item.getQuestion_is_existence())) {
                        Intent intent = new Intent(context, FenLeiQuestionDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("wenti_id", item.getQuestion_id() + "");
                        intent.putExtras(bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "问题已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            //去往个人主页
            questionPurchaseViewHolder.select_head_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", item.getUser_id() + "");
                    context.startActivity(intent);
                }
            });

            //去往个人主页
            questionPurchaseViewHolder.top_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", item.getUser_id() + "");
                    context.startActivity(intent);
                }
            });
            questionPurchaseViewHolder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            questionPurchaseViewHolder.content_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            questionPurchaseViewHolder.time_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            questionPurchaseViewHolder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            questionPurchaseViewHolder.user_name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
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
        } else {
            if (list.get(position).getType() == 1) {//显示关注我的人
                return TYPE_ITEM;
            } else {//显示关注问题
                return TYPE_QUESTION;
            }
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

    public void setData(List<GuanZhuWentiDate.Guanzhuen> list) {
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
        public CircleImageView select_head_image;
        public TextView user_name, time_tv, content_tv;
        private TextView title;
        private RelativeLayout top_rel;

        public PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                select_head_image = (CircleImageView) itemView.findViewById(R.id.select_head_image);
                user_name = (TextView) itemView.findViewById(R.id.user_name);
                time_tv = (TextView) itemView.findViewById(R.id.time_tv);
                content_tv = (TextView) itemView.findViewById(R.id.content_tv);
                title = itemView.findViewById(R.id.title);
                top_rel = itemView.findViewById(R.id.top_rel);
            }
        }
    }

    public class QuestionPurchaseViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public CircleImageView select_head_image;
        public TextView user_name, time_tv, content_tv, image_tv;
        private TextView title;
        private RelativeLayout top_rel;

        public QuestionPurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                select_head_image = (CircleImageView) itemView.findViewById(R.id.select_head_image);
                user_name = (TextView) itemView.findViewById(R.id.user_name);
                time_tv = (TextView) itemView.findViewById(R.id.time_tv);
                content_tv = (TextView) itemView.findViewById(R.id.content_tv);
                image_tv = (TextView) itemView.findViewById(R.id.image_tv);
                title = itemView.findViewById(R.id.title);
                top_rel = itemView.findViewById(R.id.top_rel);
            }
        }
    }

}

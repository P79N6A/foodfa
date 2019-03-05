package com.app.cookbook.xinhe.foodfamily.shiyujia.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.ImageTextDetailActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.ImageTextComment;
import com.app.cookbook.xinhe.foodfamily.util.CircleImageView;
import com.app.cookbook.xinhe.foodfamily.util.FormatCurrentData;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.bumptech.glide.Glide;

import java.util.List;

public class ImageTextDetailAdapter extends BaseRecyclerAdapter<ImageTextDetailAdapter.Holder> {

    private Context context;
    private LayoutInflater inflater;
    private List<ImageTextComment.DataBean> imageTextComments;
    private Handler handler;

    public ImageTextDetailAdapter(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        inflater = LayoutInflater.from(context);
    }

    public void setImageTextComments(List<ImageTextComment.DataBean> imageTextComments) {
        this.imageTextComments = imageTextComments;
    }

    @Override
    public Holder getViewHolder(View view) {
        return new Holder(view);

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = inflater.inflate(R.layout.image_text_detail_item, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position, boolean isItem) {
        ImageTextComment.DataBean dataBean = imageTextComments.get(position);

        if ("1".equals(dataBean.getIs_thumbs())) {
            holder.comment_im.setImageResource(R.drawable.icon_answer_collect_on);
        } else if ("2".equals(dataBean.getIs_thumbs())) {
            holder.comment_im.setImageResource(R.drawable.icon_answer_collect);
        }
        if ("2".equals(dataBean.getIs_existence())) {
            holder.delete_btn.setVisibility(View.INVISIBLE);
        } else if ("1".equals(dataBean.getIs_existence())) {
            holder.delete_btn.setVisibility(View.VISIBLE);
        }
        Glide.with(context)
                .load(dataBean.getUser_data().getAvatar())
                .error(R.drawable.touxiang)
                .into(holder.user_head);
        holder.user_name.setText(dataBean.getUser_data().getName());
        holder.comment_time.setText(FormatCurrentData.getTimeRange(Long.valueOf(dataBean.getCreated_at())));
        holder.zan_number.setText(dataBean.getThumbs());
        holder.comment_content.setText(dataBean.getContent());
        if (dataBean.getSon_data() != null) {
            holder.reply_layout.setVisibility(View.VISIBLE);
            holder.reply_content.setText(dataBean.getSon_data().getUser_data().getName() + ": " + dataBean.getSon_data().getContent());
            holder.reply_number.setText("共" + dataBean.getSon_count() + "条回复 >");
        } else {
            holder.reply_layout.setVisibility(View.GONE);
        }

        holder.user_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 400;
                msg.arg1 = position;
                handler.sendMessage(msg);
            }
        });
        holder.reply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 401;
                msg.arg1 = position;
                handler.sendMessage(msg);
            }
        });
        holder.zan_comm_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                } else {
                    if ("2".equals(dataBean.getIs_thumbs())) {
                        //点赞
                        holder.comment_im.setImageResource(R.drawable.icon_answer_collect_on);
                    } else if ("1".equals(dataBean.getIs_thumbs())) {
                        //取消点赞
                        holder.comment_im.setImageResource(R.drawable.icon_answer_collect);
                    }
                    Message msg = new Message();
                    msg.what = 402;
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                }
            }
        });
        holder.reply_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 403;
                msg.arg1 = position;
                handler.sendMessage(msg);
            }
        });
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 404;
                msg.arg1 = position;
                handler.sendMessage(msg);
            }
        });

        //条目点击时间
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

    }

    @Override
    public int getAdapterItemCount() {
        return imageTextComments == null ? 0 : imageTextComments.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        //条目布局
        private RelativeLayout comm_layoutt;
        private RelativeLayout user_layout;
        private CircleImageView user_head;
        private TextView user_name;
        private TextView comment_time;
        private ImageView reply_btn;
        private LinearLayout zan_comm_layout;
        private ImageView comment_im;
        private TextView zan_number;
        private TextView comment_content;
        private LinearLayout reply_layout;
        private TextView reply_content;
        private TextView reply_number;
        private ImageView delete_btn;

        public Holder(View itemView) {
            super(itemView);
            //条目布局
            comm_layoutt = itemView.findViewById(R.id.comm_layoutt);
            user_layout = itemView.findViewById(R.id.user_layout);
            user_head = itemView.findViewById(R.id.user_head);
            user_name = itemView.findViewById(R.id.user_name);
            comment_time = itemView.findViewById(R.id.comment_time);
            reply_btn = itemView.findViewById(R.id.reply_btn);
            zan_comm_layout = itemView.findViewById(R.id.zan_comm_layout);
            comment_im = itemView.findViewById(R.id.comment_im);
            zan_number = itemView.findViewById(R.id.zan_number);
            comment_content = itemView.findViewById(R.id.comment_content);
            reply_layout = itemView.findViewById(R.id.reply_layout);
            reply_content = itemView.findViewById(R.id.reply_content);
            reply_number = itemView.findViewById(R.id.reply_number);
            delete_btn = itemView.findViewById(R.id.delete_btn);
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

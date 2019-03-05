package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
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

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.entity.AnswerDate;
import com.app.cookbook.xinhe.foodfamily.util.FormatCurrentData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 18030150 on 2018/4/8.
 */

public class HotCommentAdapter extends RecyclerView.Adapter<HotCommentAdapter.Holder> {

    private Context context;
    private LayoutInflater inflater;
    private List<AnswerDate.HotComment> answerListOnes = new ArrayList<>();
    private Handler handler;

    public HotCommentAdapter(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.inflater = LayoutInflater.from(context);
    }

    public void setAnswerListOnes(List<AnswerDate.HotComment> answerListOnes) {
        this.answerListOnes = answerListOnes;
    }

    @Override
    public HotCommentAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.answer_comment_item, null);
        HotCommentAdapter.Holder holder = new HotCommentAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final HotCommentAdapter.Holder holder, final int position) {
        AnswerDate.HotComment listOne = answerListOnes.get(position);
        holder.user_name.setText(listOne.getName());
        holder.tv_time.setText(FormatCurrentData.getTimeRange(Long.valueOf(listOne.getComment_created_at())));
        holder.answer_content.setText(listOne.getComment_content());
        if (!"0".equals(listOne.getSon_count()) && listOne.getSon_count() != null) {
            holder.replies_num.setVisibility(View.VISIBLE);
            holder.replies_num.setText("查看" + listOne.getSon_count() + "条回复>");
        } else {
            holder.replies_num.setVisibility(View.GONE);
        }

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.zan_number.getLayoutParams();
        String comment_thumbs = listOne.getComment_thumbs();
        if (listOne.getComment_thumbs().length() == 1) {
            lp.setMargins(0, 0, 35, 0);
            holder.zan_number.setLayoutParams(lp);
            holder.zan_number.setText(comment_thumbs);
        } else if (listOne.getComment_thumbs().length() == 2) {
            lp.setMargins(0, 0, 17, 0);
            holder.zan_number.setLayoutParams(lp);
            holder.zan_number.setText(comment_thumbs);
        } else if (listOne.getComment_thumbs().length() == 3) {
            lp.setMargins(0, 0, 0, 0);
            holder.zan_number.setLayoutParams(lp);
            holder.zan_number.setText(comment_thumbs);
        } else if (listOne.getComment_thumbs().length() == 4) {
            lp.setMargins(0, 0, 17, 0);
            holder.zan_number.setLayoutParams(lp);
            comment_thumbs = listOne.getComment_thumbs();
            comment_thumbs = comment_thumbs.substring(0, 1);
            holder.zan_number.setText(comment_thumbs + "k");
        } else if (listOne.getComment_thumbs().length() == 5) {
            lp.setMargins(0, 0, 0, 0);
            holder.zan_number.setLayoutParams(lp);
            comment_thumbs = listOne.getComment_thumbs();
            comment_thumbs = comment_thumbs.substring(0, 2);
            holder.zan_number.setText(comment_thumbs + "k");
        }

        if ("1".equals(listOne.getIs_thumbs())) {
            holder.zan_state.setImageResource(R.drawable.icon_answer_collect_on);
        } else if ("2".equals(listOne.getIs_thumbs())) {
            holder.zan_state.setImageResource(R.drawable.icon_answer_collect);
        }
        Glide.with(context)
                .load(listOne.getAvatar())
                .error(R.drawable.touxiang)
                .into(holder.user_logo);
        holder.zan_alyout.setOnClickListener(new View.OnClickListener() {
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
        holder.answer_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 401;
                msg.arg1 = position;
                handler.sendMessage(msg);
            }
        });

        holder.user_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 402;
                msg.arg1 = position;
                handler.sendMessage(msg);
            }
        });
        holder.user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 402;
                msg.arg1 = position;
                handler.sendMessage(msg);
            }
        });

        //条目点击时间
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

    @Override
    public int getItemCount() {
        return answerListOnes == null ? 0 : answerListOnes.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private ImageView user_logo;
        private TextView user_name;
        private TextView tv_time;
        private TextView reply_btn;
        private TextView zan_number;
        private ImageView zan_state;
        private TextView answer_content;
        private RelativeLayout zan_alyout;
        private View line;
        //回复数
        private TextView replies_num;

        public Holder(View itemView) {
            super(itemView);
            user_logo = itemView.findViewById(R.id.user_logo);
            user_name = itemView.findViewById(R.id.user_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            reply_btn = itemView.findViewById(R.id.reply_btn);
            zan_number = itemView.findViewById(R.id.zan_number);
            zan_state = itemView.findViewById(R.id.zan_state);
            answer_content = itemView.findViewById(R.id.answer_content);
            zan_alyout = itemView.findViewById(R.id.zan_alyout);
            line = itemView.findViewById(R.id.line);
            replies_num = itemView.findViewById(R.id.replies_num);
        }
    }

    private HotCommentAdapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(HotCommentAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

}

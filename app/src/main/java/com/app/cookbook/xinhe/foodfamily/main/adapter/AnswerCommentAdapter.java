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
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.entity.AnswerListOne;
import com.app.cookbook.xinhe.foodfamily.util.FormatCurrentData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 18030150 on 2018/3/26.
 */

public class AnswerCommentAdapter extends RecyclerView.Adapter<AnswerCommentAdapter.Holder> {


    private Context context;
    private LayoutInflater inflater;
    private List<AnswerListOne> answerListOnes = new ArrayList<>();
    private Handler handler;

    public AnswerCommentAdapter(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.inflater = LayoutInflater.from(context);
    }

    public void setAnswerListOnes(List<AnswerListOne> answerListOnes) {
        this.answerListOnes = answerListOnes;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.answer_comment_item, null);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        AnswerListOne listOne = answerListOnes.get(position);
        holder.user_name.setText(listOne.getUser_name());
        holder.tv_time.setText(FormatCurrentData.getTimeRange(Long.valueOf(listOne.getCreated_at())));
        holder.answer_content.setText(listOne.getComment_content());
        holder.zan_number.setText(listOne.getComment_thumbs());
        if ("1".equals(listOne.getIs_thumbs())) {
            holder.zan_state.setImageResource(R.drawable.dianzan_red);
        } else if ("2".equals(listOne.getIs_thumbs())) {
            holder.zan_state.setImageResource(R.drawable.question_dianzan);
        }
        Glide.with(context)
                .load(listOne.getUser_avatar())
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
        private LinearLayout zan_alyout;

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

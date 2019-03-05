package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.AnswerReplyActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.SonDataBean;
import com.app.cookbook.xinhe.foodfamily.util.FormatCurrentData;

import java.util.List;

/**
 * Created by 18030150 on 2018/3/28.
 */

public class CommentChildAdapter extends RecyclerView.Adapter<CommentChildAdapter.Holder> {

    private Context context;
    private LayoutInflater inflater;
    private List<SonDataBean> sonDataBean;
    private String son_count;
    private String answer_id;
    private String Comment_id;
    private String Users_avatar;
    private String Users_name;
    private String Comment_content;
    private String Created_at;
    private String Comment_thumbs;
    private String Is_thumbs;
    private String Users_id;
    private Handler handler;

    public CommentChildAdapter(Context context, String son_count, String answer_id
            , String Comment_id, String Users_avatar, String Users_name, String Comment_content,
                               String Created_at, String Comment_thumbs, String Is_thumbs, String Users_id, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.son_count = son_count;
        this.answer_id = answer_id;
        this.Comment_id = Comment_id;
        this.Users_avatar = Users_avatar;
        this.Users_name = Users_name;
        this.Comment_content = Comment_content;
        this.Comment_thumbs = Comment_thumbs;
        this.Created_at = Created_at;
        this.Is_thumbs = Is_thumbs;
        this.Users_id = Users_id;
        this.inflater = LayoutInflater.from(context);
    }

    public void setSonDataBean(List<SonDataBean> sonDataBean) {
        this.sonDataBean = sonDataBean;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.comment_child_adapter, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        final SonDataBean sonData = sonDataBean.get(position);
        String userName = sonData.getParent_users_name();
        String CommentContent = sonData.getComment_content();
        holder.comment_user_name.setText(userName + ":");
        if (!TextUtils.isEmpty(sonData.getComment_crated_at())) {
            holder.comment_time.setText(FormatCurrentData.getTimeRange(Long.valueOf(sonData.getComment_crated_at())));
        }
        holder.reply_content.setText(CommentContent);
//        final String content = userName + ":" + CommentContent;
//        holder.reply_content.setText(Textutil.matcherSearchText(R.color.color_5377B3, userName + ":" + CommentContent, userName + ":"));
//        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
//        ssb.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_5377B3)),
//                0, userName.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        holder.reply_content.setText(ssb);

//        if (2 < Integer.valueOf(son_count)) {
//            if (sonDataBean.size() == position + 1) {
//                holder.more_reply.setVisibility(View.VISIBLE);
//                holder.more_reply.setText("共" + son_count + "条回复 >");
//            }
//        } else {
//            holder.more_reply.setVisibility(View.GONE);
//        }
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
//        holder.more_reply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, AnswerReplyActivity.class);
//                intent.putExtra("answer_id", answer_id);
//                intent.putExtra("original_id", Comment_id);
//                intent.putExtra("image", Users_avatar);
//                intent.putExtra("name", Users_name);
//                intent.putExtra("content", Comment_content);
//                intent.putExtra("time", Created_at);
//                intent.putExtra("thumbs", Comment_thumbs);
//                intent.putExtra("isthumbs", Is_thumbs);
//                intent.putExtra("user_id", Users_id);
//                intent.putExtra("mesComm", "1");
//                intent.putExtra("isCheck", "1");
//                context.startActivity(intent);
//
//            }
//        });
        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AnswerReplyActivity.class);
                intent.putExtra("answer_id", answer_id);
                intent.putExtra("original_id", Comment_id);
                intent.putExtra("image", Users_avatar);
                intent.putExtra("name", Users_name);
                intent.putExtra("content", Comment_content);
                intent.putExtra("time", Created_at);
                intent.putExtra("thumbs", Comment_thumbs);
                intent.putExtra("isthumbs", Is_thumbs);
                intent.putExtra("user_id", Users_id);
                intent.putExtra("mesComm", "1");
                intent.putExtra("isCheck", "1");
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return sonDataBean == null ? 0 : sonDataBean.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        //评论者名称
        private TextView comment_user_name;
        //评论时间
        private TextView comment_time;
        //评论内容
        private TextView reply_content;
        //        private TextView more_reply;
        private LinearLayout item_layout;

        public Holder(View itemView) {
            super(itemView);
            comment_user_name = itemView.findViewById(R.id.comment_user_name);
            comment_time = itemView.findViewById(R.id.comment_time);
            reply_content = itemView.findViewById(R.id.reply_content);
//            more_reply = itemView.findViewById(R.id.more_reply);
            item_layout = itemView.findViewById(R.id.item_layout);
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

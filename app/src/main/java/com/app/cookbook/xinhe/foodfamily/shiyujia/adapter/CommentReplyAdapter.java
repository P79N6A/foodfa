package com.app.cookbook.xinhe.foodfamily.shiyujia.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.adapter.AnswerCommentAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.CommentLeveTwoData;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.CommentReply;
import com.app.cookbook.xinhe.foodfamily.util.FormatCurrentData;
import com.bumptech.glide.Glide;

import java.util.List;

public class CommentReplyAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<CommentReply.DataBean> commentLeveTwos;
    private Handler handler;
    private boolean ista;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private String more;

    public CommentReplyAdapter(Context mcontext, Handler handler, boolean mista) {
        this.context = mcontext;
        this.handler = handler;
        this.ista = mista;
        this.inflater = LayoutInflater.from(context);
    }

    public void isMore(String more) {
        this.more = more;
    }

    public void setCommentLeveTwos(List<CommentReply.DataBean> commentLeveTwos) {
        this.commentLeveTwos = commentLeveTwos;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        Holder holder = new Holder(view, false);
        return holder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        RecyclerView.ViewHolder holder = getViewHolderByViewType(viewType, parent);
        return holder;
    }

    private RecyclerView.ViewHolder getViewHolderByViewType(int viewType, ViewGroup parent) {
        View empty_view = LayoutInflater.from(context).inflate(R.layout.message_comment_empty_layout, parent, false);
        View shidan_view = LayoutInflater.from(context).inflate(R.layout.comment_reply_item, parent, false);
        RecyclerView.ViewHolder holder = null;
        if (viewType == EMPTY_VIEW) {
            holder = new EmptyViewHolder(empty_view, true);
        } else {
            holder = new Holder(shidan_view, true);

        }
        return holder;
    }

    String type = "0";

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position, boolean isItem) {
        if (holder instanceof Holder) {
            Holder holder1 = (Holder) holder;
            CommentReply.DataBean dataBean = commentLeveTwos.get(position);
            if (position == 0) {
                if ("1".equals(more)) {
                    holder1.tv_more.setVisibility(View.VISIBLE);
                } else if ("2".equals(more)) {
                    holder1.tv_more.setVisibility(View.GONE);
                }
                if ("2".equals(dataBean.getIs_existence())) {
                    holder1.delete_btn.setVisibility(View.INVISIBLE);
                } else if ("1".equals(dataBean.getIs_existence())) {
                    holder1.delete_btn.setVisibility(View.VISIBLE);
                }
                holder1.item_main_layout.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder1.line_layout.setVisibility(View.GONE);
                holder1.user_name.setText(dataBean.getUser_data().getName());
                holder1.tv_time.setText(FormatCurrentData.getTimeRange(Long.valueOf(dataBean.getCreated_at())));
                holder1.answer_content.setText(dataBean.getContent());

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder1.zan_number.getLayoutParams();
                String comment_thumbs = dataBean.getThumbs();
                if (dataBean.getThumbs().length() == 1) {
                    lp.setMargins(0, 0, 35, 0);
                    holder1.zan_number.setLayoutParams(lp);
                    holder1.zan_number.setText(comment_thumbs);
                } else if (dataBean.getThumbs().length() == 2) {
                    lp.setMargins(0, 0, 17, 0);
                    holder1.zan_number.setLayoutParams(lp);
                    holder1.zan_number.setText(comment_thumbs);
                } else if (dataBean.getThumbs().length() == 3) {
                    lp.setMargins(0, 0, 0, 0);
                    holder1.zan_number.setLayoutParams(lp);
                    holder1.zan_number.setText(comment_thumbs);
                } else if (dataBean.getThumbs().length() == 4) {
                    lp.setMargins(0, 0, 17, 0);
                    holder1.zan_number.setLayoutParams(lp);
                    comment_thumbs = dataBean.getThumbs();
                    comment_thumbs = comment_thumbs.substring(0, 1);
                    holder1.zan_number.setText(comment_thumbs + "k");
                } else if (dataBean.getThumbs().length() == 5) {
                    lp.setMargins(0, 0, 0, 0);
                    holder1.zan_number.setLayoutParams(lp);
                    comment_thumbs = dataBean.getThumbs();
                    comment_thumbs = comment_thumbs.substring(0, 2);
                    holder1.zan_number.setText(comment_thumbs + "k");
                }

                if ("1".equals(dataBean.getIs_thumbs())) {
                    holder1.zan_state.setImageResource(R.drawable.icon_answer_collect_on);
                } else if ("2".equals(dataBean.getIs_thumbs())) {
                    holder1.zan_state.setImageResource(R.drawable.icon_answer_collect);
                }
                Glide.with(context)
                        .load(dataBean.getUser_data().getAvatar())
                        .error(R.drawable.touxiang)
                        .into(holder1.user_logo);
                if (dataBean.getParent_data().getName() != null && !TextUtils.isEmpty(dataBean.getParent_data().getName())) {
                    String content = "回复" + dataBean.getParent_data().getName() + ":" + dataBean.getContent();

                    //设置特殊字体颜色,并且家点击事件
                    SpannableString spannableString1 = new SpannableString(content);
                    spannableString1.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            type = "1";
                            Message msg = new Message();
                            msg.what = 404;
                            msg.arg1 = position;
                            handler.sendMessage(msg);
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setColor(context.getResources().getColor(R.color.color_5377B3));
                            ds.setUnderlineText(false);
                            ds.clearShadowLayer();
                        }
                    }, 2, dataBean.getParent_data().getName().length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder1.answer_content.setText(spannableString1);
                    holder1.answer_content.setMovementMethod(LinkMovementMethod.getInstance());

                } else {
                    holder1.answer_content.setText(dataBean.getContent());
                }
            } else {
                holder1.tv_more.setVisibility(View.GONE);
                holder1.item_main_layout.setBackgroundColor(context.getResources().getColor(R.color.edit_hui));
                holder1.line_layout.setVisibility(View.GONE);

                if ("2".equals(dataBean.getIs_existence())) {
                    holder1.delete_btn.setVisibility(View.INVISIBLE);
                } else if ("1".equals(dataBean.getIs_existence())) {
                    holder1.delete_btn.setVisibility(View.VISIBLE);
                }

                holder1.user_name.setText(dataBean.getUser_data().getName());
                holder1.tv_time.setText(FormatCurrentData.getTimeRange(Long.valueOf(dataBean.getCreated_at())));
                if (dataBean.getParent_data().getName() != null && !TextUtils.isEmpty(dataBean.getParent_data().getName())) {
                    String content = "回复" + dataBean.getParent_data().getName() + ":" + dataBean.getContent();

                    //设置特殊字体颜色,并且家点击事件
                    SpannableString spannableString1 = new SpannableString(content);
                    spannableString1.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            type = "1";
                            Message msg = new Message();
                            msg.what = 404;
                            msg.arg1 = position;
                            handler.sendMessage(msg);
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setColor(context.getResources().getColor(R.color.color_5377B3));
                            ds.setUnderlineText(false);
                            ds.clearShadowLayer();
                        }
                    }, 2, dataBean.getParent_data().getName().length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder1.answer_content.setText(spannableString1);
                    holder1.answer_content.setMovementMethod(LinkMovementMethod.getInstance());

                } else {
                    holder1.answer_content.setText(dataBean.getContent());
                }
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder1.zan_number.getLayoutParams();
                String comment_thumbs = dataBean.getThumbs();
                if (dataBean.getThumbs().length() == 1) {
                    lp.setMargins(0, 0, 35, 0);
                    holder1.zan_number.setLayoutParams(lp);
                    holder1.zan_number.setText(comment_thumbs);
                } else if (dataBean.getThumbs().length() == 2) {
                    lp.setMargins(0, 0, 17, 0);
                    holder1.zan_number.setLayoutParams(lp);
                    holder1.zan_number.setText(comment_thumbs);
                } else if (dataBean.getThumbs().length() == 3) {
                    lp.setMargins(0, 0, 0, 0);
                    holder1.zan_number.setLayoutParams(lp);
                    holder1.zan_number.setText(comment_thumbs);
                } else if (dataBean.getThumbs().length() == 4) {
                    lp.setMargins(0, 0, 17, 0);
                    holder1.zan_number.setLayoutParams(lp);
                    comment_thumbs = dataBean.getThumbs();
                    comment_thumbs = comment_thumbs.substring(0, 1);
                    holder1.zan_number.setText(comment_thumbs + "k");
                } else if (dataBean.getThumbs().length() == 5) {
                    lp.setMargins(0, 0, 0, 0);
                    holder1.zan_number.setLayoutParams(lp);
                    comment_thumbs = dataBean.getThumbs();
                    comment_thumbs = comment_thumbs.substring(0, 2);
                    holder1.zan_number.setText(comment_thumbs + "k");
                }

                if ("1".equals(dataBean.getIs_thumbs())) {
                    holder1.zan_state.setImageResource(R.drawable.icon_answer_collect_on);
                } else if ("2".equals(dataBean.getIs_thumbs())) {
                    holder1.zan_state.setImageResource(R.drawable.icon_answer_collect);
                }
                Glide.with(context)
                        .load(dataBean.getUser_data().getAvatar())
                        .error(R.drawable.touxiang)
                        .into(holder1.user_logo);

            }
            holder1.zan_alyout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("2".equals(dataBean.getIs_thumbs())) {
                        //点赞
                        holder1.zan_state.setImageResource(R.drawable.icon_answer_collect_on);
                    } else if ("1".equals(dataBean.getIs_thumbs())) {
                        //取消点赞
                        holder1.zan_state.setImageResource(R.drawable.icon_answer_collect);
                    }
                    Message msg = new Message();
                    msg.what = 400;
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                }
            });
            holder1.reply_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = 401;
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                }
            });
            holder1.answer_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!"1".equals(type)) {
                        Message msg = new Message();
                        msg.what = 401;
                        msg.arg1 = position;
                        handler.sendMessage(msg);
                    } else {
                        type = "0";
                    }
                }
            });
            holder1.user_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = 402;
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                }
            });
            holder1.user_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = 402;
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                }
            });
            holder1.tv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = 403;
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                }
            });
            //删除回复
            holder1.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = 405;
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
    }

    @Override
    public int getAdapterItemViewType(int position) {
        if (commentLeveTwos.size() == 0) {
            return EMPTY_VIEW;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getAdapterItemCount() {
        return commentLeveTwos == null ? 0 : commentLeveTwos.size();
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

    class Holder extends RecyclerView.ViewHolder {

        private ImageView user_logo;
        private TextView user_name;
        private TextView tv_time;
        private ImageView reply_btn;
        private TextView zan_number;
        private ImageView zan_state;
        private TextView answer_content;
        private LinearLayout zan_alyout;
        private RelativeLayout item_main_layout;
        private View line_layout;
        private TextView tv_more;
        private ImageView delete_btn;

        public Holder(View itemView, boolean ism) {
            super(itemView);
            user_logo = itemView.findViewById(R.id.user_logo);
            user_name = itemView.findViewById(R.id.user_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            reply_btn = itemView.findViewById(R.id.reply_btn);
            zan_number = itemView.findViewById(R.id.zan_number);
            zan_state = itemView.findViewById(R.id.zan_state);
            answer_content = itemView.findViewById(R.id.answer_content);
            zan_alyout = itemView.findViewById(R.id.zan_alyout);
            item_main_layout = itemView.findViewById(R.id.item_main_layout);
            line_layout = itemView.findViewById(R.id.line_layout);
            tv_more = itemView.findViewById(R.id.tv_more);
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

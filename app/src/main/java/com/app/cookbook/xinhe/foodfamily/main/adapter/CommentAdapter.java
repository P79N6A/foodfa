package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.app.cookbook.xinhe.foodfamily.callback.DialogCallBack;
import com.app.cookbook.xinhe.foodfamily.main.AnswerReplyActivity;
import com.app.cookbook.xinhe.foodfamily.main.CommentActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.AnswerListOne;
import com.app.cookbook.xinhe.foodfamily.util.FormatCurrentData;
import com.app.cookbook.xinhe.foodfamily.util.PopWindowHelper;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 18030150 on 2018/3/28.
 */

public class CommentAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<AnswerListOne> answerListOnes = new ArrayList<>();
    private Handler handler;
    private String answer_id;

    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private boolean ista;
    CommentActivity commentActivity;

    public CommentAdapter(CommentActivity mcommentActivity, Context context, Handler handler, boolean mista, String answer_id, List<AnswerListOne> answerListOnes) {
        this.context = context;
        this.commentActivity = mcommentActivity;
        this.handler = handler;
        this.ista = mista;
        this.answer_id = answer_id;
        this.answerListOnes = answerListOnes;
        this.inflater = LayoutInflater.from(context);
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
        View shidan_view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        RecyclerView.ViewHolder holder = null;
        if (viewType == EMPTY_VIEW) {
            holder = new EmptyViewHolder(empty_view, true);
        } else {
            holder = new Holder(shidan_view, true);

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder_view, final int position, boolean isItem) {
        if (getAdapterItemViewType(position) == TYPE_ITEM) {
            Holder holder = (Holder) holder_view;
            final AnswerListOne listOne = answerListOnes.get(position);
            holder.user_name.setText(listOne.getUser_name());
            holder.tv_time.setText(FormatCurrentData.getTimeRange(Long.valueOf(listOne.getCreated_at())));
            holder.comm_content.setText(listOne.getComment_content());

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

            if ("2".equals(listOne.getIs_delete())) {
                holder.delete_btn.setVisibility(View.INVISIBLE);
            } else if ("1".equals(listOne.getIs_delete())) {
                holder.delete_btn.setVisibility(View.VISIBLE);
            }

            Glide.with(context)
                    .load(listOne.getUser_avatar())
                    .error(R.drawable.touxiang)
                    .into(holder.user_logo);
            if (2 <= listOne.getSon_data().size()) {
                holder.more_reply.setVisibility(View.VISIBLE);
                holder.more_reply.setText("共" + listOne.getSon_count() + "条回复 >");
            } else {
                holder.more_reply.setVisibility(View.GONE);
            }
            if (listOne.getSon_data() != null && listOne.getSon_data().size() > 0) {
                holder.reply_layout.setVisibility(View.VISIBLE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                holder.recyclerView.setLayoutManager(linearLayoutManager);
                holder.recyclerView.setHasFixedSize(true);
                CommentChildAdapter childAdapter = new CommentChildAdapter(context, listOne.getSon_count(), answer_id
                        , listOne.getComment_id(), listOne.getUser_avatar(), listOne.getUser_name(), listOne.getComment_content(),
                        listOne.getCreated_at(), listOne.getComment_thumbs(), listOne.getIs_thumbs(), listOne.getUser_id(), handler);
                childAdapter.setSonDataBean(listOne.getSon_data());
                holder.recyclerView.setAdapter(childAdapter);
            } else {
                holder.reply_layout.setVisibility(View.GONE);
            }

            holder.more_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AnswerReplyActivity.class);
                    intent.putExtra("answer_id", answer_id);
                    intent.putExtra("original_id", listOne.getComment_id());
                    intent.putExtra("image", listOne.getUser_avatar());
                    intent.putExtra("name", listOne.getUser_name());
                    intent.putExtra("content", listOne.getComment_content());
                    intent.putExtra("time", listOne.getCreated_at());
                    intent.putExtra("thumbs", listOne.getComment_thumbs());
                    intent.putExtra("isthumbs", listOne.getIs_thumbs());
                    intent.putExtra("user_id", listOne.getUser_id());
                    intent.putExtra("mesComm", "1");
                    intent.putExtra("isCheck", "1");
                    context.startActivity(intent);
                }
            });

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
            holder.comm_content.setOnClickListener(new View.OnClickListener() {
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
            holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopWindowHelper.public_tishi_pop(commentActivity, "删除评论", "是否删除该评论？", "否", "是", new DialogCallBack() {
                        @Override
                        public void save() {
                            Message msg = new Message();
                            msg.what = 403;
                            msg.arg1 = position;
                            handler.sendMessage(msg);
                        }

                        @Override
                        public void cancel() {

                        }
                    });
                }
            });

            //条目点击时间
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int position = holder_view.getPosition();
                        mOnItemClickListener.onItemClick(holder_view.itemView, position);
                    }
                });
            }
        } else {
            EmptyViewHolder holder = (EmptyViewHolder) holder_view;
            holder.title.setVisibility(View.GONE);

        }
    }

    @Override
    public int getAdapterItemViewType(int position) {
        if (answerListOnes.size() == 0) {
            return EMPTY_VIEW;
        }
        return TYPE_ITEM;
    }


    @Override
    public int getAdapterItemCount() {
//        return answerListOnes == null ? 0 : answerListOnes.size();
        return answerListOnes.size() > 0 ? answerListOnes.size() : 1;
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

    class Holder extends RecyclerView.ViewHolder {
        private ImageView user_logo;
        private TextView user_name;
        private TextView tv_time;
        private TextView reply_btn;
        private RelativeLayout zan_alyout;
        private ImageView zan_state;
        private TextView zan_number;
        private TextView comm_content;
        private RelativeLayout reply_layout;
        private RecyclerView recyclerView;
        private TextView delete_btn;
        //更多评论
        private TextView more_reply;

        public Holder(View itemView, boolean isItem) {
            super(itemView);
            user_logo = itemView.findViewById(R.id.user_logo);
            user_name = itemView.findViewById(R.id.user_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            reply_btn = itemView.findViewById(R.id.reply_btn);
            zan_alyout = itemView.findViewById(R.id.zan_alyout);
            zan_state = itemView.findViewById(R.id.zan_state);
            zan_number = itemView.findViewById(R.id.zan_number);
            comm_content = itemView.findViewById(R.id.comm_content);
            reply_layout = itemView.findViewById(R.id.reply_layout);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            delete_btn = itemView.findViewById(R.id.delete_btn);
            more_reply = itemView.findViewById(R.id.more_reply);
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

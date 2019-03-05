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

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageComment;
import com.app.cookbook.xinhe.foodfamily.util.FormatCurrentData;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by 18030150 on 2018/3/29.
 */

public class MessageCommentAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<MessageComment.DataBean> messageComments;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private boolean ista;
    private Handler handler;

    public MessageCommentAdapter(List<MessageComment.DataBean> messageComments, Context mcontext, boolean mista, Handler handler) {
        this.context = mcontext;
        this.messageComments = messageComments;
        this.ista = mista;
        this.handler = handler;
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
        View shidan_view = LayoutInflater.from(context).inflate(R.layout.message_comment_item, parent, false);
        RecyclerView.ViewHolder holder = null;
        if (viewType == EMPTY_VIEW) {
            holder = new EmptyViewHolder(empty_view, true);
        } else {
            holder = new Holder(shidan_view, true);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_view, final int position, boolean isItem) {
        if (holder_view instanceof Holder) {
            final Holder holder = (Holder) holder_view;
            if (position == 0) {
                holder.title.setVisibility(View.VISIBLE);
            } else {
                holder.title.setVisibility(View.GONE);
            }
            MessageComment.DataBean comment = messageComments.get(position);

            if ("1".equals(comment.getLevel())) {
                holder.comm_name.setText(comment.getUser_data().getName());
                holder.comm_type.setText("评论了");
            } else if ("2".equals(comment.getLevel())) {
                holder.comm_name.setText(comment.getUser_data().getName());
                holder.comm_type.setText("回复了你");
            }
            holder.comm_content.setText(comment.getComment_content());
            holder.comm_time.setText(FormatCurrentData.getTimeRange(Long.valueOf(comment.getCreated_at())));
            Glide.with(context)
                    .load(comment.getUser_data().getAvatar())
                    .error(R.drawable.touxiang)
                    .into(holder.comm_image);

            if (mOnItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = holder.getPosition();
                        mOnItemLongClickListener.onItemLongClick(holder.itemView, position);
                        return false;
                    }
                });
            }
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
            holder.comm_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = 400;
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                }
            });
//            holder.comm_content.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Message msg = new Message();
//                    msg.what = 401;
//                    msg.arg1 = position;
//                    handler.sendMessage(msg);
//                }
//            });

        }


    }

    @Override
    public int getAdapterItemViewType(int position) {
        if (messageComments.size() == 0) {
            return EMPTY_VIEW;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getAdapterItemCount() {
//        return messageComments == null ? 0 : messageComments.size();
        return messageComments.size() > 0 ? messageComments.size() : 1;
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

        private ImageView comm_image;
        private TextView comm_name;
        private TextView comm_time;
        private TextView comm_content;
        private TextView comm_type;
        private TextView title;
        private LinearLayout comm_name_layout;

        public Holder(View itemView, boolean isItem) {
            super(itemView);
            comm_image = itemView.findViewById(R.id.comm_image);
            comm_name = itemView.findViewById(R.id.comm_name);
            comm_time = itemView.findViewById(R.id.comm_time);
            comm_content = itemView.findViewById(R.id.comm_content);
            comm_type = itemView.findViewById(R.id.comm_type);
            title = itemView.findViewById(R.id.title);
            comm_name_layout = itemView.findViewById(R.id.comm_name_layout);
        }
    }

    private OnItemLongClickListener mOnItemLongClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

}

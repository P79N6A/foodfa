package com.app.cookbook.xinhe.foodfamily.shiyujia.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.LabelDetailsActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.ImageTextComment;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.ImageTextDetails;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.VideoComment;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.VideoDetails;
import com.app.cookbook.xinhe.foodfamily.shiyujia.view.LabelsView;
import com.app.cookbook.xinhe.foodfamily.util.CircleImageView;
import com.app.cookbook.xinhe.foodfamily.util.FormatCurrentData;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.bumptech.glide.Glide;

import java.util.List;

public class VideoCommentAdapter extends RecyclerView.Adapter<VideoCommentAdapter.Holder> {

    private Context context;
    private LayoutInflater inflater;
    private List<VideoComment.DataBean> imageTextComments;
    private Handler handler;
    private String Uid;
    private String is_follow_user;
    private String content;
    private String avatar;
    private String name;
    private String personal_signature;
    private String thumb_number;
    private String coment_number;
    private String collection_number;
    private String created_at;
    private List<VideoDetails.ClassDataBean> class_data;

    public VideoCommentAdapter(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.inflater = LayoutInflater.from(context);
    }

    public void setImageTextComments(List<VideoComment.DataBean> imageTextComments) {
        this.imageTextComments = imageTextComments;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getIs_follow_user() {
        return is_follow_user;
    }

    public void setIs_follow_user(String is_follow_user) {
        this.is_follow_user = is_follow_user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonal_signature() {
        return personal_signature;
    }

    public void setPersonal_signature(String personal_signature) {
        this.personal_signature = personal_signature;
    }

    public String getThumb_number() {
        return thumb_number;
    }

    public void setThumb_number(String thumb_number) {
        this.thumb_number = thumb_number;
    }

    public String getComent_number() {
        return coment_number;
    }

    public void setComent_number(String coment_number) {
        this.coment_number = coment_number;
    }

    public String getCollection_number() {
        return collection_number;
    }

    public void setCollection_number(String collection_number) {
        this.collection_number = collection_number;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<VideoDetails.ClassDataBean> getClass_data() {
        return class_data;
    }

    public void setClass_data(List<VideoDetails.ClassDataBean> class_data) {
        this.class_data = class_data;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Holder holder = new Holder(inflater.inflate(R.layout.video_comment_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        VideoComment.DataBean dataBean = imageTextComments.get(position);
//        if (position == 0) {
//            holder.author_msg_layout.setVisibility(View.VISIBLE);
//            if (dataBean.getId() == null) {
//                holder.empty_layout.setVisibility(View.VISIBLE);
//            } else {
//                holder.empty_layout.setVisibility(View.GONE);
//            }
//            holder.comm_layoutt.setVisibility(View.VISIBLE);
//
//            String user_id = SharedPreferencesHelper.get(context, "user_id", "").toString();
//            if (!TextUtils.isEmpty(user_id)) {
//                if (user_id.equals(Uid)) {
//                    holder.user_attention.setVisibility(View.GONE);
//                    holder.user_attention_off.setVisibility(View.GONE);
//                } else {
//                    if ("1".equals(is_follow_user)) {
//                        holder.user_attention_off.setVisibility(View.VISIBLE);
//                        holder.user_attention.setVisibility(View.GONE);
//                    } else if ("2".equals(is_follow_user)) {
//                        holder.user_attention.setVisibility(View.VISIBLE);
//                        holder.user_attention_off.setVisibility(View.GONE);
//                    }
//
//                }
//            }
//            if (!TextUtils.isEmpty(content)) {
//                holder.details_content.setVisibility(View.VISIBLE);
//                holder.details_content.setText(content);
//            } else {
//                holder.details_content.setVisibility(View.GONE);
//            }
//
//            Glide.with(context)
//                    .load(avatar)
//                    .error(R.drawable.wodetouxiang)
//                    .into(holder.user_head_cir);
//            holder.user_name_tv.setText(name);
//            holder.user_signature.setText(personal_signature);
//            holder.zan_and_ping_layout.setText(thumb_number + "点赞 " + coment_number + "评论 "
//                    + collection_number + "收藏");
//            if (!TextUtils.isEmpty(created_at)) {
//                holder.details_time_layout.setText(FormatCurrentData.getTimeRange(Long.valueOf(created_at)));
//            }
//            holder.comm_number.setText("全部评论(" + coment_number + ")");
//            if (class_data != null) {
//                holder.labels.setVisibility(View.VISIBLE);
//                holder.labels.setLabels(class_data, new LabelsView.LabelTextProvider<VideoDetails.ClassDataBean>() {
//                    @Override
//                    public CharSequence getLabelText(TextView label, int position, VideoDetails.ClassDataBean data) {
//                        return "# " + data.getTitle();
//                    }
//                });
//                holder.labels.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
//                    @Override
//                    public void onLabelClick(TextView label, Object data, int position) {
//                        VideoDetails.ClassDataBean item = class_data.get(position);
//                        Intent intent = new Intent(context, LabelDetailsActivity.class);
//                        intent.putExtra("id", item.getId());
//                        context.startActivity(intent);
//                    }
//                });
//            } else {
//                holder.labels.setVisibility(View.GONE);
//            }
//
//            if (dataBean != null && dataBean.getUser_data() != null) {
//                if ("1".equals(dataBean.getIs_thumbs())) {
//                    holder.comment_im.setImageResource(R.drawable.icon_comment_number_on);
//                } else {
//                    holder.comment_im.setImageResource(R.drawable.icon_comment_number_off);
//                }
//                if ("2".equals(dataBean.getIs_existence())) {
//                    holder.delete_btn.setVisibility(View.INVISIBLE);
//                } else if ("1".equals(dataBean.getIs_existence())) {
//                    holder.delete_btn.setVisibility(View.VISIBLE);
//                }
//                Glide.with(context)
//                        .load(dataBean.getUser_data().getAvatar())
//                        .error(R.drawable.wodetouxiang)
//                        .into(holder.user_head);
//                holder.user_name.setText(dataBean.getUser_data().getName());
//                holder.comment_time.setText(FormatCurrentData.getTimeRange(Long.valueOf(dataBean.getCreated_at())));
//                holder.zan_number.setText(dataBean.getThumbs());
//                holder.comment_content.setText(dataBean.getContent());
//                if (dataBean.getSon_data() != null) {
//                    holder.reply_layout.setVisibility(View.VISIBLE);
//                    holder.reply_content.setText(dataBean.getSon_data().getUser_data().getName() + ": " + dataBean.getSon_data().getContent());
//                    holder.reply_number.setText("共" + dataBean.getSon_count() + "条回复 >");
//                } else {
//                    holder.reply_layout.setVisibility(View.GONE);
//                }
//            } else {
//                holder.comm_layoutt.setVisibility(View.GONE);
//            }
//
//        } else {
            holder.author_msg_layout.setVisibility(View.GONE);
            holder.empty_layout.setVisibility(View.GONE);
            holder.comm_layoutt.setVisibility(View.VISIBLE);
            if ("1".equals(dataBean.getIs_thumbs())) {
                holder.comment_im.setImageResource(R.drawable.icon_answer_collect_on);
            } else {
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
//        }

        holder.content_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        holder.user_llayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        holder.user_msg_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FriendPageActivity.class);
                intent.putExtra("UserId", Uid);
                context.startActivity(intent);
            }
        });

        holder.user_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                } else {
                    Message msg = new Message();
                    msg.what = 405;
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                }
            }
        });

        holder.user_attention_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                } else {
                    Message msg = new Message();
                    msg.what = 4051;
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                }
            }
        });

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


//        holder.comm_layoutt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Message msg = new Message();
//                msg.what = 401;
//                msg.arg1 = position;
//                handler.sendMessage(msg);
//            }
//        });

        holder.zan_comm_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 402;
                msg.arg1 = position;
                handler.sendMessage(msg);
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
    public int getItemCount() {
        return imageTextComments == null ? 0 : imageTextComments.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        //详情作者布局
        private RelativeLayout author_msg_layout;
        private RelativeLayout user_msg_layout;
        private CircleImageView user_head_cir;
        private TextView user_name_tv;
        private TextView user_signature;
        private TextView user_attention;
        private TextView user_attention_off;
        private TextView details_content;
        private LabelsView labels;
        private TextView zan_and_ping_layout;
        private TextView details_time_layout;
        private TextView comm_number;
        private RelativeLayout content_layout;
        private RelativeLayout user_llayout;

        //空状态
        private RelativeLayout empty_layout;


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
            user_llayout = itemView.findViewById(R.id.user_llayout);
            author_msg_layout = itemView.findViewById(R.id.author_msg_layout);
            user_msg_layout = itemView.findViewById(R.id.user_msg_layout);
            user_head_cir = itemView.findViewById(R.id.user_head_cir);
            user_name_tv = itemView.findViewById(R.id.user_name_tv);
            user_signature = itemView.findViewById(R.id.user_signature);
            user_attention = itemView.findViewById(R.id.user_attention);
            user_attention_off = itemView.findViewById(R.id.user_attention_off);
            details_content = itemView.findViewById(R.id.details_content);
            labels = itemView.findViewById(R.id.labels);
            zan_and_ping_layout = itemView.findViewById(R.id.zan_and_ping_layout);
            details_time_layout = itemView.findViewById(R.id.details_time_layout);
            comm_number = itemView.findViewById(R.id.comm_number);
            content_layout = itemView.findViewById(R.id.content_layout);
            //空状态
            empty_layout = itemView.findViewById(R.id.empty_layout);

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

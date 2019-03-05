package com.app.cookbook.xinhe.foodfamily.shiyujia.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.AnserActivity;
import com.app.cookbook.xinhe.foodfamily.main.FastAnswerActivity;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.NewAnswer;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tencent.stat.StatService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewAnswerAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<NewAnswer.DataBean> list;
    private Context context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_ITEM = 1;
    private Boolean is_new_page;
    private Handler handler;

    public NewAnswerAdapter(List<NewAnswer.DataBean> mlist, Context mcontext, boolean mis_new_page, Handler handler) {
        list = mlist;
        this.handler = handler;
        is_new_page = mis_new_page;
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
        View empty_view = LayoutInflater.from(context).inflate(R.layout.search_empty_layout, parent, false);
        View shidan_view = LayoutInflater.from(context).inflate(R.layout.new_fragment_item, parent, false);
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
        if (view_holder instanceof PurchaseViewHolder) {
            final PurchaseViewHolder holder = (PurchaseViewHolder) view_holder;

            ((PurchaseViewHolder) view_holder).view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });
            if (is_new_page) {//从最新列表进来的
                if (position == 0) {
                    holder.question_title.setVisibility(View.GONE);
                    holder.transpt_layout.setVisibility(View.GONE);
                } else {
                    holder.question_title.setVisibility(View.GONE);
                    holder.transpt_layout.setVisibility(View.GONE);

                }
            }
            final NewAnswer.DataBean dataBean = list.get(position);

            holder.question_content.setText(dataBean.getQuestion_data().getTitle());

            //设置用户名字
            holder.answer_user_name.setText(dataBean.getUser_data().getName());

            //设置用户头像
            Glide.with(context).load(dataBean.getUser_data().getAvatar())
                    .asBitmap()
                    .error(R.drawable.touxiang)
                    .into(holder.touxiang_image);

            if ("1".equals(dataBean.getQuestion_data().getIs_follow())) {
                holder.attention_image.setLiked(true);
                holder.attention_tv.setText("已关注");
                holder.attention_image.setLikeDrawableRes(R.drawable.icon_answer_collect_on);
            } else if ("2".equals(dataBean.getQuestion_data().getIs_follow())) {
                holder.attention_image.setLiked(false);
                holder.attention_tv.setText("关注问题");
                holder.attention_image.setUnlikeDrawableRes(R.drawable.icon_answer_collect);
            }
            if (!TextUtils.isEmpty(dataBean.getContent_remove())) {
                holder.question_answer.setVisibility(View.VISIBLE);
                holder.question_answer.setTextColor(context.getResources().getColor(R.color.color_565656));
                holder.question_answer.setText(Html.fromHtml(dataBean.getContent_remove()));
            } else {
                holder.question_answer.setVisibility(View.GONE);
            }

            //设置点赞数
            holder.zan_number.setText(dataBean.getThumbs() + "个赞");
            //设置评论数
            holder.shoucang_number.setText(dataBean.getComment_count() + "条评论");

            //跳转问题详情页
            holder.question_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Properties prop = new Properties();
                    prop.setProperty("name", "question_content");
                    StatService.trackCustomKVEvent(context, "Newest_details_problem", prop);

                    Intent intent = new Intent(context, FenLeiQuestionDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("wenti_id", dataBean.getQuestion_data().getId());
                    bundle.putString("flag", "0");
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            holder.attention_image.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                        holder.attention_image.setLiked(false);
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        dataBean.getQuestion_data().setIs_follow("2");
                    } else {
                        Message msg = new Message();
                        msg.what = 401;
                        msg.arg1 = position;
                        handler.sendMessage(msg);
                        dataBean.getQuestion_data().setIs_follow("1");
                        holder.attention_tv.setText("已关注");
                    }
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        dataBean.getQuestion_data().setIs_follow("1");
                    } else {
                        Message msg = new Message();
                        msg.what = 400;
                        msg.arg1 = position;
                        handler.sendMessage(msg);
                        dataBean.getQuestion_data().setIs_follow("2");
                        holder.attention_tv.setText("关注问题");
                    }
                }
            });
            //回答问题
            holder.answer_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, FastAnswerActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("question_id", dataBean.getQuestion_data().getId());
                        bundle.putString("is_go_to_detai", "true");
                        bundle.putString("question_title", dataBean.getQuestion_data().getTitle());
                        intent.putExtras(bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });

            if (null != dataBean.getHead_image()) {
                //设置只显示一张图片
                if (null != dataBean.getHead_image().getPath()) {
                    holder.images_view.setVisibility(View.VISIBLE);

                    Glide.with(context).load(dataBean.getHead_image().getPath())
                            //.centerCrop()
                            .asBitmap()
                            .placeholder(R.drawable.morenbg)
                            .error(R.drawable.morenbg)
                            .into(holder.images_view);
                } else {
                    holder.images_view.setVisibility(View.GONE);
                }

            } else {
                holder.images_view.setVisibility(View.GONE);
            }

            holder.images_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Properties prop = new Properties();
                    prop.setProperty("name", "question_answer");
                    StatService.trackCustomKVEvent(context, "Newest_details_answer", prop);
                    Intent intent = new Intent(context, AnserActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("answer_id", dataBean.getId() + "");
                    bundle.putString("question_id", dataBean.getQuestion_data().getId());
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            holder.question_answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Properties prop = new Properties();
                    prop.setProperty("name", "question_answer");
                    StatService.trackCustomKVEvent(context, "Newest_details_answer", prop);
                    Intent intent = new Intent(context, AnserActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("answer_id", dataBean.getId() + "");
                    bundle.putString("question_id", dataBean.getQuestion_data().getId());
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            holder.click_answer_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Properties prop = new Properties();
                    prop.setProperty("name", "question_answer");
                    StatService.trackCustomKVEvent(context, "Newest_details_answer", prop);
                    Intent intent = new Intent(context, AnserActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("answer_id", dataBean.getId() + "");
                    bundle.putString("question_id", dataBean.getQuestion_data().getId());
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
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
        public TextView question_title, question_content, question_answer,
                zan_number, shoucang_number, time_tv, answer_user_name;
        public RoundedImageView images_view;
        public ImageView dianzan_image, pinglun_image;
        public LinearLayout dianzan_btn, shoucang_btn, transpt_layout;
        public CircleImageView touxiang_image;
        public RelativeLayout touxiang_layout, click_answer_detail;
        //点赞评论布局
        private LinearLayout zan_and_ping_layout;
        //关注问题
        private RelativeLayout attention_layout;
        private LikeButton attention_image;
        private TextView attention_tv;
        //回答问题
        private LinearLayout answer_layout;
        private ImageView answer_image;
        private LinearLayout answer_ll;

        public PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                dianzan_image = (ImageView) itemView.findViewById(R.id.dianzan_image);
                pinglun_image = (ImageView) itemView.findViewById(R.id.pinglun_image);
                question_title = (TextView) itemView.findViewById(R.id.question_title);
                question_content = (TextView) itemView.findViewById(R.id.question_content);
                question_answer = (TextView) itemView.findViewById(R.id.question_answer);
                images_view = (RoundedImageView) itemView.findViewById(R.id.images_view);
                zan_number = (TextView) itemView.findViewById(R.id.zan_number);
                shoucang_number = (TextView) itemView.findViewById(R.id.shoucang_number);
                time_tv = (TextView) itemView.findViewById(R.id.time_tv);
                dianzan_btn = (LinearLayout) itemView.findViewById(R.id.dianzan_btn);
                touxiang_layout = (RelativeLayout) itemView.findViewById(R.id.touxiang_layout);
                shoucang_btn = (LinearLayout) itemView.findViewById(R.id.shoucang_btn);
                transpt_layout = (LinearLayout) itemView.findViewById(R.id.transpt_layout);
                touxiang_image = (CircleImageView) itemView.findViewById(R.id.touxiang_image);
                answer_user_name = (TextView) itemView.findViewById(R.id.answer_user_name);
                zan_and_ping_layout = itemView.findViewById(R.id.zan_and_ping_layout);
                click_answer_detail = itemView.findViewById(R.id.click_answer_detail);
                attention_layout = itemView.findViewById(R.id.attention_layout);
                attention_image = itemView.findViewById(R.id.attention_image);
                attention_tv = itemView.findViewById(R.id.attention_tv);
                answer_layout = itemView.findViewById(R.id.answer_layout);
                answer_image = itemView.findViewById(R.id.answer_image);
                answer_ll = itemView.findViewById(R.id.answer_ll);
            }
        }
    }
}

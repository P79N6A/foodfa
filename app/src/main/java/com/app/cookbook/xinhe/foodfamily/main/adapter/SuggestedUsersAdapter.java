package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.main.OtherUserActivity;
import com.app.cookbook.xinhe.foodfamily.main.SuggestedUsersActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.Attention;
import com.app.cookbook.xinhe.foodfamily.main.entity.SuggestedUsers;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.CommtentReplyActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.util.CircleImageView;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;

public class SuggestedUsersAdapter extends RecyclerView.Adapter<SuggestedUsersAdapter.Holder> {

    private Context context;
    private LayoutInflater inflater;
    private Handler handler;
    private List<SuggestedUsers.DataBean> suggestedUsers = new ArrayList<>();

    protected Subscription subscription;


    public SuggestedUsersAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setSuggestedUsers(List<SuggestedUsers.DataBean> suggestedUsers) {
        this.suggestedUsers = suggestedUsers;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.suggested_users_item, null);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        final SuggestedUsers.DataBean users = suggestedUsers.get(position);
        if (null != users.getUser_data()) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.main_layout.
                    getLayoutParams();
            if (position == 0) {
                lp.setMargins(30, 0, 0, 0);
                holder.main_layout.setLayoutParams(lp);
            } else {
                lp.setMargins(0, 0, 0, 0);
                holder.main_layout.setLayoutParams(lp);
            }

            Glide.with(context).load(users.getUser_data().getAvatar())
                    .error(R.drawable.touxiang)
                    .into(holder.user_hand);
            holder.user_name.setText(users.getUser_data().getName());

            if (!TextUtils.isEmpty(users.getUser_data().getAnswer_count())) {
                String answerCount = users.getUser_data().getAnswer_count();
                if (answerCount.length() == 4) {
                    answerCount = answerCount.substring(0, 1);
                    holder.answer_count.setText(answerCount + "k内容 ");
                } else if (answerCount.length() == 5) {
                    answerCount = answerCount.substring(0, 2);
                    holder.answer_count.setText(answerCount + "k内容 ");
                } else {
                    holder.answer_count.setText(answerCount + "内容 ");
                }
            } else {
                holder.answer_count.setText("0内容 ");
            }

            if (!TextUtils.isEmpty(users.getUser_data().getThumbs_count())) {
                String thumbsCount = users.getUser_data().getThumbs_count();
                if (thumbsCount.length() == 4) {
                    thumbsCount = thumbsCount.substring(0, 1);
                    holder.thumbs_count.setText("获赞" + thumbsCount + "k");
                } else if (thumbsCount.length() == 5) {
                    thumbsCount = thumbsCount.substring(0, 2);
                    holder.thumbs_count.setText("获赞" + thumbsCount + "k");
                } else {
                    holder.thumbs_count.setText("获赞" + thumbsCount);
                }
            } else {
                holder.thumbs_count.setText("获赞0");
            }

            if (suggestedUsers.size() >= 3) {
                if (suggestedUsers.size() == position + 1) {
                    holder.confine_image.setVisibility(View.VISIBLE);
                } else {
                    holder.confine_image.setVisibility(View.GONE);
                }
            } else {
                holder.confine_image.setVisibility(View.GONE);
            }

            holder.user_hand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    if (null != users.getUser_id()) {
                        if (null != users.getUser_id()) {
                            intent.putExtra("UserId", users.getUser_id());
                            context.startActivity(intent);
                        }
                    }
                }
            });

            holder.user_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    if (null != users.getUser_id()) {
                        if (null != users.getUser_id()) {
                            intent.putExtra("UserId", users.getUser_id());
                            context.startActivity(intent);
                        }
                    }
                }
            });

            holder.confine_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SuggestedUsersActivity.class);
                    context.startActivity(intent);
                }
            });

            //关注按钮
            holder.user_attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    } else {
                        guanzhu_submit(users.getUser_id(), position);
                        holder.user_attention.setVisibility(View.GONE);
                        holder.quxiaoguanzhu_layout_top.setVisibility(View.VISIBLE);
                    }
                }
            });
            //取消关注
            holder.quxiaoguanzhu_layout_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    } else {
                        quxiao_guanzhu_submit(users.getUser_id(), position);
                        holder.quxiaoguanzhu_layout_top.setVisibility(View.GONE);
                        holder.user_attention.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return suggestedUsers == null ? 0 : suggestedUsers.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        private FrameLayout main_layout;
        //头像
        private CircleImageView user_hand;
        //昵称
        private TextView user_name;
        //内容数，
        private TextView answer_count;
        //获赞数
        private TextView thumbs_count;
        //关注
        private LinearLayout user_attention;
        //取消关注
        private LinearLayout quxiaoguanzhu_layout_top;
        //界线图标
        private RelativeLayout confine_image;


        public Holder(View itemView) {
            super(itemView);
            user_hand = itemView.findViewById(R.id.user_hand);
            user_name = itemView.findViewById(R.id.user_name);
            answer_count = itemView.findViewById(R.id.answer_count);
            thumbs_count = itemView.findViewById(R.id.thumbs_count);
            user_attention = itemView.findViewById(R.id.user_attention);
            quxiaoguanzhu_layout_top = itemView.findViewById(R.id.quxiaoguanzhu_layout_top);
            confine_image = itemView.findViewById(R.id.confine_image);
            main_layout = itemView.findViewById(R.id.main_layout);
        }
    }

    /**
     * 取消关注
     *
     * @param user_id
     */
    private void quxiao_guanzhu_submit(String user_id, final int position) {
        Map<String, String> params = new HashMap<>();
        if (null != user_id) {
            params.put("uuid", user_id);
        }
        Log.e("取消关注分类ID：", params.toString());
        subscription = Network.getInstance("取消关注用户", context)
                .quxiaoguanzhuta(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "取消关注分类成功：" + result.getMsg());
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "取消关注分类报错：" + error);
                            }
                        }, context, false));

    }

    //关注按钮
    private void guanzhu_submit(String id, final int position) {
        Map<String, String> params = new HashMap<>();
        params.put("uuid", id);
        Log.e("关注他：", params.toString());
        subscription = Network.getInstance("关注他", context)
                .guanzhuta(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "关注他成功：" + result.getMsg());
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "关注他报错：" + error);
                            }
                        }, context, false));
    }


}

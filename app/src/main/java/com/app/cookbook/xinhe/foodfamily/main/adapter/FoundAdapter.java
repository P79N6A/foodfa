package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.app.cookbook.xinhe.foodfamily.main.FenLeiDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.main.OtherUserActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.FoundEntity;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.net.rx.RxUtils;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.LabelDetailsActivity;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.tencent.stat.StatService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by shiyujia02 on 2018/1/26.
 */

public class FoundAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<FoundEntity> list;
    private Context context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;


    public FoundAdapter(List<FoundEntity> mlist, Context mcontext) {
        list = mlist;
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
        View empty_view = LayoutInflater.from(context).inflate(R.layout.found_empty_layout, parent, false);
        View shidan_view = LayoutInflater.from(context).inflate(R.layout.found_beaty_item, parent, false);
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
            PurchaseViewHolder holder = (PurchaseViewHolder) view_holder;

            if (position == 0) {
                holder.title.setVisibility(View.VISIBLE);
                holder.fenge_layout.setVisibility(View.VISIBLE);
            } else {
                holder.title.setVisibility(View.GONE);
                holder.fenge_layout.setVisibility(View.GONE);
            }

            ((PurchaseViewHolder) view_holder).view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });

            //设置数据
            Glide.with(context).load(list.get(position).getPath())
                    .error(R.drawable.icon_logo)
                    .into(holder.select_head_image);
            holder.title_tv.setText(list.get(position).getTitle());
            holder.guanzhu_number.setText(list.get(position).getFollow() + "关注");
            holder.question_number.setText(list.get(position).getQuestions() + "问题");
            //判断有没有推荐问题
            if (null != list.get(position).getQuestion_data()) {
                if (null != list.get(position).getQuestion_data().getQuestion_title()) {
                    holder.tuijian_wenti.setVisibility(View.VISIBLE);
                    holder.question_content.setText(list.get(position).getQuestion_data().getQuestion_title());
                } else {
                    holder.tuijian_wenti.setVisibility(View.GONE);
                }
            } else {
                holder.tuijian_wenti.setVisibility(View.GONE);
            }

            //判断有没有推荐用户
            if (null != list.get(position).getUser_data()) {
                if (null != list.get(position).getUser_data()) {
                    holder.user_layout.setVisibility(View.VISIBLE);
                    Glide.with(context).load(list.get(position).getUser_data().getAvatar())
                            .error(R.drawable.touxiang)
                            .into(holder.user_head);
                    holder.user_name.setText(list.get(position).getUser_data().getName());
                } else {
                    holder.user_layout.setVisibility(View.GONE);
                }
            } else {
                holder.user_layout.setVisibility(View.GONE);
            }

            //跳转分类详情
            holder.click_item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Properties prop1 = new Properties();
                    prop1.setProperty("name", "click_item_layout");
                    StatService.trackCustomKVEvent(context, "Found_details_tag", prop1);

                    Intent intent = new Intent(context, LabelDetailsActivity.class);
                    intent.putExtra("id", list.get(position).getId());
                    context.startActivity(intent);
                }
            });

            //跳转问题详情
            holder.tuijian_wenti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(position).getQuestion_data() != null) {
                        Properties prop1 = new Properties();
                        prop1.setProperty("name", "tuijian_wenti");
                        StatService.trackCustomKVEvent(context, "Found_recommend_question", prop1);

                        Intent intent = new Intent(context, FenLeiQuestionDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("wenti_id", list.get(position).getQuestion_data().getQuestion_id());
                        intent.putExtras(bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent);
                    }
                }
            });

            //跳转其他人主页
            holder.content_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Properties prop1 = new Properties();
                    prop1.setProperty("name", "content_layout");
                    StatService.trackCustomKVEvent(context, "Found_recommend_user", prop1);

                    Intent intent = new Intent(context, FriendPageActivity.class);
                    if (null != list.get(position).getUser_data()) {
                        if (null != list.get(position).getUser_data()) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("UserId",list.get(position).getUser_data().getId());
                            context.startActivity(intent);
                        }
                    }
                }
            });

            //设置关注按钮状态
            if (list.get(position).getIs_follow().equals("1")) {
                holder.guanzhu_layout_top.setVisibility(View.GONE);
                holder.yiguanzhu_layout_top.setVisibility(View.VISIBLE);
            } else {
                holder.guanzhu_layout_top.setVisibility(View.VISIBLE);
                holder.yiguanzhu_layout_top.setVisibility(View.GONE);
            }
            //点击关注
            RxUtils.clickView(holder.guanzhu_layout_top)
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                                Intent intent = new Intent(context, LoginActivity.class);
                                context.startActivity(intent);
                            } else {
                                guanzhu_submit(list.get(position).getId(), position);
                            }
                        }
                    });

            //取消关注
            RxUtils.clickView(holder.yiguanzhu_layout_top)
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                                Intent intent = new Intent(context, LoginActivity.class);
                                context.startActivity(intent);
                            } else {
                                quxiao_guanzhu_submit(list.get(position).getId(), position);
                            }
                        }
                    });
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
            params.put("id", user_id);
        }
        Log.e("取消关注分类ID：", params.toString());
        subscription = Network.getInstance("取消关注分类", context)
                .quxiao_guanzhu(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123","取消关注分类成功：" + result.getCode());
                                list.get(position).setIs_follow("2");
                                list.get(position).setFollow(Integer.valueOf(list.get(position).getFollow()) - 1 + "");

                                notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123","取消关注分类报错：" + error);
                            }
                        }, context, false));
    }

    /**
     * Rxjava
     */
    protected Subscription subscription;

    private void guanzhu_submit(String user_id, final int position) {
        Map<String, String> params = new HashMap<>();
        if (null != user_id) {
            params.put("id", user_id);
        }
        Log.e("关注分类ID：", params.toString());
        subscription = Network.getInstance("关注分类", context)
                .guanzhufenlei(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123","关注分类成功：" + result.getCode());
                                list.get(position).setIs_follow("1");
                                list.get(position).setFollow(Integer.valueOf(list.get(position).getFollow()) + 1 + "");
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123","关注分类报错：" + error);
                            }
                        }, context, false));

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

    public void setData(List<FoundEntity> list) {
        this.list = list;
        notifyDataSetChanged();
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
        public CircleImageView select_head_image, user_head;
        public TextView title_tv, guanzhu_number, question_number, question_content, user_name, title;
        public LinearLayout content_layout, guanzhu_layout_top, click_item_layout,
                question_fenge, question_fenge1, yiguanzhu_layout_top, fenge_layout;
        public RelativeLayout touxiang_layout, tuijian_wenti, user_layout;

        public PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                select_head_image = (CircleImageView) itemView.findViewById(R.id.select_head_image);
                title_tv = (TextView) itemView.findViewById(R.id.title_tv);
                guanzhu_number = (TextView) itemView.findViewById(R.id.guanzhu_number);
                question_number = (TextView) itemView.findViewById(R.id.question_number);
                question_content = (TextView) itemView.findViewById(R.id.question_content);
                question_fenge1 = (LinearLayout) itemView.findViewById(R.id.question_fenge1);
                question_fenge = (LinearLayout) itemView.findViewById(R.id.question_fenge);
                user_head = (CircleImageView) itemView.findViewById(R.id.user_head);
                user_name = (TextView) itemView.findViewById(R.id.user_name);
                title = (TextView) itemView.findViewById(R.id.title);
                touxiang_layout = (RelativeLayout) itemView.findViewById(R.id.touxiang_layout);
                user_layout = (RelativeLayout) itemView.findViewById(R.id.user_layout);
                tuijian_wenti = (RelativeLayout) itemView.findViewById(R.id.tuijian_wenti);
                content_layout = (LinearLayout) itemView.findViewById(R.id.content_layout);
                yiguanzhu_layout_top = (LinearLayout) itemView.findViewById(R.id.yiguanzhu_layout_top);
                guanzhu_layout_top = (LinearLayout) itemView.findViewById(R.id.guanzhu_layout_top);
                click_item_layout = (LinearLayout) itemView.findViewById(R.id.click_item_layout);
                fenge_layout = (LinearLayout) itemView.findViewById(R.id.fenge_layout);
            }
        }
    }
}

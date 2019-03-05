package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.callback.ImageBiggerCallBack;
import com.app.cookbook.xinhe.foodfamily.main.AnserActivity;
import com.app.cookbook.xinhe.foodfamily.main.FastAnswerActivity;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.QuesEn;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.util.DensityUtil;
import com.app.cookbook.xinhe.foodfamily.util.FormatCurrentData;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;

/**
 * Created by shiyujia02 on 2018/2/26.
 */

public class SimpleAdapter2 extends BaseRecyclerAdapter<SimpleAdapter2.SimpleAdapterViewHolder> {
    private List<QuesEn> answerEntities;
    private int largeCardHeight, smallCardHeight;
    private Context context;
    ImageBiggerCallBack imageBiggerCallBack;

    public SimpleAdapter2(List<QuesEn> list, Context mcontext, ImageBiggerCallBack mimageBiggerCallBack) {
        context = mcontext;
        this.answerEntities = list;
        imageBiggerCallBack = mimageBiggerCallBack;
        largeCardHeight = DensityUtil.dip2px(context, 150);
        smallCardHeight = DensityUtil.dip2px(context, 100);
    }

    @Override
    public void onBindViewHolder(SimpleAdapterViewHolder holder, final int position, boolean isItem) {

        if (null != answerEntities.get(position).getAnswer_data()) {
            holder.dianzan_pinglun_layout.setVisibility(View.VISIBLE);

            //设置回答人头像
            Glide.with(context).load(answerEntities.get(position).getAnswer_data().getUsers_avatar())
                    .error(R.drawable.touxiang)
                    .into(holder.touxiang_image);

            if (null != answerEntities.get(position).getAnswer_data().getImg_data()) {
                holder.answer_img.setVisibility(View.VISIBLE);
                Glide.with(context).load(answerEntities.get(position).getAnswer_data().getImg_data().getPath())
                        .error(R.drawable.morenbg)
                        .into(holder.answer_img);
                holder.answer_img.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        imageBiggerCallBack.image_url(view, answerEntities.get(position).getAnswer_data().getImg_data().getPath());
                        return false;
                    }
                });
                holder.answer_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, AnserActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("answer_id", answerEntities.get(position).getAnswer_data().getId());
                        intent.putExtras(bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
            } else {
                holder.answer_img.setVisibility(View.GONE);
            }


            //答案
            if (null == answerEntities.get(position).getAnswer_data().getContent_remove() || "".equals(answerEntities.get(position).getAnswer_data().getContent_remove()) && null == answerEntities.get(position).getAnswer_data().getImg_data()) {
                holder.question_answer.setVisibility(View.VISIBLE);
                holder.answer_issue_btn.setVisibility(View.VISIBLE);
                holder.question_answer.setTextColor(context.getResources().getColor(R.color.color_bbbbbb));
                holder.question_answer.setText("期待您的见解...");
            } else {
                holder.answer_issue_btn.setVisibility(View.GONE);
                holder.question_answer.setVisibility(View.VISIBLE);
                holder.question_answer.setTextColor(context.getResources().getColor(R.color.color_565656));
                holder.question_answer.setText(answerEntities.get(position).getAnswer_data().getContent_remove());
            }

            holder.zan_number.setText(answerEntities.get(position).getAnswer_data().getThumbs()+"个赞");
            holder.shoucang_number.setText(answerEntities.get(position).getAnswer_data().getComment_count()+"条评论");
            if (null == answerEntities.get(position).getAnswer_data().getName() || "".equals(answerEntities.get(position).getAnswer_data().getName())) {
                holder.touxiang_layout.setVisibility(View.GONE);
            } else {
                holder.touxiang_layout.setVisibility(View.VISIBLE);
            }        //设置回答人姓名
            holder.answer_user_name.setText(answerEntities.get(position).getAnswer_data().getName());

            //设置回答时间
            if (null == answerEntities.get(position).getAnswer_data().getCreated_at() || "".equals(answerEntities.get(position).getAnswer_data().getCreated_at())) {
                holder.time_tv.setText("");

            } else {
                holder.time_tv.setText(FormatCurrentData.getTimeRange(Long.valueOf(answerEntities.get(position).getAnswer_data().getCreated_at())));
            }

            holder.touxiang_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    if (null != answerEntities.get(position).getAnswer_data().getUid()) {
                        intent.putExtra("UserId",  answerEntities.get(position).getAnswer_data().getUid());
                        context.startActivity(intent);
                    }
                }
            });

            holder.question_answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AnserActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("answer_id", answerEntities.get(position).getAnswer_data().getId());
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                holder.rootView.getLayoutParams().height = position % 2 != 0 ? largeCardHeight : smallCardHeight;
            }
        } else {
            holder.answer_img.setVisibility(View.GONE);
            //头像名称布局
            holder.touxiang_layout.setVisibility(View.GONE);
            //问题标题
            holder.dianzan_pinglun_layout.setVisibility(View.GONE);
            holder.question_answer.setVisibility(View.VISIBLE);
            holder.answer_issue_btn.setVisibility(View.VISIBLE);
            holder.question_answer.setTextColor(context.getResources().getColor(R.color.color_bbbbbb));
            holder.question_answer.setText("期待您的见解...");
        }
        holder.question_content.setText(answerEntities.get(position).getQuestion_title());

        holder.question_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FenLeiQuestionDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("wenti_id", answerEntities.get(position).getQuestion_id());
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.answer_issue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, FastAnswerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("question_id", answerEntities.get(position).getQuestion_id() + "");
                    bundle.putString("is_go_to_detai", "true");
                    bundle.putString("question_title", answerEntities.get(position).getQuestion_title());
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    /**
     * 收藏
     *
     * @param answer_id
     * @param question_id
     * @param position
     */
    private void ShouCangNet(String answer_id, String question_id, final int position) {
        if (null == answer_id || "".equals(answer_id)) {
            Toast.makeText(context, "答案ID为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (null == question_id || "".equals(question_id)) {
            Toast.makeText(context, "问题ID为空！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("answer_id", answer_id);
            params.put("question_id", question_id);
            Log.e("收藏：", params.toString());
            subscription = Network.getInstance("收藏", context)
                    .shoucang(params,
                            new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                                @Override
                                public void onNext(Bean<List<String>> result) {
                                    Log.e("123","收藏成功：" + result.getCode());
                                    answerEntities.get(position).setIs_collect("1");
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                }
                            }, context, true));
        }

    }

    /**
     * 点赞
     *
     * @param answer_id
     * @param question_id
     */
    private void DianZanNet(String answer_id, String question_id, final int position) {
        if (null == answer_id || "".equals(answer_id)) {
            Toast.makeText(context, "答案ID为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (null == question_id || "".equals(question_id)) {
            Toast.makeText(context, "问题ID为空！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("answer_id", answer_id);
            params.put("question_id", question_id);
            Log.e("点赞：", params.toString());
            subscription = Network.getInstance("点赞", context)
                    .dianzan(params,
                            new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                                @Override
                                public void onNext(Bean<List<String>> result) {
                                    Log.e("123","点赞成功：" + result.getCode());
                                    answerEntities.get(position).setIs_thumbs("1");
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                }
                            }, context, true));
        }


    }

    /**
     * 关注问题
     *
     * @param question_id
     * @param position
     */
    private void guanzhu_question(String question_id, final int position) {
        if (null == question_id || "".equals(question_id)) {
            Toast.makeText(context, "问题ID为空！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("question_id", question_id);
            Log.e("关注问题：", params.toString());
            subscription = Network.getInstance("关注问题", context)
                    .guanzhu_question(params,
                            new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                                @Override
                                public void onNext(Bean<List<String>> result) {
                                    Log.e("123","关注问题成功：" + result.getCode());
                                    answerEntities.get(position).setIs_follow("1");
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                }
                            }, context, false));
        }
    }

    /**
     * 取消关注问题
     *
     * @param question_id
     * @param position
     */
    private void un_guanzhu_question(String question_id, final int position) {
        if (null == question_id || "".equals(question_id)) {
            Toast.makeText(context, "问题ID为空！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("question_id", question_id);
            Log.e("取消关注问题：", params.toString());
            subscription = Network.getInstance("取消关注问题", context)
                    .quxiao_question(question_id,
                            new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                                @Override
                                public void onNext(Bean<List<String>> result) {
                                    Log.e("123","取消关注问题成功：" + result.getCode());
                                    answerEntities.get(position).setIs_follow("2");
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                }
                            }, context, false));
        }
    }

    /**
     * Rxjava
     */
    protected Subscription subscription;

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
        return 0;
    }

    @Override
    public int getAdapterItemCount() {
        return answerEntities.size();
    }

    @Override
    public SimpleAdapterViewHolder getViewHolder(View view) {
        return new SimpleAdapterViewHolder(view, false);
    }

    public void setData(List<QuesEn> list) {
        this.answerEntities = list;
        notifyDataSetChanged();
    }

    @Override
    public SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.wenti_detail_item, parent, false);
        SimpleAdapterViewHolder vh = new SimpleAdapterViewHolder(v, true);
        return vh;
    }

    public void insert(QuesEn person, int position) {
        insert(answerEntities, person, position);
    }

    public void remove(int position) {
        remove(answerEntities, position);
    }

    public void clear() {
        clear(answerEntities);
    }

    public class SimpleAdapterViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout rootView;
        TextView question_content, question_answer, zan_number,
                shoucang_number, guanzhu_wenti_btn, liji_huida_btn, answer_user_name, time_tv;
        //ListViewForScrollView listViewForScrollView;
        RoundedImageView answer_img;

        LinearLayout dianzan_btn, shoucang_btn;
        RelativeLayout dianzan_pinglun_layout;
        ImageView dianzan_image, shoucang_image;
        CircleImageView touxiang_image;
        public RelativeLayout touxiang_layout;
        //回答问题
        private TextView answer_issue_btn;

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                rootView = itemView
                        .findViewById(R.id.card_view);
                question_content = itemView.findViewById(R.id.question_content);
                question_answer = itemView.findViewById(R.id.question_answer);
                answer_issue_btn = itemView.findViewById(R.id.answer_issue_btn);
                zan_number = itemView.findViewById(R.id.zan_number);
                dianzan_pinglun_layout = itemView.findViewById(R.id.dianzan_pinglun_layout);
                shoucang_number = itemView.findViewById(R.id.shoucang_number);
                guanzhu_wenti_btn = itemView.findViewById(R.id.guanzhu_wenti_btn);
                liji_huida_btn = itemView.findViewById(R.id.liji_huida_btn);
                //listViewForScrollView = itemView.findViewById(R.id.images_view);
                dianzan_btn = itemView.findViewById(R.id.dianzan_btn);
                dianzan_image = itemView.findViewById(R.id.dianzan_image);
                shoucang_image = itemView.findViewById(R.id.shoucang_image);
                shoucang_btn = itemView.findViewById(R.id.shoucang_btn);
                touxiang_layout = itemView.findViewById(R.id.touxiang_layout);
                touxiang_image = itemView.findViewById(R.id.touxiang_image);
                answer_user_name = itemView.findViewById(R.id.answer_user_name);
                time_tv = itemView.findViewById(R.id.time_tv);
                answer_img = itemView.findViewById(R.id.answer_img);
            }

        }
    }

    public QuesEn getItem(int position) {
        if (position < answerEntities.size())
            return answerEntities.get(position);
        else
            return null;
    }

}

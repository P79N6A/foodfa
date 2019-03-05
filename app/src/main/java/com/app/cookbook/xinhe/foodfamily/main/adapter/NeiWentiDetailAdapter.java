package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.AnserActivity;
import com.app.cookbook.xinhe.foodfamily.main.AnswerReplyActivity;
import com.app.cookbook.xinhe.foodfamily.main.FastAnswerActivity;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.main.OtherUserActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.ImageDate;
import com.app.cookbook.xinhe.foodfamily.main.entity.QuesEn;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.otherUi.ListViewForScrollView;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;

/**
 * Created by shiyujia02 on 2018/1/18.
 */

public class NeiWentiDetailAdapter extends BaseAdapter {
    List<QuesEn> cateDates;
    Context context;
    private LayoutInflater mInflater;

    public NeiWentiDetailAdapter(List<QuesEn> mcateDates, Context mcontext) {
        cateDates = mcateDates;
        context = mcontext;
        mInflater = LayoutInflater.from(mcontext);

    }

    @Override
    public int getCount() {
        return cateDates.size();
    }

    @Override
    public Object getItem(int position) {
        return cateDates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final NeiWentiDetailAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new NeiWentiDetailAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.wenti_detail_item, parent, false);
            holder.question_content = convertView.findViewById(R.id.question_content);
            holder.question_answer = convertView.findViewById(R.id.question_answer);
            holder.answer_issue_btn = convertView.findViewById(R.id.answer_issue_btn);
            holder.zan_number = convertView.findViewById(R.id.zan_number);
            holder.shoucang_number = convertView.findViewById(R.id.shoucang_number);
            holder.guanzhu_wenti_btn = convertView.findViewById(R.id.guanzhu_wenti_btn);
            holder.liji_huida_btn = convertView.findViewById(R.id.liji_huida_btn);
            holder.listViewForScrollView = convertView.findViewById(R.id.images_view);
            holder.dianzan_btn = convertView.findViewById(R.id.dianzan_btn);
            holder.dianzan_image = convertView.findViewById(R.id.dianzan_image);
            holder.shoucang_image = convertView.findViewById(R.id.shoucang_image);
            holder.shoucang_btn = convertView.findViewById(R.id.shoucang_btn);
            holder.touxiang_layout = convertView.findViewById(R.id.touxiang_layout);
            holder.touxiang_image = convertView.findViewById(R.id.touxiang_image);
            holder.answer_user_name = convertView.findViewById(R.id.answer_user_name);
            holder.time_tv = convertView.findViewById(R.id.time_tv);
            convertView.setTag(holder);
        } else {
            holder = (NeiWentiDetailAdapter.ViewHolder) convertView.getTag();
        }
        if (null != cateDates.get(position).getImg_data()) {
            holder.listViewForScrollView.setVisibility(View.VISIBLE);

            List<ImageDate> show_imageDates = new ArrayList<>();
            show_imageDates.add(cateDates.get(position).getImg_data());
            ImagesAdapter2 imagesAdapter = new ImagesAdapter2(show_imageDates, context, cateDates.get(position).getAnswer_id());
            holder.listViewForScrollView.setAdapter(imagesAdapter);
        } else {
            holder.listViewForScrollView.setVisibility(View.VISIBLE);

        }
        holder.question_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AnserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("answer_id", cateDates.get(position).getAnswer_id());
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


        holder.listViewForScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int mposition, long id) {
                Intent intent = new Intent(context, AnserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("answer_id", cateDates.get(position).getAnswer_id());
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


        //问题标题
        holder.question_content.setText(cateDates.get(position).getQuestion_title());
        //答案
        if (null == cateDates.get(position).getAnswer_content_remove() || TextUtils.isEmpty(cateDates.get(position).getAnswer_content_remove())) {
            holder.question_answer.setText("期待您的见解...");
            holder.question_answer.setVisibility(View.VISIBLE);
            holder.answer_issue_btn.setVisibility(View.VISIBLE);
        } else {
            holder.answer_issue_btn.setVisibility(View.GONE);
            holder.question_answer.setVisibility(View.VISIBLE);
            holder.question_answer.setText(cateDates.get(position).getAnswer_content_remove());
        }

        //收藏
        holder.shoucang_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShouCangNet(cateDates.get(position).getAnswer_id(), cateDates.get(position).getQuestion_id(), position);
            }
        });
        //点赞
        holder.dianzan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DianZanNet(cateDates.get(position).getAnswer_id(), cateDates.get(position).getQuestion_id(), position);
            }
        });
        //关注问题
        holder.guanzhu_wenti_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                } else {
                    if (cateDates.get(position).getIs_follow().equals("1")) {//关注成功，则取消关注
                        un_guanzhu_question(cateDates.get(position).getQuestion_id(), position);
                    } else {
                        guanzhu_question(cateDates.get(position).getQuestion_id(), position);
                    }
                }
            }
        });

        //立即回答
        holder.liji_huida_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, FastAnswerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("question_id", cateDates.get(position).getQuestion_id());
                    bundle.putString("question_title", cateDates.get(position).getQuestion_title());

                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });

        //设置点赞状态
        if (cateDates.get(position).getIs_thumbs().equals("1")) {
            holder.zan_number.setText("已点赞");
            holder.dianzan_image.setImageResource(R.drawable.dianzan_red);
        } else {
            holder.zan_number.setText("未点赞");
            holder.dianzan_image.setImageResource(R.drawable.question_dianzan);
        }

        //设置收藏状态
        if (cateDates.get(position).getIs_collect().equals("1")) {
            holder.shoucang_number.setText("已收藏");
            holder.shoucang_image.setImageResource(R.drawable.shoucang_iamge);
        } else {
            holder.shoucang_number.setText("未收藏");
            holder.shoucang_image.setImageResource(R.drawable.question_shoucang);
        }

        //设置关注问题的状态
        if (cateDates.get(position).getIs_follow().equals("1")) {
            holder.guanzhu_wenti_btn.setText("已关注");
        } else {
            holder.guanzhu_wenti_btn.setText("关注问题");
        }

        if (null == cateDates.get(position).getAnswers_users_name() || "".equals(cateDates.get(position).getAnswers_users_name())) {
            holder.touxiang_layout.setVisibility(View.GONE);
        } else {
            holder.touxiang_layout.setVisibility(View.VISIBLE);

        }        //设置回答人姓名
        holder.answer_user_name.setText(cateDates.get(position).getAnswers_users_name());

        //设置回答人头像
        Glide.with(context).load(cateDates.get(position).getAnswers_users_avatar())
                .error(R.drawable.touxiang)
                .into(holder.touxiang_image);

        //设置回答时间
        if (null == cateDates.get(position).getAnswer_created_at() || "".equals(cateDates.get(position).getAnswer_created_at())) {
            holder.time_tv.setText("");

        } else {
            Long time = Long.valueOf(cateDates.get(position).getAnswer_created_at());
            String month = "";
            String date = "";
            if (transForDate(time).getMonth() < 10) {
                month = "0" + (Integer.valueOf(transForDate(time).getMonth()) + 1);
            } else {
                month = (Integer.valueOf(transForDate(time).getMonth()) + 1) + "";
            }
            if (transForDate(time).getDate() < 10) {
                date = "0" + transForDate(time).getDate();
            } else {
                date = transForDate(time).getDate() + "";
            }
            holder.time_tv.setText(month + "-" + date);

        }

        holder.question_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FenLeiQuestionDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("wenti_id", cateDates.get(position).getQuestion_id());
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.touxiang_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FriendPageActivity.class);
                if (null != cateDates.get(position).getAnswers_users_id()) {
                    intent.putExtra("UserId", cateDates.get(position).getAnswers_users_id());
                    context.startActivity(intent);
                }
            }
        });


        return convertView;

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
                                    Log.e("123", "取消关注问题成功：" + result.getCode());
                                    cateDates.get(position).setIs_follow("2");
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
                                    Log.e("123", "关注问题成功：" + result.getCode());
                                    cateDates.get(position).setIs_follow("1");
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
                                    Log.e("123", "收藏成功：" + result.getCode());
                                    cateDates.get(position).setIs_collect("1");
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
     * Rxjava
     */
    protected Subscription subscription;

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
                                    Log.e("123", "点赞成功：" + result.getCode());
                                    cateDates.get(position).setIs_thumbs("1");
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                }
                            }, context, true));
        }


    }

    class ViewHolder {
        TextView question_content, question_answer, zan_number,
                shoucang_number, guanzhu_wenti_btn, liji_huida_btn, answer_user_name, time_tv;
        ListViewForScrollView listViewForScrollView;
        LinearLayout dianzan_btn, shoucang_btn, answer_layout, list_content;
        ImageView dianzan_image, shoucang_image;
        CircleImageView touxiang_image;
        public RelativeLayout touxiang_layout;
        //回答问题
        private TextView answer_issue_btn;

    }

    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }


    @Override
    public boolean isEnabled(int position) {
        // all items are separator
        return true;
    }
}

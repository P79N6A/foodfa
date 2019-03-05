package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.FenLeiDetailDate;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.otherUi.ListViewForScrollView;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;

/**
 * Created by shiyujia02 on 2018/1/18.
 */

public class FenLeiDetailAdapter extends RecyclerView.Adapter {
    public static final int TYPE_CONTENT = 1;
    public static final int TYPE_HEADER = 0;

    private FenLeiDetailActivity shiPingDetailAnimationActivity;
    //传过来的数据
    private FenLeiDetailDate mData;

    public FenLeiDetailAdapter(FenLeiDetailActivity mshiPingDetailAnimationActivity, FenLeiDetailDate Data) {
        shiPingDetailAnimationActivity = mshiPingDetailAnimationActivity;
        mData = Data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_CONTENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scorll_layout, null);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        } else {
            HeaderViewHolder headerViewHolder = new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_layout, null));
            return headerViewHolder;
        }
    }

    private View.OnClickListener leftonClickListener;

    public void setLeftImageClickListener(View.OnClickListener monClickListener) {
        leftonClickListener = monClickListener;
    }

    private int tvContentHeight;
    private int tvBackHeight;

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_CONTENT) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            NeiWentiDetailAdapter buZhouAdapter = new NeiWentiDetailAdapter(mData.getQuestion_data().getData(), shiPingDetailAnimationActivity);
            myViewHolder.buzhou_view.setAdapter(buZhouAdapter);
        } else {
            if (position == 0) {
                final HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                //添加描述
                if (null != mData.getDesc()) {
                    if (mData.getDesc().length() == 0) {
                        headerViewHolder.tv_content.setVisibility(View.GONE);
                        headerViewHolder.tv_back.setVisibility(View.GONE);
                        headerViewHolder.iv_arrow.setVisibility(View.GONE);
                    } else {
                        headerViewHolder.tv_content.setText(mData.getDesc());
                        headerViewHolder.tv_back.setText(mData.getDesc());
                        //测量tv_content的高度
                        ViewTreeObserver vto1 = headerViewHolder.tv_content.getViewTreeObserver();
                        vto1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                tvContentHeight = headerViewHolder.tv_content.getHeight();
                                //获得高度之后，移除监听
                                headerViewHolder.tv_content.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                            }
                        });
                        //测量tv_back的高度
                        ViewTreeObserver vto = headerViewHolder.tv_back.getViewTreeObserver();
                        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                tvBackHeight = headerViewHolder.tv_back.getHeight();
                                headerViewHolder.tv_back.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                //比较高度：
                                if (tvBackHeight > tvContentHeight) {
                                    //说明有展开的内容：
                                    //默认是关闭状态：
                                    headerViewHolder.tv_content.setTag(true);
                                    headerViewHolder.iv_arrow.setVisibility(View.VISIBLE);

                                } else {
                                    headerViewHolder.iv_arrow.setVisibility(View.GONE);
                                    headerViewHolder.tv_content.setTag(false);
                                }
                                headerViewHolder.tv_back.setVisibility(View.GONE);

                            }
                        });
                        //点击文字展开
                        headerViewHolder.tv_content.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean tag = (boolean) headerViewHolder.tv_content.getTag();
                                if (tag) {
                                    //如果是true,点击则展开：
                                    headerViewHolder.tv_content.setTag(false);
                                    headerViewHolder.tv_content.setMaxLines(Integer.MAX_VALUE);
                                    headerViewHolder.iv_arrow.setImageResource(R.drawable.shangla);

                                } else {
                                    headerViewHolder.iv_arrow.setImageResource(R.drawable.xiala);
                                    headerViewHolder.tv_content.setTag(true);
                                    headerViewHolder.tv_content.setMaxLines(3);
                                }
                            }
                        });
                        //点击图标展开
                        headerViewHolder.iv_arrow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean tag = (boolean) headerViewHolder.tv_content.getTag();
                                if (tag) {
                                    //如果是true,点击则展开：
                                    headerViewHolder.tv_content.setTag(false);
                                    headerViewHolder.tv_content.setMaxLines(Integer.MAX_VALUE);
                                    headerViewHolder.iv_arrow.setImageResource(R.drawable.shangla);
                                } else {
                                    headerViewHolder.iv_arrow.setImageResource(R.drawable.xiala);
                                    headerViewHolder.tv_content.setTag(true);
                                    headerViewHolder.tv_content.setMaxLines(3);
                                }
                            }
                        });
                    }

                } else {
                    headerViewHolder.tv_content.setVisibility(View.GONE);
                    headerViewHolder.tv_back.setVisibility(View.GONE);
                    headerViewHolder.iv_arrow.setVisibility(View.GONE);
                }
                //设置右上角头像
                Glide.with(shiPingDetailAnimationActivity).load(mData.getPath())
                        .error(R.drawable.touxiang)
                        .into(headerViewHolder.user_head_image);

                //设置关注数量
                headerViewHolder.guanzhu_number.setText(mData.getFollow() + "关注");

                //设置问题数量
                headerViewHolder.toolbar_right_button1.setText(mData.getQuestions() + "问题");

                headerViewHolder.question_number.setText(mData.getQuestions() + "问题");
                //分类标题
                headerViewHolder.guanzhu_title.setText(mData.getTitle());

                //设置关注
                headerViewHolder.guanzhu_layout_top.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("".equals(SharedPreferencesHelper.get(shiPingDetailAnimationActivity, "login_token", ""))) {
                            Intent intent = new Intent(shiPingDetailAnimationActivity, LoginActivity.class);
                            shiPingDetailAnimationActivity.startActivity(intent);
                        } else {
                            guanzhu_submit(mData.getId());
                        }
                    }
                });

                //取消关注
                headerViewHolder.huxiangguanzhu_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("".equals(SharedPreferencesHelper.get(shiPingDetailAnimationActivity, "login_token", ""))) {
                            Intent intent = new Intent(shiPingDetailAnimationActivity, LoginActivity.class);
                            shiPingDetailAnimationActivity.startActivity(intent);
                        } else {
                            quxiao_guanzhu_submit(mData.getId());
                        }
                    }
                });

                //设置关注按钮状态
                if (mData.getIs_follow().equals("1")) {
                    headerViewHolder.guanzhu_layout_top.setVisibility(View.GONE);
                    headerViewHolder.huxiangguanzhu_tv.setVisibility(View.VISIBLE);
                } else {
                    headerViewHolder.guanzhu_layout_top.setVisibility(View.VISIBLE);
                    headerViewHolder.huxiangguanzhu_tv.setVisibility(View.GONE);
                }

                headerViewHolder.toolbar_left_button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shiPingDetailAnimationActivity.finish();
                    }
                });
            }

        }
    }

    /**
     * Rxjava
     */
    protected Subscription subscription;

    private void guanzhu_submit(String user_id) {
        Map<String, String> params = new HashMap<>();
        if (null != user_id) {
            params.put("id", user_id);
        }
        Log.e("关注分类ID：", params.toString());
        subscription = Network.getInstance("关注分类", shiPingDetailAnimationActivity)
                .guanzhufenlei(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123","关注分类成功：" + result.getCode());
                                mData.setIs_follow("1");
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123","关注分类报错：" + error);
                            }
                        }, shiPingDetailAnimationActivity, true));

    }

    /**
     * 取消关注
     *
     * @param user_id
     */
    private void quxiao_guanzhu_submit(String user_id) {
        Map<String, String> params = new HashMap<>();
        if (null != user_id) {
            params.put("id", user_id);
        }
        Log.e("取消关注分类ID：", params.toString());
        subscription = Network.getInstance("取消关注分类", shiPingDetailAnimationActivity)
                .quxiao_guanzhu(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123","取消关注分类成功：" + result.getCode());
                                mData.setIs_follow("2");
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123","取消关注分类报错：" + error);
                            }
                        }, shiPingDetailAnimationActivity, true));

    }


    @Override
    public int getItemCount() {
        return mData == null ? 1 : 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_CONTENT;
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_content, tv_back, question_number;
        public ImageView iv_arrow, toolbar_left_button1;
        public CircleImageView user_head_image;
        public TextView guanzhu_number, wenti_number, guanzhu_title, toolbar_right_button1;
        public LinearLayout guanzhu_layout_top, huxiangguanzhu_tv;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            user_head_image = (CircleImageView) itemView.findViewById(R.id.user_head_image);
            guanzhu_number = (TextView) itemView.findViewById(R.id.guanzhu_number);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_back = (TextView) itemView.findViewById(R.id.tv_back);
            iv_arrow = (ImageView) itemView.findViewById(R.id.iv_arrow);
            toolbar_left_button1 = (ImageView) itemView.findViewById(R.id.toolbar_left_button1);
            wenti_number = (TextView) itemView.findViewById(R.id.wenti_number);
            guanzhu_title = (TextView) itemView.findViewById(R.id.guanzhu_title);
            huxiangguanzhu_tv = (LinearLayout) itemView.findViewById(R.id.huxiangguanzhu_tv);
            guanzhu_layout_top = (LinearLayout) itemView.findViewById(R.id.guanzhu_layout_top);
            toolbar_right_button1 = (TextView) itemView.findViewById(R.id.toolbar_right_button1);
            question_number = (TextView) itemView.findViewById(R.id.question_number);
        }


    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ListViewForScrollView buzhou_view;


        public MyViewHolder(View itemView) {
            super(itemView);
            buzhou_view = (ListViewForScrollView) itemView.findViewById(R.id.buzhou_view);
        }

    }


}

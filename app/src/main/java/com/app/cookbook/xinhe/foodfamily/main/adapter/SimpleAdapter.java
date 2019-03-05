package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.andview.refreshview.utils.LogUtils;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.callback.EditTextCallBack;
import com.app.cookbook.xinhe.foodfamily.callback.ImageBiggerCallBack;
import com.app.cookbook.xinhe.foodfamily.jiguang.MainActivity;
import com.app.cookbook.xinhe.foodfamily.main.AnserActivity;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.main.OtherUserActivity;
import com.app.cookbook.xinhe.foodfamily.main.PhotoBrowserActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.AnswerEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.WenTIDetail;
import com.app.cookbook.xinhe.foodfamily.main.webview.MJavascriptInterface;
import com.app.cookbook.xinhe.foodfamily.main.webview.MyWebViewClient;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.LabelDetailsActivity;
import com.app.cookbook.xinhe.foodfamily.util.DensityUtil;
import com.app.cookbook.xinhe.foodfamily.util.ExpandableTextView;
import com.app.cookbook.xinhe.foodfamily.util.FormatCurrentData;
import com.app.cookbook.xinhe.foodfamily.util.NormalExpandableTextView;
import com.app.cookbook.xinhe.foodfamily.util.ScreenUtilsHelper;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.StringUtils;
import com.app.cookbook.xinhe.foodfamily.util.otherUi.HorizontalListView;
import com.app.cookbook.xinhe.foodfamily.util.otherUi.ListViewForScrollView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.tencent.stat.StatService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;

public class SimpleAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<AnswerEntity> answerEntities;
    private FenLeiQuestionDetailActivity context;
    ImageBiggerCallBack imageBiggerCallBack;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_HEAD = 3;
    WenTIDetail wenTIDetail;
    protected Subscription subscription;
    RecyclerView mrecycler_view;

    public SimpleAdapter(List<AnswerEntity> list, RecyclerView recycler_view
            , WenTIDetail mwenTIDetail,
                         FenLeiQuestionDetailActivity mcontext,
                         ImageBiggerCallBack mimageBiggerCallBack) {
        imageBiggerCallBack = mimageBiggerCallBack;
        this.answerEntities = list;
        mrecycler_view = recycler_view;
        this.wenTIDetail = mwenTIDetail;
        this.wenTIDetail.setIs_zhankai(true);
        context = mcontext;
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
        long msl = ms * 1000;
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
        if (getAdapterItemCount() > 2) {
            if (position == 0) {
                return TYPE_HEAD;
            } else {
                return TYPE_ITEM;
            }
        }
        if (getAdapterItemCount() == 2) {
            if (answerEntities.size() > 0) {
                if (position == 0) {
                    return TYPE_HEAD;
                } else {
                    return TYPE_ITEM;
                }
            } else {
                if (position == 0) {
                    return TYPE_HEAD;
                } else {
                    return EMPTY_VIEW;
                }
            }
        } else {
            return EMPTY_VIEW;
        }
    }

    @Override
    public int getAdapterItemCount() {
        return answerEntities.size() > 0 ? answerEntities.size() + 1 : 2;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return new SimpleAdapterViewHolder(view, false);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        RecyclerView.ViewHolder holder = getViewHolderByViewType(viewType, parent);
        return holder;
    }

    private RecyclerView.ViewHolder getViewHolderByViewType(int viewType, ViewGroup parent) {
        View empty_view = LayoutInflater.from(context).inflate(R.layout.fenlei_answer_empty_view, parent, false);
        View answer_view = LayoutInflater.from(context).inflate(R.layout.item_recylerview, parent, false);
        View head_view = LayoutInflater.from(context).inflate(R.layout.bannerview_zhihu, parent, false);

        RecyclerView.ViewHolder holder = null;
        if (viewType == TYPE_ITEM) {
            holder = new SimpleAdapterViewHolder(answer_view, true);
        } else if (viewType == TYPE_HEAD) {
            holder = new HeaderViewHolderTwo(head_view, true);
        } else {
            holder = new EmptyViewHolder(empty_view, true);

        }
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, final int position, boolean isItem) {
        if (getAdapterItemViewType(position) == TYPE_ITEM) {
            final int simple_position = position - 1;
            SimpleAdapterViewHolder simpleAdapterViewHolder = (SimpleAdapterViewHolder) view_holder;
            if (null != answerEntities.get(simple_position).getImg_data()) {
                simpleAdapterViewHolder.answer_img_view.setVisibility(View.VISIBLE);
                Glide.with(context).load(answerEntities.get(simple_position).getImg_data().getPath())
                        //.centerCrop()
                        .asBitmap()
                        .placeholder(R.drawable.morenbg)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .error(R.drawable.morenbg)
                        .into(simpleAdapterViewHolder.answer_img_view);
                simpleAdapterViewHolder.answer_img_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, AnserActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("answer_id", answerEntities.get(simple_position).getId());
                        intent.putExtras(bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });

            } else {
                simpleAdapterViewHolder.answer_img_view.setVisibility(View.GONE);
            }
            //回答内容
            if (null == answerEntities.get(simple_position).getContent_remove() ||
                    "".equals(answerEntities.get(simple_position).getContent_remove())) {
                simpleAdapterViewHolder.question_answer.setVisibility(View.GONE);
            } else {
                simpleAdapterViewHolder.question_answer.setVisibility(View.VISIBLE);
                simpleAdapterViewHolder.question_answer.setText(answerEntities.get(simple_position).getContent_remove());
            }

            simpleAdapterViewHolder.user_top.setVisibility(View.VISIBLE);
            if (null != answerEntities.get(simple_position).getName()) {
                simpleAdapterViewHolder.user_name.setText(answerEntities.get(simple_position).getName());
            }

            Glide.with(context).load(answerEntities.get(simple_position).getUsers_avatar())
                    .asBitmap()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.touxiang)
                    .into(simpleAdapterViewHolder.user_head_image);

            if (null != answerEntities.get(simple_position).getCreated_at()) {
                simpleAdapterViewHolder.time_tv.setText(FormatCurrentData.getTimeRange(Long.valueOf(answerEntities.get(simple_position).getCreated_at())));
            }
            if (null != answerEntities.get(simple_position).getThumbs()) {
                simpleAdapterViewHolder.zan_number.setText(answerEntities.get(simple_position).getThumbs() + "个赞");
            } else {
                simpleAdapterViewHolder.zan_number.setText("0个赞");
            }

            if (null != answerEntities.get(simple_position).getComment_count()) {
                simpleAdapterViewHolder.pinglun_number.setText(answerEntities.get(simple_position).getComment_count() + "条评论");
            } else {
                simpleAdapterViewHolder.pinglun_number.setText("0条评论");
            }

            //跳转答案详情
            simpleAdapterViewHolder.question_answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Properties prop = new Properties();
                    prop.setProperty("name", "question_answer");
                    StatService.trackCustomKVEvent(context, "Details_problem_details_answer", prop);

                    Intent intent = new Intent(context, AnserActivity.class);
                    Bundle bundle = new Bundle();
                    Log.e("跳转的答", answerEntities.get(simple_position).getId());
                    bundle.putString("answer_id", answerEntities.get(simple_position).getId());
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            simpleAdapterViewHolder.user_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Properties prop = new Properties();
                    prop.setProperty("name", "user_top");
                    StatService.trackCustomKVEvent(context, "Details_problem_personal_home_page", prop);

                    Intent intent = new Intent(context, FriendPageActivity.class);
                    if (null != answerEntities.get(simple_position).getUid()) {
                        intent.putExtra("UserId", answerEntities.get(simple_position).getUid());
                        context.startActivity(intent);
                    }
                }
            });


        } else if (getAdapterItemViewType(position) == EMPTY_VIEW) {

        } else {
            final HeaderViewHolderTwo headerViewHolder = (HeaderViewHolderTwo) view_holder;
            //之前的布局
            //setHeadViews(headerViewHolder);
            setHeadViews(headerViewHolder);
        }
    }

    private void setHeadViews(final HeaderViewHolderTwo headerViewHolder) {
        //设置标题
        headerViewHolder.title_content.setText(wenTIDetail.getTitle());

        //设置内容
        headerViewHolder.expand_content_tv.setOnclickCallBack(new EditTextCallBack() {
            @Override
            public void onclick() {
                Log.e("已经点击了", "已经点击了");
                show_web_detail(headerViewHolder);
            }
        });

        //去除换行和空格
        headerViewHolder.expand_content_tv.setText(wenTIDetail.getContent_remove().replace("\n", "").replace(" ", ""));

        //设置收藏
        if (null != wenTIDetail.getIs_follow_text()) {
            headerViewHolder.shoucang_tv_number.setText(wenTIDetail.getFollow() + "人关注");
        } else {
            headerViewHolder.shoucang_tv_number.setText("0人关注");
        }
        //设置回答
        if (null != wenTIDetail.getAnswer()) {
            headerViewHolder.answer_tv_number.setText(wenTIDetail.getAnswer() + "条回答");
        } else {
            headerViewHolder.answer_tv_number.setText("0条回答");
        }
        //设置横向标签列表
        if (null != wenTIDetail.getCategory_data()) {
            HengXiangAdapter buZhouAdapter =
                    new HengXiangAdapter(wenTIDetail.getCategory_data(), context);
            headerViewHolder.horizontalListView.setAdapter(buZhouAdapter);
        }
        //跳往标签详情页
        headerViewHolder.horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Properties prop = new Properties();
                prop.setProperty("name", "horizontalListView");
                StatService.trackCustomKVEvent(context, "Details_problem_details_tag", prop);
                Intent intent = new Intent(context, LabelDetailsActivity.class);
                intent.putExtra("id", wenTIDetail.getCategory_data().get(position).getId());
                context.startActivity(intent);
            }
        });
        //根据图片的张数，判断显示的布局和逻辑
        if (null != wenTIDetail.getImg_data() && wenTIDetail.getImg_data().size() == 1) {//只有一张图片
            headerViewHolder.expand_content_tv.setVisibility(View.VISIBLE);
            headerViewHolder.normal_expand_content_tv.setVisibility(View.GONE);
            headerViewHolder.image_frame.setVisibility(View.VISIBLE);
            headerViewHolder.image_one_layout.setVisibility(View.VISIBLE);
            headerViewHolder.image_two_layout.setVisibility(View.GONE);
            headerViewHolder.image_three_layout.setVisibility(View.GONE);
            headerViewHolder.image_more_layout.setVisibility(View.GONE);
            //设置图片的宽高
            headerViewHolder.image_one_layout_image.post(new Runnable() {
                @Override
                public void run() {
                    set_image_size(headerViewHolder.image_one_layout_image, 0.93, 0.34, null);

                }
            });
            //设置逻辑
            Picasso.with(context)
                    .load(wenTIDetail.getImg_data().get(0).getPath())
                    .error(R.drawable.morenbg)
                    .into(headerViewHolder.image_one_layout_image);

            /*Glide.with(context).load(wenTIDetail.getImg_data().get(0).getPath())
                    .asBitmap()
                    .placeholder(R.drawable.morenbg)
                    .error(R.drawable.morenbg)
                    .into(headerViewHolder.image_one_layout_image);*/

        } else if (null != wenTIDetail.getImg_data() && wenTIDetail.getImg_data().size() == 2) {//只有两张图片
            headerViewHolder.expand_content_tv.setVisibility(View.VISIBLE);
            headerViewHolder.normal_expand_content_tv.setVisibility(View.GONE);
            headerViewHolder.image_frame.setVisibility(View.VISIBLE);
            headerViewHolder.image_one_layout.setVisibility(View.GONE);
            headerViewHolder.image_two_layout.setVisibility(View.VISIBLE);
            headerViewHolder.image_three_layout.setVisibility(View.GONE);
            headerViewHolder.image_more_layout.setVisibility(View.GONE);
            //设置图片的宽高
            headerViewHolder.image_two_layout_iamge_one.post(new Runnable() {
                @Override
                public void run() {
                    set_image_size(headerViewHolder.image_two_layout_iamge_one, 0.46, 0.56, null);
                }
            });
            headerViewHolder.image_two_layout_iamge_two.post(new Runnable() {
                @Override
                public void run() {
                    set_image_size(headerViewHolder.image_two_layout_iamge_two, 0.46, 0.56, null);

                }
            });
            Picasso.with(context)
                    .load(wenTIDetail.getImg_data().get(0).getPath())
                    .error(R.drawable.morenbg)
                    .into(headerViewHolder.image_two_layout_iamge_one);
            Picasso.with(context)
                    .load(wenTIDetail.getImg_data().get(1).getPath())
                    .error(R.drawable.morenbg)
                    .into(headerViewHolder.image_two_layout_iamge_two);
            //设置逻辑
            /*Glide.with(context).load(wenTIDetail.getImg_data().get(0).getPath())
                    .asBitmap()
                    .placeholder(R.drawable.morenbg)
                    .error(R.drawable.morenbg)
                    .into(headerViewHolder.image_two_layout_iamge_one);

            Glide.with(context).load(wenTIDetail.getImg_data().get(1).getPath())
                    .asBitmap()
                    .placeholder(R.drawable.morenbg)
                    .error(R.drawable.morenbg)
                    .into(headerViewHolder.image_two_layout_iamge_two);*/


        } else if (null != wenTIDetail.getImg_data() && wenTIDetail.getImg_data().size() == 3) {//三张图片
            headerViewHolder.expand_content_tv.setVisibility(View.VISIBLE);
            headerViewHolder.normal_expand_content_tv.setVisibility(View.GONE);
            headerViewHolder.image_frame.setVisibility(View.VISIBLE);
            headerViewHolder.image_one_layout.setVisibility(View.GONE);
            headerViewHolder.image_two_layout.setVisibility(View.GONE);
            headerViewHolder.image_three_layout.setVisibility(View.VISIBLE);
            headerViewHolder.image_more_layout.setVisibility(View.GONE);

            //设置图片的宽高
            headerViewHolder.image_three_layout_iamge_one.post(new Runnable() {
                @Override
                public void run() {
                    set_image_size(headerViewHolder.image_three_layout_iamge_one, 0.3, 0.65, null);

                }
            });
            headerViewHolder.image_three_layout_iamge_two.post(new Runnable() {
                @Override
                public void run() {
                    set_image_size(headerViewHolder.image_three_layout_iamge_two, 0.3, 0.65, null);

                }
            });
            headerViewHolder.image_three_layout_iamge_three.post(new Runnable() {
                @Override
                public void run() {
                    set_image_size(headerViewHolder.image_three_layout_iamge_three, 0.3, 0.65, null);

                }
            });

            Picasso.with(context)
                    .load(wenTIDetail.getImg_data().get(0).getPath())
                    .error(R.drawable.morenbg)
                    .into(headerViewHolder.image_three_layout_iamge_one);
            Picasso.with(context)
                    .load(wenTIDetail.getImg_data().get(1).getPath())
                    .error(R.drawable.morenbg)
                    .into(headerViewHolder.image_three_layout_iamge_two);
            Picasso.with(context)
                    .load(wenTIDetail.getImg_data().get(2).getPath())
                    .error(R.drawable.morenbg)
                    .into(headerViewHolder.image_three_layout_iamge_three);
            //设置逻辑
           /* Glide.with(context).load(wenTIDetail.getImg_data().get(0).getPath())
                    .asBitmap()
                    .placeholder(R.drawable.morenbg)
                    .error(R.drawable.morenbg)
                    .into(headerViewHolder.image_three_layout_iamge_one);
            Glide.with(context).load(wenTIDetail.getImg_data().get(1).getPath())
                    .asBitmap()
                    .placeholder(R.drawable.morenbg)
                    .error(R.drawable.morenbg)
                    .into(headerViewHolder.image_three_layout_iamge_two);
            Glide.with(context).load(wenTIDetail.getImg_data().get(2).getPath())
                    .asBitmap()
                    .placeholder(R.drawable.morenbg)
                    .error(R.drawable.morenbg)
                    .into(headerViewHolder.image_three_layout_iamge_three);*/


        } else if (null != wenTIDetail.getImg_data() && wenTIDetail.getImg_data().size() > 3) {//大于三张图片
            headerViewHolder.expand_content_tv.setVisibility(View.VISIBLE);
            headerViewHolder.normal_expand_content_tv.setVisibility(View.GONE);
            headerViewHolder.image_frame.setVisibility(View.VISIBLE);
            headerViewHolder.image_one_layout.setVisibility(View.GONE);
            headerViewHolder.image_two_layout.setVisibility(View.GONE);
            headerViewHolder.image_three_layout.setVisibility(View.GONE);
            headerViewHolder.image_more_layout.setVisibility(View.VISIBLE);

            //设置图片的宽高
            headerViewHolder.image_more_layout_iamge_one.post(new Runnable() {
                @Override
                public void run() {
                    set_image_size(headerViewHolder.image_more_layout_iamge_one, 0.3, 0.65, headerViewHolder.mengban_bg);

                }
            });
            headerViewHolder.image_more_layout_iamge_two.post(new Runnable() {
                @Override
                public void run() {
                    set_image_size(headerViewHolder.image_more_layout_iamge_two, 0.3, 0.65, headerViewHolder.mengban_bg);

                }
            });
            headerViewHolder.image_more_layout_iamge_three.post(new Runnable() {
                @Override
                public void run() {
                    set_image_size(headerViewHolder.image_more_layout_iamge_three, 0.3, 0.65, headerViewHolder.mengban_bg);

                }
            });

            Picasso.with(context)
                    .load(wenTIDetail.getImg_data().get(0).getPath())
                    .error(R.drawable.morenbg)
                    .into(headerViewHolder.image_more_layout_iamge_one);
            Picasso.with(context)
                    .load(wenTIDetail.getImg_data().get(1).getPath())
                    .error(R.drawable.morenbg)
                    .into(headerViewHolder.image_more_layout_iamge_two);
            Picasso.with(context)
                    .load(wenTIDetail.getImg_data().get(2).getPath())
                    .error(R.drawable.morenbg)
                    .into(headerViewHolder.image_more_layout_iamge_three);

            //设置逻辑
           /* Glide.with(context).load(wenTIDetail.getImg_data().get(0).getPath())
                    .asBitmap()
                    .placeholder(R.drawable.morenbg)
                    .error(R.drawable.morenbg)
                    .into(headerViewHolder.image_more_layout_iamge_one);

            Glide.with(context).load(wenTIDetail.getImg_data().get(1).getPath())
                    .asBitmap()
                    .placeholder(R.drawable.morenbg)
                    .error(R.drawable.morenbg)
                    .into(headerViewHolder.image_more_layout_iamge_two);

            Glide.with(context).load(wenTIDetail.getImg_data().get(2).getPath())
                    .asBitmap()
                    .placeholder(R.drawable.morenbg)
                    .error(R.drawable.morenbg)
                    .into(headerViewHolder.image_more_layout_iamge_three);*/

            int more_number = wenTIDetail.getImg_data().size() - 3;
            headerViewHolder.number_tv.setText("+" + more_number);


        } else {//一张图片都没有
            Log.e("萨芬几十块两节课富士康", "sjlfdk");
            headerViewHolder.image_frame.setVisibility(View.GONE);
            headerViewHolder.expand_content_tv.setVisibility(View.GONE);
            headerViewHolder.normal_expand_content_tv.setVisibility(View.VISIBLE);
            headerViewHolder.normal_expand_content_tv.setText(wenTIDetail.getContent_remove());
            headerViewHolder.normal_expand_content_tv.setOnclickCallBack(new EditTextCallBack() {
                @Override
                public void onclick() {
                    Log.e("已经点击了", "已经点击了");
                    show_web_detail(headerViewHolder);
                }
            });
        }

        //设置展开详情页
        headerViewHolder.image_one_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_web_detail(headerViewHolder);
            }
        });
        headerViewHolder.image_two_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_web_detail(headerViewHolder);
            }
        });
        headerViewHolder.image_three_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_web_detail(headerViewHolder);
            }
        });
        headerViewHolder.image_more_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_web_detail(headerViewHolder);
            }
        });

    }

    private void show_web_detail(HeaderViewHolderTwo mheaderViewHolder) {
        mheaderViewHolder.content_remove_layout.setVisibility(View.GONE);
        mheaderViewHolder.content_webview.setVisibility(View.VISIBLE);
        //设置webview内容跟样式
        final String linkCss = "<style type=\"text/css\"> " +
                "img {" +
                "max-width:100%;" +
                "max-height:100%;" +
                "margin:5px auto;" +
                "display:block;" +
                "}" +
                "p {" +
                "font-size:18px;" +
                "line-height:25px " +
                "}" +
                "body {" +
                "word-wrap:break-word;" +
                "font-family:Arial;" +
                "margin-right:15px;" +
                "margin-left:15px;" +
                "margin-top:5px;" +
                "font-size:15px;" +
                "}" + "</style>";
        String html = "<html><header>" + linkCss + "</header><body>" + wenTIDetail.getContent().replace("\\'", "'") + "</body></html>";
        mheaderViewHolder.content_webview.getSettings().setJavaScriptEnabled(true);
        mheaderViewHolder.content_webview.getSettings().setAppCacheEnabled(true);
        mheaderViewHolder.content_webview.getSettings().setDatabaseEnabled(true);
        mheaderViewHolder.content_webview.getSettings().setDomStorageEnabled(true);
        mheaderViewHolder.content_webview.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        //点击图片查看大图
        String[] imageUrls = StringUtils.returnImageUrlsFromHtml(html);
        mheaderViewHolder.content_webview.addJavascriptInterface(new
                MJavascriptInterface(context, imageUrls), "imagelistener");
        mheaderViewHolder.content_webview.setWebViewClient(new MyWebViewClient());
    }

    /**
     * @param roundedImageView
     * @param beishu           宽是高的多少倍
     */
    private void set_image_size(RoundedImageView roundedImageView, Double mwidth_beishu, Double beishu, LinearLayout mengban_bg) {
        if (null != mengban_bg) {//设置蒙版
            int screenWidth = (int) Math.round(ScreenUtilsHelper.getScreenWidth(context) * mwidth_beishu);
            Log.e("设置的宽度", screenWidth + "");
            ViewGroup.LayoutParams lp = mengban_bg.getLayoutParams();
            Double height = screenWidth * beishu;
            lp.width = screenWidth;
            lp.height = (int) Math.round(height);
            mengban_bg.setLayoutParams(lp);
            Log.e("设置的高度", "" + (int) Math.round(height));
        }
        int screenWidth = (int) Math.round(ScreenUtilsHelper.getScreenWidth(context) * mwidth_beishu);
        Log.e("设置的宽度", screenWidth + "");
        ViewGroup.LayoutParams lp = roundedImageView.getLayoutParams();
        Double height = screenWidth * beishu;
        lp.width = screenWidth;
        lp.height = (int) Math.round(height);
        roundedImageView.setLayoutParams(lp);
        Log.e("设置的高度", "" + (int) Math.round(height));


    }


    public void remove(int position) {
        remove(answerEntities, position);
    }

    public void clear() {
        clear(answerEntities);
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public EmptyViewHolder(View item_view, boolean isItem) {
            super(item_view);
            if (isItem) {
                this.view = item_view;
            }
        }
    }

    public class SimpleAdapterViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout rootView;
        TextView question_content, question_answer, user_name, time_tv, zan_number, shoucang_number, pinglun_number;
        RoundedImageView answer_img_view;
        CircleImageView user_head_image;
        RelativeLayout user_top;

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                rootView = itemView
                        .findViewById(R.id.card_view);
                pinglun_number = itemView.findViewById(R.id.pinglun_number);
                question_content = itemView.findViewById(R.id.question_content);
                answer_img_view = itemView.findViewById(R.id.answer_img_view);
                question_answer = itemView.findViewById(R.id.question_answer);
                user_top = itemView.findViewById(R.id.user_top);
                user_name = itemView.findViewById(R.id.user_name);
                user_head_image = itemView.findViewById(R.id.user_head_image);
                time_tv = itemView.findViewById(R.id.time_tv);
                zan_number = itemView.findViewById(R.id.zan_number);
                shoucang_number = itemView.findViewById(R.id.shoucang_number);
            }

        }
    }


    public class HeaderViewHolderTwo extends RecyclerView.ViewHolder {
        public View view;
        public LinearLayout image_one_layout, image_two_layout, image_three_layout, image_more_layout;
        public RoundedImageView image_one_layout_image, image_two_layout_iamge_one,
                image_two_layout_iamge_two, image_three_layout_iamge_one,
                image_three_layout_iamge_two, image_three_layout_iamge_three, image_more_layout_iamge_one,
                image_more_layout_iamge_two, image_more_layout_iamge_three;
        public TextView number_tv, title_content, shoucang_tv_number, answer_tv_number;
        public LinearLayout image_frame, mengban_bg, content_remove_layout;
        public HorizontalListView horizontalListView;
        public ExpandableTextView expand_content_tv;
        public NormalExpandableTextView normal_expand_content_tv;
        public WebView content_webview;

        public HeaderViewHolderTwo(View item_view, boolean isItem) {
            super(item_view);
            if (isItem) {
                this.view = item_view;
                image_one_layout = view.findViewById(R.id.image_one_layout);
                image_one_layout_image = view.findViewById(R.id.image_one_layout_image);
                image_two_layout = view.findViewById(R.id.image_two_layout);
                image_two_layout_iamge_one = view.findViewById(R.id.image_two_layout_iamge_one);
                image_two_layout_iamge_two = view.findViewById(R.id.image_two_layout_iamge_two);
                image_three_layout = view.findViewById(R.id.image_three_layout);
                image_three_layout_iamge_one = view.findViewById(R.id.image_three_layout_iamge_one);
                image_three_layout_iamge_two = view.findViewById(R.id.image_three_layout_iamge_two);
                image_three_layout_iamge_three = view.findViewById(R.id.image_three_layout_iamge_three);
                image_more_layout = view.findViewById(R.id.image_more_layout);
                image_more_layout_iamge_one = view.findViewById(R.id.image_more_layout_iamge_one);
                image_more_layout_iamge_two = view.findViewById(R.id.image_more_layout_iamge_two);
                image_more_layout_iamge_three = view.findViewById(R.id.image_more_layout_iamge_three);
                number_tv = view.findViewById(R.id.number_tv);
                image_frame = view.findViewById(R.id.image_frame);
                mengban_bg = view.findViewById(R.id.mengban_bg);
                title_content = view.findViewById(R.id.title_content);
                horizontalListView = view.findViewById(R.id.horizontalListView);
                expand_content_tv = view.findViewById(R.id.expand_content_tv);
                shoucang_tv_number = view.findViewById(R.id.shoucang_tv_number);
                answer_tv_number = view.findViewById(R.id.answer_tv_number);
                content_webview = view.findViewById(R.id.content_webview);
                content_remove_layout = view.findViewById(R.id.content_remove_layout);
                normal_expand_content_tv = view.findViewById(R.id.normal_expand_content_tv);
            }
        }

    }


    public AnswerEntity getItem(int position) {
        if (position < answerEntities.size())
            return answerEntities.get(position);
        else
            return null;
    }


}
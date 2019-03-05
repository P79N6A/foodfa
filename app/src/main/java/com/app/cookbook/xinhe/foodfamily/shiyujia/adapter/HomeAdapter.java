package com.app.cookbook.xinhe.foodfamily.shiyujia.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.HomeItem;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.JudgeWifi;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.MD5Utils;

import com.app.cookbook.xinhe.foodfamily.shiyujia.view.GlideImageLoader;
import com.app.cookbook.xinhe.foodfamily.shiyujia.view.Love;
import com.app.cookbook.xinhe.foodfamily.util.CircleImageView;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.like.LikeButton;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chuangyuan.ycj.videolibrary.listener.VideoInfoListener;
import chuangyuan.ycj.videolibrary.video.ExoUserPlayer;
import chuangyuan.ycj.videolibrary.video.VideoPlayerManager;
import chuangyuan.ycj.videolibrary.widget.VideoPlayerView;
import rx.Subscription;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.Holder> {

    private Context context;
    private LayoutInflater inflater;
    private List<HomeItem.DataBean> homeItems;
    private List<String> imageList = new ArrayList<>();

    //点击次数
    private int count = 0;
    //第一次点击时间
    private Handler handler;
    private Runnable runnable = null;
    private String url = "";
    //记录滑动位置
    private int currenItem = 0;


    public HomeAdapter(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.inflater = LayoutInflater.from(context);
    }

    public void setHomeItems(List<HomeItem.DataBean> homeItems) {
        this.homeItems = homeItems;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    @Override
    public int getItemViewType(int position) {
        return homeItems.get(position).getStatus();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Holder holder = null;
        switch (viewType) {
            case 1:
                holder = new VideoHolder(inflater.inflate(R.layout.home_item_adapter, parent, false));
                break;
            case 2:
                holder = new ImageTextHolder(inflater.inflate(R.layout.image_text_item, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        holder.bindHolder(homeItems.get(position), position);
        currenItem = position;
    }

    @Override
    public int getItemCount() {
        return homeItems == null ? 0 : homeItems.size();
    }

    public abstract class Holder extends RecyclerView.ViewHolder {
        public RelativeLayout main_layout;
        public RelativeLayout head_layout;
        public CircleImageView im_head;
        public TextView user_name;
        public LinearLayout more_btn;
        public Banner home_kanner;
        ExoUserPlayer userPlayer;
        VideoPlayerView playerView;
        public RelativeLayout start_layout;
        public RelativeLayout content_layout;
        public Love love_btn;
        public TextView zan_layout;
        public TextView comm_layout;
        public TextView coll_layout;
        public TextView content_tv;
        public LikeButton like_btn;
        public ImageView comm_btn;
        public LikeButton collect_btn;
        public ImageView share_btn;
        public TextView tv;
        public ImageView preview_image;
        public ImageView bg_image;

        public Holder(View itemView) {
            super(itemView);
        }

        public abstract void bindHolder(HomeItem.DataBean data, int position);
    }

    //图文自布局
    public class ImageTextHolder extends Holder {

        public ImageTextHolder(View itemView) {
            super(itemView);
            main_layout = itemView.findViewById(R.id.main_layout);
            head_layout = itemView.findViewById(R.id.head_layout);
            im_head = itemView.findViewById(R.id.im_head);
            user_name = itemView.findViewById(R.id.user_name);
            more_btn = itemView.findViewById(R.id.more_btn);
            home_kanner = itemView.findViewById(R.id.home_kanner);
            content_layout = itemView.findViewById(R.id.content_layout);
            zan_layout = itemView.findViewById(R.id.zan_layout);
            comm_layout = itemView.findViewById(R.id.comm_layout);
            coll_layout = itemView.findViewById(R.id.coll_layout);
            content_tv = itemView.findViewById(R.id.content_tv);
            like_btn = itemView.findViewById(R.id.like_btn);
            comm_btn = itemView.findViewById(R.id.comm_btn);
            collect_btn = itemView.findViewById(R.id.collect_btn);
            share_btn = itemView.findViewById(R.id.share_btn);
            love_btn = itemView.findViewById(R.id.love_btn);
        }

        private int pos;

        @Override
        public void bindHolder(final HomeItem.DataBean data, final int position) {
            pos = position;
            if (data.getUser_data() != null) {
                Glide.with(context).load(data.getUser_data().getAvatar())
                        .error(R.drawable.touxiang)
                        .into(im_head);
                user_name.setText(data.getUser_data().getName());
            }
            if ("1".equals(data.getIs_thumbs())) {
                like_btn.setLiked(true);
                like_btn.setLikeDrawableRes(R.drawable.home_like_on);
            } else {
                like_btn.setLiked(false);
                like_btn.setUnlikeDrawableRes(R.drawable.home_like_off);
            }
            if ("1".equals(data.getIs_collect())) {
                collect_btn.setLiked(true);
                collect_btn.setLikeDrawableRes(R.drawable.home_collect_on);
            } else {
                collect_btn.setLiked(false);
                collect_btn.setUnlikeDrawableRes(R.drawable.home_collect_off);
            }
            if (data.getImage_data() != null) {
                for (int i = 0; i < data.getImage_data().size(); i++) {
                    imageList.add(data.getImage_data().get(i).getPath());
                }
                data.setImageList(imageList);
            }
            Resources resources = context.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) home_kanner.getLayoutParams();
            if (height < 1900) {
                lp.height = width;
            } else {
                lp.height = height - 800;
            }
            home_kanner.setLayoutParams(lp);
            if (data.getImageList() != null) {
                home_kanner.setImages(data.getImageList())
                        .setImageLoader(new GlideImageLoader())
                        .start();
                home_kanner.setIndicatorGravity(BannerConfig.RIGHT);
            }
            imageList.clear();
            zan_layout.setText(data.getThumb_number() + "点赞 ");
            comm_layout.setText(data.getComent_number() + "评论 ");
            coll_layout.setText(data.getCollection_number() + "收藏");
            content_tv.setText(data.getContent());
            FrameLayout.LayoutParams lp2 = (FrameLayout.LayoutParams) love_btn.getLayoutParams();
            if (height < 1900) {
                lp2.height = width - 120;
            } else {
                lp2.height = height - 920;
            }
            love_btn.setLayoutParams(lp2);


            head_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = 400;
                    msg.arg1 = pos;
                    handler.sendMessage(msg);
                }
            });

            more_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = 401;
                    msg.arg1 = pos;
                    handler.sendMessage(msg);
                }
            });

            like_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if ("1".equals(data.getIs_thumbs())) {
                        if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                            like_btn.setLiked(true);
                            like_btn.setUnlikeDrawableRes(R.drawable.home_like_on);
                            intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        } else {
                            Message msg = new Message();
                            msg.what = 4021;
                            msg.arg1 = pos;
                            handler.sendMessage(msg);
                            int comment_number = Integer.valueOf(data.getThumb_number());
                            data.setThumb_number(String.valueOf(comment_number - 1));
                            homeItems.get(pos).setIs_thumbs("2");
                            zan_layout.setText(data.getThumb_number() + "点赞 ");
                            like_btn.setLiked(false);
                            like_btn.setUnlikeDrawableRes(R.drawable.home_like_off);
                        }
                    } else {
                        if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                            like_btn.setLiked(false);
                            like_btn.setUnlikeDrawableRes(R.drawable.home_like_off);
                            intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        } else {
                            Message msg = new Message();
                            msg.what = 402;
                            msg.arg1 = pos;
                            handler.sendMessage(msg);
                            int comment_number = Integer.valueOf(data.getThumb_number());
                            data.setThumb_number(String.valueOf(comment_number + 1));
                            homeItems.get(pos).setIs_thumbs("1");
                            zan_layout.setText(data.getThumb_number() + "点赞 ");
                            like_btn.setLiked(true);
                            like_btn.setLikeDrawableRes(R.drawable.home_like_on);
                        }
                    }
                }
            });
            comm_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = 403;
                    msg.arg1 = pos;
                    handler.sendMessage(msg);
                }
            });
            collect_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if ("1".equals(data.getIs_collect())) {
                        if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                            like_btn.setLiked(true);
                            like_btn.setUnlikeDrawableRes(R.drawable.home_like_on);
                            intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        } else {
                            Message msg = new Message();
                            msg.what = 4041;
                            msg.arg1 = pos;
                            handler.sendMessage(msg);
                            int Collection_number = Integer.valueOf(data.getCollection_number());
                            data.setCollection_number(String.valueOf(Collection_number - 1));
                            homeItems.get(pos).setIs_collect("2");
                            coll_layout.setText(data.getCollection_number() + "收藏");
                            collect_btn.setLiked(false);
                            collect_btn.setUnlikeDrawableRes(R.drawable.home_collect_off);
                        }
                    } else {
                        if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                            like_btn.setLiked(false);
                            like_btn.setUnlikeDrawableRes(R.drawable.home_like_off);
                            intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        } else {
                            Message msg = new Message();
                            msg.what = 404;
                            msg.arg1 = pos;
                            handler.sendMessage(msg);
                            int Collection_number = Integer.valueOf(data.getCollection_number());
                            data.setCollection_number(String.valueOf(Collection_number + 1));
                            homeItems.get(pos).setIs_collect("1");
                            coll_layout.setText(data.getCollection_number() + "收藏");
                            collect_btn.setLiked(true);
                            collect_btn.setLikeDrawableRes(R.drawable.home_collect_on);
                        }
                    }
                }
            });
            share_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = 405;
                    msg.arg1 = pos;
                    handler.sendMessage(msg);
                }
            });

            home_kanner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    Message msg = new Message();
                    msg.what = 406;
                    msg.arg1 = pos;
                    handler.sendMessage(msg);
                    count++;
                    if (count == 1) {
                        //记录第一次点击时间
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.what = 4061;
                                msg.arg1 = pos;
                                handler.sendMessage(msg);
                                count = 0;
                            }
                        };
                        handler.postDelayed(runnable, 500);
                    } else {
                        handler.removeCallbacks(runnable);
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                count = 0;
                                if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    context.startActivity(intent);
                                } else {
                                    if (!"1".equals(homeItems.get(pos).getIs_thumbs())) {
                                        int comment_number = Integer.valueOf(data.getThumb_number());
                                        data.setThumb_number(String.valueOf(comment_number + 1));
                                        homeItems.get(pos).setIs_thumbs("1");
                                        zan_layout.setText(data.getThumb_number() + "点赞 ");
                                        like_btn.setLiked(true);
                                        like_btn.setLikeDrawableRes(R.drawable.home_like_on);
                                    }
                                }
                            }
                        };
                        handler.postDelayed(runnable, 500);
                    }
                }
            });

            //条目点击时间
            content_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = 408;
                    msg.arg1 = pos;
                    handler.sendMessage(msg);
                }
            });
        }
    }

    private View view;

    //视频子布局
    public class VideoHolder extends Holder {

        public VideoHolder(View itemView) {
            super(itemView);
            view = itemView;
            main_layout = itemView.findViewById(R.id.main_layout);
            head_layout = itemView.findViewById(R.id.head_layout);
            im_head = itemView.findViewById(R.id.im_head);
            user_name = itemView.findViewById(R.id.user_name);
            more_btn = itemView.findViewById(R.id.more_btn);
            content_layout = itemView.findViewById(R.id.content_layout);
            zan_layout = itemView.findViewById(R.id.zan_layout);
            comm_layout = itemView.findViewById(R.id.comm_layout);
            coll_layout = itemView.findViewById(R.id.coll_layout);
            content_tv = itemView.findViewById(R.id.content_tv);
            like_btn = itemView.findViewById(R.id.like_btn);
            comm_btn = itemView.findViewById(R.id.comm_btn);
            collect_btn = itemView.findViewById(R.id.collect_btn);
            share_btn = itemView.findViewById(R.id.share_btn);
            love_btn = itemView.findViewById(R.id.love_btn);
            start_layout = itemView.findViewById(R.id.start_layout);
            playerView = itemView.findViewById(R.id.exo_play_context_id);
            bg_image = itemView.findViewById(R.id.bg_image);
            preview_image = itemView.findViewById(R.id.preview_image);
            tv = itemView.findViewById(R.id.tv);
            userPlayer = new VideoPlayerManager.Builder(VideoPlayerManager.TYPE_PLAY_USER, playerView).create();
        }

        private int pos;

        @Override
        public void bindHolder(final HomeItem.DataBean data, final int position) {
            pos = position;
            if (data.getUser_data() != null) {
                Glide.with(context).load(data.getUser_data().getAvatar())
                        .error(R.drawable.touxiang)
                        .into(im_head);
                user_name.setText(data.getUser_data().getName());
            }
            if ("1".equals(data.getIs_thumbs())) {
                like_btn.setLiked(true);
                like_btn.setLikeDrawableRes(R.drawable.home_like_on);
            } else {
                like_btn.setLiked(false);
                like_btn.setUnlikeDrawableRes(R.drawable.home_like_off);
            }
            if ("1".equals(data.getIs_collect())) {
                collect_btn.setLiked(true);
                collect_btn.setLikeDrawableRes(R.drawable.home_collect_on);
            } else {
                collect_btn.setLiked(false);
                collect_btn.setUnlikeDrawableRes(R.drawable.home_collect_off);
            }
            zan_layout.setText(data.getThumb_number() + "点赞 ");
            comm_layout.setText(data.getComent_number() + "评论 ");
            coll_layout.setText(data.getCollection_number() + "收藏");
            if (!TextUtils.isEmpty(data.getContent())) {
                content_tv.setVisibility(View.VISIBLE);
                content_tv.setText(data.getContent());
            } else {
                content_tv.setVisibility(View.GONE);
            }
            Resources resources = context.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) playerView.getLayoutParams();
            if (height < 1900) {
                lp.height = width;
            } else {
                lp.height = height - 800;
            }
            playerView.setLayoutParams(lp);
//            RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) playerView.getPreviewImage().getLayoutParams();
//            if (height < 1900) {
//                lp1.height = width;
//            } else {
//                lp1.height = height - 730;
//            }
//            playerView.getPreviewImage().setLayoutParams(lp1);
            FrameLayout.LayoutParams lp2 = (FrameLayout.LayoutParams) love_btn.getLayoutParams();
            if (height < 1900) {
                lp2.height = width - 120;
            } else {
                lp2.height = height - 920;
            }
            tv.setLayoutParams(lp2);
            love_btn.setLayoutParams(lp2);
            if (data.getVideo_data() != null) {
                if ("1".equals(data.getVideo_data().getIs_transcoding())) {
                    url = data.getVideo_data().getVideo_path();
                    userPlayer.setPlayUri(url);
                    //设置列表item播放当前进度一定设置.不然不会保存进度
                    userPlayer.setTag(getAdapterPosition());
                    playerView.setWGh(false);
                    playerView.setShowBack(false);

                    Glide.with(context)
                            .load(data.getVideo_data().getFace_path())
                            .into(playerView.getPreviewImage());
                } else if ("2".equals(data.getVideo_data().getIs_transcoding())) {
                    String path = data.getVideo_data().getTranscoding_data().get(0).getTranscoding_path();
                    //链接头部
                    String urlHead = path.substring(0, path.indexOf(".com")) + ".com";
                    //链接中间
                    String urlMiddle = path.substring(25, path.indexOf("?"));
                    //链接尾部
                    String newStr = path.substring(path.indexOf("?auth_key="), path.length());
                    String urlTail = newStr.substring(10, newStr.length());
                    String time = String.valueOf(Calendar.getInstance().getTimeInMillis());
                    String newTime = time.substring(0, 10);
                    String newUrl = MD5Utils.encode(urlMiddle + "-" + newTime + "-0-0-SHoXEVJALKeRij7JBnLgAIEDMPPKwgL2");
                    userPlayer.setPlayUri(urlHead + urlMiddle + "?auth_key=" + newTime + "-0-0-" + newUrl);
                    //设置列表item播放当前进度一定设置.不然不会保存进度
                    userPlayer.setTag(getAdapterPosition());
                    //如果使用自定义预览的布局，播放器标题根据业务是否设置
                    playerView.setWGh(false);
                    playerView.setShowBack(false);
                }
                Glide.with(context)
                        .load(data.getVideo_data().getFace_path())
                        .into(playerView.getPreviewImage());

                Glide.with(context)
                        .load(data.getVideo_data().getFace_path())
                        .into(bg_image);

                if ("1".equals(Contacts.isVideoPlay)) {
                    if (Contacts.isPlay == true) {
                        userPlayer.startPlayer();
                    } else if (JudgeWifi.isWifiAvailable(context) == true) {
                        if (Contacts.isPlay == true) {
                            userPlayer.startPlayer();
                        }
                    } else if ("2".equals(SharedPreferencesHelper.get(context, "isWiFI", ""))) {
                        if (!userPlayer.isPlaying()) {
                            userPlayer.startPlayer();
                        }
                    } else {
                        if (Contacts.isPlay == true) {
                            if (Contacts.currPosition != 0 && Contacts.currItem == pos) {
                                userPlayer.setPosition(Contacts.currPosition);
                                Contacts.currPosition = 0;
                                Contacts.currItem = 0;
                            }
                            userPlayer.startPlayer();
                        }
                    }
                }
                userPlayer.setOnPlayClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //处理业务操作 完成后
                        userPlayer.startPlayer();//开始播放
                    }
                });

                userPlayer.addVideoInfoListener(new VideoInfoListener() {
                    @Override
                    public void onPlayStart(long currPosition) {
//                        Log.e("123", "   onPlayStart  ");
                        Contacts.isPlay = true;
                        if ("0".equals(iswifi)) {
                            getWIFIStart("2");
                        }
                        love_btn.setVisibility(View.VISIBLE);
                        tv.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingChanged() {
//                        Log.e("1234", "   onLoadingChanged  ");
                        love_btn.setVisibility(View.GONE);
                        tv.setVisibility(View.GONE);
                    }

                    @Override
                    public void onPlayerError(@Nullable ExoPlaybackException e) {
                        Log.e("1234", "   onPlayerError  ");
                        love_btn.setVisibility(View.GONE);
                        tv.setVisibility(View.GONE);
                    }

                    @Override
                    public void onPlayEnd() {
                        love_btn.setVisibility(View.GONE);
                        tv.setVisibility(View.GONE);
                    }

                    @Override
                    public void isPlaying(boolean playWhenReady) {
                        if (!userPlayer.isPlaying()) {
                            love_btn.setVisibility(View.GONE);
                            tv.setVisibility(View.GONE);
                        }
                    }
                });
            }

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = 406;
                    msg.arg1 = pos;
                    handler.sendMessage(msg);
                    count++;
                    if (count == 1) {
                        //记录第一次点击时间
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                count = 0;
                            }
                        };
                        handler.postDelayed(runnable, 500);
                    } else {
                        handler.removeCallbacks(runnable);
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                count = 0;
                                if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    context.startActivity(intent);
                                } else {
                                    if (!"1".equals(homeItems.get(pos).getIs_thumbs())) {
                                        int comment_number = Integer.valueOf(data.getThumb_number());
                                        data.setThumb_number(String.valueOf(comment_number + 1));
                                        homeItems.get(pos).setIs_thumbs("1");
                                        zan_layout.setText(data.getThumb_number() + "点赞 ");
                                        like_btn.setLiked(true);
                                        like_btn.setLikeDrawableRes(R.drawable.home_like_on);
                                    }
                                }
                            }
                        };
                        handler.postDelayed(runnable, 500);
                    }
                }
            });

            head_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    love_btn.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);
                    Message msg = new Message();
                    msg.what = 400;
                    msg.arg1 = pos;
                    handler.sendMessage(msg);
                }
            });

            more_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    love_btn.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);
                    Message msg = new Message();
                    msg.what = 401;
                    msg.arg1 = pos;
                    handler.sendMessage(msg);
                }
            });


            like_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if ("1".equals(data.getIs_thumbs())) {
                        if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                            like_btn.setLiked(true);
                            like_btn.setUnlikeDrawableRes(R.drawable.home_like_on);
                            intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        } else {
                            Message msg = new Message();
                            msg.what = 4021;
                            msg.arg1 = pos;
                            handler.sendMessage(msg);
                            int comment_number = Integer.valueOf(data.getThumb_number());
                            data.setThumb_number(String.valueOf(comment_number - 1));
                            homeItems.get(pos).setIs_thumbs("2");
                            zan_layout.setText(data.getThumb_number() + "点赞 ");
                            like_btn.setLiked(false);
                            like_btn.setUnlikeDrawableRes(R.drawable.home_like_off);
                        }
                    } else {
                        if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                            like_btn.setLiked(false);
                            like_btn.setUnlikeDrawableRes(R.drawable.home_like_off);
                            intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        } else {
                            Message msg = new Message();
                            msg.what = 402;
                            msg.arg1 = pos;
                            handler.sendMessage(msg);
                            int comment_number = Integer.valueOf(data.getThumb_number());
                            data.setThumb_number(String.valueOf(comment_number + 1));
                            homeItems.get(pos).setIs_thumbs("1");
                            zan_layout.setText(data.getThumb_number() + "点赞 ");
                            like_btn.setLiked(true);
                            like_btn.setLikeDrawableRes(R.drawable.home_like_on);
                        }
                    }
                }
            });
            comm_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = 403;
                    msg.arg1 = pos;
                    handler.sendMessage(msg);
                }
            });
            collect_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if ("1".equals(data.getIs_collect())) {
                        if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                            collect_btn.setLiked(true);
                            collect_btn.setUnlikeDrawableRes(R.drawable.home_collect_on);
                            intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        } else {
                            Message msg = new Message();
                            msg.what = 4041;
                            msg.arg1 = pos;
                            handler.sendMessage(msg);
                            int Collection_number = Integer.valueOf(data.getCollection_number());
                            data.setCollection_number(String.valueOf(Collection_number - 1));
                            homeItems.get(pos).setIs_collect("2");
                            coll_layout.setText(data.getCollection_number() + "收藏");
                            collect_btn.setLiked(false);
                            collect_btn.setUnlikeDrawableRes(R.drawable.home_collect_off);
                        }
                    } else {
                        if ("".equals(SharedPreferencesHelper.get(context, "login_token", ""))) {
                            collect_btn.setLiked(false);
                            collect_btn.setUnlikeDrawableRes(R.drawable.home_collect_off);
                            intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        } else {
                            Message msg = new Message();
                            msg.what = 404;
                            msg.arg1 = pos;
                            handler.sendMessage(msg);
                            int Collection_number = Integer.valueOf(data.getCollection_number());
                            data.setCollection_number(String.valueOf(Collection_number + 1));
                            homeItems.get(pos).setIs_collect("1");
                            coll_layout.setText(data.getCollection_number() + "收藏");
                            collect_btn.setLiked(true);
                            collect_btn.setLikeDrawableRes(R.drawable.home_collect_on);
                        }
                    }
                }
            });

            share_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = 405;
                    msg.arg1 = pos;
                    handler.sendMessage(msg);
                }
            });
            content_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    love_btn.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);
                    Message msg = new Message();
                    msg.what = 407;
                    msg.arg1 = pos;
                    handler.sendMessage(msg);
                }
            });
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    /**
     * Rxjava
     */
    protected Subscription subscription;
    private String iswifi = "0";

    //自动播放是否打开
    private void getWIFIStart(String isWifi) {
        Map<String, String> params = new HashMap<>();
        params.put("is_wifi", isWifi);
        Log.e("修改wifi状态：", params.toString());
        subscription = Network.getInstance("修改wifi状态", context)
                .getUpdateWifi(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                iswifi = "1";
//                                Log.e("123", "       修改wifi状态        " + result.getMsg());
                            }

                            @Override
                            public void onError(String error) {
                            }
                        }, context, false));
    }


}

package com.app.cookbook.xinhe.foodfamily.shiyujia.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.AnserActivity;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.SuggestedUsersActivity;
import com.app.cookbook.xinhe.foodfamily.main.adapter.SuggestedUsersAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.SuggestedUsers;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.ImageTextDetailActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.LabelDetailsActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.VideoDetailsActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.DiscoverBanner;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.DiscoverRecommend;
import com.app.cookbook.xinhe.foodfamily.shiyujia.view.GlideImageLoader;
import com.bumptech.glide.Glide;
import com.youth.banner.BannerConfig;
import com.youth.banner.DiscoveresBanner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class DiscoversAdapter extends RecyclerView.Adapter<DiscoversAdapter.Holder> {

    private Context context;
    private LayoutInflater inflater;
    private List<DiscoverBanner> discoverBanners = new ArrayList<>();
    private List<DiscoverRecommend.DataBean> discoverRecommends = new ArrayList<>();
    private List<SuggestedUsers.DataBean> suggestedUsers = new ArrayList<>();
    private List<String> imageList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private List<String> userHandList = new ArrayList<>();
    private List<String> typeList = new ArrayList<>();
    private String Sig;
    private String type;
    private String type2;
    private String type3;

    public DiscoversAdapter(Context context, String type, String type2, String type3, String Sig) {
        this.context = context;
        this.type = type;
        this.type2 = type2;
        this.type3 = type3;
        this.Sig = Sig;
        this.inflater = LayoutInflater.from(context);
    }

    public void setDiscoverBanners(List<DiscoverBanner> discoverBanners) {
        this.discoverBanners = discoverBanners;
    }

    public void setDiscoverRecommends(List<DiscoverRecommend.DataBean> discoverRecommends) {
        this.discoverRecommends = discoverRecommends;
    }

    public void setSuggestedUsers(List<SuggestedUsers.DataBean> suggestedUsers) {
        this.suggestedUsers = suggestedUsers;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public void setTitleList(List<String> titleList) {
        this.titleList = titleList;
    }

    public void setUserHandList(List<String> userHandList) {
        this.userHandList = userHandList;
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Holder holder = new Holder(inflater.inflate(R.layout.discovers_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        if ("1".equals(type)) {
            holder.banner_layout.setVisibility(View.VISIBLE);
        } else {
            holder.banner_layout.setVisibility(View.GONE);
        }
        if ("1".equals(type2)) {
            holder.att_user_layout.setVisibility(View.VISIBLE);
        } else {
            holder.att_user_layout.setVisibility(View.GONE);
        }
        if ("1".equals(type3)) {
            holder.label_layout.setVisibility(View.VISIBLE);
        } else {
            holder.label_title.setVisibility(View.GONE);
            holder.label_layout.setVisibility(View.GONE);
        }
        if ("2".equals(type) && "2".equals(type2)) {
            if (position == 0) {
                holder.label_title.setVisibility(View.VISIBLE);
            } else if (position == 1) {
                holder.label_title.setVisibility(View.INVISIBLE);
            } else {
                holder.label_title.setVisibility(View.GONE);
            }
            DiscoverRecommend.DataBean item = discoverRecommends.get(position);
            Glide.with(context)
                    .load(item.getPath())
                    .error(R.drawable.morenbg)
                    .into(holder.label_im);

            holder.label_name.setText("#" + item.getTitle());
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

            holder.attention_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SuggestedUsersActivity.class);
                    context.startActivity(intent);
                }
            });
        } else {
            if (position == 0) {
                if (TextUtils.isEmpty(discoverRecommends.get(position).getTitle())) {
                    holder.label_layout.setVisibility(View.GONE);
                }
                holder.label_title.setVisibility(View.GONE);
                holder.banner_layout.setImages(imageList)
                        .setBannerTitles(titleList)
                        .setUserHand(userHandList)
                        .setType(typeList)
                        .setSig(Sig)
                        .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                        .setImageLoader(new GlideImageLoader())
                        .start();

                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                holder.user_recycler.setLayoutManager(layoutManager);
                holder.user_recycler.setHasFixedSize(true);
                SuggestedUsersAdapter suggestedUsersAdapter = new SuggestedUsersAdapter(context);
                suggestedUsersAdapter.setSuggestedUsers(suggestedUsers);
                holder.user_recycler.setAdapter(suggestedUsersAdapter);

                holder.banner_layout.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        DiscoverBanner banner = discoverBanners.get(position);
                        Intent intent = null;
                        if ("1".equals(banner.getType())) {
                            intent = new Intent(context, ImageTextDetailActivity.class);
                            intent.putExtra("id", banner.getAscription_id());
                            context.startActivity(intent);
                        } else if ("2".equals(banner.getType())) {
                            intent = new Intent(context, VideoDetailsActivity.class);
                            intent.putExtra("id", banner.getAscription_id());
                            context.startActivity(intent);
                        } else if ("3".equals(banner.getType())) {
                            intent = new Intent(context, FriendPageActivity.class);
                            intent.putExtra("UserId", banner.getAscription_id());
                            context.startActivity(intent);
                        } else if ("4".equals(banner.getType())) {
                            intent = new Intent(context, LabelDetailsActivity.class);
                            intent.putExtra("id", banner.getAscription_id());
                            context.startActivity(intent);
                        } else if ("5".equals(banner.getType())) {
                            intent = new Intent(context, FenLeiQuestionDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("wenti_id", banner.getAscription_id());
                            bundle.putString("flag", "0");
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else if ("6".equals(banner.getType())) {
                            intent = new Intent(context, AnserActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("answer_id", banner.getAscription_id());
                            intent.putExtras(bundle);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
                });
            } else {
                if (position == 1) {
                    holder.label_title.setVisibility(View.VISIBLE);
                } else if (position == 2) {
                    holder.label_title.setVisibility(View.INVISIBLE);
                } else {
                    holder.label_title.setVisibility(View.GONE);
                }
                holder.banner_layout.setVisibility(View.GONE);
                holder.att_user_layout.setVisibility(View.GONE);
                holder.label_name.setVisibility(View.VISIBLE);
                holder.label_im.setVisibility(View.VISIBLE);
                DiscoverRecommend.DataBean item = discoverRecommends.get(position);
                if (position % 2 == 0) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.label_layout.getLayoutParams();
                    layoutParams.setMargins(8, 20, 28, 0);
                    holder.label_layout.setLayoutParams(layoutParams);
                } else {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.label_layout.getLayoutParams();
                    layoutParams.setMargins(28, 20, 8, 0);
                    holder.label_layout.setLayoutParams(layoutParams);
                }
                Glide.with(context)
                        .load(item.getPath())
                        .error(R.drawable.morenbg)
                        .into(holder.label_im);

                holder.label_name.setText("#" + item.getTitle());
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
            }

            holder.attention_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SuggestedUsersActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return discoverRecommends == null ? 0 : discoverRecommends.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private DiscoveresBanner banner_layout;
        //值得关注的用户
        private RelativeLayout att_user_layout;
        private ImageView attention_more;
        private RecyclerView user_recycler;
        //推荐标签
        private RelativeLayout label_layout;
        private RelativeLayout label_title;
        private ImageView label_im;
        private TextView label_name;

        public Holder(View itemView) {
            super(itemView);
            banner_layout = itemView.findViewById(R.id.banner_layout);
            att_user_layout = itemView.findViewById(R.id.att_user_layout);
            attention_more = itemView.findViewById(R.id.attention_more);
            user_recycler = itemView.findViewById(R.id.user_recycler);
            label_layout = itemView.findViewById(R.id.label_layout);
//            recommend_label = itemView.findViewById(R.id.recommend_label);
            label_title = itemView.findViewById(R.id.label_title);
            label_im = itemView.findViewById(R.id.label_im);
            label_name = itemView.findViewById(R.id.label_name);
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

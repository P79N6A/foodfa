package com.app.cookbook.xinhe.foodfamily.shiyujia.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.DiscoverRecommend;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.CornerTransform;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.ImageUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.app.cookbook.xinhe.foodfamily.util.ScreenUtilsHelper.dip2px;

public class RecommendLabelAdapter extends RecyclerView.Adapter<RecommendLabelAdapter.Holder> {

    private Context context;
    private LayoutInflater inflater;
    private List<DiscoverRecommend.DataBean> discoverRecommends = new ArrayList<>();


    public RecommendLabelAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setDiscoverRecommends(List<DiscoverRecommend.DataBean> discoverRecommends) {
        this.discoverRecommends = discoverRecommends;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Holder holder = new Holder(inflater.inflate(R.layout.recomment_label_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        DiscoverRecommend.DataBean item = discoverRecommends.get(position);

        if (position % 2 == 0) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.label_layout.getLayoutParams();
            layoutParams.setMargins(48, 20, 10, 0);
            holder.label_layout.setLayoutParams(layoutParams);
        } else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.label_layout.getLayoutParams();
            layoutParams.setMargins(10, 20, 48, 0);
            holder.label_layout.setLayoutParams(layoutParams);
        }
//
//        Glide.with(context).load(item.getPath())
//                .bitmapTransform(new
//                        RoundedCornersTransformation(context,
//                        30, 0,
//                        RoundedCornersTransformation.CornerType.BOTTOM))
//                .crossFade(1000).into(holder.label_im);
        CornerTransform transformation = new CornerTransform(context, ImageUtil.dip2px(context, 4));
        //只是绘制左上角和右上角圆角
        transformation.setExceptCorner(false, false, false, false);
        Glide.with(context).load(item.getPath())
                .asBitmap()
                .error(R.drawable.biao_qian_moren)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .transform(transformation)
                .into(holder.label_im);

        holder.label_name.setText("#" + item.getTitle());

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
        return discoverRecommends == null ? 0 : discoverRecommends.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private RelativeLayout label_layout;
        private ImageView label_im;
        private TextView label_name;

        public Holder(View itemView) {
            super(itemView);
            label_layout = itemView.findViewById(R.id.label_layout);
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

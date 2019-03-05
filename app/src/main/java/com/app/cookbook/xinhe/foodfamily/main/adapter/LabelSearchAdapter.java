package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
import com.app.cookbook.xinhe.foodfamily.main.SelectLabelActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.Label;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.CornerTransform;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.ImageUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 18030150 on 2018/4/1.
 */

public class LabelSearchAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private Context context;
    private static final int EMPTY_VIEW = 1;

    private List<Label.DataBean> labels;
    private LayoutInflater inflater;
    private Handler handler;

    public LabelSearchAdapter(Context mcontext, Handler handler) {
        this.handler = handler;
        this.context = mcontext;
        this.inflater = LayoutInflater.from(context);
    }

    public void setLabels(List<Label.DataBean> labels) {
        this.labels = labels;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        LabelSearchAdapter.PurchaseViewHolder purchaseViewHolder = new LabelSearchAdapter.PurchaseViewHolder(view, false);
        return purchaseViewHolder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        return new LabelSearchAdapter.PurchaseViewHolder(inflater
                .inflate(R.layout.jingxuanshidan_item, parent, false), true);
    }

    private LabelSearchAdapter.PurchaseViewHolder getViewHolderByViewType(int viewType, ViewGroup parent) {
        if (viewType == EMPTY_VIEW) {
            return new LabelSearchAdapter.PurchaseViewHolder(inflater
                    .inflate(R.layout.biaoqian_empty_layout, parent, false), true);
        } else {
            return new LabelSearchAdapter.PurchaseViewHolder(inflater
                    .inflate(R.layout.jingxuanshidan_item, parent, false), true);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, final int position, boolean isItem) {
        if (view_holder instanceof LabelSearchAdapter.PurchaseViewHolder) {
            if (labels.size() > 0) {
                final Label.DataBean item = labels.get(position);
                final LabelSearchAdapter.PurchaseViewHolder holder = (LabelSearchAdapter.PurchaseViewHolder) view_holder;
                //改变按钮状态
                if (position == 0) {
                    if ("0".equals(SelectLabelActivity.islabel) && item.getTitle().equals(SelectLabelActivity.searchKey)) {
                        if (item.isCheck()) {
                            holder.select_image.setVisibility(View.VISIBLE);
                            holder.add_select_tv.setVisibility(View.VISIBLE);
                        } else {
                            holder.select_image.setVisibility(View.INVISIBLE);
                            holder.add_select_tv.setVisibility(View.VISIBLE);
                        }

                        holder.label_item_layout.setBackgroundResource(R.drawable.label_xin_bg);
                        CornerTransform transformation = new CornerTransform(context, ImageUtil.dip2px(context, 4));
                        //只是绘制左上角和右上角圆角
                        transformation.setExceptCorner(false, true, false, true);
                        Glide.with(context)
                                .load(R.drawable.icon_xin_labe)
                                .asBitmap().
                                skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .transform(transformation)
                                .error(R.drawable.icon_label_bg)
                                .into(holder.biaoqian_image);
                    } else {
                        holder.label_item_layout.setBackgroundResource(R.drawable.xuan_label_bg);
                        CornerTransform transformation = new CornerTransform(context, ImageUtil.dip2px(context, 4));
                        //只是绘制左上角和右上角圆角
                        transformation.setExceptCorner(false, true, false, true);
                        Glide.with(context)
                                .load(item.getPath())
                                .asBitmap().
                                skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .transform(transformation)
                                .error(R.drawable.icon_label_bg)
                                .into(holder.biaoqian_image);
                    }
                } else {
                    holder.label_item_layout.setBackgroundResource(R.drawable.xuan_label_bg);
                    CornerTransform transformation = new CornerTransform(context, ImageUtil.dip2px(context, 4));
                    //只是绘制左上角和右上角圆角
                    transformation.setExceptCorner(false, true, false, true);
                    Glide.with(context)
                            .load(item.getPath())
                            .asBitmap().
                            skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .transform(transformation)
                            .error(R.drawable.icon_label_bg)
                            .into(holder.biaoqian_image);
                }

                if (item.isCheck()) {
                    holder.select_image.setVisibility(View.VISIBLE);
//                    holder.add_btn.setBackgroundResource(R.drawable.biaoqian_bg_hui);
//                    holder.add_tv.setText("已添加");
                } else {
                    holder.select_image.setVisibility(View.INVISIBLE);
//                    holder.add_btn.setBackgroundResource(R.drawable.biaoqian_bg_red);
//                    holder.add_tv.setText("添加");
                }

                if ("0".equals(SelectLabelActivity.islabel) && item.getTitle().equals(SelectLabelActivity.searchKey)) {
                    if (item.isCheck()) {
                        holder.select_image.setVisibility(View.VISIBLE);
                        holder.add_select_tv.setVisibility(View.VISIBLE);
//                        holder.add_btn.setBackgroundResource(R.drawable.biaoqian_bg_hui);
//                        holder.add_tv.setText("新建");
                    } else {
                        holder.select_image.setVisibility(View.INVISIBLE);
                        holder.add_select_tv.setVisibility(View.VISIBLE);
//                        holder.add_btn.setBackgroundResource(R.drawable.new_label);
//                        holder.add_tv.setText("新建");
                    }
                }

                holder.biaoqian_name.setText(item.getTitle());
                //关注数
                holder.guanzhu_tv.setText(item.getFollow() + "关注");
                //问题数
                holder.wenti_question.setText(item.getQuestions() + "问题");

                //条目点击时间
//                if (mOnItemClickListener != null) {
//                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            mOnItemClickListener.onItemClick(holder.itemView, position);
//                        }
//                    });
//                }
                holder.label_item_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.isCheck()) {//取消选中
                            Message msg = new Message();
                            msg.what = 401;
                            msg.arg1 = position;
                            handler.sendMessage(msg);
                            holder.select_image.setVisibility(View.INVISIBLE);
                        } else {//选中
                            Message msg = new Message();
                            msg.what = 400;
                            msg.arg1 = position;
                            handler.sendMessage(msg);
                            holder.select_image.setVisibility(View.VISIBLE);
                        }
                    }
                });

                //添加按钮
//                holder.add_btn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (item.isCheck()) {
//                            Log.e("123", "       选种     ");
//                            Message msg = new Message();
//                            msg.what = 401;
//                            msg.arg1 = position;
//                            handler.sendMessage(msg);
//                        } else {
//                            Log.e("123", "       取消选种     ");
//                            Message msg = new Message();
//                            msg.what = 400;
//                            msg.arg1 = position;
//                            handler.sendMessage(msg);
//                        }
//                    }
//                });
            }
        }
    }

    @Override
    public int getAdapterItemViewType(int position) {
        if (labels.size() == 0) {
            return EMPTY_VIEW;
        }
        return super.getAdapterItemViewType(position);
    }

    @Override
    public int getAdapterItemCount() {
//        return labels.size() > 0 ? labels.size() : 1;
        return labels == null ? 0 : labels.size();
    }


    public class PurchaseViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView biaoqian_name, guanzhu_tv, wenti_question, add_tv;
        public LinearLayout add_btn;
        public ImageView biaoqian_image;
        public ImageView select_image;
        public TextView add_select_tv;
        public RelativeLayout label_item_layout;

        public PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                label_item_layout = itemView.findViewById(R.id.label_item_layout);
                biaoqian_name = (TextView) itemView
                        .findViewById(R.id.biaoqian_name);
                guanzhu_tv = (TextView) itemView
                        .findViewById(R.id.guanzhu_tv);
                add_btn = (LinearLayout) itemView.findViewById(R.id.add_btn);
                biaoqian_image = (ImageView) itemView.findViewById(R.id.biaoqian_image);
                wenti_question = (TextView) itemView
                        .findViewById(R.id.wenti_question);
                add_tv = (TextView) itemView
                        .findViewById(R.id.add_tv);
                select_image = itemView.findViewById(R.id.select_image);
                add_select_tv = itemView.findViewById(R.id.add_select_tv);
            }
        }
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


    private LabelSearchAdapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    public void setOnItemClickListener(LabelSearchAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}


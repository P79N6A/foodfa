package com.app.cookbook.xinhe.foodfamily.shiyujia.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.BannerEntity;
import com.app.cookbook.xinhe.foodfamily.util.ScreenUtilsHelper;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

public class ZhiZhenAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<BannerEntity> list;
    private Context context;
    public static int select_positonn=-1;
    int sum_width;
    int jiange_index_width;
    int jiange_index_height;
    int zhizhen_width;
    public ZhiZhenAdapter(List<BannerEntity> mlist, Context mcontext) {
        list = mlist;
        context = mcontext;
        //图片的宽是118dp
        sum_width = ScreenUtilsHelper.dip2px(context,118);
        //获取间隔的宽度
        jiange_index_width =  ScreenUtilsHelper.dip2px(context,1)*(list.size()-1);
        //获取指针的高度
        jiange_index_height = ScreenUtilsHelper.dip2px(context,2);
        //获取每个指针的宽度
        zhizhen_width = (sum_width-jiange_index_width)/list.size();
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        PurchaseViewHolder purchaseViewHolder = new PurchaseViewHolder(view, false);
        return purchaseViewHolder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        RecyclerView.ViewHolder holder = getViewHolderByViewType(parent);
        return holder;
    }

    private RecyclerView.ViewHolder getViewHolderByViewType(ViewGroup parent) {
        View shidan_view = LayoutInflater.from(context).inflate(R.layout.zhizhen_item, parent, false);
        RecyclerView.ViewHolder holder = new PurchaseViewHolder(shidan_view, true);
        return holder;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, final int position, boolean isItem) {
        if (view_holder instanceof PurchaseViewHolder) {
            PurchaseViewHolder holder = (PurchaseViewHolder) view_holder;
            ((PurchaseViewHolder) view_holder).view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.index_item.getLayoutParams();
            params.height = jiange_index_height;//设置当前控件布局的高度
            params.width = zhizhen_width;
            holder.index_item.setLayoutParams(params);//将设置好的布局参数应用到控件中

        }
    }


    public void setData(List<BannerEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getAdapterItemCount() {
        return list.size() > 0 ? list.size() : 0;
    }

    public class PurchaseViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public LinearLayout index_item;

        public PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                index_item = view.findViewById(R.id.index_item);

            }
        }
    }
}

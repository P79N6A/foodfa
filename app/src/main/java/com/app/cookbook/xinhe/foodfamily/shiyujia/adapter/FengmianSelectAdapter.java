package com.app.cookbook.xinhe.foodfamily.shiyujia.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.bumptech.glide.Glide;
import java.io.File;
import java.util.List;

public class FengmianSelectAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<Bitmap> list;
    private Context context;

    public FengmianSelectAdapter(List<Bitmap> mlist, Context mcontext) {
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
        RecyclerView.ViewHolder holder = getViewHolderByViewType(parent);
        return holder;
    }

    private RecyclerView.ViewHolder getViewHolderByViewType(ViewGroup parent) {
        View shidan_view = LayoutInflater.from(context).inflate(R.layout.fengmian_select_item, parent, false);
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

            holder.image_view.setImageBitmap(list.get(position));

        }
    }



    @Override
    public int getAdapterItemCount() {
        return list.size() > 0 ? list.size() : 0;
    }

    public class PurchaseViewHolder extends RecyclerView.ViewHolder {
        public View view;
        private ImageView image_view;
        public PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                image_view = (ImageView) itemView.findViewById(R.id.image_view);
            }
        }
    }
}

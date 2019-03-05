package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.entity.FunctionItem;
import com.app.cookbook.xinhe.foodfamily.main.entity.ShareImageItem;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.bumptech.glide.Glide;

import java.util.List;

public class AnswerFunctionAdapter extends RecyclerView.Adapter<AnswerFunctionAdapter.Holder> {

    private List<FunctionItem> shareImageItems;
    private Context context;
    private LayoutInflater inflater;

    public AnswerFunctionAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setShareImageItems(List<FunctionItem> shareImageItems) {
        this.shareImageItems = shareImageItems;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.share_image_item, null);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        FunctionItem imageItem = shareImageItems.get(position);
        if (position == 0) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(75, 30, 0, 0);
            holder.lin_layout.setLayoutParams(layoutParams);
        } else if (position == shareImageItems.size() - 1) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(101, 30, 90, 0);
            holder.lin_layout.setLayoutParams(layoutParams);
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(101, 30, 0, 0);
            holder.lin_layout.setLayoutParams(layoutParams);
        }

        Glide.with(context).load(imageItem.getFunctionImage())
                .error(R.drawable.touxiang)
                .into(holder.share_image);

        holder.share_name.setText(imageItem.getFunctionName());

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

    @Override
    public int getItemCount() {
        return shareImageItems.size() > 0 ? shareImageItems.size() : 1;
    }

    class Holder extends RecyclerView.ViewHolder {

        private LinearLayout lin_layout;
        private ImageView share_image;
        private TextView share_name;

        public Holder(View itemView) {
            super(itemView);
            lin_layout = itemView.findViewById(R.id.lin_layout);
            share_image = itemView.findViewById(R.id.share_image);
            share_name = itemView.findViewById(R.id.share_name);
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

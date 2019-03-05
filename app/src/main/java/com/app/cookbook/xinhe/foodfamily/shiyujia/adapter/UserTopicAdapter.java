package com.app.cookbook.xinhe.foodfamily.shiyujia.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.entity.TiWenEntity;

import java.util.List;

public class UserTopicAdapter extends RecyclerView.Adapter<UserTopicAdapter.Holder> {

    private Context context;
    private LayoutInflater inflater;
    private List<TiWenEntity> tiWenEntities;

    public UserTopicAdapter(Context context) {
        this.context = context;
        if (context != null) {
            this.inflater = LayoutInflater.from(context);
        }
    }

    public void setTiWenEntities(List<TiWenEntity> tiWenEntities) {
        this.tiWenEntities = tiWenEntities;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Holder holder = new Holder(inflater.inflate(R.layout.my_tiwen_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        TiWenEntity item = tiWenEntities.get(position);
        holder.tiwen_title.setText(item.getTitle());

        if (item.getCountUsers().equals("0")) {
            holder.tiwen_number.setVisibility(View.GONE);
            holder.tv_number.setText("暂无回答");
            holder.tv_number.setTextColor(context.getResources().getColor(R.color.color_dbdbdb));
        } else {
            holder.tiwen_number.setVisibility(View.VISIBLE);
            holder.tiwen_number.setText(item.getCountUsers());
            holder.tv_number.setText("个回答");
            holder.tv_number.setTextColor(context.getResources().getColor(R.color.bottom_hui));
            holder.tiwen_number.setTextColor(context.getResources().getColor(R.color.bottom_hui));
        }

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
        return tiWenEntities == null ? 0 : tiWenEntities.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        public TextView tiwen_title;
        public TextView title;
        public TextView tiwen_number;
        public TextView tv_number;

        public Holder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            tiwen_title = itemView.findViewById(R.id.tiwen_title);
            tiwen_number = itemView.findViewById(R.id.tiwen_number);
            tv_number = itemView.findViewById(R.id.tv_number);
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

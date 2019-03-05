package com.app.cookbook.xinhe.foodfamily.shiyujia.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.MyDraftsPrint;
import com.app.cookbook.xinhe.foodfamily.util.DateHelper;
import com.app.cookbook.xinhe.foodfamily.util.FormatCurrentData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyDraftsPrintAdapter extends RecyclerView.Adapter<MyDraftsPrintAdapter.Holder> {

    private Context context;
    private LayoutInflater inflater;
    private List<MyDraftsPrint.DataBean> myDraftsPrint = new ArrayList<>();


    public MyDraftsPrintAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setMyDraftsPrint(List<MyDraftsPrint.DataBean> myDraftsPrint) {
        this.myDraftsPrint = myDraftsPrint;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Holder holder = new Holder(inflater.inflate(R.layout.my_drafts_print_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        MyDraftsPrint.DataBean item = myDraftsPrint.get(position);

        holder.content_tv.setText(item.getContent());
        if (!TextUtils.isEmpty(item.getCreated_at())) {
            holder.time_tv.setText(FormatCurrentData.getFetureDate(Long.valueOf(item.getCreated_at())));
        }
        if (item.getImage_data() != null) {
            Glide.with(context).load(item.getImage_data().get(0).getPath()).into(holder.image_view);
        }

        //条目点击时间
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = holder.getPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onLongClickListener != null) {
                        onLongClickListener.onLongClick(v, position);
                    }
                    return false;
                }

            });
        }

    }

    @Override
    public int getItemCount() {
        return myDraftsPrint == null ? 0 : myDraftsPrint.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView image_view;
        private TextView content_tv;
        private TextView time_tv;

        public Holder(View itemView) {
            super(itemView);
            image_view = itemView.findViewById(R.id.image_view);
            content_tv = itemView.findViewById(R.id.content_tv);
            time_tv = itemView.findViewById(R.id.time_tv);

        }
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    private OnLongClickListener onLongClickListener;

    public interface OnLongClickListener {
        void onLongClick(View view, int position);
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }


}

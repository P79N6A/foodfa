package com.app.cookbook.xinhe.foodfamily.shiyujia.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.CaiPuEntity;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyCollectCaipuAdapter extends RecyclerView.Adapter<MyCollectCaipuAdapter.Holder> {
    private Context context;
    private LayoutInflater inflater;
    private List<CaiPuEntity.DataBean> daEntities;


    public MyCollectCaipuAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setDaEntities(List<CaiPuEntity.DataBean> daEntities) {
        this.daEntities = daEntities;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Holder holder = new Holder(inflater.inflate(R.layout.banner_item_layout2, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyCollectCaipuAdapter.Holder holder, final int position) {
        final CaiPuEntity.DataBean item = daEntities.get(position);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

        Glide.with(context).load(item.getFace_path())
                .asBitmap()
                .error(R.drawable.touxiang)
                .into(holder.banner_img);
        holder.shipu_name.setText(item.getName());
        holder.shipu_tips.setText(item.getNutrition_tips());


    }

    @Override
    public int getItemCount() {
        return daEntities == null ? 0 : daEntities.size();
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
        long msl = (long) ms * 1000;
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

    public class Holder extends RecyclerView.ViewHolder {

        public View view;
        RoundedImageView banner_img;
        TextView shipu_name,shipu_tips;
        public Holder(View itemView) {
            super(itemView);
            view = itemView;
            banner_img = itemView.findViewById(R.id.banner_img);
            shipu_name = itemView.findViewById(R.id.shipu_name);
            shipu_tips = itemView.findViewById(R.id.shipu_tips);
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

package com.app.cookbook.xinhe.foodfamily.shiyujia.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.AnserActivity;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.adapter.ImagesAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.HuiDaEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.ImageDate;
import com.app.cookbook.xinhe.foodfamily.util.DateUtils;
import com.app.cookbook.xinhe.foodfamily.util.otherUi.ListViewForScrollView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserAnswerAdapter extends RecyclerView.Adapter<UserAnswerAdapter.Holder> {

    private Context context;
    private LayoutInflater inflater;
    private List<HuiDaEntity> daEntities;

    public UserAnswerAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }
    public void setDaEntities(List<HuiDaEntity> daEntities) {
        this.daEntities = daEntities;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Holder holder = new Holder(inflater.inflate(R.layout.user_answer_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        final HuiDaEntity item = daEntities.get(position);

        holder.question_content.setText(item.getTitle());
        if (null != item.getImg_data()) {
            //默认只显示头一张图片
            List<ImageDate> imageDates = new ArrayList<>();
            List<ImageDate> show_imageDates = new ArrayList<>();
            if (item.getImg_data().size() > 1) {
                holder.images_view.setVisibility(View.VISIBLE);
                for (int i = 0; i < item.getImg_data().size(); i++) {
                    if (null != item.getImg_data().get(i).getPath()) {
                        imageDates.add(item.getImg_data().get(i));
                    }
                }
                show_imageDates.add(imageDates.get(0));
                ImagesAdapter imagesAdapter = new ImagesAdapter(show_imageDates, context);
                holder.images_view.setAdapter(imagesAdapter);
            } else {
                holder.images_view.setVisibility(View.VISIBLE);
                ImagesAdapter imagesAdapter = new ImagesAdapter(item.getImg_data(), context);
                holder.images_view.setAdapter(imagesAdapter);
            }
        } else {
            holder.images_view.setVisibility(View.GONE);
        }

        holder.images_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, AnserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("answer_id", item.getId());
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });
        if (!TextUtils.isEmpty(item.getContent_remove())) {
            holder.question_answer.setVisibility(View.VISIBLE);
            holder.question_answer.setText(item.getContent_remove());
        } else {
            holder.question_answer.setVisibility(View.GONE);
        }
        holder.zan_number.setText(item.getThumbs());
        holder.shoucang_number.setText(item.getCollection());
        if (!TextUtils.isEmpty(item.getCreated_at())) {
            holder.time_tv.setText(DateUtils.getTimeFormatText(transForDate(Long.valueOf(item.getCreated_at()))));
        }

        holder.question_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FenLeiQuestionDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("wenti_id", item.getQid());
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.question_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AnserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("answer_id", item.getId());
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


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
        public TextView question_content;
        public TextView question_answer;
        public TextView zan_number;
        public TextView shoucang_number;
        public TextView time_tv;
        public ListViewForScrollView images_view;
        public TextView title;

        public Holder(View itemView) {
            super(itemView);
            question_content = (TextView) itemView.findViewById(R.id.question_content);
            question_answer = (TextView) itemView.findViewById(R.id.question_answer);
            images_view = (ListViewForScrollView) itemView.findViewById(R.id.images_view);
            zan_number = (TextView) itemView.findViewById(R.id.zan_number);
            shoucang_number = (TextView) itemView.findViewById(R.id.shoucang_number);
            time_tv = (TextView) itemView.findViewById(R.id.time_tv);
            title = itemView.findViewById(R.id.title);
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

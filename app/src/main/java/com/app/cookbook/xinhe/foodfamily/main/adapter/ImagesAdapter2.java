package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.AnserActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.ImageDate;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by shiyujia02 on 2018/2/28.
 */

public class ImagesAdapter2 extends BaseAdapter {
    List<ImageDate> cateDates;
    Context context;
    private LayoutInflater mInflater;
    String answer_id;

    public ImagesAdapter2(List<ImageDate> mcateDates, Context mcontext, String manswer_id) {
        cateDates = mcateDates;
        context = mcontext;
        answer_id = manswer_id;
        mInflater = LayoutInflater.from(mcontext);

    }

    @Override
    public int getCount() {
        return cateDates.size();
    }

    @Override
    public Object getItem(int position) {
        return cateDates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImagesAdapter2.ViewHolder holder;
        if (convertView == null) {
            holder = new ImagesAdapter2.ViewHolder();
            convertView = mInflater.inflate(R.layout.image_item, parent, false);
            holder.answer_img = convertView.findViewById(R.id.answer_img);
            convertView.setTag(holder);
        } else {
            holder = (ImagesAdapter2.ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(cateDates.get(position)
                .getPath())
                .error(R.drawable.morenbg)
                .into(holder.answer_img);
        holder.answer_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AnserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("answer_id", answer_id);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder {
        RoundedImageView answer_img;
    }
}

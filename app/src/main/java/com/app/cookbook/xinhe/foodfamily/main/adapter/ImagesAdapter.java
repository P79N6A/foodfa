package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.entity.ImageDate;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by shiyujia02 on 2018/1/24.
 */

public class ImagesAdapter extends BaseAdapter {
    List<ImageDate> cateDates;
    Context context;
    private LayoutInflater mInflater;

    public ImagesAdapter(List<ImageDate> mcateDates, Context mcontext) {
        cateDates = mcateDates;
        context = mcontext;
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
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.image_item, parent, false);
            holder.answer_img = convertView.findViewById(R.id.answer_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(cateDates.get(position).getPath())
                //.centerCrop()
                .asBitmap()
                .placeholder(R.drawable.morenbg)
                .error(R.drawable.morenbg)
                .into(holder.answer_img);


        return convertView;
    }

    class ViewHolder {
        RoundedImageView answer_img;
    }
}

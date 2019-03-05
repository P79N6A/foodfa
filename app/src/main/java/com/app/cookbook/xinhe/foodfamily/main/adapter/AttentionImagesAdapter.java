package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.entity.Attention;
import com.app.cookbook.xinhe.foodfamily.main.entity.AttentionList;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by 18030150 on 2018/5/29.
 */

public class AttentionImagesAdapter extends BaseAdapter {
    private List<AttentionList.DataBean.AnswerImgDataBean> cateDates;
    private Context context;
    private LayoutInflater mInflater;

    public AttentionImagesAdapter(List<AttentionList.DataBean.AnswerImgDataBean> mcateDates, Context mcontext) {
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
        final AttentionImagesAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new AttentionImagesAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.image_item, parent, false);
            holder.answer_img = convertView.findViewById(R.id.answer_img);
            convertView.setTag(holder);
        } else {
            holder = (AttentionImagesAdapter.ViewHolder) convertView.getTag();
        }

        Glide.with(context).load(cateDates.get(position).getPath())
                //.centerCrop()
                .placeholder(R.drawable.morenbg)
                .error(R.drawable.morenbg)
                .into(holder.answer_img);


        return convertView;
    }

    class ViewHolder {
        RoundedImageView answer_img;
    }
}
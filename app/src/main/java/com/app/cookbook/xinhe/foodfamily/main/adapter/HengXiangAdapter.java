package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.entity.CategoryDateEn;

import java.util.List;

/**
 * Created by shiyujia02 on 2018/1/18.
 */

public class HengXiangAdapter extends BaseAdapter {
    List<CategoryDateEn> cateDates;
    Context context;
    private LayoutInflater mInflater;

    public HengXiangAdapter(List<CategoryDateEn> mcateDates, Context mcontext) {
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
            convertView = mInflater.inflate(R.layout.hengxiang_item, parent, false);
            holder.question_content = convertView.findViewById(R.id.question_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.question_content.setText(cateDates.get(position).getTitle());

        return convertView;
    }

    class ViewHolder {
        TextView question_content;
    }
}

package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.entity.UseLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 18030150 on 2018/5/1.
 */

public class UseLabelAdapter extends BaseAdapter {
    private Context context;
    private Handler handler;
    private LayoutInflater inflater;

    private List<UseLabel.DataBean> list = new ArrayList<>();

    public UseLabelAdapter(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<UseLabel.DataBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final viewHolder holder;
        if (convertView == null) {
            holder = new viewHolder();
            convertView = inflater.inflate(R.layout.use_label_item, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        final UseLabel.DataBean item = list.get(position);
        if(!TextUtils.isEmpty(item.getTitle())){
            String title = item.getTitle().replace(" ", "");
            holder.tv_name.setText(title);
        }

        if (item.isSelect() == true) {
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.color_777777));
            holder.tv_name.setBackgroundResource(R.drawable.select_label_bg);
        } else {
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.color_bbbbbb));
            holder.tv_name.setBackgroundResource(R.drawable.use_label_bg);
        }

        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isSelect()) {
                    Log.e("123", "       取消选中     ");
                    Message msg = new Message();
                    msg.what = 403;
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                } else {
                    Log.e("123", "       选中      ");
                    Message msg = new Message();
                    msg.what = 402;
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                }
            }
        });
        return convertView;
    }

    private class viewHolder {
        private TextView tv_name;
    }
}
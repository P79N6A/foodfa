package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;

import java.util.List;

/**
 * Created by 18030150 on 2018/4/1.
 */

public class SelectLabelAdapter extends RecyclerView.Adapter<SelectLabelAdapter.Holder> {

    private List<String> selectList;
    private Context context;
    private LayoutInflater inflater;

    private Handler handler;

    public SelectLabelAdapter(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.inflater = LayoutInflater.from(context);
    }

    public void setSelectList(List<String> selectList) {
        this.selectList = selectList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.select_label_item, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        String str = selectList.get(position);
        holder.tv_name.setText(str);
        holder.tv_name2.setText(str);
        if (Contacts.deleteSelsctType == position) {
            holder.label_layout1.setVisibility(View.VISIBLE);
            holder.label_layout.setVisibility(View.GONE);
            Contacts.deleteSelsctType = -1;
        }

        holder.label_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message msg = new Message();
                msg.what = 300;
                msg.arg1 = position;
                handler.sendMessage(msg);
                holder.label_layout1.setVisibility(View.VISIBLE);
                holder.label_layout.setVisibility(View.GONE);
            }
        });
        holder.label_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.label_layout1.setVisibility(View.GONE);
                holder.label_layout.setVisibility(View.VISIBLE);
                Message msg = new Message();
                msg.what = 301;
                msg.arg1 = position;
                handler.sendMessage(msg);
            }
        });

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 302;
                msg.arg1 = position;
                handler.sendMessage(msg);
            }
        });

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
        return selectList == null ? 0 : selectList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private TextView tv_name2;
        private ImageView delete_btn;
        private LinearLayout label_layout;
        private LinearLayout label_layout1;

        public Holder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_name2 = itemView.findViewById(R.id.tv_name2);
            delete_btn = itemView.findViewById(R.id.delete_btn);
            label_layout = itemView.findViewById(R.id.label_layout);
            label_layout1 = itemView.findViewById(R.id.label_layout1);
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

package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.entity.ReportMsg;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;


import java.util.List;

/**
 * Created by 18030150 on 2018/3/22.
 */

public class ReportMsgAdapter extends RecyclerView.Adapter<ReportMsgAdapter.Holder> {

    private Context context;
    private LayoutInflater inflater;
    private List<ReportMsg> data;
    private Handler handler;

    public ReportMsgAdapter(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(List<ReportMsg> data) {
        this.data = data;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.report_item, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        final ReportMsg msg = data.get(position);
        holder.tv_msg.setText(msg.getName());
        if (data.size() == position + 1) {
            holder.line_layout.setVisibility(View.GONE);
        } else {
            holder.line_layout.setVisibility(View.VISIBLE);
        }
        holder.bianji_msg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Contacts.typeMsg.indexOf(msg.getId()) == -1 && Contacts.typeMsg.size() < 2) {
                    //添加
                    holder.tv_msg.setTextColor(context.getResources().getColor(R.color.meishititle));
                    Contacts.typeMsg.add(msg.getId());
                    Message msg = new Message();
                    msg.what = 500;
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                } else {
                    if (Contacts.typeMsg.size() == 1) {
                        holder.tv_msg.setTextColor(context.getResources().getColor(R.color.top_heiziti));
                        Contacts.typeMsg.clear();
                        Message msg = new Message();
                        msg.what = 502;
                        msg.arg1 = position;
                        handler.sendMessage(msg);
                    } else if (Contacts.typeMsg.size() > 1 && Contacts.typeMsg.size() == 2) {
                        holder.tv_msg.setTextColor(context.getResources().getColor(R.color.top_heiziti));
                        Contacts.typeMsg.remove(msg.getId());
                        Message msg = new Message();
                        msg.what = 501;
                        msg.arg1 = position;
                        handler.sendMessage(msg);
                    }
                }
//                Message msg = new Message();
//                msg.what = 400;
//                msg.arg1 = position;
//                handler.sendMessage(msg);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private TextView tv_msg;
        private LinearLayout bianji_msg1;
        private TextView line_layout;

        public Holder(View itemView) {
            super(itemView);
            tv_msg = itemView.findViewById(R.id.tv_msg);
            bianji_msg1 = itemView.findViewById(R.id.bianji_msg1);
            line_layout = itemView.findViewById(R.id.line_layout);
        }
    }
}

package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.entity.HotLabel;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by 18030150 on 2018/5/1.
 */

public class HotLabelAdapter extends RecyclerView.Adapter<HotLabelAdapter.Holder> {

    private List<HotLabel.DataBean> selectList;
    private Context context;
    private LayoutInflater inflater;
    private Handler handler;

    public HotLabelAdapter(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.inflater = LayoutInflater.from(context);
    }

    public void setSelectList(List<HotLabel.DataBean> selectList) {
        this.selectList = selectList;
    }

    @Override
    public HotLabelAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.hot_label_item, parent, false);
        HotLabelAdapter.Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        final HotLabel.DataBean str = selectList.get(position);
        if (str.isSelect() == true) {
            holder.im_hand_elect.setVisibility(View.VISIBLE);
        } else {
            holder.im_hand_elect.setVisibility(View.GONE);
        }

        holder.tv_title.setText(str.getTitle());
        Glide.with(context)
                .load(str.getPath())
                .error(R.drawable.icon_logo) //
                .into(holder.im_hand);

        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (str.isSelect()) {
                    Message msg = new Message();
                    msg.what = 405;
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = 404;
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                }

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
        private TextView tv_title;
        private ImageView im_hand;
        private ImageView im_hand_elect;
        private RelativeLayout item_layout;

        public Holder(View itemView) {
            super(itemView);
            im_hand = itemView.findViewById(R.id.im_hand);
            im_hand_elect = itemView.findViewById(R.id.im_hand_elect);
            tv_title = itemView.findViewById(R.id.tv_title);
            item_layout = itemView.findViewById(R.id.item_layout);
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

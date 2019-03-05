package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.entity.TiWenEntity;

import java.util.List;


/**
 * Created by 18030150 on 2018/5/1.
 */

public class MyTiWenAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<TiWenEntity> list;
    private Context context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private boolean ista;

    private String titleName;

    public MyTiWenAdapter(List<TiWenEntity> mlist, Context mcontext, boolean mista) {
        list = mlist;
        context = mcontext;
        ista = mista;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        PurchaseViewHolder purchaseViewHolder = new PurchaseViewHolder(view, false);
        return purchaseViewHolder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        RecyclerView.ViewHolder holder = getViewHolderByViewType(viewType, parent);
        return holder;
    }

    private RecyclerView.ViewHolder getViewHolderByViewType(int viewType, ViewGroup parent) {
        View empty_view;
        if (ista) {
            empty_view = LayoutInflater.from(context).inflate(R.layout.ta_tiwen_empty_layout, parent, false);

        } else {
            empty_view = LayoutInflater.from(context).inflate(R.layout.tiwen_empty_layout, parent, false);

        }
        View shidan_view = LayoutInflater.from(context).inflate(R.layout.my_tiwen_item, parent, false);
        RecyclerView.ViewHolder holder = null;
        if (viewType == EMPTY_VIEW) {
            holder = new EmptyViewHolder(empty_view, true);
        } else {
            holder = new PurchaseViewHolder(shidan_view, true);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, final int position, boolean isItem) {
        if (view_holder instanceof PurchaseViewHolder) {
            final PurchaseViewHolder holder = (PurchaseViewHolder) view_holder;
            if (position == 0) {
                holder.title.setText(getTitleName());
                holder.title.setVisibility(View.VISIBLE);
            } else {
                holder.title.setText(getTitleName());
                holder.title.setVisibility(View.GONE);
            }
            holder.tiwen_title.setText(list.get(position).getTitle());

            if (list.get(position).getCountUsers().equals("0")) {
                holder.tiwen_number.setVisibility(View.GONE);
                holder.tv_number.setText("暂无回答");
                holder.tv_number.setTextColor(context.getResources().getColor(R.color.color_dbdbdb));
            } else {
                holder.tiwen_number.setVisibility(View.VISIBLE);
                holder.tiwen_number.setText(list.get(position).getCountUsers());
                holder.tv_number.setText("个回答");
                holder.tv_number.setTextColor(context.getResources().getColor(R.color.bottom_hui));
                holder.tiwen_number.setTextColor(context.getResources().getColor(R.color.bottom_hui));

            }

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

        } else if (view_holder instanceof EmptyViewHolder) {
            EmptyViewHolder holder = (EmptyViewHolder) view_holder;
//            if (position == 0) {
                holder.title.setText(getTitleName());
                holder.title.setVisibility(View.VISIBLE);
//            } else {
//                holder.title.setText(getTitleName());
//                holder.title.setVisibility(View.GONE);
//            }
        }
    }


    @Override
    public int getAdapterItemViewType(int position) {
        if (list.size() == 0) {
            return EMPTY_VIEW;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getAdapterItemCount() {
        return list.size() > 0 ? list.size() : 1;
    }

    public void setData(List<TiWenEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_top;
        public View view;
        private TextView title;

        public EmptyViewHolder(View item_view, boolean isItem) {
            super(item_view);
            if (isItem) {
                this.view = item_view;
                img_top = (ImageView) item_view
                        .findViewById(R.id.img_top);
                title = item_view.findViewById(R.id.title);
            }
        }
    }

    public class PurchaseViewHolder extends RecyclerView.ViewHolder {
        public TextView tiwen_title;
        public TextView title;
        public TextView tiwen_number;
        public TextView tv_number;

        public PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                title = itemView.findViewById(R.id.title);
                tiwen_title = itemView.findViewById(R.id.tiwen_title);
                tiwen_number = itemView.findViewById(R.id.tiwen_number);
                tv_number = itemView.findViewById(R.id.tv_number);
            }
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

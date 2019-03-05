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
import com.app.cookbook.xinhe.foodfamily.main.entity.LocationDate;
import com.app.cookbook.xinhe.foodfamily.util.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rx.Subscription;

/**
 * Created by shiyujia02 on 2018/1/25.
 */

public class XiTongAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<LocationDate.DataBean> list;
    private Context context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_ITEM = 1;
    /**
     * Rxjava
     */
    protected Subscription subscription;

    public XiTongAdapter(List<LocationDate.DataBean> mlist, Context mcontext) {
        list = mlist;
        context = mcontext;
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

    RecyclerView.ViewHolder holder = null;

    private RecyclerView.ViewHolder getViewHolderByViewType(int viewType, ViewGroup parent) {
        View empty_view = LayoutInflater.from(context).inflate(R.layout.location_empty_layout, parent, false);
        View shidan_view = LayoutInflater.from(context).inflate(R.layout.xitong_location_item_new, parent, false);

        if (viewType == EMPTY_VIEW) {
            holder = new EmptyViewHolder(empty_view, true);

        } else if (viewType == TYPE_ITEM) {
            holder = new PurchaseViewHolder(shidan_view, true);

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, final int position, boolean isItem) {
        if (view_holder instanceof PurchaseViewHolder) {
            final PurchaseViewHolder holder = (PurchaseViewHolder) view_holder;
            if (position == 0) {
                holder.title.setVisibility(View.VISIBLE);
            } else {
                holder.title.setVisibility(View.GONE);
            }
            if ("1".equals(list.get(position).getClick())) {//不需要点击
                holder.system_dianzan_img_hui.setVisibility(View.GONE);
            } else {//可以点击
                holder.system_dianzan_img_hui.setVisibility(View.VISIBLE);
                //条目点击时间
                if (mOnItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemClickListener.onItemClick(holder.itemView, position);
                        }
                    });
                }
            }


            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });

            holder.user_name.setText("食与家");

            holder.content_tv.setText(list.get(position).getContent());

            holder.time_tv.setText(DateUtils.getTimeFormatText(transForDate(Long.valueOf(list.get(position).getCreated_at()))));
//
//            holder.content_tv.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    if (onItemLongClickListener != null) {
//                        onItemLongClickListener.onItemLongClick(v, position);
//                    }
//                    return false;
//                }
//
//            });
//            holder.time_tv.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    if (onItemLongClickListener != null) {
//                        onItemLongClickListener.onItemLongClick(v, position);
//                    }
//                    return false;
//                }
//
//            });
//
//            holder.system_dianzan_img_hui.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    if (onItemLongClickListener != null) {
//                        onItemLongClickListener.onItemLongClick(v, position);
//                    }
//                    return false;
//                }
//
//            });


        }
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

    @Override
    public int getAdapterItemViewType(int position) {
        if (list.size() == 0) {
            return EMPTY_VIEW;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getAdapterItemCount() {
        return list.size() > 0 ? list.size() : 1;
    }

//    public interface OnItemClickListener {
//        void onItemClick(View view, int position);
//    }

//    private OnItemClickListener mOnItemClickListener;
//
//    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
//        this.mOnItemClickListener = mOnItemClickListener;
//    }

    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


    public void setData(List<LocationDate.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_top;
        public View view;

        public EmptyViewHolder(View item_view, boolean isItem) {
            super(item_view);
            if (isItem) {
                this.view = item_view;
                img_top = (ImageView) item_view
                        .findViewById(R.id.img_top);
            }
        }
    }

    public class PurchaseViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView time_tv, content_tv;
        public ImageView system_dianzan_img_hui;
        private TextView title;
        private TextView user_name;

        public PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                time_tv = (TextView) itemView.findViewById(R.id.time_tv);
                content_tv = (TextView) itemView.findViewById(R.id.content_tv);
                system_dianzan_img_hui = (ImageView) itemView.findViewById(R.id.system_dianzan_img_hui);
                title = itemView.findViewById(R.id.title);
                user_name = itemView.findViewById(R.id.user_name);
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

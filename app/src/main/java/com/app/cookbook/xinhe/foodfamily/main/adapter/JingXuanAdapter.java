package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.AnserActivity;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.HuiDaEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.ImageDate;
import com.app.cookbook.xinhe.foodfamily.util.DateUtils;
import com.app.cookbook.xinhe.foodfamily.util.otherUi.ListViewForScrollView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shiyujia02 on 2018/1/12.
 */

public class JingXuanAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<HuiDaEntity> list;
    private Context context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private boolean ista;
    private String type = "0";
    private String titleName;

    public JingXuanAdapter(List<HuiDaEntity> mlist, Context mcontext, boolean mista) {
        list = mlist;
        ista = mista;
        context = mcontext;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
            empty_view = LayoutInflater.from(context).inflate(R.layout.ta_answer_empty_layout, parent, false);

        } else {
            empty_view = LayoutInflater.from(context).inflate(R.layout.answer_empty_layout, parent, false);

        }
        View shidan_view = LayoutInflater.from(context).inflate(R.layout.item_recylerviewthree, parent, false);
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
            PurchaseViewHolder holder = (PurchaseViewHolder) view_holder;
            if ("1".equals(getType())) {
                if (position == 0) {
                    holder.title.setText(getTitleName());
                    holder.title.setVisibility(View.VISIBLE);
                } else {
                    holder.title.setText(getTitleName());
                    holder.title.setVisibility(View.GONE);
                }
            }
            ((PurchaseViewHolder) view_holder).view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });
            holder.question_content.setText(list.get(position).getTitle());

//            if(null!=list.get(position).getImg_data()){
//                ImagesAdapter imagesAdapter = new ImagesAdapter(list.get(position).getImg_data(), context);
//                holder.images_view.setAdapter(imagesAdapter);
//            }

            if (null != list.get(position).getImg_data()) {
                //默认只显示头一张图片
                List<ImageDate> imageDates = new ArrayList<>();
                List<ImageDate> show_imageDates = new ArrayList<>();
                if (list.get(position).getImg_data().size() > 1) {
                    holder.images_view.setVisibility(View.VISIBLE);
                    for (int i = 0; i < list.get(position).getImg_data().size(); i++) {
                        if (null != list.get(position).getImg_data().get(i).getPath()) {
                            imageDates.add(list.get(position).getImg_data().get(i));
                        }
                    }
                    show_imageDates.add(imageDates.get(0));
                    ImagesAdapter imagesAdapter = new ImagesAdapter(show_imageDates, context);
                    holder.images_view.setAdapter(imagesAdapter);
                } else {
                    holder.images_view.setVisibility(View.VISIBLE);

                    ImagesAdapter imagesAdapter = new ImagesAdapter(list.get(position).getImg_data(), context);
                    holder.images_view.setAdapter(imagesAdapter);
                }
            } else {
                holder.images_view.setVisibility(View.GONE);
            }
            holder.images_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, AnserActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("answer_id", list.get(position).getId());
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(intent);
                }
            });
            if (!TextUtils.isEmpty(list.get(position).getContent_remove())) {
                holder.question_answer.setVisibility(View.VISIBLE);
                holder.question_answer.setText(list.get(position).getContent_remove());
            } else {
                holder.question_answer.setVisibility(View.GONE);
            }
            holder.zan_number.setText(list.get(position).getThumbs());
            holder.shoucang_number.setText(list.get(position).getCollection());

//            Long time = Long.valueOf(list.get(position).getCreated_at());
//            String month = "";
//            String date = "";
//            if (transForDate(time).getMonth() < 10) {
//                month = "0" + (Integer.valueOf(transForDate(time).getMonth()) + 1);
//            } else {
//                month = (Integer.valueOf(transForDate(time).getMonth()) + 1) + "";
//            }
//            if (transForDate(time).getDate() < 10) {
//                date = "0" + transForDate(time).getDate();
//            } else {
//                date = transForDate(time).getDate() + "";
//            }
//
//            DateFormat df1 = DateFormat.getDateInstance();//日期格式，精确到日
            //holder.time_tv.setText(df1.format(transForDate(time)));

            holder.time_tv.setText(DateUtils.getTimeFormatText(transForDate(Long.valueOf(list.get(position).getCreated_at()))));

            holder.question_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FenLeiQuestionDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("wenti_id", list.get(position).getQid());
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            holder.question_answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AnserActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("answer_id", list.get(position).getId());
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        } else if (view_holder instanceof EmptyViewHolder) {
//            if ("1".equals(getType())) {
            EmptyViewHolder holder = (EmptyViewHolder) view_holder;
//                if (position == 0) {
            holder.title.setText(getTitleName());
            holder.title.setVisibility(View.VISIBLE);
//                } else {
//                    holder.title.setText(getTitleName());
//                    holder.title.setVisibility(View.GONE);
//                }
//            }
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
        }
        return TYPE_ITEM;
    }

    @Override
    public int getAdapterItemCount() {
        return list.size() > 0 ? list.size() : 1;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setData(List<HuiDaEntity> list) {
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
        public View view;
        public TextView question_title, question_content, question_answer, zan_number, shoucang_number, time_tv;
        public ListViewForScrollView images_view;
        private TextView title;

        public PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                question_title = (TextView) itemView.findViewById(R.id.question_title);
                question_content = (TextView) itemView.findViewById(R.id.question_content);
                question_answer = (TextView) itemView.findViewById(R.id.question_answer);
                images_view = (ListViewForScrollView) itemView.findViewById(R.id.images_view);
                zan_number = (TextView) itemView.findViewById(R.id.zan_number);
                shoucang_number = (TextView) itemView.findViewById(R.id.shoucang_number);
                time_tv = (TextView) itemView.findViewById(R.id.time_tv);
                title = itemView.findViewById(R.id.title);
            }
        }
    }
}

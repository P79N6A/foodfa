package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.AnserActivity;
import com.app.cookbook.xinhe.foodfamily.main.AnswerReplyActivity;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.OtherUserActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.ImageDate;
import com.app.cookbook.xinhe.foodfamily.main.entity.MyShouCangEntity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.util.DataTypeAdaptor;
import com.app.cookbook.xinhe.foodfamily.util.otherUi.ListViewForScrollView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by shiyujia02 on 2018/1/13.
 */

public class ShouCangAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<MyShouCangEntity> list;
    private Context context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private boolean ista;

    private String titleName;

    public ShouCangAdapter(List<MyShouCangEntity> mlist, Context mcontext, boolean mista) {
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
            empty_view = LayoutInflater.from(context).inflate(R.layout.ta_shoucang_empty_layout, parent, false);

        } else {
            empty_view = LayoutInflater.from(context).inflate(R.layout.shoucang_empty_layout, parent, false);

        }

        View shidan_view = LayoutInflater.from(context).inflate(R.layout.guanzhuwenti1, parent, false);
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

            if (position == 0) {
                holder.title.setText(getTitleName());
                holder.title.setVisibility(View.VISIBLE);
            } else {
                holder.title.setText(getTitleName());
                holder.title.setVisibility(View.GONE);
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
            holder.question_answer.setText(list.get(position).getContent_remove());
            Glide.with(context.getApplicationContext()).load(list.get(position).getAvatar())
                    .asBitmap()
                    .error(R.drawable.touxiang)
                    .into(holder.select_head_image);
            holder.user_name.setText(list.get(position).getName());
            holder.zan_number.setText(list.get(position).getThumbs());
            holder.shoucang_number.setText(list.get(position).getCollection());
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
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            //跳转问题详情
            holder.question_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FenLeiQuestionDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("wenti_id", list.get(position).getPid());
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(intent);
                }
            });
            //跳转答案详情
            holder.question_answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AnserActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("answer_id", list.get(position).getId());
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            //跳转他人主页
            holder.touxiang_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    if (null != list.get(position).getUid()) {
                        context.startActivity(intent);
                        intent.putExtra("UserId", list.get(position).getUid());
                        context.startActivity(intent);
                    }
                }
            });

        } else if (view_holder instanceof EmptyViewHolder) {
            EmptyViewHolder holder = (EmptyViewHolder) view_holder;
            if (position == 0) {
                holder.title.setText(getTitleName());
                holder.title.setVisibility(View.VISIBLE);
            } else {
                holder.title.setText(getTitleName());
                holder.title.setVisibility(View.GONE);
            }
        }


    }

    private static Gson buildGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapterFactory(DataTypeAdaptor.FACTORY);
        return gsonBuilder.create();
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

    public void setData(List<MyShouCangEntity> list) {
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
        public TextView question_content, question_answer, user_name, zan_number, shoucang_number;
        public CircleImageView select_head_image;
        public RelativeLayout touxiang_layout;
        public ListViewForScrollView images_view;
        private TextView title;

        public PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                images_view = (ListViewForScrollView) itemView.findViewById(R.id.images_view);
                question_content = (TextView) itemView.findViewById(R.id.question_content);
                question_answer = (TextView) itemView.findViewById(R.id.question_answer);
                select_head_image = (CircleImageView) itemView.findViewById(R.id.select_head_image);
                user_name = (TextView) itemView.findViewById(R.id.user_name);
                zan_number = (TextView) itemView.findViewById(R.id.zan_number);
                shoucang_number = (TextView) itemView.findViewById(R.id.shoucang_number);
                touxiang_layout = (RelativeLayout) itemView.findViewById(R.id.touxiang_layout);
                title = itemView.findViewById(R.id.title);
            }
        }
    }
}

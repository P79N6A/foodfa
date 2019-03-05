package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.entity.SearchAnswerItem;
import com.app.cookbook.xinhe.foodfamily.util.Textutil;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;


/**
 * Created by 18030150 on 2018/3/20.
 */

public class SearchAnswerAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {

    private List<SearchAnswerItem> searchAnswers;
    private List<String> participle;
    private LayoutInflater inflater;
    private Context context;
    private boolean ista;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Handler handler;

    public SearchAnswerAdapter(Context context, boolean mista, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public void setSearchAnswers(List<SearchAnswerItem> searchAnswers) {
        this.searchAnswers = searchAnswers;
    }

    public void setParticiple(List<String> participle) {
        this.participle = participle;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        Holder holder = new Holder(view, false);
        return holder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        RecyclerView.ViewHolder holder = getViewHolderByViewType(viewType, parent);
        return holder;
    }

    private RecyclerView.ViewHolder getViewHolderByViewType(int viewType, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_answer_item, parent, false);
        View empty_view = LayoutInflater.from(context).inflate(R.layout.search_empty_layout, parent, false);
        RecyclerView.ViewHolder holder = null;
        if (viewType == EMPTY_VIEW) {
            holder = new EmptyViewHolder(empty_view, true);
        } else {
            holder = new Holder(view, true);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderView, final int position, boolean isItem) {
        if (holderView instanceof Holder) {
            final Holder holder = (Holder) holderView;
            SearchAnswerItem issue = searchAnswers.get(position);
            if (issue.getQuestions_title() != null && !TextUtils.isEmpty(issue.getQuestions_title()) && participle.size() > 0) {
                //多次标红
                SpannableString str = Textutil.foramtString(context,issue.getQuestions_title(), participle);
                holder.answer_title.setText(str);
            } else {
                holder.answer_title.setText(issue.getQuestions_title());
            }
            if (issue.getContent_remove() != null && !TextUtils.isEmpty(issue.getContent_remove()) && participle.size() > 0) {
                //多次标红
                SpannableString str1 = Textutil.foramtString(context,Html.fromHtml(issue.getContent_remove()).toString(), participle);
                holder.answer_content.setText(str1);
            } else {
                holder.answer_content.setText(Html.fromHtml(issue.getContent_remove()).toString());
            }

            if (issue.getImg_data() != null) {
                holder.answer_image.setVisibility(View.VISIBLE);
                String imagePath = issue.getImg_data().getPath();
                Glide.with(context).load(imagePath)
                        .asBitmap()
                        .placeholder(R.drawable.morenbg)
                        .error(R.drawable.morenbg)
                        .into(holder.answer_image);
            } else {
                holder.answer_image.setVisibility(View.GONE);
            }

            holder.answer_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = 400;
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                }
            });

            holder.content_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = 401;
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
    }

    @Override
    public int getAdapterItemViewType(int position) {
        if (searchAnswers.size() == 0) {
            return EMPTY_VIEW;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getAdapterItemCount() {
        return searchAnswers == null ? 0 : searchAnswers.size();
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

    class Holder extends RecyclerView.ViewHolder {

        private TextView answer_title;
        private TextView answer_content;
        private RoundedImageView answer_image;
        private LinearLayout content_layout;

        public Holder(View itemView, boolean isItem) {
            super(itemView);
            content_layout = itemView.findViewById(R.id.content_layout);
            answer_title = (TextView) itemView.findViewById(R.id.answer_title);
            answer_content = (TextView) itemView.findViewById(R.id.answer_content);
            answer_image = (RoundedImageView) itemView.findViewById(R.id.answer_image);
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

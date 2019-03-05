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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.entity.SearchItems;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.util.Textutil;
import com.bumptech.glide.Glide;

import java.util.List;


/**
 * Created by 18030150 on 2018/3/20.
 */

public class SearchIssueAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {

    private List<SearchItems> searchIssues;
    private List<String> participle;
    private Context context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_ITEM = 1;

    private Handler handler;

    public SearchIssueAdapter(Context context, boolean mista, Handler handler) {
        this.context = context;
        this.handler = handler;
//        ista = mista;
    }

    public void setSearchIssues(List<SearchItems> searchIssues) {
        this.searchIssues = searchIssues;
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
        View view = LayoutInflater.from(context).inflate(R.layout.search_issue_item, parent, false);
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
            SearchItems issue = searchIssues.get(position);
            if (issue.getQuestion_title() != null && !TextUtils.isEmpty(issue.getQuestion_title()) && participle.size() > 0) {
                SpannableString strtv = Textutil.foramtString(context,issue.getQuestion_title(), participle);
                holder.tv_title.setText(strtv);
            } else {
                holder.tv_title.setText(issue.getQuestion_title());
            }
            holder.tv_answer_num.setText(issue.getAnswer_count() + "个回答");
            if (!"0".endsWith(issue.getAnswer_count())) {
                if (!TextUtils.isEmpty(issue.getAnswer().getAnswer_content_remove()) && null != issue.getAnswer().getAnswer_content_remove() && participle.size() > 0) {
                    holder.tv_content_layout.setVisibility(View.VISIBLE);
                    SpannableString str = Textutil.foramtString(context,Html.fromHtml(issue.getAnswer().getAnswer_content_remove()).toString(), participle);
                    holder.tv_content.setText(str);
                } else {
                    holder.tv_content.setText(Html.fromHtml(issue.getAnswer().getAnswer_content_remove()).toString());
                }
                if (issue.getAnswer().getImg_data() != null) {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.tv_content_layout.getLayoutParams();
                    lp.setMargins(0, 0, 330, 0);
                    holder.tv_content_layout.setLayoutParams(lp);
                    holder.image_issue.setVisibility(View.VISIBLE);
                    String imagePath = issue.getAnswer().getImg_data().getPath();
                    Glide.with(context).load(imagePath)
                            .asBitmap()
                            .error(R.drawable.morenbg)
                            .into(holder.image_issue);
                } else {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.tv_content_layout.getLayoutParams();
                    lp.setMargins(0, 0, 45, 0);
                    holder.tv_content_layout.setLayoutParams(lp);
                    holder.image_issue.setVisibility(View.GONE);
                }
            } else {
                holder.tv_content.setVisibility(View.GONE);
                holder.image_issue.setVisibility(View.GONE);
            }

            holder.tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = 400;
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                }
            });

            holder.tv_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = 401;
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                }
            });

            holder.image_issue.setOnClickListener(new View.OnClickListener() {
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
                        mOnItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
            }
        }
    }

    @Override
    public int getAdapterItemViewType(int position) {
        if (searchIssues.size() == 0) {
            return EMPTY_VIEW;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getAdapterItemCount() {
        return searchIssues == null ? 0 : searchIssues.size();
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

        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_answer_num;
        private ImageView image_issue;
        private RelativeLayout content_layout;
        private RelativeLayout tv_content_layout;

        public Holder(View itemView, boolean isItem) {
            super(itemView);
            content_layout = itemView.findViewById(R.id.content_layout);
            tv_content_layout = itemView.findViewById(R.id.tv_content_layout);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_answer_num = (TextView) itemView.findViewById(R.id.tv_answer_num);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            image_issue = (ImageView) itemView.findViewById(R.id.image_issue);
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

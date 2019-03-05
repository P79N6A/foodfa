package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.entity.SearchUserItem;
import com.app.cookbook.xinhe.foodfamily.util.Textutil;
import com.bumptech.glide.Glide;

import java.util.List;


/**
 * Created by 18030150 on 2018/3/20.
 */

public class SearchUserAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {

    private List<SearchUserItem> searchIssues;
    private List<String> participle;
    private LayoutInflater inflater;
    private Context context;
    private boolean ista;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public SearchUserAdapter(Context context, boolean mista) {
        this.context = context;
//        this.inflater = LayoutInflater.from(context);
//        ista = mista;
    }

    public void setSearchIssues(List<SearchUserItem> searchIssues) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_item, parent, false);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holderView, int position, boolean isItem) {
        Log.e("123", "              " + position);
        if (holderView instanceof Holder) {
            final Holder holder = (Holder) holderView;
            SearchUserItem issue = searchIssues.get(position);
            if (issue.getName() != null && !TextUtils.isEmpty(issue.getName()) && participle.size() > 0) {
                SpannableString str = Textutil.foramtString(context,issue.getName(), participle);
                holder.user_title.setText(str);
            } else {
                holder.user_title.setText(issue.getName());
            }
            if (null != issue.getProfession()) {
                holder.uder_signature.setText(issue.getProfession());
            } else {
                holder.uder_signature.setText("未知");
            }
            holder.user_issue.setText(issue.getQuestions() + "提问");
            holder.user_answer.setText(issue.getAnswer() + "回答");

            if (issue.getAnswer() != null) {
                if (issue.getAvatar() != null) {
                    holder.touxiang_image.setVisibility(View.VISIBLE);
                    String imagePath = issue.getAvatar();
                    Glide.with(context).load(imagePath)
                            .error(R.drawable.touxiang)
                            .into(holder.touxiang_image);
                }
            } else {
                holder.touxiang_image.setVisibility(View.GONE);
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

        private TextView user_title;
        private TextView uder_signature;
        private TextView user_issue;
        private TextView user_answer;
        private ImageView touxiang_image;

        public Holder(View itemView, boolean isItem) {
            super(itemView);
            user_title = (TextView) itemView.findViewById(R.id.user_title);
            uder_signature = (TextView) itemView.findViewById(R.id.uder_signature);
            user_issue = (TextView) itemView.findViewById(R.id.user_issue);
            user_answer = (TextView) itemView.findViewById(R.id.user_answer);
            touxiang_image = (ImageView) itemView.findViewById(R.id.touxiang_image);
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

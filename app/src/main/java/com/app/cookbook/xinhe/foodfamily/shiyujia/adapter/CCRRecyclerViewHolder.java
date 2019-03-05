package com.app.cookbook.xinhe.foodfamily.shiyujia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.cookbook.xinhe.foodfamily.util.listen.CCROnNoDoubleClickListener;
import com.app.cookbook.xinhe.foodfamily.util.listen.CCROnRVItemClickListener;
import com.app.cookbook.xinhe.foodfamily.util.listen.CCROnRVItemLongClickListener;

/**
 * 在此写用途
 *
 * @Author: Acheng
 * @Email: 345887272@qq.com
 * @Date: 2017-09-22 11:59
 * @Version: V1.0 <描述当前版本功能>
 */

public class CCRRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
    protected Context mContext;
    protected CCROnRVItemClickListener mOnRVItemClickListener;
    protected CCROnRVItemLongClickListener mOnRVItemLongClickListener;
    protected CCRViewHolderHelper mViewHolderHelper;
    protected RecyclerView mRecyclerView;
    protected CCRRecyclerViewAdapter mRecyclerViewAdapter;

    public CCRRecyclerViewHolder(CCRRecyclerViewAdapter recyclerViewAdapter, RecyclerView recyclerView, View itemView, CCROnRVItemClickListener onRVItemClickListener, CCROnRVItemLongClickListener onRVItemLongClickListener) {
        super(itemView);
        mRecyclerViewAdapter = recyclerViewAdapter;
        mRecyclerView = recyclerView;
        mContext = mRecyclerView.getContext();
        mOnRVItemClickListener = onRVItemClickListener;
        mOnRVItemLongClickListener = onRVItemLongClickListener;
        itemView.setOnClickListener(new CCROnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (v.getId() == CCRRecyclerViewHolder.this.itemView.getId() && null != mOnRVItemClickListener) {
                    mOnRVItemClickListener.onRVItemClick(mRecyclerView, v, getAdapterPositionWrapper());
                }
            }
        });
        itemView.setOnLongClickListener(this);

        mViewHolderHelper = new CCRViewHolderHelper(mRecyclerView, this);
    }

    public CCRViewHolderHelper getViewHolderHelper() {
        return mViewHolderHelper;
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == this.itemView.getId() && null != mOnRVItemLongClickListener) {
            return mOnRVItemLongClickListener.onRVItemLongClick(mRecyclerView, v, getAdapterPositionWrapper());
        }
        return false;
    }

    public int getAdapterPositionWrapper() {
        if (mRecyclerViewAdapter.getHeadersCount() > 0) {
            return getAdapterPosition() - mRecyclerViewAdapter.getHeadersCount();
        } else {
            return getAdapterPosition();
        }
    }
}

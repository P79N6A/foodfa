package com.app.cookbook.xinhe.foodfamily.util.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.util.StringsHelper;


/**
 * Created by wn on 2017/2/17.
 */

public class LoadMoreView {
    private Context mContext;
    private View mView;
    private TextView mTextView;

    @SuppressLint("InflateParams")
    public LoadMoreView(Context context) {
        mContext = context;
        mView = LayoutInflater.from(mContext).inflate(
                R.layout.bbs_loadmore_layout, null);
        mTextView = (TextView) mView.findViewById(R.id.loadmore_text);
        mTextView.setText(StringsHelper.getString(R.string.bbs_proress_content));
    }

    public View getView() {
        return mView;
    }

}

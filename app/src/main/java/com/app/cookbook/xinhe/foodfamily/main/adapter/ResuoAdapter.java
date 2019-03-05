package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.entity.HotSearch;

import java.util.List;

public class ResuoAdapter extends RecyclerView.Adapter<ResuoAdapter.ViewHolder>{
    private List<HotSearch> mFruitList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tag_tv;
        View itemView;
        public ViewHolder(View mitemView) {
            super(mitemView);
            itemView = mitemView;
            tag_tv = (TextView) itemView.findViewById(R.id.tag_tv);

        }
    }

    /**
     * 用于把要展示的数据源传进来，并赋值给一个全局变量mFruitList，我们后续的操作都将在这个数据源的基础上进行。
     * @param mFruitList
     */
    public ResuoAdapter(List<HotSearch> mFruitList) {
        this.mFruitList = mFruitList;
    }


    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    /**
     * 用于创建ViewHolder实例的，并把加载出来的布局传入到构造函数当中，最后将viewholder的实例返回
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resou_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * 用于对RecyclerView子项的数据进行赋值的，会在每个子项被滚动到屏幕内的时候执行，这里我们通过
     * position参数得到当前项的Fruit实例，然后再将数据设置到ViewHolder的Imageview和textview当中即可，
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        HotSearch hotSearch = mFruitList.get(position);
        holder.tag_tv.setText(hotSearch.getName());

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int mposition = position;
                    mOnItemClickListener.onItemClick(holder.itemView, mposition);
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return mFruitList.size();
    }
}

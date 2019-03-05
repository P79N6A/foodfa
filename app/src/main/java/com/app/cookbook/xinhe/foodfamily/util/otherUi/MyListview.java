package com.app.cookbook.xinhe.foodfamily.util.otherUi;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by shiyujia02 on 2018/1/7.
 */

public class MyListview extends AdapterView<ListAdapter> {
    private ListAdapter adapter = null;
    private GestureDetector mGesture;
    private Queue<View> cacheView = new LinkedList<>();//列表项缓存视图
    private int firstItemIndex = 0;//显示的第一个子项的下标
    private int lastItemIndex = -1;//显示的最后的一个子项的下标
    private int scrollValue=0;//列表已经发生有效滚动的位移值
    private int hasToScrollValue=0;//接下来列表发生滚动所要达到的位移值
    private int maxScrollValue=Integer.MAX_VALUE;//列表发生滚动所能达到的最大位移值（这个由最后显示的列表项决定）
    private int displayOffset=0;//列表显示的偏移值（用于矫正列表显示的所有子项的显示位置）

    public MyListview(Context context) {
        super(context);
        init(context);
    }

    public MyListview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyListview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void init(Context context){
        mGesture = new GestureDetector(getContext(), mOnGesture);
    }

    private void initParams(){
        removeAllViewsInLayout();
        if(adapter!=null&&lastItemIndex<adapter.getCount())
            hasToScrollValue=scrollValue;//保持显示位置不变
        else hasToScrollValue=0;//滚动到列表头
        scrollValue=0;//列表已经发生有效滚动的位移值
        firstItemIndex = 0;//显示的第一个子项的下标
        lastItemIndex = -1;//显示的最后的一个子项的下标
        maxScrollValue=Integer.MAX_VALUE;//列表发生滚动所能达到的最大位移值（这个由最后显示的列表项决定）
        displayOffset=0;//列表显示的偏移值（用于矫正列表显示的所有子项的显示位置）
        requestLayout();
    }


    private DataSetObserver mDataObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            //执行Adapter数据改变时的逻辑
            initParams();
        }

        @Override
        public void onInvalidated() {
            //执行Adapter数据失效时的逻辑
            initParams();
        }

    };

    @Override
    public ListAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if(adapter!=null){
            adapter.registerDataSetObserver(mDataObserver);
        }
        if(this.adapter!=null){
            this.adapter.unregisterDataSetObserver(mDataObserver);
        }
        this.adapter=adapter;
        requestLayout();
    }

    @Override
    public View getSelectedView() {
        return null;
    }

    @Override
    public void setSelection(int position) {

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        /*
         1).计算当前列表发生滚动的滚动“位移值”，记录已经发生有效滚动的“位移累加值”
         2).根据“位移值”提取需要缓存的视图（已经滚动到可视区域外的列表项）
         3).根据“位移值”设置需要显示的的列表项
         4).根据整体列表“显示偏移值”整顿所有列表项位置（调用子view的列表项）
         5).计算可以发生滚动的“最大位移值”
         */
        int dx=calculateScrollValue();
        removeNonVisibleItems(dx);
        showListItem(dx);
        adjustItems();
        calculateMaxScrollValue();
    }




    /**
     * 计算这一次整体滚动偏移量
     * @return
     */
    private int calculateScrollValue(){
        int dx=0;
        hasToScrollValue=hasToScrollValue<0? 0:hasToScrollValue;
        hasToScrollValue=hasToScrollValue>maxScrollValue? maxScrollValue:hasToScrollValue;
        dx=hasToScrollValue-scrollValue;
        scrollValue=hasToScrollValue;

        return -dx;
    }

    /**
     * 计算最大滚动值
     */
    private void calculateMaxScrollValue(){
        if(adapter==null) return;
        if(lastItemIndex==adapter.getCount()-1) {//已经显示了最后一项
            if(getChildAt(getChildCount() - 1).getRight()>=getShowEndEdge()) {
                maxScrollValue = scrollValue + getChildAt(getChildCount() - 1).getRight() - getShowEndEdge();            }else{
                maxScrollValue = 0;
            }
        }
    }

    /**
     * 根据偏移量提取需要缓存视图
     * @param dx
     */
    private void removeNonVisibleItems(int dx) {
        if(getChildCount()>0) {
            //移除列表头
            View child = getChildAt(getChildCount());
            while (getChildCount()>0&&child != null && child.getRight() + dx <= 0) {
                displayOffset += child.getMeasuredWidth();
                cacheView.offer(child);
                removeViewInLayout(child);
                firstItemIndex++;
                child = getChildAt(0);
            }

            //移除列表尾
            child = getChildAt(getChildCount()-1);
            while (getChildCount()>0&&child != null && child.getLeft() + dx >= getShowEndEdge()) {
                cacheView.offer(child);
                removeViewInLayout(child);
                lastItemIndex--;
                child = getChildAt(getChildCount()-1);
            }
        }
    }
    /**
     * 根据偏移量显示新的列表项
     * @param dx
     */
    private void showListItem(int dx) {
        if(adapter==null)return;

        int firstItemEdge = getFirstItemLeftEdge()+dx;
        int lastItemEdge = getLastItemRightEdge()+dx;
        displayOffset+=dx;//计算偏移量
        //显示列表头视图
        while(firstItemEdge > getPaddingLeft() && firstItemIndex-1 >= 0) {
            firstItemIndex--;//往前显示一个列表项
            View child = adapter.getView(firstItemIndex, cacheView.poll(), this);
            addAndMeasureChild(child, 0);
            firstItemEdge -= child.getMeasuredWidth();
            displayOffset -= child.getMeasuredWidth();
        }
        //显示列表未视图
        while(lastItemEdge < getShowEndEdge() && lastItemIndex+1 < adapter.getCount()) {
            lastItemIndex++;//往后显示一个列表项
            View child = adapter.getView(lastItemIndex, cacheView.poll(), this);
            addAndMeasureChild(child, getChildCount());
            lastItemEdge += child.getMeasuredWidth();
        }
    }
    /**
     * 调整各个item的位置
     */
    private void adjustItems() {
        if(getChildCount() > 0){
            int left = displayOffset+getPaddingLeft();
            int endIndex = getChildCount()-1;

            for(int i=0;i<=endIndex;i++){
                View child = getChildAt(i);
                int childWidth = child.getMeasuredWidth();
                child.layout(left, getPaddingTop(), left + childWidth, child.getMeasuredHeight()+getPaddingTop());
                left += childWidth + child.getPaddingRight();
            }
        }
    }

    /**
     * 取得视图可见区域的右边界
     * @return
     */
    private int getShowEndEdge(){
        return getWidth()-getPaddingRight();
    }

    private int getFirstItemLeftEdge(){
        if(getChildCount()>0) {
            return getChildAt(0).getLeft();
        }else{
            return 0;
        }
    }

    private int getLastItemRightEdge(){
        if(getChildCount()>0) {
            return getChildAt(getChildCount()-1).getRight();
        }else{
            return 0;
        }
    }

    private void addAndMeasureChild(View child, int viewIndex) {
        LayoutParams params = child.getLayoutParams();
        params = params==null ? new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT):params;

        addViewInLayout(child, viewIndex, params, true);
        child.measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.UNSPECIFIED));
    }
    /**
     * 在onTouchEvent处理事件，让子视图优先消费事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGesture.onTouchEvent(event);
    }

    private GestureDetector.OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            synchronized(MyListview.this){
                hasToScrollValue += (int)distanceX;
            }
            requestLayout();
            return true;
        }
    };
}

package com.app.cookbook.xinhe.foodfamily.util.layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 *
 * @author linxiao
 * @version 1.0
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.setRetainInstance(true);
    }


    /**
     * 通过此方法简化绑定View流程,避免每次都写强制转型
     * */
    protected <T extends View> T findView(View layoutView, int resId) {
        return (T) layoutView.findViewById(resId);
    }

    /**
     * 当前界面是否呈现给用户的状态标志
     */
    protected boolean isVisible;
    /**
     * 重写Fragment父类生命周期方法，在onCreate之前调用该方法，实现Fragment数据的缓加载.
     * @param isVisibleToUser 当前是否已将界面显示给用户的状态
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
    /**
     * 当界面呈现给用户，即设置可见时执行，进行加载数据的方法
     * 在用户可见时加载数据，而不在用户不可见的时候加载数据，是为了防止控件对象出现空指针异常
     */
    protected void onVisible(){
        setlazyLoad();
    }
    /**
     * 当界面还没呈现给用户，即设置不可见时执行
     */
    protected void onInvisible(){
    }
    /**
     * 加载数据方法
     */
    protected void setlazyLoad(){
    }

}

package com.app.cookbook.xinhe.foodfamily.shiyujia.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.TabItemAdapter;
import com.app.cookbook.xinhe.foodfamily.shiyujia.fragment.MyCaiPuFragment;
import com.app.cookbook.xinhe.foodfamily.shiyujia.fragment.MyCollectAnswerFragment;
import com.app.cookbook.xinhe.foodfamily.shiyujia.fragment.MyCollectPrintFragment;
import com.app.cookbook.xinhe.foodfamily.util.StatusBarUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import chuangyuan.ycj.videolibrary.video.VideoPlayerManager;

public class MyCollectActivity extends AppCompatActivity implements View.OnClickListener {

    //返回
    @BindView(R.id.iamge_back)
    LinearLayout iamge_back;
    //标题
    @BindView(R.id.title_tv)
    TextView title_tv;
    //选项
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        steepStatusBar();
        setContentView(R.layout.my_action_activity);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        title_tv.setText("我的收藏");
        ArrayList<String> titleDatas = new ArrayList<>();
        titleDatas.add("印迹");
        titleDatas.add("回答");
        titleDatas.add("菜谱");
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new MyCollectPrintFragment());
        fragmentList.add(new MyCollectAnswerFragment());
        fragmentList.add(new MyCaiPuFragment());

        TabItemAdapter tabItemAdapter = new TabItemAdapter(this.getSupportFragmentManager(), titleDatas, fragmentList);
        viewPager.setAdapter(tabItemAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(tabItemAdapter);

        iamge_back.setOnClickListener(this);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    VideoPlayerManager.getInstance().onPause(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iamge_back:
                finish();
                break;
        }
    }

    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(MyCollectActivity.this, R.color.white);
        } else {
            StatusBarUtil.transparencyBar(MyCollectActivity.this);
        }
    }
}

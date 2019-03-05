package com.app.cookbook.xinhe.foodfamily.main.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.FoodFamilyActivity;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.main.MainSearchActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageEvent;
import com.app.cookbook.xinhe.foodfamily.util.CircleImageView;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.ScrollLinearLayout;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.ui.ViewPagerFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 18030150 on 2018/5/24.
 */

public class MainFragment extends ViewPagerFragment {


    private View view;
    private TabLayout tabLayout;
    //    private TabLayout tabLayoutTwo;
//    @BindView(R.id.mAppBarLayout)
//    AppBarLayout mAppBarLayout;
    @BindView(R.id.sll_nav)
    ScrollLinearLayout sll_nav;
    @BindView(R.id.search_btn_layout)
    LinearLayout search_btn_layout;
    //返回顶部按钮
    @BindView(R.id.top_btn)
    CircleImageView top_btn;
    //选中页
    private int pageType = 2;
    //选项适配器
    private ViewPagerAdapter adapter;

    public static MainFragment newInstance(String title) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString("home_fragment_title", title);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.main_fragment, null);
            EventBus.getDefault().register(this);
            ButterKnife.bind(this, view);//绑定黄牛刀
            setupToolbar();
            setupViewPager();
            setupCollapsingToolbar();
            setBackTop();
        }
        return view;
    }


    private void setupCollapsingToolbar() {
//        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(
//                R.id.collapse_toolbar);
//        collapsingToolbar.setTitleEnabled(false);

        //打开搜索页面
        search_btn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), PerfectInformationActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(getActivity(), MainSearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupViewPager() {
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
//        tabLayoutTwo = (TabLayout) view.findViewById(R.id.tabsTwo);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayoutTwo.setupWithViewPager(viewPager);

        setIndicator(tabLayout, 30, 30);

        //设置tab的点击监听器
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                        top_btn.setVisibility(View.GONE);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                }

                switch (tab.getPosition()) {
                    case 0:
                        if ("105".equals(Contacts.attentionType)) {
                            top_btn.setVisibility(View.GONE);
                        } else if ("106".equals(Contacts.attentionType)) {
                            top_btn.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 1:
                        if ("101".equals(Contacts.hotType)) {
                            top_btn.setVisibility(View.GONE);
                        } else if ("102".equals(Contacts.hotType)) {
                            top_btn.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 2:
                        if ("103".equals(Contacts.newType)) {
                            top_btn.setVisibility(View.GONE);
                        } else if ("104".equals(Contacts.newType)) {
                            top_btn.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setupToolbar() {
//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        FoodFamilyActivity.activity.setSupportActionBar(toolbar);
//        final ActionBar ab = FoodFamilyActivity.activity.getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(true);
    }


    private void setupViewPager(final ViewPager viewPager) {
        adapter = new ViewPagerAdapter(FoodFamilyActivity.activity.getSupportFragmentManager());
        adapter.addFrag(new AttentionFragment(), "关注");
        adapter.addFrag(new HotFragment(), "精选");
        adapter.addFrag(new NewFragment(), "最新");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageType = position + 1;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void fetchData() {

    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    //订阅方法，当接收到事件的时候，会调用该方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        switch (messageEvent.getMessage()) {
            case "400":
//                setIndicator(tabLayoutTwo, 30, 30);
//                tabLayout.setVisibility(View.GONE);
//                tabLayoutTwo.setVisibility(View.VISIBLE);
                break;
            case "401":
                setIndicator(tabLayout, 30, 30);
//                tabLayout.setVisibility(View.VISIBLE);
//                tabLayoutTwo.setVisibility(View.GONE);
//                mAppBarLayout.setExpanded(true);
                break;
            case "402":
//                setIndicator(tabLayoutTwo, 30, 30);
//                tabLayout.setVisibility(View.GONE);
//                tabLayoutTwo.setVisibility(View.VISIBLE);
                break;
            case "403":
                setIndicator(tabLayout, 30, 30);
//                tabLayout.setVisibility(View.VISIBLE);
//                tabLayoutTwo.setVisibility(View.GONE);
//                mAppBarLayout.setExpanded(true);
                break;
            case "404":
//                setIndicator(tabLayoutTwo, 30, 30);
//                tabLayout.setVisibility(View.GONE);
//                tabLayoutTwo.setVisibility(View.VISIBLE);
                break;
            case "405":
                setIndicator(tabLayout, 30, 30);
//                tabLayout.setVisibility(View.VISIBLE);
//                tabLayoutTwo.setVisibility(View.GONE);
//                mAppBarLayout.setExpanded(true);
                break;
            case "101":
                top_btn.setVisibility(View.GONE);
                break;
            case "102":
                top_btn.setVisibility(View.VISIBLE);
                break;
            case "103":
                top_btn.setVisibility(View.GONE);
                break;
            case "104":
                top_btn.setVisibility(View.VISIBLE);
                break;
            case "105":
                top_btn.setVisibility(View.GONE);
                break;
            case "106":
                top_btn.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setBackTop() {
        top_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIndicator(tabLayout, 30, 30);
                tabLayout.setVisibility(View.VISIBLE);
//                tabLayoutTwo.setVisibility(View.GONE);
                if (pageType == 1) {
//                    mAppBarLayout.setExpanded(true);
                    EventBus.getDefault().post(new MessageEvent("303"));
                } else if (pageType == 2) {
//                    mAppBarLayout.setExpanded(true);
                    EventBus.getDefault().post(new MessageEvent("301"));
                } else if (pageType == 3) {
//                    mAppBarLayout.setExpanded(true);
                    EventBus.getDefault().post(new MessageEvent("302"));
                }
            }
        });
    }


}

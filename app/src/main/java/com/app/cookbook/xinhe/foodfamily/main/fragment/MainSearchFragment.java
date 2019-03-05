package com.app.cookbook.xinhe.foodfamily.main.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.AddQuestionActivity;
import com.app.cookbook.xinhe.foodfamily.main.MainSearchActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageEvent;
import com.app.cookbook.xinhe.foodfamily.util.CircleImageView;
import com.app.cookbook.xinhe.foodfamily.util.ui.ViewPagerFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;


public class MainSearchFragment extends ViewPagerFragment {
    /**
     * Rxjava
     */
    protected Subscription subscription;
    @BindView(R.id.question_btn)
    LinearLayout question_btn;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.jianxuan_title)
    TextView jianxuan_title;
    @BindView(R.id.jingxuan_fenge)
    LinearLayout jingxuan_fenge;
    @BindView(R.id.zuixin_title)
    TextView zuixin_title;
    @BindView(R.id.zuixin_fenge)
    LinearLayout zuixin_fenge;
    @BindView(R.id.select_jingxuan)
    LinearLayout select_jingxuan;
    @BindView(R.id.select_zuixin)
    LinearLayout select_zuixin;
    @BindView(R.id.search_btn)
    ImageView search_btn;
    @BindView(R.id.rel_top)
    RelativeLayout rel_top;
    @BindView(R.id.search_btn_layout)
    LinearLayout search_btn_layout;
    @BindView(R.id.top_btn)
    CircleImageView top_btn;

    @BindView(R.id.fnege_layout_two)
    RelativeLayout fnege_layout_two;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;


    private ContentPagerAdapterMy contentAdapter;
    private List<Fragment> displayFragments;
    View view;

    //
    private int pageType = 1;


    public static MainSearchFragment newInstance(String title) {
        MainSearchFragment fragment = new MainSearchFragment();
        Bundle args = new Bundle();
        args.putString("home_fragment_title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void fetchData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_search_fragement, null);
            EventBus.getDefault().register(this);
            ButterKnife.bind(this, view);//绑定黄牛刀
            initDate();
            setBackTop();
        }

        //打开提问页面
        question_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddQuestionActivity.class);
                intent.putExtra("flag", 0);//编辑笔记
                startActivity(intent);
            }
        });


        //打开搜索页面
        search_btn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainSearchActivity.class);
                startActivity(intent);
            }
        });
        select_jingxuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
                show_one();
                pageType = 1;
            }
        });

        select_zuixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
                show_two();
                pageType = 2;
            }
        });
        return view;
    }

    FragmentManager fragmentManager;

    private void initDate() {
        displayFragments = new ArrayList<>();
        displayFragments.add(HotFragment.newInstance("最热"));
        displayFragments.add(NewFragment.newInstance("最新"));
        if (fragmentManager == null) {
            fragmentManager = getChildFragmentManager();
        }
        if (contentAdapter == null) {
            contentAdapter = new ContentPagerAdapterMy(fragmentManager);
        }
        viewPager.setAdapter(contentAdapter);
        viewPager.setCurrentItem(0);
        //首先选择第一个
        show_one();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    show_one();
                    pageType = 1;
                } else if (position == 1) {
                    show_two();
                    pageType = 2;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void setBackTop() {
        top_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageType == 1) {
                    jianxuan_title.setTextSize(16);
                    zuixin_title.setTextSize(16);
                    rel_top.setVisibility(View.VISIBLE);
                    EventBus.getDefault().post(new MessageEvent("300"));
                } else if (pageType == 2) {
                    jianxuan_title.setTextSize(16);
                    zuixin_title.setTextSize(16);
                    rel_top.setVisibility(View.VISIBLE);
                    EventBus.getDefault().post(new MessageEvent("301"));
                }
            }
        });
    }


    class ContentPagerAdapterMy extends FragmentPagerAdapter {

        public ContentPagerAdapterMy(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return displayFragments.get(position);
        }

        @Override
        public int getCount() {
            return displayFragments.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }

    private void show_one() {
        jianxuan_title.setTextColor(Color.parseColor("#FF411C"));
        zuixin_title.setTextColor(Color.parseColor("#777777"));
        jingxuan_fenge.setVisibility(View.VISIBLE);
        zuixin_fenge.setVisibility(View.INVISIBLE);
    }


    private void show_two() {
        jianxuan_title.setTextColor(Color.parseColor("#777777"));
        zuixin_title.setTextColor(Color.parseColor("#FF411C"));
        jingxuan_fenge.setVisibility(View.INVISIBLE);
        zuixin_fenge.setVisibility(View.VISIBLE);
    }

    private boolean scrollType;

    //订阅方法，当接收到事件的时候，会调用该方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        switch (messageEvent.getMessage()) {
            case "200":
                if (pageType == 1) {
                    jianxuan_title.setTextSize(16);
                    zuixin_title.setTextSize(16);
                    rel_top.setVisibility(View.VISIBLE);
                    EventBus.getDefault().post(new MessageEvent("300"));
                } else if (pageType == 2) {
                    jianxuan_title.setTextSize(16);
                    zuixin_title.setTextSize(16);
                    rel_top.setVisibility(View.VISIBLE);
                    EventBus.getDefault().post(new MessageEvent("301"));
                }
                break;
            case "400":
                if (scrollType == false) {
                    jianxuan_title.setTextSize(14);
                    zuixin_title.setTextSize(14);
                    linearLayout.setVisibility(View.GONE);
                    fnege_layout_two.setVisibility(View.VISIBLE);
                    scrollType = true;
                }
                break;
            case "401":
                if (scrollType == true) {
                    jianxuan_title.setTextSize(16);
                    zuixin_title.setTextSize(16);
                    fnege_layout_two.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    scrollType = false;
                }
                break;
            case "402":
                if (scrollType == false) {
                    jianxuan_title.setTextSize(14);
                    zuixin_title.setTextSize(14);
                    linearLayout.setVisibility(View.GONE);
                    fnege_layout_two.setVisibility(View.VISIBLE);
                    scrollType = true;
                }
                break;
            case "403":
                if (scrollType == true) {
                    jianxuan_title.setTextSize(16);
                    zuixin_title.setTextSize(16);
                    fnege_layout_two.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    scrollType = false;
                }
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
        }
    }


}

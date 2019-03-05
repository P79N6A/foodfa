package com.app.cookbook.xinhe.foodfamily.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.main.fragment.DraftAnswerFragment;
import com.app.cookbook.xinhe.foodfamily.main.fragment.DraftQuestionFragment;
import com.app.cookbook.xinhe.foodfamily.util.ui.NoScrollViewPager;
import com.tencent.stat.StatService;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 草稿箱
 */
public class DraftActivity extends BaseActivity {
    @BindView(R.id.back_layout)
    LinearLayout back_layout;
    @BindView(R.id.huida_btn)
    LinearLayout huida_btn;
    @BindView(R.id.tiwen_btn)
    LinearLayout tiwen_btn;
    @BindView(R.id.tiwen_fra)
    FrameLayout tiwen_fra;
    @BindView(R.id.huida_fra)
    FrameLayout huida_fra;
    @BindView(R.id.tiwen_btn2)
    LinearLayout tiwen_btn2;
    @BindView(R.id.huida_btn2)
    LinearLayout huida_btn2;
    @BindView(R.id.viewpager_content)
    NoScrollViewPager viewPager;

    private List<Fragment> displayFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public void initView() {
        setContentLayout(R.layout.activity_draft);
        //初始化黄牛刀
        ButterKnife.bind(this);
        isShowTitle(false);
    }

    @Override
    public void initonResume() {

    }

    @Override
    public void initonPause() {

    }

    @Override
    public void doBusiness(Context mContext) {
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        huida_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "huida_btn");
                StatService.trackCustomKVEvent(getApplicationContext(), "Install_drafts_answer", prop);

                viewPager.setCurrentItem(1);
                show_two();

            }
        });
        huida_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "huida_btn");
                StatService.trackCustomKVEvent(getApplicationContext(), "Install_drafts_answer", prop);

                viewPager.setCurrentItem(1);
                show_two();
            }
        });
        tiwen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "tiwen_btn");
                StatService.trackCustomKVEvent(getApplicationContext(), "Install_drafts_question", prop);

                viewPager.setCurrentItem(0);
                show_one();
            }
        });
        tiwen_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "tiwen_btn");
                StatService.trackCustomKVEvent(getApplicationContext(), "Install_drafts_question", prop);

                viewPager.setCurrentItem(0);
                show_one();
            }
        });
        initFragment();

    }


    private void initFragment() {
        displayFragments.clear();
        displayFragments.add(DraftQuestionFragment.newInstance("提问草稿"));
        displayFragments.add(DraftAnswerFragment.newInstance("回答草稿"));
        ContentPagerAdapterMy contentAdapter = new ContentPagerAdapterMy(getSupportFragmentManager());
        viewPager.setAdapter(contentAdapter);
        viewPager.setCurrentItem(0);

    }

    private void show_two() {
        tiwen_fra.setVisibility(View.GONE);
        huida_fra.setVisibility(View.VISIBLE);
    }

    private void show_one() {
        tiwen_fra.setVisibility(View.VISIBLE);
        huida_fra.setVisibility(View.GONE);
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
            //super.destroyItem(container, position, object);
        }
    }

    @Override
    public void widgetClick(View v) {

    }
}

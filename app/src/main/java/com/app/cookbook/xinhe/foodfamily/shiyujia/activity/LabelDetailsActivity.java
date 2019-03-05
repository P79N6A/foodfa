package com.app.cookbook.xinhe.foodfamily.shiyujia.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.LabelDetails;
import com.app.cookbook.xinhe.foodfamily.shiyujia.fragment.LabelPrintFragment;
import com.app.cookbook.xinhe.foodfamily.shiyujia.fragment.QuestionsAnswersFragment;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.AppBarStateChangeListener;
import com.app.cookbook.xinhe.foodfamily.shiyujia.view.PolygonView;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.StatusBarUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;
import rx.Subscription;

public class LabelDetailsActivity extends AppCompatActivity {


    /**
     * Rxjava
     */
    protected Subscription subscription;
    //返回
    @BindView(R.id.iamge_back)
    LinearLayout iamge_back;
    //标题
    @BindView(R.id.title_tv)
    TextView title_tv;
    //关注
    @BindView(R.id.user_attention_two)
    TextView user_attention_two;
    //标签图片
    @BindView(R.id.label_im)
    PolygonView label_im;
    //标签名
    @BindView(R.id.label_name)
    TextView label_name;
    //标签信息
    @BindView(R.id.label_msg)
    TextView label_msg;
    //关注按钮
    @BindView(R.id.user_attention)
    TextView user_attention;
    //标签介绍
    @BindView(R.id.label_introduce)
    TextView label_introduce;

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.group)
    RadioGroup mGroup;

    @BindView(R.id.print_btn)
    RadioButton print_btn;

    @BindView(R.id.questions_answers_btn)
    RadioButton questions_answers_btn;

    private String id;

    //标签是否已经关注
    private String is_follow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_details);
        ButterKnife.bind(this);
        steepStatusBar();
        initView();
        requestUserMsg();
        setupCollapsingToolbar();
    }

    private void initView() {
        id = getIntent().getStringExtra("id");
        mGroup.setOnCheckedChangeListener(new CheckedChangeListener());
        mGroup.check(R.id.print_btn);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new LabelPrintFragment(), "印迹");
        adapter.addFrag(new QuestionsAnswersFragment(), "问答");
        viewpager.setAdapter(adapter);
        /**Tab
         * */
        viewpager.setOnPageChangeListener(new PageChangeListener());
        viewpager.setOffscreenPageLimit(2);

        iamge_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        user_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(LabelDetailsActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(LabelDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if ("2".equals(is_follow)) {
                        guanzhu_submit();
                    } else if ("1".equals(is_follow)) {
                        quxiao_guanzhu_submit();
                    }
                }
            }
        });

        user_attention_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(LabelDetailsActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(LabelDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if ("2".equals(is_follow)) {
                        guanzhu_submit();
                    } else if ("1".equals(is_follow)) {
                        quxiao_guanzhu_submit();
                    }
                }
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    title_tv.setVisibility(View.GONE);
                    user_attention_two.setVisibility(View.GONE);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    title_tv.setVisibility(View.VISIBLE);
                    user_attention_two.setVisibility(View.VISIBLE);
                } else {
                    //中间状态
                    title_tv.setVisibility(View.GONE);
                    user_attention_two.setVisibility(View.GONE);
                }
            }
        });
    }

    private void requestUserMsg() {
        Map<String, String> params = new HashMap<>();
        params.put("class_id", id);
        Log.e("标签详情：", params.toString());
        subscription = Network.getInstance("标签详情", LabelDetailsActivity.this)
                .getClassDetail(params,
                        new ProgressSubscriberNew<>(LabelDetails.class, new GsonSubscriberOnNextListener<LabelDetails>() {
                            @Override
                            public void on_post_entity(LabelDetails result) {
                                //设置页面的信息
                                is_follow = result.getIs_follow();
                                if ("2".equals(is_follow)) {
                                    user_attention.setText("关注");
                                    user_attention_two.setText("关注");
                                    user_attention.setTextColor(getResources().getColor(R.color.color_51B55C));
                                    user_attention.setBackgroundResource(R.drawable.answer_attention_user_bg_on);
                                    user_attention_two.setTextColor(getResources().getColor(R.color.color_51B55C));
                                } else if ("1".equals(is_follow)) {
                                    user_attention.setText("已关注");
                                    user_attention_two.setText("已关注");
                                    user_attention.setTextColor(getResources().getColor(R.color.pro_bg_error1));
                                    user_attention.setBackgroundResource(R.drawable.answer_attention_user_bg);
                                    user_attention_two.setTextColor(getResources().getColor(R.color.pro_bg_error1));
                                }

                                label_name.setText(result.getTitle());
                                label_msg.setText(result.getCount_follow_users() + "关注  " + result.getQuestion_count() + "内容");
                                label_introduce.setText(result.getDesc());
                                title_tv.setText(result.getTitle());

                                Glide.with(LabelDetailsActivity.this).load(result.getPath())
                                        .error(R.drawable.lingxing)
                                        .into(label_im);

                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "标签详情报错：" + error);
                            }
                        }, LabelDetailsActivity.this, false));
    }

    /**
     * 取消关注
     */
    private void quxiao_guanzhu_submit() {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        Log.e("取消关注分类ID：", params.toString());
        subscription = Network.getInstance("取消关注分类", LabelDetailsActivity.this)
                .quxiao_guanzhu(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "取消关注分类成功：" + result.getCode());
                                is_follow = "2";
                                user_attention.setText("关注");
                                user_attention_two.setText("关注");
                                user_attention.setTextColor(getResources().getColor(R.color.color_51B55C));
                                user_attention.setBackgroundResource(R.drawable.answer_attention_user_bg_on);
                                user_attention_two.setTextColor(getResources().getColor(R.color.color_51B55C));
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "取消关注分类报错：" + error);
                            }
                        }, LabelDetailsActivity.this, false));
    }

    /**
     * Rxjava
     */

    private void guanzhu_submit() {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        Log.e("关注分类ID：", params.toString());
        subscription = Network.getInstance("关注分类", LabelDetailsActivity.this)
                .guanzhufenlei(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "关注分类成功：" + result.getCode());
                                is_follow = "1";
                                user_attention.setText("已关注");
                                user_attention_two.setText("已关注");
                                user_attention.setTextColor(getResources().getColor(R.color.color_777777));
                                user_attention.setBackgroundResource(R.drawable.answer_attention_user_bg);
                                user_attention_two.setTextColor(getResources().getColor(R.color.color_777777));
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "关注分类报错：" + error);
                            }
                        }, LabelDetailsActivity.this, false));

    }


    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(LabelDetailsActivity.this, R.color.white);
        } else {
            StatusBarUtil.transparencyBar(LabelDetailsActivity.this);
        }
    }

    private void setupCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse_toolbar);
        collapsingToolbar.setTitleEnabled(true);
    }


    private class CheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.print_btn:
                    viewpager.setCurrentItem(0);
                    getPrint();
                    break;
                case R.id.questions_answers_btn:
                    viewpager.setCurrentItem(1);
                    getQuestionsAnswers();
                    break;
            }
        }
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    mGroup.check(R.id.print_btn);
                    break;
                case 1:
                    mGroup.check(R.id.questions_answers_btn);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
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

    private void getPrint() {
        print_btn.setText("印迹");
        print_btn.setTextColor(getResources().getColor(R.color.color_0EB39B));
        questions_answers_btn.setText("问答");
        questions_answers_btn.setTextColor(getResources().getColor(R.color.color_292c31));
    }

    private void getQuestionsAnswers() {
        print_btn.setText("印迹");
        print_btn.setTextColor(getResources().getColor(R.color.color_292c31));
        questions_answers_btn.setText("问答");
        questions_answers_btn.setTextColor(getResources().getColor(R.color.color_0EB39B));
    }

}

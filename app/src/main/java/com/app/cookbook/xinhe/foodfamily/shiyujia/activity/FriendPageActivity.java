package com.app.cookbook.xinhe.foodfamily.shiyujia.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.BianJiActivity;
import com.app.cookbook.xinhe.foodfamily.main.GuanZhuActivity;
import com.app.cookbook.xinhe.foodfamily.main.GuanZhuTaActivity;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageEvent;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.ViewPagerAdapter;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.FriendPage;
import com.app.cookbook.xinhe.foodfamily.shiyujia.fragment.FriendCreationIssueFragment;
import com.app.cookbook.xinhe.foodfamily.shiyujia.fragment.FriendCreationPrintFragment;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.AppBarStateChangeListener;
import com.app.cookbook.xinhe.foodfamily.util.CircleImageView;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.StatusBarUtil;
import com.app.cookbook.xinhe.foodfamily.util.Utils_android_P;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.orhanobut.logger.Logger;
import com.tencent.stat.StatService;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import chuangyuan.ycj.videolibrary.video.VideoPlayerManager;
import jp.wasabeef.glide.transformations.BlurTransformation;
import rx.Subscription;

public class FriendPageActivity extends AppCompatActivity implements View.OnClickListener {

    protected Subscription subscription;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    //标题布局
    @BindView(R.id.title_layout)
    RelativeLayout title_layout;
    //返回
    @BindView(R.id.iamge_back)
    ImageView iamge_back;
    //标题
    @BindView(R.id.title_tv)
    TextView title_tv;
    //关注
    @BindView(R.id.attention_tv)
    TextView attention_tv;
    @BindView(R.id.attention_tv_off)
    TextView attention_tv_off;
    //用户头像
    @BindView(R.id.user_hand)
    CircleImageView user_hand;
    //性别
    @BindView(R.id.user_sex)
    ImageView user_sex;
    //编辑
    @BindView(R.id.compile_tv)
    TextView compile_tv;
    //关注按钮
    @BindView(R.id.user_attention)
    TextView user_attention;
    @BindView(R.id.user_attention_off)
    TextView user_attention_off;
    //用户名
    @BindView(R.id.user_name)
    TextView user_name;
    //用户职业
    @BindView(R.id.user_profession)
    TextView user_profession;
    //用户签名
    @BindView(R.id.user_signature)
    TextView user_signature;
    //用户关注布局
    @BindView(R.id.attention_btn)
    LinearLayout attention_btn;
    //用户别关注数
    @BindView(R.id.attention_number)
    TextView attention_number;
    //用户收藏布局
    @BindView(R.id.collect_btn)
    LinearLayout collect_btn;
    //用户被收藏数
    @BindView(R.id.collect_number)
    TextView collect_number;
    //用户粉丝布局
    @BindView(R.id.fans_btn)
    LinearLayout fans_btn;
    //用户粉丝
    @BindView(R.id.fans_number)
    TextView fans_number;
    //用户获赞布局
    @BindView(R.id.praise_btn)
    LinearLayout praise_btn;
    //用户获赞
    @BindView(R.id.praise_number)
    TextView praise_number;

    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.group)
    RadioGroup mGroup;

    @BindView(R.id.print_btn)
    RadioButton print_btn;

    @BindView(R.id.questions_answers_btn)
    RadioButton questions_answers_btn;

    @BindView(R.id.lin_layout)
    LinearLayout lin_layout;

    @BindView(R.id.mengban)
    LinearLayout mengban;

    @BindView(R.id.image_bg)
    LinearLayout image_bg;

    @BindView(R.id.lin1)
    LinearLayout lin1;

    @BindView(R.id.haha)
    LinearLayout haha;
    @BindView(R.id.back_btn)
    LinearLayout back_btn;
    @BindView(R.id.sanjiao_btn)
    ImageView sanjiao_btn;
    private String userId;
    //是否被關注
    private String relational;
    private String textType = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setchenjing();
        setContentView(R.layout.activity_friend_page);
        ButterKnife.bind(this);
        initView();
        //影藏虚拟键
        hideBottomUIMenu();
        setlayout(true);

        setupCollapsingToolbar();

    }

    private void setlayout(boolean is_show_bottom) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lin_layout.getLayoutParams();
        if (Utils_android_P.hasNotchResult(getApplicationContext())) {//如果有刘海
            if ("HUAWEI".equals(Build.BRAND)) {
                // lp.setMargins(0, 30 + Utils_android_P.set_add_height(), 0, 0);
                if (is_show_bottom) {
                    lp.setMargins(0, 40 + Utils_android_P.set_add_height(), 0, 0);
                } else {
                    lp.setMargins(0, 40 + Utils_android_P.set_add_height(), 0, 0);
                }
            } else {
                lp.setMargins(0, 40 + Utils_android_P.set_add_height(), 0, 0);
            }
        } else if ("Xiaomi".equals(Build.BRAND)) {//小米手机获取特定型号
            Log.e("小米8的型号", Build.MODEL);
            if (Build.MODEL.equals("MI 8")) {
                lp.setMargins(0, 140, 0, 0);
            } else {
                lp.setMargins(0, 0, 0, 0);
            }
        } else {
            if ("HUAWEI".equals(Build.BRAND)) {
                if (is_show_bottom) {
                    Log.e("进来了", "进来了");
                    lp.setMargins(0, 0, 0, 0);
                } else {
                    lp.setMargins(0, 0, 0, 0);
                }
            } else {
                lp.setMargins(0, 0, 0, 0);
            }
        }
        lin_layout.setLayoutParams(lp);
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        getWindow().setAttributes(params);
    }

    private void setchenjing() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(FriendPageActivity.this, R.color.white);
        } else {
            StatusBarUtil.transparencyBar(FriendPageActivity.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestUserMsg(userId);
        //影藏虚拟键
        hideBottomUIMenu();
    }

    private void initView() {
        userId = getIntent().getStringExtra("UserId");
        mGroup.setOnCheckedChangeListener(new CheckedChangeListener());
        mGroup.check(R.id.print_btn);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FriendCreationPrintFragment(), "印迹");
        adapter.addFrag(new FriendCreationIssueFragment(), "回答");
        viewpager.setAdapter(adapter);
        /**Tab
         * */
        viewpager.setOnPageChangeListener(new PageChangeListener());
        viewpager.setOffscreenPageLimit(2);

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    lin1.setVisibility(View.VISIBLE);
                    haha.setVisibility(View.GONE);
                    //展开状态
                    title_tv.setText("");
                    iamge_back.setImageResource(R.drawable.back_image);
//                    title_layout.setBackgroundColor(getResources().getColor(R.color.color_00000000));
                    compile_tv.setTextColor(Color.parseColor("#000000"));
                    image_bg.setVisibility(View.GONE);
                    mengban.setVisibility(View.GONE);
                } else if (state == State.COLLAPSED) {
                    lin1.setVisibility(View.GONE);
                    haha.setVisibility(View.VISIBLE);
                    //折叠状态
                    title_tv.setText(userName);
                    iamge_back.setImageResource(R.drawable.icon_details_back);
                    compile_tv.setTextColor(Color.parseColor("#ffffff"));
                    image_bg.setVisibility(View.VISIBLE);
                    mengban.setVisibility(View.VISIBLE);
                } else {
                    lin1.setVisibility(View.GONE);
                    haha.setVisibility(View.GONE);
                    //中间状态
                    title_tv.setText("");
                    iamge_back.setImageResource(R.drawable.back_image);
//                    title_layout.setBackgroundColor(getResources().getColor(R.color.color_00000000));
                    image_bg.setVisibility(View.GONE);
                    mengban.setVisibility(View.GONE);
                }
            }
        });

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    getQuestionsAnswers();
                    VideoPlayerManager.getInstance().onPause(true);
                } else {
                    getPrint();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        back_btn.setOnClickListener(this);
        compile_tv.setOnClickListener(this);
        print_btn.setOnClickListener(this);
        questions_answers_btn.setOnClickListener(this);
        attention_btn.setOnClickListener(this);
        collect_btn.setOnClickListener(this);
        fans_btn.setOnClickListener(this);
        praise_btn.setOnClickListener(this);
        user_attention.setOnClickListener(this);
        user_attention_off.setOnClickListener(this);
        attention_tv_off.setOnClickListener(this);
        attention_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.attention_btn:
                intent = new Intent(FriendPageActivity.this, FriendAttentionActivity.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
                break;
            case R.id.collect_btn:
                intent = new Intent(FriendPageActivity.this, FriendCollectActivity.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
                break;
            case R.id.fans_btn:
                intent = new Intent(FriendPageActivity.this, GuanZhuTaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id", userId);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.praise_btn:

                break;
            case R.id.compile_tv:
                Properties prop = new Properties();
                prop.setProperty("name", "top_rel");
                StatService.trackCustomKVEvent(FriendPageActivity.this, "Myself_edite_personal_data", prop);
                if ("".equals(SharedPreferencesHelper.get(FriendPageActivity.this, "login_token", ""))) {
                    intent = new Intent(FriendPageActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(FriendPageActivity.this, BianJiActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.print_btn:
                sanjiao_btn.setVisibility(View.INVISIBLE);

                viewpager.setCurrentItem(0);
                getPrint();
                break;
            case R.id.questions_answers_btn:
                sanjiao_btn.setVisibility(View.VISIBLE);

                String str = questions_answers_btn.getText().toString();
                if (viewpager.getCurrentItem() == 1) {
                    if ("回答".equals(str)) {
                        questions_answers_btn.setText("提问");
                        questions_answers_btn.setTextColor(getResources().getColor(R.color.color_0EB39B));
                        EventBus.getDefault().post(new MessageEvent("203"));
                        textType = "1";
                    } else if ("提问".equals(str)) {
                        questions_answers_btn.setText("回答");
                        questions_answers_btn.setTextColor(getResources().getColor(R.color.color_0EB39B));
                        EventBus.getDefault().post(new MessageEvent("204"));
                        textType = "2";
                    }
                } else {
                    viewpager.setCurrentItem(1);
                    getQuestionsAnswers();
                }
                break;
            case R.id.user_attention:
                if ("".equals(SharedPreferencesHelper.get(FriendPageActivity.this, "login_token", ""))) {
                    intent = new Intent(FriendPageActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    guanzhu_submit();
                }
                break;
            case R.id.user_attention_off:
                if ("".equals(SharedPreferencesHelper.get(FriendPageActivity.this, "login_token", ""))) {
                    intent = new Intent(FriendPageActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    quxiao_guanzhu_submit();
                }
                break;
            case R.id.attention_tv:
                if ("".equals(SharedPreferencesHelper.get(FriendPageActivity.this, "login_token", ""))) {
                    intent = new Intent(FriendPageActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    guanzhu_submit();
                }
                break;
            case R.id.attention_tv_off:
                if ("".equals(SharedPreferencesHelper.get(FriendPageActivity.this, "login_token", ""))) {
                    intent = new Intent(FriendPageActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    quxiao_guanzhu_submit();
                }
                break;
        }
    }


    private String handPath;
    private String userName;


    private void requestUserMsg(String user_id) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        Log.e("获取好友信息参数：", params.toString());
        subscription = Network.getInstance("获取好友信息", FriendPageActivity.this)
                .getOtherInformation(params,
                        new ProgressSubscriberNew<>(FriendPage.class, new GsonSubscriberOnNextListener<FriendPage>() {
                            @Override
                            public void on_post_entity(FriendPage result) {
                                relational = result.getRelational();
                                handPath = result.getAvatar();
                                userName = result.getName();
//                                Log.e("123", "      ------>   " + result.getId() + "     " + userId +
//                                        "      " + SharedPreferencesHelper.get(FriendPageActivity.this, "userId", "") + "        " + relational);
                                Glide.with(FriendPageActivity.this).load(result.getAvatar())
                                        .bitmapTransform(new BlurTransformation(FriendPageActivity.this, 60)
                                                , new CenterCrop(FriendPageActivity.this))
                                        .into(new ViewTarget<View, GlideDrawable>(image_bg) {
                                            //括号里为需要加载的控件
                                            @Override
                                            public void onResourceReady(GlideDrawable resource,
                                                                        GlideAnimation<? super GlideDrawable> glideAnimation) {
                                                this.view.setBackground(resource.getCurrent());
                                            }
                                        });

                                if (result.getId().equals(SharedPreferencesHelper.get(FriendPageActivity.this, "userId", ""))) {
//                                    Log.e("123", "      +++++------->     " + relational);
                                    user_attention.setVisibility(View.GONE);
                                    user_attention_off.setVisibility(View.GONE);
                                    attention_tv.setVisibility(View.GONE);
                                    attention_tv_off.setVisibility(View.GONE);
                                } else {
//                                    Log.e("123", "      ++++++++++>     " + relational);
                                    if ("1".equals(relational)) {
                                        user_attention.setVisibility(View.GONE);
                                        user_attention_off.setVisibility(View.VISIBLE);
                                        attention_tv.setVisibility(View.GONE);
                                        attention_tv_off.setVisibility(View.VISIBLE);
                                    } else if ("2".equals(relational)) {
                                        user_attention.setVisibility(View.VISIBLE);
                                        user_attention_off.setVisibility(View.GONE);
                                        attention_tv.setVisibility(View.VISIBLE);
                                        attention_tv_off.setVisibility(View.GONE);
                                    }
                                }

                                Glide.with(FriendPageActivity.this)
                                        .load(result.getAvatar())
                                        .error(R.drawable.touxiang)
                                        .into(user_hand);


                                if ("1".equals(result.getSex())) {
                                    user_sex.setImageResource(R.drawable.xingbie_nan);
                                } else if ("2".equals(result.getSex())) {
                                    user_sex.setImageResource(R.drawable.xingbie_nv);
                                }

                                user_name.setText(result.getName());
                                user_profession.setText(result.getProfession());
                                user_signature.setText(result.getPersonal_signature());
                                attention_number.setText(result.getFollow_count());
                                collect_number.setText(result.getCollect_count());
                                fans_number.setText(result.getFans());
                                praise_number.setText(result.getThumb_count());

                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "获取好友信息报错：" + error);
                            }
                        }, FriendPageActivity.this, false));
    }

    private void quxiao_guanzhu_submit() {
        Map<String, String> params = new HashMap<>();
        params.put("uuid", userId);
        Log.e("取消关注他：", params.toString());
        subscription = Network.getInstance("取消关注他", this)
                .quxiaoguanzhuta(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Logger.e("取消关注他成功：" + result.getCode());
                                //切换布局
                                user_attention.setVisibility(View.VISIBLE);
                                attention_tv.setVisibility(View.VISIBLE);
                                user_attention_off.setVisibility(View.GONE);
                                attention_tv_off.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(String error) {
                                Logger.e("取消关注他报错：" + error);
                            }
                        }, this, false));
    }

    private void guanzhu_submit() {
        Map<String, String> params = new HashMap<>();
        params.put("uuid", userId);
        Log.e("关注他：", params.toString());
        subscription = Network.getInstance("关注他", this)
                .guanzhuta(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Logger.e("关注他成功：" + result.getCode());
                                //切换布局
                                user_attention.setVisibility(View.GONE);
                                attention_tv.setVisibility(View.GONE);
                                user_attention_off.setVisibility(View.VISIBLE);
                                attention_tv_off.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Logger.e("关注他报错：" + error);
                            }
                        }, this, false));
    }

    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(FriendPageActivity.this, R.color.white);
        } else {
            StatusBarUtil.transparencyBar(FriendPageActivity.this);
        }
    }

    private void setupCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse_toolbar);
        collapsingToolbar.setTitleEnabled(false);
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


    private void getPrint() {
        print_btn.setText("印迹");
        print_btn.setTextColor(getResources().getColor(R.color.color_0EB39B));
        if ("1".equals(textType)) {
            questions_answers_btn.setText("提问");
            questions_answers_btn.setTextColor(getResources().getColor(R.color.color_292c31));
        } else if ("2".equals(textType)) {
            questions_answers_btn.setText("回答");
            questions_answers_btn.setTextColor(getResources().getColor(R.color.color_292c31));
        } else if ("0".equals(textType)) {
            questions_answers_btn.setText("回答");
            questions_answers_btn.setTextColor(getResources().getColor(R.color.color_292c31));
        }
    }

    private void getQuestionsAnswers() {
        print_btn.setText("印迹");
        print_btn.setTextColor(getResources().getColor(R.color.color_292c31));
        if ("1".equals(textType)) {
            questions_answers_btn.setText("提问");
            questions_answers_btn.setTextColor(getResources().getColor(R.color.color_0EB39B));
        } else if ("2".equals(textType)) {
            questions_answers_btn.setText("回答");
            questions_answers_btn.setTextColor(getResources().getColor(R.color.color_0EB39B));
        } else if ("0".equals(textType)) {
            questions_answers_btn.setText("回答");
            questions_answers_btn.setTextColor(getResources().getColor(R.color.color_0EB39B));
        }
    }

}

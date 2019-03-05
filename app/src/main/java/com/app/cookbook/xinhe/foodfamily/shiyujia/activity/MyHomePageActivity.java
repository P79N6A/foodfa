package com.app.cookbook.xinhe.foodfamily.shiyujia.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
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

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.BianJiActivity;
import com.app.cookbook.xinhe.foodfamily.main.GuanZhuActivity;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageEvent;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.ViewPagerAdapter;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.FriendPage;
import com.app.cookbook.xinhe.foodfamily.shiyujia.fragment.MyPrintFragment;
import com.app.cookbook.xinhe.foodfamily.shiyujia.fragment.MyQuestionsAnswersFragment;
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

public class MyHomePageActivity extends AppCompatActivity implements View.OnClickListener {

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

    @BindView(R.id.lin1)
    LinearLayout lin1;

    @BindView(R.id.haha)
    LinearLayout haha;

    @BindView(R.id.back_btn)
    LinearLayout back_btn;

    @BindView(R.id.mengban)
    LinearLayout mengban;

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
        setContentView(R.layout.activity_my_home_page);
        ButterKnife.bind(this);
        initView();
        //影藏虚拟键
        hideBottomUIMenu();
        setlayout(true);
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

    private void setlayout(boolean is_show_bottom) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lin_layout.getLayoutParams();
        if (Utils_android_P.hasNotchResult(getApplicationContext())) {//如果有刘海
            if ("HUAWEI".equals(android.os.Build.BRAND)) {
                // lp.setMargins(0, 30 + Utils_android_P.set_add_height(), 0, 0);
                if (is_show_bottom) {
                    lp.setMargins(0, 40 + Utils_android_P.set_add_height(), 0, 0);
                } else {
                    lp.setMargins(0, 40 + Utils_android_P.set_add_height(), 0, 0);
                }
            } else {
                lp.setMargins(0, 40 + Utils_android_P.set_add_height(), 0, 0);
            }
        } else if ("Xiaomi".equals(android.os.Build.BRAND)) {//小米手机获取特定型号
            Log.e("小米8的型号", Build.MODEL);
            if (Build.MODEL.equals("MI 8")) {
                lp.setMargins(0, 140, 0, 0);
            } else {
                lp.setMargins(0, 0, 0, 0);
            }
        } else {
            if ("HUAWEI".equals(android.os.Build.BRAND)) {
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


    @Override
    protected void onResume() {
        super.onResume();
        //添加虚拟键
        hideBottomUIMenu();
        requestUserMsg(userId);
    }

    private boolean issend;

    private void initView() {
        userId = getIntent().getStringExtra("UserId");
        mGroup.setOnCheckedChangeListener(new CheckedChangeListener());
        mGroup.check(R.id.print_btn);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MyPrintFragment(), "印迹");
        adapter.addFrag(new MyQuestionsAnswersFragment(), "回答");
        viewpager.setAdapter(adapter);
        /**Tab
         * */
        viewpager.setOnPageChangeListener(new PageChangeListener());
        viewpager.setOffscreenPageLimit(2);

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    Log.e("进来了", "展开");
                    lin1.setVisibility(View.VISIBLE);
                    haha.setVisibility(View.GONE);
//                    sendMessageToFrame(2);
                    //展开状态
                    title_tv.setText("");
                    attention_tv.setVisibility(View.GONE);
                    iamge_back.setImageResource(R.drawable.back_image);
                    title_layout.setBackgroundColor(getResources().getColor(R.color.color_00000000));
                    compile_tv.setTextColor(Color.parseColor("#000000"));
                    mengban.setVisibility(View.GONE);

                } else if (state == State.COLLAPSED) {
                    Log.e("进来了", "折叠");
                    lin1.setVisibility(View.GONE);
                    haha.setVisibility(View.VISIBLE);
//                    sendMessageToFrame(3);
                    //折叠状态
                    title_tv.setText(userName);
                    compile_tv.setTextColor(Color.parseColor("#ffffff"));
                    attention_tv.setVisibility(View.GONE);
                    iamge_back.setImageResource(R.drawable.icon_details_back);
                    Glide.with(MyHomePageActivity.this).load(handPath)
                            .bitmapTransform(new BlurTransformation(MyHomePageActivity.this, 60)
                                    , new CenterCrop(MyHomePageActivity.this))
                            .into(new ViewTarget<View, GlideDrawable>(title_layout) {
                                //括号里为需要加载的控件
                                @Override
                                public void onResourceReady(GlideDrawable resource,
                                                            GlideAnimation<? super GlideDrawable> glideAnimation) {
                                    this.view.setBackground(resource.getCurrent());
                                }
                            });
                    mengban.setVisibility(View.VISIBLE);
                } else {
                    lin1.setVisibility(View.GONE);
                    haha.setVisibility(View.GONE);
//                    sendMessageToFrame(3);
                    //中间状态
                    title_tv.setText("");
                    iamge_back.setImageResource(R.drawable.back_image);
                    attention_tv.setVisibility(View.GONE);
                    title_layout.setBackgroundColor(getResources().getColor(R.color.color_00000000));
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
    }

//    private void sendMessageToFrame(int what) {
//        MePrintFragment fragment = null;
//        List<Fragment> list = this.getSupportFragmentManager().getFragments();
//        for (Fragment fment : list) {
//            if (fment instanceof MePrintFragment) {
//                fragment = (MePrintFragment) fment;
//            }
//        }
//        Handler handler = fragment.getHandler();
//        handler.sendEmptyMessage(what);
//    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.attention_btn:
                intent = new Intent(MyHomePageActivity.this, MyAttentionActivity.class);
                startActivity(intent);
                break;
            case R.id.collect_btn:
                intent = new Intent(MyHomePageActivity.this, MyCollectActivity.class);
                startActivity(intent);
                break;
            case R.id.fans_btn:
                intent = new Intent(MyHomePageActivity.this, GuanZhuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("flag", "1");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.praise_btn:

                break;
            case R.id.compile_tv:
                Properties prop = new Properties();
                prop.setProperty("name", "top_rel");
                StatService.trackCustomKVEvent(MyHomePageActivity.this, "Myself_edite_personal_data", prop);
                if ("".equals(SharedPreferencesHelper.get(MyHomePageActivity.this, "login_token", ""))) {
                    intent = new Intent(MyHomePageActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(MyHomePageActivity.this, BianJiActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.print_btn:
                viewpager.setCurrentItem(0);
                sanjiao_btn.setVisibility(View.INVISIBLE);
                getPrint();
                break;
            case R.id.questions_answers_btn:
                sanjiao_btn.setVisibility(View.VISIBLE);
                String str = questions_answers_btn.getText().toString();
                if (viewpager.getCurrentItem() == 1) {
                    if ("回答".equals(str)) {
                        questions_answers_btn.setText("提问");
                        questions_answers_btn.setTextColor(getResources().getColor(R.color.color_0EB39B));
                        EventBus.getDefault().post(new MessageEvent("101"));
                        textType = "1";
                    } else if ("提问".equals(str)) {
                        questions_answers_btn.setText("回答");
                        questions_answers_btn.setTextColor(getResources().getColor(R.color.color_0EB39B));
                        EventBus.getDefault().post(new MessageEvent("102"));
                        textType = "2";
                    }
                } else {
                    viewpager.setCurrentItem(1);
                    getQuestionsAnswers();
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
        subscription = Network.getInstance("获取好友信息", MyHomePageActivity.this)
                .getOtherInformation(params,
                        new ProgressSubscriberNew<>(FriendPage.class, new GsonSubscriberOnNextListener<FriendPage>() {
                            @Override
                            public void on_post_entity(FriendPage result) {
                                relational = result.getRelational();
                                handPath = result.getAvatar();
                                userName = result.getName();
                                if ("1".equals(relational)) {
                                    user_attention.setText("已关注");
                                } else if ("2".equals(relational)) {
                                    user_attention.setText("关注");
                                }

                                Glide.with(MyHomePageActivity.this)
                                        .load(result.getAvatar())
                                        .error(R.drawable.touxiang)
                                        .into(user_hand);
                                if ("1".equals(result.getSex())) {
                                    user_sex.setImageResource(R.drawable.xingbie_nan);
                                } else if ("2".equals(result.getSex())) {
                                    user_sex.setImageResource(R.drawable.xingbie_nv);
                                }

                                if (userId.equals(result.getId())) {
                                    user_attention.setVisibility(View.GONE);
                                } else {
                                    user_attention.setVisibility(View.VISIBLE);
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
                        }, MyHomePageActivity.this, false));
    }


    private void setchenjing() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            getWindow().getDecorView().setSystemUiVisibility(option);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().setStatusBarColor(Color.parseColor("#00000000"));
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(MyHomePageActivity.this, R.color.transparent);
        } else {
            StatusBarUtil.transparencyBar(MyHomePageActivity.this);
        }
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

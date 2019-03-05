package com.app.cookbook.xinhe.foodfamily.shiyujia.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.GuanZhuActivity;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.main.WenTiActivity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.MyAttentionActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.MyCollectActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.MyCreationAvtivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.MyDraftsActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.MyFollowerActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.PersonalHomepageActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.SettingActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.MyMsg;
import com.app.cookbook.xinhe.foodfamily.util.CircleImageView;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.ui.ViewPagerFragment;
import com.bumptech.glide.Glide;
import com.tencent.stat.StatService;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

public class MyFragment extends ViewPagerFragment implements View.OnClickListener {


    private View view;
    /**
     * Rxjava
     */
    protected Subscription subscription;
    //用户信息布局
    @BindView(R.id.my_msg_layout)
    RelativeLayout my_msg_layout;
    //头像
    @BindView(R.id.hand_layout)
    CircleImageView hand_layout;
    //性别
    @BindView(R.id.xingbie_image)
    ImageView xingbie_image;
    //用户名
    @BindView(R.id.user_name)
    TextView user_name;
    //职业
    @BindView(R.id.zhiye_tv)
    TextView zhiye_tv;
    //签名
    @BindView(R.id.qianming_tv)
    TextView qianming_tv;
    //进入个人主页
    @BindView(R.id.personal_homepage_tv)
    TextView personal_homepage_tv;
    //关注布局
    @BindView(R.id.attention_btn)
    LinearLayout attention_btn;
    //关注数
    @BindView(R.id.attention_number)
    TextView attention_number;
    //粉丝布局
    @BindView(R.id.fans_btn)
    LinearLayout fans_btn;
    //粉丝
    @BindView(R.id.fans_number)
    TextView fans_number;
    //获赞布局
    @BindView(R.id.likenum_btn)
    LinearLayout likenum_btn;
    //获赞
    @BindView(R.id.likenum_number)
    TextView likenum_number;
    //我的创作
    @BindView(R.id.my_creation_layout)
    RelativeLayout my_creation_layout;
    //我的创作数
    @BindView(R.id.my_creation_number)
    TextView my_creation_number;
    //我的关注
    @BindView(R.id.my_attention_layout)
    RelativeLayout my_attention_layout;
    //我的关注数
    @BindView(R.id.my_attention_number)
    TextView my_attention_number;
    //我的收藏
    @BindView(R.id.my_collect_layout)
    RelativeLayout my_collect_layout;
    //我的收藏数
    @BindView(R.id.my_collect_number)
    TextView my_collect_number;
    //草稿箱
    @BindView(R.id.my_drafts_layout)
    RelativeLayout my_drafts_layout;
    //草稿箱数
    @BindView(R.id.my_drafts_number)
    TextView my_drafts_number;
    //用户反馈
    @BindView(R.id.my_feedback_layout)
    RelativeLayout my_feedback_layout;
    //设置
    @BindView(R.id.my_setting_layout)
    RelativeLayout my_setting_layout;

    private String userId;
    private String is_wifi;


    public static MyFragment myInstance(String title) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString("home_fragment_title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.my_page_fragment, container, false);
            ButterKnife.bind(this, view);//绑定黄牛刀
            initView();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
            user_name.setText("");
            zhiye_tv.setText("未知");
            qianming_tv.setText("懒懒路人");
            hand_layout.setImageResource(R.drawable.touxiang);
            //设置性别
            xingbie_image.setVisibility(View.GONE);
            //关注
            attention_number.setText("0");
            //粉絲
            fans_number.setText("0");
            //获赞
            likenum_number.setText("0");
        } else {
            requestUserMsg();
        }
    }

    private void initView() {
        my_msg_layout.setOnClickListener(this);
        personal_homepage_tv.setOnClickListener(this);
        attention_btn.setOnClickListener(this);
        fans_btn.setOnClickListener(this);
        likenum_btn.setOnClickListener(this);
        my_creation_layout.setOnClickListener(this);
        my_attention_layout.setOnClickListener(this);
        my_collect_layout.setOnClickListener(this);
        my_drafts_layout.setOnClickListener(this);
        my_feedback_layout.setOnClickListener(this);
        my_setting_layout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Properties prop;
        switch (v.getId()) {
            //个人主页
            case R.id.my_msg_layout:
                if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), PersonalHomepageActivity.class);
                    intent.putExtra("UserId", userId);
                    startActivity(intent);
                }
                break;
            //个人主页
            case R.id.personal_homepage_tv:
                if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), PersonalHomepageActivity.class);
                    intent.putExtra("UserId", userId);
                    startActivity(intent);
                }
                break;
            //关注我的人
            case R.id.attention_btn:
                if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), MyFollowerActivity.class);
                    startActivity(intent);
                }
                break;
            //粉丝
            case R.id.fans_btn:
                if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    prop = new Properties();
                    prop.setProperty("name", "guanzhuwo_btn");
                    StatService.trackCustomKVEvent(getActivity(), "Myself_user_pay_attention", prop);
                    intent = new Intent(getActivity(), GuanZhuActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("flag", "1");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.likenum_btn:

                break;
            //我的创作
            case R.id.my_creation_layout:
                if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), MyCreationAvtivity.class);
                    intent.putExtra("UserId", userId);
                    startActivity(intent);
                }
                break;
            //我的关注
            case R.id.my_attention_layout:
                if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), MyAttentionActivity.class);
                    startActivity(intent);
                }
                break;
            //我的收藏
            case R.id.my_collect_layout:
                if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), MyCollectActivity.class);
                    startActivity(intent);
                }
                break;
            //草稿箱
            case R.id.my_drafts_layout:
                if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), MyDraftsActivity.class);
                    startActivity(intent);
                }
                break;
            //用户反馈
            case R.id.my_feedback_layout:
                if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    prop = new Properties();
                    prop.setProperty("name", "wentifankui_layout");
                    StatService.trackCustomKVEvent(getActivity(), "Install_Install_user_feedback", prop);
                    intent = new Intent(getActivity(), WenTiActivity.class);
                    startActivity(intent);
                }
                break;
            //用户设置
            case R.id.my_setting_layout:
                if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    prop = new Properties();
                    prop.setProperty("name", "setting_layout");
                    StatService.trackCustomKVEvent(getActivity(), "Install", prop);
                    intent = new Intent(getActivity(), SettingActivity.class);
                    intent.putExtra("is_wifi", is_wifi);
                    startActivity(intent);
                }
                break;
        }
    }


    private void requestUserMsg() {
        Map<String, String> params = new HashMap<>();
        Log.e("获取用户信息参数：", params.toString());
        subscription = Network.getInstance("获取用户信息", getActivity())
                .getOwnInformation(
                        new ProgressSubscriberNew<>(MyMsg.class, new GsonSubscriberOnNextListener<MyMsg>() {
                            @Override
                            public void on_post_entity(MyMsg userEntity) {
                                Log.e("123", "获取用户信息成功：" + userEntity.getName());
                                userId = userEntity.getId();
                                is_wifi = userEntity.getIs_wifi();
                                SharedPreferencesHelper.put(getActivity(), "isWiFI", is_wifi);
                                //设置页面的信息
                                SharedPreferencesHelper.put(getActivity(), "user_id", userEntity.getId() + "");
                                SharedPreferencesHelper.put(getActivity(), "userId", userEntity.getId() + "");
                                Contacts.weixin_login = userEntity.getWeixin_login();
//                                Contacts.weixin_name = userEntity.getWechat_name();

                                Contacts.weibo_login = userEntity.getWeibo_login();
//                                Contacts.weibo_name = userEntity.getWeibo_name();

                                //对用户ID进行缓存
                                SharedPreferencesHelper.put(getActivity(), "tel_phone", userEntity.getTel() + "");
                                SharedPreferencesHelper.put(getActivity(), "user_name", userEntity.getName() + "");

                                set_user_information(userEntity);
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "获取用户信息报错：" + error);
                            }
                        }, getActivity(), false));
    }

    private void set_user_information(MyMsg data) {
        if (null != data.getName() && !TextUtils.isEmpty(data.getName())) {
            user_name.setText(data.getName());
        } else {
            user_name.setText("");
        }
        if (null != data.getProfession() && !TextUtils.isEmpty(data.getProfession())) {
            zhiye_tv.setText(data.getProfession());
        } else {
            zhiye_tv.setText("未知");
        }
        if (null != data.getPersonal_signature() && !TextUtils.isEmpty(data.getPersonal_signature())) {
            qianming_tv.setText(data.getPersonal_signature());
        } else {
            qianming_tv.setText("懒懒路人");
        }
        if (null != data.getAvatar() && !TextUtils.isEmpty(data.getAvatar())) {
            //设置头像
            Glide.with(getActivity()).load(data.getAvatar())
                    .error(R.drawable.touxiang)
                    .into(hand_layout);
        } else {
            hand_layout.setImageResource(R.drawable.touxiang);
        }
        //设置性别
        xingbie_image.setVisibility(View.VISIBLE);
        if ("1".equals(data.getSex())) {//男
            xingbie_image.setImageResource(R.drawable.xingbie_nan);
        } else if ("2".equals(data.getSex())) {
            xingbie_image.setImageResource(R.drawable.xingbie_nv);
        }

        //关注
        if ("0".equals(data.getFollow_users())) {
            attention_number.setText("0");
        } else {
            attention_number.setText(data.getFollow_users());
        }
        //粉絲
        if ("0".equals(data.getFollowed_users())) {
            fans_number.setText("0");
        } else {
            fans_number.setText(data.getFollowed_users());
        }
        //获赞
        if ("0".equals(data.getThumb_count())) {
            likenum_number.setText("0");
        } else {
            likenum_number.setText(data.getThumb_count());
        }
        //对用户ID进行缓存
        SharedPreferencesHelper.put(getActivity(), "tel_phone", data.getTel() + "");
        SharedPreferencesHelper.put(getActivity(), "user_name", data.getName() + "");
    }


    @Override
    public void fetchData() {

    }


}

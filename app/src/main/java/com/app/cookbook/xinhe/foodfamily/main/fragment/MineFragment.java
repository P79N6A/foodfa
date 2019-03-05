package com.app.cookbook.xinhe.foodfamily.main.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.app.cookbook.xinhe.foodfamily.main.BianJiActivity;
import com.app.cookbook.xinhe.foodfamily.main.DraftActivity;
import com.app.cookbook.xinhe.foodfamily.main.GuanZhuActivity;
import com.app.cookbook.xinhe.foodfamily.main.GuanZhuBiaoQianActivity;
import com.app.cookbook.xinhe.foodfamily.main.GuanZhuQuestionActivity;
import com.app.cookbook.xinhe.foodfamily.main.HuiDaActivity;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.main.MyShouCangActivity;
import com.app.cookbook.xinhe.foodfamily.main.WoDeTiWenActivity;
import com.app.cookbook.xinhe.foodfamily.main.WoGuanZhuActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.UserEntity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
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
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscription;


public class MineFragment extends ViewPagerFragment {
    /**
     * Rxjava
     */
    protected Subscription subscription;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.zhiye_tv)
    TextView zhiye_tv;
    @BindView(R.id.qianming_tv)
    TextView qianming_tv;
    @BindView(R.id.woguanzhu_btn)
    LinearLayout woguanzhu_btn;
    @BindView(R.id.guanzhuwo_btn)
    LinearLayout guanzhuwo_btn;
    @BindView(R.id.guanzhubiaoqian_btn)
    LinearLayout guanzhubiaoqian_btn;
    @BindView(R.id.wodetiwen_btn)
    RelativeLayout wodetiwen_btn;
    @BindView(R.id.huida_layout)
    RelativeLayout huida_layout;
    @BindView(R.id.guanzhu_layout)
    RelativeLayout guanzhu_layout;
    @BindView(R.id.shoucang_layout)
    RelativeLayout shoucang_layout;
    @BindView(R.id.touxiang_image)
    CircleImageView touxiang_image;
    @BindView(R.id.biaoqian_number)
    TextView biaoqian_number;
    @BindView(R.id.guanzhu_people_number)
    TextView guanzhu_people_number;
    @BindView(R.id.guanzhuwo_number)
    TextView guanzhuwo_number;
    @BindView(R.id.tiwen_number)
    TextView tiwen_number;
    @BindView(R.id.answer_numbers)
    TextView answer_numbers;
    @BindView(R.id.guanzhu_question)
    TextView guanzhu_question;
    @BindView(R.id.my_shoucang_number)
    TextView my_shoucang_number;
    @BindView(R.id.xingbie_image)
    ImageView xingbie_image;
    @BindView(R.id.setting_btn)
    ImageView setting_btn;
    @BindView(R.id.top_rel)
    RelativeLayout top_rel;
    @BindView(R.id.top_layout)
    LinearLayout top_layout;
    @BindView(R.id.mes_Layout)
    LinearLayout mes_Layout;
    @BindView(R.id.setting_layout)
    LinearLayout setting_layout;
    //草稿箱
    @BindView(R.id.select_caogao)
    TextView select_caogao;

    @BindView(R.id.setting_btn_layout)
    RelativeLayout setting_btn_layout;

    View view;

    public static MineFragment newInstance(String title) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString("home_fragment_title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.e("进来了我的模块","进来了我的模块");
        if (view == null) {
            view = inflater.inflate(R.layout.geren_zhongxin_fragement, null);
            ButterKnife.bind(this, view);//绑定黄牛刀
            //页面跳转逻辑
            business_method();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //获取用户个人资料
        if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
            user_name.setText("");
            zhiye_tv.setText("未知");
            qianming_tv.setText("懒懒路人");
            xingbie_image.setVisibility(View.GONE);
            biaoqian_number.setText("0");
            guanzhu_people_number.setText("0");
            guanzhuwo_number.setText("0");
            tiwen_number.setText("0");
            answer_numbers.setText("0");
            guanzhu_question.setText("0");
            my_shoucang_number.setText("0");
            //设置头像
            Glide.with(getActivity()).load(R.drawable.touxiang)
                    .error(R.drawable.touxiang)
                    .into(touxiang_image);
        } else {
            get_user_information();
        }
    }


    private void business_method() {
        //设置页
        setting_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Properties prop = new Properties();
                prop.setProperty("name", "setting_layout");
                StatService.trackCustomKVEvent(getActivity(), "Install", prop);
                if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {

                }
            }
        });
        //昵称点击
        top_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Properties prop = new Properties();
                prop.setProperty("name", "top_rel");
                StatService.trackCustomKVEvent(getActivity(), "Myself_edite_personal_data", prop);
                if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), BianJiActivity.class);
                    startActivity(intent);
                }
            }
        });
        //草稿箱
        select_caogao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "select_caogao");
                StatService.trackCustomKVEvent(getActivity(), "Install_drafts", prop);
                if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), DraftActivity.class);
                    startActivity(intent);
                }
            }
        });
        //我关注的人
        woguanzhu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "woguanzhu_btn");
                StatService.trackCustomKVEvent(getActivity(), "Myself_pay_attention_user", prop);
                if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), WoGuanZhuActivity.class);
                    startActivity(intent);
                }
            }
        });
        //关注我的人
        guanzhuwo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "guanzhuwo_btn");
                StatService.trackCustomKVEvent(getActivity(), "Myself_user_pay_attention", prop);
                if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), GuanZhuActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("flag", "1");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        //关注的领域
        guanzhubiaoqian_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "guanzhubiaoqian_btn");
                StatService.trackCustomKVEvent(getActivity(), "Myself_pay_attention_tag", prop);
                if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), GuanZhuBiaoQianActivity.class);
                    startActivity(intent);
                }
            }
        });
        //我的提问
        wodetiwen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "wodetiwen_btn");
                StatService.trackCustomKVEvent(getActivity(), "Myself_question", prop);
                if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), WoDeTiWenActivity.class);
                    startActivity(intent);
                }
            }
        });
        //我的回答
        huida_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "huida_layout");
                StatService.trackCustomKVEvent(getActivity(), "Myself_answer", prop);
                if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), HuiDaActivity.class);
                    startActivity(intent);
                }
            }
        });
        //关注的问题
        guanzhu_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "guanzhu_layout");
                StatService.trackCustomKVEvent(getActivity(), "Myself_pay_attention_problem", prop);
                if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), GuanZhuQuestionActivity.class);
                    startActivity(intent);
                }
            }
        });
        //我的收藏
        shoucang_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "shoucang_layout");
                StatService.trackCustomKVEvent(getActivity(), "Myself_collection", prop);
                if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), MyShouCangActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    private void get_user_information() {
        Map<String, String> params = new HashMap<>();
        Log.e("获取用户信息参数：", params.toString());
        subscription = Network.getInstance("获取用户信息", getActivity())
                .get_user_information(params,
                        new ProgressSubscriberNew<>(UserEntity.class, new GsonSubscriberOnNextListener<UserEntity>() {
                            @Override
                            public void on_post_entity(UserEntity userEntity) {
                                Log.e("123", "获取用户信息成功：" + userEntity.getName());
                                //设置页面的信息
                                SharedPreferencesHelper.put(getActivity(), "userId", userEntity.getUid() + "");
                                Contacts.weixin_login = userEntity.getWeixin_login();
                                Contacts.weixin_name = userEntity.getWechat_name();

                                Contacts.weibo_login = userEntity.getWeibo_login();
                                Contacts.weibo_name = userEntity.getWeibo_name();

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

    private void set_user_information(UserEntity data) {
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
                    .into(touxiang_image);
        } else {
            touxiang_image.setImageResource(R.drawable.touxiang);
        }
        //设置性别
        xingbie_image.setVisibility(View.VISIBLE);
        if (data.getSex() == 1) {//男
            xingbie_image.setImageResource(R.drawable.xingbie_nan);
        } else if (data.getSex() == 2) {
            xingbie_image.setImageResource(R.drawable.xingbie_nv);
        }

        if (data.getFollow_class() == 0) {
            biaoqian_number.setText("0");
        } else {
            biaoqian_number.setText(data.getFollow_class() + "");
        }
        if (data.getFollow_users() == 0) {
            guanzhu_people_number.setText("0");
        } else {
            guanzhu_people_number.setText(data.getFollow_users() + "");
        }
        if (data.getFollowed_users() == 0) {
            guanzhuwo_number.setText("0");
        } else {
            guanzhuwo_number.setText(data.getFollowed_users() + "");
        }
        if (data.getQuestions() == 0) {
            tiwen_number.setText("0");
        } else {
            tiwen_number.setText(data.getQuestions() + "");
        }
        if (data.getAnswer() == 0) {
            answer_numbers.setText("0");
        } else {
            answer_numbers.setText(data.getAnswer() + "");
        }
        if (data.getFollow_questions() == 0) {
            guanzhu_question.setText("0");
        } else {
            guanzhu_question.setText(data.getFollow_questions() + "");
        }
        if (data.getCollecton_answer() == 0) {
            my_shoucang_number.setText("0");
        } else {
            my_shoucang_number.setText(data.getCollecton_answer() + "");
        }

        //对用户ID进行缓存
        SharedPreferencesHelper.put(getActivity(), "tel_phone", data.getTel() + "");
        SharedPreferencesHelper.put(getActivity(), "user_name", data.getName() + "");
    }

    @Override
    public void fetchData() {

    }

}

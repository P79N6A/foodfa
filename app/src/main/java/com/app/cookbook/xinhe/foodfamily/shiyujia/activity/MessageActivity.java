package com.app.cookbook.xinhe.foodfamily.shiyujia.activity;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.callback.DialogCallBack;
import com.app.cookbook.xinhe.foodfamily.main.DianZanActivity;
import com.app.cookbook.xinhe.foodfamily.main.GuanZhuAndQuestionActivity;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.main.MessageCommentActivity;
import com.app.cookbook.xinhe.foodfamily.main.NewQuestionActivity;
import com.app.cookbook.xinhe.foodfamily.main.XiTongLocationActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.Location;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.PopWindowHelper;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.StatusBarUtil;
import com.tencent.stat.StatService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

public class MessageActivity extends AppCompatActivity {


    /**
     * Rxjava
     */
    Subscription subscription;
    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.xiaoxi_layout)
    RelativeLayout xiaoxi_layout;
    @BindView(R.id.nicheng_layout)
    RelativeLayout nicheng_layout;
    @BindView(R.id.touxiang_layout)
    RelativeLayout touxiang_layout;
    @BindView(R.id.zhiye_layout)
    RelativeLayout zhiye_layout;
    @BindView(R.id.pinglun_layout)
    RelativeLayout pinglun_layout;
    @BindView(R.id.zuixin_message_number)
    TextView zuixin_message_number;
    @BindView(R.id.dianzan_message_number)
    TextView dianzan_message_number;
    @BindView(R.id.guanzhu_number)
    TextView guanzhu_number;
    @BindView(R.id.xitong_location_number)
    TextView xitong_location_number;
    @BindView(R.id.system_location_img_hui)
    ImageView system_location_img_hui;
    @BindView(R.id.system_location_img_hong)
    ImageView system_location_img_hong;
    @BindView(R.id.system_guanzhu_img_hui)
    ImageView system_guanzhu_img_hui;
    @BindView(R.id.system_guanzhu_img_hong)
    ImageView system_guanzhu_img_hong;
    @BindView(R.id.system_dianzan_img_hui)
    ImageView system_dianzan_img_hui;
    @BindView(R.id.system_dianzan_img_hong)
    ImageView system_dianzan_img_hong;
    @BindView(R.id.system_answer_img_hui)
    ImageView system_answer_img_hui;
    @BindView(R.id.system_answer_img_hong)
    ImageView system_answer_img_hong;
    @BindView(R.id.pinglun_message_comm_number)
    TextView pinglun_message_comm_number;
    @BindView(R.id.system_pinglun_comm_img_hui)
    ImageView system_pinglun_comm_img_hui;
    @BindView(R.id.system_pinglun_comm_img_hong)
    ImageView system_pinglun_comm_img_hong;
    @BindView(R.id.open_mes)
    RelativeLayout open_mes;
    @BindView(R.id.mes_name)
    TextView mes_name;
    @BindView(R.id.mes_close)
    TextView mes_close;
    @BindView(R.id.no_open)
    TextView no_open;
    //一天时间
    private int seconds_of_1day = 24 * 60 * 60;


    //private boolean isVisable = false;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private static final long DELAY = 5000;
    public MyHandler handler;
    private Location location = new Location();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        steepStatusBar();
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        ClickListener();
    }

    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(MessageActivity.this, R.color.white);
        } else {
            StatusBarUtil.transparencyBar(MessageActivity.this);
        }
    }

    private void ClickListener() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /***
         *打开自启动
         */
        mes_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(MessageActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(MessageActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    handlerStart.sendEmptyMessage(0);
                }
            }
        });

        no_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(MessageActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(MessageActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    long str = System.currentTimeMillis();
                    int inttime = (int) str;
                    String strtime = String.valueOf(inttime);
                    SharedPreferencesHelper.put(MessageActivity.this, "ColseTime", strtime);
                    open_mes.setVisibility(View.GONE);
                }
            }
        });

        /**
         * 关注了我问题
         */
        xiaoxi_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop2 = new Properties();
                prop2.setProperty("name", "xiaoxi_layout");
                StatService.trackCustomKVEvent(MessageActivity.this, "News_pay_attention_me_problem", prop2);
                if ("".equals(SharedPreferencesHelper.get(MessageActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(MessageActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MessageActivity.this, GuanZhuAndQuestionActivity.class);
                    startActivity(intent);
                }
            }
        });

        /**
         * 点赞收藏
         */
        nicheng_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop2 = new Properties();
                prop2.setProperty("name", "nicheng_layout");
                StatService.trackCustomKVEvent(MessageActivity.this, "News_praise_collection", prop2);
                if ("".equals(SharedPreferencesHelper.get(MessageActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(MessageActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MessageActivity.this, DianZanActivity.class);
                    startActivity(intent);
                }
            }
        });
        /***
         * 评论
         */
        pinglun_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop2 = new Properties();
                prop2.setProperty("name", "pinglun_layout");
                StatService.trackCustomKVEvent(MessageActivity.this, "News_comment", prop2);
                if ("".equals(SharedPreferencesHelper.get(MessageActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(MessageActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MessageActivity.this, MessageCommentActivity.class);
                    startActivity(intent);
                }
            }
        });

        /**
         * 最新回答
         */
        touxiang_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop2 = new Properties();
                prop2.setProperty("name", "touxiang_layout");
                StatService.trackCustomKVEvent(MessageActivity.this, "News_laster_answer", prop2);
                if ("".equals(SharedPreferencesHelper.get(MessageActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(MessageActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MessageActivity.this, NewQuestionActivity.class);
                    startActivity(intent);
                }
            }
        });

        /**
         * 系统通知
         */
        zhiye_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop2 = new Properties();
                prop2.setProperty("name", "zhiye_layout");
                StatService.trackCustomKVEvent(MessageActivity.this, "News_system_notification", prop2);
                if ("".equals(SharedPreferencesHelper.get(MessageActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(MessageActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MessageActivity.this, XiTongLocationActivity.class);
                    startActivity(intent);
                }
            }
        });

    }


    private final Runnable mRegularAction = new Runnable() {
        @Override
        public void run() {
            //每隔五秒请求一次
            initDatetwo(MessageActivity.this);//请求网络
            mHandler.postDelayed(this, DELAY);
        }
    };

    private void initDatetwo(Context context) {
        subscription = Network.getInstance("消息页面获取到消息数量", context)
                .get_location_shouye(
                        new ProgressSubscriberNew<>(Location.class, new GsonSubscriberOnNextListener<Location>() {
                            @Override
                            public void on_post_entity(Location mlocation) {
//                                Log.e("123","消息页面获取到消息数量：");
                                location = mlocation;  //通知FoodFamlyActivity更新UI
                                new Thread(new DownThread()).start();//启动线程
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "消息页面获取到消息数量报错：" + error);
                            }
                        }, context, false));
    }


    //每个一秒发一个通知
    class DownThread implements Runnable {
        private Handler mhandler;

        public DownThread() {
            this.mhandler = getHandler();
        }

        public void run() {
            try {
                Thread.sleep(100);
                Message msg = new Message();
                msg.what = 2;
                this.mhandler.sendMessage(msg);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public MyHandler getHandler() {
        if (null != handler) {
            return handler;
        } else {
            MyHandler myHandler = new MyHandler();
            return myHandler;
        }
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != 1) {
                int i = msg.what;
                if (i == 2) {
                    Log.e("后台有新的数据", "后台有新的数据");
                    updateUi(location);
                } else {
                    Log.e("未知", "未知");
                }
            }

        }

    }


    @Override
    public void onResume() {
        initDate(false);
        super.onResume();
        if ("".equals(SharedPreferencesHelper.get(MessageActivity.this, "login_token", ""))) {
            zuixin_message_number.setText("");
            system_answer_img_hui.setVisibility(View.VISIBLE);
            system_answer_img_hong.setVisibility(View.INVISIBLE);

            dianzan_message_number.setText("");
            system_dianzan_img_hui.setVisibility(View.VISIBLE);
            system_dianzan_img_hong.setVisibility(View.INVISIBLE);

            guanzhu_number.setText("");
            system_guanzhu_img_hui.setVisibility(View.VISIBLE);
            system_guanzhu_img_hong.setVisibility(View.INVISIBLE);

            xitong_location_number.setText("");
            system_location_img_hong.setVisibility(View.INVISIBLE);
            system_location_img_hui.setVisibility(View.VISIBLE);

            pinglun_message_comm_number.setText("");
            system_pinglun_comm_img_hui.setVisibility(View.VISIBLE);
            system_pinglun_comm_img_hong.setVisibility(View.INVISIBLE);
        } else {
            initDate(false);
            //判断通知是否打开
            if (isNotificationEnabled(MessageActivity.this) == true) {
                open_mes.setVisibility(View.GONE);
            } else {
                open_mes.setVisibility(View.VISIBLE);
                String time = SharedPreferencesHelper.get(MessageActivity.this, "ColseTime", "").toString();
                if (time != null && !TextUtils.isEmpty(time)) {
                    long str = System.currentTimeMillis();
                    int time1 = (int) str;
                    int time2 = Integer.valueOf(time);
                    int time3 = (time1 - time2);
                    if (time3 == seconds_of_1day) {
                        open_mes.setVisibility(View.VISIBLE);
                    } else {
                        open_mes.setVisibility(View.GONE);
                    }
                }
            }

            mHandler = new Handler(Looper.getMainLooper());
            mHandler.removeCallbacks(mRegularAction);
            mHandler.post(mRegularAction);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != mHandler) {
            mHandler.removeCallbacks(mRegularAction);   //把runable移除队列
            mHandler = null;
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (null != mHandler) {
            mHandler.removeCallbacks(mRegularAction);   //把runable移除队列
            mHandler = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mHandler) {
            mHandler.removeCallbacks(mRegularAction);   //把runable移除队列
            mHandler = null;
        }
    }

    private void initDate(boolean is_show) {
        subscription = Network.getInstance("消息页面获取通知界面", MessageActivity.this)
                .get_location_shouye(
                        new ProgressSubscriberNew<>(Location.class, new GsonSubscriberOnNextListener<Location>() {
                            @Override
                            public void on_post_entity(Location location) {
                                updateUi(location);
                                Log.e("123", "消息页面获取通知界面成功：");
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "消息页面获取通知界面报错：" + error);
                            }
                        }, MessageActivity.this, is_show));
    }


    private void updateUi(Location location) {
        if (location.getAnswer_number() == 0 && location.getThumbs_count() == 0 &&
                location.getFollow_count() == 0 && location.getSys_count() == 0 &&
                location.getComment_count() == 0) {
            Log.e("123", "       消息为空     ");
            Contacts.mesgNumber = "";
        }
        if (location.getAnswer_number() == 0) {
            zuixin_message_number.setText("");
            system_answer_img_hui.setVisibility(View.VISIBLE);
            system_answer_img_hong.setVisibility(View.INVISIBLE);
        } else {
            zuixin_message_number.setText(String.valueOf(location.getAnswer_number()));
            system_answer_img_hui.setVisibility(View.INVISIBLE);
            system_answer_img_hong.setVisibility(View.VISIBLE);
        }
        if (location.getThumbs_count() == 0) {
            dianzan_message_number.setText("");
            system_dianzan_img_hui.setVisibility(View.VISIBLE);
            system_dianzan_img_hong.setVisibility(View.INVISIBLE);
        } else {
            dianzan_message_number.setText(String.valueOf(location.getThumbs_count()));
            system_dianzan_img_hui.setVisibility(View.INVISIBLE);
            system_dianzan_img_hong.setVisibility(View.VISIBLE);

        }
        if (location.getFollow_count() == 0) {
            guanzhu_number.setText("");
            system_guanzhu_img_hui.setVisibility(View.VISIBLE);
            system_guanzhu_img_hong.setVisibility(View.INVISIBLE);
        } else {
            guanzhu_number.setText(String.valueOf(location.getFollow_count()));
            system_guanzhu_img_hui.setVisibility(View.INVISIBLE);
            system_guanzhu_img_hong.setVisibility(View.VISIBLE);
        }
        if (location.getSys_count() == 0) {
            xitong_location_number.setText("");
            system_location_img_hong.setVisibility(View.INVISIBLE);
            system_location_img_hui.setVisibility(View.VISIBLE);
        } else {
            xitong_location_number.setText(String.valueOf(location.getSys_count()));
            system_location_img_hui.setVisibility(View.INVISIBLE);
            system_location_img_hong.setVisibility(View.VISIBLE);
        }

        if (location.getComment_count() == 0) {
            pinglun_message_comm_number.setText("");
            system_pinglun_comm_img_hui.setVisibility(View.VISIBLE);
            system_pinglun_comm_img_hong.setVisibility(View.INVISIBLE);
        } else {
            pinglun_message_comm_number.setText(String.valueOf(location.getComment_count()));
            system_pinglun_comm_img_hui.setVisibility(View.INVISIBLE);
            system_pinglun_comm_img_hong.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 开启自启动
     */
    Handler handlerStart = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //判断自启动权限
            PopWindowHelper.public_tishi_pop(MessageActivity.this, "食与家提示", "是否设置后台接受推送？", "取消", "设置", new DialogCallBack() {
                @Override
                public void save() {
//                    openStart(MessageActivity.this);
                    toSetting();
                }

                @Override
                public void cancel() {
                }
            });
        }
    };

    private void toSetting() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", "com.app.cookbook.xinhe.foodfamily", null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", "com.app.cookbook.xinhe.foodfamily");
        }
        startActivity(localIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

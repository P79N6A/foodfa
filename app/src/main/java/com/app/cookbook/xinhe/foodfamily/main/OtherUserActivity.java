package com.app.cookbook.xinhe.foodfamily.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.main.adapter.AnswerFunctionAdapter;
import com.app.cookbook.xinhe.foodfamily.main.adapter.ReportMsgAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.FunctionItem;
import com.app.cookbook.xinhe.foodfamily.main.entity.OtherUserEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.ReportMsg;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.tencent.stat.StatService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class OtherUserActivity extends BaseActivity {
    @BindView(R.id.back_layout)
    LinearLayout back_layout;
    @BindView(R.id.more_btn)
    ImageView more_btn;
    @BindView(R.id.huxiang_layout_top)
    LinearLayout huxiang_guanzhu_btn;
    @BindView(R.id.user_name)
    TextView user_name;
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
    @BindView(R.id.top_layout)
    LinearLayout top_layout;
    @BindView(R.id.zhiye_tv)
    TextView zhiye_tv;
    @BindView(R.id.qianming_tv)
    TextView qianming_tv;
    @BindView(R.id.touxiang_image)
    CircleImageView touxiang_image;
    @BindView(R.id.xingbie_image)
    ImageView xingbie_image;
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
    @BindView(R.id.guanzhu_layout_top)
    LinearLayout guanzhu_layout_top;
    @BindView(R.id.huxiangngquxiaoguanzhu_layout_top)
    LinearLayout huxiangngquxiaoguanzhu_layout_top;


    OtherUserEntity otherUserEntity = new OtherUserEntity();
    private String id = "-1";
    //判断是否被禁用
    private String is_delete;

    //删除，编辑，举报
    private int[] functionImageids = {R.drawable.jubao};
    private String[] functionName = {"举报"};
    private List<FunctionItem> functionListem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void RightImgOnclick() {

    }

    @Override
    public void initParms(Bundle parms) {
        if (null != parms) {
            if (null != parms.getString("user_id")) {
                id = parms.getString("user_id");
                Log.e("传过来的UserID", id);
            } else {
                id = "-1";
            }
        }
    }

    @Override
    public void initView() {
        setContentLayout(R.layout.activity_other_user);
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

        //页面跳转逻辑
        business_method();
        //关注用户
        guanzhu_layout_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(OtherUserActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(OtherUserActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    guanzhu_submit();
                }
            }
        });

        //取消关注用户
        huxiang_guanzhu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(OtherUserActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(OtherUserActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    quxiao_guanzhu_submit();
                }
            }
        });

        huxiangngquxiaoguanzhu_layout_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(OtherUserActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(OtherUserActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    quxiao_guanzhu_submit();
                }
            }
        });

        Properties prop = new Properties();
        prop.setProperty("name", "into_other_user_detail");
        StatService.trackCustomKVEvent(getApplicationContext(), "TA", prop);


    }

    @Override
    protected void onResume() {
        if (id.equals("-1")) {

        } else {
            //获取用户个人资料
            get_user_information();
        }
        super.onResume();
    }

    private void quxiao_guanzhu_submit() {
        Map<String, String> params = new HashMap<>();
        if (null != id) {
            params.put("uuid", id);
        }
        Log.e("取消关注他：", params.toString());
        subscription = Network.getInstance("取消关注他", this)
                .quxiaoguanzhuta(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "取消关注他成功：" + result.getCode());
                                //切换布局
//                                huxiang_guanzhu_btn.setVisibility(View.GONE);
//                                guanzhu_layout_top.setVisibility(View.VISIBLE);
                                get_user_information();

                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Log.e("123", "取消关注他报错：" + error);
                            }
                        }, this, false));
    }


    private void guanzhu_submit() {
        Map<String, String> params = new HashMap<>();
        if (null != id) {
            params.put("uuid", id);
        }
        Log.e("关注他：", params.toString());
        subscription = Network.getInstance("关注他", this)
                .guanzhuta(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "关注他成功：" + result.getCode());
                                //切换布局
//                                huxiang_guanzhu_btn.setVisibility(View.VISIBLE);
//                                guanzhu_layout_top.setVisibility(View.GONE);
//                                huxiangngquxiaoguanzhu_layout_top.setVisibility(View.VISIBLE);


                                get_user_information();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Log.e("123", "关注他报错：" + error);
                            }
                        }, this, false));
    }

    OtherUserEntity data = new OtherUserEntity();

    private void get_user_information() {
        Map<String, String> params = new HashMap<>();
        if (null != id) {
            params.put("id", id);
        }
        Log.e("获取他人信息参数：", params.toString());
        subscription = Network.getInstance("获取他人信息", this)
                .get_other_user_information(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<OtherUserEntity>>() {
                            @Override
                            public void onNext(Bean<OtherUserEntity> result) {
                                Log.e("123", "获取他人信息成功：" + result.getCode());
                                data = result.getData();
                                if ("2".equals(data.getUser_status())) {
                                    Toast.makeText(OtherUserActivity.this, "该账号因违反平台相关规定，已被禁止访问", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                //设置页面的信息
                                set_user_information();
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "获取他人信息报错：" + error);
                                Toast.makeText(OtherUserActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }, this, false));
    }

    private void set_user_information() {
        otherUserEntity = data;
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
            Glide.with(this).load(data.getAvatar())
                    .error(R.drawable.touxiang)
                    .into(touxiang_image);
        }
        //设置性别
        if (null != data.getSex()) {
            if (data.getSex().equals("1")) {//男
                xingbie_image.setImageResource(R.drawable.xingbie_nan);
            } else {
                xingbie_image.setImageResource(R.drawable.xingbie_nv);
            }
        }
        if (data.getFollow_class().equals("0")) {
            biaoqian_number.setText("0");
        } else {
            biaoqian_number.setText(data.getFollow_class());
        }
        if (data.getFollow_users().equals("0")) {
            guanzhu_people_number.setText("0");
        } else {
            guanzhu_people_number.setText(data.getFollow_users());
        }
        if (data.getFollowed_users().equals("0")) {
            guanzhuwo_number.setText("0");
        } else {
            guanzhuwo_number.setText(data.getFollowed_users());
        }
        if (data.getQuestions().equals("0")) {
            tiwen_number.setText("0");
        } else {
            tiwen_number.setText(data.getQuestions());
        }
        if (data.getAnswer().equals("0")) {
            answer_numbers.setText("0");
        } else {
            answer_numbers.setText(data.getAnswer());
        }
        if (data.getFollow_questions().equals("0")) {
            guanzhu_question.setText("0");
        } else {
            guanzhu_question.setText(data.getFollow_questions());
        }
        if (data.getCollecton_answer().equals("0")) {
            my_shoucang_number.setText("0");
        } else {
            my_shoucang_number.setText(data.getCollecton_answer());
        }

        String user_id = SharedPreferencesHelper.get(getApplicationContext(), "user_id", "").toString();

        if (!TextUtils.isEmpty(user_id)) {
            if (user_id.equals(String.valueOf(id))) {
                guanzhu_layout_top.setVisibility(View.GONE);
                huxiang_guanzhu_btn.setVisibility(View.GONE);
                huxiangngquxiaoguanzhu_layout_top.setVisibility(View.GONE);
            } else {
                if (data.getRelational().equals("1")) {//已关注
                    guanzhu_layout_top.setVisibility(View.GONE);
                    huxiang_guanzhu_btn.setVisibility(View.VISIBLE);
                    huxiangngquxiaoguanzhu_layout_top.setVisibility(View.GONE);
                } else if (data.getRelational().equals("0")) {//互相关注
                    huxiangngquxiaoguanzhu_layout_top.setVisibility(View.VISIBLE);
                    huxiang_guanzhu_btn.setVisibility(View.GONE);
                    guanzhu_layout_top.setVisibility(View.GONE);
                } else if (data.getRelational().equals("2")) {//未关注
                    huxiang_guanzhu_btn.setVisibility(View.GONE);
                    guanzhu_layout_top.setVisibility(View.VISIBLE);
                    huxiangngquxiaoguanzhu_layout_top.setVisibility(View.GONE);
                }
            }
        }
    }

    private void business_method() {
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (functionListem.size() > 0) {
                    functionListem.clear();
                }
                select_more_popwindow();
            }
        });

        //ta关注的人
        woguanzhu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "woguanzhu_btn");
                StatService.trackCustomKVEvent(getApplicationContext(), "TA_pay_attention_user", prop);

                Intent intent = new Intent(OtherUserActivity.this, TaGuanZhuDeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //关注ta的人
        guanzhuwo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "guanzhuwo_btn");
                StatService.trackCustomKVEvent(getApplicationContext(), "TA_user_pay_attention", prop);

                Intent intent = new Intent(OtherUserActivity.this, GuanZhuTaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //Ta关注的标签
        guanzhubiaoqian_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "guanzhubiaoqian_btn");
                StatService.trackCustomKVEvent(getApplicationContext(), "TA_pay_attention_tag", prop);

                Intent intent = new Intent(OtherUserActivity.this, TaGuanZhuBiaoQianActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //他的提问
        wodetiwen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "wodetiwen_btn");
                StatService.trackCustomKVEvent(getApplicationContext(), "TA_question", prop);

                Intent intent = new Intent(OtherUserActivity.this, TaDeTiWenActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id", id);
                bundle.putString("other_user_id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //TA的回答
        huida_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "huida_layout");
                StatService.trackCustomKVEvent(getApplicationContext(), "TA_answer", prop);

                Intent intent = new Intent(OtherUserActivity.this, TAHuiDaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //TA关注的问题
        guanzhu_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "guanzhu_layout");
                StatService.trackCustomKVEvent(getApplicationContext(), "TA_pay_attention_problem", prop);

                Intent intent = new Intent(OtherUserActivity.this, TAGuanZhuQuestionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //Ta的收藏
        shoucang_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "shoucang_layout");
                StatService.trackCustomKVEvent(getApplicationContext(), "TA_collection", prop);

                Intent intent = new Intent(OtherUserActivity.this, TaShouCangActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id", id + "");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //头像点击
        top_layout.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                String[] imageUrls = new String[]{data.getAvatar()};
                if (imageUrls.length > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("imageUrls", imageUrls);
//                intent.putExtra("curImageUrl", data.getAvatar());
                    intent.putExtra("imageType", "1");
                    intent.setClass(OtherUserActivity.this, PhotoBrowserActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


//    /**
//     * 去往编辑的页面
//     */
//    private void go_to_edit() {
//        Note note = new Note();
//        saveNoteData(note, answerDate.getId(), answerDate.getAnswer_content_remove(), answerDate.getContent());
//        Intent intent = new Intent(AnserActivity.this, AnswerDetailActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("flag", "1");
//        bundle.putString("question_id", answerDate.getPid());
//        bundle.putString("answer_id", answerDate.getId());
//        intent.putExtras(bundle);
//        startActivity(intent);
//        dialog.dismiss();
//    }
//
//    /**
//     * 保存数据,=0销毁当前界面，=1不销毁界面，为了防止在后台时保存笔记并销毁，应该只保存笔记
//     */
//    private NoteDao noteDao;
//    private void saveNoteData(Note note, String answer_id, String content, String mcontent) {
//        //以问题的ID作为分类
//        note.setTitle("回答");
//        note.setContent(content);
//        note.setMcontent(mcontent);
//        note.setGroupId(Integer.valueOf(answer_id));
//        note.setGroupName(answer_id);
//        note.setType(2);
//        note.setBgColor("#FFFFFF");
//        note.setIsEncrypt(0);
//        note.setCreateTime(DateUtils.date2string(new Date()));
//        //新建笔记
//        long noteId = noteDao.insertNote(note);
//        Log.i("", "新建立note答案Id: " + noteId);
//    }

    /**
     * 举报
     *
     * @param v
     */
    private Dialog dialog;
    //编辑按钮
    private LinearLayout bianji_layout;


    private void select_more_popwindow() {
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.more_popwindow, null);
        bianji_layout = view.findViewById(R.id.bianji_layout);
        //分享列表
        RecyclerView more_recyclerView = view.findViewById(R.id.more_recyclerView);
        more_recyclerView.setVisibility(View.GONE);

        //刪除，編輯，舉報功能列表
        RecyclerView function_recyclerView = view.findViewById(R.id.function_recyclerView);
        LinearLayoutManager function_layoutManager = new LinearLayoutManager(this);
        function_layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        function_recyclerView.setLayoutManager(function_layoutManager);
        function_recyclerView.setHasFixedSize(true);
        //取消
        LinearLayout select_dismiss = view.findViewById(R.id.select_dismiss);

        dialog = new Dialog(this, R.style.transparentFrameWindowStyle2);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        if (functionListem.size() == 0) {
            for (int i = 0; i < functionName.length; i++) {
                FunctionItem item = new FunctionItem();
                item.setFunctionImage(functionImageids[i]);
                item.setFunctionName(functionName[i]);
                functionListem.add(item);
            }
        }
        //功能填充适配器
        AnswerFunctionAdapter answerFunctionAdapter = new AnswerFunctionAdapter(this);
        answerFunctionAdapter.setShareImageItems(functionListem);
        function_recyclerView.setAdapter(answerFunctionAdapter);

        answerFunctionAdapter.setOnItemClickListener(new AnswerFunctionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if ("".equals(SharedPreferencesHelper.get(OtherUserActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(OtherUserActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (reportMsgs.size() > 0) {
                        reportMsgs.clear();
                    }
                    report_msg_question();
                    dialog.dismiss();
                }
            }
        });

        select_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private LinearLayoutManager linearLayoutManager;
    private ReportMsgAdapter reportMsgAdapter;
    private List<ReportMsg> reportMsgs = new ArrayList<>();
    private TextView tv_report;
    private TextView report_title;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 500:
                    tv_report.setTextColor(getResources().getColor(R.color.bottom_color));
                    break;
                case 501:
                    tv_report.setTextColor(getResources().getColor(R.color.bottom_color));
                    break;
                case 502:
                    tv_report.setTextColor(getResources().getColor(R.color.meishititle));
                    break;
            }

        }
    };

    private void select_report_popwindow(List<ReportMsg> msgs) {
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.report_popwindow, null);
        //举报信息
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayout report_btn = view.findViewById(R.id.report_btn);
        tv_report = view.findViewById(R.id.tv_report);
        report_title = view.findViewById(R.id.report_title);
        report_title.setText("举报用户");

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        reportMsgAdapter = new ReportMsgAdapter(this, handler);
        reportMsgAdapter.setData(msgs);
        recyclerView.setAdapter(reportMsgAdapter);

        WindowManager wm1 = this.getWindowManager();
        int height1 = wm1.getDefaultDisplay().getHeight();


        dialog = new Dialog(this, R.style.transparentFrameWindowStyle2);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.height = height1 - (height1 / 3);
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                //处理监听事件
                Contacts.typeMsg.clear();
            }
        });
        //设置逻辑交互
        //编辑
        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = Contacts.typeMsg.toString();
                int len = str.length() - 1;
                String ids = str.substring(1, len).replace(" ", "");//"keyids":”1,2,3”
                if (!TextUtils.isEmpty(ids)) {
                    report_question(id, ids);
                } else {
                    Toast.makeText(OtherUserActivity.this, "请选择举报内容", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    /**
     * 举报
     *
     * @param question_id
     */
    private void report_question(String question_id, String type) {
        Map<String, String> params = new HashMap<>();
        params.put("reported_id", question_id);
        params.put("type", type);
        Log.e("关注问题：", params.toString());
        subscription = Network.getInstance("举报问题", this)
                .reports_user_request(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "举报问题：" + result.getCode());
                                Toast.makeText(OtherUserActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                Contacts.typeMsg.clear();
                                dialog.dismiss();
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "举报问题异常：" + error.toString());
//                                Toast.makeText(OtherUserActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }, this, false));

    }

    /**
     * 获取举报数据
     */
    private void report_msg_question() {
        subscription = Network.getInstance("获取举报问题类型", this)
                .report_user_type_request(new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<ReportMsg>>>() {
                    @Override
                    public void onNext(Bean<List<ReportMsg>> result) {

                        for (ReportMsg msg : result.getData()) {
//                            Logger.e("获取举报问题类型：" + msg.getId() + "            " + msg.getName());
                            reportMsgs.add(msg);
                        }
                        select_report_popwindow(reportMsgs);
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("123", "获取举报问题类型异常：" + error.toString());
                    }
                }, this, false));
    }

    @Override
    public void widgetClick(View v) {

    }
}

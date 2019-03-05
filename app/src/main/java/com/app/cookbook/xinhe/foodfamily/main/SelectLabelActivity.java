package com.app.cookbook.xinhe.foodfamily.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.BaseActivity;
import com.app.cookbook.xinhe.foodfamily.application.MyApplication;
import com.app.cookbook.xinhe.foodfamily.main.adapter.HotLabelAdapter;
import com.app.cookbook.xinhe.foodfamily.main.adapter.LabelSearchAdapter;
import com.app.cookbook.xinhe.foodfamily.main.adapter.SelectLabelAdapter;
import com.app.cookbook.xinhe.foodfamily.main.adapter.UseLabelAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.CategoryDateEn;
import com.app.cookbook.xinhe.foodfamily.main.entity.HotLabel;
import com.app.cookbook.xinhe.foodfamily.main.entity.Label;
import com.app.cookbook.xinhe.foodfamily.main.entity.UploadImageEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.UseLabel;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.net.rx.RxUtils;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.AddImagesActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.PlayerActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferensListHelper;
import com.app.cookbook.xinhe.foodfamily.util.StringUtils;
import com.app.cookbook.xinhe.foodfamily.util.layout.TagCloudLayout;
import com.orhanobut.logger.Logger;
import com.tencent.stat.StatService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

import static com.app.cookbook.xinhe.foodfamily.util.Textutil.setEtFilter;

public class SelectLabelActivity extends BaseActivity {

    //返回
    @BindView(R.id.iamge_back)
    ImageView iamge_back;
    //发布按钮
    @BindView(R.id.fabu_btn)
    TextView fabu_btn;
    //输入框
    @BindView(R.id.et_search)
    EditText et_search;
    //删除输入内容
    @BindView(R.id.shanchu_btn)
    ImageView shanchu_btn;
    //已选 标签布局
    @BindView(R.id.select_label_layout)
    FrameLayout select_label_layout;
    //已选标签
    @BindView(R.id.select_recyclerView)
    RecyclerView select_recyclerView;
    //已选标签占位符
    @BindView(R.id.select_label_image)
    ImageView select_label_image;

    //常用 标签布局
    @BindView(R.id.use_label_layout)
    LinearLayout use_label_layout;
    //常用 标签
    @BindView(R.id.use_recyclerView)
    TagCloudLayout creatTabGv;

    //热门 标签布局
    @BindView(R.id.hot_label_layout)
    LinearLayout hot_label_layout;
    //热门标签
    @BindView(R.id.hot_recyclerView)
    RecyclerView hot_recyclerView;

    //最近使用标签文字
    @BindView(R.id.tv_shiyong)
    TextView tv_shiyong;
    //搜索列表
    @BindView(R.id.recycler_layout)
    LinearLayout recycler_layout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.custom_view)
    XRefreshView xrefreshview;
    //已选适配器
    private SelectLabelAdapter selectLabelAdapter;
    //已选标签布局管理器
    private LinearLayoutManager linearLayoutManager;
    //添加后名称集合
    private List<String> str = new ArrayList();
    private List<String> strName = new ArrayList();
    //搜索结果集合
    private List<Label.DataBean> labels = new ArrayList<>();
    //搜索适配器
    private LabelSearchAdapter labelSearchAdapter;
    private LinearLayoutManager layoutManager;
    private int PAGE = 0;
    public static String searchKey = null;
    //常用标签
    private UseLabel.DataBean useDataBean;
    private UseLabelAdapter useLabelAdapter;
    private List<UseLabel.DataBean> useLabelList = new ArrayList<>();

    //热门标签
    private HotLabel.DataBean hotDataBean;
    private HotLabelAdapter hotLabelAdapter;
    private List<HotLabel.DataBean> hotLabelList = new ArrayList<>();

    //常用标签,热门标签布局格式
    private GridLayoutManager hotGridLayoutManager;

    //数据存储工具
    private Label.DataBean item;
    //缓存集合
    private List<Label.DataBean> data = new ArrayList<>();

    //接受的数据
    private Bundle bundle;
    private String title, question_id, img_data, flag, content;
    public static List<CategoryDateEn> chuan_biaoqian_list = new ArrayList<>();
    private List<UploadImageEntity> upload_iamge_paths = new ArrayList<>();
    private boolean is_net_success;
    private SharedPreferensListHelper sharedPreferensListHelper;
    private ArrayList<UploadImageEntity> huancun_detail_image = new ArrayList<>();
    private String enter_select_add_images = "";

    //图片提交页传过来的content
    public static String fabu_content = "";
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 400:
                    item = labels.get(msg.arg1);
                    if (data.size() < 5 && str.size() < 5) {
                        select_label_image.setVisibility(View.GONE);
                        select_recyclerView.setVisibility(View.VISIBLE);
                        select_label_layout.setVisibility(View.VISIBLE);
                        item.setCheck(true);
                        if (!TextUtils.isEmpty(item.getId())) {
                            str.add(item.getId().replace(" ", ""));
                        } else {
                            str.add(item.getTitle().replace(" ", ""));
                        }
                        strName.add(item.getTitle().replace(" ", ""));
                        Label.DataBean dataBean = new Label.DataBean();
                        dataBean.setCheck(false);
                        dataBean.setTitle(item.getTitle().replace(" ", ""));
                        dataBean.setQuestions(item.getQuestions());
                        dataBean.setPath(item.getPath());
                        dataBean.setId(item.getId());
                        dataBean.setFollow(item.getFollow());
                        dataBean.setDesc(item.getDesc());
                        data.add(dataBean);
                        selectLabelAdapter.setSelectList(strName);
                        select_recyclerView.setAdapter(selectLabelAdapter);
                        selectLabelAdapter.notifyDataSetChanged();
//                        labelSearchAdapter.notifyDataSetChanged();
                        getHotClass();
                        getUsersUsedClass();
                    } else {
                        Toast.makeText(SelectLabelActivity.this, "标签设置不可超过5个", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 401:
                    item = labels.get(msg.arg1);
                    item.setCheck(false);
                    for (int i = 0; i < str.size(); i++) {
                        if (str.get(i).equals(item.getTitle().replace(" ", "")) || str.get(i).equals(item.getId().replace(" ", ""))) {
                            str.remove(str.get(i));

                        }
                    }
                    for (int i = 0; i < strName.size(); i++) {
                        if (strName.get(i).equals(item.getTitle())) {
                            strName.remove(strName.get(i));
                        }
                    }
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getTitle().equals(item.getTitle()) || data.get(i).getId().equals(item.getId())) {
                            data.remove(data.get(i));
                        }
                    }
                    selectLabelAdapter.notifyDataSetChanged();
//                    labelSearchAdapter.notifyDataSetChanged();
                    getHotClass();
                    getUsersUsedClass();
//                    if (strName.size() <= 0) {
//                        select_label_layout.setVisibility(View.GONE);
//                    }
                    break;
                case 402:
                    useDataBean = useLabelList.get(msg.arg1);
                    if (data.size() < 5 && str.size() < 5) {
                        select_label_layout.setVisibility(View.VISIBLE);
                        useDataBean.setSelect(true);
                        Log.e("123", "       str.size() --- 3 ----       " + str.size());
                        if (!TextUtils.isEmpty(useDataBean.getId())) {
                            str.add(useDataBean.getId().replace(" ", ""));
                        } else {
                            str.add(useDataBean.getTitle().replace(" ", ""));
                        }
                        strName.add(useDataBean.getTitle().replace(" ", ""));
                        Label.DataBean dataBean = new Label.DataBean();
                        dataBean.setCheck(false);
                        dataBean.setTitle(useDataBean.getTitle().replace(" ", ""));
                        dataBean.setQuestions("");
                        dataBean.setPath(useDataBean.getPath());
                        dataBean.setId(useDataBean.getId());
                        dataBean.setFollow("");
                        dataBean.setDesc("");
                        data.add(dataBean);
                        selectLabelAdapter.setSelectList(strName);
                        select_recyclerView.setAdapter(selectLabelAdapter);
                        selectLabelAdapter.notifyDataSetChanged();
                        useLabelAdapter.notifyDataSetChanged();
                        getHotClass();
                    } else {
                        Toast.makeText(SelectLabelActivity.this, "标签设置不可超过5个", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 403:
                    useDataBean = useLabelList.get(msg.arg1);
                    useDataBean.setSelect(false);
                    for (int i = 0; i < str.size(); i++) {
                        if (str.get(i).equals(useDataBean.getTitle().replace(" ", "")) || str.get(i).equals(useDataBean.getId().replace(" ", ""))) {
                            str.remove(str.get(i));
                        }
                    }
                    for (int i = 0; i < strName.size(); i++) {
                        if (strName.get(i).equals(useDataBean.getTitle().replace(" ", ""))) {
                            strName.remove(strName.get(i));
                        }
                    }
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getTitle().equals(useDataBean.getTitle()) || data.get(i).getId().equals(useDataBean.getId())) {
                            data.remove(data.get(i));
                        }
                    }
                    selectLabelAdapter.notifyDataSetChanged();
                    useLabelAdapter.notifyDataSetChanged();

                    getHotClass();
                    break;
                case 404:
                    hotDataBean = hotLabelList.get(msg.arg1);
                    //添加
                    if (data.size() < 5 && str.size() < 5) {
                        select_label_layout.setVisibility(View.VISIBLE);
                        hotDataBean.setSelect(true);
                        if (!TextUtils.isEmpty(hotDataBean.getClass_id())) {
                            str.add(hotDataBean.getClass_id().replace(" ", ""));
                        } else {
                            str.add(hotDataBean.getTitle().replace(" ", ""));
                        }
                        strName.add(hotDataBean.getTitle().replace(" ", ""));
                        Label.DataBean dataBean = new Label.DataBean();
                        dataBean.setCheck(false);
                        dataBean.setTitle(hotDataBean.getTitle().replace(" ", ""));
                        dataBean.setQuestions("");
                        dataBean.setPath(hotDataBean.getPath());
                        dataBean.setId(hotDataBean.getClass_id());
                        dataBean.setFollow("");
                        dataBean.setDesc("");
                        data.add(dataBean);
                        selectLabelAdapter.setSelectList(strName);
                        select_recyclerView.setAdapter(selectLabelAdapter);
                        selectLabelAdapter.notifyDataSetChanged();
                        hotLabelAdapter.notifyDataSetChanged();
                        getUsersUsedClass();
                    } else {
                        Toast.makeText(SelectLabelActivity.this, "标签设置不可超过5个", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 405:
                    hotDataBean = hotLabelList.get(msg.arg1);
                    hotDataBean.setSelect(false);
                    for (int i = 0; i < str.size(); i++) {
                        if (str.get(i).equals(hotDataBean.getTitle().replace(" ", "")) || str.get(i).equals(hotDataBean.getClass_id().replace(" ", ""))) {
                            str.remove(str.get(i));
                        }
                    }
                    for (int i = 0; i < strName.size(); i++) {
                        if (strName.get(i).equals(hotDataBean.getTitle())) {
                            strName.remove(strName.get(i));
                        }
                    }
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getTitle().equals(hotDataBean.getTitle()) || data.get(i).getId().equals(hotDataBean.getClass_id())) {
                            data.remove(data.get(i));
                        }
                    }
                    selectLabelAdapter.notifyDataSetChanged();
                    hotLabelAdapter.notifyDataSetChanged();
                    useLabelAdapter.notifyDataSetChanged();
                    getUsersUsedClass();
                    break;
                case 300:
                    if (Contacts.selsctType == false) {
                        Contacts.selsctType = true;
                    } else {
                        Contacts.deleteSelsctType = msg.arg1;
                        Contacts.selsctType = true;
                        selectLabelAdapter.setSelectList(strName);
                        select_recyclerView.setAdapter(selectLabelAdapter);
                        selectLabelAdapter.notifyDataSetChanged();
                    }
                    break;
                case 301:
//                    selectLabelAdapter.setSelectList(strName);
//                    select_recyclerView.setAdapter(selectLabelAdapter);
//                    selectLabelAdapter.notifyDataSetChanged();
                    break;
                case 302:
                    for (int i = 0; i < labels.size(); i++) {
                        if (labels.get(i).getTitle().equals(strName.get(msg.arg1)) || labels.get(i).getId().equals(strName.get(msg.arg1))) {
                            labels.get(i).setCheck(false);
                        }
                    }
                    for (int i = 0; i < data.size(); i++) {
                        if (!TextUtils.isEmpty(data.get(i).getTitle())) {
                            if (data.get(i).getTitle().equals(strName.get(msg.arg1))) {
                                data.remove(data.get(i));
                            }
                        }
                    }
                    for (int i = 0; i < hotLabelList.size(); i++) {
                        if (hotLabelList.get(i).getTitle() != null && hotLabelList.get(i).getClass_id() != null) {
                            if (hotLabelList.get(i).getTitle().equals(strName.get(msg.arg1)) || hotLabelList.get(i).getClass_id().equals(strName.get(msg.arg1))) {
                                hotLabelList.get(i).setSelect(false);
                            }
                        }
                    }
                    for (int i = 0; i < useLabelList.size(); i++) {
                        if (useLabelList.get(i).getTitle() != null && useLabelList.get(i).getId() != null) {
                            if (useLabelList.get(i).getTitle().equals(strName.get(msg.arg1)) || useLabelList.get(i).getId().equals(strName.get(msg.arg1))) {
                                useLabelList.get(i).setSelect(false);
                            }
                        }
                    }
                    str.remove(msg.arg1);
                    strName.remove(msg.arg1);
                    selectLabelAdapter.notifyDataSetChanged();
                    if (labels.size() > 0) {
                        labelSearchAdapter.notifyDataSetChanged();
                    }
                    getHotClass();
                    getUsersUsedClass();
                    Contacts.deleteSelsctType = -1;
                    selectLabelAdapter.setSelectList(strName);
                    select_recyclerView.setAdapter(selectLabelAdapter);
                    selectLabelAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public void RightImgOnclick() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (is_net_success) {
            MyApplication.destoryActivity("AddQuestionActivity");
        }
        searchKey = "";
    }

    @Override
    public void initParms(Bundle parms) {
        bundle = parms;
        if (null != bundle.getString("img_data")) {
            img_data = bundle.getString("img_data");
        } else {
            img_data = "";
        }
        if (null != bundle.getString("enter_select_add_images")) {
            enter_select_add_images = bundle.getString("enter_select_add_images");
        } else {
            enter_select_add_images = "";
        }
        get_chuan_resurce();
    }

    private void get_chuan_resurce() {
        flag = bundle.getString("flag");
        title = bundle.getString("title");
        content = bundle.getString("content");
        if (null != bundle.getString("question_id")) {
            question_id = bundle.getString("question_id");
        }
        Log.e("123", "    flag    " + flag + "      question_id      " + question_id);
        //接受传过来的标签集合
        if (null != bundle.getParcelableArrayList("biaoqian_list")) {
            chuan_biaoqian_list = bundle.getParcelableArrayList("biaoqian_list");
        }
        if (null != bundle.getParcelableArrayList("upload_iamge_paths")) {
            upload_iamge_paths = bundle.getParcelableArrayList("upload_iamge_paths");
        }
        //需要缓存到详情页的图片
        if (null != bundle.getParcelableArrayList("huancun_detail_image")) {
            huancun_detail_image = bundle.getParcelableArrayList("huancun_detail_image");
        }
    }

    @Override
    public void initView() {
        setContentView(R.layout.select_label_actvity);
        //初始化黄牛刀
        ButterKnife.bind(this);
        isShowTitle(false);
        try {
            Contacts.VersionName = Contacts.getLocalVersionName(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Contacts.brand = android.os.Build.BRAND + "(" + android.os.Build.MODEL + ")";
        if (hotLabelList.size() > 0) {
            hotLabelList.clear();
        }
        if (useLabelList.size() > 0) {
            useLabelList.clear();
        }
        getHotClass();
        getUsersUsedClass();
        getiniteView();
        getListener();
    }

    @Override
    public void initonResume() {

    }

    @Override
    public void initonPause() {

    }

    private void getiniteView() {
        String selectStr = SharedPreferencesHelper.get(getApplicationContext(), "selectStr", "").toString();
        String selectStrName = SharedPreferencesHelper.get(getApplicationContext(), "selectStrName", "").toString();
        try {
            if (flag.equals("3") || flag.equals("0")) {
                if (chuan_biaoqian_list.size() == 0) {
                    strName = SharedPreferencesHelper.String2SceneList(selectStrName);
                    str = SharedPreferencesHelper.String2SceneList(selectStr);
                }
            } else if (flag.equals("4")) {//从添加图片页进来的
                if (AddImagesActivity.selectStrName.length() > 0) {
                    Log.e("123", "          " + AddImagesActivity.selectStrName);
                    strName = SharedPreferencesHelper.String2SceneList(AddImagesActivity.selectStrName);
                }
                if (AddImagesActivity.selectStr.length() > 0) {
                    Log.e("123", "          " + AddImagesActivity.selectStr);
                    str = SharedPreferencesHelper.String2SceneList(AddImagesActivity.selectStr);
                }
                fabu_btn.setText("保存");

            } else if (flag.equals("5")) {//从添加视频页进来的
                if (PlayerActivity.selectStrName.length() > 0) {
                    strName = SharedPreferencesHelper.String2SceneList(PlayerActivity.selectStrName);
                }
                if (PlayerActivity.selectStr.length() > 0) {
                    str = SharedPreferencesHelper.String2SceneList(PlayerActivity.selectStr);
                }
                fabu_btn.setText("保存");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        linearLayoutManager = new LinearLayoutManager(SelectLabelActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        select_recyclerView.setHasFixedSize(true);
        select_recyclerView.setItemAnimator(null);
        select_recyclerView.setLayoutManager(linearLayoutManager);

        selectLabelAdapter = new SelectLabelAdapter(SelectLabelActivity.this, handler);
        useLabelAdapter = new UseLabelAdapter(SelectLabelActivity.this, handler);
        //热门标签
        hotGridLayoutManager = new GridLayoutManager(this, 3);
        hot_recyclerView.setLayoutManager(hotGridLayoutManager);
        hotLabelAdapter = new HotLabelAdapter(SelectLabelActivity.this, handler);
        hotLabelAdapter.setSelectList(hotLabelList);
        hot_recyclerView.setAdapter(hotLabelAdapter);

        if (chuan_biaoqian_list.size() > 0) {
            for (int i = 0; i < chuan_biaoqian_list.size(); i++) {
                str.add(chuan_biaoqian_list.get(i).getId());
                strName.add(chuan_biaoqian_list.get(i).getTitle());
            }
            select_label_image.setVisibility(View.GONE);
            select_recyclerView.setVisibility(View.VISIBLE);
            select_label_layout.setVisibility(View.VISIBLE);
            selectLabelAdapter.setSelectList(strName);
        } else {
            if (strName.size() > 0) {
                select_label_layout.setVisibility(View.VISIBLE);
                selectLabelAdapter.setSelectList(strName);
                select_label_image.setVisibility(View.GONE);
                select_recyclerView.setVisibility(View.VISIBLE);
            } else {
                select_label_image.setVisibility(View.VISIBLE);
                select_recyclerView.setVisibility(View.GONE);
            }
        }
        select_recyclerView.setAdapter(selectLabelAdapter);

        iamge_back.setOnClickListener(this);
        shanchu_btn.setOnClickListener(this);
        RxUtils.clickView(fabu_btn)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Properties prop = new Properties();
                        prop.setProperty("name", "fabu_btn");
                        StatService.trackCustomKVEvent(SelectLabelActivity.this, "Put_questions_publish", prop);
                        if (enter_select_add_images.equals("enter_select_add_images")) {//从添加图片页面进来的
                            if (data.size() > 0 && isData == false) {
                                try {
                                    SharedPreferencesHelper.remove(getApplicationContext(), "selectStr");
                                    SharedPreferencesHelper.remove(getApplicationContext(), "selectStrName");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                isData = true;
                            }
                            try {
                                select_strName = SharedPreferencesHelper.SceneList2String(strName);
                                //存储发送后台的数据
                                select_str = SharedPreferencesHelper.SceneList2String(str);
                                AddImagesActivity.selectStr = select_str.replace(" ", "");
                                AddImagesActivity.selectStrName = select_strName.replace(" ", "");

                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if ("".equals(SharedPreferencesHelper.get(SelectLabelActivity.this, "login_token", ""))) {
                                Intent intent = new Intent(SelectLabelActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                //传递过去标签
                                AddImagesActivity.classification_name = update_biaoqian_names_Image();
                                AddImagesActivity.classification_id = update_biaoqian_patams_image();
                                finish();
                            }

                        } else if (enter_select_add_images.equals("enter_player_activity")) {//从添加视频页面进来的
                            if (data.size() > 0 && isData == false) {
                                try {
                                    SharedPreferencesHelper.remove(getApplicationContext(), "selectStr");
                                    SharedPreferencesHelper.remove(getApplicationContext(), "selectStrName");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                isData = true;
                            }

                            try {
                                select_strName = SharedPreferencesHelper.SceneList2String(strName);
                                //存储发送后台的数据
                                select_str = SharedPreferencesHelper.SceneList2String(str);
                                PlayerActivity.selectStr = select_str.replace(" ", "");
                                PlayerActivity.selectStrName = select_strName.replace(" ", "");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if ("".equals(SharedPreferencesHelper.get(SelectLabelActivity.this, "login_token", ""))) {
                                Intent intent = new Intent(SelectLabelActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                //传递过去标签
                                PlayerActivity.classification_name = update_biaoqian_names_Image();
                                PlayerActivity.classification_id = update_biaoqian_patams_image();
                                finish();
                            }
                        } else {//不为空证明是从添加图片页面进来的
                            if (data.size() > 0 && isData == false) {
                                try {
                                    SharedPreferencesHelper.remove(getApplicationContext(), "selectStr");
                                    SharedPreferencesHelper.remove(getApplicationContext(), "selectStrName");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                isData = true;
                            }
                            if ("".equals(SharedPreferencesHelper.get(SelectLabelActivity.this, "login_token", ""))) {
                                Intent intent = new Intent(SelectLabelActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                //代表是保存编辑草稿箱数据
                                if (flag != null) {
                                    if (flag.equals("3")) {
                                        edit_caogao_question();
                                    } else if (flag.equals("1")) {
                                        update();
                                    } else {
                                        tijiao();
                                    }
                                }
                            }
                        }
                    }
                });
    }


    private void getListener() {
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 文字改变时 回掉此方法
//                Toast.makeText(SelectLabelActivity.this, "文字改变时 回掉此方法", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 文字改变之前 回掉此方法
//                Toast.makeText(SelectLabelActivity.this, "文字改变之前 回掉此方法", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 文字改变之后 回掉此方法
//                Toast.makeText(SelectLabelActivity.this, "文字改变之后 回掉此方法   " + s.toString(), Toast.LENGTH_SHORT).show();
                tv_shiyong.setVisibility(View.GONE);
                searchKey = s.toString();
                if (labels.size() > 0) {
                    labels.clear();
                }
                if (!TextUtils.isEmpty(s.toString())) {
                    PAGE = 0;
                    islabel = "0";
                    hot_label_layout.setVisibility(View.GONE);
                    use_label_layout.setVisibility(View.GONE);
                    recycler_layout.setVisibility(View.VISIBLE);
                    init_net_search(s.toString(), false);
                } else {
                    hot_label_layout.setVisibility(View.VISIBLE);
                    use_label_layout.setVisibility(View.VISIBLE);
                    recycler_layout.setVisibility(View.GONE);
                }
            }
        });

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    tv_shiyong.setVisibility(View.GONE);
                    if (labels.size() > 0) {
                        labels.clear();
                    }
                    PAGE = 0;
                    if (!TextUtils.isEmpty(searchKey)) {
                        islabel = "0";
                        hot_label_layout.setVisibility(View.GONE);
                        use_label_layout.setVisibility(View.GONE);
                        recycler_layout.setVisibility(View.VISIBLE);
                        init_net_search(searchKey, true);
                    } else {
                        hot_label_layout.setVisibility(View.VISIBLE);
                        use_label_layout.setVisibility(View.VISIBLE);
                        recycler_layout.setVisibility(View.GONE);
                    }
                    return true;
                }
                return false;
            }
        });

        //已选标签
//        selectLabelAdapter.setOnItemClickListener(new SelectLabelAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                for (int i = 0; i < labels.size(); i++) {
//                    if (labels.get(i).getTitle().equals(strName.get(position)) || labels.get(i).getId().equals(strName.get(position))) {
//                        labels.get(i).setCheck(false);
//                    }
//                }
//                for (int i = 0; i < data.size(); i++) {
//                    if (!TextUtils.isEmpty(data.get(i).getTitle())) {
//                        if (data.get(i).getTitle().equals(strName.get(position))) {
//                            data.remove(data.get(i));
//                        }
//                    }
//                }
//                for (int i = 0; i < hotLabelList.size(); i++) {
//                    if (hotLabelList.get(i).getTitle() != null && hotLabelList.get(i).getClass_id() != null) {
//                        if (hotLabelList.get(i).getTitle().equals(strName.get(position)) || hotLabelList.get(i).getClass_id().equals(strName.get(position))) {
//                            hotLabelList.get(i).setSelect(false);
//                        }
//                    }
//                }
//                for (int i = 0; i < useLabelList.size(); i++) {
//                    if (useLabelList.get(i).getTitle() != null && useLabelList.get(i).getId() != null) {
//                        if (useLabelList.get(i).getTitle().equals(strName.get(position)) || useLabelList.get(i).getId().equals(strName.get(position))) {
//                            useLabelList.get(i).setSelect(false);
//                        }
//                    }
//                }
//                Log.e("123", "       str.size() --- 2 ----       " + str.size());
//                str.remove(position);
//                strName.remove(position);
//                selectLabelAdapter.notifyDataSetChanged();
//                if (labels.size() > 0) {
//                    labelSearchAdapter.notifyDataSetChanged();
//                }
//                if (hotLabelList.size() > 0) {
//                    hotLabelList.clear();
//                }
//                if (useLabelList.size() > 0) {
//                    useLabelList.clear();
//                }
//                getHotClass();
//                getUsersUsedClass();
////                if (strName.size() <= 0) {
////                    select_label_layout.setVisibility(View.GONE);
////                }
//            }
//        });
    }


    @Override
    public void doBusiness(Context mContext) {
    }

    private boolean isData;
    String list_str = null;
    String select_str = null;
    String select_strName = null;

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.iamge_back:
                Properties prop = new Properties();
                prop.setProperty("name", "iamge_back");
                StatService.trackCustomKVEvent(SelectLabelActivity.this, "Put_questions_return", prop);

                Log.e("123", "      data.size()    " + data.size() + "     isData     " + isData);
                if (data.size() > 0 && isData == false) {
                    try {
//                        //存储选中的
                        select_strName = SharedPreferencesHelper.SceneList2String(strName);
                        Log.e("123", "      select_strName    " + select_strName);
                        SharedPreferencesHelper.put(SelectLabelActivity.this, "selectStrName", select_strName);
                        //存储发送后台的数据
                        select_str = SharedPreferencesHelper.SceneList2String(str);
                        SharedPreferencesHelper.put(SelectLabelActivity.this, "selectStr", select_str);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isData = true;
                }
                searchKey = "";
                finish();
                break;
            case R.id.shanchu_btn:
                if (et_search.getText().toString().length() > 0) {
                    et_search.setText("");
                    if (labels.size() > 0) {
                        labels.clear();
                    }
                    hot_label_layout.setVisibility(View.VISIBLE);
                    use_label_layout.setVisibility(View.VISIBLE);
                    recycler_layout.setVisibility(View.GONE);
                    labelSearchAdapter.notifyDataSetChanged();
                }
                break;
        }
    }


    /**
     * 标签转1,2，3
     *
     * @return
     */
    private String update_biaoqian_patams() {

        String strlist = stringFilter(str.toString());
        int len = strlist.length();
        String ids = strlist.substring(0, len).replace(" ", ",");//"keyids":”1,2,3”
        return ids;
    }

    private String update_biaoqian_patams_image() {

        String strlist = stringFilter(str.toString());
        int len = strlist.length();
        String ids = strlist.substring(0, len).replace(" ", ",");//"keyids":”1,2,3”
        return ids;
    }

    private String update_biaoqian_patams(String str) {
        String strlist = stringFilter(str.toString());
        int len = strlist.length() - 1;
        String ids = strlist.substring(1, len).replace("", "");//"keyids":”1,2,3”
        return ids;
    }

    /**
     * 标签名称
     *
     * @return
     */
    private String update_biaoqian_names() {
        String strlist = stringFilter(strName.toString());
        int len = strlist.length() - 1;
        String ids = strlist.substring(1, len).replace("", "");//"keyids":”1,2,3”
        return ids;
    }

    private String update_biaoqian_names_Image() {
        String strlist = stringFilter(strName.toString());
        int len = strlist.length();
        String ids = strlist.substring(0, len).replace(" ", ",");//"keyids":”1,2,3”
        return ids;
    }

    public static String stringFilter(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    //是否添加新标签
    public static String islabel = "0";

    //搜索标签
    private void init_net_search(String searchkey, boolean is_show_dialog) {
        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(PAGE));
        params.put("keyword", searchkey.replace(" ", ""));
        Log.e("搜索标签", params.toString());
        subscription = Network.getInstance("搜索标签", SelectLabelActivity.this)
                .search_class_request(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<Label>>() {
                            @Override
                            public void onNext(Bean<Label> result) {
                                Log.e("123", "      搜索标签         " + result.getMsg()
                                        + "        " + result.getCode());
                                if (labels.size() > 0 && PAGE > 0) {//这表示是"加载"
                                    if (result.getData().getData().size() == 0) {
                                        //表示没有更多数据了
                                        labels.addAll(result.getData().getData());
                                        labelSearchAdapter.notifyDataSetChanged();
                                    } else {
                                        if (strName.size() > 0) {
                                            for (int j = 0; j < strName.size(); j++) {
                                                for (int i = 0; i < result.getData().getData().size(); i++) {
                                                    if (result.getData().getData().get(i).getTitle().equals(strName.get(j))) {
                                                        result.getData().getData().get(i).setCheck(true);
                                                    }
                                                    if (result.getData().getData().get(i).getTitle().equals(searchKey)) {
                                                        islabel = "1";
                                                    }
                                                    labels.add(result.getData().getData().get(i));
                                                }
                                            }
                                        } else {
                                            for (int i = 0; i < result.getData().getData().size(); i++) {
                                                if (result.getData().getData().get(i).getTitle().equals(searchKey)) {
                                                    islabel = "1";
                                                }
                                                labels.add(result.getData().getData().get(i));
                                            }
                                        }
                                        labelSearchAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    if (strName.size() > 0) {
                                        for (int j = 0; j < strName.size(); j++) {
                                            for (int i = 0; i < result.getData().getData().size(); i++) {
                                                if (result.getData().getData().get(i).getTitle().equals(strName.get(j))) {
                                                    result.getData().getData().get(i).setCheck(true);
                                                }
                                                if (result.getData().getData().get(i).getTitle().equals(searchKey)) {
                                                    islabel = "1";
                                                }
                                                labels.add(result.getData().getData().get(i));
                                            }
                                        }
                                    } else {
                                        for (int i = 0; i < result.getData().getData().size(); i++) {
                                            if (result.getData().getData().get(i).getTitle().equals(searchKey)) {
                                                islabel = "1";
                                            }
                                            labels.add(result.getData().getData().get(i));
                                        }
                                    }
                                    initBiaoQians(labels, "2", islabel);
                                }
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("错误:  ", error);
                                if (labels.size() == 0) {
                                    islabel = "0";
                                    Label.DataBean data = new Label.DataBean();
                                    data.setCheck(false);
                                    data.setDesc("");
                                    data.setFollow("");
                                    data.setId("");
                                    data.setPath("");
                                    data.setQuestions("");
                                    data.setTitle(searchKey.replace(" ", ""));
                                    labels.add(0, data);
                                    initBiaoQians(labels, "2", islabel);
                                }
                            }
                        }, SelectLabelActivity.this, is_show_dialog));
    }


    /***
     *获取热门标签
     */
    private void getHotClass() {
        Log.e("jskljfksdj", "jskdfjklds");
        subscription = Network.getInstance("获取热门标签", SelectLabelActivity.this)
                .get_hot_class_request(
                        new ProgressSubscriberNew<>(HotLabel.class, new GsonSubscriberOnNextListener<HotLabel>() {
                            @Override
                            public void on_post_entity(HotLabel hotLabel) {
                                if (hotLabelList.size() > 0) {
                                    hotLabelList.clear();
                                }
                                if (strName.size() > 0) {
                                    select_label_image.setVisibility(View.GONE);
                                    select_recyclerView.setVisibility(View.VISIBLE);
                                    for (HotLabel.DataBean item : hotLabel.getData()) {
                                        for (int j = 0; j < strName.size(); j++) {
                                            if (item.getTitle().equals(strName.get(j))) {
                                                String title = item.getTitle().replace(" ", "");//去掉所用空格
                                                item.setTitle(title);
                                                item.setSelect(true);
                                            }
                                            hotLabelList.add(item);
                                        }
                                    }
                                } else {
                                    select_label_image.setVisibility(View.VISIBLE);
                                    select_recyclerView.setVisibility(View.GONE);
                                    for (HotLabel.DataBean item : hotLabel.getData()) {
                                        String title = item.getTitle().replace(" ", "");//去掉所用空格
                                        item.setTitle(title);
                                        item.setSelect(false);
                                        hotLabelList.add(item);
                                    }
                                }
                                hotLabelAdapter.setSelectList(hotRemoveDuplicate(hotLabelList));
                                hot_recyclerView.setAdapter(hotLabelAdapter);
                                hotLabelAdapter.notifyDataSetChanged();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Logger.e("获取热门标签错误：" + error);
                            }
                        }, SelectLabelActivity.this, false));
    }

    /***
     *用户常见标签列表
     */
    private void getUsersUsedClass() {
        subscription = Network.getInstance("用户常见标签列表", SelectLabelActivity.this)
                .get_users_used_request(
                        new ProgressSubscriberNew<>(UseLabel.class, new GsonSubscriberOnNextListener<UseLabel>() {
                            @Override
                            public void on_post_entity(UseLabel useLabel) {
                                Log.e("123", "       用户常见标签列表      " + useLabel.getData().size());
                                if (useLabel.getData().size() == 0) {
                                    use_label_layout.setVisibility(View.GONE);
                                } else {
                                    use_label_layout.setVisibility(View.VISIBLE);
                                }
                                if (useLabelList.size() > 0) {
                                    useLabelList.clear();
                                }
                                if (strName.size() > 0) {
                                    select_label_image.setVisibility(View.GONE);
                                    select_recyclerView.setVisibility(View.VISIBLE);
                                    for (UseLabel.DataBean item : useLabel.getData()) {
                                        for (int j = 0; j < strName.size(); j++) {
                                            if (!TextUtils.isEmpty(item.getTitle())) {
                                                if (item.getTitle().equals(strName.get(j))) {
//                                                    String title = item.getTitle().replace(" ", "");//去掉所用空格
//                                                    item.setTitle(title);
                                                    item.setSelect(true);
                                                }
                                                useLabelList.add(item);
                                            }
                                        }
                                    }
                                } else {
                                    select_label_image.setVisibility(View.VISIBLE);
                                    select_recyclerView.setVisibility(View.GONE);
                                    for (UseLabel.DataBean item : useLabel.getData()) {
//                                        String title = item.getTitle().replace(" ", "");//去掉所用空格
//                                        item.setTitle(title);
                                        item.setSelect(false);
                                        useLabelList.add(item);
                                    }
                                }
                                use_label_layout.setVisibility(View.VISIBLE);
                                //常用标签
                                useLabelAdapter.setList(useRemoveDuplicate(useLabelList));
                                creatTabGv.setAdapter(useLabelAdapter);
                                useLabelAdapter.notifyDataSetChanged();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Logger.e("用户常见标签列表错误：" + error);
                            }
                        }, SelectLabelActivity.this, false));
    }

    /**
     * 修改草稿
     */

    private void edit_caogao_question() {
        Map<String, String> params = new HashMap<>();
        params.put("title", title);
        params.put("img_data", img_data);
        params.put("content", content);
        params.put("classification_id", update_biaoqian_patams());
        params.put("draft_id", question_id);
        params.put("app_version", Contacts.VersionName);
        params.put("controller", "2");
        params.put("phone_version", Contacts.brand);
        Log.e("保存编辑草稿数据", params.toString());
        subscription = Network.getInstance("消息页面获取通知界面", SelectLabelActivity.this)
                .fabu_edit_caogao(params,
                        new ProgressSubscriberNew<>(String.class, new GsonSubscriberOnNextListener<String>() {
                            @Override
                            public void on_post_entity(String string) {
                                Toast.makeText(getApplicationContext(), "发布成功!", Toast.LENGTH_SHORT).show();
                                is_net_success = true;
                                Logger.e("提交问题数据成功：");
                                //直接根据返回的ID进来数据详情j
//                                Intent intent = new Intent(SelectLabelActivity.this, FenLeiQuestionDetailActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("wenti_id", string);
//                                intent.putExtras(bundle);
//                                startActivity(intent);
                                if (data.size() > 0) {
                                    data.clear();
                                }
                                if (str.size() > 0) {
                                    str.clear();
                                }
                                if (strName.size() > 0) {
                                    strName.clear();
                                }
                                finish();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(SelectLabelActivity.this, error, Toast.LENGTH_SHORT).show();
                                Logger.e("消息页面获取通知界面报错：" + error);
                            }
                        }, SelectLabelActivity.this, true));
    }


    //更新
    private void update() {
        Map<String, String> params = new HashMap<>();
        params.put("title", title);
        params.put("content", content);
        if (update_biaoqian_patams().equals("")) {
            Toast.makeText(getApplicationContext(), "标签不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else {
            params.put("classification_id", update_biaoqian_patams());
        }
        params.put("img_data", img_data);
        params.put("question_id", question_id);
        Log.e("更新问题的参数", params.toString());
        subscription = Network.getInstance("更新问题数据", getApplicationContext())
                .update_quesiont(params,
                        new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                            @Override
                            public void on_post_entity(List list) {
                                is_net_success = true;
                                Logger.e("更新问题数据成功：");
                                //直接根据返回的ID进来数据详情
                                Intent intent = new Intent(SelectLabelActivity.this, FenLeiQuestionDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("wenti_id", question_id);
                                bundle.putString("flag", flag);
                                bundle.putParcelableArrayList("huancun_detail_image", huancun_detail_image);
                                Log.e("从标签页传过去的缓存list", huancun_detail_image.size() + "");
                                intent.putExtras(bundle);
                                startActivity(intent);

                                sharedPreferensListHelper = new SharedPreferensListHelper(getApplicationContext(), question_id);
                                //把上传成功的网络图片地址保存在本地
                                sharedPreferensListHelper.setDataList(question_id, upload_iamge_paths);
                                //销毁已经缓存的标签
                                SharedPreferencesHelper.remove(getApplicationContext(), "finish_huancun_list");
                                SharedPreferencesHelper.remove(getApplicationContext(), "biaoqian_list");
                                SharedPreferencesHelper.put(getApplicationContext(), "biaoqian_list_deltete", "false");
                                if (data.size() > 0) {
                                    data.clear();
                                }
                                if (str.size() > 0) {
                                    str.clear();
                                }
                                if (strName.size() > 0) {
                                    strName.clear();
                                }
                                finish();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(SelectLabelActivity.this, error, Toast.LENGTH_SHORT).show();
                                Logger.e("更新问题数据失败：" + error);
                            }
                        }, this, true));
    }


    private void tijiao() {
        Map<String, String> params = new HashMap<>();
        params.put("title", title);//上传标题
        params.put("content", content);//上传内容
        if (update_biaoqian_patams().equals("")) {
            Toast.makeText(getApplicationContext(), "标签不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else {
            params.put("classification_id", update_biaoqian_patams());//上传标签
        }
        params.put("img_data", img_data);//上传图片
        params.put("app_version", Contacts.VersionName);
        params.put("controller", "2");
        params.put("phone_version", Contacts.brand);
        Log.e("提交问题数据参数", params.toString());
        subscription = Network.getInstance("提交问题数据", getApplicationContext())
                .add_quesiont(params,
                        new ProgressSubscriberNew<>(String.class, new GsonSubscriberOnNextListener<String>() {
                            @Override
                            public void on_post_entity(String numberSource) {
                                is_net_success = true;
                                Logger.e("提交问题数据成功：");
                                //直接根据返回的ID进来数据详情
                                Intent intent = new Intent(SelectLabelActivity.this, FenLeiQuestionDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("wenti_id", Double.valueOf(numberSource).intValue() + "");
                                bundle.putParcelableArrayList("huancun_detail_image", huancun_detail_image);
                                Log.e("从标签页传过去的缓存list", huancun_detail_image.size() + "");
                                intent.putExtras(bundle);
                                startActivity(intent);
                                sharedPreferensListHelper = new SharedPreferensListHelper(getApplicationContext(), Double.valueOf(numberSource).intValue() + "");
                                //把上传成功的网络图片地址保存在本地
                                sharedPreferensListHelper.setDataList(numberSource, upload_iamge_paths);
                                data.clear();
                                str.clear();
                                strName.clear();
                                //埋点
                                Properties prop = new Properties();
                                prop.setProperty("name", "into_detail");
                                StatService.trackCustomKVEvent(getApplicationContext(), "Put_questions_details", prop);
                                finish();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(SelectLabelActivity.this, error, Toast.LENGTH_SHORT).show();
                                Logger.e("提交问题数据失败：" + error);
                            }
                        }, this, true));
    }

    /**
     * 初始化标签列表
     */
    private void initBiaoQians(List<Label.DataBean> labelsData, String type, String islabels) {
        if ("0".equals(islabels)) {
            if ("2".equals(type)) {
                if (labelsData.size() > 0) {
                    if (!labels.get(0).getTitle().equals(searchKey.replace(" ", ""))) {
                        Label.DataBean data = new Label.DataBean();
                        data.setCheck(false);
                        data.setDesc("");
                        data.setFollow("");
                        data.setId("");
                        data.setPath("");
                        data.setQuestions("");
                        data.setTitle(searchKey.replace(" ", ""));
                        labels.add(0, data);
                    }
                }
            }
        }
        recycler_layout.setVisibility(View.VISIBLE);
        layoutManager = new LinearLayoutManager(SelectLabelActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        //如果缓存的list不等于空
        labelSearchAdapter = new LabelSearchAdapter(SelectLabelActivity.this, handler);
        labelSearchAdapter.setLabels(StringUtils.removeDuplicate(labelsData));
        recyclerView.setAdapter(labelSearchAdapter);

        //设置刷新完成以后，headerview固定的时间
        xrefreshview.setSilenceLoadMore(true);
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);
        xrefreshview.setPullRefreshEnable(true);
        xrefreshview.setPullLoadEnable(true);
        xrefreshview.setAutoLoadMore(false);
        //给recycler_view设置底部加载布局
        xrefreshview.enableReleaseToLoadMore(true);
        xrefreshview.enableRecyclerViewPullUp(true);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
        xrefreshview.setPreLoadCount(10);
        if (labels.size() > 7) {
            xrefreshview.enableReleaseToLoadMore(true);
//            labelSearchAdapter.setCustomLoadMoreView(new XRefreshViewFooter(this));//加载更多
            xrefreshview.setLoadComplete(false);//显示底部
        } else {
            xrefreshview.enableReleaseToLoadMore(false);
            xrefreshview.setLoadComplete(true);//隐藏底部
        }

        xrefreshview.setOnRecyclerViewScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PAGE = 0;
                        //填写刷新数据的网络请求，一般page=1，List集合清空操作
                        if (!TextUtils.isEmpty(searchKey)) {
                            if (labels.size() > 0) {
                                labels.clear();
                            }
                            init_net_search(searchKey, false);
                        }
                        xrefreshview.stopRefresh();//刷新停止

                    }
                }, 500);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        PAGE = PAGE + 1;
                        //填写加载更多的网络请求，一般page++
                        init_net_search(searchKey, false);
                        //没有更多数据时候
                        xrefreshview.setLoadComplete(true);
                    }
                }, 1000);
            }
        });
    }

    //常用标签去除重复
    public static List<UseLabel.DataBean> useRemoveDuplicate(List<UseLabel.DataBean> list) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (!TextUtils.isEmpty(list.get(i).getId())) {
                    if (list.get(i).getId().equals(list.get(j).getId())) {
                        list.remove(i);
                        //下标会减1
                        i = i - 1;
                        break;
                    }
                }
            }
        }
        return list;
    }

    //热门标签去除重复
    public static List<HotLabel.DataBean> hotRemoveDuplicate(List<HotLabel.DataBean> list) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (list.get(i).getClass_id().equals(list.get(j).getClass_id())) {
                    list.remove(i);
                    //下标会减1
                    i = i - 1;
                    break;
                }
            }
        }
        return list;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 返回按键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (data.size() > 0 && isData == false) {
                try {
                    //存储选中的
                    select_strName = SharedPreferencesHelper.SceneList2String(strName);
                    SharedPreferencesHelper.put(getApplicationContext(), "selectStrName", select_strName);
                    //存储发送后台的数据
                    select_str = SharedPreferencesHelper.SceneList2String(str);
                    SharedPreferencesHelper.put(getApplicationContext(), "selectStr", select_str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isData = true;
            }
            searchKey = "";
            finish();
        }
        return true;
    }

}

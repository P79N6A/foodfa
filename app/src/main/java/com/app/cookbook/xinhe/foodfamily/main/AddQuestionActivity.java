package com.app.cookbook.xinhe.foodfamily.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSConstants;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.common.utils.IOUtils;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.application.MyApplication;
import com.app.cookbook.xinhe.foodfamily.main.db.NoteDao;
import com.app.cookbook.xinhe.foodfamily.main.db.entity.Note;
import com.app.cookbook.xinhe.foodfamily.main.entity.CategoryDateEn;
import com.app.cookbook.xinhe.foodfamily.main.entity.UploadImageEntity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.util.AndroidWorkaround;
import com.app.cookbook.xinhe.foodfamily.util.CommonUtil;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.DataCleanManager;
import com.app.cookbook.xinhe.foodfamily.util.DateUtils;
import com.app.cookbook.xinhe.foodfamily.util.DensityUtil;
import com.app.cookbook.xinhe.foodfamily.util.FileUtils;
import com.app.cookbook.xinhe.foodfamily.util.GlideEngine;
import com.app.cookbook.xinhe.foodfamily.util.SDCardUtil;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferensListHelper;
import com.app.cookbook.xinhe.foodfamily.util.StatusBarUtil;
import com.app.cookbook.xinhe.foodfamily.util.StringUtils;
import com.app.cookbook.xinhe.foodfamily.util.StringsHelper;
import com.app.cookbook.xinhe.foodfamily.util.Utils_android_P;
import com.app.cookbook.xinhe.foodfamily.util.richtextediter.QuestionRichTextEditText;
import com.app.cookbook.xinhe.foodfamily.util.toast.RunBeyToast;
import com.app.cookbook.xinhe.foodfamily.util.ui.ProgressUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.orhanobut.logger.Logger;
import com.seek.biscuit.Biscuit;
import com.seek.biscuit.CompressResult;
import com.seek.biscuit.OnCompressCompletedListener;
import com.tencent.stat.StatService;

import rx.android.schedulers.AndroidSchedulers;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import hei.permission.PermissionActivity;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;


public class AddQuestionActivity extends PermissionActivity implements View.OnClickListener {
    @BindView(R.id.et_new_title)
    EditText et_new_title;
    @BindView(R.id.quxiao_btn)
    TextView quxiao_btn;
    @BindView(R.id.xiayibu_btn)
    TextView xiayibu_btn;
    @BindView(R.id.jianpan_top_view)
    RelativeLayout jianpan_top_view;
    @BindView(R.id.lin_layout)
    LinearLayout lin_layout;

    private static final int REQUEST_CODE_CHOOSE = 23;
    private Note note;//笔记对象
    private int flag;//区分是新建笔记还是编辑笔记
    //添加后名称集合
    private List<String> str = new ArrayList();
    private Subscription subsLoading;
    private Subscription subsInsert;
    RelativeLayout.LayoutParams params;
    protected Subscription subscription;
    //提交图片
    private String uploud_iamge;
    ArrayList<UploadImageEntity> upload_iamge_paths = new ArrayList<>();
    private NoteDao noteDao;
    List<CategoryDateEn> chuan_biaoqian_list = new ArrayList<>();
    SharedPreferensListHelper sharedPreferensListHelper;
    ArrayList<UploadImageEntity> upload_image_net_path = new ArrayList<>();
    ArrayList<UploadImageEntity> huancun_list = new ArrayList<>();//需要传递到详情页的图片集合
    private String question_id = "";
    int current_length = 0;
    int tanchu_number = 0;

    SimpleDateFormat df;
    Date curDate;
    Date endDate;
    QuestionRichTextEditText et_new_content;
    private boolean is_chao_number = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        steepStatusBar();
        setContentView(R.layout.activity_new);


        //添加其他设置
        initOtherSetting();

        et_new_content = findViewById(R.id.et_new_content);

        try {
            Contacts.VersionName = Contacts.getLocalVersionName(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Contacts.brand = android.os.Build.BRAND + "(" + android.os.Build.MODEL + ")";
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        curDate = new Date(System.currentTimeMillis());
        noteDao = new NoteDao(this);
        note = new Note();
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", 0);//0新建，1编辑,3草稿
        if (null != intent.getStringExtra("question_id")) {
            question_id = intent.getStringExtra("question_id");
        }

        if (null != SharedPreferencesHelper.getObject(getApplicationContext(), "file_save_note", "note_save")) {
            final Note note1 = (Note) SharedPreferencesHelper.getObject(getApplicationContext(), "file_save_note", "note_save");
            et_new_content.post(new Runnable() {
                @Override
                public void run() {
                    et_new_content.clearAllLayout();
                    et_new_title.setText(note1.getTitle());
                    String str = StringUtils.stringFilterTwo(note1.getMcontent());
                    if (str.length() == 0) {

                    } else {
                        et_new_content.clearAllLayout();
                        showDataSync(str);
                    }

                    SharedPreferencesHelper.deleteObject(getApplicationContext(), "file_save_note", "note_save");
                }
            });
        } else {
            if (flag == 1) {//编辑问题
                //首先显示需要编辑的数据
                init_edit_resource();

                Bundle bundle = intent.getBundleExtra("data");
                //接受传过来的标签集合
                if (null != bundle.getParcelableArrayList("biaoqian_list")) {
                    chuan_biaoqian_list = bundle.getParcelableArrayList("biaoqian_list");
                }
                //接受缓存的图片合集
                sharedPreferensListHelper = new SharedPreferensListHelper(getApplicationContext(), String.valueOf(note.getGroupId()));
                //获取缓存的网络图片地址
                Gson gson = new Gson();
                if ("".equals(sharedPreferensListHelper.getDataList(String.valueOf(note.getGroupId())))) {
                } else {
                    String huancun_list_str = sharedPreferensListHelper.getDataList(String.valueOf(note.getGroupId()));
                    upload_image_net_path = gson.fromJson(huancun_list_str,
                            new TypeToken<ArrayList<UploadImageEntity>>() {
                            }.getType());
                }

            } else if (flag == 3) {//修改草稿
                init_edit_resource();
            } else {//新建问题
                StatService.trackBeginPage(getApplicationContext(), "Question_time");
            }
        }


        //进入页面的时候，自动定位到第一个输入框
        et_new_title.setFocusable(true);
        et_new_title.setFocusableInTouchMode(true);
        et_new_title.requestFocus();
        et_new_title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    jianpan_top_view.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) et_new_content.getLayoutParams();
                    lp.bottomMargin = 0;
                    et_new_content.setLayoutParams(lp);

                } else {
                    jianpan_top_view.setVisibility(View.VISIBLE);
//                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) et_new_content.getLayoutParams();
//                    lp.bottomMargin = 130;
//                    et_new_content.setLayoutParams(lp);
                }
            }
        });
        //点击下一步
        xiayibu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "xiayibu_btn");
                StatService.trackCustomKVEvent(getApplicationContext(), "Put_questions_next_step", prop);
                if ("".equals(et_new_title.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "发布内容不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Intent intent1 = new Intent(AddQuestionActivity.this, SelectLabelActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", et_new_title.getText().toString());
                    bundle.putString("img_data", need_update_iamge_method());
                    if (is_chao_number) {//如果超过了字数限制
                        RunBeyToast runBeyToast = new RunBeyToast(getBaseContext(), 1000, "最多140个字"
                                , R.color.black, 255);
                        runBeyToast.show();
                        return;
                    } else {
                        bundle.putString("content", getEditData());
                    }
                    bundle.putString("flag", String.valueOf(flag));
                    upload_iamge_paths.addAll(upload_image_net_path);
                    bundle.putParcelableArrayList("upload_iamge_paths", upload_iamge_paths);
                    if ("true".equals(SharedPreferencesHelper.get(getApplicationContext(), "biaoqian_list_deltete", ""))) {
                    } else {
                        bundle.putParcelableArrayList("biaoqian_list", (ArrayList<? extends Parcelable>) chuan_biaoqian_list);
                    }
                    if (null != note) {
                        bundle.putString("question_id", note.getGroupId() + "");
                    }
                    bundle.putParcelableArrayList("huancun_detail_image", huancun_list);
                    intent1.putExtras(bundle);
                    startActivity(intent1);
                }
            }
        });

        //取消回到上一个页面
        quxiao_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "quxiao_btn");
                StatService.trackCustomKVEvent(getApplicationContext(), "Put_questions_cancel", prop);

                if ("".equals(et_new_title.getText().toString())) {
                    finish();
                } else {
                    select_save_popwindow();
                }
            }
        });

        setTextChanged();
        getEditChangedListener();

        et_new_content.setOnWrithClickListener(new QuestionRichTextEditText.OnWrithClickListener() {
            @Override
            public void onwhrite(EditText editText, Editable editable) {
                current_length = 0;
                List<QuestionRichTextEditText.EditData> editList = et_new_content.buildEditData();
                for (int i = 0; i < editList.size(); i++) {
                    QuestionRichTextEditText.EditData itemData = editList.get(i);
                    if (itemData.inputStr != null) {
                        current_length = current_length + itemData.inputStr.length();
                    }
                }

                if (current_length >= 141) {
                    is_chao_number = true;
                    int last_index = editText.getText().toString().length() - 1;
                    if (tanchu_number == 0) {
                        //Toast.makeText(getApplicationContext(), "上限140个字", Toast.LENGTH_SHORT).show();
                        RunBeyToast runBeyToast = new RunBeyToast(getBaseContext(), 1000, "最多140个字"
                                , R.color.black, 255
                        );
                        runBeyToast.show();
                        tanchu_number = 1;
                    }
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, last_index);
                    editText.setText(newStr);
                    editable = editText.getText();
                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                }

                if (current_length < 140) {
                    is_chao_number = false;
                    tanchu_number = 0;
                }
            }

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //埋点
        Properties prop = new Properties();
        prop.setProperty("tiwen_time", "提问功能使用时长");
        StatService.trackCustomEndKVEvent(this, "Question_time", prop);
        //保存
        endDate = new Date(System.currentTimeMillis());
        long time_cha = (endDate.getTime() - curDate.getTime()) / 1000;
        if (time_cha > 30) {
            //保存异常退出时需要保存的数据
            Note note_save = new Note();
            note_save.setTitle(et_new_title.getText().toString() + "");
            note_save.setMcontent(getEditData() + " ");
            note_save.setGroupId(88888888);
            note_save.setGroupName("save_note");
            note_save.setType(2);
            note_save.setBgColor("#FFFFFF");
            note_save.setIsEncrypt(0);
            note_save.setCreateTime(DateUtils.date2string(new Date()));
            SharedPreferencesHelper.saveObject(getApplicationContext(), "file_save_note", "note_save", note_save);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferencesHelper.deleteObject(getApplicationContext(), "file_save_note", "note_save");

        //为了防止相册图片重复出现，删除缓存中的图片信息
        File file = new File(Environment.getExternalStorageDirectory() + "/images/");
        String image_str_size = DataCleanManager.getFormatSize(DataCleanManager.getFolderSize(file));
        if (image_str_size.equals("0k")) {
        } else {
            DataCleanManager.deleteFolderFile(Environment.getExternalStorageDirectory() + "/images/", true);
        }
    }

    /**
     * 设置相关监听器
     */
    private void setListener() {
        et_new_title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (null != event) {
                    return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
                } else {
                    return false;
                }
            }
        });


    }


    private void setTextChanged() {
        et_new_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 文字改变时 回掉此方法
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 文字改变之前 回掉此方法
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 文字改变之后 回掉此方法
                if (s.length() == 50) {
                    RunBeyToast runBeyToast = new RunBeyToast(getBaseContext(), 1000, "最多50个字"
                            , R.color.black, 255
                    );
                    runBeyToast.show();
                }
            }
        });
    }

    private void getEditChangedListener() {
        et_new_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jianpan_top_view.setVisibility(View.VISIBLE);
            }
        });

        //选择图片插入文本
        jianpan_top_view.setOnClickListener(this);
    }

    /**
     * 首先显示需要编辑的数据
     */
    private void init_edit_resource() {
        if (null != noteDao.queryNoteOne(Integer.valueOf(question_id))) {
            note = noteDao.queryNoteOne(Integer.valueOf(question_id));
            et_new_title.setText(note.getTitle());
            et_new_content.post(new Runnable() {
                @Override
                public void run() {
                    et_new_content.clearAllLayout();
                    String myContent = note.getMcontent();
                    Log.e("所发生的继父回家是的啦", myContent);
                    Log.e("是否是点击开发", note.getContent());

                    showDataSync(myContent + "");
                }
            });
        }
    }

    private void initOtherSetting() {
        //初始化黄牛刀
        ButterKnife.bind(this);
        setListener();
        //为了不重复回退到此界面
        MyApplication.addDestoryActivity(AddQuestionActivity.this, "TuWenActivity");

        /**为了适配华为屏幕软键盘*/
        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) jianpan_top_view.getLayoutParams();
            jianpan_top_view.setLayoutParams(lp);
        }

        /**动态设置距离状态栏高度*/
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lin_layout.getLayoutParams();
        if (Utils_android_P.hasNotchResult(getApplicationContext())) {//如果有刘海
            if ("HUAWEI".equals(android.os.Build.BRAND)) {
                lp.setMargins(10, 40 + Utils_android_P.set_add_height(), 10, DensityUtil.dip2px(getApplicationContext(), 26));
            } else {
                lp.setMargins(10, 40 + Utils_android_P.set_add_height(), 10, 0);
            }
        } else if ("Xiaomi".equals(android.os.Build.BRAND)) {//小米手机获取特定型号
            Log.e("小米8的型号", Build.MODEL);
            if (Build.MODEL.equals("MI 8")) {
                lp.setMargins(10, 140, 10, 0);
            } else {
                lp.setMargins(10, 70, 10, 0);
            }
        } else {
            if ("HUAWEI".equals(android.os.Build.BRAND)) {
                lp.setMargins(10, 70, 10, DensityUtil.dip2px(getApplicationContext(), 26));
            } else {
                lp.setMargins(10, 70, 10, 0);
            }
        }
        lin_layout.setLayoutParams(lp);
    }


    /**
     * 是否保存草稿
     */
    Dialog dialog;
    private void select_save_popwindow() {
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.select_save_layout, null);
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
        //设置逻辑交互
        String selectStrName = SharedPreferencesHelper.get(getApplicationContext(), "selectStrName", "").toString();
        try {
            str = SharedPreferencesHelper.String2SceneList(selectStrName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        LinearLayout select_save = view.findViewById(R.id.select_save);
        select_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(SharedPreferencesHelper.get(AddQuestionActivity.this, "login_token", ""))) {
                    Intent intent = new Intent(AddQuestionActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    //删除草稿缓存标签
                    SharedPreferencesHelper.remove(getApplicationContext(), "selectStr");
                    SharedPreferencesHelper.remove(getApplicationContext(), "selectStrName");
                    if (flag == 3) {//编辑草稿
                        edit_caogao();
                    } else {//保存草稿
                        save_caogao();
                    }
                    dialog.dismiss();
                }
            }
        });
        LinearLayout select_no = view.findViewById(R.id.select_no);
        select_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesHelper.remove(getApplicationContext(), "selectStr");
                SharedPreferencesHelper.remove(getApplicationContext(), "selectStrName");
                dialog.dismiss();
                finish();
            }
        });
        LinearLayout select_dismiss = view.findViewById(R.id.select_dismiss);
        select_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void edit_caogao() {
        Map<String, String> params = new HashMap<>();
        params.put("title", et_new_title.getText().toString());
        params.put("img_data", need_update_iamge_method());
        params.put("content", getEditData());
        params.put("id", question_id);
        params.put("classification_id", update_biaoqian_patams());
        params.put("app_version", Contacts.VersionName);
        params.put("controller", "2");
        params.put("phone_version", Contacts.brand);
        Log.e("编辑草稿数据参数", params.toString());
        subscription = Network.getInstance("消息页面获取通知界面", AddQuestionActivity.this)
                .save_edit_caogao(params,
                        new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                            @Override
                            public void on_post_entity(List s) {
                                Logger.e("编辑草稿数据成功：");
                                Toast.makeText(getApplicationContext(), "更新成功!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                finish();
                            }

                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Logger.e("编辑草稿数据报错：" + error);
                            }
                        }, this, true));
    }

    /**
     * 需要上传修改的图片
     *
     * @return
     */
    private String need_update_iamge_method() {
        StringBuffer image_buffer = new StringBuffer();
        List<QuestionRichTextEditText.EditData> editList = et_new_content.buildEditData();
        List<String> need_uploads = new ArrayList<>();
        List<UploadImageEntity> chuan_list = new ArrayList<>();
        for (int i = 0; i < editList.size(); i++) {
            QuestionRichTextEditText.EditData itemData = editList.get(i);
            if (itemData.imagePath != null) {
                //缓存的图片和上传的图片合并
                yi_huancun_images.addAll(upload_iamge_paths);
                for (int k = 0; k < yi_huancun_images.size(); k++) {
                    if (flag == 1 || flag == 3) {
                        if (yi_huancun_images.get(k).getIamge_path().equals(itemData.imagePath) || yi_huancun_images.get(k).getUpload_iamge_path().equals(itemData.imagePath)) {
                            need_uploads.add(yi_huancun_images.get(k).getUpload_iamge_path());
                            //需要显示在下一个缓存的图片集合
                            chuan_list.add(yi_huancun_images.get(k));
                        }
                    } else {
                        if (yi_huancun_images.get(k).getIamge_path().equals(itemData.imagePath)) {
                            need_uploads.add(yi_huancun_images.get(k).getUpload_iamge_path());
                            //需要显示在下一个缓存的图片集合
                            chuan_list.add(yi_huancun_images.get(k));
                        }
                    }

                }
            }
        }
        //去除缓存中的重复数据
        List<UploadImageEntity> temp1 = new ArrayList<>();
        Iterator<UploadImageEntity> it1 = chuan_list.listIterator();
        while (it1.hasNext()) {
            Object o = it1.next();
            if (!temp1.contains(o)) { // 如果temp还没有这个元素，就添加
                temp1.add((UploadImageEntity) o);
            }
        }
        for (int i = 0; i < temp1.size(); i++) {
            huancun_list.add(temp1.get(i));
        }
        //去除重复数据
        List<String> temp = new ArrayList<String>();
        Iterator<String> it = need_uploads.listIterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (!temp.contains(o)) { // 如果temp还没有这个元素，就添加
                temp.add((String) o);
            }
        }
        for (int i = 0; i < temp.size(); i++) {
            if (i == temp.size() - 1) {
                image_buffer.append(temp.get(i));
            } else {
                image_buffer.append(temp.get(i)).append(",");
            }
        }
        return image_buffer.toString();
    }

    /**
     * 标签转1,2，3
     *
     * @return
     */
    private String update_biaoqian_patams() {
        String strlist = str.toString();
        int len = strlist.length() - 1;
        String ids = strlist.substring(1, len).replace("", "");//"keyids":”1,2,3”
        return ids;
    }

    private void save_caogao() {
        Map<String, String> params = new HashMap<>();
        params.put("title", et_new_title.getText().toString());
        params.put("img_data", need_update_iamge_method());
        params.put("content", getEditData());
        params.put("classification_id", update_biaoqian_patams());
        params.put("app_version", Contacts.VersionName);
        params.put("controller", "2");
        params.put("phone_version", Contacts.brand);
        Log.e("保存草稿数据参数", params.toString());
        subscription = Network.getInstance("保存草稿数据", getApplicationContext())
                .save_caogao(params,
                        new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                            @Override
                            public void on_post_entity(List list) {
                                Logger.e("保存草稿数据成功：");
                                Toast.makeText(getApplicationContext(), "保存草稿成功!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                finish();

                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Logger.e("保存草稿数据失败：" + error);
                            }
                        }, this, true));
    }

    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(AddQuestionActivity.this, R.color.white);
        } else {
            StatusBarUtil.transparencyBar(AddQuestionActivity.this);
        }

    }

    /**
     * 异步方式显示数据
     *
     * @param html
     */
    private void showDataSync(final String html) {
        //开始加载loading
        final ProgressUtil progressUtil = new ProgressUtil();
        progressUtil.showProgressDialog(AddQuestionActivity.this, "载入中...");
        subsLoading = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                showEditData(subscriber, html, getApplicationContext());
            }
        })
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        progressUtil.dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("接口的萨克雷", e.getMessage());
//                        Toast.makeText(getApplicationContext(), "解析错误：图片不存在或已损坏", Toast.LENGTH_SHORT).show();
                        Message msg = new Message();
                        msg.what = 401;
                        handlers.sendMessage(msg);
                        progressUtil.dismissProgressDialog();
                    }

                    @Override
                    public void onNext(String text) {
                        StringBuffer sb = new StringBuffer();
                        String[] strArray = text.split("\n");
                        for (String str : strArray) {
                            sb.append(Html.fromHtml(str));
                            sb.append("\n");
                        }
                        String content_str = sb.toString();
                        Log.e("数据库法律是", SDCardUtil.getPictureDir());
                        if (isImage(text)) {
                            Log.e("手机单", text);
                            et_new_content.addImageViewAtIndex(et_new_content.getLastIndex(), text);
                        } else {
                            Log.e("111手机单", text);
                            et_new_content.addEditTextAtIndex(et_new_content.getLastIndex(), content_str);
                        }

                    }
                });
    }

    private boolean isImage(String str) {
        if (str.endsWith(".jpg")) {
            return true;
        } else if (str.endsWith(".jpeg")) {
            return true;
        } else if (str.endsWith(".png")) {
            return true;
        } else if (str.endsWith(".gif")) {
            return true;
        } else if (str.endsWith(".bmp")) {
            return true;
        } else if (str.endsWith(".tiff")) {
            return true;
        } else if (str.endsWith(".ai")) {
            return true;
        } else if (str.endsWith(".cdr")) {
            return true;
        } else if (str.endsWith(".eps")) {
            return true;
        } else {
            return false;
        }
    }

    private Handler handlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 401:
                    Toast.makeText(getApplicationContext(), "解析错误：图片不存在或已损坏", Toast.LENGTH_SHORT).show();
                    break;
            }


        }
    };

    List<UploadImageEntity> yi_huancun_images = new ArrayList<>();
    /**
     * 显示数据
     */
    Bitmap mBitmap;
    String path;
    String imagePath;

    protected void showEditData(Subscriber<? super String> subscriber, String html, Context context) {
        try {
            List<String> textList = StringUtils.cutStringByImgTag(html);
            for (int i = 0; i < textList.size(); i++) {
                String text = textList.get(i);
                if (text.contains("<img")) {
                    imagePath = StringUtils.getImgSrc(text);
                    Log.e("打印出来的东东", imagePath);
                    if (null != imagePath) {
                        //先把图片缓存在本地，然后再传本地文件的路
                        mBitmap = getBitmap(imagePath);
                        path = SDCardUtil.saveToSdCard(mBitmap);
                        UploadImageEntity uploadImageEntity = new UploadImageEntity();
                        uploadImageEntity.setUpload_iamge_path(imagePath);
                        uploadImageEntity.setIamge_path(path);
                        yi_huancun_images.add(uploadImageEntity);
                        subscriber.onNext(imagePath);
                    } else {
                        Toast.makeText(context, "图片" + i + "已丢失，请重新插入！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // String content = text.replace("<p>", "").replace("</p>", "").replace("</br>", "").replace("\r\n", "");
                    String str = StringUtils.stringFilterTwo(text);
                    subscriber.onNext(str);
                }
                if (i == textList.size() - 1) {
                    subscriber.onNext("");//添加新的一行空格
                }
            }
            subscriber.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            subscriber.onError(e);
        }
    }

    public Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            HttpURLConnection http = (HttpURLConnection) conn;
            int length = http.getContentLength();
            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    /**
     * 提交到服务器的html文本
     */
    private String getEditData() {
        if (flag == 1) {//编辑
            List<QuestionRichTextEditText.EditData> editList = et_new_content.buildEditData();
            StringBuffer content = new StringBuffer();
            for (int i = 0; i < editList.size(); i++) {
                QuestionRichTextEditText.EditData itemData = editList.get(i);
                if (itemData.inputStr != null) {
                    if (itemData.inputStr.equals("") || itemData.inputStr.equals(" ")) {
                    } else {
                        String str = TextUtils.htmlEncode(itemData.inputStr).toString();
                        //Log.e("贾师傅简历上", str);
                        content.append("<p>").append(str).append("</p>");
                    }
                } else if (itemData.imagePath != null) {
                    yi_huancun_images.addAll(upload_iamge_paths);
                    for (int k = 0; k < yi_huancun_images.size(); k++) {
                        if (yi_huancun_images.get(k).getIamge_path().equals(itemData.imagePath) || yi_huancun_images.get(k).getUpload_iamge_path().equals(itemData.imagePath)) {
                            itemData.imagePath = "";
                            content.append("<img src=\"")
                                    .append(yi_huancun_images.get(k).getUpload_iamge_path()).append("\"/>");
                        }
                    }
                }
            }
            // String mcontent = content.toString().replace("\n\r", "</br>").replace("\n", "<br/>");
            String mcontent = content.toString().replace("\n\r", "<br/>").replace("\n", "<br/>").replace("<p></p>", "");
            String post_content = mcontent.replace("\n\r", "").replace("\n", "").replace(" ", "");
            if (post_content.equals("<p></p>")) {
                return "";
            }
            return mcontent;
        } else if (flag == 3) {//草稿箱
            List<QuestionRichTextEditText.EditData> editList = et_new_content.buildEditData();
            StringBuffer content = new StringBuffer();
            for (int i = 0; i < editList.size(); i++) {
                QuestionRichTextEditText.EditData itemData = editList.get(i);
                if (itemData.inputStr != null) {
                    if (itemData.inputStr.equals("") || itemData.inputStr.equals(" ")) {
                    } else {
                        String str = TextUtils.htmlEncode(itemData.inputStr).toString();
                        //Log.e("贾师傅简历上", str);
                        content.append("<p>").append(str).append("</p>");
                    }
                } else if (itemData.imagePath != null) {
                    yi_huancun_images.addAll(upload_iamge_paths);
                    for (int k = 0; k < yi_huancun_images.size(); k++) {
                        if (yi_huancun_images.get(k).getIamge_path().equals(itemData.imagePath) || yi_huancun_images.get(k).getUpload_iamge_path().equals(itemData.imagePath)) {
                            itemData.imagePath = "";
                            content.append("<img src=\"")
                                    .append(yi_huancun_images.get(k).getUpload_iamge_path()).append("\"/>");
                        }
                    }
                }
            }
            // String mcontent = content.toString().replace("\n\r", "</br>").replace("\n", "<br/>");
            String mcontent = content.toString().replace("\n\r", "<br/>").replace("\n", "<br/>").replace("<p></p>", "");
            String post_content = mcontent.replace("\n\r", "").replace("\n", "").replace(" ", "");
            if (post_content.equals("<p></p>")) {
                return "";
            }
            Log.e("保存草稿", mcontent);
            return mcontent;
        } else {//提交
            List<QuestionRichTextEditText.EditData> editList = et_new_content.buildEditData();
            StringBuffer content = new StringBuffer();
            for (int i = 0; i < editList.size(); i++) {
                QuestionRichTextEditText.EditData itemData = editList.get(i);
                if (itemData.inputStr != null) {
                    //content.append("<p>").append(StringUtils.stringFilter(itemData.inputStr)).append("</p>");
                    String str = TextUtils.htmlEncode(itemData.inputStr).toString();
                    //Log.e("贾师傅简历上", str);
                    content.append("<p>").append(str).append("</p>");
                } else if (itemData.imagePath != null) {
                    for (int j = 0; j < upload_iamge_paths.size(); j++) {
                        Log.e("数据库福建省的看来", itemData.imagePath);
                        if (upload_iamge_paths.get(j).getIamge_path().equals(itemData.imagePath)) {
                            content.append("<img src=\"")
                                    .append(upload_iamge_paths.get(j).getUpload_iamge_path()).append("\"/>");
                        }
                    }
                }
            }
            //为了去掉，英文字符"/"自动转化为"\n"的bug
            String mcontent = content.toString().replace("\n\r", "<br/>").replace("\n", "<br/>").replace("<p></p>", "");
            String post_content = mcontent.replace("\n\r", "").replace("\n", "").replace(" ", "");
            if (post_content.equals("<p></p>")) {
                return "";
            }
            return mcontent;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (RESULT_OK == resultCode) {
            //相机或相册回调
            if (requestCode == 101) {
                //返回对象集合：如果你需要了解图片的宽、高、大小、用户是否选中原图选项等信息，可以用这个
                ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                for (int i = 0; i < resultPhotos.size(); i++) {
                    Log.e("图片地址", resultPhotos.get(i).path);
                }
                //返回图片地址集合：如果你只需要获取图片的地址，可以用这个
                ArrayList<String> resultPaths = data.getStringArrayListExtra(EasyPhotos.RESULT_PATHS);
                //返回图片地址集合时如果你需要知道用户选择图片时是否选择了原图选项，用如下方法获取
                boolean selectedOriginal = data.getBooleanExtra(EasyPhotos.RESULT_SELECTED_ORIGINAL, false);

                insertImagesSync(resultPaths);

                return;
            }


        } else if (RESULT_CANCELED == resultCode) {
            Log.e("关闭了相册", "关闭了相册");
            //Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
        }
    }

    int iamge_list_length = 0;
    int images_index = 0;
    Subscriber<? super String> subscriber;
    Biscuit mBiscuit;

    private void insertImagesSync(final ArrayList<String> mselectedPhotoList) {
        progress_upload = new ProgressUtil();
        progress_upload.showProgressDialog(AddQuestionActivity.this, "载入中...");
        subsInsert = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> msubscriber) {
                subscriber = msubscriber;
                try {
                    et_new_content.measure(0, 0);
                    //循环压缩
                    mBiscuit = Biscuit.with(AddQuestionActivity.this)
                            .path(mselectedPhotoList) //可以传入一张图片路径，也可以传入一个图片路径列表
                            .loggingEnabled(true)//是否输出log 默认输出
                            //  .quality(50)//质量压缩值（0...100）默认已经非常接近微信，所以没特殊需求可以不用自定义
                            //     .originalName(false) //使用原图名字来命名压缩后的图片，默认不使用原图名字,随机图片名字
                            .listener(new OnCompressCompletedListener() {
                                @Override
                                public void onCompressCompleted(CompressResult compressResult) {
                                    List<String> paths = new ArrayList<>();
                                    paths.addAll(compressResult.mSuccessPaths);
                                    iamge_list_length = mselectedPhotoList.size();
                                    for (int i = 0; i < paths.size(); i++) {
                                        images_index = i;
                                        if (mselectedPhotoList.get(i).contains(".gif")) {//如果是动态图，不压缩
                                            //保存到压缩文件夹
                                            File file = new File(FileUtils.getImageDir());
                                            if (!file.exists()) {
                                                file.mkdir();
                                            }
                                            if (!TextUtils.isEmpty(FileUtils.getImageDir())) {
                                                String last = FileUtils.getImageDir().substring(FileUtils.getImageDir().length() - 1, FileUtils.getImageDir().length());
                                                if (!last.equals(File.separator)) {
                                                    throw new IllegalArgumentException("targetDir must be end with " + File.separator);
                                                }
                                            }
                                            Log.e("保存到的目录：", file.getAbsolutePath());
                                            //将文件复制到指定目录
                                            String s = mselectedPhotoList.get(i);
                                            String temp[] = s.replaceAll("\\\\", "/").split("/");
                                            String fileName = "";
                                            if (temp.length > 1) {
                                                fileName = temp[temp.length - 1];//获取图片的名称
                                                String new_path = copyFile(s, FileUtils.getImageDir() + System.currentTimeMillis() + fileName);
                                                subscriber.onNext(new_path);
                                            }
                                        } else {
                                            subscriber.onNext(paths.get(i));
                                        }
                                    }
                                }

                            })//压缩完成监听
                            .targetDir(FileUtils.getImageDir())//自定义压缩保存路径
//                              .executor(executor) //自定义实现执行，注意：必须在子线程中执行 默认使用AsyncTask线程池执行
                            //   .ignoreAlpha(true)//忽略alpha通道，对图片没有透明度要求可以这么做，默认不忽略。
                            //    .compressType(Biscuit.SAMPLE)//采用采样率压缩方式，默认是使用缩放压缩方式，也就是和微信的一样。
                            .ignoreLessThan(100)//忽略小于100kb的图片不压缩，返回原图路径
                            .build();
                    mBiscuit.asyncCompress();


                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }

        })
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e("咖啡机克鲁赛德", "图片插入完成");
                        et_new_content.addEditTextAtIndex(et_new_content.getLastIndex(), " ");
                        Toast.makeText(getApplicationContext(), "图片插入完成", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("空间环境是否会对", e.getMessage());
                        Toast.makeText(getApplicationContext(), "图片插入失败", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(String imagePath) {
                        Log.e("插入进去的图片", imagePath);
                        et_new_content.insertImage(imagePath, et_new_content.getMeasuredWidth(), iamge_list_length);
                        //请求阿里上传图片
                        //判断图片格式是否是gif
                        if (imagePath.contains(".gif")) {
                            upload_user_image("syjapppic", Network.bucketPath + "question/" + DateUtils.getNowYear() + "_" + DateUtils.getNowMonth() + "/" + StringsHelper.getUUID() + ".gif", imagePath);
                        } else {
                            upload_user_image("syjapppic", Network.bucketPath + "question/" + DateUtils.getNowYear() + "_" + DateUtils.getNowMonth() + "/" + StringsHelper.getUUID() + ".jpg", imagePath);

                        }

                    }
                });
    }


    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public String copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

        return newPath;

    }

    ProgressUtil progress_upload;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progress_upload = new ProgressUtil();
            progress_upload.showProgressDialog(AddQuestionActivity.this, "载入中...");
        }
    };


    /**
     * 上传图片到阿里服务器
     * * @param bucketName
     *
     * @param objectKey
     * @param uploadFilePath
     */
    private void upload_user_image(
            String bucketName
            , final String objectKey
            , final String uploadFilePath) {
        final ProgressUtil progressUtil = new ProgressUtil();
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        //动态获取token验证
        OSSCredentialProvider credentialProvider1 = new OSSFederationCredentialProvider() {
            @Override
            public OSSFederationToken getFederationToken() {
                String net_url = Network.main_url + "/aliyun/sts-servers/sts.php?userid=1";
                // 您需要在这里实现获取一个FederationToken，并构造成OSSFederationToken对象返回
                // 如果因为某种原因获取失败，可直接返回nil
                // 下面是一些获取token的代码，比如从您的server获取
                try {
                    URL stsUrl = new URL(net_url);
                    HttpURLConnection conn = (HttpURLConnection) stsUrl.openConnection();
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    Network.trustAllHttpsCertificates();//跳过ssl验证
                    InputStream input = conn.getInputStream();
                    String jsonText = IOUtils.readStreamAsString(input, OSSConstants.DEFAULT_CHARSET_NAME);
                    JSONObject jsonObjs = new JSONObject(jsonText);
                    String ak = jsonObjs.getString("AccessKeyId");
                    String sk = jsonObjs.getString("AccessKeySecret");
                    String token = jsonObjs.getString("SecurityToken");
                    String expiration = jsonObjs.getString("Expiration");
                    return new OSSFederationToken(ak, sk, token, expiration);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(9); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        //开启可以在控制台看到日志，并且会支持写入手机sd卡中的一份日志文件位置在SDCard_path\OSSLog\logs.csv  默认不开启
        //日志会记录oss操作行为中的请求数据，返回数据，异常信息
        //例如requestId,response header等
        //android_version：5.1  android版本
        //mobile_model：XT1085  android手机型号
        //network_state：connected  网络状况
        //network_type：WIFI 网络连接类型
        //具体的操作行为信息:
        //[2017-09-05 16:54:52] - Encounter local execpiton: //java.lang.IllegalArgumentException: The bucket name is invalid.
        //A bucket name must:
        //1) be comprised of lower-case characters, numbers or dash(-);
        //2) start with lower case or numbers;
        //3) be between 3-63 characters long.
        //------>end of log
        OSSLog.enableLog();//开启日志记录
        OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider1);

        // 构造上传请求(同步上传)
        PutObjectRequest put = new PutObjectRequest(
                bucketName,
                objectKey,
                uploadFilePath);


        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("图片当前上传进度：", "currentSize: " + currentSize + " 图片总上传进度: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.e("PutObject", "上传图片成功");
                //progressUtil.dismissProgressDialog();
                uploud_iamge = "https://syjapppic.oss-cn-hangzhou.aliyuncs.com/" + objectKey;
                Log.e("上传图片地址:", uploud_iamge);
                UploadImageEntity uploadImageEntity = new UploadImageEntity();
                uploadImageEntity.setIamge_path(uploadFilePath);
                uploadImageEntity.setUpload_iamge_path(uploud_iamge);
                upload_iamge_paths.add(uploadImageEntity);

                if (images_index == iamge_list_length - 1) {
                    subscriber.onCompleted();
                    progress_upload.dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
        Log.e("看来大家发送", task.isCompleted() + "");
        // task.cancel(); // 可以取消任务
        task.waitUntilFinished(); //可以等待直到任务完成

    }


    @Override
    protected void onStop() {
        super.onStop();
        //如果APP处于后台，或者手机锁屏，则启用密码锁
        if (CommonUtil.isAppOnBackground(getApplicationContext()) ||
                CommonUtil.isLockScreeen(getApplicationContext())) {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 返回按键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ("".equals(et_new_title.getText().toString())) {
                finish();
            } else {
                select_save_popwindow();
            }
        }
        return true;
    }

    private int max_num = 9;
    private int cureent_numl;

    @Override
    protected void onResume() {
        super.onResume();
        Properties prop = new Properties();
        prop.setProperty("tiwen_time", "提问功能使用时长");
        StatService.trackCustomBeginKVEvent(this, "Question_time", prop);
        //每次选完照片之后，重申最大选择数量
        max_num = 9;


    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.jianpan_top_view:
                //设置最多只能上传9张图片
                cureent_numl = 0;
                //遍历图片数量设置index
                List<QuestionRichTextEditText.EditData> editList = et_new_content.buildEditData();
                for (int i = 0; i < editList.size(); i++) {
                    QuestionRichTextEditText.EditData itemData = editList.get(i);
                    if (itemData.imagePath != null) {
                        cureent_numl++;
                    }
                }
                if (cureent_numl == max_num) {//已经满了9张
                    cureent_numl = 9;
                } else if (cureent_numl < max_num) {
                    max_num = max_num - cureent_numl;
                }

                if (cureent_numl == 9) {
                    Toast.makeText(getApplicationContext(), "只能上传9张图片哦～", Toast.LENGTH_SHORT).show();
                } else if (cureent_numl < 9) {
                    EasyPhotos.createAlbum(this, true, GlideEngine.getInstance())
                            .setFileProviderAuthority("com.app.cookbook.xinhe.foodfamily.fileprovider")
                            .setPuzzleMenu(false)
                            .setCount(max_num)
                            .setOriginalMenu(false, true, null)
                            .start(101);
                }


                break;
        }
    }

    @Override
    public void onBackPressed() {
        dealwithExit();
    }

    /**
     * 退出处理
     */
    private void dealwithExit() {
        Properties prop = new Properties();
        prop.setProperty("name", "quxiao_btn");
        StatService.trackCustomKVEvent(getApplicationContext(), "Put_questions_cancel", prop);

        if ("".equals(et_new_title.getText().toString())) {
            finish();
        } else {
            select_save_popwindow();
        }
    }
}

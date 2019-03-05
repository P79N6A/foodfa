package com.app.cookbook.xinhe.foodfamily.main;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextUtils;
import android.util.Log;
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
import com.app.cookbook.xinhe.foodfamily.main.db.NoteDao;
import com.app.cookbook.xinhe.foodfamily.main.db.entity.Note;
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
import com.app.cookbook.xinhe.foodfamily.util.SDCardUtil;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferensListHelper;
import com.app.cookbook.xinhe.foodfamily.util.StatusBarUtil;
import com.app.cookbook.xinhe.foodfamily.util.StringUtils;
import com.app.cookbook.xinhe.foodfamily.util.StringsHelper;
import com.app.cookbook.xinhe.foodfamily.util.Utils_android_P;
import com.app.cookbook.xinhe.foodfamily.util.richtextediter.RichTextEditor;
import com.app.cookbook.xinhe.foodfamily.util.ui.ProgressUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.lzy.imagepicker.bean.ImageItem;
import com.orhanobut.logger.Logger;
import com.seek.biscuit.Biscuit;
import com.seek.biscuit.CompressResult;
import com.seek.biscuit.OnCompressCompletedListener;
import com.tencent.stat.StatService;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class QuestionAnswerDetailActivity extends PermissionActivity implements View.OnClickListener {
    @BindView(R.id.et_new_content)
    RichTextEditor et_new_content;
    @BindView(R.id.quxiao_btn)
    TextView quxiao_btn;
    @BindView(R.id.xiayibu_btn)
    TextView xiayibu_btn;
    @BindView(R.id.jianpan_top_view)
    RelativeLayout jianpan_top_view;
    @BindView(R.id.question_tv)
    TextView question_tv;
    @BindView(R.id.lin_layout)
    LinearLayout lin_layout;

    private Note note = new Note();//笔记对象
    private String myContent;
    private String myGroupName;
    private int flag;//区分是新建笔记还是编辑笔记
    private String caogaoxiang_detail;
    private String answer_id;

    private Subscription subsLoading;
    private Subscription subsInsert;
    RelativeLayout.LayoutParams params;
    /**
     * Rxjava
     */
    protected Subscription subscription;
    //提交图片
    private String uploud_iamge;
    List<UploadImageEntity> upload_iamge_paths = new ArrayList<>();

    private NoteDao noteDao;
    private String question_id = "";
    ArrayList<UploadImageEntity> upload_image_net_path = new ArrayList<>();
    SharedPreferensListHelper sharedPreferensListHelper;
    int current_length = 0;
    int tanchu_number = 0;
    int last_index = 0;
    private boolean is_chao_number = false;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private static final int REQUEST_CODE = 0; //请求码
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    SimpleDateFormat df;
    Date curDate;
    Date endDate;
    private String question_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        steepStatusBar();
        setContentView(R.layout.activity_answer_detail);
        //初始化黄牛刀
        ButterKnife.bind(this);

        /**动态设置距离状态栏高度*/
        LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) lin_layout.getLayoutParams();
        if (Utils_android_P.hasNotchResult(getApplicationContext())) {//如果有刘海
            if ("HUAWEI".equals(android.os.Build.BRAND)) {
                lp1.setMargins(10, 40 + Utils_android_P.set_add_height(), 10, DensityUtil.dip2px(getApplicationContext(), 26));
            } else {
                lp1.setMargins(10, 40 + Utils_android_P.set_add_height(), 10, 0);
            }
        } else if ("Xiaomi".equals(android.os.Build.BRAND)) {//小米手机获取特定型号
            Log.e("小米8的型号", Build.MODEL);
            if (Build.MODEL.equals("MI 8")) {
                lp1.setMargins(10, 140, 10, 0);
            } else {
                lp1.setMargins(10, 70, 10, 0);
            }
        } else {
            if ("HUAWEI".equals(android.os.Build.BRAND)) {
                lp1.setMargins(10, 70, 10, DensityUtil.dip2px(getApplicationContext(), 26));

            } else {
                lp1.setMargins(10, 70, 10, 0);
            }
        }
        lin_layout.setLayoutParams(lp1);

        try {
            Contacts.VersionName = Contacts.getLocalVersionName(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Contacts.brand = android.os.Build.BRAND + "(" + android.os.Build.MODEL + ")";

        /**为了适配华为屏幕软键盘*/
        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) jianpan_top_view.getLayoutParams();
            jianpan_top_view.setLayoutParams(lp);
        }
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        curDate = new Date(System.currentTimeMillis());
        noteDao = new NoteDao(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        flag = Integer.valueOf(bundle.getString("flag"));//0新建，1编辑
        if (null != bundle.getString("caogaoxiang_detail")) {
            caogaoxiang_detail = bundle.getString("caogaoxiang_detail");
        } else {
            caogaoxiang_detail = "";
        }
        question_id = bundle.getString("question_id");
        question_title = bundle.getString("question_title");
        if (null != question_title) {
            question_tv.setText(question_title);
        }
        answer_id = bundle.getString("answer_id");
        //拿出来缓存的回答信息
        note = new Note();
        sharedPreferensListHelper = new SharedPreferensListHelper(getApplicationContext(), answer_id);
        //获取缓存的网络图片地址
        Gson gson = new Gson();
        //Log.e("获取已经缓存好的内容", sharedPreferensListHelper.getDataList(answer_id).toString());
        if ("".equals(sharedPreferensListHelper.getDataList(answer_id).toString())) {

        } else {
            String huancun_list_str = sharedPreferensListHelper.getDataList(answer_id).toString();
            upload_image_net_path = gson.fromJson(huancun_list_str,
                    new TypeToken<ArrayList<UploadImageEntity>>() {
                    }.getType());
//            if (upload_image_net_path.size() > 0) {
//                //在页面影藏的时候，进行数据的缓存
//                for (int i = 0; i < upload_image_net_path.size(); i++) {
//                    Log.e("获取缓存图片地址", upload_image_net_path.get(i).getUpload_iamge_path());
//                }
//            }
        }

        et_new_content = (RichTextEditor) findViewById(R.id.et_new_content);
        et_new_content.setOnWrithClickListener(new RichTextEditor.OnWrithClickListener() {
            @Override
            public void onwhrite(EditText editText, Editable editable) {
                current_length = 0;
                List<RichTextEditor.EditData> editList = et_new_content.buildEditData();
                for (RichTextEditor.EditData editData : editList) {
                    if (editData.inputStr != null) {
                        current_length = current_length + editData.inputStr.length();
                    }
                }
                if (tanchu_number == 0) {
                    if ((current_length + 1) == 30001) {
                        tanchu_number = 1;
                        is_chao_number = true;
                        last_index = editable.length() - 1;
                        Toast.makeText(getApplicationContext(), "最多30000个字", Toast.LENGTH_SHORT).show();
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
                }

                if (is_chao_number) {
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(last_index)});
                }
            }

        });

        sharedPreferensListHelper = new SharedPreferensListHelper(getApplicationContext(), answer_id);

        //选择图片插入文本
        jianpan_top_view.setOnClickListener(this);

        //把缓存的和现在的合并

        //upload_iamge_paths.addAll(huancun_images);
        //点击下一步
        xiayibu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("接口进的拉克丝", tijiao_wenben_string() + "a");
                //Log.e("111接口进的拉克丝", tijiao_image_size().size() + "");
                if (tijiao_wenben_string().isEmpty() && tijiao_image_size().size() == 0) {
                    Toast.makeText(getApplicationContext(), "发布内容不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (getUploud_iamge_String().length() > 0) {//有上传新的
                        if (flag == 3) {
                            if (getUploud_iamge_String().length() > 0) {
                                String str = getUploud_iamge_String();
//                                str = str.replace("", "<p>").replace("\n", "<br/>").replace("", "</p>");
                                str = StringUtils.stringFilterTwo(str);
                                String string = str.substring(0, str.length() - 1);
                                update_caogao(str);
                            }
                        } else {
                            if (getUploud_iamge_String().length() > 0) {
                                String str = getUploud_iamge_String();
//                                str = str.replace("", "<p>").replace("\n", "<br/>").replace("", "</p>");
                                str = StringUtils.stringFilterTwo(str);
                                String string = str.substring(0, str.length() - 1);
                                tijiao(str);
                            }
                        }
                    } else {
                        if (flag == 3) {
                            update_caogao("");
                        } else {
                            tijiao("");
                        }
                    }
                }
            }
        });

        //取消回到上一个页面
        quxiao_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 3) {
                    if (tijiao_wenben_string().isEmpty() && tijiao_image_size().size() == 0) {
                        finish();
                    } else {
                        select_save_popwindow();
                    }
                } else {
                    finish();
                }
            }
        });

        //点击输入框弹出键盘
        //et_new_content.setOnTouchListener(new EtOnTouchListener());
        if (flag == 1) {//编辑
            xiayibu_btn.setText("更新");
            if (null != noteDao.queryNoteOne(Integer.valueOf(answer_id))) {
                //拿出来缓存的回答信息
                note = noteDao.queryNoteOne(Integer.valueOf(answer_id));
                myContent = note.getMcontent();
                et_new_content.post(new Runnable() {
                    @Override
                    public void run() {
                        et_new_content.clearAllLayout();
                        String str = StringUtils.stringFilterTwo(myContent);
                        Log.e("爱喝酒副驾驶看到", myContent);
                        Log.e("圣诞节快乐晋升的", str);

                        showDataSync(str.replace("\\'", "'"));
                    }
                });
            }
        } else if (flag == 3) {
            xiayibu_btn.setText("发布");
            if (null != noteDao.queryNoteOne(Integer.valueOf(answer_id))) {
                if (null != SharedPreferencesHelper.getObject(getApplicationContext(), "file_save_note", "note_save")) {
                    //拿出来缓存的回答信息
                    final Note note1 = (Note) SharedPreferencesHelper.getObject(getApplicationContext(), "file_save_note", "note_save");
                    et_new_content.post(new Runnable() {
                        @Override
                        public void run() {
                            et_new_content.clearAllLayout();
                            String str = StringUtils.stringFilterTwo(note1.getMcontent());
                            showDataSync(str.replace("\\'", "'") + "");
                            SharedPreferencesHelper.deleteObject(getApplicationContext(), "file_save_note", "note_save");
                        }
                    });
                    if (caogaoxiang_detail.equals("true")) {
                        return;
                    }
                }
                {
                    //拿出来缓存的回答信息
                    note = noteDao.queryNoteOne(Integer.valueOf(answer_id));
                    et_new_content.post(new Runnable() {
                        @Override
                        public void run() {
                            et_new_content.clearAllLayout();
                            String str = StringUtils.stringFilterTwo(note.getMcontent());
                            showDataSync(str.replace("\\'", "'"));
                        }
                    });
                }


            }
        } else {
            xiayibu_btn.setText("发布");
            if (null != SharedPreferencesHelper.getObject(getApplicationContext(), "file_save_note", "note_save")) {
                Log.e("拿来缓存的数据", "拿来缓存的数据");
                //拿出来缓存的回答信息
                final Note note1 = (Note) SharedPreferencesHelper.getObject(getApplicationContext(), "file_save_note", "note_save");
                et_new_content.post(new Runnable() {
                    @Override
                    public void run() {
                        et_new_content.clearAllLayout();
                        String str = StringUtils.stringFilterTwo(note1.getMcontent());
                        showDataSync(str.replace("\\'", "'") + "");
                        SharedPreferencesHelper.deleteObject(getApplicationContext(), "file_save_note", "note_save");
                    }
                });
            }
            {
                setTitle("新建笔记");
                if (myGroupName == null || "全部笔记getEditData".equals(myGroupName)) {
                    myGroupName = "默认笔记";
                }
            }

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


    @Override
    protected void onPause() {
        super.onPause();
        Properties prop = new Properties();
        prop.setProperty("tiwen_time", "提问功能使用时长");
        StatService.trackCustomEndKVEvent(this, "Question_time", prop);
        endDate = new Date(System.currentTimeMillis());
        long time_cha = (endDate.getTime() - curDate.getTime()) / 1000;
        if (time_cha > 30) {
            //保存异常退出时需要保存的数据
            Note note_save = new Note();
            note_save.setTitle("答案");
            note_save.setContent(getEditDataRemove());
            note_save.setMcontent(getEditData() + " ");
            note_save.setGroupId(88888888);
            note_save.setGroupName("save_note");
            note_save.setType(2);
            note_save.setBgColor("#FFFFFF");
            note_save.setIsEncrypt(0);
            note_save.setCreateTime(DateUtils.date2string(new Date()));
            SharedPreferencesHelper.saveObject(getApplicationContext(), "file_save_note", "note_save", note_save);
//        Note  note = (Note) SharedPreferencesHelper.getObject(getApplicationContext(),"file_save_note","note_save");
//        Log.e("是放假了的快速",note.getMcontent());
        }
    }

    private List<RichTextEditor.EditData> tijiao_image_size() {
        List<RichTextEditor.EditData> need_editList = new ArrayList<>();
        List<RichTextEditor.EditData> editList = et_new_content.buildEditData();
        for (int i = 0; i < editList.size(); i++) {
            RichTextEditor.EditData itemData = editList.get(i);
            if (itemData.imagePath != null) {
                need_editList.add(itemData);
            }
        }
        return need_editList;
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
        LinearLayout select_save = view.findViewById(R.id.select_save);
        select_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断有没有输入文字
                if (tijiao_wenben_string().isEmpty() && tijiao_image_size().size() == 0) {
                    Toast.makeText(getApplicationContext(), "保存内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (getUploud_iamge_String().length() > 0) {
                        String str = getUploud_iamge_String();
//                        str = str.replace("", "<p>").replace("\n", "<br/>").replace("", "</p>");
                        String string = str.substring(0, str.length() - 1);
                        update_caogao_answer(str);//更新草稿
                    } else {
                        update_caogao_answer("");//更新草稿
                    }
                }
            }
        });
        LinearLayout select_no = view.findViewById(R.id.select_no);
        select_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        LinearLayout select_dismiss = view.findViewById(R.id.select_dismiss);
        select_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
    }


    private String getUploud_iamge_String() {
        StringBuffer image_buffer = new StringBuffer();
        List<RichTextEditor.EditData> editList = et_new_content.buildEditData();
        List<String> need_uploads = new ArrayList<>();
        for (int i = 0; i < editList.size(); i++) {
            RichTextEditor.EditData itemData = editList.get(i);
            if (itemData.imagePath != null) {
                //缓存的图片和上传的图片合并
                yi_huancun_images.addAll(upload_iamge_paths);
                for (int k = 0; k < yi_huancun_images.size(); k++) {
                    if (yi_huancun_images.get(k).getIamge_path().equals(itemData.imagePath)) {
                        need_uploads.add(yi_huancun_images.get(k).getUpload_iamge_path());
                    }
                }
            }
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


    private String tijiao_wenben_string() {
        StringBuffer iwenben_buffer = new StringBuffer();
        List<RichTextEditor.EditData> editList = et_new_content.buildEditData();
        for (int i = 0; i < editList.size(); i++) {
            RichTextEditor.EditData itemData = editList.get(i);
            if (itemData.inputStr != null) {
                iwenben_buffer.append(itemData.inputStr);
            }
        }
        return iwenben_buffer.toString().replace("\n", "")
                .replace(" ", "").replace("\n", "");
    }


    @Override
    protected void onResume() {
        max_num = 9;
        super.onResume();
    }

    private void update_caogao(String image_datas) {
        Map<String, String> params = new HashMap<>();
        params.put("content", getEditData());
        params.put("img_data", image_datas);
        params.put("id", answer_id + "");
        params.put("app_version", Contacts.VersionName);
        params.put("controller", "2");
        params.put("phone_version", Contacts.brand);
        Log.e("修改回答草稿参数", params.toString());
        subscription = Network.getInstance("修改回答草稿", QuestionAnswerDetailActivity.this)
                .fabu_edit_caogao_answer(params,
                        new ProgressSubscriberNew<>(String.class, new GsonSubscriberOnNextListener<String>() {
                            @Override
                            public void on_post_entity(String string) {
                                Toast.makeText(getApplicationContext(), "发布成功！", Toast.LENGTH_SHORT).show();
                                updateNote();//修改数据库信息
                                //再次对上传的图片做缓存
                                sharedPreferensListHelper.setDataList(answer_id, upload_iamge_paths);
//                                Intent intent = new Intent(AnswerDetailActivity.this, AnserActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("answer_id", string);
//                                intent.putExtras(bundle);
//                                startActivity(intent);
                                finish();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Log.e("123","编辑失败：" + error);
                            }
                        }, QuestionAnswerDetailActivity.this, true));
    }

    private void update_caogao_answer(String image_datas) {
        Map<String, String> params = new HashMap<>();
        params.put("content", getEditData());
        params.put("img_data", image_datas);
        params.put("id", answer_id + "");
        params.put("app_version", Contacts.VersionName);
        params.put("controller", "2");
        params.put("phone_version", Contacts.brand);
        Log.e("修改回答草稿参数", params.toString());
        subscription = Network.getInstance("修改回答草稿", QuestionAnswerDetailActivity.this)
                .bianji_caogao_answer(params,
                        new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                            @Override
                            public void on_post_entity(List list) {
                                Toast.makeText(getApplicationContext(), "更新成功！", Toast.LENGTH_SHORT).show();
                                finish();
                                dialog.dismiss();
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Log.e("123", "编辑失败：" + error);
                            }
                        }, QuestionAnswerDetailActivity.this, true));
    }

    //提交
    private void tijiao(String image_datas) {
        Map<String, String> params = new HashMap<>();
        params.put("content", getEditData());
        if (image_datas.length() > 0) {
            params.put("img_data", image_datas);
        }
        if (null != question_id) {
            params.put("question_id", question_id);
        }
        params.put("answer_id", note.getGroupId() + "");
        params.put("app_version", Contacts.VersionName);
        params.put("controller", "2");
        params.put("phone_version", Contacts.brand);
        Log.e("修改回答参数", params.toString());
        subscription = Network.getInstance("修改回答", QuestionAnswerDetailActivity.this)
                .update_answer(params,
                        new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                            @Override
                            public void on_post_entity(List list) {
                                Toast.makeText(getApplicationContext(), "更新成功！", Toast.LENGTH_SHORT).show();
                                updateNote();//修改数据库信息
                                //再次对上传的图片做缓存
                                sharedPreferensListHelper.setDataList(answer_id, upload_iamge_paths);

                                Intent intent = new Intent(QuestionAnswerDetailActivity.this, AnserActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("answer_id", answer_id);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();

                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                if (error.equals("超过三分钟无法编辑")) {
                                    quxiao_btn.setVisibility(View.GONE);
                                }
                                Log.e("123","更新失败：" + error);
                            }
                        }, QuestionAnswerDetailActivity.this, true));
    }


    private void updateNote() {
        note.setTitle("回答");
        note.setContent(getEditDataRemove());
        note.setMcontent(getEditData());
        note.setGroupId(Integer.valueOf(answer_id));
        note.setGroupName(answer_id);
        note.setType(2);
        note.setBgColor("#FFFFFF");
        note.setIsEncrypt(0);
        note.setCreateTime(DateUtils.date2string(new Date()));
        noteDao.updateNote(note);
        //紧接着查询一波
//        Note mnote = noteDao.queryNoteOne(Integer.valueOf(answer_id));
//        Log.e("查询出来的内容", mnote.getMcontent());
    }

    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(QuestionAnswerDetailActivity.this, R.color.white);
        } else {
            StatusBarUtil.transparencyBar(QuestionAnswerDetailActivity.this);

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
        progressUtil.showProgressDialog(QuestionAnswerDetailActivity.this, "载入中...");
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
                        //判断最后一张是不是图片,如果有图片，就加一行空文字，用来显示光标
                        List<RichTextEditor.EditData> editList = et_new_content.buildEditData();
                        for (int i = 0; i < editList.size(); i++) {
                            if (i == editList.size() - 1) {
                                if (editList.get(i).imagePath != null && editList.get(i).inputStr == null) {
                                    et_new_content.addEditTextAtIndex(et_new_content.getLastIndex(), "");
                                }
                            }
                        }
                        //关闭加载loading
                        progressUtil.dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), "解析错误：图片不存在或已损坏", Toast.LENGTH_SHORT).show();
                        //关闭加载loading
                        progressUtil.dismissProgressDialog();

                    }

                    @Override
                    public void onNext(String text) {
                        StringBuffer sb = new StringBuffer();
                        String[] strArray = text.split("\n");
                        for (String str : strArray) {
                            Log.e("666666666", str);
                            sb.append(Html.fromHtml(str));
                            sb.append("\n");
                        }
                        String content_str = sb.toString();
                        if (isImage(text)) {
                            et_new_content.addImageViewAtIndex(et_new_content.getLastIndex(), text);
                        } else {
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

    List<UploadImageEntity> yi_huancun_images = new ArrayList<>();
    /**
     * 显示数据
     */
    Bitmap mBitmap;
    String path;
    String imagePath;

    protected void showEditData(Subscriber<? super String> subscriber, String html, Context context) {
        try {
            Log.e("填充进来的数据", html);
            List<String> textList = StringUtils.cutStringByImgTag(html);
            for (int i = 0; i < textList.size(); i++) {
                String text = textList.get(i);
                if (text.contains("<img")) {
                    imagePath = StringUtils.getImgSrc(text);
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
                    String str = StringUtils.stringFilterTwo(text);
                    String content_str = str.replace("\r\n", "").replace("\n" + "</br>", "");
                    subscriber.onNext(content_str);
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
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    private String getEditData() {
        List<RichTextEditor.EditData> editList = et_new_content.buildEditData();
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < editList.size(); i++) {
            RichTextEditor.EditData itemData = editList.get(i);
            if (itemData.inputStr != null) {
                if (itemData.inputStr.equals("") || itemData.inputStr.equals(" ")) {

                } else {
                    String str = TextUtils.htmlEncode(itemData.inputStr).toString();
                    content.append("<p>").append(str).append("</p>");

                }
            } else if (itemData.imagePath != null) {
                //缓存的图片和上传的图片合并
                yi_huancun_images.addAll(upload_iamge_paths);
                for (int k = 0; k < yi_huancun_images.size(); k++) {
                    if (yi_huancun_images.get(k).getUpload_iamge_path().equals(itemData.imagePath) || yi_huancun_images.get(k).getIamge_path().equals(itemData.imagePath)) {
                        itemData.imagePath = "";
                        content.append("<img src=\"")
                                .append(yi_huancun_images.get(k).getUpload_iamge_path()).append("\"/>");
                    }
                }
            }
        }

        String mcontent = content.toString().replace("\n\r", "<br/>").replace("\n", "<br/>");
        Log.e("编辑上传的content内容", mcontent);
        return mcontent;
    }

    private String getEditDataRemove() {
        List<RichTextEditor.EditData> editList = et_new_content.buildEditData();
        StringBuffer content_remove = new StringBuffer();
        for (int i = 0; i < editList.size(); i++) {
            RichTextEditor.EditData itemData = editList.get(i);
            if (itemData.inputStr != null) {
                content_remove.append(itemData.inputStr);
            } else if (itemData.imagePath != null) {
                content_remove.append("<img src=\"").append(itemData.imagePath).append("\"/>");
            }

        }
        Log.e("需要保存在本地数据库的内容", content_remove.toString());
        return content_remove.toString();
    }


    /**
     * 调用图库选择
     */
    ArrayList<ImageItem> images = null;


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            //相机或相册回调
            if (requestCode == 101) {
                //返回对象集合：如果你需要了解图片的宽、高、大小、用户是否选中原图选项等信息，可以用这个
                ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);

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

    /**
     * 异步方式插入图片
     **/
    Biscuit mBiscuit;
    MyThread thread;
    int iamge_list_length = 0;
    int images_index = 0;
    Subscriber<? super String> subscriber;
    ProgressUtil progress_upload;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progress_upload = new ProgressUtil();
            progress_upload.showProgressDialog(QuestionAnswerDetailActivity.this, "载入中...");

        }
    };

    //可以终止的线程
    public class MyThread extends Thread {
        public void run() {
            handler.sendEmptyMessage(0);
            while (true) {
                if (this.isInterrupted()) {
                    progress_upload.dismissProgressDialog();
                    return;
                }
            }
        }
    }

    private void insertImagesSync(final ArrayList<String> mselectedPhotoList) {
//        thread = new MyThread();
//        thread.start();
        progress_upload = new ProgressUtil();
        progress_upload.showProgressDialog(QuestionAnswerDetailActivity.this, "载入中...");
        subsInsert = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> msubscriber) {
                subscriber = msubscriber;
                try {
                    //可以同时插入多张图片
                    mBiscuit = Biscuit.with(QuestionAnswerDetailActivity.this)
                            .path(mselectedPhotoList) //可以传入一张图片路径，也可以传入一个图片路径列表
                            .loggingEnabled(true)//是否输出log 默认输出
//                                          .quality(50)//质量压缩值（0...100）默认已经非常接近微信，所以没特殊需求可以不用自定义
                            .originalName(false) //使用原图名字来命名压缩后的图片，默认不使用原图名字,随机图片名字
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
                            .targetDir(Environment.getExternalStorageDirectory() + "/images/")//自定义压缩保存路径
//                                          .executor(executor) //自定义实现执行，注意：必须在子线程中执行 默认使用AsyncTask线程池执行
//                                          .ignoreAlpha(true)//忽略alpha通道，对图片没有透明度要求可以这么做，默认不忽略。
//                                          .compressType(Biscuit.SAMPLE)//采用采样率压缩方式，默认是使用缩放压缩方式，也就是和微信的一样。
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
                        Log.e("上传图片的本地地址", imagePath);
                        et_new_content.insertImage(imagePath, et_new_content.getMeasuredWidth(), iamge_list_length);
                        //请求阿里上传图片
                        if (imagePath.contains(".gif")) {
                            upload_user_image("syjapppic", Network.bucketPath + "answer/" + DateUtils.getNowYear() + "_" + DateUtils.getNowMonth() + "/" + StringsHelper.getUUID() + ".gif", imagePath);

                        } else {
                            upload_user_image("syjapppic", Network.bucketPath + "answer/" + DateUtils.getNowYear() + "_" + DateUtils.getNowMonth() + "/" + StringsHelper.getUUID() + ".jpg", imagePath);

                        } /* new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                HttpURLConnection conn = null;//声明连接对象
                                String urlStr = "https://app1.shiyujia.com/answerApi/qiniu/upload";
                                InputStream is = null;
                                String resultData = "";
                                try {
                                    URL url = new URL(urlStr); //URL对象
                                    conn = (HttpURLConnection) url.openConnection(); //使用URL打开一个链接,下面设置这个连接
                                    conn.setRequestMethod("GET"); //使用get请求

                                    if (conn.getResponseCode() == 200) {//返回200表示连接成功
                                        is = conn.getInputStream(); //获取输入流
                                        InputStreamReader isr = new InputStreamReader(is);
                                        BufferedReader bufferReader = new BufferedReader(isr);
                                        String inputLine = "";
                                        while ((inputLine = bufferReader.readLine()) != null) {
                                            resultData += inputLine + "\n";
                                        }
                                        JSONObject object = new JSONObject(resultData);
                                        String code = object.getString("code");
                                        if ("1".equals(code)) {
                                            JSONObject data = object.getJSONObject("data");
                                            String token = data.getString("token");
                                            ToKen = token;
                                            // Toast.makeText(MainActivity.this, "       获取Token     " + token, Toast.LENGTH_SHORT).show();
                                            Log.e("123", "        get方法取回内容：" + token + "  ----->  " + resultData);
                                        }
                                    }
                                    Message message = new Message();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("iamge_path",imagePath);
                                    message.setData(bundle);
                                    post_image_handel.sendMessage(message);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();*/
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

        // 文件元信息的设置是可选的
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setContentType("application/octet-stream"); // 设置content-type
        // metadata.setContentMD5(BinaryUtil.calculateBase64Md5(uploadFilePath)); // 校验MD5
        // put.setMetadata(metadata);
        /*try {
            PutObjectResult putResult = oss.putObject(put);
            Log.d("PutObject", "UploadSuccess");
            Log.d("ETag", putResult.getETag());
            Log.d("RequestId", putResult.getRequestId());
            Log.e("PutObject", "上传图片成功");
            //progressUtil.dismissProgressDialog();
            uploud_iamge = "https://syjapppic.oss-cn-hangzhou.aliyuncs.com/" + objectKey;
            //Log.e("上传图片地址:", uploud_iamge);
            UploadImageEntity uploadImageEntity = new UploadImageEntity();
            uploadImageEntity.setIamge_path(uploadFilePath);
            uploadImageEntity.setUpload_iamge_path(uploud_iamge);
            upload_iamge_paths.add(uploadImageEntity);
        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常
            Log.e("RequestId", e.getRequestId());
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        }*/
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("图片当前上传进度：", "currentSize: " + currentSize + " 图片总上传进度: " + totalSize);
                //开始loading
                //progress_upload.showProgressDialog(LijiHuidaActivity.this, "上传中...");


            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.e("PutObject", "上传图片成功");
                //progressUtil.dismissProgressDialog();
                uploud_iamge = "https://syjapppic.oss-cn-hangzhou.aliyuncs.com/" + objectKey;
                //Log.e("上传图片地址:", uploud_iamge);
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

    /**
     * 退出处理
     */
    private void dealwithExit() {
        if (flag == 3) {
            if (tijiao_wenben_string().isEmpty() && tijiao_image_size().size() == 0) {
                finish();
            } else {
                select_save_popwindow();
            }
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        dealwithExit();
    }

    private int max_num = 9;
    private int cureent_numl;

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.jianpan_top_view:
                //设置最多只能上传9张图片
                cureent_numl = 0;
                //遍历图片数量设置index
                List<RichTextEditor.EditData> editList = et_new_content.buildEditData();
                for (int i = 0; i < editList.size(); i++) {
                    RichTextEditor.EditData itemData = editList.get(i);
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
                    EasyPhotos.createAlbum(this, true, com.app.cookbook.xinhe.foodfamily.util.GlideEngine.getInstance())
                            .setFileProviderAuthority("com.app.cookbook.xinhe.foodfamily.fileprovider")
                            .setPuzzleMenu(false)
                            .setCount(max_num)
                            .setOriginalMenu(false, true, null)
                            .start(101);
                }


                break;
        }
    }
}

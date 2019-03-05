package com.sendtion.xrichtext;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 可编辑富文本
 */
public class RichTextEditor extends ScrollView implements View.OnClickListener {
    private static final int EDIT_PADDING = 10; // edittext常规padding是10dp
    private int viewTagIndex = 1; // 新生的view都会打一个tag，对每个view来说，这个tag是唯一的。
    private LinearLayout allLayout; // 这个是所有子view的容器，scrollView内部的唯一一个ViewGroup
    private LayoutInflater inflater;
    private OnKeyListener keyListener; // 所有EditText的软键盘监听器
    private OnClickListener btnListener; // 图片右上角红叉按钮监听器
    private OnFocusChangeListener focusListener; // 所有EditText的焦点监听listener
    private EditText lastFocusEdit; // 最近被聚焦的EditText
    private LayoutTransition mTransitioner; // 只在图片View添加或remove时，触发transition动画
    private int editNormalPadding = 0; //
    private int disappearingImageIndex = 0;
    private Context mContext;
    public static int MaxLeng = 140;

    public RichTextEditor(Context context) {

        this(context, null);
        mContext = context;
    }

    public RichTextEditor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;

    }

    private EditText fist_lin_editetext;

    public RichTextEditor(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);

        // 1. 初始化allLayout
        allLayout = new LinearLayout(context);
        allLayout.setOrientation(LinearLayout.VERTICAL);
        setupLayoutTransitions();
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        allLayout.setPadding(50, 15, 50, 15);//设置间距，防止生成图片时文字太靠边，不能用margin，否则有黑边
        addView(allLayout, layoutParams);

        // 2. 初始化键盘退格监听
        // 主要用来处理点击回删按钮时，view的一些列合并操作
        keyListener = new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    EditText edit = (EditText) v;
                    onBackspacePress(edit);
                }
                return false;
            }
        };

        // 3. 图片叉掉处理
        btnListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                RelativeLayout parentView = (RelativeLayout) v.getParent();
                onImageCloseClick(parentView);
            }
        };

        focusListener = new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    lastFocusEdit = (EditText) v;
                }
            }
        };

        LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        EditText firstEdit = createEditText("开始添加内容"
                , dip2px(context, EDIT_PADDING));
        fist_lin_editetext = firstEdit;
        firstEdit.setHintTextColor(Color.parseColor("#777777"));
        firstEdit.setTextColor(Color.parseColor("#000000"));
        allLayout.addView(firstEdit, firstEditParam);
        lastFocusEdit = firstEdit;

        //设置上限140个字
        lastFocusEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = lastFocusEdit.getText();
                if (s.length()==140) {
                    Log.e("几十块了房价快速的", "" + s.length());
                }

//                int len = editable.length();
//                if (len > 140) {
//                    Toast.makeText(mContext, "上限140个字！", Toast.LENGTH_SHORT).show();
//                    is_chao_number = true;
////                    int selEndIndex = Selection.getSelectionEnd(editable);
////                    String str = editable.toString();
////                    //截取新字符串
////                    String newStr = str.substring(0, 140);
////                    lastFocusEdit.setText(newStr);
////                    editable = lastFocusEdit.getText();
////
////                    //新字符串的长度
////                    int newLen = editable.length();
////                    //旧光标位置超过字符串长度
////                    if (selEndIndex > newLen) {
////                        selEndIndex = editable.length();
////                    }
////                    //设置新光标所在的位置
////                    Selection.setSelection(editable, selEndIndex);
//                } else {
//                    is_chao_number = false;
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    List<String> iamges_list = new ArrayList<>();//用来记录，文本中是否存在图片

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //只在首次点击触发
        if (buildEditData().size() == 0) {//当点击区域一片空白的时候
            //Log.e("就开始放假看电视了", "看沙发接口的反倒是");
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                lastFocusEdit.requestFocus();
                lastFocusEdit.setSelection(0);
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                return false;
            } else {
                return super.onTouchEvent(ev);
            }
        } else if (buildEditData().size() > 0) {//当页面有内容的时候
            iamges_list.clear();
            //Log.e("看似简单开了房", "看沙发接口的");
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                List<RichTextEditor.EditData> editList = buildEditData();
                for (int i = 0; i < editList.size(); i++) {
                    RichTextEditor.EditData itemData = editList.get(i);
                    if (itemData.imagePath != null) {
                        iamges_list.add(itemData.imagePath);
                    }
                }
                if (iamges_list.size() > 0) {//如果有图片，就照常
                    return super.onTouchEvent(ev);

                } else {
                    //当没有图片只有文字的时候，弹出键盘
                    int num = allLayout.getChildCount();
                    for (int index = 0; index < num; index++) {
                        View itemView = allLayout.getChildAt(index);
                        if (itemView instanceof EditText) {
                            EditText item = (EditText) itemView;
                            item.requestFocus();
                            item.setSelection(item.getText().toString().length());
                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            assert imm != null;
                            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                        }
                    }
                    return false;
                }

            } else {
                return super.onTouchEvent(ev);
            }
        } else {
            return super.onTouchEvent(ev);
        }


    }

    /**
     * 插入一张图片
     */

    public void insertImage(Bitmap bitmap, String imagePath) {
        String lastEditStr = lastFocusEdit.getText().toString();
        int cursorIndex = lastFocusEdit.getSelectionStart();
        String editStr1 = lastEditStr.substring(0, cursorIndex).trim();
        int lastEditIndex = allLayout.indexOfChild(lastFocusEdit);
        if (lastEditStr.length() == 0 || editStr1.length() == 0) {
            // 如果EditText为空，或者光标已经顶在了editText的最前面，则直接插入图片，并且EditText下移即可
            addImageViewAtIndex(lastEditIndex, imagePath);
            lastFocusEdit.setHint("");
        } else {
            // 如果EditText非空且光标不在最顶端，则需要添加新的imageView和EditText
            lastFocusEdit.setText(editStr1);
            String editStr2 = lastEditStr.substring(cursorIndex).trim();
            if (editStr2.length() == 0) {//如果下方没有文字
                editStr2 = "";
            } else {//如果下方有文字
                addEditTextAtIndex(lastEditIndex + 1, editStr2);
            }
            if (allLayout.getChildCount() - 1 == lastEditIndex) {
                addEditTextAtIndex(lastEditIndex + 1, editStr2);
            }
            addImageViewAtIndex(lastEditIndex + 1, imagePath);
            lastFocusEdit.requestFocus();
            lastFocusEdit.setSelection(editStr1.length(), editStr1.length());

        }
        hideKeyBoard();
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * 初始化transition动画
     */
    private void setupLayoutTransitions() {
        mTransitioner = new LayoutTransition();
        allLayout.setLayoutTransition(mTransitioner);
        mTransitioner.addTransitionListener(new LayoutTransition.TransitionListener() {
            @Override
            public void startTransition(LayoutTransition transition,
                                        ViewGroup container, View view, int transitionType) {

            }

            @Override
            public void endTransition(LayoutTransition transition,
                                      ViewGroup container, View view, int transitionType) {
                if (!transition.isRunning()
                        && transitionType == LayoutTransition.CHANGE_DISAPPEARING) {
                    // transition动画结束，合并EditText
                    // mergeEditText();
                }
            }
        });
        mTransitioner.setDuration(300);
    }

    public int dip2px(Context context, float dipValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }

    /**
     * 处理软键盘backSpace回退事件
     *
     * @param editTxt 光标所在的文本输入框
     */
    private void onBackspacePress(EditText editTxt) {
        int startSelection = editTxt.getSelectionStart();
        // 只有在光标已经顶到文本输入框的最前方，在判定是否删除之前的图片，或两个View合并
        if (startSelection == 0) {
            int editIndex = allLayout.indexOfChild(editTxt);
            View preView = allLayout.getChildAt(editIndex - 1); // 如果editIndex-1<0,
            // 则返回的是null
            if (null != preView) {
                if (preView instanceof RelativeLayout) {
                    // 光标EditText的上一个view对应的是图片
                    onImageCloseClick(preView);

                } else if (preView instanceof EditText) {
                    // 光标EditText的上一个view对应的还是文本框EditText
                    String str1 = editTxt.getText().toString();
                    EditText preEdit = (EditText) preView;
                    String str2 = preEdit.getText().toString();
                    allLayout.removeView(editTxt);

                    // 文本合并
                    preEdit.setText(str2 + str1);
                    preEdit.requestFocus();
                    preEdit.setSelection(str2.length(), str2.length());
                    lastFocusEdit = preEdit;
                    if (lastFocusEdit.getText().length() == 0) {
                        lastFocusEdit.setHint("");
                    }
                }
            }
        }
    }

    /**
     * 处理图片叉掉的点击事件
     *
     * @param view 整个image对应的relativeLayout view
     * @type 删除类型 0代表backspace删除 1代表按红叉按钮删除
     */
    private void onImageCloseClick(View view) {
        disappearingImageIndex = allLayout.indexOfChild(view);
        //删除文件夹里的图片
        List<EditData> dataList = buildEditData();
        EditData editData = dataList.get(disappearingImageIndex);
        //Log.i("", "editData: "+editData);
        if (editData.imagePath != null) {
            SDCardUtil.deleteFile(editData.imagePath);
        }
        allLayout.removeView(view);
    }

    public void clearAllLayout() {
        allLayout.removeAllViews();
    }

    public int getLastIndex() {
        int lastEditIndex = allLayout.getChildCount();
        return lastEditIndex;
    }

    /**
     * 生成文本输入框
     */
    public EditText createEditText(String hint, int paddingTop) {
        EditText editText = (EditText) inflater.inflate(R.layout.rich_edittext, null);
        editText.setOnKeyListener(keyListener);
        editText.setTag(viewTagIndex++);
        editText.setPadding(editNormalPadding, paddingTop, editNormalPadding, paddingTop);
        editText.setHint(hint);
        editText.setOnFocusChangeListener(focusListener);
        return editText;
    }

    /**
     * 生成图片View
     */
    private RelativeLayout createImageLayout() {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.edit_imageview, null);
        layout.setTag(viewTagIndex++);
        View closeView = layout.findViewById(R.id.image_close);
        //closeView.setVisibility(GONE);
        closeView.setTag(layout.getTag());
        closeView.setOnClickListener(btnListener);
        return layout;
    }

    /**
     * 根据绝对路径添加view
     *
     * @param imagePath
     */
    public void insertImage(String imagePath, int width) {
        Bitmap bmp = getScaledBitmap(imagePath, width);
        insertImage(bmp, imagePath);
    }


    /**
     * 隐藏小键盘
     */
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(lastFocusEdit.getWindowToken(), 0);
    }

    /**
     * 在特定位置插入EditText
     *
     * @param index   位置
     * @param editStr EditText显示的文字
     */
    StringBuffer add_text_buffer = new StringBuffer();
    private boolean is_chao_number = false;
    int length = 0;

    public void addEditTextAtIndex(final int index, CharSequence editStr) {
        String lastEditStr = lastFocusEdit.getText().toString();
        add_text_buffer.append(lastEditStr);
        add_text_buffer.append(editStr);
        final EditText editText2 = createEditText("", EDIT_PADDING);
        editText2.setText(editStr);


        //设置上限140个字
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Editable editable = editText2.getText();
//                int num = allLayout.getChildCount();
//                for (int index = 0; index < num; index++) {
//                    View itemView = allLayout.getChildAt(index);
//                    if (itemView instanceof EditText) {
//                        EditText item = (EditText) itemView;
//                        length = length + item.getText().toString().length();
//                        if (length == 141) {
//                            Toast.makeText(mContext, "上限140个字！", Toast.LENGTH_SHORT).show();
//                            is_chao_number = true;
//                            int selEndIndex = Selection.getSelectionEnd(editable);
//                            String str = editable.toString();
//                            //截取新字符串
//                            String newStr = str.substring(0, 140);
//                            editText2.setText(newStr);
//                            editable = editText2.getText();
//                            //新字符串的长度
//                            int newLen = editable.length();
//                            //旧光标位置超过字符串长度
//                            if (selEndIndex > newLen) {
//                                selEndIndex = editable.length();
//                            }
//                            //设置新光标所在的位置
//                            Selection.setSelection(editable, selEndIndex);
//                        }
//                    }
//                }


//                int len = editable.length() + (add_text_buffer.toString().length());
//                Log.e("文字的长度", len + "");
//                if (len > 140) {
//                    Toast.makeText(mContext, "上限140个字！", Toast.LENGTH_SHORT).show();
//                    is_chao_number = true;
////                    int selEndIndex = Selection.getSelectionEnd(editable);
////                    String str = editable.toString();
////                    //截取新字符串
////                    String newStr = str.substring(0, 140);
////                    editText2.setText(newStr);
////                    editable = editText2.getText();
////
////                    //新字符串的长度
////                    int newLen = editable.length();
////                    //旧光标位置超过字符串长度
////                    if (selEndIndex > newLen) {
////                        selEndIndex = editable.length();
////                    }
////                    //设置新光标所在的位置
////                    Selection.setSelection(editable, selEndIndex);
//                    //MyApplication.is_caoguo_number = true;
//                } else {
//                    is_chao_number = false;
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText2.setOnFocusChangeListener(focusListener);
        editText2.setHintTextColor(Color.parseColor("#777777"));
        editText2.setTextColor(Color.parseColor("#000000"));
        allLayout.addView(editText2, index);
    }

    public boolean is_chao_number() {
        return is_chao_number;
    }

    /**
     * 在特定位置添加ImageView
     */
    public void addImageViewAtIndex(final int index, String imagePath) {
        final RelativeLayout imageLayout = createImageLayout();
        DataImageView imageView = (DataImageView) imageLayout.findViewById(R.id.edit_imageView);
        File file = new File(imagePath);
        Uri uri = Uri.fromFile(new File(imagePath));
        final RequestOptions options = new RequestOptions();
        options.centerCrop()
                .placeholder(R.drawable.__picker_ic_photo_black_48dp)
                .error(R.drawable.__picker_ic_broken_image_black_48dp);

        Glide.with(mContext)
                .load(uri)
                .apply(options)
                .thumbnail(0.1f)
                .into(imageView);

        //Glide.with(mContext).load(imagePath).asBitmap().into(imageView);

        imageView.setAbsolutePath(imagePath);//保留这句，后面保存数据会用
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//裁剪剧中

        // 调整imageView的高度，根据宽度来调整高度
        Bitmap bmp = BitmapFactory.decodeFile(imagePath);
        int imageHeight = 500;
        if (bmp != null) {
            imageHeight = allLayout.getWidth() * bmp.getHeight() / bmp.getWidth();
            bmp.recycle();
        }
        // TODO: 17/3/1 调整图片高度，这里是否有必要，如果出现微博长图，可能会很难看
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, imageHeight);//设置图片固定高度
        lp.bottomMargin = 5;
        imageView.setLayoutParams(lp);

        allLayout.addView(imageLayout, index);

    }

    /**
     * 根据view的宽度，动态缩放bitmap尺寸
     *
     * @param width view的宽度
     */
    public Bitmap getScaledBitmap(String filePath, int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int sampleSize = options.outWidth > width ? options.outWidth / width
                + 1 : 1;
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 对外提供的接口, 生成编辑数据上传
     */
    public List<EditData> buildEditData() {
        List<EditData> dataList = new ArrayList<EditData>();
        int num = allLayout.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = allLayout.getChildAt(index);
            EditData itemData = new EditData();
            if (itemView instanceof EditText) {
                EditText item = (EditText) itemView;
                itemData.inputStr = item.getText().toString();
            } else if (itemView instanceof RelativeLayout) {
                DataImageView item = (DataImageView) itemView.findViewById(R.id.edit_imageView);
                itemData.imagePath = item.getAbsolutePath();
            }
            dataList.add(itemData);
        }

        return dataList;
    }


    public class EditData {
        public String inputStr;
        public String imagePath;
    }
}

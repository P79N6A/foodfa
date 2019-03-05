package com.app.cookbook.xinhe.foodfamily.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

/**
 * Created by 21032 on 2017/9/14.
 */

public class SavePictureUtil {
    private static SavePictureUtil instance;

    private SavePictureUtil(){

    }

    public static SavePictureUtil getInstance(){
        if (instance == null) {
            synchronized (SavePictureUtil.class) {
                if (instance == null) {
                    instance = new SavePictureUtil();
                }
            }
        }
        return instance;
    }

    public String savePic(Context context,Bitmap bitmap,String filePath, String fileName){
        //获取内部存储状态
        String state = Environment.getExternalStorageState();
        //如果状态不是mounted，无法读写
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        File file = getFilePath(filePath ,fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();//此处报错！
            }
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public String getFilePath(String path){
        return Environment.getExternalStorageDirectory().toString() + path;
    }

    //通过UUID生成字符串文件名
    public String getFileNameByUuid(){
        String fileName = "/"+UUID.randomUUID().toString()+".jpg";
        return fileName;
    }

    //通过Random()类生成数组命名
    public String getFileNameByRandom(){
        Random random = new Random();
        String fileName = "/"+String.valueOf(random.nextInt(Integer.MAX_VALUE))+".jpg";
        return fileName;
    }

    //通过时间生成字符串文件名
    public String getFileNameByTime(){
        Calendar now = new GregorianCalendar();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String fileName = "/"+simpleDate.format(now.getTime())+".jpg";
        return fileName;
    }


    public File getFilePath(String filePath, String fileName) {

        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {

        }
    }
}

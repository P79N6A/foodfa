package com.app.cookbook.xinhe.foodfamily.util;


//import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
//import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * Created by joyce on 2017/5/31.
 */

public class BarCodeHelper {
    /**
     * 生成带logo的二维码
     *
     * @param activity
     * @param str
     * @param imageView
     */
//    public static void createChineseQRCodeWithLogo(final Activity activity, final String str, final ImageView imageView) {
//        /*
//        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
//        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
//         */
//        new AsyncTask<Void, Void, Bitmap>() {
//            @Override
//            protected Bitmap doInBackground(Void... params) {
//                Bitmap logoBitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher);
//                return QRCodeEncoder.syncEncodeQRCode(str, BGAQRCodeUtil.dp2px(activity, 150), Color.parseColor("#333333"), logoBitmap);
//            }
//
//            @Override
//            protected void onPostExecute(Bitmap bitmap) {
//                if (bitmap != null) {
//                    imageView.setImageBitmap(bitmap);
//                } else {
//                    Toast.makeText(activity, "生成二维码失败", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }.execute();
//    }
}

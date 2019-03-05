package com.app.cookbook.xinhe.foodfamily.shiyujia.view;

import android.content.Context;
import android.widget.ImageView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.youth.banner.loader.ImageLoader;

public class DiscoverGlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        Glide.with(context.getApplicationContext())
                .load(path.toString())
                .dontAnimate()
                .placeholder(R.drawable.icon_banner_bg) // 预加载
                .error(R.drawable.icon_banner_bg) // 加载错误
                .into(imageView);

    }

//    @Override
//    public ImageView createImageView(Context context) {
//        //圆角
//        return new RoundAngleImageView(context);
//    }
}
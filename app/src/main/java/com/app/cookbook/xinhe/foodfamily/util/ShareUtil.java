package com.app.cookbook.xinhe.foodfamily.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.app.cookbook.xinhe.foodfamily.main.AnserActivity;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageEvent;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QQClientNotExistException;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import onekeyshare.OnekeyShare;
import rx.Subscription;

/**
 * Created by 18030150 on 2018/3/25.
 */

public class ShareUtil {
    public static void ShareAll(Context context) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("食与家");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://fwq.liuliugo.cn:8080/liuliu/silkBag/SilkBagDetail/sid/");
        // text是分享文本，所有平台都需要这个字段
        if (!TextUtils.isEmpty("分享")) {
            oks.setText("分享");
        } else {
            oks.setText("记录心情");
        }
        // 网络图片地址，所有平台都需要
//            if (listStr.size() > 0) {
        oks.setImageUrl("分享");
//            } else if (listStr.size() == 0) {
//                oks.setImageUrl(App.IMAGE + "silk_bag/liuliuShare.jpg");
//            }
        oks.setUrl("http://fwq.liuliugo.cn:8080/liuliu/silkBag/SilkBagDetail/sid/");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我的评论:");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("食与家");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://fwq.liuliugo.cn:8080/liuliu/silkBag/SilkBagDetail/sid/");
        // 启动分享GUI
        oks.show(context);
    }

    //分享微博
    public static void ShareWeiBo(final Context context, String title, String content, String imageUrl, String Url, final String type) {
        Log.e("123", "      Url         " + Url);
        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setTitle(ShartStr(title));
        sp.setText(ShartStr(title) + " " + Url + " (想看更多？下载 App: " + "http://t.cn/RuImb43" + ")");
//        sp.setUrl(Url);
//        sp.setImageUrl(imageUrl);
        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                Toast.makeText(context, "已分享", Toast.LENGTH_SHORT).show();
                if ("1".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("121"));
                } else if ("2".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("122"));
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
//                Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Platform platform, int i) {
//                Toast.makeText(context, "取消分享", Toast.LENGTH_SHORT).show();
            }
        }); // 设置分享事件回调
        // 执行图文分享
        weibo.share(sp);
    }

    //分享微信
    public static void ShareWechat(final Context context, String title, String content, String inageUrl, String Url, final String type) {
//        Log.e("123", "      Url         " + Url + "            " + inageUrl);
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setText(ShartStr(content));
        sp.setUrl(Url);
        sp.setTitle(ShartStr(title));
        sp.setImageUrl(inageUrl);
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                Toast.makeText(context, "已分享", Toast.LENGTH_SHORT).show();
                if ("1".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("121"));
                } else if ("2".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("122"));
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
//                Log.e("123", "          微信分享失败       " + throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
//                Log.e("123", "         取消分享        ");
            }
        }); // 设置分享事件回调
        // 执行图文分享
        wechat.share(sp);
    }

    //分享微信朋友圈
    public static void ShareWechatMoments(final Context context, String title, String content, String inageUrl, String Url, final String type) {
        Log.e("123", "      Url         " + Url);
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setText(ShartStr(content));
        sp.setUrl(Url);
        sp.setTitle(ShartStr(title));
        sp.setImageUrl(inageUrl);
        sp.setImagePath("");
        Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                Log.e("123", "        分享朋友圈成功         ");
                if ("1".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("121"));
                } else if ("2".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("122"));
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
//                Log.e("123", "        微信朋友圈分享失败         " + throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
//                Log.e("123", "         取消分享朋友圈        ");
            }
        }); // 设置分享事件回调
        // 执行图文分享
        wechat.share(sp);
    }

    //分享QQ
    public static void ShareQQ(final Context context, String title, String content, String inageUrl, String Url, final String type) {
        Log.e("123", "      Url         " + Url);
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitleUrl(Url);
        sp.setText(ShartStr(content));
        sp.setTitle(ShartStr(title));
        sp.setImageUrl(inageUrl);
        sp.setSite("食与家");
        sp.setSiteUrl(Url);
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.e("123", "        分享QQ成功         ");
                if ("1".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("121"));
                } else if ("2".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("122"));
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
//                Log.e("123", "        微信朋友圈分享失败         " + throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
//                Log.e("123", "         取消分享分享QQ        ");
            }
        }); // 设置分享事件回调
        // 执行图文分享
        qq.share(sp);
    }

    //分享QQ空间
    public static void ShareQZone(final Context context, String title, String content, String inageUrl, String Url, final String type) {
        Log.e("123", "      Url         " + Url);
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(ShartStr(title));
        sp.setTitleUrl(Url);
        sp.setText(ShartStr(content));
        sp.setImageUrl(inageUrl);
        sp.setSite("食与家");
        sp.setSiteUrl(Url);
        Platform Qzone = ShareSDK.getPlatform(QZone.NAME);
        Qzone.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.e("123", "        分享QQ空间成功         ");
                if ("1".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("121"));
                } else if ("2".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("122"));
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
//                Log.e("123", "        微信QQ空间分享失败         " + throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
//                Log.e("123", "         取消分享QQ空间        ");
            }
        }); // 设置分享事件回调
        // 执行图文分享
        Qzone.share(sp);
    }

    public static void SharePathQZone(String imagePath, final String type) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setImagePath(imagePath);
        sp.setSite("食与家");
        Platform Qzone = ShareSDK.getPlatform(QZone.NAME);
        Qzone.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.e("123", "        分享QQ空间成功         ");
                if ("1".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("121"));
                } else if ("2".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("122"));
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
//                Log.e("123", "        微信QQ空间分享失败         " + throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
//                Log.e("123", "         取消分享QQ空间        ");
            }
        }); // 设置分享事件回调
        // 执行图文分享
        Qzone.share(sp);
    }

    public static void SharePathQQ(String imagePath, final String type) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setSite("食与家");
        sp.setImagePath(imagePath);
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.e("123", "        分享QQ成功         ");
                if ("1".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("121"));
                } else if ("2".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("122"));
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
//                Log.e("123", "        微信朋友圈分享失败         " + throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
//                Log.e("123", "         取消分享分享QQ        ");
            }
        }); // 设置分享事件回调
        // 执行图文分享
        qq.share(sp);
    }

    public static void SharePathWechatMoments(String imagePath, final String type) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setImagePath(imagePath);
        Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                Log.e("123", "        分享朋友圈成功         ");
                if ("1".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("121"));
                } else if ("2".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("122"));
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
//                Log.e("123", "        微信朋友圈分享失败         " + throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
//                Log.e("123", "         取消分享朋友圈        ");
            }
        }); // 设置分享事件回调
        // 执行图文分享
        wechat.share(sp);
    }

    public static void SharePathWechat(String imagePath, final String type) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setImagePath(imagePath);
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                Toast.makeText(context, "已分享", Toast.LENGTH_SHORT).show();
                if ("1".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("121"));
                } else if ("2".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("122"));
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
//                Log.e("123", "          微信分享失败       " + throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
//                Log.e("123", "         取消分享        ");
            }
        }); // 设置分享事件回调
        // 执行图文分享
        wechat.share(sp);
    }

    public static void SharePathWeiBo(String title, String imagePath, final String type) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setText(title);
        sp.setImagePath(imagePath);
        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                Toast.makeText(context, "已分享", Toast.LENGTH_SHORT).show();
                if ("1".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("121"));
                } else if ("2".equals(type)) {
                    EventBus.getDefault().post(new MessageEvent("122"));
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
//                Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Platform platform, int i) {
//                Toast.makeText(context, "取消分享", Toast.LENGTH_SHORT).show();
            }
        }); // 设置分享事件回调
        // 执行图文分享
        weibo.share(sp);
    }

    /**
     * 分享APP记录
     */
    protected static Subscription subscription;

    public static void add_share_logs_question(Context context, String question_id, String type, String answer_id) {
        Map<String, String> params = new HashMap<>();
        params.put("question_id", question_id);
        params.put("type", type);
        params.put("answer_id", answer_id);
        Log.e("分享APP记录：", params.toString());
        subscription = Network.getInstance("分享APP记录", context)
                .add_share_logs_request(params, new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                    @Override
                    public void onNext(Bean<List<String>> result) {
                        Log.e("123", "       分享APP记录     " + result.getMsg());

                    }

                    @Override
                    public void onError(String error) {
                        Log.e("123", "错误：" + error.toString());
                    }
                }, context, false));
    }


    private static String ShartStr(String strContent) {
        String content = strContent;
        if (TextUtils.isEmpty(strContent) && strContent != null) {
            if (content.contains("&nbsp;")) {
                content = content.replace("&nbsp;", " ");
            }
        }
        return content;
    }
}

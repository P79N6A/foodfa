package com.app.cookbook.xinhe.foodfamily.net;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.app.cookbook.xinhe.foodfamily.main.entity.AdviceLeixing;
import com.app.cookbook.xinhe.foodfamily.main.entity.AliEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.AnswerListOneData;
import com.app.cookbook.xinhe.foodfamily.main.entity.CommentLeveTwo;
import com.app.cookbook.xinhe.foodfamily.main.entity.FenLeiBiaoQianEn;
import com.app.cookbook.xinhe.foodfamily.main.entity.HotSearch;
import com.app.cookbook.xinhe.foodfamily.main.entity.HuiDaDate;
import com.app.cookbook.xinhe.foodfamily.main.entity.JiexiLei;
import com.app.cookbook.xinhe.foodfamily.main.entity.Label;
import com.app.cookbook.xinhe.foodfamily.main.entity.LocationDate;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageComment;
import com.app.cookbook.xinhe.foodfamily.main.entity.OtherUserEntity;
import com.app.cookbook.xinhe.foodfamily.main.entity.ReportMsg;
import com.app.cookbook.xinhe.foodfamily.main.entity.WoGuanZhuEntity;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.net.interceptor.LogInterceptor;
import com.app.cookbook.xinhe.foodfamily.net.services.MyService;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.CollectAnswer;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.DiscoverAttention;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.DiscoverBanner;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.DiscoverConfiguration;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.DiscoverRecommend;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.DraftDelete;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.ImageTextComment;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.VideoComment;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by shiyujia02 on 2017/8/16.
 */
public class Network {
    public static final int DEFAULT_TIMEOUT = 7;
    private static Network mInstance;
    public static String token = "";
    public MyService service;
    //测试服
    public static String main_url = "https://app1.shiyujia.com/";
    //正式服
//    public static String main_url = "https://app.shiyujia.com/";
//    public static String main_url = "https://appuat.shiyujia.com/";
    //public static String main_url = "https://appprd.shiyujia.com/";//临时测试正式服

    public static String bucketPath = "appDebug/";//阿里测试服图片库地址
//    public static String bucketPath = "appProduction/";//阿里正式服图片库地址

    //分享测试路径图片
    public static String ShareImage = "https://s3-011-shinho-syj-uat-bjs.s3.cn-north-1.amazonaws.com.cn/syjapp/2018_07/applogo.png";
    //分享正式路径图片
//    public static String ShareImage = "https://s3-014-shinho-syj-prd-bjs.s3.cn-north-1.amazonaws.com.cn/syjapp/2018_07/applogo.png";

    //分享网页测试地址
    public static String ShareUrl = Network.main_url +"answerPhone2/index.html#/";
    //    分享网页正式地址
//    public static String ShareUrl = Network.main_url +"answerPhone/index.html#/";
    //视频分享
    public static String VideoUrl = main_url + "answerPhone2/index.html?from=singlemessage#/videos?id=";
    public static String ImageTextUrl = main_url + "answerPhone2/index.html?from=singlemessage#/images?id=";

    //获取单例
    public static Network getInstance(String method, Context context) {
        if (context != null) {
            if ("".equals(SharedPreferencesHelper.get(context, "login_token", "").toString())) {
                token = "";
                Log.e("没有添加token", token);
            } else {
                token = SharedPreferencesHelper.get(context, "login_token", "").toString();
                Log.e("添加的头部的token", token);
            }
            if (mInstance == null) {
                synchronized (Network.class) {
                    mInstance = new Network(method, context);
                }
            }
        }
        return mInstance;
    }

    Retrofit retrofit;

    private Network(String method, Context context) {

        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};

        // Install the all-trusting trust manager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory())//去掉okhttp https证书验证
                .addInterceptor(new LogInterceptor(method))//添加日志拦截器
                .addInterceptor(new Interceptor() {//添加token
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("token", token)
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }
                })
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MINUTES)//设置写的超时时间
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MINUTES)//超时处理
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MINUTES)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Network.main_url)//设置请求网址根部
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        service = retrofit.create(MyService.class);

    }

    public static void trustAllHttpsCertificates()
            throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[1];
        trustAllCerts[0] = new TrustAllManager();
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(
                sc.getSocketFactory());
    }

    private static class TrustAllManager
            implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkServerTrusted(X509Certificate[] certs,
                                       String authType)
                throws CertificateException {
        }

        public void checkClientTrusted(X509Certificate[] certs,
                                       String authType)
                throws CertificateException {
        }
    }


    /**
     * 新增问题
     *
     * @param subscriber
     * @return
     */
    public Subscription add_quesiont(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.add_question(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 保存草稿
     *
     * @param subscriber
     * @return
     */
    public Subscription save_caogao(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.save_caogao(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 发布图文内容
     *
     * @param subscriber
     * @return
     */
    public Subscription fabu_content(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return service.fabu_content(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 保存草稿图文内容
     *
     * @param subscriber
     * @return
     */
    public Subscription save_content_caogao(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return service.fabu_content_caogao(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 编辑图文内容
     *
     * @param subscriber
     * @return
     */
    public Subscription save_content_bianji(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return service.bianji_content_caogao(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 编辑草稿内容
     *
     * @param subscriber
     * @return
     */
    public Subscription save_content_caogao_bianji(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return service.save_content_caogao_bianji(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 发布草稿内容
     *
     * @param subscriber
     * @return
     */
    public Subscription fabu_caogao_content(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return service.fabu_caogao_content(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 发布图文内容
     *
     * @param subscriber
     * @return
     */
    public Subscription fabu_movie(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return service.fabu_movie(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 保存编辑草稿
     *
     * @param subscriber
     * @return
     */
    public Subscription save_edit_caogao(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.save_edit_caogao(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 保存编辑草稿问题
     *
     * @param subscriber
     * @return
     */
    public Subscription fabu_edit_caogao(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.fabu_edit_caogao(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 保存编辑草稿答案
     *
     * @param subscriber
     * @return
     */
    public Subscription fabu_edit_caogao_answer(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.fabu_edit_caogao_answer(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 编辑草稿答案
     *
     * @param subscriber
     * @return
     */
    public Subscription bianji_caogao_answer(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.update_answer_caogao(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 保存答案草稿
     *
     * @param subscriber
     * @return
     */
    public Subscription save_answer_caogao(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.save_answer_caogao(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 新增问题
     *
     * @param subscriber
     * @return
     */
    public Subscription update_quesiont(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.update_question(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 新增回答
     *
     * @param subscriber
     * @return
     */
    public Subscription new_answer(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.new_answer(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 修改回答
     *
     * @param subscriber
     * @return
     */
    public Subscription update_answer(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.update_answer(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 热词点击
     *
     * @param subscriber
     * @return
     */
    public Subscription reci_click_number(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.reci_click_number(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 解析返回数据
     *
     * @param subscriber
     * @return
     */
    public Subscription jiexi(Subscriber<Bean<JiexiLei>> subscriber) {
        return service.jiexi()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);


    }

    /**
     * 获取验证码
     *
     * @param subscriber
     * @return
     */
    public Subscription get_yanzhengma(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.get_yanzhengma(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取验证码
     *
     * @param subscriber
     * @return
     */
    public Subscription get_login_yanzhengma(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.get_login_yanzhengma(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取验证码
     *
     * @param subscriber
     * @return
     */
    public Subscription get_update_password_yanzhengma(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.get_update_password_yanzhengma(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取验证码
     *
     * @param subscriber
     * @return
     */
    public Subscription get_xiugai_password_yanzhengma(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.get_xiugai_password_yanzhengma(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取修改手机号码验证码
     *
     * @param subscriber
     * @return
     */
    public Subscription get_xiugai_phone_yanzhengma(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.get_xiugai_phone_yanzhengma(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 快速注册
     *
     * @param subscriber
     * @return
     */
    public Subscription fastRegister(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.fast_register(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 修改密码
     *
     * @param subscriber
     * @return
     */
    public Subscription update_password(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.update_password(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 修改密码2
     *
     * @param subscriber
     * @return
     */
    public Subscription update_password_two(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.update_password_two(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 修改手机号
     *
     * @param subscriber
     * @return
     */
    public Subscription update_phone_number(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.update_phone_number(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 登录
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription login(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.fast_login(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }


    /**
     * 登录
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription yanzhengma_login(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.yanzhengma_login(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 登录
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription yanzhengma_login2(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.yanzhengma_login2(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 用户反馈
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription fankui(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return service.fankui(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 用户反馈
     *
     * @param subscriber
     * @return
     */
    public Subscription get_user_leixing(Subscriber<Bean<List<AdviceLeixing>>> subscriber) {
        return service.get_user_leixing()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 用户协议url
     *
     * @param subscriber
     * @return
     */
    public Subscription get_user_xieyi(Subscriber<Bean<Object>> subscriber) {
        return service.get_user_xieyi()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取阿里云的信息
     *
     * @param subscriber
     * @return
     */
    public Subscription get_ali(String url, Subscriber<AliEntity> subscriber) {
        return service.get_ali(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 设置个人信息
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription set_information(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.set_information(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取个人信息
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription get_user_information(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.get_user_information(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 发现美好列表
     *
     * @param subscriber
     * @return
     */
    public Subscription found_beauty_list(String page, Subscriber<Bean<Object>> subscriber) {
        return service.found_beauty(page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 发现详情detail
     *
     * @param subscriber
     * @return
     */
    public Subscription found_detail(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.found_detail(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 问题详情detail
     *
     * @param subscriber
     * @return
     */
    public Subscription question_detail(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.question_detail(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 答案详情detail
     *
     * @param subscriber
     * @return
     */
    public Subscription answer_detail(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.answer_detail(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取时间戳
     *
     * @param subscriber
     * @return
     */
    public Subscription get_time(Subscriber<Bean<String>> subscriber) {
        return service.get_time()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取消息首页,,,
     *
     * @param subscriber
     * @return
     */
    public Subscription get_location_shouye(Subscriber<Bean<Object>> subscriber) {
        return service.get_location_shouye()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取Ta的收藏
     *
     * @param subscriber
     * @return
     */
    public Subscription get_other_shoucang(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.other_shoucang_resource(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 获取最新回答
     *
     * @param subscriber
     * @return
     */
    public Subscription get_zuixin_answer(String message_id, Subscriber<Bean<Object>> subscriber) {
        return service.zuixin_huida(message_id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取我的收藏
     *
     * @param subscriber
     * @return
     */
    public Subscription get_my_shoucang(String page, Subscriber<Bean<Object>> subscriber) {
        return service.get_my_shoucang(page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取点赞列表
     *
     * @param subscriber
     * @return
     */
    public Subscription get_dianzan_list(int message_id, Subscriber<Bean<Object>> subscriber) {
        return service.get_dianzan_list(message_id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取他人信息
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription get_other_user_information(Map<String, String> params, Subscriber<Bean<OtherUserEntity>> subscriber) {
        return service.get_other_user_information(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取关注的标签
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription get_guanzhu_biaoqian(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.get_guanzhu_biaoqian(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 我获取关注的标签
     *
     * @return
     */
    public Subscription get_my_guanzhu_biaoqian(String page, Subscriber<Bean<Object>> subscriber) {
        return service.get_my_guanzhu_biaoqian(page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * Ta关注的标签
     *
     * @return
     */
    public Subscription get_my_guanzhu_biaoqian(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.get_other_follow_class(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 获取关注我的人
     *
     * @param subscriber
     * @return
     */
    public Subscription get_guanzhu_ren(String page, Subscriber<Bean<Object>> subscriber) {
        return service.get_guanzhu_wo_ren(page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取关注我的问题&人
     *
     * @param subscriber
     * @return
     */
    public Subscription get_guanzhu_question_ren(String messageid, Subscriber<Bean<Object>> subscriber) {
        return service.get_guanzhu_question(messageid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取系统消息
     *
     * @param subscriber
     * @return
     */
    public Subscription get_xitong_message(String message_id, Subscriber<Bean<LocationDate>> subscriber) {
        return service.get_xitong_xiaoxi(message_id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取系统详情
     *
     * @param subscriber
     * @return
     */
    public Subscription get_xitong_detail(String type, String id, Subscriber<Bean<List<String>>> subscriber) {
        return service.get_xitong_detail(type, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 删除系统消息
     *
     * @param subscriber
     * @return
     */
    public Subscription delete_xitong_message(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.delete_xitong_message(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 删除最新回答
     *
     * @param subscriber
     * @return
     */
    public Subscription delete_new_answer(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.delete_zuixin_answer(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 删除点赞&收藏
     *
     * @param subscriber
     * @return
     */
    public Subscription delete_dianzan_shoucang(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.delete_dianzan_shoucang(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 删除关注我&问题
     *
     * @param subscriber
     * @return
     */
    public Subscription delete_guanzhuwo_question(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.delete_guanzhuwo_question(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 删除关注我&问题
     *
     * @param subscriber
     * @return
     */
    public Subscription delete_commentList_question(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.delete_commentList_question(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取自定义分类
     *
     * @param subscriber
     * @return
     */
    public Subscription get_zidingyi_fenlei(String is_system, Subscriber<Bean<List<FenLeiBiaoQianEn>>> subscriber) {
        return service.get_zidingyifenlei(is_system)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取启动页
     *
     * @param subscriber
     * @return
     */
    public Subscription get_qidongye(Subscriber<Bean<Object>> subscriber) {
        return service.get_splash_page()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 版本统计
     *
     * @param subscriber
     * @return
     */
    public Subscription version_tongji(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.version_tongji(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 获取关注我的主页关注的问题
     *
     * @return
     */
    public Subscription get_guanzhu_mine_question(String page, Subscriber<Bean<Object>> subscriber) {
        return service.get_guanzhu_mine_question(page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 获取ta关注的问题
     *
     * @param subscriber
     * @return
     */
    public Subscription get_ta_guanzhu_question(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.get_ta_guanzhu_question(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取我提问
     *
     * @param subscriber
     * @return
     */
    public Subscription get_my_question(String page, Subscriber<Bean<Object>> subscriber) {
        return service.get_my_question(page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取ta的提问
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription get_ta_question(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.get_question(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * TAde 提问
     */
    public Subscription getOtherQuestion(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.get_other_question(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取提问列表
     *
     * @param keyword
     * @param subscriber
     * @return
     */
    public Subscription get_tiwen_liebiao(String page, String keyword, String question_id, Subscriber<Bean<Object>> subscriber) {
        return service.get_tiwen_liebiao(page, keyword, question_id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取提问列表
     *
     * @param subscriber
     * @return
     */
    public Subscription get_zuixin_liebiao(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.get_zuixin_liebiao(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 得到精选列表
     *
     * @param subscriber
     * @return
     */
    public Subscription get_jingxuan_liebiao(String PAGE, Subscriber<Bean<Object>> subscriber) {
        return service.get_jingxuan_liebiao(PAGE)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取关注列表
     *
     * @param subscriber
     * @return
     */
    public Subscription get_attention_home_liebiao(String PAGE, Subscriber<Bean<Object>> subscriber) {
        return service.attention_home_liebiao(PAGE)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 得到问题草稿列表
     *
     * @param subscriber
     * @return
     */
    public Subscription get_question_caogao_liebiao(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.get_question_caogao_liebiao(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 得到答案草稿列表
     *
     * @param subscriber
     * @return
     */
    public Subscription get_answer_caogao_liebiao(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.get_answer_caogao_liebiao(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 获取回答
     *
     * @param subscriber
     * @return
     */
    public Subscription get_answer(String page, Subscriber<Bean<HuiDaDate>> subscriber) {
        return service.get_answer(page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取ta回答
     *
     * @param subscriber
     * @return
     */
    public Subscription get_ta_answer(Map<String, String> params, Subscriber<Bean<HuiDaDate>> subscriber) {
        return service.get_ta_answer(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取我关注的人
     *
     * @param subscriber
     * @return
     */
    public Subscription get_wo_guanzhu_ren(String status, String page, String message, Subscriber<Bean<WoGuanZhuEntity>> subscriber) {
        return service.get_wo_guanzhu_ren(status, page, message)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取热搜列表
     *
     * @param subscriber
     * @return
     */
    public Subscription get_resou_date(Subscriber<Bean<List<HotSearch>>> subscriber) {
        return service.get_resou_date()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 获取ta关注的人
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription get_ta_guanzhu_ren(Map<String, String> params, Subscriber<Bean<WoGuanZhuEntity>> subscriber) {
        return service.get_ta_guanzhu_ren(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取ta关注的人
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription get_guanzhu_ta_ren(Map<String, String> params, Subscriber<Bean<WoGuanZhuEntity>> subscriber) {
        return service.get_guanzhu_ta_ren(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取我关注的人
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription guanzhuta(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.gaunzhuta(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 获取我关注的人
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription quxiaoguanzhuta(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.quxiaogaunzhuta(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取我关注的人
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription guanzhufenlei(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.guanzhufenlei(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 取消关注
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription quxiao_guanzhu(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.quxiao_guanzhufenlei(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 点赞
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription dianzan(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.dianzan(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 取消点赞
     *
     * @param subscriber
     * @return
     */
    public Subscription undianzan(String answer_id, String question_id, Subscriber<Bean<List<String>>> subscriber) {
        return service.undiatnzan(answer_id, question_id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 收藏
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription shoucang(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.shoucang(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 不收藏
     *
     * @param subscriber
     * @return
     */
    public Subscription unshoucang(String answer_id, String question_id, Subscriber<Bean<List<String>>> subscriber) {
        return service.unshoucang(answer_id, question_id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 关注问题
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription guanzhu_question(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.guanzhu_question(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 删除草稿箱答案
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription delete_answer_caozao(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.delete_answer_caozao(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 删除草稿箱问题
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription delete_question_caozao(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.delete_question_caozao(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 关注人
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription guanzhu_ren(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.guanzhu_ren(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 取消关注问题
     *
     * @return
     */
    public Subscription quxiao_question(String question_id, Subscriber<Bean<List<String>>> subscriber) {
        return service.quxiao_guanzhu_question(question_id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 新搜索提问
     */
    public Subscription get_issue_request(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.get_issue(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 新搜索回答
     */
    public Subscription get_answer_request(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.get_answer(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 搜索用户
     */
    public Subscription get_user_request(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.get_user(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 搜索标签
     */
    public Subscription search_biaoqian_resources(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.search_biaoqian_resources(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 举报问题
     */
    public Subscription report_question(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.reports_request(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取举报问题数据
     */
    public Subscription report_msg_request(Subscriber<Bean<List<ReportMsg>>> subscriber) {
        return service.report_msgs_request()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 举报答案
     */
    public Subscription reports_Answer_request(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.reports_Answer(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取举报答案数据 report/getReportImageType
     */
    public Subscription report_Answer_type_request(Subscriber<Bean<List<ReportMsg>>> subscriber) {
        return service.report_Answer_type()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 举报用户
     */
    public Subscription reports_user_request(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.reports_user(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取举报用户数据
     */
    public Subscription report_user_type_request(Subscriber<Bean<List<ReportMsg>>> subscriber) {
        return service.rswer_user_type()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 启动APP记录
     */
    public Subscription updata_request(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.updata_index(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 一级评论列表
     */
    public Subscription comment_level_one_request(Map<String, String> params, Subscriber<Bean<AnswerListOneData>> subscriber) {
        return service.comment_level_one(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 点赞评论
     */
    public Subscription cthumbs_comment_request(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.thumbs_comment(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 取消点赞评论
     */
    public Subscription un_thumbs_comment_request(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.un_thumbs_comment(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 一级评论列表
     */
    public Subscription comment_level_two_request(Map<String, String> params, Subscriber<Bean<CommentLeveTwo>> subscriber) {
        return service.comment_level_two(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 添加评论
     */
    public Subscription add_comment_request(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.add_comment(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 消息评论列表
     */
    public Subscription comment_list_request(String page, Subscriber<Bean<MessageComment>> subscriber) {
        return service.comment_list(page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 分享APP记录
     */
    public Subscription add_share_logs_request(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.add_share_logs(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 搜索标签
     */
    public Subscription search_class_request(Map<String, String> params, Subscriber<Bean<Label>> subscriber) {
        return service.search_class(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取用户排名
     */
    public Subscription user_ranking_request(Subscriber<Bean<Object>> subscriber) {
        return service.get_user_ranking()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 录入信息
     */
    public Subscription demo_xin_request(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.get_demo_xin(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 根据评论id 获取二轮评论列表页
     */
    public Subscription get_byId_request(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.get_comment_byid(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 验证邀请码
     */
    public Subscription invitation_code_request(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.check_invitation_code(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取热门标签
     */
    public Subscription get_hot_class_request(Subscriber<Bean<Object>> subscriber) {
        return service.get_hot_class()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取热门标签
     */
    public Subscription get_users_used_request(Subscriber<Bean<Object>> subscriber) {
        return service.get_users_used_class()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 立即登录
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription new_login(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.fast_new_login(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 删除答案
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription delete_daan_request(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.delete_daan(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 删除问题
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription delete_wenti_request(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.delete_wenti(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 删除回复
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription delete_comment_request(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.delete_comment(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 完善信息
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription user_data_request(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.user_data(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 值得关注的人
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription follow_users_request(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.follow_users(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 微信登陆
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription check_wechat_request(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.check_wechat(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 微博登陆
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription check_weibo_request(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.check_weibo(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 微信登陆绑定手机号
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription wechat_bind_phone_request(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.wechat_bind_phone(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 微博登陆绑定手机号
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription weibo_bind_phone_request(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.weibo_bind_phone(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 绑定手机号验证码
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription code_bind_phone_request(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.code_bind_phone(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 绑定微信
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription bind_wechat_request(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.bind_wechat(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 绑定微博
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription bind_weibo_request(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.bind_weibo(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 解除微信绑定
     *
     * @param subscriber
     * @return
     */
    public Subscription untie_wechat_request(Subscriber<Bean<Object>> subscriber) {
        return service.untie_wechat()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 解除微信绑定
     *
     * @param subscriber
     * @return
     */
    public Subscription untie_weibo_request(Subscriber<Bean<Object>> subscriber) {
        return service.untie_weibo()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 首页
     *
     * @param subscriber
     * @return
     */
    public Subscription get_home(String PAGE, Subscriber<Bean<Object>> subscriber) {
        return service.get_home(PAGE)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 首页图文点赞
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription home_dianzan(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.home_dianzan(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 首页视频点赞
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription home_video_dianzan(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.home_video_dianzan(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 圖文取消点赞
     *
     * @param subscriber
     * @return
     */
    public Subscription home_undianzan(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.home_undiatnzan(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 首页视频点赞
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription home_videoThumbs(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.home_video_thumbs(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 视频取消点赞
     *
     * @param subscriber
     * @return
     */
    public Subscription home_videoUnthumbs(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.home_video_unthumbs(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 首页图文收藏成功
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription home_shoucang(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.home_shoucang(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 首页图文取消收藏
     *
     * @param subscriber
     * @return
     */
    public Subscription home_unshoucang(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.home_unshoucang(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 首页视频收藏成功
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription home_videoCollect(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.home_videoCollect(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 首页视频取消收藏
     *
     * @param subscriber
     * @return
     */
    public Subscription home_videoUncollect(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.home_videoUncollect(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 图文详情
     *
     * @param subscriber
     * @return
     */
    public Subscription getImageTextDetail(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.Image_Text_Detail(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 视频详情
     *
     * @param subscriber
     * @return
     */
    public Subscription getVideoDetails(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.video_details(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 图文评论
     *
     * @param subscriber
     * @return
     */
    public Subscription getImageTextComment(Map<String, String> params, Subscriber<Bean<ImageTextComment>> subscriber) {
        return service.Image_Text_Comment(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 视频评论
     *
     * @param subscriber
     * @return
     */
    public Subscription getVideoComment(Map<String, String> params, Subscriber<Bean<VideoComment>> subscriber) {
        return service.get_video_comment(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 举报图文
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription getReportImage(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.reportImage(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 举报視頻
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription getReportVideo(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.reportVideo(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 举报图文类型
     */
    public Subscription getReportImageType(Subscriber<Bean<List<ReportMsg>>> subscriber) {
        return service.get_report_image_type()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 举报視頻类型
     */
    public Subscription getReportVideoType(Subscriber<Bean<List<ReportMsg>>> subscriber) {
        return service.reportVideoType()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 删除视频
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription getDeleteVideo(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.deleteVideo(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 删除视频
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription getDeleteImage(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.deleteImage(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * draftDelete 删除草稿
     */
    public Subscription getDraftDelete(Map<String, String> params, Subscriber<Bean<DraftDelete>> subscriber) {
        return service.draftDelete(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 添加图文评论
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription getAddComment(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.addComment(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 添加视频评论
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription getAddCommentVideo(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.addCommentVideo(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 删除图文评论
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription getDeleteComment(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.deleteComment(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 取消图文点赞评论
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription getUnthumbsComment(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.unthumbsComment(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 图文点赞评论
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription getThumbsComment(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.thumbsComment(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 图文点赞评论
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription getAddVideoPlay(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.addVideoPlay(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 取消视频点赞评论
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription getUnthumbsVideoComment(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.unthumbsVideoComment(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 视频点赞评论
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription getThumbsCommentVideo(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.thumbsCommentVideo(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 删除视频评论
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription getDeleteVideoComment(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.deleteVideoComment(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 删除图文评论
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription getDeleteImageComment(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.deleteImageComment(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 发现轮播图
     *
     * @param subscriber
     * @return
     */
    public Subscription getBannerList(Subscriber<Bean<List<DiscoverBanner>>> subscriber) {
        return service.banner_list()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 发现名称 配置
     *
     * @param subscriber
     * @return
     */
    public Subscription getFindName(Subscriber<Bean<List<DiscoverConfiguration>>> subscriber) {
        return service.find_name()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 发现推荐标签
     *
     * @param subscriber
     * @return
     */
    public Subscription getRecommendClass(String PAGE, Subscriber<Bean<Object>> subscriber) {
        return service.recommend_class(PAGE)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 图文评论回复
     *
     * @param subscriber
     * @return
     */
    public Subscription getImageTextCommentList(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.image_text_commentList(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 视频评论回复
     *
     * @param subscriber
     * @return
     */
    public Subscription getVideoCommentList(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.video_commentList(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 发现详情
     *
     * @param subscriber
     * @return
     */
    public Subscription getClassDetail(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.class_detail(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 标签下的印迹
     *
     * @param subscriber
     * @return
     */
    public Subscription getClassImprinting(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.class_imprinting(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 标签下的印迹
     *
     * @param subscriber
     * @return
     */
    public Subscription getClassQuestion(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.class_question(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 登陆者信息
     *
     * @param subscriber
     * @return
     */
    public Subscription getOwnInformation(Subscriber<Bean<Object>> subscriber) {
        return service.own_information()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 好友主页信息
     *
     * @param subscriber
     * @return
     */
    public Subscription getOtherInformation(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.other_information(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 我发布的印迹
     *
     * @param subscriber
     * @return
     */
    public Subscription getOwnImprinting(String PAGE, Subscriber<Bean<Object>> subscriber) {
        return service.own_imprinting(PAGE)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 我的收藏问答
     *
     * @param subscriber
     * @return
     */
    public Subscription getOwnCollectAnswer(String PAGE, Subscriber<Bean<Object>> subscriber) {
        return service.own_collectAnswer(PAGE)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 我的收藏菜谱
     *
     * @param subscriber
     * @return
     */
    public Subscription getOwnCollectCaipu(String PAGE, Subscriber<Bean<Object>> subscriber) {
        return service.own_collectRecipe(PAGE)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * TA的收藏的问答
     *
     * @param subscriber
     * @return
     */
    public Subscription getOtherCollectAnswer(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.other_collect_answer(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 我的收藏的印迹
     *
     * @param subscriber
     * @return
     */
    public Subscription getOwnCollectImprinting(String PAGE, Subscriber<Bean<Object>> subscriber) {
        return service.own_collect_imprinting(PAGE)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * TA的收藏的印迹
     *
     * @param subscriber
     * @return
     */
    public Subscription getOtherCollectImprinting(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.other_collect_imprinting(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * TA发布的印迹
     *
     * @param subscriber
     * @return
     */
    public Subscription getOtherImprinting(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.other_imprinting(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 修改wifi状态
     *
     * @param subscriber
     * @return
     */
    public Subscription getUpdateWifi(Map<String, String> params, Subscriber<Bean<List<String>>> subscriber) {
        return service.update_wifi(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 最新答案
     *
     * @param PAGE
     * @param subscriber
     * @return
     */
    public Subscription get_answer_list(String PAGE, Subscriber<Bean<Object>> subscriber) {
        return service.get_answer_list(PAGE)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 最新问题
     *
     * @param PAGE
     * @param subscriber
     * @return
     */
    public Subscription get_question_list(String PAGE, Subscriber<Bean<Object>> subscriber) {
        return service.get_question_list(PAGE)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 草稿箱印迹
     *
     * @param PAGE
     * @param subscriber
     * @return
     */
    public Subscription get_draft_list(String PAGE, Subscriber<Bean<Object>> subscriber) {
        return service.get_draft_list(PAGE)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}

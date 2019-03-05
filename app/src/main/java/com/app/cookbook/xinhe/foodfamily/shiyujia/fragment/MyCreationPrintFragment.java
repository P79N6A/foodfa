package com.app.cookbook.xinhe.foodfamily.shiyujia.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.callback.DialogCallBack;
import com.app.cookbook.xinhe.foodfamily.main.LoginActivity;
import com.app.cookbook.xinhe.foodfamily.main.PhotoBrowserActivity;
import com.app.cookbook.xinhe.foodfamily.main.adapter.AnswerFunctionAdapter;
import com.app.cookbook.xinhe.foodfamily.main.adapter.ReportMsgAdapter;
import com.app.cookbook.xinhe.foodfamily.main.adapter.ShareImageItemAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.FunctionItem;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageEvent;
import com.app.cookbook.xinhe.foodfamily.main.entity.ReportMsg;
import com.app.cookbook.xinhe.foodfamily.main.entity.ShareImageItem;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.AddImagesActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.ImageTextDetailActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.VideoDetailsActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.HomeAdapter;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.HomeItem;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.ImageTextDetails;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.JudgeWifi;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.MD5Utils;
import com.app.cookbook.xinhe.foodfamily.util.PopWindowHelper;
import com.app.cookbook.xinhe.foodfamily.util.ShareUtil;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.snackbar.BaseTransientBottomBar;
import com.app.cookbook.xinhe.foodfamily.util.snackbar.TopSnackBar;
import com.orhanobut.logger.Logger;
import com.tencent.stat.StatService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import chuangyuan.ycj.videolibrary.video.ExoUserPlayer;
import chuangyuan.ycj.videolibrary.video.VideoPlayerManager;
import rx.Subscription;

/**
 * Created by wenlaisu on 2018/4/12.
 */
@SuppressLint("ValidFragment")
public class MyCreationPrintFragment extends Fragment {

    protected Subscription subscription;
    @BindView(R.id.amount_tv)
    TextView amount_tv;
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.empty_layout)
    RelativeLayout empty_layout;
    @BindView(R.id.image_empty)
    ImageView image_empty;
    @BindView(R.id.tv_empty)
    TextView tv_empty;

    private View mView;
    private LinearLayout ll;
    private LinearLayoutManager layoutManager;

    private HomeAdapter homeAdapter;
    private List<HomeItem.DataBean> homeItems = new ArrayList<>();
    private HomeItem.DataBean dataItem;
    private List<String> imageList = new ArrayList<>();

    //分享数据
    private int[] moreImageids = {R.drawable.icon_wechat, R.drawable.icon_wechat_moments, R.drawable.icon_weibo, R.drawable.icon_qq, R.drawable.icon_qqzone};
    private String[] moreName = {"微信", "朋友圈", "新浪微博", "QQ", "QQ空间"};
    //分享数据集合
    private List<ShareImageItem> moreListem = new ArrayList<>();
    //删除，编辑，举报
    private int[] functionImageids = {R.drawable.bianji, R.drawable.more_shanchu, R.drawable.jubao};
    private String[] functionName = {"编辑", "删除", "举报"};
    private List<FunctionItem> functionListem = new ArrayList<>();

    //获取详情数据
    private ImageTextDetails imageTextDetails;
    /***
     * 举报
     */
    private LinearLayoutManager linearLayoutManager;
    private ReportMsgAdapter reportMsgAdapter;
    private List<ReportMsg> data = new ArrayList<>();
    private TextView tv_report;
    private TextView report_title;

    private int PAGE = 1;
    //是否有更多
    private boolean is_not_more;
    private int pos;
    private String image_id;
    private String is_thumbs;
    private String is_delete;
    private String Uid;
    private String is_update;
    private String url = "";
    private String ImageUrl;
    //监听条目滑动状态
    private String facePath;
    //记录滑动位置
    private int currenItem = 0;
    private int currenUp = 0;
    private int curren = 0;
    public int firstVisibleItem, lastVisibleItem, visibleCount;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent;
            dataItem = homeItems.get(msg.arg1);
            pos = msg.arg1;
            image_id = dataItem.getTotal_id();
            is_thumbs = dataItem.getIs_thumbs();
            Uid = dataItem.getUser_data().getId();
            Log.e("123", "     msg.what      " + msg.what);
            switch (msg.what) {
                case 400:
                    intent = new Intent(getActivity(), FriendPageActivity.class);
                    intent.putExtra("UserId", dataItem.getUser_id());
                    startActivity(intent);
                    break;
                case 401:
                    getImageText(dataItem.getTotal_id());
                    is_delete = dataItem.getIs_existence();
                    is_update = dataItem.getIs_update();
                    if (functionListem.size() > 0) {
                        functionListem.clear();
                    }
                    select_more_popwindow(dataItem, dataItem.getStatus());
                    break;
                case 402:
                    if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        if (dataItem.getStatus() == 1) {
                            VideoDianZanNet(image_id, "1");
                        } else {
                            DianZanNet(image_id, "1");
                        }
                    }
                    break;
                case 4021:
                    if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        if (dataItem.getStatus() == 1) {
                            VideoUnDianZanNet(image_id);
                        } else {
                            UnDianZanNet(image_id);
                        }
                    }
                    break;
                case 403:
                    if (dataItem.getStatus() == 1) {
                        if (dataItem.getVideo_data() != null) {
                            facePath = dataItem.getVideo_data().getFace_path();
                            if ("1".equals(dataItem.getVideo_data().getIs_transcoding())) {
                                url = dataItem.getVideo_data().getVideo_path();
                                ImageUrl = dataItem.getVideo_data().getVideo_path();
                            } else if ("2".equals(dataItem.getVideo_data().getIs_transcoding())) {
                                String path = dataItem.getVideo_data().getTranscoding_data().get(0).getTranscoding_path();
                                ImageUrl = dataItem.getVideo_data().getTranscoding_data().get(0).getTranscoding_path();
                                //链接头部
                                String urlHead = path.substring(0, path.indexOf(".com")) + ".com";
                                //链接中间
                                String urlMiddle = path.substring(25, path.indexOf("?"));
                                //獲取PHP時間戳
                                String time = String.valueOf(Calendar.getInstance().getTimeInMillis());
                                String newTime = time.substring(0, 10);
//                    Log.e("123", "   urlTail   " + urlMiddle + "-" + newTime + "-0-0-SHoXEVJALKeRij7JBnLgAIEDMPPKwgL2");
                                String newUrl = MD5Utils.encode(urlMiddle + "-" + newTime + "-0-0-SHoXEVJALKeRij7JBnLgAIEDMPPKwgL2");
//                    Log.e("123", "   newUrl   " + newUrl);
//                    Log.e("123", "   Url   " + urlHead + urlMiddle + "?auth_key=" + newTime + "-0-0-" + newUrl);
                                url = urlHead + urlMiddle + "?auth_key=" + newTime + "-0-0-" + newUrl;
                            }

                            long currPosition = 0;
                            ExoUserPlayer manualPlayer = VideoPlayerManager.getInstance().getVideoPlayer();
                            if (manualPlayer != null) {
                                currPosition = manualPlayer.getCurrentPosition();
                            }
                            Contacts.currItem = msg.arg1;
                            intent = new Intent(getActivity(), VideoDetailsActivity.class);
                            intent.putExtra("id", dataItem.getTotal_id());
                            intent.putExtra("url", url);
                            intent.putExtra("ImageUrl", ImageUrl);
                            intent.putExtra("handImage", facePath);
                            intent.putExtra("currPosition", currPosition);
                            intent.putExtra("tan", "1");
                            startActivity(intent);
                        } else {
                            intent = new Intent(getActivity(), VideoDetailsActivity.class);
                            intent.putExtra("id", dataItem.getTotal_id());
                            intent.putExtra("tan", "1");
                            startActivity(intent);
                        }
                    } else {
                        intent = new Intent(getActivity(), ImageTextDetailActivity.class);
                        intent.putExtra("id", dataItem.getTotal_id());
                        intent.putExtra("tan", "1");
                        startActivity(intent);
                    }
                    break;
                case 404:
                    if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        if (dataItem.getStatus() == 1) {
                            VideoShouCangNet(image_id);
                        } else {
                            ShouCangNet(image_id);
                        }
                    }
                    break;
                case 4041:
                    if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        if (dataItem.getStatus() == 1) {
                            VideoUnShouCangNet(image_id);
                        } else {
                            UnShouCangNet(image_id);
                        }
                    }
                    break;
                case 405:
                    getImageText(dataItem.getTotal_id());
                    is_delete = dataItem.getIs_existence();
                    is_update = dataItem.getIs_update();
                    if (functionListem.size() > 0) {
                        functionListem.clear();
                    }
                    select_more_popwindow(dataItem, dataItem.getStatus());
                    break;
                case 406:
                    image_id = dataItem.getTotal_id();
                    is_thumbs = dataItem.getIs_thumbs();
                    break;
                case 4061:
                    if (dataItem != null && dataItem.getImage_data() != null) {
                        String strings[] = new String[dataItem.getImage_data().size()];
                        for (int i = 0; i < dataItem.getImage_data().size(); i++) {
                            strings[i] = dataItem.getImage_data().get(i).getPath();
                        }
                        if (strings.length > 0) {
                            intent = new Intent();
                            intent.putExtra("imageUrls", strings);
                            intent.putExtra("curImageUrl", dataItem.getUser_data().getAvatar());
                            intent.putExtra("imageType", "2");
                            intent.setClass(getActivity(), PhotoBrowserActivity.class);
                            startActivity(intent);
                        }
                    }
                    break;
                case 407:
                    if (dataItem.getVideo_data() != null) {
                        facePath = dataItem.getVideo_data().getFace_path();
                        if ("1".equals(dataItem.getVideo_data().getIs_transcoding())) {
                            url = dataItem.getVideo_data().getVideo_path();
                            ImageUrl = dataItem.getVideo_data().getVideo_path();
                        } else if ("2".equals(dataItem.getVideo_data().getIs_transcoding())) {
                            String path = dataItem.getVideo_data().getTranscoding_data().get(0).getTranscoding_path();
                            ImageUrl = dataItem.getVideo_data().getTranscoding_data().get(0).getTranscoding_path();
                            //链接头部
                            String urlHead = path.substring(0, path.indexOf(".com")) + ".com";
                            //链接中间
                            String urlMiddle = path.substring(25, path.indexOf("?"));
                            //獲取PHP時間戳
                            String time = String.valueOf(Calendar.getInstance().getTimeInMillis());
                            String newTime = time.substring(0, 10);
//                    Log.e("123", "   urlTail   " + urlMiddle + "-" + newTime + "-0-0-SHoXEVJALKeRij7JBnLgAIEDMPPKwgL2");
                            String newUrl = MD5Utils.encode(urlMiddle + "-" + newTime + "-0-0-SHoXEVJALKeRij7JBnLgAIEDMPPKwgL2");
//                    Log.e("123", "   newUrl   " + newUrl);
//                    Log.e("123", "   Url   " + urlHead + urlMiddle + "?auth_key=" + newTime + "-0-0-" + newUrl);
                            url = urlHead + urlMiddle + "?auth_key=" + newTime + "-0-0-" + newUrl;
                        }

                        long currPosition = 0;
                        ExoUserPlayer manualPlayer = VideoPlayerManager.getInstance().getVideoPlayer();
                        if (manualPlayer != null) {
                            currPosition = manualPlayer.getCurrentPosition();
                        }
                        Contacts.currItem = msg.arg1;
                        intent = new Intent(getActivity(), VideoDetailsActivity.class);
                        intent.putExtra("id", dataItem.getTotal_id());
                        intent.putExtra("url", url);
                        intent.putExtra("ImageUrl", ImageUrl);
                        intent.putExtra("handImage", facePath);
                        intent.putExtra("currPosition", currPosition);
                        startActivity(intent);
                    } else {
                        intent = new Intent(getActivity(), VideoDetailsActivity.class);
                        intent.putExtra("id", dataItem.getTotal_id());
                        startActivity(intent);
                    }
                    break;
                case 408:
                    intent = new Intent(getActivity(), ImageTextDetailActivity.class);
                    intent.putExtra("id", dataItem.getTotal_id());
                    startActivity(intent);
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.recyclerview_list, container, false);
            ButterKnife.bind(this, mView);
            EventBus.getDefault().register(this);
            initView();
            initJingXuanDate(false);
        }
        return mView;
    }


    public LinearLayout getLl() {
        return ll;
    }

    private void initView() {
        if (JudgeWifi.isWifiAvailable(getActivity()) == true ||
                "2".equals(SharedPreferencesHelper.get(getActivity(), "isWiFI", ""))) {
            Contacts.isPlay = true;
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                if (JudgeWifi.isWifiAvailable(getActivity()) == true||
//                        "2".equals(SharedPreferencesHelper.get(getActivity(), "isWiFI", ""))) {
//                    Contacts.isPlay = false;
//                }

                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                visibleCount = lastVisibleItem - firstVisibleItem;
//                Log.e("123", "   firstVisibleItem    " + firstVisibleItem + "    lastVisibleItem    " + lastVisibleItem
//                        + "       getCurrentViewIndex()     " + getCurrentViewIndex());

                if (firstVisibleItem == 0) {
                    currenItem = firstVisibleItem;
                    Contacts.isVideoPlay = "1";
                    homeAdapter.notifyDataSetChanged();
                }
//                else if (dy < 0) {
//                    if (getCurrentViewIndex() >= firstVisibleItem) {
//                        if (currenUp == 0) {
//                            currenItem = getCurrentViewIndex();
////                            Contacts.isVideoPlay = "1";
////                            homeAdapter.notifyDataSetChanged();
//                            currenUp = 1;
//                        }
//                    }
//                } else if (dy > 0) {
//                    if (getCurrentViewIndex() >= firstVisibleItem) {
//                        if (curren == 0) {
//                            currenItem = firstVisibleItem;
////                            Contacts.isVideoPlay = "1";
////                            homeAdapter.notifyDataSetChanged();
//                            curren = 1;
//                        }
//                    }
//                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        if (JudgeWifi.isWifiAvailable(getActivity()) == true ||
                                "2".equals(SharedPreferencesHelper.get(getActivity(), "isWiFI", ""))) {
                            Contacts.isPlay = true;
                        }
                        if (homeItems.get(getCurrentViewIndex()).getStatus() == 1) {
                            if (lastVisibleItem == getCurrentViewIndex()) {
                                Contacts.isVideoPlay = "1";
                                homeAdapter.notifyDataSetChanged();
                            }
                        }
//                        if (lastVisibleItem == getCurrentViewIndex()) {
//                            if (firstVisibleItem == getCurrentViewIndex() && lastVisibleItem == getCurrentViewIndex()) {
//                                Contacts.isVideoPlay = "1";
//                                homeAdapter.notifyDataSetChanged();
//                                curren = 0;
//                            } else if (firstVisibleItem == getCurrentViewIndex() && lastVisibleItem > getCurrentViewIndex()) {
//                                if ("0".equals(Contacts.isVideoPlay)) {
//                                    Contacts.isVideoPlay = "1";
//                                    homeAdapter.notifyDataSetChanged();
//                                    currenUp = 0;
//                                }
//                            }
////                            Log.e("123", "   Contacts.isVideoPlay     " + Contacts.isVideoPlay);
//                        }
//                        if (lastVisibleItem > getCurrentViewIndex()) {
//                            if (firstVisibleItem == getCurrentViewIndex() && lastVisibleItem == getCurrentViewIndex()) {
//                                Contacts.isVideoPlay = "1";
//                                homeAdapter.notifyDataSetChanged();
//                                currenUp = 0;
//                            } else if (firstVisibleItem == getCurrentViewIndex() && lastVisibleItem > getCurrentViewIndex()) {
//                                if ("0".equals(Contacts.isVideoPlay)) {
//                                    Contacts.isVideoPlay = "1";
//                                    homeAdapter.notifyDataSetChanged();
//                                    currenUp = 0;
//                                }
//                            }
//                            Log.e("123", "   ---->Contacts.isVideoPlay     " + Contacts.isVideoPlay);
//                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        // 持续滚动开始，只触发一次
                        if (JudgeWifi.isWifiAvailable(getActivity()) == true ||
                                "2".equals(SharedPreferencesHelper.get(getActivity(), "isWiFI", ""))) {
                            Contacts.isPlay = false;
                            Contacts.isVideoPlay = "0";
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        // 持续滚动开始，只触发一次
                        if (JudgeWifi.isWifiAvailable(getActivity()) == true ||
                                "2".equals(SharedPreferencesHelper.get(getActivity(), "isWiFI", ""))) {
                            Contacts.isPlay = false;
                            Contacts.isVideoPlay = "0";
                        }
                        break;
                }
            }
        });
    }

    //订阅方法，当接收到事件的时候，会调用该方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        switch (messageEvent.getMessage()) {
            case "122":
                ShareUtil.add_share_logs_question(getActivity(), "", ShareType, image_id);
                TopSnackBar.make(ll, "分享成功", BaseTransientBottomBar.LENGTH_SHORT, getActivity()).show();

                Properties prop = new Properties();
                prop.setProperty("share_time", "分享后返回时长");
                StatService.trackCustomEndKVEvent(getActivity(), "Share_back_time", prop);
                break;
            case "501":
                if (homeItems.get(pos).getStatus() == 1) {
                    if ("2".equals(homeItems.get(pos).getIs_thumbs())) {
                        VideoDianZanNet(homeItems.get(pos).getTotal_id(), "1");
                    }
                } else {
                    if ("2".equals(homeItems.get(pos).getIs_thumbs())) {
                        DianZanNet(homeItems.get(pos).getTotal_id(), "2");
                    }
                }
                break;
        }
    }

    public int getCurrentViewIndex() {
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        int currentIndex = firstVisibleItem;
        int lastHeight = 0;
        for (int i = firstVisibleItem; i <= lastVisibleItem; i++) {
            View view = layoutManager.getChildAt(i - firstVisibleItem);
            if (null == view) {
                continue;
            }
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            Rect localRect = new Rect();
            view.getLocalVisibleRect(localRect);
            int showHeight = localRect.bottom - localRect.top;
            if (showHeight > lastHeight) {
                currentIndex = i;
                lastHeight = showHeight;
            }
        }

        if (currentIndex < 0) {
            currentIndex = 0;
        }
        return currentIndex;
    }


    private void initJingXuanDate(boolean is_true) {
        subscription = Network.getInstance("印迹", getActivity())
                .getOwnImprinting(String.valueOf(PAGE),
                        new ProgressSubscriberNew<>(HomeItem.class, new GsonSubscriberOnNextListener<HomeItem>() {
                            @Override
                            public void on_post_entity(HomeItem result) {
                                Log.e("123", "     印迹      " + result.getData().size());
                                amount_tv.setVisibility(View.VISIBLE);
                                amount_tv.setText("共" + result.getTotal() + "条印迹");
                                if (homeItems.size() > 0 && PAGE == 1) {
                                    homeItems.clear();
                                }
                                if (PAGE == 1 && result.getData().size() > 0) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    empty_layout.setVisibility(View.GONE);
                                } else if (PAGE == 1 && result.getData().size() == 0) {
                                    image_empty.setImageResource(R.drawable.icon_yinji_null);
                                    tv_empty.setText("还没有创作印迹呦～");
                                    empty_layout.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                                if (homeItems.size() > 0) {//这表示是"加载"
                                    if (result.getData().size() == 0) {
                                        is_not_more = true;
                                        homeItems.addAll(result.getData());
                                        homeAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        homeItems.addAll(result.getData());
                                        homeAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    homeItems = result.getData();
                                    set_list_resource(homeItems);
                                }
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                if ("无数据".equals(error)) {
                                }
                                if (homeItems.size() == 0 && error.contains("无") || "断网".equals(error)) {
                                    image_empty.setImageResource(R.drawable.icon_yinji_null);
                                    tv_empty.setText("还没有创作印迹呦～");
                                    empty_layout.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                                Log.e("123", "   印迹报错  " + error);
                            }
                        }, getActivity(), is_true));
    }

    //点赞
    private void DianZanNet(String image_text_id, final String type) {
        Map<String, String> params = new HashMap<>();
        params.put("image_text_id", image_text_id);
        Log.e("点赞：", params.toString());
        subscription = Network.getInstance("点赞成功", getActivity())
                .home_dianzan(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "       点赞成功        ");
                                Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
//                                int comment_number = Integer.valueOf(dataItem.getThumb_number());
//                                dataItem.setThumb_number(String.valueOf(comment_number + 1));
//                                homeItems.get(pos).setIs_thumbs("1");
//                                homeAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                if ("1".equals(type)) {
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, getActivity(), false));
    }

    //取消点赞
    private void UnDianZanNet(String image_text_id) {
        Map<String, String> params = new HashMap<>();
        params.put("image_text_id", image_text_id);
        Log.e("取消点赞：", params.toString());
        subscription = Network.getInstance("取消点赞", getActivity())
                .home_undianzan(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
                                Log.e("123", "取消点赞成功：" + result.getCode());
//                                int comment_number = Integer.valueOf(dataItem.getThumb_number());
//                                dataItem.setThumb_number(String.valueOf(comment_number - 1));
//                                homeItems.get(pos).setIs_thumbs("2");
//                                homeAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            }
                        }, getActivity(), false));
    }

    //收藏
    private void ShouCangNet(String image_text_id) {
        Map<String, String> params = new HashMap<>();
        params.put("image_text_id", image_text_id);
        Log.e("收藏：", params.toString());
        subscription = Network.getInstance("收藏", getActivity())
                .home_shoucang(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
                                Log.e("123", "收藏成功：" + result.getCode());
//                                int Collection_number = Integer.valueOf(dataItem.getCollection_number());
//                                dataItem.setCollection_number(String.valueOf(Collection_number + 1));
//                                homeItems.get(pos).setIs_collect("1");
//                                homeAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            }
                        }, getActivity(), false));
    }

    //取消收藏
    private void UnShouCangNet(String image_text_id) {
        Map<String, String> params = new HashMap<>();
        params.put("image_text_id", image_text_id);
        Log.e("取消收藏：", params.toString());
        subscription = Network.getInstance("取消收藏", getActivity())
                .home_unshoucang(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "取消收藏成功：" + result.getCode());
                                Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
//                                int Collection_number = Integer.valueOf(dataItem.getCollection_number());
//                                dataItem.setCollection_number(String.valueOf(Collection_number - 1));
//                                homeItems.get(pos).setIs_collect("2");
//                                homeAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            }
                        }, getActivity(), false));
    }

    //视频点赞
    private void VideoDianZanNet(String video_id, final String type) {
        Map<String, String> params = new HashMap<>();
        params.put("video_id", video_id);
        Log.e("点赞：", params.toString());
        subscription = Network.getInstance("点赞成功", getActivity())
                .home_videoThumbs(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "       点赞成功        ");
                                Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
//                                int comment_number = Integer.valueOf(dataItem.getThumb_number());
//                                dataItem.setThumb_number(String.valueOf(comment_number + 1));
//                                homeItems.get(pos).setIs_thumbs("1");
//                                homeAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                if ("1".equals(type)) {
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, getActivity(), false));
    }

    //视频取消点赞
    private void VideoUnDianZanNet(String video_id) {
        Map<String, String> params = new HashMap<>();
        params.put("video_id", video_id);
        Log.e("取消点赞：", params.toString());
        subscription = Network.getInstance("取消点赞", getActivity())
                .home_videoUnthumbs(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "取消点赞成功：" + result.getCode());
                                Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
//                                int comment_number = Integer.valueOf(dataItem.getThumb_number());
//                                dataItem.setThumb_number(String.valueOf(comment_number - 1));
//                                homeItems.get(pos).setIs_thumbs("2");
//                                homeAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            }
                        }, getActivity(), false));
    }

    //视频收藏
    private void VideoShouCangNet(String video_id) {
        Map<String, String> params = new HashMap<>();
        params.put("video_id", video_id);
        Log.e("收藏：", params.toString());
        subscription = Network.getInstance("收藏", getActivity())
                .home_videoCollect(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "收藏成功：" + result.getCode());
                                Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
//                                int Collection_number = Integer.valueOf(dataItem.getCollection_number());
//                                dataItem.setCollection_number(String.valueOf(Collection_number + 1));
//                                homeItems.get(pos).setIs_collect("1");
//                                homeAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            }
                        }, getActivity(), false));
    }

    //视频取消收藏
    private void VideoUnShouCangNet(String video_id) {
        Map<String, String> params = new HashMap<>();
        params.put("video_id", video_id);
        Log.e("取消收藏：", params.toString());
        subscription = Network.getInstance("取消收藏", getActivity())
                .home_videoUncollect(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "取消收藏成功：" + result.getCode());
                                Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
//                                int Collection_number = Integer.valueOf(dataItem.getCollection_number());
//                                dataItem.setCollection_number(String.valueOf(Collection_number - 1));
//                                homeItems.get(pos).setIs_collect("2");
//                                homeAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            }
                        }, getActivity(), false));
    }

    private void getImageText(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        Log.e("图文详情：", params.toString());
        subscription = Network.getInstance("图文详情", getActivity())
                .getImageTextDetail(params,
                        new ProgressSubscriberNew<>(ImageTextDetails.class, new GsonSubscriberOnNextListener<ImageTextDetails>() {
                            @Override
                            public void on_post_entity(ImageTextDetails result) {
                                Log.e("123", "     图文详情      " + result);
                                imageTextDetails = result;
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "        图文详情报错    " + error);
                            }
                        }, getActivity(), false));
    }


    private void set_list_resource(final List<HomeItem.DataBean> dates) {
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        homeAdapter = new HomeAdapter(getActivity(), handler);
        homeAdapter.setHomeItems(homeItems);
        homeAdapter.setImageList(imageList);
        recyclerView.setAdapter(homeAdapter);
        recyclerView.setHasFixedSize(true);
        // 设置静默加载模式
        // 设置静默加载模式
        xrefreshview.setSilenceLoadMore(true);
        //设置刷新完成以后，headerview固定的时间
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);
        xrefreshview.setPullRefreshEnable(true);
        xrefreshview.setPullLoadEnable(true);
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.enableRecyclerViewPullUp(true);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
        //给recycler_view设置底部加载布局
        xrefreshview.enableReleaseToLoadMore(true);
        xrefreshview.enableRecyclerViewPullUp(true);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
        xrefreshview.setPreLoadCount(10);
        if (homeItems.size() > 7) {
            xrefreshview.enableReleaseToLoadMore(true);
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
                        //填写刷新数据的网络请求，一般page=1，List集合清空操作
                        PAGE = 1;
                        initJingXuanDate(true);
                        xrefreshview.stopRefresh();//刷新停止

                    }
                }, 1000);//2000是刷新的延时，使得有个动画效果
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        PAGE++;
                        //填写加载更多的网络请求，一般page++
                        initJingXuanDate(true);
                        //没有更多数据时候
                        if (is_not_more) {
                            xrefreshview.setLoadComplete(true);
                        } else {
                            //刷新完成必须调用此方法停止加载
                            xrefreshview.stopLoadMore(true);
                        }
                    }
                }, 1000);//1000是加载的延时，使得有个动画效果
            }
        });
    }


    public static Dialog dialog;
    //微博分享标题
    private String content;
    //分享类型
    private String ShareType;

    private void select_more_popwindow(HomeItem.DataBean data, int type) {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        View view = factory.inflate(R.layout.more_popwindow, null);

        //分享列表
        RecyclerView more_recyclerView = view.findViewById(R.id.more_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        more_recyclerView.setLayoutManager(layoutManager);
        more_recyclerView.setHasFixedSize(true);

        //刪除，編輯，舉報功能列表
        RecyclerView function_recyclerView = view.findViewById(R.id.function_recyclerView);
        LinearLayoutManager function_layoutManager = new LinearLayoutManager(getActivity());
        function_layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        function_recyclerView.setLayoutManager(function_layoutManager);
        function_recyclerView.setHasFixedSize(true);

        //取消
        LinearLayout select_dismiss = view.findViewById(R.id.select_dismiss);

        dialog = new Dialog(getActivity(), R.style.transparentFrameWindowStyle2);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        function_recyclerView.setVisibility(View.VISIBLE);

        if (moreListem.size() == 0) {
            for (int i = 0; i < moreName.length; i++) {
                ShareImageItem item = new ShareImageItem();
                item.setShareImage(moreImageids[i]);
                item.setShareNamr(moreName[i]);
                moreListem.add(item);
            }
        }
        //分享填充适配器
        ShareImageItemAdapter shareImageItemAdapter = new ShareImageItemAdapter(getActivity());
        shareImageItemAdapter.setShareImageItems(moreListem);
        more_recyclerView.setAdapter(shareImageItemAdapter);

        //设置逻辑交互
        //编辑
        if (functionListem.size() == 0) {
            if (SharedPreferencesHelper.get(getActivity(), "user_id", "").toString().equals(Uid)) {
                FunctionItem item2 = new FunctionItem();
                item2.setFunctionImage(functionImageids[2]);
                item2.setFunctionName(functionName[2]);
                functionListem.add(item2);
                if (dataItem.getStatus() != 1) {
                    if ("1".equals(is_update)) {
                        FunctionItem item = new FunctionItem();
                        item.setFunctionImage(functionImageids[0]);
                        item.setFunctionName(functionName[0]);
                        functionListem.add(item);
                    }
                }
                if ("1".equals(is_delete)) {
                    FunctionItem item1 = new FunctionItem();
                    item1.setFunctionImage(functionImageids[1]);
                    item1.setFunctionName(functionName[1]);
                    functionListem.add(item1);
                }
            } else {
                FunctionItem item2 = new FunctionItem();
                item2.setFunctionImage(functionImageids[2]);
                item2.setFunctionName(functionName[2]);
                functionListem.add(item2);
                if ("1".equals(is_update)) {
                    FunctionItem item = new FunctionItem();
                    item.setFunctionImage(functionImageids[0]);
                    item.setFunctionName(functionName[0]);
                    functionListem.add(item);
                }
                if ("1".equals(is_delete)) {
                    FunctionItem item1 = new FunctionItem();
                    item1.setFunctionImage(functionImageids[1]);
                    item1.setFunctionName(functionName[1]);
                    functionListem.add(item1);
                }
            }
        }
        //功能填充适配器
        AnswerFunctionAdapter answerFunctionAdapter = new AnswerFunctionAdapter(getActivity());
        answerFunctionAdapter.setShareImageItems(functionListem);
        function_recyclerView.setAdapter(answerFunctionAdapter);

        shareImageItemAdapter.setOnItemClickListener(new ShareImageItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Properties prop = new Properties();
                switch (moreListem.get(position).getShareNamr()) {
                    case "微信":
                        prop.setProperty("name", "wechat_layout");
                        StatService.trackCustomKVEvent(getActivity(), "Details_answer_more_share_wechat", prop);
                        if (type == 1) {
                            //视频分享
                            if (!TextUtils.isEmpty(dataItem.getContent())) {
                                if (!TextUtils.isEmpty(dataItem.getVideo_data().getFace_path())) {
                                    ShareUtil.ShareWechat(getActivity(), dataItem.getUser_data().getName() + "发布了视频", dataItem.getContent(),
                                            dataItem.getVideo_data().getFace_path(),
                                            Network.VideoUrl + dataItem.getId(), "2");
                                } else {
                                    ShareUtil.ShareWechat(getActivity(), dataItem.getUser_data().getName() + "发布了视频", dataItem.getContent(),
                                            Network.ShareImage,
                                            Network.VideoUrl + dataItem.getId(), "2");
                                }
                            } else {
                                if (!TextUtils.isEmpty(dataItem.getVideo_data().getFace_path())) {
                                    ShareUtil.ShareWechat(getActivity(), dataItem.getUser_data().getName() + "发布了视频", "快去分享您的印迹吧",
                                            dataItem.getVideo_data().getFace_path(),
                                            Network.VideoUrl + dataItem.getId(), "2");
                                } else {
                                    ShareUtil.ShareWechat(getActivity(), dataItem.getUser_data().getName() + "发布了视频", "快去分享您的印迹吧",
                                            Network.ShareImage,
                                            Network.VideoUrl + dataItem.getId(), "2");
                                }
                            }
                        } else {
                            //圖文分享
                            if (!TextUtils.isEmpty(dataItem.getContent())) {
                                if (!TextUtils.isEmpty(dataItem.getImage_data().get(0).getPath())) {
                                    ShareUtil.ShareWechat(getActivity(), dataItem.getUser_data().getName() + "发布了图文", dataItem.getContent(),
                                            dataItem.getImage_data().get(0).getPath(),
                                            Network.ImageTextUrl + dataItem.getId(), "2");
                                } else {
                                    ShareUtil.ShareWechat(getActivity(), dataItem.getUser_data().getName() + "发布了图文", dataItem.getContent(),
                                            Network.ShareImage,
                                            Network.VideoUrl + dataItem.getId(), "2");
                                }
                            } else {
                                if (!TextUtils.isEmpty(dataItem.getImage_data().get(0).getPath())) {
                                    ShareUtil.ShareWechat(getActivity(), dataItem.getUser_data().getName() + "发布了图文", "快去分享您的印迹吧",
                                            dataItem.getImage_data().get(0).getPath(),
                                            Network.ImageTextUrl + dataItem.getId(), "2");
                                } else {
                                    ShareUtil.ShareWechat(getActivity(), dataItem.getUser_data().getName() + "发布了图文", "快去分享您的印迹吧",
                                            Network.ShareImage,
                                            Network.ImageTextUrl + dataItem.getId(), "2");
                                }
                            }
                        }

                        ShareType = "2";
                        dialog.dismiss();
                        break;
                    case "朋友圈":
                        prop.setProperty("name", "wechat_moments_layout");
                        StatService.trackCustomKVEvent(getActivity(), "Details_answer_more_share_wechat_circle", prop);
                        if (type == 1) {
                            //视频分享
                            if (!TextUtils.isEmpty(dataItem.getContent())) {
                                if (!TextUtils.isEmpty(dataItem.getVideo_data().getFace_path())) {
                                    ShareUtil.ShareWechatMoments(getActivity(), dataItem.getUser_data().getName() + "发布了视频", dataItem.getContent(),
                                            dataItem.getVideo_data().getFace_path(),
                                            Network.VideoUrl + dataItem.getId(), "2");
                                } else {
                                    ShareUtil.ShareWechatMoments(getActivity(), dataItem.getUser_data().getName() + "发布了视频", dataItem.getContent(),
                                            Network.ShareImage,
                                            Network.VideoUrl + dataItem.getId(), "2");
                                }
                            } else {
                                if (!TextUtils.isEmpty(dataItem.getVideo_data().getFace_path())) {
                                    ShareUtil.ShareWechatMoments(getActivity(), dataItem.getUser_data().getName() + "发布了视频", "快去分享您的印迹吧",
                                            dataItem.getVideo_data().getFace_path(),
                                            Network.VideoUrl + dataItem.getId(), "2");
                                } else {
                                    ShareUtil.ShareWechatMoments(getActivity(), dataItem.getUser_data().getName() + "发布了视频", "快去分享您的印迹吧",
                                            Network.ShareImage,
                                            Network.VideoUrl + dataItem.getId(), "2");
                                }
                            }
                        } else {
                            //圖文分享
                            if (!TextUtils.isEmpty(dataItem.getContent())) {
                                if (!TextUtils.isEmpty(dataItem.getImage_data().get(0).getPath())) {
                                    ShareUtil.ShareWechat(getActivity(), dataItem.getUser_data().getName() + "发布了图文", dataItem.getContent(),
                                            dataItem.getImage_data().get(0).getPath(),
                                            Network.ImageTextUrl + dataItem.getId(), "2");
                                } else {
                                    ShareUtil.ShareWechat(getActivity(), dataItem.getUser_data().getName() + "发布了图文", dataItem.getContent(),
                                            Network.ShareImage,
                                            Network.VideoUrl + dataItem.getId(), "2");
                                }
                            } else {
                                if (!TextUtils.isEmpty(dataItem.getImage_data().get(0).getPath())) {
                                    ShareUtil.ShareWechat(getActivity(), dataItem.getUser_data().getName() + "发布了图文", "暂无介绍",
                                            dataItem.getImage_data().get(0).getPath(),
                                            Network.ImageTextUrl + dataItem.getId(), "2");
                                } else {
                                    ShareUtil.ShareWechat(getActivity(), dataItem.getUser_data().getName() + "发布了图文", "暂无介绍",
                                            Network.ShareImage,
                                            Network.ImageTextUrl + dataItem.getId(), "2");
                                }
                            }
                        }
                        ShareType = "1";
                        dialog.dismiss();
                        break;
                    case "新浪微博":
                        prop.setProperty("name", "sinaWeibo_layout");
                        StatService.trackCustomKVEvent(getActivity(), "Details_answer_more_share_sina", prop);
                        if (type == 1) {
                            //视频分享
                            if (!TextUtils.isEmpty(dataItem.getContent())) {
                                if (!TextUtils.isEmpty(dataItem.getVideo_data().getFace_path())) {
                                    ShareUtil.ShareWeiBo(getActivity(), dataItem.getUser_data().getName() + "发布了视频", dataItem.getContent(),
                                            dataItem.getVideo_data().getFace_path(),
                                            Network.VideoUrl + dataItem.getId(), "2");
                                } else {
                                    ShareUtil.ShareWeiBo(getActivity(), dataItem.getUser_data().getName() + "发布了视频", dataItem.getContent(),
                                            Network.ShareImage,
                                            Network.VideoUrl + dataItem.getId(), "2");
                                }
                            } else {
                                if (!TextUtils.isEmpty(dataItem.getVideo_data().getFace_path())) {
                                    ShareUtil.ShareWeiBo(getActivity(), dataItem.getUser_data().getName() + "发布了视频", "快去分享您的印迹吧",
                                            dataItem.getVideo_data().getFace_path(),
                                            Network.VideoUrl + dataItem.getId(), "2");
                                } else {
                                    ShareUtil.ShareWeiBo(getActivity(), dataItem.getUser_data().getName() + "发布了视频", "快去分享您的印迹吧",
                                            Network.ShareImage,
                                            Network.VideoUrl + dataItem.getId(), "2");
                                }
                            }
                        } else {
                            //圖文分享
                            if (!TextUtils.isEmpty(dataItem.getContent())) {
                                if (!TextUtils.isEmpty(dataItem.getImage_data().get(0).getPath())) {
                                    ShareUtil.ShareWeiBo(getActivity(), dataItem.getUser_data().getName() + "发布了图文", dataItem.getContent(),
                                            dataItem.getImage_data().get(0).getPath(),
                                            Network.ImageTextUrl + dataItem.getId(), "2");
                                } else {
                                    ShareUtil.ShareWeiBo(getActivity(), dataItem.getUser_data().getName() + "发布了图文", dataItem.getContent(),
                                            Network.ShareImage,
                                            Network.VideoUrl + dataItem.getId(), "2");
                                }
                            } else {
                                if (!TextUtils.isEmpty(dataItem.getImage_data().get(0).getPath())) {
                                    ShareUtil.ShareWeiBo(getActivity(), dataItem.getUser_data().getName() + "发布了图文", "快去分享您的印迹吧",
                                            dataItem.getImage_data().get(0).getPath(),
                                            Network.ImageTextUrl + dataItem.getId(), "2");
                                } else {
                                    ShareUtil.ShareWeiBo(getActivity(), dataItem.getUser_data().getName() + "发布了图文", "快去分享您的印迹吧",
                                            Network.ShareImage,
                                            Network.ImageTextUrl + dataItem.getId(), "2");
                                }
                            }
                        }
                        ShareType = "3";
                        dialog.dismiss();
                        break;
                    case "QQ":
                        prop.setProperty("name", "QQ_layout");
                        StatService.trackCustomKVEvent(getActivity(), "Details_answer_more_share_qq", prop);
                        if (type == 1) {
                            //视频分享
                            if (!TextUtils.isEmpty(dataItem.getContent())) {
                                if (!TextUtils.isEmpty(dataItem.getVideo_data().getFace_path())) {
                                    ShareUtil.ShareQQ(getActivity(), dataItem.getUser_data().getName() + "发布了视频", dataItem.getContent(),
                                            dataItem.getVideo_data().getFace_path(),
                                            Network.VideoUrl + dataItem.getId(), "2");
                                } else {
                                    ShareUtil.ShareQQ(getActivity(), dataItem.getUser_data().getName() + "发布了视频", dataItem.getContent(),
                                            Network.ShareImage,
                                            Network.VideoUrl + dataItem.getId(), "2");
                                }
                            } else {
                                if (!TextUtils.isEmpty(dataItem.getVideo_data().getFace_path())) {
                                    ShareUtil.ShareQQ(getActivity(), dataItem.getUser_data().getName() + "发布了视频", "快去分享您的印迹吧",
                                            dataItem.getVideo_data().getFace_path(),
                                            Network.VideoUrl + dataItem.getId(), "2");
                                } else {
                                    ShareUtil.ShareQQ(getActivity(), dataItem.getUser_data().getName() + "发布了视频", "快去分享您的印迹吧",
                                            Network.ShareImage,
                                            Network.VideoUrl + dataItem.getId(), "2");
                                }
                            }
                        } else {
                            //圖文分享
                            if (!TextUtils.isEmpty(dataItem.getContent())) {
                                if (!TextUtils.isEmpty(dataItem.getImage_data().get(0).getPath())) {
                                    ShareUtil.ShareQQ(getActivity(), dataItem.getUser_data().getName() + "发布了图文", dataItem.getContent(),
                                            dataItem.getImage_data().get(0).getPath(),
                                            Network.ImageTextUrl + dataItem.getId(), "2");
                                } else {
                                    ShareUtil.ShareQQ(getActivity(), dataItem.getUser_data().getName() + "发布了图文", dataItem.getContent(),
                                            Network.ShareImage,
                                            Network.VideoUrl + dataItem.getId(), "2");
                                }
                            } else {
                                if (!TextUtils.isEmpty(dataItem.getImage_data().get(0).getPath())) {
                                    ShareUtil.ShareQQ(getActivity(), dataItem.getUser_data().getName() + "发布了图文", "快去分享您的印迹吧",
                                            dataItem.getImage_data().get(0).getPath(),
                                            Network.ImageTextUrl + dataItem.getId(), "2");
                                } else {
                                    ShareUtil.ShareQQ(getActivity(), dataItem.getUser_data().getName() + "发布了图文", "快去分享您的印迹吧",
                                            Network.ShareImage,
                                            Network.ImageTextUrl + dataItem.getId(), "2");
                                }
                            }

                        }
                        ShareType = "4";
                        dialog.dismiss();
                        break;
                    case "QQ空间":
                        prop.setProperty("name", "QQzone_layout");
                        StatService.trackCustomKVEvent(getActivity(), "Details_answer_more_share_qqzone", prop);
                        if (type == 1) {
                            //视频分享
                            if (!TextUtils.isEmpty(dataItem.getContent())) {
                                if (!TextUtils.isEmpty(dataItem.getVideo_data().getFace_path())) {
                                    ShareUtil.ShareQZone(getActivity(), dataItem.getUser_data().getName() + "发布了视频", dataItem.getContent(),
                                            dataItem.getVideo_data().getFace_path(),
                                            Network.VideoUrl + dataItem.getId(), "2");
                                } else {
                                    ShareUtil.ShareQZone(getActivity(), dataItem.getUser_data().getName() + "发布了视频", dataItem.getContent(),
                                            Network.ShareImage,
                                            Network.VideoUrl + dataItem.getId(), "2");
                                }
                            } else {
                                if (!TextUtils.isEmpty(dataItem.getVideo_data().getFace_path())) {
                                    ShareUtil.ShareQZone(getActivity(), dataItem.getUser_data().getName() + "发布了视频", "快去分享您的印迹吧",
                                            dataItem.getVideo_data().getFace_path(),
                                            Network.VideoUrl + dataItem.getId(), "2");
                                } else {
                                    ShareUtil.ShareQZone(getActivity(), dataItem.getUser_data().getName() + "发布了视频", "快去分享您的印迹吧",
                                            Network.ShareImage,
                                            Network.VideoUrl + dataItem.getId(), "2");
                                }
                            }
                        } else {
                            //圖文分享
                            if (!TextUtils.isEmpty(dataItem.getContent())) {
                                if (!TextUtils.isEmpty(dataItem.getImage_data().get(0).getPath())) {
                                    ShareUtil.ShareQZone(getActivity(), dataItem.getUser_data().getName() + "发布了图文", dataItem.getContent(),
                                            dataItem.getImage_data().get(0).getPath(),
                                            Network.ImageTextUrl + dataItem.getId(), "2");
                                } else {
                                    ShareUtil.ShareQZone(getActivity(), dataItem.getUser_data().getName() + "发布了图文", dataItem.getContent(),
                                            Network.ShareImage,
                                            Network.VideoUrl + dataItem.getId(), "2");
                                }
                            } else {
                                if (!TextUtils.isEmpty(dataItem.getImage_data().get(0).getPath())) {
                                    ShareUtil.ShareQZone(getActivity(), dataItem.getUser_data().getName() + "发布了图文", "快去分享您的印迹吧",
                                            dataItem.getImage_data().get(0).getPath(),
                                            Network.ImageTextUrl + dataItem.getId(), "2");
                                } else {
                                    ShareUtil.ShareQZone(getActivity(), dataItem.getUser_data().getName() + "发布了图文", "快去分享您的印迹吧",
                                            Network.ShareImage,
                                            Network.ImageTextUrl + dataItem.getId(), "2");
                                }
                            }
                        }
                        ShareType = "5";
                        dialog.dismiss();
                        break;
                }
            }
        });

        answerFunctionAdapter.setOnItemClickListener(new AnswerFunctionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Properties prop = new Properties();
                switch (functionListem.get(position).getFunctionName()) {
                    case "编辑":
                        if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        } else {
                            prop.setProperty("name", "bianji_layout");
                            StatService.trackCustomKVEvent(getActivity(), "Details_answer_more_edite", prop);
                            //编辑
                            Intent intent = new Intent(getActivity(), AddImagesActivity.class);
                            Bundle bundle = new Bundle();
                            //传递必要数据(图片,id,内容,标签)
                            ArrayList<String> list = new ArrayList<>();
                            for (int i = 0; i < imageTextDetails.getImg_data().size(); i++) {
                                list.add(imageTextDetails.getImg_data().get(i).getPath());
                            }
                            if (null != imageTextDetails.getId()) {
                                intent.putExtra("update_id", imageTextDetails.getId());
                            }
                            if (null != imageTextDetails.getContent()) {
                                intent.putExtra("update_content", imageTextDetails.getContent());
                            }
                            if (null != imageTextDetails.getClass_data() && imageTextDetails.getClass_data().size() > 0) {
                                bundle.putParcelableArrayList("biaoqians", (ArrayList<? extends Parcelable>) imageTextDetails.getClass_data());
                            }
                            intent.putStringArrayListExtra("update_image_paths", list);
                            intent.putExtra("flag", "1");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        dialog.dismiss();
                        break;
                    case "举报":
                        prop.setProperty("name", "jubao_layout");
                        StatService.trackCustomKVEvent(getActivity(), "Details_answer_more_report", prop);
                        if ("".equals(SharedPreferencesHelper.get(getActivity(), "login_token", ""))) {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        } else {
                            report_msg_question();
                        }
                        dialog.dismiss();
                        break;
                    case "删除":
                        dialog.dismiss();
                        prop.setProperty("name", "delete_layout");
                        StatService.trackCustomKVEvent(getActivity(), "Details_answer_more_delete", prop);
                        PopWindowHelper.public_tishi_pop(getActivity(), "删除印迹", "是否删除该印迹？", "否", "是", new DialogCallBack() {
                            @Override
                            public void save() {
                                if (dataItem.getStatus() == 1) {
                                    deleteVideo();
                                } else {
                                    deleteImage();
                                }
                            }

                            @Override
                            public void cancel() {

                            }
                        });
                        break;
                }
            }
        });

        //取消
        select_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties prop = new Properties();
                prop.setProperty("name", "select_dismiss");
                StatService.trackCustomKVEvent(getActivity(), "Details_answer_cancel", prop);
                dialog.dismiss();
            }
        });
    }

    private void select_report_popwindow(List<ReportMsg> msgs) {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        View view = factory.inflate(R.layout.report_popwindow, null);
        //举报信息
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayout report_btn = view.findViewById(R.id.report_btn);
        tv_report = view.findViewById(R.id.tv_report);
        report_title = view.findViewById(R.id.report_title);
        if (dataItem.getStatus() == 1) {
            report_title.setText("举报视频");
        } else {
            report_title.setText("举报图文");
        }

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        reportMsgAdapter = new ReportMsgAdapter(getActivity(), handler1);
        reportMsgAdapter.setData(msgs);
        recyclerView.setAdapter(reportMsgAdapter);

        WindowManager wm1 = getActivity().getWindowManager();
        int height1 = wm1.getDefaultDisplay().getHeight();

        dialog = new Dialog(getActivity(), R.style.transparentFrameWindowStyle2);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getActivity().getWindowManager().getDefaultDisplay().getHeight();
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
                ids = str.substring(1, len).replace(" ", "");//"keyids":”1,2,3”
                if (!TextUtils.isEmpty(ids)) {
                    if (dataItem.getStatus() == 1) {
                        reportVideo();
                    } else {
                        reportImage();
                    }
                } else {
                    Toast.makeText(getActivity(), "请选择举报内容", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    String ids;

    private Handler handler1 = new Handler() {

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

    /**
     * 获取举报视频数据
     */
    private void report_msg_question() {
        subscription = Network.getInstance("获取举报问题类型", getActivity())
                .getReportVideoType(new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<ReportMsg>>>() {
                    @Override
                    public void onNext(Bean<List<ReportMsg>> result) {
                        if (data.size() > 0) {
                            data.clear();
                        }
                        for (ReportMsg msg : result.getData()) {
                            Logger.e("获取举报问题类型：" + msg.getId() + "            " + msg.getName());
                            data.add(msg);
                        }
                        select_report_popwindow(data);
                    }

                    @Override
                    public void onError(String error) {
                        Logger.e("获取举报问题类型异常：" + error.toString());
                    }
                }, getActivity(), false));
    }

    //举报视频
    private void reportVideo() {
        Map<String, String> params = new HashMap<>();
        params.put("video_id", image_id);
        params.put("type", ids);
        Log.e("舉報视频：", params.toString());
        subscription = Network.getInstance("舉報视频", getActivity())
                .getReportVideo(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "舉報视频成功" + result.getCode());
                                Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                Log.e("123", "舉報视频报错：" + error);
                                dialog.dismiss();
                            }
                        }, getActivity(), false));
    }

    //舉報圖文
    private void reportImage() {
        Map<String, String> params = new HashMap<>();
        params.put("image_text_id", image_id);
        params.put("type", ids);
        Log.e("举报图文：", params.toString());
        subscription = Network.getInstance("举报图文", getActivity())
                .getReportImage(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "举报图文成功" + result.getCode());
                                Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "举报图文报错：" + error);
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }, getActivity(), false));
    }

    //刪除圖文
    private void deleteImage() {
        Map<String, String> params = new HashMap<>();
        params.put("image_text_id", image_id);
        Log.e("刪除圖文：", params.toString());
        subscription = Network.getInstance("刪除圖文", getActivity())
                .getDeleteImage(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
                                Log.e("123", "刪除圖文成功" + result.getCode());
                            }

                            @Override
                            public void onError(String error) {
                                Logger.e("刪除圖文报错：" + error);
                            }
                        }, getActivity(), false));
    }

    //刪除视频
    private void deleteVideo() {
        Map<String, String> params = new HashMap<>();
        params.put("video_id", image_id);
        Log.e("刪除视频：", params.toString());
        subscription = Network.getInstance("刪除视频", getActivity())
                .getDeleteVideo(params,
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<String>>>() {
                            @Override
                            public void onNext(Bean<List<String>> result) {
                                Log.e("123", "刪除视频成功" + result.getCode());
                                Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(String error) {
//                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                Logger.e("刪除视频报错：" + error);
                            }
                        }, getActivity(), false));
    }


    private boolean isReset = true;

    @Override
    public void onPause() {
        super.onPause();
        //如果进入详情播放则不暂停视频释放资源//为空内部已经处理
//        VideoPlayerManager.getInstance().onPause(isReset);
        VideoPlayerManager.getInstance().onPause(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        VideoPlayerManager.getInstance().onResume();
        if (homeItems.size() > 0) {
            homeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        VideoPlayerManager.getInstance().onDestroy();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        VideoPlayerManager.getInstance().onConfigurationChanged(newConfig);//横竖屏切换
    }

}

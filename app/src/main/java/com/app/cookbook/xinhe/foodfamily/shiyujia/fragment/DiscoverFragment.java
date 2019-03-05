package com.app.cookbook.xinhe.foodfamily.shiyujia.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.AnserActivity;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.MainSearchActivity;
import com.app.cookbook.xinhe.foodfamily.main.SuggestedUsersActivity;
import com.app.cookbook.xinhe.foodfamily.main.adapter.SuggestedUsersAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageEvent;
import com.app.cookbook.xinhe.foodfamily.main.entity.SuggestedUsers;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.ImageTextDetailActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.LabelDetailsActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.MessageActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.VideoDetailsActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.RecommendLabelAdapter;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.DiscoverBanner;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.DiscoverConfiguration;
import com.app.cookbook.xinhe.foodfamily.shiyujia.entity.DiscoverRecommend;
import com.app.cookbook.xinhe.foodfamily.shiyujia.view.DiscoverGlideImageLoader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youth.banner.BannerConfig;
import com.youth.banner.DiscoveresBanner;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

/**
 * 发现页
 */
public class DiscoverFragment extends Fragment {
    private View view;
    protected Subscription subscription;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.message_alert)
    ImageView message_alert;
    @BindView(R.id.mesg_hint)
    TextView mesg_hint;
    @BindView(R.id.search_btn)
    ImageView search_btn;
    private RelativeLayout head_layout;
    private DiscoveresBanner banner_layout;
    private RelativeLayout att_user_layout;
    private ImageView attention_more;
    private RecyclerView user_recycler;
    private RelativeLayout label_layout;
    @BindView(R.id.recyclerView)
    XRecyclerView recyclerView;
    //图片集合
    private List<String> imageList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private List<String> userHandList = new ArrayList<>();
    private List<String> typeList = new ArrayList<>();

    //bannner数据
    private List<DiscoverBanner> discoverBanners = new ArrayList<>();
    private String Sig;

    //值得关注的人
    private LinearLayoutManager suggestedUsers_layoutManager;
    private SuggestedUsersAdapter suggestedUsersAdapter;
    private List<SuggestedUsers.DataBean> suggestedUsers = new ArrayList<>();

    //推荐标签
    private RecommendLabelAdapter recommendLabelAdapter;
    private List<DiscoverRecommend.DataBean> discoverRecommends = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;

    private int PAGE = 1;
    //列表
    private String NoMsg = "0";
    //记录滑动位置
    private boolean itemPosition;


    public static DiscoverFragment newInstance(String title) {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        args.putString("home_fragment_title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.discover_fragment, container, false);
            ButterKnife.bind(this, view);//绑定黄牛刀
            EventBus.getDefault().register(this);
            initView();
        }
        return view;
    }

    private void initView() {
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        recommendLabelAdapter = new RecommendLabelAdapter(getActivity());
        recommendLabelAdapter.setDiscoverRecommends(discoverRecommends);
        recyclerView.setAdapter(recommendLabelAdapter);
        getRefresh(recyclerView);

    }

    //订阅方法，当接收到事件的时候，会调用该方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        switch (messageEvent.getMessage()) {
            case "1":
                FindName_Request();
                break;
        }
    }

    private void initHandLayout() {
        suggestedUsers_layoutManager = new LinearLayoutManager(getActivity());
        suggestedUsers_layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        user_recycler.setLayoutManager(suggestedUsers_layoutManager);
        user_recycler.setHasFixedSize(true);
        suggestedUsersAdapter = new SuggestedUsersAdapter(getActivity());
        suggestedUsersAdapter.setSuggestedUsers(suggestedUsers);
        user_recycler.setAdapter(suggestedUsersAdapter);
    }


    private void getOnClickListener() {
        banner_layout.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                DiscoverBanner banner = discoverBanners.get(position);
                Intent intent = null;
                if ("1".equals(banner.getType())) {
                    intent = new Intent(getActivity(), ImageTextDetailActivity.class);
                    intent.putExtra("id", banner.getAscription_id());
                    startActivity(intent);
                } else if ("2".equals(banner.getType())) {
                    intent = new Intent(getActivity(), VideoDetailsActivity.class);
                    intent.putExtra("id", banner.getAscription_id());
                    startActivity(intent);
                } else if ("3".equals(banner.getType())) {
                    intent = new Intent(getActivity(), FriendPageActivity.class);
                    intent.putExtra("UserId", banner.getAscription_id());
                    startActivity(intent);
                } else if ("4".equals(banner.getType())) {
                    intent = new Intent(getActivity(), LabelDetailsActivity.class);
                    intent.putExtra("id", banner.getAscription_id());
                    startActivity(intent);
                } else if ("5".equals(banner.getType())) {
                    intent = new Intent(getActivity(), FenLeiQuestionDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("wenti_id", banner.getAscription_id());
                    bundle.putString("flag", "0");
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if ("6".equals(banner.getType())) {
                    intent = new Intent(getActivity(), AnserActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("answer_id", banner.getAscription_id());
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainSearchActivity.class);
                startActivity(intent);
            }
        });

        message_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
            }
        });
        attention_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SuggestedUsersActivity.class);
                startActivity(intent);
            }
        });

        recommendLabelAdapter.setOnItemClickListener(new RecommendLabelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.e("123", "    position    " + position);
                DiscoverRecommend.DataBean recommend = discoverRecommends.get(position);
                Intent intent = new Intent(getActivity(), LabelDetailsActivity.class);
                intent.putExtra("id", recommend.getId());
                startActivity(intent);
            }
        });
    }

    private void FindName_Request() {
        subscription = Network.getInstance("发现名称配置", getActivity())
                .getFindName(new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<DiscoverConfiguration>>>() {
                    @Override
                    public void onNext(Bean<List<DiscoverConfiguration>> result) {
                        title.setText(result.getData().get(0).getName());

                        if ("1".equals(result.getData().get(1).getConfigure())) {
                            BannerList_Request();
                            banner_layout.setVisibility(View.VISIBLE);
                        } else {
                            banner_layout.setVisibility(View.GONE);
                        }

                        if ("1".equals(result.getData().get(2).getConfigure())) {
                            user_ranking_question();
                            att_user_layout.setVisibility(View.VISIBLE);
                        } else {
                            att_user_layout.setVisibility(View.GONE);
                        }

                        if ("1".equals(result.getData().get(3).getConfigure())) {
                            RecommendClass_Request();
                            label_layout.setVisibility(View.VISIBLE);
                        } else {
                            label_layout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("123", "发现名称配置：" + error.toString());
                    }
                }, getActivity(), false));
    }


    private void BannerList_Request() {
        subscription = Network.getInstance("发现轮播图", getActivity())
                .getBannerList(new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<DiscoverBanner>>>() {
                    @Override
                    public void onNext(Bean<List<DiscoverBanner>> result) {
                        Log.e("123", "     发现轮播图      " + result);
                        if (discoverBanners.size() > 0 && PAGE == 1) {
                            discoverBanners.clear();
                            imageList.clear();
                            titleList.clear();
                            userHandList.clear();
                            typeList.clear();
                        }
                        for (DiscoverBanner item : result.getData()) {
                            discoverBanners.add(item);
                        }
                        if (result.getData() != null) {
                            for (int i = 0; i < result.getData().size(); i++) {
                                imageList.add(result.getData().get(i).getBanner_path());
                                if ("1".equals(result.getData().get(i).getType())) {
                                    typeList.add(result.getData().get(i).getType());
                                    userHandList.add(result.getData().get(i).getUser_release_data().getAvatar());
                                    titleList.add(result.getData().get(i).getUser_release_data().getName());
                                } else if ("2".equals(result.getData().get(i).getType())) {
                                    typeList.add(result.getData().get(i).getType());
                                    userHandList.add(result.getData().get(i).getUser_release_data().getAvatar());
                                    titleList.add(result.getData().get(i).getUser_release_data().getName());
                                } else if ("3".equals(result.getData().get(i).getType())) {
                                    typeList.add(result.getData().get(i).getType());
                                    Sig = result.getData().get(i).getUser_release_data().getPersonal_signature();
                                    userHandList.add(result.getData().get(i).getUser_release_data().getAvatar());
                                    titleList.add(result.getData().get(i).getUser_release_data().getName());
                                } else if ("4".equals(result.getData().get(i).getType())) {
                                    typeList.add(result.getData().get(i).getType());
                                    userHandList.add("");
                                    titleList.add("#" + result.getData().get(i).getAscription_class_data().getTitle());
                                } else if ("5".equals(result.getData().get(i).getType())) {
                                    typeList.add(result.getData().get(i).getType());
                                    userHandList.add("");
                                    titleList.add(result.getData().get(i).getAscription_question_data().getTitle());
                                } else if ("6".equals(result.getData().get(i).getType())) {
                                    typeList.add(result.getData().get(i).getType());
                                    userHandList.add(result.getData().get(i).getUser_release_data().getAvatar());
                                    titleList.add(result.getData().get(i).getUser_release_data().getName());
                                }
                            }
                        }
                        Log.e("123", "    userTitleList.size()     " + userHandList.size());
                        banner_layout.setImages(imageList)
                                .setBannerTitles(titleList)
                                .setUserHand(userHandList)
                                .setType(typeList)
                                .setSig(Sig)
                                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                                .setImageLoader(new DiscoverGlideImageLoader())
                                .start();
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("123", "发现轮播图报错：" + error.toString());
                    }
                }, getActivity(), false));
    }

    /**
     * 值得关注的用户
     */
    private void user_ranking_question() {
        Map<String, String> params = new HashMap<>();
        params.put("sort", "");
        Log.e("123", "        " + params);
        subscription = Network.getInstance("值得关注的用户", getActivity())
                .follow_users_request(params,
                        new ProgressSubscriberNew<>(SuggestedUsers.class, new GsonSubscriberOnNextListener<SuggestedUsers>() {
                            @Override
                            public void on_post_entity(SuggestedUsers result) {
                                Log.e("123", "值得关注的人：" + result.getData().size());
                                if (suggestedUsers.size() > 0) {
                                    suggestedUsers.clear();
                                }
                                suggestedUsers.addAll(result.getData());
                                suggestedUsersAdapter.setSuggestedUsers(suggestedUsers);
                                user_recycler.setAdapter(suggestedUsersAdapter);
                                suggestedUsersAdapter.notifyDataSetChanged();
                                if (suggestedUsers.size() == 0) {
                                    att_user_layout.setVisibility(View.GONE);
                                } else if (suggestedUsers.size() > 0) {
                                    att_user_layout.setVisibility(View.VISIBLE);
                                }
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                                Log.e("123", "值得关注的用户：error   " + error);
                            }
                        }, getActivity(), false));
    }

    private void RecommendClass_Request() {
        subscription = Network.getInstance("推荐标签", getActivity())
                .getRecommendClass(String.valueOf(PAGE),
                        new ProgressSubscriberNew<>(DiscoverRecommend.class, new GsonSubscriberOnNextListener<DiscoverRecommend>() {
                            @Override
                            public void on_post_entity(DiscoverRecommend result) {
                                Log.e("123", "     推荐标签      " + result);
                                if (result.getData().size() == 0) {
                                    recyclerView.setNoMore(true);
                                } else {
                                    recyclerView.loadMoreComplete();
                                }
                                if (discoverRecommends.size() > 0 && PAGE == 1) {
                                    discoverRecommends.clear();
                                }
                                if (discoverRecommends.size() > 0) {//这表示是"加载"
                                    if (result.getData().size() == 0) {
                                        discoverRecommends.addAll(result.getData());
                                        recommendLabelAdapter.notifyDataSetChanged();
                                    } else {
                                        discoverRecommends.addAll(result.getData());
                                        recommendLabelAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    discoverRecommends = result.getData();
                                    recommendLabelAdapter.setDiscoverRecommends(discoverRecommends);
                                    recyclerView.setAdapter(recommendLabelAdapter);
                                }
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                if ("无数据".equals(error)) {
                                    NoMsg = "1";
                                    PAGE = PAGE - 1;
                                }
                                Log.e("123", "推荐标签报错：" + error.toString());
                            }
                        }, getActivity(), true));
    }

    private void getRefresh(XRecyclerView recyclerView) {
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerView.setArrowImageView(R.drawable.xrefreshview_arrow);
        recyclerView
                .getDefaultRefreshHeaderView()
                .setRefreshTimeVisible(true);
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.discover_hand_layout, (ViewGroup) view.findViewById(android.R.id.content), false);
        head_layout = header.findViewById(R.id.head_layout);
        head_layout.setFocusable(false);
        banner_layout = header.findViewById(R.id.banner_layout);
        banner_layout.setFocusable(false);
        att_user_layout = header.findViewById(R.id.att_user_layout);
        attention_more = header.findViewById(R.id.attention_more);
        user_recycler = header.findViewById(R.id.user_recycler);
        user_recycler.setFocusable(false);
        label_layout = header.findViewById(R.id.label_layout);
        recyclerView.addHeaderView(header);
        recyclerView.getDefaultFootView().setLoadingHint("加载中...");
        recyclerView.getDefaultFootView().setNoMoreHint("加载完毕");
        FindName_Request();
        initHandLayout();
        getOnClickListener();

        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        PAGE = 1;
                        FindName_Request();
                        if (recyclerView != null) {
                            recyclerView.refreshComplete();
                        }
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        PAGE++;
                        RecommendClass_Request();
                    }
                }, 1000);
            }
        });
    }


}

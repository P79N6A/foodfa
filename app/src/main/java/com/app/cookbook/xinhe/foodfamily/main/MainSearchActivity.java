package com.app.cookbook.xinhe.foodfamily.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.adapter.ResuoAdapter;
import com.app.cookbook.xinhe.foodfamily.main.entity.HotSearch;
import com.app.cookbook.xinhe.foodfamily.main.entity.MessageEvent;
import com.app.cookbook.xinhe.foodfamily.main.fragment.SearchAnswerFragment;
import com.app.cookbook.xinhe.foodfamily.main.fragment.SearchIssueFragment;
import com.app.cookbook.xinhe.foodfamily.main.fragment.SearchTagFragment;
import com.app.cookbook.xinhe.foodfamily.main.fragment.SearchUserFragment;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriber;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;
import com.app.cookbook.xinhe.foodfamily.util.StatusBarUtil;
import com.app.cookbook.xinhe.foodfamily.util.ui.TagCloudView;
import com.tencent.stat.StatService;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;


public class MainSearchActivity extends FragmentActivity implements View.OnClickListener, TextView.OnEditorActionListener {
    /**
     * Rxjava
     */
    protected Subscription subscription;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    //问题布局
    @BindView(R.id.select_issue)
    LinearLayout select_issue;
    //问题名称
    @BindView(R.id.issue_title)
    TextView issue_title;
    //问题下划线
    @BindView(R.id.issue_fenge)
    LinearLayout issue_fenge;

    //答案布局
    @BindView(R.id.select_answer)
    LinearLayout select_answer;
    //答案名称
    @BindView(R.id.answer_title)
    TextView answer_title;
    //答案下划线
    @BindView(R.id.answer_fenge)
    LinearLayout answer_fenge;

    //用户布局
    @BindView(R.id.select_user)
    LinearLayout select_user;
    //用户名称
    @BindView(R.id.user_title)
    TextView user_title;
    //用户下划线
    @BindView(R.id.user_fenge)
    LinearLayout user_fenge;

    //标签布局
    @BindView(R.id.select_tag)
    LinearLayout select_tag;
    //标签名称
    @BindView(R.id.tag_title)
    TextView tag_title;
    //标签下划线
    @BindView(R.id.tag_fenge)
    LinearLayout tag_fenge;

    @BindView(R.id.iamge_back)
    ImageView iamge_back;

    @BindView(R.id.edit_search)
    EditText edit_search;

    @BindView(R.id.shanchu_btn)
    ImageView shanchu_btn;

    @BindView(R.id.search_btn)
    ImageView search_btn;

    @BindView(R.id.search_btn_layout)
    LinearLayout search_btn_layout;

    @BindView(R.id.creat_tab_gv)
    TagCloudView creatTabGv;

    @BindView(R.id.clear_btn)
    LinearLayout clear_btn;

    @BindView(R.id.clear_top_rel)
    RelativeLayout clear_top_rel;

    @BindView(R.id.label_layout)
    RelativeLayout label_layout;

    @BindView(R.id.search_view)
    RelativeLayout search_view;

    @BindView(R.id.resou_recyclerView)
    RecyclerView resou_recyclerView;


    public static String keyword;
    private ContentPagerAdapterMy contentAdapter;
    private List<Fragment> displayFragments;
    private List<String> AllTagsNormal = new ArrayList<>(0);//整个标签存放集合
    private final String SAVE_TAGS = "save_tags";
    View view;
    private boolean is_click_keybord_search = false;//判断是不是用户主动搜索，是，则添加进历史，不是，则不添加进历史

    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(MainSearchActivity.this, R.color.white);
        } else {
            StatusBarUtil.transparencyBar(MainSearchActivity.this);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        steepStatusBar();
        setContentView(R.layout.activity_main_search);
        ButterKnife.bind(this);
        initDate();
        select_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
                show_one();
            }
        });
        select_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
                show_two();
            }
        });

        select_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
                show_three();
            }
        });
        select_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
                show_four();
            }
        });

        edit_search.setOnEditorActionListener(this);

        //拿到历史tag
        String tag_list_string = SharedPreferencesHelper.get(getApplicationContext(), SAVE_TAGS, "").toString();
        try {
            if (SharedPreferencesHelper.String2SceneList(tag_list_string).size() == 0) {//代表没有历史数据
                clear_top_rel.setVisibility(View.GONE);
                creatTabGv.setVisibility(View.GONE);
            } else {
                clear_top_rel.setVisibility(View.VISIBLE);
                creatTabGv.setVisibility(View.VISIBLE);
            }
            AllTagsNormal.addAll(SharedPreferencesHelper.String2SceneList(tag_list_string));
            //最近搜索的显示在前,所以需要反向排序
            Collections.reverse(AllTagsNormal);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //单独点击标签的时候
        creatTabGv.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
            @Override
            public void onTagClick(int position) {
                is_click_keybord_search = true;
                keyword = AllTagsNormal.get(position);
                edit_search.setText(keyword);
                edit_search.setSelection(keyword.length());
                //给最近搜索增加条目,添加需要反转
                Collections.reverse(AllTagsNormal);
                AllTagsNormal.add(keyword);

                label_layout.setVisibility(View.GONE);
                search_view.setVisibility(View.VISIBLE);

                viewPager.setAdapter(contentAdapter);
                viewPager.setCurrentItem(0);
                EventBus.getDefault().post(new MessageEvent(keyword));

                if (CurrentNumber == 0) {
                    show_one();
                    viewPager.setCurrentItem(0);
                } else if (CurrentNumber == 1) {
                    show_two();
                    viewPager.setCurrentItem(1);
                } else if (CurrentNumber == 2) {
                    show_three();
                    viewPager.setCurrentItem(2);
                } else if (CurrentNumber == 3) {
                    show_four();
                    viewPager.setCurrentItem(3);
                }

            }
        });
        creatTabGv.setTags(removeDuplicateWithOrder(AllTagsNormal));
        //清空历史标签
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllTagsNormal.clear();
                creatTabGv.setTags(AllTagsNormal);
                creatTabGv.setVisibility(View.GONE);
                clear_top_rel.setVisibility(View.GONE);
                SharedPreferencesHelper.remove(getApplicationContext(), SAVE_TAGS);
                if (label_layout.getVisibility() == View.VISIBLE) {
                    is_click_keybord_search = false;
                } else {
                    is_click_keybord_search = true;
                }

            }
        });
    }

    FragmentManager fragmentManager;

    private void initDate() {
        if (fragmentManager == null) {
            fragmentManager = this.getSupportFragmentManager();
        }
        if (contentAdapter == null) {
            contentAdapter = new ContentPagerAdapterMy(fragmentManager);
        }
        displayFragments = new ArrayList<>();
        displayFragments.add(SearchIssueFragment.newInstance("提问"));
        displayFragments.add(SearchAnswerFragment.newInstance("回答"));
        displayFragments.add(SearchUserFragment.newInstance("用户"));
        displayFragments.add(SearchTagFragment.newInstance("标签"));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    Properties prop = new Properties();
                    prop.setProperty("name", "viewPager");
                    StatService.trackCustomKVEvent(getApplicationContext(), "Search_problem", prop);

                    show_one();
                } else if (position == 1) {
                    Properties prop = new Properties();
                    prop.setProperty("name", "viewPager");
                    StatService.trackCustomKVEvent(getApplicationContext(), "Search_answer", prop);

                    show_two();
                } else if (position == 2) {
                    Properties prop = new Properties();
                    prop.setProperty("name", "viewPager");
                    StatService.trackCustomKVEvent(getApplicationContext(), "Search_user", prop);

                    show_three();
                } else if (position == 3) {
                    Properties prop = new Properties();
                    prop.setProperty("name", "viewPager");
                    StatService.trackCustomKVEvent(getApplicationContext(), "Search_tag", prop);
                    show_four();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        iamge_back.setOnClickListener(this);
        shanchu_btn.setOnClickListener(this);
        search_btn.setOnClickListener(this);

        //加载热门标签
        init_resou_date();

    }

    private void init_resou_date() {
        Log.e("设计费看来是", "jskjf");
        subscription = Network.getInstance("热搜标签", this)
                .get_resou_date(
                        new ProgressSubscriber<>(new SubscriberOnNextListener<Bean<List<HotSearch>>>() {
                            @Override
                            public void onNext(Bean<List<HotSearch>> result) {
                                Log.e("热搜标签成功", "成功");
                                set_resou_date(result.getData());
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("热搜标签失败", error);

                            }
                        }, this, false));
    }

    private void set_resou_date(final List<HotSearch> data) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        resou_recyclerView.setLayoutManager(linearLayoutManager);
        ResuoAdapter adapter = new ResuoAdapter(data);
        adapter.setOnItemClickListener(new ResuoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                is_click_keybord_search = true;

                keyword = data.get(position).getName();
                edit_search.setText(keyword);
                edit_search.setSelection(keyword.length());
                //给最近搜索增加条目,添加需要反转
                Collections.reverse(AllTagsNormal);
                AllTagsNormal.add(keyword);

                label_layout.setVisibility(View.GONE);
                search_view.setVisibility(View.VISIBLE);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                viewPager.setAdapter(contentAdapter);
                viewPager.setCurrentItem(0);
                EventBus.getDefault().post(new MessageEvent(keyword));

                if (CurrentNumber == 0) {
                    show_one();
                    viewPager.setCurrentItem(0);
                } else if (CurrentNumber == 1) {
                    show_two();
                    viewPager.setCurrentItem(1);
                } else if (CurrentNumber == 2) {
                    show_three();
                    viewPager.setCurrentItem(2);
                } else if (CurrentNumber == 3) {
                    show_four();
                    viewPager.setCurrentItem(3);
                }

                //统计热词点击次数
                add_reci_number(data.get(position).getId());
            }
        });
        resou_recyclerView.setAdapter(adapter);
    }

    private void add_reci_number(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        Log.e("搜索热词参数", params.toString());
        subscription = Network.getInstance("热词点击", MainSearchActivity.this)
                .reci_click_number(params,
                        new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                            @Override
                            public void on_post_entity(List list) {
                                Log.e("热词点击成功", "热词点击成功");

                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {

                            }
                        }, MainSearchActivity.this, true));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iamge_back:
                Properties prop = new Properties();
                prop.setProperty("name", "iamge_back");
                StatService.trackCustomKVEvent(getApplicationContext(), "Search_return", prop);
                finish();
                break;
            case R.id.shanchu_btn:
                if (edit_search.getText().toString().length() > 0) {
                    edit_search.setText("");
                    if (label_layout.getVisibility() == View.VISIBLE) {
                        is_click_keybord_search = false;
                    } else {
                        is_click_keybord_search = true;
                    }
                } else {
                    return;
                }
                break;
            case R.id.search_btn:
                if (edit_search.getText().toString().length() > 0) {
                    if (is_click_keybord_search) {//如果是用户主动搜索的，删除的时候，添加进历史，纯删除关键字的，不添加进历史记录
                        //隐藏输入法
                        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(MainSearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        //切换布局
                        label_layout.setVisibility(View.VISIBLE);
                        search_view.setVisibility(View.GONE);
                        //给最近搜索增加条目,添加需要反转
                        keyword = edit_search.getText().toString();
                        Collections.reverse(AllTagsNormal);
                        AllTagsNormal.add(keyword);

                        if (AllTagsNormal.size() == 0) {//代表没有历史数据
                            clear_top_rel.setVisibility(View.GONE);
                            creatTabGv.setVisibility(View.GONE);
                        } else {
                            //去除重复数据
                            Collections.reverse(AllTagsNormal);
                            creatTabGv.setTags(removeDuplicateWithOrder(AllTagsNormal));
                            clear_top_rel.setVisibility(View.VISIBLE);
                            creatTabGv.setVisibility(View.VISIBLE);
                        }
                        if (label_layout.getVisibility() == View.VISIBLE) {
                            is_click_keybord_search = false;
                        } else {
                            is_click_keybord_search = true;
                        }
                    }
                    edit_search.setText("");
                } else {
                    return;
                }
                break;
        }
    }

    /**
     * List去除重复数据并保持顺序
     *
     * @param list
     * @return
     */
    public List removeDuplicateWithOrder(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        list.clear();
        list.addAll(newList);
        return list;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //保存最近搜索条目
        try {
            String tag_list_string = SharedPreferencesHelper.SceneList2String(AllTagsNormal);
            SharedPreferencesHelper.put(getApplicationContext(), SAVE_TAGS, tag_list_string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            is_click_keybord_search = true;
            if (edit_search.getText().toString().length() == 0) {
                return false;
            }
            keyword = edit_search.getText().toString();
            //给最近搜索增加条目,添加需要反转
            Collections.reverse(AllTagsNormal);
            AllTagsNormal.add(keyword);

            label_layout.setVisibility(View.GONE);
            search_view.setVisibility(View.VISIBLE);

            EventBus.getDefault().post(new MessageEvent(keyword));
            viewPager.setAdapter(contentAdapter);
            viewPager.setCurrentItem(0);

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            if (CurrentNumber == 0) {
                show_one();
                viewPager.setCurrentItem(0);
            } else if (CurrentNumber == 1) {
                show_two();
                viewPager.setCurrentItem(1);
            } else if (CurrentNumber == 2) {
                show_three();
                viewPager.setCurrentItem(2);
            } else if (CurrentNumber == 3) {
                show_four();
                viewPager.setCurrentItem(3);
            }
            return true;
        }
        return false;
    }


    class ContentPagerAdapterMy extends FragmentPagerAdapter {

        public ContentPagerAdapterMy(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return displayFragments.get(position);
        }

        @Override
        public int getCount() {
            return displayFragments.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }

    private int CurrentNumber = 0;

    private void show_one() {
        issue_title.setTextColor(Color.parseColor("#51B55C"));
        answer_title.setTextColor(Color.parseColor("#777777"));
        user_title.setTextColor(Color.parseColor("#777777"));
        tag_title.setTextColor(Color.parseColor("#777777"));
        tag_fenge.setVisibility(View.INVISIBLE);
        user_fenge.setVisibility(View.INVISIBLE);
        issue_fenge.setVisibility(View.VISIBLE);
        answer_fenge.setVisibility(View.INVISIBLE);
        CurrentNumber = 0;
    }

    private void show_two() {
        issue_title.setTextColor(Color.parseColor("#777777"));
        answer_title.setTextColor(Color.parseColor("#51B55C"));
        user_title.setTextColor(Color.parseColor("#777777"));
        tag_title.setTextColor(Color.parseColor("#777777"));
        tag_fenge.setVisibility(View.INVISIBLE);
        user_fenge.setVisibility(View.INVISIBLE);
        issue_fenge.setVisibility(View.INVISIBLE);
        answer_fenge.setVisibility(View.VISIBLE);
        CurrentNumber = 1;
    }

    private void show_three() {
        issue_title.setTextColor(Color.parseColor("#777777"));
        answer_title.setTextColor(Color.parseColor("#777777"));
        user_title.setTextColor(Color.parseColor("#51B55C"));
        tag_title.setTextColor(Color.parseColor("#777777"));
        tag_fenge.setVisibility(View.INVISIBLE);
        user_fenge.setVisibility(View.VISIBLE);
        issue_fenge.setVisibility(View.INVISIBLE);
        answer_fenge.setVisibility(View.INVISIBLE);
        CurrentNumber = 2;
    }

    private void show_four() {
        issue_title.setTextColor(Color.parseColor("#777777"));
        answer_title.setTextColor(Color.parseColor("#777777"));
        user_title.setTextColor(Color.parseColor("#777777"));
        tag_title.setTextColor(Color.parseColor("#51B55C"));
        tag_fenge.setVisibility(View.VISIBLE);
        user_fenge.setVisibility(View.INVISIBLE);
        issue_fenge.setVisibility(View.INVISIBLE);
        answer_fenge.setVisibility(View.INVISIBLE);
        CurrentNumber = 3;
    }


}

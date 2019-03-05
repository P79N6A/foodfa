package com.app.cookbook.xinhe.foodfamily.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.callback.DialogCallBack;
import com.app.cookbook.xinhe.foodfamily.main.AddQuestionActivity;
import com.app.cookbook.xinhe.foodfamily.main.adapter.DraftQuestionAdapter;
import com.app.cookbook.xinhe.foodfamily.main.db.NoteDao;
import com.app.cookbook.xinhe.foodfamily.main.db.entity.Note;
import com.app.cookbook.xinhe.foodfamily.main.entity.DraftQuestionEntity;
import com.app.cookbook.xinhe.foodfamily.net.GsonSubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.Network;
import com.app.cookbook.xinhe.foodfamily.net.ProgressSubscriberNew;
import com.app.cookbook.xinhe.foodfamily.net.SubscriberOnNextListener;
import com.app.cookbook.xinhe.foodfamily.net.entity.Bean;
import com.app.cookbook.xinhe.foodfamily.util.DateUtils;
import com.app.cookbook.xinhe.foodfamily.util.PopWindowHelper;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

public class DraftQuestionFragment extends android.support.v4.app.Fragment {
    @BindView(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    private DraftQuestionAdapter draftQuestionAdapter;
    LinearLayoutManager layoutManager;
    List<DraftQuestionEntity.DraftQuestionEn> searchEns = new ArrayList<>();
    /**
     * Rxjava
     */
    protected Subscription subscription;
    private boolean is_not_more;
    NoteDao noteDao;
    private String message_id = "";

    public DraftQuestionFragment() {

    }

    public static DraftQuestionFragment newInstance(String title) {
        DraftQuestionFragment fragment = new DraftQuestionFragment();
        Bundle args = new Bundle();
        args.putString("draft_fragment_title", title);
        fragment.setArguments(args);
        return fragment;
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_draft_question, container, false);
        }
        ButterKnife.bind(this, view);//绑定黄牛刀
        noteDao = new NoteDao(getActivity());
        //isVisible = true;
        return view;
    }

//    /**
//     * @param isVisibleToUser
//     */
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            Log.e("计算机反馈加快速度",""+isVisible);
//            if (isVisible){
//                message_id="";
//                initCaoGaoDate(true);
//            }
//            isVisible = true;
//        } else {
//            //相当于Fragment的onPause，为false时，Fragment不可见
//        }
//    }

    @Override
    public void onResume() {
        //Log.e("就是看的扣水电费","缴费基数看到了");
        message_id = "";
        initCaoGaoDate(true);
        super.onResume();
    }

    private void initCaoGaoDate(boolean is_show_dialog) {
        Map<String, String> params = new HashMap<>();
        params.put("draft_id", message_id);
        Log.e("设计费是点击", params.toString());
        subscription = Network.getInstance("获取问题草稿列表", getActivity())
                .get_question_caogao_liebiao(params,
                        new ProgressSubscriberNew<>(DraftQuestionEntity.class, new GsonSubscriberOnNextListener<DraftQuestionEntity>() {
                            @Override
                            public void on_post_entity(DraftQuestionEntity result) {
                                if (searchEns.size() > 0) {//这表示是"加载"
                                    if (message_id.equals("")) {//表示刷新
                                        searchEns.clear();
                                    }
                                    if (result.getData().size() == 0) {
                                        //表示没有更多数据了
                                        is_not_more = true;
                                        searchEns.addAll(result.getData());
                                        draftQuestionAdapter.notifyDataSetChanged();
                                    } else {
                                        is_not_more = false;
                                        searchEns.addAll(result.getData());
                                        draftQuestionAdapter.notifyDataSetChanged();
                                        message_id = searchEns.get(searchEns.size() - 1).getId() + "";
                                    }
                                } else {
                                    searchEns = result.getData();
                                    init_new_source(result.getData());
                                    message_id = searchEns.get(searchEns.size() - 1).getId() + "";
                                }
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                            }

                            @Override
                            public void onError(String error) {
                                is_not_more = true;
                                if (error.equals("无数据")) {
                                    if (searchEns.size() > 0) {
                                        if (searchEns.size() == 1) {//防止最后一条数据不更新
                                         //   List<DraftQuestionEntity.DraftQuestionEn> mdates = new ArrayList<>();
                                            List<DraftQuestionEntity.DraftQuestionEn> mdates = new ArrayList<>();
                                            init_new_source(mdates);
                                            //init_new_source(searchEns);

                                        }
                                        if (delte_true) {//未满8条删除
                                            draftQuestionAdapter.notifyDataSetChanged();
                                            delte_true = false;
                                        }
                                    } else {
                                        List<DraftQuestionEntity.DraftQuestionEn> mdates = new ArrayList<>();
                                        init_new_source(mdates);
                                    }

                                } else {
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
//
                                }
                            }
                        }, getActivity(), is_show_dialog));
    }


    private void init_new_source(final List<DraftQuestionEntity.DraftQuestionEn> searchEns) {
        //设置上拉刷新下拉加载
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemAnimator(null);
        layoutManager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(layoutManager);
        draftQuestionAdapter = new DraftQuestionAdapter(searchEns, getActivity());
        draftQuestionAdapter.setmOnItemLongClickListener(new DraftQuestionAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int mposition) {
                PopWindowHelper.public_tishi_pop(getActivity(), "食与家提示", "是否删除？", "取消", "确定", new DialogCallBack() {
                    @Override
                    public void save() {
                        DraftQuestionAdapter.select_point=-1;
                        delete_caogao_question(mposition, searchEns.get(mposition).getId() + "");
                    }

                    @Override
                    public void cancel() {
                        DraftQuestionAdapter.select_point=-1;
                        draftQuestionAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        draftQuestionAdapter.setOnItemClickListener(new DraftQuestionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Note note = new Note();
                saveNoteData(note, searchEns.get(position).getId() + "", searchEns.get(position).getTitle(), searchEns.get(position).getContent(), searchEns.get(position).getContent());
                //存储选中的
                Log.e("123", "  ---->>>>>     " + searchEns.get(position).getClassification_id());
                if (!TextUtils.isEmpty(searchEns.get(position).getClassification_id())) {
                    String str2 = searchEns.get(position).getClassification_id().replace(" ", "");//去掉所用空格
                    List<String> list = Arrays.asList(str2.split(","));
                    List arrList = new ArrayList(list);
                    try {
                        str2 = SharedPreferencesHelper.SceneList2String(arrList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("123", "      str2     " + str2 + "           " + arrList);
                    SharedPreferencesHelper.put(getContext(), "selectStrName", str2);
                    SharedPreferencesHelper.put(getContext(), "selectStr", str2);
                }
                Intent intent = new Intent(getActivity(), AddQuestionActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtra("flag", 3);//编辑笔记
                intent.putExtra("question_id", searchEns.get(position).getId() + "");
                intent.putExtra("data", bundle);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        recycler_view.setAdapter(draftQuestionAdapter);
        // 静默加载模式不能设置footerview
        // 设置静默加载模式
        // xRefreshView1.setSilenceLoadMore();
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
        xrefreshview.enableRecyclerViewPullUp(true);
        xrefreshview.enablePullUpWhenLoadCompleted(true);
        xrefreshview.setPreLoadCount(10);
        //设置静默加载时提前加载的item个数
//        xefreshView1.setPreLoadCount(4);
        if (searchEns.size() >= 8) {
            xrefreshview.enableReleaseToLoadMore(true);
//            draftQuestionAdapter.setCustomLoadMoreView(new XRefreshViewFooter(getActivity()));//加载更多
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
                        message_id = "";
                        //填写刷新数据的网络请求，一般page=1，List集合清空操作
                        initCaoGaoDate(false);
                        xrefreshview.stopRefresh();//刷新停止

                    }
                }, 1000);//2000是刷新的延时，使得有个动画效果
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //填写加载更多的网络请求，一般page++
                        initCaoGaoDate(false);

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

    private void saveNoteData(Note note, String answer_id, String question_title, String content, String mcontent) {
        //以问题的ID作为分类
        note.setTitle(question_title);
        note.setContent(content);
        note.setMcontent(mcontent);
        note.setGroupId(Integer.valueOf(answer_id));
        note.setGroupName(answer_id);
        note.setType(2);
        note.setBgColor("#FFFFFF");
        note.setIsEncrypt(0);
        note.setCreateTime(DateUtils.date2string(new Date()));
        //新建笔记
        long noteId = noteDao.insertNote(note);
        Log.i("", "新建立note答案Id: " + noteId);
    }

    private boolean delte_true;

    private void delete_caogao_question(final int position, String id) {
        Map<String, String> params = new HashMap<>();
        params.put("draft_id", id);
        Log.e(" 删除草稿箱", params.toString());
        subscription = Network.getInstance("删除草稿箱", getActivity())
                .delete_question_caozao(params,
                        new ProgressSubscriberNew<>(List.class, new GsonSubscriberOnNextListener<List>() {
                            @Override
                            public void on_post_entity(List list) {
                                Log.e("123", "删除草稿箱成功：" + "fsdf");
                                //删除第一页数据之后自动加载第二页数据
                                if (searchEns.size() <= 8) {
                                    searchEns.remove(position);
                                    delte_true = true;

                                    initCaoGaoDate(false);
                                } else {
                                    searchEns.remove(position);
                                    draftQuestionAdapter.notifyDataSetChanged();
                                }
                            }
                        }, new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {

                            }

                            @Override
                            public void onError(String error) {
                            }
                        }, getActivity(), false));
    }


}

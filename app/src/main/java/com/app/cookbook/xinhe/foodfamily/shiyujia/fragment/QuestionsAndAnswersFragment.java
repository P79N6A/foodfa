package com.app.cookbook.xinhe.foodfamily.shiyujia.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.MainSearchActivity;
import com.app.cookbook.xinhe.foodfamily.main.fragment.HotFragment;
import com.app.cookbook.xinhe.foodfamily.main.fragment.NewFragment;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.MessageActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.adapter.TabItemAdapter;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionsAndAnswersFragment extends Fragment implements View.OnClickListener {

    private View view;
    @BindView(R.id.message_alert)
    ImageView message_alert;
    @BindView(R.id.mesg_hint)
    TextView mesg_hint;
    @BindView(R.id.search_btn)
    ImageView search_btn;
    //精选
    @BindView(R.id.handpick_layout)
    RelativeLayout handpick_layout;
    @BindView(R.id.handpick_tv)
    TextView handpick_tv;
    @BindView(R.id.handpick_im)
    TextView handpick_im;
    //答案
    @BindView(R.id.answer_layout)
    RelativeLayout answer_layout;
    @BindView(R.id.answer_tv)
    TextView answer_tv;
    @BindView(R.id.answer_im)
    TextView answer_im;
    //问题
    @BindView(R.id.issue_layout)
    RelativeLayout issue_layout;
    @BindView(R.id.issue_tv)
    TextView issue_tv;
    @BindView(R.id.issue_im)
    TextView issue_im;

    @BindView(R.id.viewPager)
    ViewPager viewPager;


    private ArrayList<Fragment> mFragments;

    public static QuestionsAndAnswersFragment newInstance(String title) {
        QuestionsAndAnswersFragment fragment = new QuestionsAndAnswersFragment();
        Bundle args = new Bundle();
        args.putString("home_fragment_title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.questions_and_answers_fragment, container, false);
            ButterKnife.bind(this, view);//绑定黄牛刀
            initView();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        mFragments = new ArrayList<>();
        mFragments.add(new HandpickFragment());
        mFragments.add(new AnswerFragment());
        mFragments.add(new IssueFragment());

        TabItemAdapter myAdapter = new TabItemAdapter(getActivity().getSupportFragmentManager(), mFragments);// 初始化adapter
        viewPager.setAdapter(myAdapter); // 设置adapter

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPs) {
                setTabTextColorAndImageView(position, positionOffset);// 更改text的颜色还有图片
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //下面的tab设置点击监听
        message_alert.setOnClickListener(this);
        search_btn.setOnClickListener(this);
        handpick_layout.setOnClickListener(this);
        answer_layout.setOnClickListener(this);
        issue_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.handpick_layout:
                viewPager.setCurrentItem(0);
                break;
            case R.id.answer_layout:
                viewPager.setCurrentItem(1);
                break;
            case R.id.issue_layout:
                viewPager.setCurrentItem(2);
                break;
            case R.id.search_btn:
                intent = new Intent(getActivity(), MainSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.message_alert:
                intent = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void setTabTextColorAndImageView(int position, float positionOffset) {
        switch (position) {
            case 0:
                handpick_tv.setTextSize(20);
                handpick_tv.setTextColor(getResources().getColor(R.color.color_0EB39B));
                handpick_im.setVisibility(View.VISIBLE);
                answer_tv.setTextSize(16);
                answer_tv.setTextColor(getResources().getColor(R.color.color_292c31));
                answer_im.setVisibility(View.GONE);
                issue_tv.setTextSize(16);
                issue_tv.setTextColor(getResources().getColor(R.color.color_292c31));
                issue_im.setVisibility(View.GONE);
                break;
            case 1:
                answer_tv.setTextSize(20);
                answer_tv.setTextColor(getResources().getColor(R.color.color_0EB39B));
                answer_im.setVisibility(View.VISIBLE);
                handpick_tv.setTextSize(16);
                handpick_tv.setTextColor(getResources().getColor(R.color.color_292c31));
                handpick_im.setVisibility(View.GONE);
                issue_tv.setTextSize(16);
                issue_tv.setTextColor(getResources().getColor(R.color.color_292c31));
                issue_im.setVisibility(View.GONE);
                break;
            case 2:
                issue_tv.setTextSize(20);
                issue_tv.setTextColor(getResources().getColor(R.color.color_0EB39B));
                issue_im.setVisibility(View.VISIBLE);
                handpick_tv.setTextSize(16);
                handpick_tv.setTextColor(getResources().getColor(R.color.color_292c31));
                handpick_im.setVisibility(View.GONE);
                answer_tv.setTextSize(16);
                answer_tv.setTextColor(getResources().getColor(R.color.color_292c31));
                answer_im.setVisibility(View.GONE);
                break;
        }
    }
}

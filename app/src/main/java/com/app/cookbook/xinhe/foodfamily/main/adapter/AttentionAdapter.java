package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.callback.ImageBiggerCallBack;
import com.app.cookbook.xinhe.foodfamily.main.AnserActivity;
import com.app.cookbook.xinhe.foodfamily.main.FenLeiQuestionDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.SuggestedUsersActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.AttentionList;
import com.app.cookbook.xinhe.foodfamily.main.entity.SuggestedUsers;
import com.app.cookbook.xinhe.foodfamily.util.CircleImageView;
import com.app.cookbook.xinhe.foodfamily.shiyujia.util.Contacts;
import com.app.cookbook.xinhe.foodfamily.util.FormatCurrentData;
import com.app.cookbook.xinhe.foodfamily.util.otherUi.ListViewForScrollView;
import com.bumptech.glide.Glide;
import com.tencent.stat.StatService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import rx.Subscription;

/**
 * Created by shiyujia02 on 2018/1/28.
 */

public class AttentionAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<AttentionList.DataBean> list;
    private Context context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    ImageBiggerCallBack imageBiggerCallBack;
    //推荐用户
    private LinearLayoutManager suggestedUsers_layoutManager;
    private SuggestedUsersAdapter suggestedUsersAdapter;
    private List<SuggestedUsers.DataBean> suggestedUsers = new ArrayList<>();


    public AttentionAdapter(List<AttentionList.DataBean> mlist, Context mcontext, ImageBiggerCallBack mimageBiggerCallBack) {
        imageBiggerCallBack = mimageBiggerCallBack;
        list = mlist;
        context = mcontext;
    }

    public void setSuggestedUsers(List<SuggestedUsers.DataBean> suggestedUsers) {
        this.suggestedUsers = suggestedUsers;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        PurchaseViewHolder purchaseViewHolder = new PurchaseViewHolder(view, false);
        return purchaseViewHolder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        RecyclerView.ViewHolder holder = getViewHolderByViewType(viewType, parent);
        return holder;
    }

    private RecyclerView.ViewHolder getViewHolderByViewType(int viewType, ViewGroup parent) {
        View empty_view = LayoutInflater.from(context).inflate(R.layout.attention_empty_layout, parent, false);
        View shidan_view = LayoutInflater.from(context).inflate(R.layout.attention_item, parent, false);
        RecyclerView.ViewHolder holder = null;
        if (viewType == EMPTY_VIEW) {
            holder = new EmptyViewHolder(empty_view, true);
        } else {
            holder = new PurchaseViewHolder(shidan_view, true);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, final int position, boolean isItem) {
        if (view_holder instanceof PurchaseViewHolder) {
            final PurchaseViewHolder holder = (PurchaseViewHolder) view_holder;
            final AttentionList.DataBean item = list.get(position);

            if (position == 0) {
                holder.suggested_layout.setVisibility(View.VISIBLE);
                //推荐用户
                if (Contacts.isGone == false) {
                    if (suggestedUsers.size() > 0) {
                        suggestedUsers_layoutManager = new LinearLayoutManager(context);
                        suggestedUsers_layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        holder.suggestedUsers_recycler.setLayoutManager(suggestedUsers_layoutManager);
                        holder.suggestedUsers_recycler.setHasFixedSize(true);
                        suggestedUsersAdapter = new SuggestedUsersAdapter(context);
                        suggestedUsersAdapter.setSuggestedUsers(suggestedUsers);
                        holder.suggestedUsers_recycler.setAdapter(suggestedUsersAdapter);
                        if (Contacts.scrollPosition < 1) {
                            holder.suggestedUsers_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                                    int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
                                    Contacts.scrollPosition = lastItemPosition;
                                }
                            });
                        } else {
                            holder.suggestedUsers_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                                    int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
                                    if (Contacts.scrollPosition != lastItemPosition && lastItemPosition > 0) {
                                        Contacts.scrollPosition = lastItemPosition;
                                    }
                                }
                            });
                            holder.suggestedUsers_recycler.scrollToPosition(Contacts.scrollPosition - 2);
                        }
                        suggestedUsersAdapter.notifyDataSetChanged();
                    } else {
                        holder.suggested_layout.setVisibility(View.GONE);
                    }
                } else {
                    holder.suggested_layout.setVisibility(View.GONE);
                }
                //设置提问者头像
                Glide.with(context).load(item.getUser_data().getAvatar())
                        //.centerCrop()
                        .asBitmap()
                        .error(R.drawable.touxiang)
                        .into(holder.question_user_image);
                //设置提问者名称
                holder.question_user_name.setText(item.getUser_data().getName());

                if ("collection".equals(item.getT_name())) {
                    holder.attenion_type.setText("收藏了回答");
                } else {
                    holder.attenion_type.setText("回答了问题");
                }

                //设置提问时间
                if (item.getCreated_at() != null && !TextUtils.isEmpty(item.getCreated_at())) {
                    holder.question_user_time.setText(FormatCurrentData.getTimeRange(Long.valueOf(item.getCreated_at())));
                }
                //设置答案头像
                Glide.with(context).load(item.getAnswer_user_data().getAvatar())
                        //.centerCrop()
                        .asBitmap()
                        .error(R.drawable.touxiang)
                        .into(holder.answer_touxiang_image);
                //设置答案名称
                holder.answer_user_name.setText(item.getAnswer_user_data().getName());
                //设置答案时间
//                if (item.getAnswer_created_at() != null && !TextUtils.isEmpty(item.getAnswer_created_at())) {
//                    holder.answer_time.setText(FormatCurrentData.getTimeRange(Long.valueOf(item.getAnswer_created_at())));
//                }
                if (!TextUtils.isEmpty(item.getAnswer_user_data().getPersonal_signature())) {
                    holder.answer_time.setText(item.getAnswer_user_data().getPersonal_signature());
                } else {
                    if (!TextUtils.isEmpty(item.getAnswer_user_data().getProfession())) {
                        holder.answer_time.setText(item.getAnswer_user_data().getProfession());
                    } else {
                        holder.answer_time.setText("保密");
                    }
                }

                //设置标题
                holder.question_content.setText(item.getQuestion_data().getTitle());

                //设置答案图片
                if (null != item.getAnswer_img_data() && !TextUtils.isEmpty(item.getAnswer_img_data().getPath())) {
                    //默认只显示头一张图片
                    holder.answer_images_view.setVisibility(View.VISIBLE);
                    List<AttentionList.DataBean.AnswerImgDataBean> show_imageDates = new ArrayList<>();
                    show_imageDates.add(item.getAnswer_img_data());
                    AttentionImagesAdapter imagesAdapter = new AttentionImagesAdapter(show_imageDates, context);
                    holder.answer_images_view.setAdapter(imagesAdapter);
                    holder.answer_images_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                            imageBiggerCallBack.image_url(view, item.getAnswer_img_data().getPath());
                            return false;
                        }
                    });
                } else {
                    holder.answer_images_view.setVisibility(View.GONE);
                }
                //设置答案内容
                if (TextUtils.isEmpty(item.getContent_remove()) || null == item.getContent_remove()) {
                    if (null == item.getAnswer_img_data() && TextUtils.isEmpty(item.getAnswer_img_data().getPath())) {
                        holder.answer_content.setVisibility(View.VISIBLE);
                        holder.answer_content.setTextColor(context.getResources().getColor(R.color.color_bbbbbb));
                        holder.answer_content.setText("期待您的见解...");
                    } else {
                        holder.answer_content.setVisibility(View.GONE);
                    }
                } else {
                    holder.answer_content.setVisibility(View.VISIBLE);
                    holder.answer_content.setTextColor(context.getResources().getColor(R.color.color_565656));
                    holder.answer_content.setText(Html.fromHtml(item.getContent_remove()));
                }

                //设置点赞数
                holder.zan_number.setText(item.getAnswer_thumbs() + "个赞");
                //设置评论数
                holder.comment_number.setText(item.getComment_count() + "条评论");

            } else {
                holder.suggested_layout.setVisibility(View.GONE);

                //设置提问者头像
                Glide.with(context).load(item.getUser_data().getAvatar())
                        //.centerCrop()
                        .asBitmap()
                        .error(R.drawable.touxiang)
                        .into(holder.question_user_image);
                //设置提问者名称
                holder.question_user_name.setText(item.getUser_data().getName());

                if ("collection".equals(item.getT_name())) {
                    holder.attenion_type.setText("收藏了回答");
                } else {
                    holder.attenion_type.setText("回答了问题");
                }
                //设置提问时间
                if (item.getCreated_at() != null && !TextUtils.isEmpty(item.getCreated_at())) {
                    holder.question_user_time.setText(FormatCurrentData.getTimeRange(Long.valueOf(item.getCreated_at())));
                }
                //设置答案头像
                Glide.with(context).load(item.getAnswer_user_data().getAvatar())
                        //.centerCrop()
                        .asBitmap()
                        .error(R.drawable.touxiang)
                        .into(holder.answer_touxiang_image);
                //设置答案名称
                holder.answer_user_name.setText(item.getAnswer_user_data().getName());
                //设置答案时间
//                if (item.getAnswer_created_at() != null && !TextUtils.isEmpty(item.getAnswer_created_at())) {
////                    holder.answer_time.setText(FormatCurrentData.getTimeRange(Long.valueOf(item.getAnswer_created_at())));
////                }
                if (!TextUtils.isEmpty(item.getAnswer_user_data().getPersonal_signature())) {
                    holder.answer_time.setText(item.getAnswer_user_data().getPersonal_signature());
                } else {
                    if (!TextUtils.isEmpty(item.getAnswer_user_data().getProfession())) {
                        holder.answer_time.setText(item.getAnswer_user_data().getProfession());
                    } else {
                        holder.answer_time.setText("保密");
                    }
                }

                //设置标题
                holder.question_content.setText(item.getQuestion_data().getTitle());

                //设置答案图片
                if (null != item.getAnswer_img_data() && !TextUtils.isEmpty(item.getAnswer_img_data().getPath())) {
                    //默认只显示头一张图片
                    holder.answer_images_view.setVisibility(View.VISIBLE);
                    List<AttentionList.DataBean.AnswerImgDataBean> show_imageDates = new ArrayList<>();
                    show_imageDates.add(item.getAnswer_img_data());
                    AttentionImagesAdapter imagesAdapter = new AttentionImagesAdapter(show_imageDates, context);
                    holder.answer_images_view.setAdapter(imagesAdapter);
                    holder.answer_images_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                            imageBiggerCallBack.image_url(view, item.getAnswer_img_data().getPath());
                            return false;
                        }
                    });
                } else {
                    holder.answer_images_view.setVisibility(View.GONE);
                }
                //设置答案内容
                if (TextUtils.isEmpty(item.getContent_remove()) || null == item.getContent_remove()) {
                    if (null == item.getAnswer_img_data() && TextUtils.isEmpty(item.getAnswer_img_data().getPath())) {
                        holder.answer_content.setVisibility(View.VISIBLE);
                        holder.answer_content.setTextColor(context.getResources().getColor(R.color.color_bbbbbb));
                        holder.answer_content.setText("期待您的见解...");
                    } else {
                        holder.answer_content.setVisibility(View.GONE);
                    }
                } else {
                    holder.answer_content.setVisibility(View.VISIBLE);
                    holder.answer_content.setTextColor(context.getResources().getColor(R.color.color_565656));
                    holder.answer_content.setText(Html.fromHtml(item.getContent_remove()));
                }
                //设置点赞数
                holder.zan_number.setText(item.getAnswer_thumbs() + "个赞");
                //设置评论数
                holder.comment_number.setText(item.getComment_count() + "条评论");
            }

            //推荐用户更多选择
            holder.more_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select_setting_popwindow(holder.more_btn);
                }
            });
            //点击答案图片跳转答案详情頁
            holder.answer_images_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int mposition, long id) {
                    Intent intent = new Intent(context, AnserActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("answer_id", item.getAnswer_id());
                    bundle.putString("question_id", item.getQuestion_id());
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            //点击答案文字跳转答案详情頁
            holder.answer_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Properties prop = new Properties();
                    prop.setProperty("name", "question_answer");
                    StatService.trackCustomKVEvent(context, "Selected_details_answer", prop);

                    Intent intent = new Intent(context, AnserActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("answer_id", item.getAnswer_id());
                    bundle.putString("question_id", item.getQuestion_id());
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            //点击点赞和评论布局跳转答案详情頁
            holder.answer_zan_alyout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Properties prop = new Properties();
                    prop.setProperty("name", "question_answer");
                    StatService.trackCustomKVEvent(context, "Selected_details_answer", prop);

                    Intent intent = new Intent(context, AnserActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("answer_id", item.getAnswer_id());
                    bundle.putString("question_id", item.getQuestion_id());
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            //点击答案文字跳转答案详情頁
            holder.answer_touxiang_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Properties prop = new Properties();
                    prop.setProperty("name", "question_answer");
                    StatService.trackCustomKVEvent(context, "Selected_details_answer", prop);

                    Intent intent = new Intent(context, AnserActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("answer_id", item.getAnswer_id());
                    bundle.putString("question_id", item.getQuestion_id());
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            //跳转问题页面
            holder.question_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Properties prop = new Properties();
                    prop.setProperty("name", "question_content");
                    StatService.trackCustomKVEvent(context, "Selected_details_problem", prop);

                    Intent intent = new Intent(context, FenLeiQuestionDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("wenti_id", item.getQuestion_id());
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            //条目点击
            ((PurchaseViewHolder) view_holder).view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });
        } else if (view_holder instanceof EmptyViewHolder) {
            final EmptyViewHolder holder = (EmptyViewHolder) view_holder;
            //推荐用户
            if (Contacts.isGone == false) {
                if (suggestedUsers.size() > 0) {
                    suggestedUsers_layoutManager = new LinearLayoutManager(context);
                    suggestedUsers_layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    holder.suggestedUsers_recycler.setLayoutManager(suggestedUsers_layoutManager);
                    holder.suggestedUsers_recycler.setHasFixedSize(true);
                    suggestedUsersAdapter = new SuggestedUsersAdapter(context);
                    suggestedUsersAdapter.setSuggestedUsers(suggestedUsers);
                    holder.suggestedUsers_recycler.setAdapter(suggestedUsersAdapter);
                    if (Contacts.scrollPosition < 1) {
                        holder.suggestedUsers_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                                int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
                                Contacts.scrollPosition = lastItemPosition;
                            }
                        });
                    } else {
                        holder.suggestedUsers_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                                int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
                                if (Contacts.scrollPosition != lastItemPosition && lastItemPosition > 0) {
                                    Contacts.scrollPosition = lastItemPosition;
                                }
                            }
                        });
                        holder.suggestedUsers_recycler.scrollToPosition(Contacts.scrollPosition - 2);
                    }
                    suggestedUsersAdapter.notifyDataSetChanged();
                } else {
                    holder.suggested_layout.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.empty_layout.getLayoutParams();
                    layoutParams.setMargins(0, 300, 0, 0);
                    holder.empty_layout.setLayoutParams(layoutParams);
                }
            } else {
                holder.suggested_layout.setVisibility(View.GONE);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.empty_layout.getLayoutParams();
                layoutParams.setMargins(0, 300, 0, 0);
                holder.empty_layout.setLayoutParams(layoutParams);
            }
            //推荐用户更多选择
            holder.more_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select_setting_popwindow(holder.more_btn);
                }
            });
        }
    }


    /**
     * 时间戳转日期
     *
     * @param ms
     * @return
     */
    public static Date transForDate(Long ms) {
        if (ms == null) {
            ms = (long) 0;
        }
        long msl = (long) ms * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date temp = null;
        if (ms != null) {
            try {
                String str = sdf.format(msl);
                temp = sdf.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    /**
     * Rxjava
     */
    protected Subscription subscription;

    @Override
    public int getAdapterItemViewType(int position) {
        if (list.size() == 0) {
            return EMPTY_VIEW;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getAdapterItemCount() {
        return list.size() > 0 ? list.size() : 1;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setList(List<AttentionList.DataBean> list) {
        this.list = list;
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_top;
        public View view;
        //推荐用户
        private LinearLayout suggested_layout;
        private RecyclerView suggestedUsers_recycler;
        //更多选择
        private ImageView more_btn;
        //空图标布局
        private RelativeLayout empty_layout;

        public EmptyViewHolder(View item_view, boolean isItem) {
            super(item_view);
            if (isItem) {
                this.view = item_view;
                img_top = (ImageView) item_view
                        .findViewById(R.id.img_top);
                //推荐用户
                suggested_layout = itemView.findViewById(R.id.suggested_layout);
                suggestedUsers_recycler = itemView.findViewById(R.id.suggestedUsers_recycler);
                //更多
                more_btn = itemView.findViewById(R.id.more_btn);
                //空图标布局
                empty_layout = itemView.findViewById(R.id.empty_layout);
            }
        }
    }

    public class PurchaseViewHolder extends RecyclerView.ViewHolder {
        public View view;
        //推荐用户
        private LinearLayout suggested_layout;
        private RecyclerView suggestedUsers_recycler;
        //更多选择
        private ImageView more_btn;
        //提问者信息布局
        private RelativeLayout question_user_layout;
        //提问者头像
        private CircleImageView question_user_image;
        //提问者名称
        private TextView question_user_name;
        //提问时间
        private TextView question_user_time;
        //提问标题
        private TextView question_content;
        //回答布局
        private RelativeLayout answer_touxiang_layout;
        //回答者头像
        private CircleImageView answer_touxiang_image;
        //回答者名称
        private TextView answer_user_name;
        //回答时间
        private TextView answer_time;
        //回答图片
        private ListViewForScrollView answer_images_view;
        //回答内容
        private TextView answer_content;
        //点赞评论数布局
        private RelativeLayout answer_zan_alyout;
        //获赞数
        private TextView zan_number;
        //评论数
        private TextView comment_number;
        //问题类型
        private TextView attenion_type;

        public PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                //推荐用户
                suggested_layout = itemView.findViewById(R.id.suggested_layout);
                suggestedUsers_recycler = itemView.findViewById(R.id.suggestedUsers_recycler);
                //更多
                more_btn = itemView.findViewById(R.id.more_btn);
                //提问者信息布局
                question_user_layout = itemView.findViewById(R.id.question_user_layout);
                //提问者头像
                question_user_image = itemView.findViewById(R.id.question_user_image);
                //提问者名称
                question_user_name = itemView.findViewById(R.id.question_user_name);
                //提问时间
                question_user_time = itemView.findViewById(R.id.question_user_time);
                //提问标题
                question_content = itemView.findViewById(R.id.question_content);
                //回答布局
                answer_touxiang_layout = itemView.findViewById(R.id.answer_touxiang_layout);
                //回答者头像
                answer_touxiang_image = itemView.findViewById(R.id.answer_touxiang_image);
                //回答者名称
                answer_user_name = itemView.findViewById(R.id.answer_user_name);
                //回答时间
                answer_time = itemView.findViewById(R.id.answer_time);
                //回答图片
                answer_images_view = itemView.findViewById(R.id.answer_images_view);
                //回答内容
                answer_content = itemView.findViewById(R.id.answer_content);
                //点赞评论数布局
                answer_zan_alyout = itemView.findViewById(R.id.answer_zan_alyout);
                //获赞数
                zan_number = itemView.findViewById(R.id.zan_number);
                //评论数
                comment_number = itemView.findViewById(R.id.comment_number);
                //问题类型
                attenion_type = itemView.findViewById(R.id.attenion_type);
            }
        }
    }


    private void select_setting_popwindow(final View views) {
        LayoutInflater factory = LayoutInflater.from(context);
        View view = factory.inflate(R.layout.select_setting_popwindow, null);
        final Dialog dialog = new Dialog(context, R.style.transparentFrameWindowStyle2);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
//        wl.y = context.getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        //查看更多
        LinearLayout select_bianji_ziliao = view.findViewById(R.id.select_bianji_ziliao);
        select_bianji_ziliao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SuggestedUsersActivity.class);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        //关闭推荐用户
        LinearLayout select_close = view.findViewById(R.id.select_close);
        select_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                views.setVisibility(View.GONE);
                Contacts.isGone = true;
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        //取消
        LinearLayout select_dismiss = view.findViewById(R.id.select_dismiss);
        select_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //取消
        FrameLayout close_btn = view.findViewById(R.id.close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}

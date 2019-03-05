package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.AnserActivity;
import com.app.cookbook.xinhe.foodfamily.main.AnswerReplyByIdActivity;
import com.app.cookbook.xinhe.foodfamily.main.MessageCommentActivity;
import com.app.cookbook.xinhe.foodfamily.main.OtherUserActivity;
import com.app.cookbook.xinhe.foodfamily.main.entity.DianZanEntity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.CommentVideoReplyActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.CommtentReplyActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.FriendPageActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.ImageTextDetailActivity;
import com.app.cookbook.xinhe.foodfamily.shiyujia.activity.VideoDetailsActivity;
import com.app.cookbook.xinhe.foodfamily.util.DateUtils;
import com.app.cookbook.xinhe.foodfamily.util.FormatCurrentData;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by shiyujia02 on 2018/1/25.
 */

public class DianZanAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<DianZanEntity.DataBean> list;
    private Context context;
    private static final int EMPTY_VIEW = 0;
    private static final int TYPE_ITEM = 1;
    private static final int SHOUCANG_ITEM = 2;
    private static final int PINGLUN_ITEM = 3;
    private static final int HUIFU_ITEM = 4;
    private static final int TUWEN_PING_ZAN_ITEM = 5;
    private static final int TUWEN_ZAN_HUI_ITEM = 6;
    private static final int TUWEN_ZAN_ITEM = 7;
    private static final int TUWEN_CANG_ITEM = 8;
    private static final int SHIPIN_ZAN_ITEM = 9;
    private static final int SHIPIN_CANG_ITEM = 10;
    private static final int SHIPIN_PING_ZAN_ITEM = 11;
    private static final int SHIPIN_ZAN_HUI_ITEM = 12;

    public DianZanAdapter(List<DianZanEntity.DataBean> mlist, Context mcontext) {
        list = mlist;
        context = mcontext;
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
        View empty_view = LayoutInflater.from(context).inflate(R.layout.dianzan_empty_layout, parent, false);
        View shidan_view = LayoutInflater.from(context).inflate(R.layout.dianzan_shoucang_item, parent, false);
        View shoucang_view = LayoutInflater.from(context).inflate(R.layout.dianzan_shoucang_item2, parent, false);
        View pinglun_view = LayoutInflater.from(context).inflate(R.layout.dianzan_pinglun_item, parent, false);
        View huifu_view = LayoutInflater.from(context).inflate(R.layout.dianzan_pinglun_item, parent, false);

        RecyclerView.ViewHolder holder = null;
        if (viewType == EMPTY_VIEW) {
            holder = new EmptyViewHolder(empty_view, true);
        } else if (viewType == SHOUCANG_ITEM) {
            holder = new ShouCang_PurchaseViewHolder(shoucang_view, true);
        } else if (viewType == PINGLUN_ITEM) {
            holder = new PingLun_PurchaseViewHolder(pinglun_view, true);
        } else if (viewType == HUIFU_ITEM) {
            holder = new Huifu_PurchaseViewHolder(huifu_view, true);
        } else if (viewType == TUWEN_PING_ZAN_ITEM) {
            holder = new Huifu_PurchaseViewHolder(huifu_view, true);
        } else if (viewType == TUWEN_ZAN_HUI_ITEM) {
            holder = new Huifu_PurchaseViewHolder(huifu_view, true);
        } else if (viewType == TUWEN_ZAN_ITEM) {
            holder = new Huifu_PurchaseViewHolder(huifu_view, true);
        } else if (viewType == TUWEN_CANG_ITEM) {
            holder = new Huifu_PurchaseViewHolder(huifu_view, true);
        } else if (viewType == SHIPIN_ZAN_ITEM) {
            holder = new Huifu_PurchaseViewHolder(huifu_view, true);
        } else if (viewType == SHIPIN_CANG_ITEM) {
            holder = new Huifu_PurchaseViewHolder(huifu_view, true);
        } else if (viewType == SHIPIN_PING_ZAN_ITEM) {
            holder = new Huifu_PurchaseViewHolder(huifu_view, true);
        } else if (viewType == SHIPIN_ZAN_HUI_ITEM) {
            holder = new Huifu_PurchaseViewHolder(huifu_view, true);
        } else {
            holder = new PurchaseViewHolder(shidan_view, true);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, final int position, boolean isItem) {
        if (getAdapterItemViewType(position) == EMPTY_VIEW) {//空状态

        } else if (getAdapterItemViewType(position) == TYPE_ITEM) {//点赞
            PurchaseViewHolder holder = (PurchaseViewHolder) view_holder;
            if (position == 0) {
                holder.title.setVisibility(View.VISIBLE);
            } else {
                holder.title.setVisibility(View.GONE);
            }

            Glide.with(context).load(list.get(position).getUser_data().getAvatar())
                    .error(R.drawable.touxiang)
                    .into(holder.select_head_image);

            holder.state_tv.setText("赞了回答");

            holder.user_name.setText(list.get(position).getUser_data().getName());

            holder.time_tv.setText(FormatCurrentData.getTimeRange(Long.valueOf(list.get(position).getCreated_at())));

            holder.content_tv.setText(list.get(position).getAnswer_data().getContent_remove());
            if (holder.content_tv.getLineCount() == 1) {
                //如果答案有图片，就显示图片文本【】
                if (list.get(position).getAnswer_data().getImg_count() != null) {
                    if ("0".equals(list.get(position).getAnswer_data().getImg_count().getImg_count())) {
                        holder.image_tv.setText("【图片】");
                        holder.image_tv.setVisibility(View.VISIBLE);
                    } else {
                        holder.image_tv.setVisibility(View.GONE);
                    }
                }
            } else if (holder.content_tv.getLineCount() == 2) {
                holder.image_tv.setVisibility(View.GONE);
            }

            ((PurchaseViewHolder) view_holder).view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });
            ((PurchaseViewHolder) view_holder).view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });

            holder.select_head_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", list.get(position).getUser_data().getId() + "");
                    context.startActivity(intent);
                }
            });

            holder.state_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getAnswer_data().getIs_existence())) {
                        Intent intent = new Intent(context, AnserActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("answer_id", list.get(position).getAnswer_id() + "");
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "答案已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //去往答案详情页
            holder.image_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getAnswer_data().getIs_existence())) {
                        Intent intent = new Intent(context, AnserActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("answer_id", list.get(position).getAnswer_id() + "");
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "答案已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //去往答案详情页
            holder.content_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getAnswer_data().getIs_existence())) {
                        Intent intent = new Intent(context, AnserActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("answer_id", list.get(position).getAnswer_id() + "");
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "答案已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            //给视图添加长按事件
            holder.image_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });
            holder.content_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });


            holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            holder.time_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            holder.user_name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });

        } else if (getAdapterItemViewType(position) == PINGLUN_ITEM) {//问答评论
            PingLun_PurchaseViewHolder pinglun_holder = (PingLun_PurchaseViewHolder) view_holder;
            if (position == 0) {
                pinglun_holder.title.setVisibility(View.VISIBLE);
            } else {
                pinglun_holder.title.setVisibility(View.GONE);
            }

            Glide.with(context).load(list.get(position).getUser_data().getAvatar())
                    .error(R.drawable.touxiang)
                    .into(pinglun_holder.select_head_image);

            pinglun_holder.state_tv.setText("赞了评论");

            pinglun_holder.user_name.setText(list.get(position).getUser_data().getName());

            pinglun_holder.time_tv.setText(DateUtils.getTimeFormatText(transForDate(Long.valueOf(list.get(position).getCreated_at()))));

            pinglun_holder.content_tv.setText(list.get(position).getComment_data().getContent());
            if (pinglun_holder.content_tv.getLineCount() == 1) {
                //如果答案有图片，就显示图片文本【】
                if (list.get(position).getAnswer_data().getImg_count() != null) {
                    if (!"0".equals(list.get(position).getAnswer_data().getImg_count().getImg_count())) {
                        pinglun_holder.image_tv.setText("【图片】");
                        pinglun_holder.image_tv.setVisibility(View.VISIBLE);
                    } else {
                        pinglun_holder.image_tv.setVisibility(View.GONE);
                    }
                }
            } else if (pinglun_holder.content_tv.getLineCount() == 2) {
                pinglun_holder.image_tv.setVisibility(View.GONE);
            }

            pinglun_holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });
            pinglun_holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });

            pinglun_holder.select_head_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", list.get(position).getUser_data().getId() + "");
                    context.startActivity(intent);
                }
            });

            //去往评论详情页
            pinglun_holder.content_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if ("1".equals(list.get(position).getComment_data().getIs_existence())) {
                        intent = new Intent(context, AnswerReplyByIdActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("answer_id", list.get(position).getAnswer_id() + "");
                        intent.putExtra("original_id", list.get(position).getComment_id());
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            pinglun_holder.state_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getComment_data().getIs_existence())) {
                        Intent intent = new Intent(context, AnswerReplyByIdActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("answer_id", list.get(position).getAnswer_id() + "");
                        intent.putExtra("original_id", list.get(position).getComment_id());
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            pinglun_holder.image_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getComment_data().getIs_existence())) {
                        Intent intent = new Intent(context, AnswerReplyByIdActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("answer_id", list.get(position).getAnswer_id() + "");
                        intent.putExtra("original_id", list.get(position).getComment_id());
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //给视图添加长按事件
            pinglun_holder.image_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            pinglun_holder.content_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });


            pinglun_holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            pinglun_holder.time_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            pinglun_holder.user_name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            pinglun_holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });

        } else if (getAdapterItemViewType(position) == HUIFU_ITEM) { //问答评论回复

            Huifu_PurchaseViewHolder huifu_holder = (Huifu_PurchaseViewHolder) view_holder;
            if (position == 0) {
                huifu_holder.title.setVisibility(View.VISIBLE);
            } else {
                huifu_holder.title.setVisibility(View.GONE);
            }

            Glide.with(context).load(list.get(position).getUser_data().getAvatar())
                    .error(R.drawable.touxiang)
                    .into(huifu_holder.select_head_image);

            huifu_holder.state_tv.setText("赞了回复");

            huifu_holder.user_name.setText(list.get(position).getUser_data().getName());

            huifu_holder.time_tv.setText(DateUtils.getTimeFormatText(transForDate(Long.valueOf(list.get(position).getCreated_at()))));

            huifu_holder.select_head_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", list.get(position).getUser_data().getId() + "");
                    context.startActivity(intent);
                }
            });

            if (list.get(position).getComment_data() != null) {
                huifu_holder.content_tv.setText(list.get(position).getComment_data().getContent());
                if (huifu_holder.content_tv.getLineCount() == 1) {
                    //如果答案有图片，就显示图片文本【】
                    if (list.get(position).getAnswer_data().getImg_count() != null) {
                        if (!"0".equals(list.get(position).getAnswer_data().getImg_count().getImg_count())) {
                            huifu_holder.image_tv.setText("【图片】");
                            huifu_holder.image_tv.setVisibility(View.VISIBLE);
                        } else {
                            huifu_holder.image_tv.setVisibility(View.GONE);
                        }
                    }
                } else if (huifu_holder.content_tv.getLineCount() == 2) {
                    huifu_holder.image_tv.setVisibility(View.GONE);
                }

                huifu_holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(v, position);
                        }
                    }
                });
                huifu_holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (onItemLongClickListener != null) {
                            onItemLongClickListener.onItemLongClick(v, position);
                        }
                        return false;
                    }
                });

                //去往评论详情页
                huifu_holder.content_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("1".equals(list.get(position).getComment_data().getIs_existence())) {
                            Intent intent = new Intent(context, AnswerReplyByIdActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("answer_id", list.get(position).getAnswer_id() + "");
                            intent.putExtra("original_id", list.get(position).getComment_id());
                            intent.putExtras(bundle);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            huifu_holder.state_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getComment_data().getIs_existence())) {
                        Intent intent = new Intent(context, AnswerReplyByIdActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("answer_id", list.get(position).getAnswer_id() + "");
                        intent.putExtra("original_id", list.get(position).getComment_id());
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            huifu_holder.image_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getComment_data().getIs_existence())) {
                        Intent intent = new Intent(context, AnswerReplyByIdActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("answer_id", list.get(position).getAnswer_id() + "");
                        intent.putExtra("original_id", list.get(position).getComment_id());
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //给视图添加长按事件
            huifu_holder.image_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.content_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });


            huifu_holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.time_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.user_name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });

        } else if (getAdapterItemViewType(position) == SHOUCANG_ITEM) {//收藏问答
            ShouCang_PurchaseViewHolder shouCang_purchaseViewHolder = (ShouCang_PurchaseViewHolder) view_holder;
            if (position == 0) {
                shouCang_purchaseViewHolder.title.setVisibility(View.VISIBLE);
            } else {
                shouCang_purchaseViewHolder.title.setVisibility(View.GONE);
            }

            Glide.with(context).load(list.get(position).getUser_data().getAvatar())
                    // .centerCrop()
                    // .placeholder(R.drawable.touxiang)
                    .error(R.drawable.touxiang)
                    .into(shouCang_purchaseViewHolder.select_head_image);

            shouCang_purchaseViewHolder.user_name.setText(list.get(position).getUser_data().getName());

            shouCang_purchaseViewHolder.time_tv.setText(
                    DateUtils.getTimeFormatText(transForDate(Long.valueOf(list.get(position).getCreated_at()))));

            shouCang_purchaseViewHolder.content_tv.setText(list.get(position).getAnswer_data().getContent_remove());

            if (shouCang_purchaseViewHolder.content_tv.getLineCount() == 1) {
                //如果答案有图片，就显示图片文本【】
                if (list.get(position).getAnswer_data().getImg_count() != null) {
                    if (!"0".equals(list.get(position).getAnswer_data().getImg_count().getImg_count())) {
                        shouCang_purchaseViewHolder.image_tv.setText("【图片】");
                        shouCang_purchaseViewHolder.image_tv.setVisibility(View.VISIBLE);
                    } else {
                        shouCang_purchaseViewHolder.image_tv.setVisibility(View.GONE);
                    }
                }
            } else if (shouCang_purchaseViewHolder.content_tv.getLineCount() == 2) {
                shouCang_purchaseViewHolder.image_tv.setVisibility(View.GONE);
            }

            ((ShouCang_PurchaseViewHolder) view_holder).view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });
            ((ShouCang_PurchaseViewHolder) view_holder).view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });

            shouCang_purchaseViewHolder.select_head_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", list.get(position).getUser_data().getId() + "");
                    context.startActivity(intent);
                }
            });

            //去往答案详情页
            shouCang_purchaseViewHolder.content_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getAnswer_data().getIs_existence())) {
                        Intent intent = new Intent(context, AnserActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("answer_id", list.get(position).getAnswer_id() + "");
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "答案已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //去往答案详情页
            shouCang_purchaseViewHolder.image_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getAnswer_data().getIs_existence())) {
                        Intent intent = new Intent(context, AnserActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("answer_id", list.get(position).getAnswer_id() + "");
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "答案已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //去往答案详情页
            shouCang_purchaseViewHolder.tv_shoucang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getAnswer_data().getIs_existence())) {
                        Intent intent = new Intent(context, AnserActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("answer_id", list.get(position).getAnswer_id() + "");
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "答案已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //给视图添加长按事件
            shouCang_purchaseViewHolder.image_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            shouCang_purchaseViewHolder.content_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });


            shouCang_purchaseViewHolder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            shouCang_purchaseViewHolder.time_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            shouCang_purchaseViewHolder.user_name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            shouCang_purchaseViewHolder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });
        } else if (getAdapterItemViewType(position) == TUWEN_PING_ZAN_ITEM) {//圖文赞了评论
            Huifu_PurchaseViewHolder huifu_holder = (Huifu_PurchaseViewHolder) view_holder;
            if (position == 0) {
                huifu_holder.title.setVisibility(View.VISIBLE);
            } else {
                huifu_holder.title.setVisibility(View.GONE);
            }

            Glide.with(context).load(list.get(position).getUser_data().getAvatar())
                    .error(R.drawable.touxiang)
                    .into(huifu_holder.select_head_image);
            huifu_holder.state_tv.setText("赞了评论");
            huifu_holder.user_name.setText(list.get(position).getUser_data().getName());
            huifu_holder.time_tv.setText(DateUtils.getTimeFormatText(transForDate(Long.valueOf(list.get(position).getCreated_at()))));

            if (list.get(position).getImpriting_comment_data() != null) {
                huifu_holder.content_tv.setText(list.get(position).getImpriting_comment_data().getContent());
                huifu_holder.image_tv.setText("【图片】");
                huifu_holder.image_tv.setVisibility(View.VISIBLE);
            }
            //去往评论详情页
            huifu_holder.content_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getImpriting_comment_data().getIs_existence())) {
                        Intent intent = new Intent(context, CommtentReplyActivity.class);
                        intent.putExtra("id", list.get(position).getImpriting_data().getId());
                        intent.putExtra("comment_id", list.get(position).getImpriting_comment_data().getTable_id());
                        intent.putExtra("total_id", list.get(position).getImpriting_comment_data().getTable_id());
                        intent.putExtra("type", "1");
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            huifu_holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });
            huifu_holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });

            huifu_holder.select_head_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", list.get(position).getUser_data().getId() + "");
                    context.startActivity(intent);
                }
            });

            huifu_holder.state_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getImpriting_comment_data().getIs_existence())) {
                        Intent intent = new Intent(context, CommtentReplyActivity.class);
                        intent.putExtra("id", list.get(position).getImpriting_data().getId());
                        intent.putExtra("comment_id", list.get(position).getImpriting_comment_data().getTable_id());
                        intent.putExtra("total_id", list.get(position).getImpriting_comment_data().getTable_id());
                        intent.putExtra("type", "1");
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            huifu_holder.image_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getImpriting_comment_data().getIs_existence())) {
                        Intent intent = new Intent(context, CommtentReplyActivity.class);
                        intent.putExtra("id", list.get(position).getImpriting_data().getId());
                        intent.putExtra("comment_id", list.get(position).getImpriting_comment_data().getTable_id());
                        intent.putExtra("total_id", list.get(position).getImpriting_comment_data().getTable_id());
                        intent.putExtra("type", "1");
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //给视图添加长按事件
            huifu_holder.image_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.content_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });

            huifu_holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.time_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.user_name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });
        } else if (getAdapterItemViewType(position) == TUWEN_ZAN_HUI_ITEM) {//图文赞了回复
            Huifu_PurchaseViewHolder huifu_holder = (Huifu_PurchaseViewHolder) view_holder;
            if (position == 0) {
                huifu_holder.title.setVisibility(View.VISIBLE);
            } else {
                huifu_holder.title.setVisibility(View.GONE);
            }

            Glide.with(context).load(list.get(position).getUser_data().getAvatar())
                    .error(R.drawable.touxiang)
                    .into(huifu_holder.select_head_image);
            huifu_holder.state_tv.setText("赞了回复");
            huifu_holder.user_name.setText(list.get(position).getUser_data().getName());
            huifu_holder.time_tv.setText(DateUtils.getTimeFormatText(transForDate(Long.valueOf(list.get(position).getCreated_at()))));

            if (list.get(position).getImpriting_data() != null) {
                huifu_holder.content_tv.setText(list.get(position).getImpriting_comment_data().getContent());
                huifu_holder.image_tv.setText("【图片】");
                huifu_holder.image_tv.setVisibility(View.VISIBLE);
            }
            //去往评论详情页
            huifu_holder.content_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getImpriting_comment_data().getIs_existence())) {
                        Intent intent = new Intent(context, CommtentReplyActivity.class);
                        intent.putExtra("id", list.get(position).getImpriting_data().getId());
                        intent.putExtra("comment_id", list.get(position).getImpriting_comment_data().getTable_id());
                        intent.putExtra("total_id", list.get(position).getImpriting_comment_data().getTable_id());
                        intent.putExtra("type", "1");
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            huifu_holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });
            huifu_holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });

            huifu_holder.select_head_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", list.get(position).getUser_data().getId() + "");
                    context.startActivity(intent);
                }
            });

            huifu_holder.state_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getImpriting_comment_data().getIs_existence())) {
                        Intent intent = new Intent(context, CommtentReplyActivity.class);
                        intent.putExtra("id", list.get(position).getImpriting_data().getId());
                        intent.putExtra("comment_id", list.get(position).getImpriting_comment_data().getTable_id());
                        intent.putExtra("total_id", list.get(position).getImpriting_comment_data().getTable_id());
                        intent.putExtra("type", "1");
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            huifu_holder.image_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getImpriting_comment_data().getIs_existence())) {
                        Intent intent = new Intent(context, CommtentReplyActivity.class);
                        intent.putExtra("id", list.get(position).getImpriting_data().getId());
                        intent.putExtra("comment_id", list.get(position).getImpriting_comment_data().getTable_id());
                        intent.putExtra("total_id", list.get(position).getImpriting_comment_data().getTable_id());
                        intent.putExtra("type", "1");
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //给视图添加长按事件
            huifu_holder.image_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.content_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });

            huifu_holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.time_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.user_name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });
        } else if (getAdapterItemViewType(position) == TUWEN_ZAN_ITEM) {//赞了图文
            Huifu_PurchaseViewHolder huifu_holder = (Huifu_PurchaseViewHolder) view_holder;
            if (position == 0) {
                huifu_holder.title.setVisibility(View.VISIBLE);
            } else {
                huifu_holder.title.setVisibility(View.GONE);
            }

            Glide.with(context).load(list.get(position).getUser_data().getAvatar())
                    .error(R.drawable.touxiang)
                    .into(huifu_holder.select_head_image);
            huifu_holder.state_tv.setText("赞了图文");
            huifu_holder.user_name.setText(list.get(position).getUser_data().getName());
            huifu_holder.time_tv.setText(DateUtils.getTimeFormatText(transForDate(Long.valueOf(list.get(position).getCreated_at()))));

            if (list.get(position).getImpriting_data() != null) {
                huifu_holder.content_tv.setText(list.get(position).getImpriting_data().getContent());
                huifu_holder.image_tv.setText("【图片】");
                huifu_holder.image_tv.setVisibility(View.VISIBLE);
            }
            //去往图文详情页
            huifu_holder.content_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getImpriting_data().getIs_existence())) {
                        Intent intent = new Intent(context, ImageTextDetailActivity.class);
                        intent.putExtra("id", list.get(position).getImpriting_data().getId());
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            huifu_holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });
            huifu_holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });

            huifu_holder.select_head_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", list.get(position).getUser_data().getId() + "");
                    context.startActivity(intent);
                }
            });

            huifu_holder.state_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getImpriting_data().getIs_existence())) {
                        Intent intent = new Intent(context, ImageTextDetailActivity.class);
                        intent.putExtra("id", list.get(position).getImpriting_data().getId());
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            huifu_holder.image_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getImpriting_data().getIs_existence())) {
                        Intent intent = new Intent(context, ImageTextDetailActivity.class);
                        intent.putExtra("id", list.get(position).getImpriting_data().getId());
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //给视图添加长按事件
            huifu_holder.image_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.content_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });

            huifu_holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.time_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.user_name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });
        } else if (getAdapterItemViewType(position) == TUWEN_CANG_ITEM) {//收藏了图文
            Huifu_PurchaseViewHolder huifu_holder = (Huifu_PurchaseViewHolder) view_holder;
            if (position == 0) {
                huifu_holder.title.setVisibility(View.VISIBLE);
            } else {
                huifu_holder.title.setVisibility(View.GONE);
            }

            Glide.with(context).load(list.get(position).getUser_data().getAvatar())
                    .error(R.drawable.touxiang)
                    .into(huifu_holder.select_head_image);
            huifu_holder.state_tv.setText("收藏了图文");
            huifu_holder.user_name.setText(list.get(position).getUser_data().getName());
            huifu_holder.time_tv.setText(DateUtils.getTimeFormatText(transForDate(Long.valueOf(list.get(position).getCreated_at()))));

            if (list.get(position).getImpriting_data() != null) {
                huifu_holder.content_tv.setText(list.get(position).getImpriting_data().getContent());
                huifu_holder.image_tv.setText("【图片】");
                huifu_holder.image_tv.setVisibility(View.VISIBLE);
                //去往评论详情页
                huifu_holder.content_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("1".equals(list.get(position).getImpriting_data().getIs_existence())) {
                            Intent intent = new Intent(context, ImageTextDetailActivity.class);
                            intent.putExtra("id", list.get(position).getImpriting_data().getId());
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            huifu_holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });
            huifu_holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });

            huifu_holder.select_head_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", list.get(position).getUser_data().getId() + "");
                    context.startActivity(intent);
                }
            });

            huifu_holder.state_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getImpriting_data().getIs_existence())) {
                        Intent intent = new Intent(context, ImageTextDetailActivity.class);
                        intent.putExtra("id", list.get(position).getImpriting_data().getId());
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            huifu_holder.image_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getImpriting_data().getIs_existence())) {
                        Intent intent = new Intent(context, ImageTextDetailActivity.class);
                        intent.putExtra("id", list.get(position).getImpriting_data().getId());
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //给视图添加长按事件
            huifu_holder.image_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.content_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });

            huifu_holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.time_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.user_name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });
        } else if (getAdapterItemViewType(position) == SHIPIN_ZAN_ITEM) {//赞了视频
            Huifu_PurchaseViewHolder huifu_holder = (Huifu_PurchaseViewHolder) view_holder;
            if (position == 0) {
                huifu_holder.title.setVisibility(View.VISIBLE);
            } else {
                huifu_holder.title.setVisibility(View.GONE);
            }

            Glide.with(context).load(list.get(position).getUser_data().getAvatar())
                    .error(R.drawable.touxiang)
                    .into(huifu_holder.select_head_image);
            huifu_holder.state_tv.setText("赞了视频");
            huifu_holder.user_name.setText(list.get(position).getUser_data().getName());
            huifu_holder.time_tv.setText(DateUtils.getTimeFormatText(transForDate(Long.valueOf(list.get(position).getCreated_at()))));

            if (list.get(position).getImpriting_data() != null) {
                huifu_holder.content_tv.setText(list.get(position).getImpriting_data().getContent());
                huifu_holder.image_tv.setText("【视频】");
                huifu_holder.image_tv.setVisibility(View.VISIBLE);
                //去往视频详情页
                huifu_holder.content_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("1".equals(list.get(position).getImpriting_data().getIs_existence())) {
                            Intent intent = new Intent(context, VideoDetailsActivity.class);
                            intent.putExtra("id", list.get(position).getImpriting_data().getId());
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            huifu_holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });
            huifu_holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });

            huifu_holder.select_head_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", list.get(position).getUser_data().getId() + "");
                    context.startActivity(intent);
                }
            });

            huifu_holder.state_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getImpriting_data().getIs_existence())) {
                        Intent intent = new Intent(context, VideoDetailsActivity.class);
                        intent.putExtra("id", list.get(position).getImpriting_data().getId());
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            huifu_holder.image_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getImpriting_data().getIs_existence())) {
                        Intent intent = new Intent(context, VideoDetailsActivity.class);
                        intent.putExtra("id", list.get(position).getImpriting_data().getId());
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //给视图添加长按事件
            huifu_holder.image_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });
            huifu_holder.content_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });

            huifu_holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });
            huifu_holder.time_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.user_name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });
        } else if (getAdapterItemViewType(position) == SHIPIN_CANG_ITEM) {//收藏视频
            Huifu_PurchaseViewHolder huifu_holder = (Huifu_PurchaseViewHolder) view_holder;
            if (position == 0) {
                huifu_holder.title.setVisibility(View.VISIBLE);
            } else {
                huifu_holder.title.setVisibility(View.GONE);
            }

            Glide.with(context).load(list.get(position).getUser_data().getAvatar())
                    .error(R.drawable.touxiang)
                    .into(huifu_holder.select_head_image);
            huifu_holder.state_tv.setText("收藏了视频");
            huifu_holder.user_name.setText(list.get(position).getUser_data().getName());
            huifu_holder.time_tv.setText(DateUtils.getTimeFormatText(transForDate(Long.valueOf(list.get(position).getCreated_at()))));

            if (list.get(position).getImpriting_data() != null) {
                huifu_holder.content_tv.setText(list.get(position).getImpriting_data().getContent());
                huifu_holder.image_tv.setText("【视频】");
                huifu_holder.image_tv.setVisibility(View.VISIBLE);
                //去往评论详情页
                huifu_holder.content_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("1".equals(list.get(position).getImpriting_data().getIs_existence())) {
                            Intent intent = new Intent(context, VideoDetailsActivity.class);
                            intent.putExtra("id", list.get(position).getImpriting_data().getId());
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            huifu_holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });
            huifu_holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });

            huifu_holder.select_head_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", list.get(position).getUser_data().getId() + "");
                    context.startActivity(intent);
                }
            });

            huifu_holder.state_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getImpriting_data().getIs_existence())) {
                        Intent intent = new Intent(context, VideoDetailsActivity.class);
                        intent.putExtra("id", list.get(position).getImpriting_data().getId());
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            huifu_holder.image_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getImpriting_data().getIs_existence())) {
                        Intent intent = new Intent(context, VideoDetailsActivity.class);
                        intent.putExtra("id", list.get(position).getImpriting_data().getId());
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //给视图添加长按事件
            huifu_holder.image_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.content_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });

            huifu_holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.time_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.user_name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });
        } else if (getAdapterItemViewType(position) == SHIPIN_PING_ZAN_ITEM) {//评论视频
            Huifu_PurchaseViewHolder huifu_holder = (Huifu_PurchaseViewHolder) view_holder;
            if (position == 0) {
                huifu_holder.title.setVisibility(View.VISIBLE);
            } else {
                huifu_holder.title.setVisibility(View.GONE);
            }

            Glide.with(context).load(list.get(position).getUser_data().getAvatar())
                    .error(R.drawable.touxiang)
                    .into(huifu_holder.select_head_image);
            huifu_holder.state_tv.setText("赞了评论");
            huifu_holder.user_name.setText(list.get(position).getUser_data().getName());
            huifu_holder.time_tv.setText(DateUtils.getTimeFormatText(transForDate(Long.valueOf(list.get(position).getCreated_at()))));

            if (list.get(position).getImpriting_comment_data() != null) {
                huifu_holder.content_tv.setText(list.get(position).getImpriting_comment_data().getContent());
                huifu_holder.image_tv.setText("【视频】");
                huifu_holder.image_tv.setVisibility(View.VISIBLE);
                //去往评论详情页
                huifu_holder.content_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("1".equals(list.get(position).getImpriting_comment_data().getIs_existence())) {
                            Intent intent = new Intent(context, CommentVideoReplyActivity.class);
                            intent.putExtra("id", list.get(position).getImpriting_data().getId());
                            intent.putExtra("comment_id", list.get(position).getImpriting_comment_data().getTable_id());
                            intent.putExtra("total_id", list.get(position).getImpriting_comment_data().getTable_id());
                            intent.putExtra("type", "2");
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            huifu_holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });
            huifu_holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });

            huifu_holder.select_head_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", list.get(position).getUser_data().getId() + "");
                    context.startActivity(intent);
                }
            });

            huifu_holder.state_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getImpriting_comment_data().getIs_existence())) {
                        Intent intent = new Intent(context, CommentVideoReplyActivity.class);
                        intent.putExtra("id", list.get(position).getImpriting_data().getId());
                        intent.putExtra("comment_id", list.get(position).getImpriting_comment_data().getTable_id());
                        intent.putExtra("total_id", list.get(position).getImpriting_comment_data().getTable_id());
                        intent.putExtra("type", "2");
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            huifu_holder.image_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getImpriting_comment_data().getIs_existence())) {
                        Intent intent = new Intent(context, CommentVideoReplyActivity.class);
                        intent.putExtra("id", list.get(position).getImpriting_data().getId());
                        intent.putExtra("comment_id", list.get(position).getImpriting_comment_data().getTable_id());
                        intent.putExtra("total_id", list.get(position).getImpriting_comment_data().getTable_id());
                        intent.putExtra("type", "2");
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //给视图添加长按事件
            huifu_holder.image_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.content_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });

            huifu_holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.time_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.user_name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });
        } else if (getAdapterItemViewType(position) == SHIPIN_ZAN_HUI_ITEM) {//赞了视频评论回复
            Huifu_PurchaseViewHolder huifu_holder = (Huifu_PurchaseViewHolder) view_holder;
            if (position == 0) {
                huifu_holder.title.setVisibility(View.VISIBLE);
            } else {
                huifu_holder.title.setVisibility(View.GONE);
            }

            Glide.with(context).load(list.get(position).getUser_data().getAvatar())
                    .error(R.drawable.touxiang)
                    .into(huifu_holder.select_head_image);
            huifu_holder.state_tv.setText("赞了回复");
            huifu_holder.user_name.setText(list.get(position).getUser_data().getName());
            huifu_holder.time_tv.setText(DateUtils.getTimeFormatText(transForDate(Long.valueOf(list.get(position).getCreated_at()))));

            if (list.get(position).getImpriting_comment_data() != null) {
                huifu_holder.content_tv.setText(list.get(position).getImpriting_comment_data().getContent());
                huifu_holder.image_tv.setText("【视频】");
                huifu_holder.image_tv.setVisibility(View.VISIBLE);
                //去往评论详情页
                huifu_holder.content_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("1".equals(list.get(position).getImpriting_comment_data().getIs_existence())) {
                            Intent intent = new Intent(context, CommentVideoReplyActivity.class);
                            intent.putExtra("id", list.get(position).getImpriting_data().getId());
                            intent.putExtra("comment_id", list.get(position).getImpriting_comment_data().getTable_id());
                            intent.putExtra("total_id", list.get(position).getImpriting_comment_data().getTable_id());
                            intent.putExtra("type", "2");
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            huifu_holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                }
            });
            huifu_holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });

            huifu_holder.select_head_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FriendPageActivity.class);
                    intent.putExtra("UserId", list.get(position).getUser_data().getId() + "");
                    context.startActivity(intent);
                }
            });

            huifu_holder.state_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getImpriting_comment_data().getIs_existence())) {
                        Intent intent = new Intent(context, CommentVideoReplyActivity.class);
                        intent.putExtra("id", list.get(position).getImpriting_data().getId());
                        intent.putExtra("comment_id", list.get(position).getImpriting_comment_data().getTable_id());
                        intent.putExtra("total_id", list.get(position).getImpriting_comment_data().getTable_id());
                        intent.putExtra("type", "2");
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            huifu_holder.image_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(list.get(position).getImpriting_comment_data().getIs_existence())) {
                        Intent intent = new Intent(context, CommentVideoReplyActivity.class);
                        intent.putExtra("id", list.get(position).getImpriting_data().getId());
                        intent.putExtra("comment_id", list.get(position).getImpriting_comment_data().getTable_id());
                        intent.putExtra("total_id", list.get(position).getImpriting_comment_data().getTable_id());
                        intent.putExtra("type", "2");
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "评论已经删除无法查看", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //给视图添加长按事件
            huifu_holder.image_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.content_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });

            huifu_holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.time_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.user_name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }

            });
            huifu_holder.select_head_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });
        }
    }

    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
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


    @Override
    public int getAdapterItemViewType(int position) {
        if (list.size() == 0) {
            return EMPTY_VIEW;
        } else {
            if (list.get(position).getType() == 1) {//显示点赞
                return TYPE_ITEM;
            } else if (list.get(position).getType() == 3) {//显示收藏
                return PINGLUN_ITEM;
            } else if (list.get(position).getType() == 2) {
                return SHOUCANG_ITEM;
            } else if (list.get(position).getType() == 5) {
                return TUWEN_PING_ZAN_ITEM;
            } else if (list.get(position).getType() == 6) {
                return TUWEN_ZAN_HUI_ITEM;
            } else if (list.get(position).getType() == 7) {
                return TUWEN_ZAN_ITEM;
            } else if (list.get(position).getType() == 8) {
                return TUWEN_CANG_ITEM;
            } else if (list.get(position).getType() == 9) {
                return SHIPIN_ZAN_ITEM;
            } else if (list.get(position).getType() == 10) {
                return SHIPIN_CANG_ITEM;
            } else if (list.get(position).getType() == 11) {
                return SHIPIN_PING_ZAN_ITEM;
            } else if (list.get(position).getType() == 12) {
                return SHIPIN_ZAN_HUI_ITEM;
            } else {
                return HUIFU_ITEM;
            }
        }

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

    public void setData(List<DianZanEntity.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_top;
        public View view;

        public EmptyViewHolder(View item_view, boolean isItem) {
            super(item_view);
            if (isItem) {
                this.view = item_view;
                img_top = (ImageView) item_view
                        .findViewById(R.id.img_top);
            }
        }
    }


    public class ShouCang_PurchaseViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView user_name, time_tv, image_tv, content_tv;
        public CircleImageView select_head_image;
        private TextView title;
        private TextView tv_shoucang;

        public ShouCang_PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                user_name = (TextView) itemView.findViewById(R.id.user_name);
                select_head_image = (CircleImageView) itemView.findViewById(R.id.select_head_image);
                time_tv = (TextView) itemView.findViewById(R.id.time_tv);
                image_tv = (TextView) itemView.findViewById(R.id.image_tv);
                content_tv = (TextView) itemView.findViewById(R.id.content_tv);
                title = itemView.findViewById(R.id.title);
                tv_shoucang = itemView.findViewById(R.id.tv_shoucang);
            }
        }
    }


    public class PurchaseViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView user_name, time_tv, image_tv, content_tv;
        public CircleImageView select_head_image;
        private TextView title;
        private TextView state_tv;

        public PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                user_name = (TextView) itemView.findViewById(R.id.user_name);
                select_head_image = (CircleImageView) itemView.findViewById(R.id.select_head_image);
                time_tv = (TextView) itemView.findViewById(R.id.time_tv);
                image_tv = (TextView) itemView.findViewById(R.id.image_tv);
                content_tv = (TextView) itemView.findViewById(R.id.content_tv);
                title = itemView.findViewById(R.id.title);
                state_tv = itemView.findViewById(R.id.state_tv);
            }
        }
    }

    public class PingLun_PurchaseViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView user_name, time_tv, image_tv, content_tv;
        public CircleImageView select_head_image;
        private TextView title;
        private TextView state_tv;

        public PingLun_PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                user_name = (TextView) itemView.findViewById(R.id.user_name);
                select_head_image = (CircleImageView) itemView.findViewById(R.id.select_head_image);
                time_tv = (TextView) itemView.findViewById(R.id.time_tv);
                image_tv = (TextView) itemView.findViewById(R.id.image_tv);
                content_tv = (TextView) itemView.findViewById(R.id.content_tv);
                title = itemView.findViewById(R.id.title);
                state_tv = itemView.findViewById(R.id.state_tv);
            }
        }
    }

    public class Huifu_PurchaseViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView user_name, time_tv, image_tv, content_tv;
        public CircleImageView select_head_image;
        private TextView title;
        private TextView state_tv;

        public Huifu_PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                user_name = (TextView) itemView.findViewById(R.id.user_name);
                select_head_image = (CircleImageView) itemView.findViewById(R.id.select_head_image);
                time_tv = (TextView) itemView.findViewById(R.id.time_tv);
                image_tv = (TextView) itemView.findViewById(R.id.image_tv);
                content_tv = (TextView) itemView.findViewById(R.id.content_tv);
                title = itemView.findViewById(R.id.title);
                state_tv = itemView.findViewById(R.id.state_tv);
            }
        }
    }
}

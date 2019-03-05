package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.QuestionAnswerDetailActivity;
import com.app.cookbook.xinhe.foodfamily.main.db.NoteDao;
import com.app.cookbook.xinhe.foodfamily.main.db.entity.Note;
import com.app.cookbook.xinhe.foodfamily.main.entity.DraftAnswerEntity;
import com.app.cookbook.xinhe.foodfamily.util.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rx.Subscription;

/**
 * Created by shiyujia02 on 2018/3/22.
 */

public class DraftAnswerAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<DraftAnswerEntity.DraftAnswer> list;
    private Context context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private NoteDao noteDao;
    public static int select_point = -1;

    public DraftAnswerAdapter(List<DraftAnswerEntity.DraftAnswer> mlist, Context mcontext) {
        noteDao = new NoteDao(mcontext);

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
        View empty_view = LayoutInflater.from(context).inflate(R.layout.answer_empty_layout2, parent, false);
        View shidan_view = LayoutInflater.from(context).inflate(R.layout.draft_answer_item, parent, false);
        RecyclerView.ViewHolder holder = null;
        if (viewType == EMPTY_VIEW) {
            holder = new EmptyViewHolder(empty_view, true);
        } else {
            holder = new PurchaseViewHolder(shidan_view, true);
        }
        return holder;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }


    private OnItemLongClickListener mOnItemLongClickListener;

    public void setmOnItemLongClickListener(OnItemLongClickListener mOnItemClickListener) {
        this.mOnItemLongClickListener = mOnItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, final int position, boolean isItem) {
        if (view_holder instanceof PurchaseViewHolder) {
            PurchaseViewHolder holder = (PurchaseViewHolder) view_holder;

            if (position == 0) {
                holder.line.setVisibility(View.VISIBLE);
            } else {
                holder.line.setVisibility(View.GONE);
            }

            ((PurchaseViewHolder) view_holder).view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemLongClickListener != null) {
                        mOnItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });
            ((PurchaseViewHolder) view_holder).view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
            if (select_point == position) {
                holder.draft_item_layout.setBackgroundColor(Color.parseColor("#F7F7F7"));
            } else {
                holder.draft_item_layout.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            holder.question_title.setText(list.get(position).getTitle());

            holder.time_tv.setText(DateUtils.getTimeFormatText(transForDate(Long.valueOf(list.get(position).getUpdated_at()))));

            holder.question_answer.setText(list.get(position).getContent_remove());

            holder.bianji_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    go_to_edit(String.valueOf(list.get(position).getId()), list.get(position).getContent_remove(),
                            list.get(position).getContent(), list.get(position).getTitle());
                }
            });

            if (null != list.get(position).getImg_data()) {
                if (list.get(position).getImg_data().size() > 0) {
                    holder.image_tv.setVisibility(View.VISIBLE);
                } else {
                    holder.image_tv.setVisibility(View.GONE);
                }
            } else {
                holder.image_tv.setVisibility(View.GONE);
            }


            holder.question_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
            holder.time_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });

            holder.question_answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
            holder.bianji_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
            holder.bianji_btn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemLongClickListener != null) {
                        mOnItemLongClickListener.onItemLongClick(v, position);
                        select_point = position;
                        notifyDataSetChanged();
                    }
                    return false;
                }
            });
            holder.question_answer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemLongClickListener != null) {
                        mOnItemLongClickListener.onItemLongClick(v, position);
                        select_point = position;
                        notifyDataSetChanged();
                    }
                    return false;
                }
            });
            holder.draft_item_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemLongClickListener != null) {
                        mOnItemLongClickListener.onItemLongClick(v, position);
                        select_point = position;
                        notifyDataSetChanged();
                    }
                    return false;
                }
            });
            holder.time_tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemLongClickListener != null) {
                        mOnItemLongClickListener.onItemLongClick(v, position);
                        select_point = position;
                        notifyDataSetChanged();
                    }
                    return false;
                }
            });
            holder.question_title.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemLongClickListener != null) {
                        mOnItemLongClickListener.onItemLongClick(v, position);
                        select_point = position;
                        notifyDataSetChanged();
                    }

                    return false;
                }
            });
        }
    }

    /**
     * 去往编辑的页面
     */
    private void go_to_edit(String answer_id, String content_remove, String mcontent, String question_title) {
        Note note = new Note();
        saveNoteData(note, answer_id, content_remove, mcontent);
        Intent intent = new Intent(context, QuestionAnswerDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("flag", "3");
        bundle.putString("answer_id", answer_id);
        bundle.putString("question_title", question_title);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 保存数据,=0销毁当前界面，=1不销毁界面，为了防止在后台时保存笔记并销毁，应该只保存笔记
     */
    private void saveNoteData(Note note, String answer_id, String content, String mcontent) {
        //以问题的ID作为分类
        note.setTitle("回答");
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


    public void setData(List<DraftAnswerEntity.DraftAnswer> list) {
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

    public class PurchaseViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView question_title, time_tv, bianji_btn, question_answer, image_tv;
        public RelativeLayout draft_item_layout;
        public View line;

        public PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                question_title = view.findViewById(R.id.question_title);
                time_tv = view.findViewById(R.id.time_tv);
                bianji_btn = view.findViewById(R.id.bianji_btn);
                question_answer = view.findViewById(R.id.question_answer);
                image_tv = view.findViewById(R.id.image_tv);
                draft_item_layout = view.findViewById(R.id.draft_item_layout);
                line = view.findViewById(R.id.line);
            }
        }
    }
}

package com.app.cookbook.xinhe.foodfamily.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.main.AddQuestionActivity;
import com.app.cookbook.xinhe.foodfamily.main.db.NoteDao;
import com.app.cookbook.xinhe.foodfamily.main.db.entity.Note;
import com.app.cookbook.xinhe.foodfamily.main.entity.DraftQuestionEntity;
import com.app.cookbook.xinhe.foodfamily.util.DateUtils;
import com.app.cookbook.xinhe.foodfamily.util.SharedPreferencesHelper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import rx.Subscription;

/**
 * Created by shiyujia02 on 2018/3/22.
 */

public class DraftQuestionAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {
    private List<DraftQuestionEntity.DraftQuestionEn> list;
    private Context context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_ITEM = 1;
    private NoteDao noteDao;
    public static int select_point = -1;

    public DraftQuestionAdapter(List<DraftQuestionEntity.DraftQuestionEn> mlist, Context mcontext) {
        list = mlist;
        context = mcontext;
        noteDao = new NoteDao(mcontext);

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
        View empty_view = LayoutInflater.from(context).inflate(R.layout.search_empty_layout2, parent, false);
        View shidan_view = LayoutInflater.from(context).inflate(R.layout.draft_question_item, parent, false);
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
    public void onBindViewHolder(final RecyclerView.ViewHolder view_holder, final int position, boolean isItem) {
        if (view_holder instanceof PurchaseViewHolder) {
            final PurchaseViewHolder holder = (PurchaseViewHolder) view_holder;

            if (position == 0) {
                holder.line.setVisibility(View.VISIBLE);
            } else {
                holder.line.setVisibility(View.GONE);
            }

            view_holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemLongClickListener != null) {
                        mOnItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });

            if (select_point == position) {
                holder.draft_item_layout.setBackgroundColor(Color.parseColor("#F7F7F7"));
            } else {
                holder.draft_item_layout.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            view_holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });


            holder.question_title.setText(list.get(position).getTitle());

            holder.time_tv.setText(DateUtils.getTimeFormatText(transForDate(Long.valueOf(list.get(position).getUpdated_at()))));

            holder.bianji_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    go_to_edit(String.valueOf(list.get(position).getId()), list.get(position).getTitle(), list.get(position).getContent_remove(), list.get(position).getContent());

                    if (!TextUtils.isEmpty(list.get(position).getClassification_id())) {
                        String str2 = list.get(position).getClassification_id().replace(" ", "");//去掉所用空格
                        List<String> list = Arrays.asList(str2.split(","));
                        List arrList = new ArrayList(list);
                        try {
                            str2 = SharedPreferencesHelper.SceneList2String(arrList);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        SharedPreferencesHelper.put(context, "selectStrName", str2);
                        SharedPreferencesHelper.put(context, "selectStr", str2);
                    }
                }
            });
            holder.question_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    go_to_edit(String.valueOf(list.get(position).getId()), list.get(position).getTitle(), list.get(position).getContent_remove(), list.get(position).getContent());

                    if (!TextUtils.isEmpty(list.get(position).getClassification_id())) {
                        String str2 = list.get(position).getClassification_id().replace(" ", "");//去掉所用空格
                        List<String> list = Arrays.asList(str2.split(","));
                        List arrList = new ArrayList(list);
                        try {
                            str2 = SharedPreferencesHelper.SceneList2String(arrList);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        SharedPreferencesHelper.put(context, "selectStrName", str2);
                        SharedPreferencesHelper.put(context, "selectStr", str2);
                    }
                }
            });

            holder.time_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    go_to_edit(String.valueOf(list.get(position).getId()), list.get(position).getTitle(), list.get(position).getContent_remove(), list.get(position).getContent());

                    if (!TextUtils.isEmpty(list.get(position).getClassification_id())) {
                        String str2 = list.get(position).getClassification_id().replace(" ", "");//去掉所用空格
                        List<String> list = Arrays.asList(str2.split(","));
                        List arrList = new ArrayList(list);
                        try {
                            str2 = SharedPreferencesHelper.SceneList2String(arrList);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        SharedPreferencesHelper.put(context, "selectStrName", str2);
                        SharedPreferencesHelper.put(context, "selectStr", str2);
                    }
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
            holder.bianji_btn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemLongClickListener != null) {
                        select_point = position;
                        notifyDataSetChanged();
                        mOnItemLongClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });


        }
    }

    public void go_to_edit(String question_id, String question_title, String question_content_remove, String question_contnt) {
        Note note = new Note();
        saveNoteData(note, question_title,
                question_id,
                question_content_remove,
                question_contnt);
        Intent intent = new Intent(context, AddQuestionActivity.class);
        intent.putExtra("flag", 3);//编辑笔记
        intent.putExtra("question_id", question_id);
        context.startActivity(intent);

    }

    private void saveNoteData(Note note, String question_title, String question_id, String content, String mcontent) {
        if (null != noteDao.queryNoteOne(Integer.valueOf(question_id))) {//如果之前有插入数据
            noteDao.deleteNote(Integer.valueOf(question_id));
            //以问题的ID作为分类
            note.setTitle(question_title);
            note.setContent(content);
            note.setMcontent(mcontent);
            note.setGroupId(Integer.valueOf(question_id));
            note.setGroupName(question_id);
            note.setType(2);
            note.setBgColor("#FFFFFF");
            note.setIsEncrypt(0);
            note.setCreateTime(DateUtils.date2string(new Date()));
            //新建笔记
            noteDao.insertNote(note);
        } else {//之前没有插入过数据
            note.setTitle(question_title);
            note.setContent(content);
            note.setMcontent(mcontent);
            note.setGroupId(Integer.valueOf(question_id));
            note.setGroupName(question_id);
            note.setType(2);
            note.setBgColor("#FFFFFF");
            note.setIsEncrypt(0);
            note.setCreateTime(DateUtils.date2string(new Date()));
            //新建笔记
            noteDao.insertNote(note);
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


    public void setData(List<DraftQuestionEntity.DraftQuestionEn> list) {
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
        public TextView question_title, time_tv, bianji_btn;
        public RelativeLayout draft_item_layout;
        public View line;

        public PurchaseViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                question_title = view.findViewById(R.id.question_title);
                time_tv = view.findViewById(R.id.time_tv);
                bianji_btn = view.findViewById(R.id.bianji_btn);
                draft_item_layout = view.findViewById(R.id.draft_item_layout);
                line = view.findViewById(R.id.line);
            }
        }
    }
}

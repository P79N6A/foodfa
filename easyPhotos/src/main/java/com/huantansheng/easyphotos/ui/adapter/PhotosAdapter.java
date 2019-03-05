package com.huantansheng.easyphotos.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huantansheng.easyphotos.R;
import com.huantansheng.easyphotos.constant.Type;
import com.huantansheng.easyphotos.models.ad.AdViewHolder;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.huantansheng.easyphotos.result.Result;
import com.huantansheng.easyphotos.setting.Setting;
import com.huantansheng.easyphotos.ui.widget.PressedImageView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 专辑相册适配器
 * Created by huan on 2017/10/23.
 */

public class PhotosAdapter extends RecyclerView.Adapter {
    private static final int TYPE_AD = 0;
    private static final int TYPE_ALBUM_ITEMS = 1;
    private ArrayList<Object> dataList;
    private LayoutInflater mInflater;
    private OnClickListener listener;
    private boolean unable, isSingle;
    private int singlePosition;
    private Context mContext;

    public PhotosAdapter(Context cxt, ArrayList<Object> dataList, OnClickListener listener) {
        this.mContext = cxt;
        this.dataList = dataList;
        this.listener = listener;
        this.mInflater = LayoutInflater.from(cxt);
        this.unable = Result.count() == Setting.count;
        this.isSingle = Setting.count == 1;
    }

    public void change() {
        unable = Result.count() == Setting.count;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_AD:
                return new AdViewHolder(mInflater.inflate(R.layout.item_ad_easy_photos, parent, false));
            default:
                return new PhotoViewHolder(mInflater.inflate(R.layout.item_rv_photos_easy_photos, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final int p = position;
        if (holder instanceof PhotoViewHolder) {
            final Photo item = (Photo) dataList.get(p);
            updateSelector(((PhotoViewHolder) holder).tvSelector, item.selected, item, p);
            String path = item.path;
            String type = item.type;
            if (Setting.showGif) {
                if (path.endsWith(Type.GIF) || type.endsWith(Type.GIF)) {
                    Setting.imageEngine.loadGifAsBitmap(((PhotoViewHolder) holder).ivPhoto.getContext(), path, ((PhotoViewHolder) holder).ivPhoto);
                    ((PhotoViewHolder) holder).tvGif.setVisibility(View.VISIBLE);
                } else {
                    Setting.imageEngine.loadPhoto(((PhotoViewHolder) holder).ivPhoto.getContext(), path, ((PhotoViewHolder) holder).ivPhoto);
                    ((PhotoViewHolder) holder).tvGif.setVisibility(View.GONE);
                }
            } else {
                Setting.imageEngine.loadPhoto(((PhotoViewHolder) holder).ivPhoto.getContext(), path, ((PhotoViewHolder) holder).ivPhoto);
                ((PhotoViewHolder) holder).tvGif.setVisibility(View.GONE);
            }

            ((PhotoViewHolder) holder).vSelector.setVisibility(View.VISIBLE);
            ((PhotoViewHolder) holder).tvSelector.setVisibility(View.VISIBLE);
            ((PhotoViewHolder) holder).ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int realPosition = p;
                    if (Setting.hasPhotosAd()) {
                        realPosition--;
                    }
                    listener.onPhotoClick(p, realPosition);
                }
            });


            ((PhotoViewHolder) holder).vSelector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (GetFileSize(item.path).contains("MB")) {
                        Log.e("最大选择兆数",5.00+"");
                        if (Double.parseDouble(GetFileSize(item.path).replace("MB", "")) > 10.00) {
                            Toast.makeText(mContext, "图片不能大于5M", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    if (isSingle) {
                        singleSelector(item, p);
                        return;
                    }
                    if (unable) {
                        if (item.selected) {
                            Result.removePhoto(item);
                            if (unable) {
                                unable = false;
                            }
                            listener.onSelectorChanged();
                            notifyDataSetChanged();
                            return;
                        }
                        listener.onSelectorOutOfMax();
                        return;
                    }
                    item.selected = !item.selected;
                    if (item.selected) {
                        Result.addPhoto(item);
                        ((PhotoViewHolder) holder).tvSelector.setBackgroundResource(R.drawable.bg_select_true_easy_photos);
                        ((PhotoViewHolder) holder).tvSelector.setText(String.valueOf(Result.count()));
                        if (Result.count() == Setting.count) {
                            unable = true;
                            notifyDataSetChanged();
                        }
                    } else {
                        Result.removePhoto(item);
                        if (unable) {
                            unable = false;
                        }
                        notifyDataSetChanged();
                    }
                    listener.onSelectorChanged();


                }
            });
            return;
        }

        if (holder instanceof AdViewHolder) {
            if (!Setting.photoAdIsOk) {
                ((AdViewHolder) holder).adFrame.setVisibility(View.GONE);
                return;
            }

            WeakReference weakReference = (WeakReference) dataList.get(p);

            if (null != weakReference) {
                View adView = (View) weakReference.get();
                if (null != adView) {
                    if (null != adView.getParent()) {
                        if (adView.getParent() instanceof FrameLayout) {
                            ((FrameLayout) adView.getParent()).removeAllViews();
                        }
                    }
                    ((AdViewHolder) holder).adFrame.setVisibility(View.VISIBLE);
                    ((AdViewHolder) holder).adFrame.removeAllViews();
                    ((AdViewHolder) holder).adFrame.addView(adView);
                }
            }
        }
    }
    public static String GetFileSize(String Path) {
        return GetFileSize(new File(Path));
    }


    public static String GetFileSize(File file) {
        String size = "";
        if (file.exists() && file.isFile()) {
            long fileS = file.length();
            DecimalFormat df = new DecimalFormat("#.00");
            if (fileS < 1024) {
                size = df.format((double) fileS) + "BT";
            } else if (fileS < 1048576) {
                size = df.format((double) fileS / 1024) + "KB";
            } else if (fileS < 1073741824) {
                size = df.format((double) fileS / 1048576) + "MB";
            } else {
                size = df.format((double) fileS / 1073741824) + "GB";
            }
        } else if (file.exists() && file.isDirectory()) {
            size = "";
        } else {
            size = "0BT";
        }
        return size;
    }
    private void singleSelector(Photo photo, int position) {
        if (!Result.isEmpty()) {
            if (Result.getPhotoPath(0).equals(photo.path)) {
                Result.removePhoto(photo);
                notifyItemChanged(position);
            } else {
                Result.removePhoto(0);
                Result.addPhoto(photo);
                notifyItemChanged(singlePosition);
                notifyItemChanged(position);
            }
        } else {
            Result.addPhoto(photo);
            notifyItemChanged(position);
        }
        listener.onSelectorChanged();
    }

    private void updateSelector(TextView tvSelector, boolean selected, Photo photo, int position) {
        if (selected) {
            String number = Result.getSelectorNumber(photo);
            if (number.equals("0")) {
                tvSelector.setBackgroundResource(R.drawable.bg_select_false_easy_photos);
                tvSelector.setText(null);
                return;
            }
            tvSelector.setText(number);
            tvSelector.setBackgroundResource(R.drawable.bg_select_true_easy_photos);
            if (isSingle) {
                singlePosition = position;
                tvSelector.setText("1");
            }
        } else {
            if (unable) {
                tvSelector.setBackgroundResource(R.drawable.bg_select_false_unable_easy_photos);
            } else {
                tvSelector.setBackgroundResource(R.drawable.bg_select_false_easy_photos);
            }
            tvSelector.setText(null);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == position && Setting.hasPhotosAd()) {
            return TYPE_AD;
        }
        return TYPE_ALBUM_ITEMS;
    }

    public interface OnClickListener {
        void onPhotoClick(int position, int realPosition);

        void onSelectorOutOfMax();

        void onSelectorChanged();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        PressedImageView ivPhoto;
        TextView tvSelector;
        View vSelector;
        TextView tvGif;

        PhotoViewHolder(View itemView) {
            super(itemView);
            this.ivPhoto = (PressedImageView) itemView.findViewById(R.id.iv_photo);
            this.tvSelector = (TextView) itemView.findViewById(R.id.tv_selector);
            this.vSelector = itemView.findViewById(R.id.v_selector);
            this.tvGif = (TextView) itemView.findViewById(R.id.tv_gif);
        }
    }
}

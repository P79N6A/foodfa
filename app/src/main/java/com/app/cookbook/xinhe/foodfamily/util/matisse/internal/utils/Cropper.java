package com.app.cookbook.xinhe.foodfamily.util.matisse.internal.utils;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.os.EnvironmentCompat;
import android.util.Log;

import com.app.cookbook.xinhe.foodfamily.R;
import com.app.cookbook.xinhe.foodfamily.util.matisse.UCrop;
import com.app.cookbook.xinhe.foodfamily.util.matisse.internal.entity.CaptureStrategy;
import com.app.cookbook.xinhe.foodfamily.util.matisse.internal.entity.SelectionSpec;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by song on 18-5-8.
 */

public class Cropper {
    private static final String TAG = "Cropper";
    public static final int REQUEST_CODE = UCrop.REQUEST_CROP;
    private final WeakReference<Activity> mContext;
    private final WeakReference<Fragment> mFragment;
    private Uri mCurrentPhotoUri;
    private String mCurrentPhotoPath;
    private final WeakReference<SelectionSpec> mSpec;

    public Cropper(Activity activity, SelectionSpec spec) {
        mContext = new WeakReference<>(activity);
        mFragment = null;
        mSpec = new WeakReference<SelectionSpec>(spec);
    }

    public Cropper(Activity activity, Fragment fragment, SelectionSpec spec) {
        mContext = new WeakReference<>(activity);
        mFragment = new WeakReference<>(fragment);
        mSpec = new WeakReference<SelectionSpec>(spec);
    }

    public void crop(String sourcePath) {
        try {
            SelectionSpec spec = mSpec.get();
            if (spec != null) {
                File sourceFile = new File(sourcePath);
                File destinationFile = createImageFile(spec.captureStrategy);

                mCurrentPhotoPath = destinationFile.getAbsolutePath();
                mCurrentPhotoUri = FileProvider.getUriForFile(mContext.get(), spec.captureStrategy.authority, destinationFile);

                float aspectRatioX = spec.aspectRatioX;
                float aspectRatioY = spec.aspectRatioY;
                int maxWidth = spec.maxWidth;
                int maxHeight = spec.maxHeight;

                UCrop uCrop = UCrop.of(Uri.fromFile(sourceFile), Uri.fromFile(destinationFile))
                        .withAspectRatio(aspectRatioX, aspectRatioY);
                if (maxWidth > 0 && maxHeight > 0) {
                    uCrop.withMaxResultSize(maxWidth, maxHeight);
                }
                UCrop.Options options = new UCrop.Options();
                Activity activity = mContext.get();
                options.setTheme(spec.themeId);
                options.setHideBottomControls(true);
                options.setToolbarColor(activity.getResources().getColor(R.color.preview_bottom_size));
                options.setStatusBarColor(activity.getResources().getColor(R.color.preview_bottom_size));
                uCrop.withOptions(options);

                if (activity != null) {
                    uCrop.start(activity);
                } else {
                    Fragment fragment = mFragment.get();
                    if (fragment != null) {
                        uCrop.start(fragment.getContext(), fragment);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }


    private File createImageFile(CaptureStrategy captureStrategy) throws IOException {
        // Create an image file name
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = String.format("JPEG_%s.jpg", timeStamp);
        File storageDir;
        if (captureStrategy.isPublic) {
            storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
        } else {
            storageDir = mContext.get().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }

        // Avoid joining path components manually
        File tempFile = new File(storageDir, imageFileName);

        // Handle the situation that user's external storage is not ready
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }

        return tempFile;
    }

    public Uri getCurrentPhotoUri() {
        return mCurrentPhotoUri;
    }

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

}

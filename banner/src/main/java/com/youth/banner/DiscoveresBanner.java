package com.youth.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoaderInterface;
import com.youth.banner.view.BannerViewPager;
import com.youth.banner.view.CircleImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.ViewPager.OnPageChangeListener;
import static android.support.v4.view.ViewPager.PageTransformer;

public class DiscoveresBanner extends FrameLayout implements OnPageChangeListener {
    public String tag = "banner";
    private int mIndicatorMargin = 10;
    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private int mIndicatorWidth2;
    private int mIndicatorHeight2;
    private int indicatorSize;
    private int indicatorSize2;
    private int bannerBackgroundImage;
    private int bannerStyle = BannerConfig.CIRCLE_INDICATOR;
    private int delayTime = BannerConfig.TIME;
    private int scrollTime = BannerConfig.DURATION;
    private boolean isAutoPlay = BannerConfig.IS_AUTO_PLAY;
    private boolean isScroll = BannerConfig.IS_SCROLL;
    private int mIndicatorSelectedResId = R.drawable.gray_radius;
    private int mIndicatorUnselectedResId = R.drawable.white_radius;
    private int mLayoutResId = R.layout.discover_banner;
    private int titleHeight;
    private int titleBackground;
    private int titleTextColor;
    private int titleTextSize;
    private int count = 0;
    private int currentItem;
    private int gravity = -1;
    private int lastPosition = 1;
    private int scaleType = 1;
    private List<String> titles;
    private List<String> typeList;
    private List<String> userHandList;
    private List imageUrls;
    private String sig;
    private List<View> imageViews;
    private List<ImageView> indicatorImages;
    private Context context;
    private BannerViewPager viewPager;
    private TextView bannerTitle, numIndicatorInside, numIndicator;
    //用户头像样式1
    private CircleImageView userHand;
    //用户名样式1
    private TextView userName;
    //用户头像样式2
    private CircleImageView user_hand;
    //用户名样式2
    private TextView user_name;
    //标题样式
    private TextView bannerTitleTwo;
    //类型图标
    private ImageView typeImage;
    private LinearLayout user_layout;
    private LinearLayout user_layout2;
    private LinearLayout indicator, indicatorInside;
    private RelativeLayout titleView;
    private ImageView bannerDefaultImage;
    private ImageLoaderInterface imageLoader;
    private BannerPagerAdapter adapter;
    private OnPageChangeListener mOnPageChangeListener;
    private BannerScroller mScroller;
    private OnBannerClickListener bannerListener;
    private OnBannerListener listener;
    private DisplayMetrics dm;

    private WeakHandler handler = new WeakHandler();

    public DiscoveresBanner(Context context) {
        this(context, null);
    }

    public DiscoveresBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiscoveresBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        titles = new ArrayList<>();
        typeList = new ArrayList<>();
        userHandList = new ArrayList<>();
        imageUrls = new ArrayList<>();
        imageViews = new ArrayList<>();
        indicatorImages = new ArrayList<>();
        dm = context.getResources().getDisplayMetrics();
        indicatorSize = dm.widthPixels / 140;
        indicatorSize2 = dm.widthPixels / 80;
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        imageViews.clear();
        handleTypedArray(context, attrs);
        View view = LayoutInflater.from(context).inflate(mLayoutResId, this, true);

        bannerDefaultImage = (ImageView) view.findViewById(R.id.bannerDefaultImage);
        bannerDefaultImage.setFocusable(false);

        viewPager = (BannerViewPager) view.findViewById(R.id.bannerViewPager);
        viewPager.setFocusable(false);

        titleView = (RelativeLayout) view.findViewById(R.id.titleView);
        titleView.setFocusable(false);

        indicator = (LinearLayout) view.findViewById(R.id.circleIndicator);
        indicator.setFocusable(false);

        indicatorInside = (LinearLayout) view.findViewById(R.id.indicatorInside);
        indicatorInside.setFocusable(false);

        bannerTitle = (TextView) view.findViewById(R.id.bannerTitle);
        bannerTitle.setFocusable(false);

        numIndicator = (TextView) view.findViewById(R.id.numIndicator);
        numIndicator.setFocusable(false);

        user_layout = view.findViewById(R.id.user_layout);
        user_layout2 = view.findViewById(R.id.user_layout2);
        typeImage = view.findViewById(R.id.typeImage);
        userHand = view.findViewById(R.id.userHand);
        userName = view.findViewById(R.id.userName);
        user_hand = view.findViewById(R.id.user_hand);
        user_name = view.findViewById(R.id.user_name);
        bannerTitleTwo = view.findViewById(R.id.bannerTitleTwo);
        numIndicatorInside = (TextView) view.findViewById(R.id.numIndicatorInside);
        bannerDefaultImage.setImageResource(bannerBackgroundImage);
        initViewPagerScroll();
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Banner);
        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_width, indicatorSize);
        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_height, indicatorSize);
        mIndicatorWidth2 = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_width, indicatorSize2);
        mIndicatorHeight2 = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_height, indicatorSize2);
        mIndicatorMargin = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_margin, 10);
        mIndicatorSelectedResId = typedArray.getResourceId(R.styleable.Banner_indicator_drawable_selected, R.drawable.gray_radius);
        mIndicatorUnselectedResId = typedArray.getResourceId(R.styleable.Banner_indicator_drawable_unselected, R.drawable.white_radius);
        scaleType = typedArray.getInt(R.styleable.Banner_image_scale_type, scaleType);
        delayTime = typedArray.getInt(R.styleable.Banner_delay_time, BannerConfig.TIME);
        scrollTime = typedArray.getInt(R.styleable.Banner_scroll_time, BannerConfig.DURATION);
        isAutoPlay = typedArray.getBoolean(R.styleable.Banner_is_auto_play, BannerConfig.IS_AUTO_PLAY);
        titleBackground = typedArray.getColor(R.styleable.Banner_title_background, BannerConfig.TITLE_BACKGROUND);
        titleHeight = typedArray.getDimensionPixelSize(R.styleable.Banner_title_height, BannerConfig.TITLE_HEIGHT);
        titleTextColor = typedArray.getColor(R.styleable.Banner_title_textcolor, BannerConfig.TITLE_TEXT_COLOR);
        titleTextSize = typedArray.getDimensionPixelSize(R.styleable.Banner_title_textsize, BannerConfig.TITLE_TEXT_SIZE);
        mLayoutResId = typedArray.getResourceId(R.styleable.Banner_banner_layout, mLayoutResId);
        bannerBackgroundImage = typedArray.getResourceId(R.styleable.Banner_banner_default_image, R.drawable.no_banner);
        typedArray.recycle();
    }

    private void initViewPagerScroll() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new BannerScroller(viewPager.getContext());
            mScroller.setDuration(scrollTime);
            mField.set(viewPager, mScroller);
        } catch (Exception e) {
            Log.e(tag, e.getMessage());
        }
    }


    public DiscoveresBanner isAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
        return this;
    }

    public DiscoveresBanner setImageLoader(ImageLoaderInterface imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    public DiscoveresBanner setDelayTime(int delayTime) {
        this.delayTime = delayTime;
        return this;
    }

    public DiscoveresBanner setIndicatorGravity(int type) {
        Log.e("123", "      type    " + type);
        switch (type) {
            case BannerConfig.LEFT:
                this.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                break;
            case BannerConfig.CENTER:
                this.gravity = Gravity.CENTER;
                break;
            case BannerConfig.RIGHT:
                this.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                break;
        }
        return this;
    }

    public DiscoveresBanner setBannerAnimation(Class<? extends PageTransformer> transformer) {
        try {
            setPageTransformer(true, transformer.newInstance());
        } catch (Exception e) {
            Log.e(tag, "Please set the PageTransformer class");
        }
        return this;
    }

    /**
     * Set the number of pages that should be retained to either side of the
     * current page in the view hierarchy in an idle state. Pages beyond this
     * limit will be recreated from the adapter when needed.
     *
     * @param limit How many pages will be kept offscreen in an idle state.
     * @return Banner
     */
    public DiscoveresBanner setOffscreenPageLimit(int limit) {
        if (viewPager != null) {
            viewPager.setOffscreenPageLimit(limit);
        }
        return this;
    }

    /**
     * Set a {@link PageTransformer} that will be called for each attached page whenever
     * the scroll position is changed. This allows the application to apply custom property
     * transformations to each page, overriding the default sliding look and feel.
     *
     * @param reverseDrawingOrder true if the supplied PageTransformer requires page views
     *                            to be drawn from last to first instead of first to last.
     * @param transformer         PageTransformer that will modify each page's animation properties
     * @return Banner
     */
    public DiscoveresBanner setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        viewPager.setPageTransformer(reverseDrawingOrder, transformer);
        return this;
    }

    public DiscoveresBanner setBannerTitles(List<String> titles) {
        this.titles = titles;
        return this;
    }

    public DiscoveresBanner setType(List<String> typeList) {
        this.typeList = typeList;
        return this;
    }

    public DiscoveresBanner setUserHand(List<String> userHandList) {
        this.userHandList = userHandList;
        return this;
    }

    public DiscoveresBanner setBannerStyle(int bannerStyle) {
        this.bannerStyle = bannerStyle;
        return this;
    }

    public DiscoveresBanner setViewPagerIsScroll(boolean isScroll) {
        this.isScroll = isScroll;
        return this;
    }

    public DiscoveresBanner setImages(List<?> imageUrls) {
        this.imageUrls = imageUrls;
        this.count = imageUrls.size();
        return this;
    }

    public DiscoveresBanner setSig(String sig) {
        this.sig = sig;
        return this;
    }

    public void update(List<?> imageUrls, List<String> titles) {
        this.titles.clear();
        this.titles.addAll(titles);
        update(imageUrls);
    }

    public void update(List<?> imageUrls) {
        this.imageUrls.clear();
        this.imageViews.clear();
        this.typeList.clear();
        this.indicatorImages.clear();
        this.userHandList.clear();
        this.imageUrls.addAll(imageUrls);
        this.count = this.imageUrls.size();
        start();
    }

    public void updateBannerStyle(int bannerStyle) {
        indicator.setVisibility(GONE);
        numIndicator.setVisibility(GONE);
        numIndicatorInside.setVisibility(GONE);
        indicatorInside.setVisibility(GONE);
        bannerTitle.setVisibility(View.GONE);
        titleView.setVisibility(View.GONE);
        this.bannerStyle = bannerStyle;
        start();
    }

    public DiscoveresBanner start() {
        setBannerStyleUI();
        setImageList(imageUrls);
        setData();
        return this;
    }

    private void setTitleStyleUI() {
        if (titles.size() != imageUrls.size()) {
            throw new RuntimeException("[Banner] --> The number of titles and images is different");
        }
        if (titleBackground != -1) {
            titleView.setBackgroundColor(titleBackground);
        }
        if (titleHeight != -1) {
            titleView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, titleHeight));
        }
        if (titleTextColor != -1) {
            bannerTitle.setTextColor(titleTextColor);
        }
        if (titleTextSize != -1) {
            bannerTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        }
        if (titles != null && titles.size() > 0) {
            if ("1".equals(typeList.get(0))) {
//                typeImage.setBackgroundColor(getResources().getColor(R.color.color_D8D8D8));
                Glide.with(context).load(userHandList.get(0))
                        .into(user_hand);
                user_name.setText(titles.get(0));
                typeImage.setImageResource(R.drawable.icon_image);
                bannerTitle.setVisibility(View.GONE);
                bannerTitleTwo.setVisibility(View.GONE);
                user_layout2.setVisibility(View.VISIBLE);
                user_layout.setVisibility(View.GONE);
                typeImage.setVisibility(View.VISIBLE);
            } else if ("2".equals(typeList.get(0))) {
//                typeImage.setBackgroundColor(getResources().getColor(R.color.color_74C997));
                Glide.with(context).load(userHandList.get(0))
                        .into(user_hand);
                user_name.setText(titles.get(0));
                typeImage.setImageResource(R.drawable.icon_video);
                bannerTitle.setVisibility(View.GONE);
                bannerTitleTwo.setVisibility(View.GONE);
                user_layout2.setVisibility(View.VISIBLE);
                user_layout.setVisibility(View.GONE);
                typeImage.setVisibility(View.VISIBLE);
            } else if ("3".equals(typeList.get(0))) {
                Glide.with(context).load(userHandList.get(0))
                        .into(userHand);
                userName.setText(titles.get(0));
                bannerTitle.setVisibility(View.GONE);
                bannerTitleTwo.setVisibility(View.GONE);
                user_layout2.setVisibility(View.GONE);
                user_layout.setVisibility(View.VISIBLE);
                typeImage.setVisibility(View.GONE);
            } else if ("4".equals(typeList.get(0))) {
                bannerTitle.setText(titles.get(0));
                bannerTitleTwo.setVisibility(View.GONE);
                user_layout2.setVisibility(View.GONE);
                user_layout.setVisibility(View.GONE);
                bannerTitle.setVisibility(View.VISIBLE);
                typeImage.setVisibility(View.GONE);
            } else if ("5".equals(typeList.get(0))) {
                bannerTitleTwo.setText(titles.get(0));
                bannerTitleTwo.setVisibility(View.VISIBLE);
                user_layout2.setVisibility(View.GONE);
                user_layout.setVisibility(View.GONE);
                bannerTitle.setVisibility(View.GONE);
                typeImage.setVisibility(View.GONE);
            } else if ("6".equals(typeList.get(0))) {
                typeImage.setImageResource(R.drawable.icon_bianji);
//                typeImage.setBackgroundColor(getResources().getColor(R.color.color_D56B6B));
                Glide.with(context).load(userHandList.get(0))
                        .into(user_hand);
                user_name.setText(titles.get(0));
                bannerTitle.setVisibility(View.GONE);
                bannerTitleTwo.setVisibility(View.GONE);
                user_layout2.setVisibility(View.VISIBLE);
                user_layout.setVisibility(View.GONE);
                typeImage.setVisibility(View.VISIBLE);
            }
            titleView.setVisibility(View.VISIBLE);
        }
    }

    private void setBannerStyleUI() {
        int visibility = count > 1 ? View.VISIBLE : View.GONE;
        switch (bannerStyle) {
            case BannerConfig.CIRCLE_INDICATOR:
//                indicator.setVisibility(visibility);
                break;
            case BannerConfig.NUM_INDICATOR:
                numIndicator.setVisibility(View.VISIBLE);
                break;
            case BannerConfig.NUM_INDICATOR_TITLE:
                numIndicatorInside.setVisibility(visibility);
                setTitleStyleUI();
                break;
            case BannerConfig.CIRCLE_INDICATOR_TITLE:
//                indicator.setVisibility(visibility);
                setTitleStyleUI();
                break;
            case BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE:
                indicatorInside.setVisibility(visibility);
                setTitleStyleUI();
                break;
        }
    }

    private void initImages() {
        imageViews.clear();
        if (bannerStyle == BannerConfig.CIRCLE_INDICATOR ||
                bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE ||
                bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE) {
            createIndicator();
        } else if (bannerStyle == BannerConfig.NUM_INDICATOR_TITLE) {
            numIndicatorInside.setText("1/" + count);
        } else if (bannerStyle == BannerConfig.NUM_INDICATOR) {
            numIndicator.setText("1/" + count);
        }
    }

    private void setImageList(List<?> imagesUrl) {
        if (imagesUrl == null || imagesUrl.size() <= 0) {
            bannerDefaultImage.setVisibility(VISIBLE);
            Log.e(tag, "The image data set is empty.");
            return;
        }
        bannerDefaultImage.setVisibility(GONE);
        initImages();
        for (int i = 0; i <= count + 1; i++) {
            View imageView = null;
            if (imageLoader != null) {
                imageView = imageLoader.createImageView(context);
            }
            if (imageView == null) {
                imageView = new ImageView(context);
            }
            setScaleType(imageView);
            String url = null;
            if (i == 0) {
                url = imagesUrl.get(count - 1).toString();
            } else if (i == count + 1) {
                url = imagesUrl.get(0).toString();
            } else {
                url = imagesUrl.get(i - 1).toString();
            }
            imageViews.add(imageView);
            if (imageLoader != null)
                imageLoader.displayImage(context, url, imageView);
            else
                Log.e(tag, "Please set images loader.");
        }
    }

    private void setScaleType(View imageView) {
        if (imageView instanceof ImageView) {
            ImageView view = ((ImageView) imageView);
            switch (scaleType) {
                case 0:
                    view.setScaleType(ScaleType.CENTER);
                    break;
                case 1:
                    view.setScaleType(ScaleType.CENTER_CROP);
                    break;
                case 2:
                    view.setScaleType(ScaleType.CENTER_INSIDE);
                    break;
                case 3:
                    view.setScaleType(ScaleType.FIT_CENTER);
                    break;
                case 4:
                    view.setScaleType(ScaleType.FIT_END);
                    break;
                case 5:
                    view.setScaleType(ScaleType.FIT_START);
                    break;
                case 6:
                    view.setScaleType(ScaleType.FIT_XY);
                    break;
                case 7:
                    view.setScaleType(ScaleType.MATRIX);
                    break;
            }

        }
    }

    private void createIndicator() {
        indicatorImages.clear();
        indicator.removeAllViews();
        indicatorInside.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mIndicatorWidth, mIndicatorHeight);
            params.leftMargin = mIndicatorMargin;
            params.rightMargin = mIndicatorMargin;
            if (i == 0) {
                imageView.setImageResource(mIndicatorSelectedResId);
            } else {
                imageView.setImageResource(mIndicatorUnselectedResId);
            }
            imageView.setLayoutParams(params);
            indicatorImages.add(imageView);
            if (bannerStyle == BannerConfig.CIRCLE_INDICATOR ||
                    bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE) {
                indicator.addView(imageView, params);
            } else if (bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE) {
                imageView.setLayoutParams(params);
                indicatorInside.addView(imageView, params);
            }
        }
    }


    private void setData() {
        currentItem = 1;
        if (adapter == null) {
            adapter = new BannerPagerAdapter();
            viewPager.addOnPageChangeListener(this);
        }
        viewPager.setAdapter(adapter);
        viewPager.setFocusable(true);
        viewPager.setCurrentItem(1);
        if (gravity != -1)
            indicator.setGravity(gravity);
        if (isScroll && count > 1) {
            viewPager.setScrollable(true);
        } else {
            viewPager.setScrollable(false);
        }
        //自动轮播
        if (isAutoPlay)
            startAutoPlay();
    }

    //自动轮播方法
    public void startAutoPlay() {
        handler.removeCallbacks(task);
        handler.postDelayed(task, delayTime);
    }

    //自动轮播方法
    public void stopAutoPlay() {
        handler.removeCallbacks(task);
    }

    //自动轮播方法
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (count > 1 && isAutoPlay) {
                currentItem = currentItem % (count + 1) + 1;
//                Log.i(tag, "curr:" + currentItem + " count:" + count);
                if (currentItem == 1) {
                    viewPager.setCurrentItem(currentItem, false);
                    handler.post(task);
                } else {
                    viewPager.setCurrentItem(currentItem);
                    handler.postDelayed(task, delayTime);
                }
            }
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.i(tag, ev.getAction() + "--" + isAutoPlay);
        if (isAutoPlay) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                //自动轮播方法
                startAutoPlay();
            } else if (action == MotionEvent.ACTION_DOWN) {
                //自动轮播方法
                stopAutoPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 返回真实的位置
     *
     * @param position
     * @return 下标从0开始
     */
    public int toRealPosition(int position) {
        int realPosition = (position - 1) % count;
        if (realPosition < 0)
            realPosition += count;
        return realPosition;
    }

    class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(imageViews.get(position));
            View view = imageViews.get(position);
            if (bannerListener != null) {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(tag, "你正在使用旧版点击事件接口，下标是从1开始，" +
                                "为了体验请更换为setOnBannerListener，下标从0开始计算");
                        bannerListener.OnBannerClick(position);
                    }
                });
            }
            if (listener != null) {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.OnBannerClick(toRealPosition(position));
                    }
                });
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
//        Log.i(tag,"currentItem: "+currentItem);
        switch (state) {
            case 0://No operation
                if (currentItem == 0) {
                    viewPager.setCurrentItem(count, false);
                } else if (currentItem == count + 1) {
                    viewPager.setCurrentItem(1, false);
                }
                break;
            case 1://start Sliding
                if (currentItem == count + 1) {
                    viewPager.setCurrentItem(1, false);
                } else if (currentItem == 0) {
                    viewPager.setCurrentItem(count, false);
                }
                break;
            case 2://end Sliding
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(toRealPosition(position), positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        currentItem = position;
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(toRealPosition(position));
        }
        if (bannerStyle == BannerConfig.CIRCLE_INDICATOR ||
                bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE ||
                bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mIndicatorWidth, mIndicatorHeight);
            params.leftMargin = mIndicatorMargin;
            params.rightMargin = mIndicatorMargin;
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(mIndicatorWidth2, mIndicatorHeight2);
            params2.leftMargin = mIndicatorMargin;
            params2.rightMargin = mIndicatorMargin;
            indicatorImages.get((lastPosition - 1 + count) % count).setLayoutParams(params);
            indicatorImages.get((lastPosition - 1 + count) % count).setImageResource(mIndicatorUnselectedResId);
            indicatorImages.get((position - 1 + count) % count).setLayoutParams(params2);
            indicatorImages.get((position - 1 + count) % count).setImageResource(mIndicatorSelectedResId);

            lastPosition = position;
        }
        if (position == 0) position = count;
        if (position > count) position = 1;
        switch (bannerStyle) {
            case BannerConfig.CIRCLE_INDICATOR:
                break;
            case BannerConfig.NUM_INDICATOR:
                numIndicator.setText(position + "/" + count);
                break;
            case BannerConfig.NUM_INDICATOR_TITLE:
                numIndicatorInside.setText(position + "/" + count);
                bannerTitle.setText(titles.get(position - 1));
                break;
            case BannerConfig.CIRCLE_INDICATOR_TITLE:
                bannerTitle.setText(titles.get(position - 1));
                break;
            case BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE:
                if ("1".equals(typeList.get(position - 1))) {
                    Glide.with(context).load(userHandList.get(position - 1))
                            .into(user_hand);
                    user_name.setText(titles.get(position - 1));
                    typeImage.setImageResource(R.drawable.icon_image);
                    bannerTitle.setVisibility(View.GONE);
                    bannerTitleTwo.setVisibility(View.GONE);
                    user_layout2.setVisibility(View.VISIBLE);
                    user_layout.setVisibility(View.GONE);
                    typeImage.setVisibility(View.VISIBLE);
                } else if ("2".equals(typeList.get(position - 1))) {
                    Glide.with(context).load(userHandList.get(position - 1))
                            .into(user_hand);
                    user_name.setText(titles.get(position - 1));
                    typeImage.setImageResource(R.drawable.icon_video);
                    bannerTitle.setVisibility(View.GONE);
                    bannerTitleTwo.setVisibility(View.GONE);
                    user_layout2.setVisibility(View.VISIBLE);
                    user_layout.setVisibility(View.GONE);
                    typeImage.setVisibility(View.VISIBLE);
                } else if ("3".equals(typeList.get(position - 1))) {
                    Glide.with(context).load(userHandList.get(position - 1))
                            .into(userHand);
                    userName.setText(titles.get(position - 1));
                    bannerTitle.setVisibility(View.GONE);
                    bannerTitleTwo.setVisibility(View.GONE);
                    user_layout2.setVisibility(View.GONE);
                    user_layout.setVisibility(View.VISIBLE);
                    typeImage.setVisibility(View.GONE);
                } else if ("4".equals(typeList.get(position - 1))) {
                    bannerTitle.setText(titles.get(position - 1));
                    bannerTitleTwo.setVisibility(View.GONE);
                    user_layout2.setVisibility(View.GONE);
                    user_layout.setVisibility(View.GONE);
                    bannerTitle.setVisibility(View.VISIBLE);
                    typeImage.setVisibility(View.GONE);
                } else if ("5".equals(typeList.get(position - 1))) {
                    bannerTitleTwo.setText(titles.get(position - 1));
                    bannerTitleTwo.setVisibility(View.VISIBLE);
                    user_layout2.setVisibility(View.GONE);
                    user_layout.setVisibility(View.GONE);
                    bannerTitle.setVisibility(View.GONE);
                    typeImage.setVisibility(View.GONE);
                } else if ("6".equals(typeList.get(position - 1))) {
                    typeImage.setImageResource(R.drawable.icon_bianji);
                    Glide.with(context).load(userHandList.get(position - 1))
                            .into(user_hand);
                    user_name.setText(titles.get(position - 1));
                    bannerTitle.setVisibility(View.GONE);
                    bannerTitleTwo.setVisibility(View.GONE);
                    user_layout2.setVisibility(View.VISIBLE);
                    user_layout.setVisibility(View.GONE);
                    typeImage.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    @Deprecated
    public DiscoveresBanner setOnBannerClickListener(OnBannerClickListener listener) {
        this.bannerListener = listener;
        return this;
    }

    /**
     * 废弃了旧版接口，新版的接口下标是从1开始，同时解决下标越界问题
     *
     * @param listener
     * @return
     */
    public DiscoveresBanner setOnBannerListener(OnBannerListener listener) {
        this.listener = listener;
        return this;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    public void releaseBanner() {
        handler.removeCallbacksAndMessages(null);
    }
}

package cn.nicolite.huthelper.view.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import cn.nicolite.huthelper.R;

/**
 * 根据图片数量分不同格式显示
 * Created by 高沛 on 2016/12/21.
 */

public abstract class PictureLayout extends ViewGroup {

    private static final float DEFUALT_SPACE = 5f;
    private static final int MAX_COUNT = 9;
    protected Context context;
    private float space = DEFUALT_SPACE;
    private int rows;
    private int colummns;
    private int width;
    private int singleWidth;

    private boolean isFirst;
    private boolean isShowAll;
    private List<String> urlList = new ArrayList<>();
    private OnClickImageListener onClickImageListener;

    public PictureLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PictureLayout);
        space = typedArray.getDimension(R.styleable.PictureLayout_space, DEFUALT_SPACE);
        typedArray.recycle();
        init(context);
    }

    public PictureLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        if (getListSize(urlList) == 0) {
            setVisibility(GONE);
        }
    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bottom) {
        width = right - left;
        singleWidth = (int) ((width - space * 2) / 3);
        if (isFirst) {
            notifyDataSetChanged();
            isFirst = false;
        }

    }

    public void setSpacing(float space) {
        this.space = space;
    }

    public void setIsShowAll(boolean isShowAll) {
        this.isShowAll = isShowAll;
    }

    private int getListSize(List<String> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }

    public void setUrlList(List<String> urlList) {
        if (getListSize(urlList) == 0) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
        this.urlList.clear();
        this.urlList.addAll(urlList);
        if (!isFirst) {
            notifyDataSetChanged();
        }
    }

    public void notifyDataSetChanged() {
        post(new TimerTask() {
            @Override
            public void run() {
                refresh();
            }
        });
    }

    private void refresh() {
        removeAllViews();
        int size = getListSize(urlList);
        if (size > 0) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
        if (size == 1) {
            String url = urlList.get(0);
            RatioImageView imageView = createImageView(0, url);
            LayoutParams params = getLayoutParams();
            params.height = singleWidth;
            setLayoutParams(params);
            imageView.layout(0, 0, singleWidth, singleWidth);
            boolean isShowDefualt = displayOneImage(imageView, url, width);
            if (isShowDefualt) {
                layoutImageView(imageView, 0, url);
            } else {
                addView(imageView);
            }
            return;
        }
        generateChildrenLayout(size);
        layoutParams();
        for (int i = 0; i < size; i++) {
            String url = urlList.get(i);
            RatioImageView imageView;
            if (!isShowAll) {
                if (i < MAX_COUNT - 1) {
                    imageView = createImageView(i, url);
                    layoutImageView(imageView, i, url);
                } else { //第9张时
                    if (size <= MAX_COUNT) {//刚好第9张
                        imageView = createImageView(i, url);
                        layoutImageView(imageView, i, url);
                    } else {//超过9张
                        imageView = createImageView(i, url);
                        layoutImageView(imageView, i, url);
                        break;
                    }
                }
            } else {
                imageView = createImageView(i, url);
                layoutImageView(imageView, i, url);
            }
        }
    }

    private void generateChildrenLayout(int length) {
        if (length <= 3) {
            rows = 1;
            colummns = length;
        } else if (length <= 6) {
            rows = 2;
            colummns = 3;
            if (length == 4)
                colummns = 2;
        } else {
            colummns = 3;
            if (isShowAll) {
                rows = length / 3;
                int b = length % 3;
                if (b > 0) {
                    rows++;
                }
            } else {
                rows = 3;
            }
        }
    }

    private void layoutParams() {
        int singleHeight = singleWidth;

        //根据子view数量确定高度
        LayoutParams params = getLayoutParams();
        params.height = (int) (singleHeight * rows + space * (rows - 1));
        setLayoutParams(params);
    }

    protected void setOneImageLayoutParams(RatioImageView imageView, int width, int height) {
        imageView.setLayoutParams(new LayoutParams(width, height));
        imageView.layout(0, 0, width, height);

        LayoutParams params = getLayoutParams();
//        params.width = width;
        params.height = height;
        setLayoutParams(params);
    }

    private void layoutImageView(RatioImageView imageView, int i, String url) {
        final int singleWidth = (int) ((width - space * (3 - 1)) / 3);
        int singleHeight = singleWidth;

        int[] position = findPosition(i);
        int left = (int) ((singleWidth + space) * position[1]);
        int top = (int) ((singleHeight + space) * position[0]);
        int right = left + singleWidth;
        int bottom = top + singleHeight;

        imageView.layout(left, top, right, bottom);

        addView(imageView);
        displayImage(imageView, url);
    }

    private RatioImageView createImageView(final int i, final String url) {
        RatioImageView imageView = new RatioImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickImageListener != null) {
                    onClickImageListener.onClickImage(i, urlList);
                }
            }
        });
        return imageView;
    }

    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colummns; j++) {
                if ((i * colummns + j) == childNum) {
                    position[0] = i;//行
                    position[1] = j;//列
                    break;
                }
            }
        }
        return position;
    }

    /**
     * @param imageView
     * @param url
     * @param parentWidth 父控件宽度
     * @return true 代表按照九宫格默认大小显示，false 代表按照自定义宽高显示
     */
    protected abstract boolean displayOneImage(RatioImageView imageView, String url, int parentWidth);

    protected abstract void displayImage(RatioImageView imageView, String url);

    public void setOnClickImageListener(OnClickImageListener onClickImageListener) {
        this.onClickImageListener = onClickImageListener;
    }

    public interface OnClickImageListener {
        void onClickImage(int position, List<String> urlList);
    }

    //禁止滑动
    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }
}

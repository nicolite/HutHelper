package cn.nicolite.huthelper.view.customView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.nicolite.huthelper.utils.DensityUtils;

/**
 * Created by 高沛 on 16-11-20.
 * 自定义条状统计图控件
 */

public class BarListView extends View {
    private static final String TAG = "BarListView";
    private Context context;
    //数据
    private List<Bar> eleList = new ArrayList<>();
    //最大值
    private float maxValue;

    private Paint p = new Paint();
    private TextPaint tp = new TextPaint();
    private boolean shouldUpdate = false;

    private float tbpadding;
    private float textpadding;
    private float textHeight;

    private Bitmap fullImage;

    public BarListView(Context context) {
        this(context, null);
    }

    public BarListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public BarListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void setBars(List<Bar> points) {
        this.eleList = points;
        shouldUpdate = true;
        requestLayout();
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    private void init() {
        tp.setColor(Color.WHITE);
        tp.setAntiAlias(true);
        tp.setTextSize(DensityUtils.sp2px(context, 15));
        Paint.FontMetrics fm = tp.getFontMetrics();
        textHeight = (float) Math.ceil(fm.descent - fm.ascent);
        textpadding = DensityUtils.dp2px(context, 10);
        tbpadding = DensityUtils.dp2px(context, 20);
    }

    public void update() {
        shouldUpdate = true;
        postInvalidate();
        invalidate();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int mWidth;
        int mHeight;
        if (wMode == MeasureSpec.EXACTLY) {
            mWidth = wSize;
        } else {
            mWidth = DensityUtils.dp2px(context, 500);
        }
        if (hMode == MeasureSpec.EXACTLY) {
            mHeight = hSize;
        } else {
            int size = eleList.size();
            if (size == 0) {
                mHeight = 0;
            } else {
                mHeight = (int) (textHeight * size + textpadding * (size - 1));
            }
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas ca) {
        if (fullImage == null || shouldUpdate) {
            if (eleList == null || eleList.size() == 0) {
                setVisibility(GONE);
                return;
            } else {
                setVisibility(VISIBLE);
            }
            fullImage = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(fullImage);
            canvas.drawColor(Color.TRANSPARENT);
            float allvalue = 0;
            float width = getWidth();
            String maxItemName = "";
            float maxItemNum = 0;

            //查找最长字符串 最数值 计算总和
            for (Bar b : eleList) {
                allvalue += b.getNum();
                if (b.getName().length() > maxItemName.length()) {
                    maxItemName = b.getName();
                }
                if (b.getNum() > maxItemNum)
                    maxItemNum = b.getNum();
            }
            if (maxValue != 0)
                allvalue = maxValue;
            p.setColor(Color.parseColor("#99ffffff"));
            p.setAntiAlias(true);
            //获取文字高宽
            Rect rect = new Rect();
            tp.getTextBounds(maxItemName, 0, maxItemName.length(), rect);
            float wa = rect.width();
            float ha = rect.height();//文字高宽
            //获取数值高宽
            rect = new Rect();
            tp.getTextBounds("" + maxItemNum, 0, ("" + maxItemNum).length(), rect);
            float wn = rect.width();
            //计算可用宽度 总宽度-字符串宽度-数值宽度-padding
            float canusewidth = width - wa - wn - 2 * tbpadding;
            //当前高度
            float nowhigh = 0;

            Paint.FontMetricsInt fontMetrics = tp.getFontMetricsInt();
            float high = fontMetrics.top - fontMetrics.bottom;

            for (Bar b : eleList) {
                String name = b.getName();
                int num = b.getNum();
                float barwidth = canusewidth * ((float) num / allvalue) + wa + tbpadding;
                float baseline = ha - 1 / 2 * (fontMetrics.ascent + fontMetrics.descent);
//            baseline =1/2 * ha + 1/2 * (fontMetrics.descent- fontMetrics.ascent) - fontMetrics.bottom;
                canvas.drawText(name, 0, nowhigh + baseline, tp);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    canvas.drawRoundRect(wa + tbpadding, nowhigh, barwidth, nowhigh + ha, 20, 20, p);
                } else {
                    canvas.drawRect(wa + tbpadding, nowhigh, barwidth, nowhigh + ha, p);
                }
                canvas.drawText("" + num, barwidth + tbpadding, nowhigh + baseline, tp);
                canvas.save();
                canvas.restore();
                nowhigh += ha + textpadding;
            }
            shouldUpdate = false;
        }
        ca.drawBitmap(fullImage, 0, 0, null);
    }

    public class Bar {
        private String name;
        private int num;

        public Bar() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}

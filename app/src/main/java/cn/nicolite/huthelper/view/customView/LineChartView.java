package cn.nicolite.huthelper.view.customView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import cn.nicolite.huthelper.model.bean.GradeRank;
import cn.nicolite.huthelper.utils.DensityUtils;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.ScreenUtils;

/**
 * Created by 高沛 on 2017/4/27.
 */

public class LineChartView extends View {

    private static final String COLOR_1 = "#fdeb6b";
    private static final String COLOR_2 = "#6effb6";

    private static final String[] XQ_INDEX = {"大一上", "大一下", "大二上", "大二下", "大三上", "大三下", "大四上", "大四下", "大五上", "大五下"};
    private static final String[] XN_INDEX = {"大一", "大二", "大三", "大四", "大五"};
    private TextPaint titlePaint;   //标题 28号 adadad
    private Paint linePaint1;   //一号线
    private Paint linePaint2;   //二号线
    private Paint circlePaint;  //空心圆
    private Paint bgLinePaint;  //背景线
    private TextPaint valuePaint1;   //数值
    private TextPaint valuePaint2;   //数值
    private TextPaint labelPaint;   //左侧标注
    private TextPaint textPaint;
    private int lineWidth;

    private boolean shouldUpdate = true;
    private List<GradeRank> rankList;
    private Bitmap fullImage;
    int paddingLeft, linePadding, startY;
    private int sp14, sp10;
    private float textHeight;

    private Context context;
    private int perNum;
    private int minNum;
    boolean isXq = true;


    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        sp10 = DensityUtils.sp2px(context, 10);
        sp14 = DensityUtils.sp2px(context, 14);
        paddingLeft = DensityUtils.dp2px(context, 30);
        linePadding = DensityUtils.dp2px(context, 40);
        startY = DensityUtils.dp2px(context, 12);
        lineWidth = DensityUtils.dp2px(context, 1.0f);

        initPaint();

        Paint.FontMetrics fm = textPaint.getFontMetrics();
        textHeight = (float) Math.ceil(fm.descent - fm.ascent);

    }

    private void initData() {
        int max = 0, min = 1000;
        for (GradeRank r : rankList) {
            int mc = Integer.parseInt(r.getZyrank());
            if (mc > max)
                max = mc;
            if (mc < min)
                min = mc;
        }

        for (GradeRank r : rankList) {
            int mc = Integer.parseInt(r.getBjrank());
            if (mc > max)
                max = mc;
            if (mc < min)
                min = mc;
        }

        min = (min / 10) * 10;
        max = (max / 10 + 1) * 10;
        perNum = (int) Math.ceil((max - min) / 3.0);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int wSize = MeasureSpec.getSize(widthMeasureSpec);
//        int wMode = MeasureSpec.getMode(widthMeasureSpec);
//        int hSize = MeasureSpec.getSize(heightMeasureSpec);
//        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int mWidth;
        int mHeight;
//        if (wMode == MeasureSpec.EXACTLY) {
//            mWidth = wSize;
//        } else {
        mWidth = ScreenUtils.getScreenWidth(context) - DensityUtils.dp2px(context, 24);
        //    }
//        if (hMode == MeasureSpec.EXACTLY) {
//            mHeight = hSize;
//        } else {
        mHeight = (int) (linePadding * 6 + startY + 2 * textHeight + 20);
        //   }
        setMeasuredDimension(mWidth, mHeight);
    }

    private void initPaint() {
        //虚线
        bgLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgLinePaint.setStyle(Paint.Style.STROKE);
        bgLinePaint.setColor(Color.parseColor("#ADADAD"));
        bgLinePaint.setStrokeWidth(2);
        PathEffect effects = new DashPathEffect(new float[]{13, 13}, 5);
        bgLinePaint.setPathEffect(effects);
        //发光线一号
        BlurMaskFilter bmf = new BlurMaskFilter(4, BlurMaskFilter.Blur.SOLID);
        linePaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint1.setStyle(Paint.Style.STROKE);
        linePaint1.setColor(Color.parseColor("#fdeb6b"));
        linePaint1.setStrokeWidth(lineWidth);
        linePaint1.setMaskFilter(bmf);
        //发光线2号
        linePaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint2.setStyle(Paint.Style.STROKE);
        linePaint2.setColor(Color.parseColor("#6effb6"));
        linePaint2.setStrokeWidth(lineWidth);
        linePaint2.setMaskFilter(bmf);
        //标题
        titlePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        titlePaint.setTextSize(sp14);
        titlePaint.setColor(Color.parseColor("#adadad"));
        //数值一号
        valuePaint1 = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        valuePaint1.setTextSize(sp10);
        valuePaint1.setColor(Color.parseColor(COLOR_1));
        //数值二号
        valuePaint2 = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        valuePaint2.setTextSize(sp10);
        valuePaint2.setColor(Color.parseColor(COLOR_2));
        //文本
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(sp10);
        textPaint.setColor(Color.WHITE);
        //标注
        labelPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setTextSize(sp10);
        labelPaint.setColor(Color.parseColor("#adadad"));
        //圆点
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas ca) {
        if (fullImage == null || shouldUpdate) {
            if (ListUtils.isEmpty(rankList)) {
                setVisibility(GONE);
                return;
            } else {
                setVisibility(VISIBLE);
            }

            fullImage = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(fullImage);
            canvas.drawColor(Color.TRANSPARENT);
            //绘制标题
            canvas.drawText("排名", 0, startY, titlePaint);
            //画虚线
            int y = startY;
            for (int i = 0; i < 5; i++) {
                canvas.drawLine(paddingLeft, y, this.getWidth(), y, bgLinePaint);
                y += linePadding;
            }
            //画标识
            canvas.drawText("班级", 0, startY + linePadding * 2, textPaint);
            Rect rect = new Rect();
            textPaint.getTextBounds("班级", 0, "班级".length(), rect);
            float wa = rect.width();
            float ha = rect.height();
            canvas.drawLine(0, startY + linePadding * 2 + ha, wa, startY + linePadding * 2 + ha, linePaint1);
            canvas.drawText("专业", 0, startY + linePadding * 3, textPaint);
            canvas.drawLine(0, startY + linePadding * 3 + ha, wa, startY + linePadding * 3 + ha, linePaint2);

            canvas.drawText(perNum + "", paddingLeft - wa, startY + linePadding, labelPaint);
            canvas.drawText(perNum * 4 + "", paddingLeft - wa, startY + linePadding * 4, labelPaint);
            //班级排名 计算数据
            int perWidth = (getWidth() - paddingLeft) / rankList.size();

            Path bjPath = new Path();//班级曲线
            Path zyPath = new Path();//专业曲线

            for (int i = 0; i < rankList.size(); i++) {
                int cx = perWidth / 2 + perWidth * i + paddingLeft;
                float bjy = startY + ((float) (Integer.parseInt(rankList.get(i).getBjrank()) - minNum) / perNum) * linePadding;
                float zyy = startY + ((float) (Integer.parseInt(rankList.get(i).getZyrank()) - minNum) / perNum) * linePadding;
                if (i == 0) {
                    bjPath.moveTo(cx, bjy);
                    zyPath.moveTo(cx, zyy);
                    continue;
                }
                bjPath.lineTo(cx, bjy);
                zyPath.lineTo(cx, zyy);
            }
            canvas.drawPath(bjPath, linePaint1);
            canvas.drawPath(zyPath, linePaint2);

            rect = new Rect();
            if (isXq) {
                textPaint.getTextBounds("大一上", 0, "大一上".length(), rect);
            } else {
                textPaint.getTextBounds("大一", 0, "大一".length(), rect);
            }

            wa = rect.width();
            ha = rect.height();

            for (int i = 0; i < rankList.size(); i++) {
                int cx = perWidth / 2 + perWidth * i + paddingLeft;
                float bjy = startY + ((float) (Integer.parseInt(rankList.get(i).getBjrank()) - minNum)) / perNum * linePadding;
                float zyy = startY + ((float) (Integer.parseInt(rankList.get(i).getZyrank()) - minNum)) / perNum * linePadding;

                //绘制圆点 数值
                canvas.drawCircle(cx, zyy, 12, circlePaint);
                canvas.drawCircle(cx, bjy, 12, circlePaint);

                canvas.drawText(rankList.get(i).getBjrank(), cx - 14, bjy - 30, valuePaint1);
                canvas.drawText(rankList.get(i).getZyrank(), cx - 14, zyy + 48, valuePaint2);
                if (isXq) {
                    canvas.drawText(XQ_INDEX[i], cx - wa / 2, linePadding * 5 + startY, textPaint);
                } else {
                    canvas.drawText(XN_INDEX[i], cx - wa / 2, linePadding * 5 + startY, textPaint);
                }

                textPaint.getTextBounds("0.35", 0, "0.35".length(), rect);
                int jdW = rect.width();
                canvas.drawText(rankList.get(i).getZhjd(), cx - jdW / 2, linePadding * 5 + startY + 2 * ha, textPaint);
                canvas.drawText(rankList.get(i).getPjf(), cx - jdW / 2, linePadding * 6 + startY + 2 * ha, textPaint);
                if (i == 0) {
                    canvas.drawText("绩点", 0, linePadding * 5 + startY + ha + ha + 5, titlePaint);
                    canvas.drawText("平均分", 0, linePadding * 6 + startY + ha + ha + 5, titlePaint);
                }
            }
            shouldUpdate = false;
        }
        ca.drawBitmap(fullImage, 0, 0, null);
    }

    public void setRankingData(List<GradeRank> ranks, boolean isXq) {
        this.rankList = ranks;
        this.isXq = isXq;
        shouldUpdate = true;
        initData();
        requestLayout();
    }
}

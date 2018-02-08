package cn.nicolite.huthelper.view.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import cn.nicolite.huthelper.utils.DensityUtils;

/**
 * Created by 高沛 on 2016/12/16.
 * 自定义圆环统计图
 */

public class CirclePie extends View {

    private static final String TAG = "CirclePie";
    private Context context;
    private int sweepInWidth;
    private int sweepOutWidth;
    private float maxNum;
    private float currNum;
    private Paint paint1;
    private Paint paint2;
    private Paint paint3;
    private int radiusIn;
    private int radiusOut;
    private int mWidth;
    private int mHeight;
    private int topTextSize;
    private int bottomTextSize;


    public CirclePie(Context context) {
        super(context);
        init(context);
    }

    public CirclePie(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CirclePie(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setCurrNum(float maxnum, float currnum) {
        this.maxNum = maxnum;
        this.currNum = currnum;
        invalidate();
    }

    private void init(Context context) {


        this.context = context;
        sweepInWidth = DensityUtils.dp2px(context, 5);
        sweepOutWidth = DensityUtils.dp2px(context, 1);
        radiusIn = DensityUtils.dp2px(context, 67);
        radiusOut = DensityUtils.dp2px(context, 75);

        topTextSize= DensityUtils.sp2px(context, 22);
        bottomTextSize= DensityUtils.sp2px(context, 10);
        paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(mWidth / 2, (mWidth) / 2);
        drawRound(canvas);  //画内外圆弧
        drawIndicator(canvas); //画当前进度值
        drawCenterText(canvas);//画中间的文字
        canvas.restore();
    }

    private void drawRound(Canvas canvas) {
        canvas.save();
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setColor(Color.WHITE);
        paint1.setStrokeWidth(sweepInWidth);
        canvas.drawCircle(0, 0, radiusIn, paint1);
        paint1.setStrokeWidth(sweepOutWidth);
        canvas.drawCircle(0, 0, radiusOut, paint1);
        canvas.restore();
    }

    private void drawIndicator(Canvas canvas) {
        canvas.save();
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setColor(0xff1ddbcd);
        paint2.setStrokeWidth(sweepInWidth);
        RectF rectf = new RectF(-radiusIn, -radiusIn, radiusIn, radiusIn);
        canvas.drawArc(rectf, 0, currNum / maxNum * 360, false, paint2);
        canvas.restore();

    }

    private void drawCenterText(Canvas canvas) {
        canvas.save();
        paint3.setStyle(Paint.Style.FILL);
        paint3.setColor(Color.WHITE);
        paint3.setFakeBoldText(true);
        paint3.setTextSize(topTextSize);
        String content1 = (maxNum-currNum) + "/" + maxNum;
        canvas.drawText(content1, -paint3.measureText(content1) / 2, 0, paint3);
        paint3.setTextSize(bottomTextSize);
        String content = "未得学分/总学分";
        paint3.setFakeBoldText(false);
        Rect r = new Rect();
        paint3.getTextBounds(content, 0, content.length(), r);
        canvas.drawText(content, -r.width() / 2, r.height() + 20, paint3);
        canvas.restore();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        if (wMode == MeasureSpec.EXACTLY) {
            mWidth = wSize;
        } else {
            mWidth = DensityUtils.dp2px(context, 300);
        }
        if (hMode == MeasureSpec.EXACTLY) {
            mHeight = hSize;
        } else {
            mHeight = DensityUtils.dp2px(context, 300);
        }
        setMeasuredDimension(mWidth, mHeight);
    }

}

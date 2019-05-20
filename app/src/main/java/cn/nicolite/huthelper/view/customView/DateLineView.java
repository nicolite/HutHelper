package cn.nicolite.huthelper.view.customView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.nicolite.huthelper.model.bean.TimeAxis;
import cn.nicolite.huthelper.utils.DensityUtils;
import cn.nicolite.huthelper.utils.ListUtils;

/**
 * 时间轴
 * Created by nicolite on 17-10-22.
 */

public class DateLineView extends View {
    private int width;
    private int height;
    private Paint linePaint;
    private TextPaint textPaint;
    private Paint circlePaint;
    private boolean shouldUpdate = false;
    private Bitmap fullImage;
    private Context context;
    private List<TimeAxis> timeAxisList;

    public DateLineView(Context context) {
        this(context, null);
    }

    public DateLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        //虚线
        linePaint = new Paint();
        linePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(3);
        DashPathEffect effect = new DashPathEffect(new float[]{8, 8}, 0);
        linePaint.setPathEffect(effect);

        //文本
        textPaint = new TextPaint();
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(DensityUtils.sp2px(context, 10));
        textPaint.setColor(Color.WHITE);

        //圆点
        circlePaint = new Paint();
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.WHITE);

        timeAxisList = new ArrayList<>();
        TimeAxis day = new TimeAxis();
        day.setDate("2017.09.12");
        day.setName("端午");
        day.setDays(0);
        timeAxisList.add(day);
        day = new TimeAxis();
        day.setDate("2017.09.12");
        day.setName("毕业");
        day.setDays(10);
        timeAxisList.add(day);
        day = new TimeAxis();
        day.setDate("2017.09.12");
        day.setName("实习");
        day.setDays(11);
        timeAxisList.add(day);
        day = new TimeAxis();
        day.setDate("2017.09.12");
        day.setName("暑假");
        day.setDays(17);
        timeAxisList.add(day);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (fullImage == null || shouldUpdate) {

            if (ListUtils.isEmpty(timeAxisList)) {
                setVisibility(GONE);
                return;
            } else {
                setVisibility(VISIBLE);
            }

            fullImage = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas2 = new Canvas(fullImage);
            canvas2.drawColor(Color.TRANSPARENT);
            //画虚线
            canvas2.drawLine(0, getHeight() / 2, this.getWidth(), getHeight() / 2, linePaint);

            Rect rect = new Rect();

            textPaint.getTextBounds(timeAxisList.get(0).getDate(), 0, timeAxisList.get(0).getDate().length(), rect);

            float timeWidth = rect.width();
            float timeHeight = rect.height();

            int dp7 = DensityUtils.dp2px(context, 8f);

            float eachPaddintWidth = (getWidth() - 4 * timeWidth) / 4;

            for (int i = 0; i < timeAxisList.size(); i++) {
                TimeAxis day = timeAxisList.get(i);
                double startPoint = eachPaddintWidth * i + timeWidth * (i + 0.5);
                canvas2.drawCircle((float) startPoint, getHeight() / 2, 10, circlePaint);
                canvas2.drawText(day.getDate(), (float) (startPoint - timeWidth / 2), getHeight() / 2 - dp7, textPaint);
                Rect dateRect = new Rect();
                String name = day.getName() + day.getDays() + "天";
                textPaint.getTextBounds(name, 0, name.length(), dateRect);
                float nameWidth = dateRect.width();
                float nameHeight = dateRect.height();
                canvas2.drawText(name, (float) (startPoint - nameWidth / 2), getHeight() / 2 + dp7 + nameHeight, textPaint);
            }
            shouldUpdate = false;
        }
        canvas.drawBitmap(fullImage, 0, 0, null);
    }

    public void setDateLineData(List<TimeAxis> list) {
        timeAxisList.clear();
        timeAxisList.addAll(list);
        shouldUpdate = true;
        requestLayout();
    }

}

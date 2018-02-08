package cn.nicolite.huthelper.view.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by nicolite on 17-12-5.
 * 自适应高度的GridView
 */

public class MyGridView extends GridView {
    public MyGridView(Context context) {
        this(context, null);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

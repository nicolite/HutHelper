package cn.nicolite.huthelper.view.customView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by 高沛 on 16-9-10.
 */
public class PictureGridView extends GridView {
    public PictureGridView(Context context) {
        this(context, null);
    }

    public PictureGridView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PictureGridView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {

        heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightSpec);
    }
}

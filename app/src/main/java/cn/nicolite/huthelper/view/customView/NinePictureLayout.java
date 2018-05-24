package cn.nicolite.huthelper.view.customView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.model.Constants;


/**
 * Created by 高沛 on 2016/12/21.
 */

public class NinePictureLayout extends PictureLayout {
    private static final String TAG = "MyNinePictureLayout";
    protected static final int MAX_W_H_RATIO = 3;

    public NinePictureLayout(Context context) {
        super(context);
    }

    public NinePictureLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean displayOneImage(final RatioImageView imageView, String url, final int parentWidth) {

        Glide
                .with(context)
                .load(Constants.PICTURE_URL + url)
                .asBitmap()
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_error)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        imageView.setImageDrawable(placeholder);
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        int w = resource.getWidth();
                        int h = resource.getHeight();
                        int newW;
                        int newH;
                        if (h > w * MAX_W_H_RATIO) {//h:w = 5:3
                            newW = parentWidth / 2;
                            newH = newW * 5 / 3;
                        } else if (h < w) {//h:w = 2:3
                            newW = parentWidth * 2 / 3;
                            newH = newW * 2 / 3;
                        } else {//newH:h = newW :w
                            newW = parentWidth / 2;
                            newH = h * newW / w;
                        }
                        setOneImageLayoutParams(imageView, newW, newH);
                        imageView.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        imageView.setImageDrawable(errorDrawable);
                    }

                });
        return false;
    }

    @Override
    protected void displayImage(RatioImageView imageView, String url) {
        Glide
                .with(context)
                .load(Constants.PICTURE_URL + url)
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_error)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .crossFade()
                .into(imageView);
    }

}

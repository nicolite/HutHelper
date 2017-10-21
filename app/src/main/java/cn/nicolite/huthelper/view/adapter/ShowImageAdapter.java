package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.util.List;

import cn.nicolite.huthelper.R;


/**
 * Created by nicolite on 17-8-22.
 */

public class ShowImageAdapter extends PagerAdapter{
    private Context context;
    private List<String> images;

    public ShowImageAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images == null ? 0 : images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final PhotoView imageView = new PhotoView(context);
        final PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
        Glide
                .with(context)
                .load(images.get(position))
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_error)
                .skipMemoryCache(true)
                .crossFade()
                .centerCrop()
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        imageView.setImageDrawable(placeholder);
                        attacher.update();
                    }

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        imageView.setImageDrawable(resource);
                        attacher.update();
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        imageView.setImageDrawable(errorDrawable);
                        attacher.update();
                    }
                });
        container.addView(imageView);
        return imageView;
    }
}

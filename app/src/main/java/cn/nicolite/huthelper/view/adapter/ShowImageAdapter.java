package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
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
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.utils.ListUtils;


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
        return ListUtils.isEmpty(images) ? 0 : images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final PhotoView imageView = new PhotoView(context);
        final PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
        String url = images.get(position);

        if (!url.startsWith("http") || !url.startsWith("https")){
            url = Constants.PICTURE_URL + url;
        }

        Glide
                .with(context)
                .load(url)
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

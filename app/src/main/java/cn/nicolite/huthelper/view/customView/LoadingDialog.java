package cn.nicolite.huthelper.view.customView;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import cn.nicolite.huthelper.R;

/**
 * 加载对话框
 * Created by nicolite on 17-10-21.
 */

public class LoadingDialog {
    private Context context;
    private Dialog dialog;
    private ImageView loadingView;
    private View dialogContentView;
    private TextView loadingText;

    public LoadingDialog(Context context) {
        this.context = context;
        dialog = new Dialog(context, R.style.loading_dialog);
        dialogContentView = LayoutInflater.from(context).inflate(R.layout.loading_dialog, null);
        loadingView = (ImageView) dialogContentView.findViewById(R.id.img);
        loadingText = (TextView) dialogContentView.findViewById(R.id.text);
        dialog.setContentView(dialogContentView);
    }

    public LoadingDialog setLoadingText(String text){
        if (!TextUtils.isEmpty(text)){
            loadingText.setText(text);
        }
        return this;
    }

    public LoadingDialog setLoadingImage(String string){
        Glide
                .with(context)
                .load(string)
                .into(loadingView);
        return this;
    }

    public LoadingDialog setLoadingImage(Uri uri){
        Glide
                .with(context)
                .load(uri)
                .into(loadingView);
        return this;
    }

    public LoadingDialog setLoadingImage(File file){
        Glide
                .with(context)
                .load(file)
                .into(loadingView);
        return this;
    }

    public LoadingDialog setLoadingImage(Integer resourceId){
        Glide
                .with(context)
                .load(resourceId)
                .into(loadingView);
        return this;
    }

    public void setCanceledOnTouchOutside(boolean cancel){
        dialog.setCanceledOnTouchOutside(cancel);
    }

    public void setCancelable(boolean cancelable){
        dialog.setCancelable(cancelable);
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener listener){
        dialog.setOnCancelListener(listener);
    }

    public void show(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(loadingView, "rotation", 0, 360);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(800);
        animator.setRepeatCount(-1);
        animator.start();
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}

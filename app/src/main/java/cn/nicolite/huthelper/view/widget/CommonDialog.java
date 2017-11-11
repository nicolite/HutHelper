package cn.nicolite.huthelper.view.widget;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.nicolite.huthelper.R;

/**
 * 自定义对话框
 * Created by nicolite on 17-9-14.
 */

public class CommonDialog {

    private AlertDialog.Builder builder;
    private View view;
    private TextView tvMessage;
    private Button btOk;
    private Button btCancel;
    private AlertDialog dialog;
    private TextView tvTitle;
    private TextInputEditText inputArea;
    private TextInputLayout inputRoot;
    private LinearLayout tvMessageRoot;

    public CommonDialog(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_common, null, false);
        tvMessage = (TextView) view.findViewById(R.id.tv_message);
        btOk = (Button) view.findViewById(R.id.bt_ok);
        btCancel = (Button) view.findViewById(R.id.bt_cancel);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvMessageRoot = (LinearLayout) view.findViewById(R.id.tv_message_root);

        inputArea = (TextInputEditText) view.findViewById(R.id.input_area);
        inputRoot = (TextInputLayout) view.findViewById(R.id.input_root);

        inputRoot.setVisibility(View.GONE);
        tvMessageRoot.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        tvMessage.setVisibility(View.GONE);
        btOk.setVisibility(View.GONE);
        btCancel.setVisibility(View.GONE);

        builder = new AlertDialog.Builder(context)
                .setView(view);
    }

    public CommonDialog setTitle(String text) {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(text);
        return this;
    }

    public CommonDialog setTitleGravity(int gravity){
        tvTitle.setGravity(gravity);
        return this;
    }

    public CommonDialog setMessage(String text) {
        tvMessageRoot.setVisibility(View.VISIBLE);
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvMessage.setText(text);
        return this;
    }

    public CommonDialog setMessageGravity(int gravity){
        tvMessage.setGravity(gravity);
        return this;
    }

    public CommonDialog setPositiveButton(final String text, View.OnClickListener onClickListener) {
        btOk.setVisibility(View.VISIBLE);
        btOk.setText(text);
        if (onClickListener != null) {
            btOk.setOnClickListener(onClickListener);
        } else {
            btOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }

        return this;
    }

    public CommonDialog setNegativeButton(String text, View.OnClickListener onClickListener) {
        btCancel.setVisibility(View.VISIBLE);
        btCancel.setText(text);
        if (onClickListener != null) {
            btCancel.setOnClickListener(onClickListener);
        } else {
            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        }
        return this;
    }

    public CommonDialog setInput() {
        inputRoot.setVisibility(View.VISIBLE);
        return this;
    }

    public CommonDialog setInputGravity(int gravity){
        inputRoot.setGravity(gravity);
        return this;
    }

    public String getInputText() {
        return inputArea.getText().toString();
    }

    public CommonDialog setCancelable(boolean flag){
        if (dialog == null){
            dialog = builder.create();
        }
        dialog.setCancelable(flag);
        return this;
    }

    public CommonDialog setCanceledOnTouchOutside(boolean flag){
        if (dialog == null){
            dialog = builder.create();
        }
        dialog.setCanceledOnTouchOutside(flag);
        return this;
    }

    public void show() {
        if (dialog == null){
            dialog = builder.create();
        }

        dialog.show();

    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}

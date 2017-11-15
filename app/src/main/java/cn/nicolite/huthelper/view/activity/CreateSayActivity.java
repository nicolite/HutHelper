package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.view.iview.ICreateSayView;

/**
 * Created by nicolite on 17-11-15.
 */

public class CreateSayActivity extends BaseActivity implements ICreateSayView {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.et_addsay_content)
    TextInputEditText etAddsayContent;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
        setDeepColorStatusBar(true);
        setSlideExit(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_create_say;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("发说说");
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_menu, R.id.add_pic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_menu:
                break;
            case R.id.add_pic:
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void closeLoading() {

    }

    @Override
    public void showMessage(String msg) {

    }
}

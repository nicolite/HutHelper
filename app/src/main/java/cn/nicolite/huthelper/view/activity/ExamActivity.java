package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.iview.IExamView;

/**
 * 考试查询界面
 * Created by nicolite on 17-11-1.
 */

public class ExamActivity extends BaseActivity implements IExamView {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.rv_exam)
    RecyclerView rvExam;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.rl_empty)
    LinearLayout rlEmpty;
    @BindView(R.id.rootView)
    LinearLayout rootView;

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
        return R.layout.activity_exam;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("考试查询");
    }


    @OnClick({R.id.toolbar_back, R.id.toolbar_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_menu:
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
        SnackbarUtils.showShortSnackbar(rootView, msg);
    }
}

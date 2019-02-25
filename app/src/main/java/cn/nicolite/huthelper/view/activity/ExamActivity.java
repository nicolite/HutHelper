package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.model.bean.Exam;
import cn.nicolite.huthelper.presenter.ExamPresenter;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.adapter.ExamAdapter;
import cn.nicolite.huthelper.view.iview.IExamView;
import cn.nicolite.huthelper.view.customView.LoadingDialog;

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
    private ExamPresenter examPresenter;
    private LoadingDialog loadingDialog;
    private List<Exam> examList = new ArrayList<>();
    private ExamAdapter adapter;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar();
        setDeepColorStatusBar();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_exam;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("考试计划");
        rvExam.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.VERTICAL, false));
        adapter = new ExamAdapter(context, examList);
        rvExam.setAdapter(adapter);
        examPresenter = new ExamPresenter(this, this);
        examPresenter.showExam(false);
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_menu:
                examPresenter.showExam(true);
                break;
        }
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null){
            loadingDialog = new LoadingDialog(context)
                    .setLoadingText("查询中...");
        }
        loadingDialog.show();
    }

    @Override
    public void closeLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg) {
        SnackbarUtils.showShortSnackbar(rootView, msg);
    }

    @Override
    public void showExam(List<Exam> examList) {
        this.examList.clear();
        this.examList.addAll(examList);
        adapter.notifyDataSetChanged();
    }
}

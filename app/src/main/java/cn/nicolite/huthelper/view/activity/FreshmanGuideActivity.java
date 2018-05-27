package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseActivity;
import cn.nicolite.huthelper.model.bean.FreshmanGuide;
import cn.nicolite.huthelper.presenter.FreshmanGuidePresenter;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.adapter.FreshmanGuideAdapter;
import cn.nicolite.huthelper.view.iview.IFreshmanGuideView;
import cn.nicolite.huthelper.view.customView.LoadingDialog;


/**
 * Created by nicolite on 17-8-21.
 */

public class FreshmanGuideActivity extends BaseActivity implements IFreshmanGuideView {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    private List<FreshmanGuide> dataList = new ArrayList<>();
    private FreshmanGuideAdapter adapter;
    private FreshmanGuidePresenter freshmanGuidePresenter;
    private LoadingDialog loadingDialog;

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
        return R.layout.activity_freshman_guide;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("新生攻略");
        recyclerView.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.VERTICAL, false));
        adapter = new FreshmanGuideAdapter(context, dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, OrientationHelper.VERTICAL));
        freshmanGuidePresenter = new FreshmanGuidePresenter(this, this);
        freshmanGuidePresenter.showGuideList();
    }

    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null){
            loadingDialog = new LoadingDialog(context)
                    .setLoadingText("加载中...");
        }
        loadingDialog.show();
    }

    @Override
    public void closeLoading() {
        if (loadingDialog != null){
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg) {
        SnackbarUtils.showShortSnackbar(rootView, msg);
    }

    @Override
    public void showGuideList(List<FreshmanGuide> list) {
        dataList.clear();
        dataList.addAll(list);
        adapter.notifyDataSetChanged();
    }
}

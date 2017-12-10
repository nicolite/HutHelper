package cn.nicolite.huthelper.view.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.presenter.CreateSayPresenter;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.adapter.ImageAdapter;
import cn.nicolite.huthelper.view.iview.ICreateSayView;
import cn.nicolite.huthelper.view.widget.CommonDialog;

/**
 * Created by nicolite on 17-11-15.
 */

public class CreateSayActivity extends BaseActivity implements ICreateSayView {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.et_add_Say_content)
    TextInputEditText etAddSayContent;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    private CreateSayPresenter createSayPresenter;
    private final int REQUEST_CODE_CHOOSE = 111;
    private List<Uri> uriList = new ArrayList<>();
    private ImageAdapter adapter;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false));
        adapter = new ImageAdapter(context, uriList, ImageAdapter.URI);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position, long itemId) {
                final CommonDialog commonDialog = new CommonDialog(context);
                commonDialog
                        .setMessage("确认删除这张图片？")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                commonDialog.dismiss();
                                uriList.remove(uriList.get(position));
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("不了", null)
                        .show();
            }
        });
        createSayPresenter = new CreateSayPresenter(this, this);
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_menu, R.id.add_pic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_menu:
                final CommonDialog commonDialog = new CommonDialog(context);
                commonDialog.setMessage("确认提交？")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (ListUtils.isEmpty(uriList)) {
                                    createSayPresenter.uploadSayInfo(etAddSayContent.getText().toString(), "");
                                } else {
                                    createSayPresenter.createSay(activity, uriList);
                                }
                                commonDialog.dismiss();
                            }
                        })
                        .setNegativeButton("再改改", null)
                        .show();
                break;
            case R.id.add_pic:
                createSayPresenter.selectImages();
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

    @Override
    public void selectImages() {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .countable(true)
                .maxSelectable(4 - uriList.size())
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.80f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    public void uploadSayInfo(String imagesInfo) {
        createSayPresenter.uploadSayInfo(etAddSayContent.getText().toString(), imagesInfo);
    }

    @Override
    public void publishSuccess() {
        setResult(Constants.PUBLISH);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            uriList.addAll(Matisse.obtainResult(data));
            adapter.notifyDataSetChanged();
        }
    }
}

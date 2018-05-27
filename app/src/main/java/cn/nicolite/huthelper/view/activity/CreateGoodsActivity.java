package cn.nicolite.huthelper.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseActivity;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.presenter.CreateGoodsPresenter;
import cn.nicolite.huthelper.utils.KeyBoardUtils;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.adapter.ImageAdapter;
import cn.nicolite.huthelper.view.iview.ICreateGoodsView;
import cn.nicolite.huthelper.view.customView.CommonDialog;

/**
 * Created by nicolite on 17-11-11.
 */

public class CreateGoodsActivity extends BaseActivity implements ICreateGoodsView {

    @BindView(R.id.rv_goods_images)
    RecyclerView rvGoodsImages;
    @BindView(R.id.tv_text_title)
    EditText tvTextLostTitle;
    @BindView(R.id.tv_text_content)
    EditText tvTextLost;
    @BindView(R.id.tv_goods_price)
    EditText tvGoodsPrice;
    @BindView(R.id.tv_goods_quality)
    TextView tvGoodsQuality;
    @BindView(R.id.tv_goods_tel)
    EditText tvGoodsTel;
    @BindView(R.id.tv_goods_location)
    EditText tvGoodsLocation;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    private static final int SOLD = 1;
    private static final int BUY = 2;
    @BindView(R.id.toolbar_select_1)
    RadioButton toolbarSelect1;
    @BindView(R.id.toolbar_select_2)
    RadioButton toolbarSelect2;

    private int type = SOLD;
    private CreateGoodsPresenter createGoodsPresenter;
    private final int REQUEST_CODE_CHOOSE = 111;
    private List<Uri> uriList = new ArrayList<>();
    @BindArray(R.array.goodsqu)
    String[] goodsQuality;
    private int goodsQuaSelected = 0;
    private ImageAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
        setSlideExit(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_create_goods;
    }

    @Override
    protected void doBusiness() {
        toolbarSelect1.setText("出售");
        toolbarSelect2.setText("求购");
        rvGoodsImages.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false));
        adapter = new ImageAdapter(context, uriList, ImageAdapter.URI);
        rvGoodsImages.setAdapter(adapter);
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

        createGoodsPresenter = new CreateGoodsPresenter(this, this);
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_select_1, R.id.toolbar_select_2,
            R.id.toolbar_ok, R.id.tv_goods_quality, R.id.add_pic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_select_1:
                type = SOLD;
                break;
            case R.id.toolbar_select_2:
                type = BUY;
                break;
            case R.id.toolbar_ok:
                KeyBoardUtils.hideSoftInput(context, getWindow());
                final CommonDialog commonDialog = new CommonDialog(context);
                commonDialog
                        .setMessage("确认提交？")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                commonDialog.dismiss();
                                if (TextUtils.isEmpty(tvTextLostTitle.getText().toString())) {
                                    showMessage("没有填写标题");
                                } else if (TextUtils.isEmpty(tvTextLost.getText().toString())) {
                                    showMessage("没有填写描述");
                                } else if (TextUtils.isEmpty(tvGoodsTel.getText().toString())) {
                                    showMessage("没有填联系方式");
                                } else if (TextUtils.isEmpty(tvGoodsQuality.getText().toString())) {
                                    showMessage("没有选商品成色");
                                } else if (TextUtils.isEmpty(tvGoodsLocation.getText().toString())) {
                                    showMessage("没有填发布区域");
                                } else if (TextUtils.isEmpty(tvGoodsPrice.getText().toString())) {
                                    showMessage("没有填写价格");
                                } else {
                                    if (!ListUtils.isEmpty(uriList)) {
                                        createGoodsPresenter.createGoods(activity, uriList);
                                    } else {
                                        showMessage("至少选择一张图片！");
                                    }
                                }
                            }
                        })
                        .setNegativeButton("再改改", null)
                        .show();
                break;
            case R.id.tv_goods_quality:
                new AlertDialog.Builder(this)
                        .setTitle("请选择成色")
                        .setSingleChoiceItems(goodsQuality, goodsQuaSelected, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                goodsQuaSelected = i;
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                tvGoodsQuality.setText(goodsQuality[goodsQuaSelected]);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.add_pic:
                createGoodsPresenter.selectImages();
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
    public void uploadGoodsInfo(String imagesInfo) {
        createGoodsPresenter.uploadGoodsInfo(type, tvTextLostTitle.getText().toString(),
                tvTextLost.getText().toString(), tvGoodsPrice.getText().toString(), goodsQuaSelected + 1,
                tvGoodsTel.getText().toString(), tvGoodsLocation.getText().toString(), imagesInfo);
    }

    @Override
    public void uploadProgress(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        progressDialog.setMessage(msg);
    }

    @Override
    public void uploadFailure(String msg) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void publishSuccess() {

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

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

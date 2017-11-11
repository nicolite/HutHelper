package cn.nicolite.huthelper.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.GoodsItem;
import cn.nicolite.huthelper.presenter.GoodsInfoPresenter;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.utils.ToastUtil;
import cn.nicolite.huthelper.view.adapter.ImageAdapter;
import cn.nicolite.huthelper.view.iview.IGoodsInfoView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;

/**
 * 商品详情页
 * Created by nicolite on 17-11-9.
 */

public class GoodsInfoActivity extends BaseActivity implements IGoodsInfoView {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_user)
    ImageView toolbarUser;
    @BindView(R.id.toolbar_delete)
    ImageView toolbarDelete;
    @BindView(R.id.tv_text_title)
    TextView tvTextLostTitle;
    @BindView(R.id.tv_text_content)
    TextView tvTextLost;
    @BindView(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @BindView(R.id.tv_goods_quality)
    TextView tvGoodsQuality;
    @BindView(R.id.tv_goods_tel)
    TextView tvGoodsTel;
    @BindView(R.id.tv_goods_location)
    TextView tvGoodsLocation;
    @BindView(R.id.iv_num_lost)
    TextView ivNumLost;
    @BindView(R.id.tv_sendtime_lost)
    TextView tvSendtimeLost;
    @BindView(R.id.rootView)
    CoordinatorLayout rootView;
    @BindView(R.id.iv_bgimage)
    ImageView ivBgimage;
    @BindView(R.id.rv_goods_images)
    RecyclerView recyclerView;
    private String userId;
    private boolean delete;
    private GoodsInfoPresenter goodsInfoPresenter;
    private String goodsId;
    private String phone;
    private List<String> imageList = new ArrayList<>();
    private ImageAdapter adapter;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
        setLayoutNoLimits(true);
        setSlideExit(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {
        if (bundle != null) {
            goodsId = bundle.getString("goodsId", null);
            userId = bundle.getString("userId", null);
            delete = bundle.getBoolean("delete", false);
            if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(goodsId)) {
                ToastUtil.showToastShort("获取信息失败！");
                finish();
            }
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_goods_info;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("商品详情");
        if (delete) {
            toolbarUser.setVisibility(View.GONE);
            toolbarDelete.setVisibility(View.VISIBLE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false));
        adapter = new ImageAdapter(context, imageList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long itemId) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("images", (ArrayList<String>) imageList);
                bundle.putInt("curr", position);
                startActivity(ShowImageActivity.class, bundle);
            }
        });
        goodsInfoPresenter = new GoodsInfoPresenter(this, this);
        goodsInfoPresenter.showGoodsInfo(goodsId);
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_user, R.id.toolbar_delete, R.id.tv_goods_tel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_user:
                break;
            case R.id.toolbar_delete:
                break;
            case R.id.tv_goods_tel:
                if (!TextUtils.isEmpty(phone) && TextUtils.isDigitsOnly(tvGoodsTel.getText().toString())) {
                    Intent dial = new Intent(Intent.ACTION_DIAL);
                    dial.setData(Uri.parse("tel:" + phone));
                    dial.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dial);
                }
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
    public void showGoodsInfo(GoodsItem goodsItem) {
        phone = goodsItem.getPhone();
        imageList.clear();
        imageList.addAll(goodsItem.getPics());
        tvSendtimeLost.setText(goodsItem.getCreated_on());
        tvTextLostTitle.setText(goodsItem.getTit());
        tvGoodsQuality.setText(goodsItem.getAttr());
        ivNumLost.setText(goodsItem.getUsername());
        tvGoodsPrice.setText(String.valueOf("￥" + goodsItem.getPrize()));
        tvGoodsTel.setText(TextUtils.isEmpty(phone) ? "无联系方式" : phone);
        tvGoodsLocation.setText(TextUtils.isEmpty(goodsItem.getAddress()) ? "湖工大" : goodsItem.getAddress());

        if (!ListUtils.isEmpty(imageList)) {
            Glide
                    .with(this)
                    .load(Constants.PICTURE_URL + imageList.get(0))
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.blur_plac_min)
                    .error(R.drawable.blur_plac_min)
                    .bitmapTransform(new BlurTransformation(context, 100), new ColorFilterTransformation(this, 0x29000000))
                    .crossFade()
                    .into(ivBgimage);
            adapter.notifyDataSetChanged();
        }
    }

}

package cn.nicolite.huthelper.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.LostAndFound;
import cn.nicolite.huthelper.presenter.LostAndFoundInfoPresenter;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.utils.ToastUtil;
import cn.nicolite.huthelper.view.adapter.ImageAdapter;
import cn.nicolite.huthelper.view.iview.ILostAndFoundInfoView;
import cn.nicolite.huthelper.view.customView.CommonDialog;

/**
 * Created by nicolite on 17-11-12.
 */

public class LostAndFoundInfoActivity extends BaseActivity implements ILostAndFoundInfoView {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_user)
    ImageView toolbarUser;
    @BindView(R.id.toolbar_delete)
    ImageView toolbarDelete;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_text_content)
    TextView tvTextContent;
    @BindView(R.id.tv_thing_title)
    TextView tvThingTitle;
    @BindView(R.id.tv_thing)
    TextView tvThing;
    @BindView(R.id.tv_time_title)
    TextView tvTimeTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_location_title)
    TextView tvLocationTitle;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_phone_title)
    TextView tvPhoneTitle;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.iv_num_lost)
    TextView ivNumLost;
    @BindView(R.id.tv_sendtime_lost)
    TextView tvSendtimeLost;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    private boolean delete = false;
    private LostAndFound lostAndFound;
    private final int[] bg = {R.drawable.bg_list, R.drawable.bg2_list, R.drawable.bg3_list, R.drawable.bg4_list};
    private LostAndFoundInfoPresenter lostAndFoundInfoPresenter;
    private int position;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
        setSlideExit(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {
        if (bundle != null) {
            delete = bundle.getBoolean("remove", false);
            position = bundle.getInt("position", -1);
            lostAndFound = (LostAndFound) bundle.getSerializable("data");
            if (lostAndFound == null || position == -1) {
                ToastUtil.showToastShort("获取信息失败！");
                finish();
            }
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_lost_and_found_info;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("失物详情");
        if (delete || lostAndFound.getUser_id().equals(userId)) {
            toolbarUser.setVisibility(View.GONE);
            toolbarDelete.setVisibility(View.VISIBLE);
        }

        switch (lostAndFound.getType()) {
            case "1":
                break;
            case "2":
                tvThingTitle.setText("丢失物品");
                tvTimeTitle.setText("丢失时间");
                tvLocationTitle.setText("丢失地点");
                break;
            default:
        }

        int num = lostAndFound.getContent().length() % 4;
        rootView.setBackgroundResource(bg[num]);

        tvTextContent.setText(lostAndFound.getContent());
        tvThing.setText(lostAndFound.getTit());
        tvTime.setText(lostAndFound.getTime());
        tvLocation.setText(lostAndFound.getLocate());
        tvPhone.setText(lostAndFound.getPhone());
        ivNumLost.setText(lostAndFound.getUsername());
        tvSendtimeLost.setText(lostAndFound.getCreated_on());

        final List<String> pics = lostAndFound.getPics();
        if (!ListUtils.isEmpty(pics)) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false));
            ImageAdapter adapter = new ImageAdapter(context, pics);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, long itemId) {
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("images", (ArrayList<String>) pics);
                    bundle.putInt("curr", position);
                    startActivity(ShowImageActivity.class, bundle);
                }
            });
        }

        lostAndFoundInfoPresenter = new LostAndFoundInfoPresenter(this, this);
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_user, R.id.toolbar_delete, R.id.tv_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_user:
                Bundle bundle = new Bundle();
                bundle.putString("userId", lostAndFound.getUser_id());
                bundle.putString("username", lostAndFound.getUsername());
                startActivity(UserInfoCardActivity.class, bundle);
                break;
            case R.id.toolbar_delete:
                final CommonDialog commonDialog = new CommonDialog(context);
                commonDialog
                        .setMessage("确认删除？")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                lostAndFoundInfoPresenter.deleteLostAndFound(lostAndFound.getId());
                                commonDialog.dismiss();
                            }
                        })
                        .setNegativeButton("不了", null)
                        .show();
                break;
            case R.id.tv_phone:
                if (!TextUtils.isEmpty(lostAndFound.getPhone()) && TextUtils.isDigitsOnly(lostAndFound.getPhone())) {
                    Intent dial = new Intent(Intent.ACTION_DIAL);
                    dial.setData(Uri.parse("tel:" + lostAndFound.getPhone()));
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
    public void deleteSuccess() {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        setResult(Constants.DELETE, intent);
        finish();
    }
}

package cn.nicolite.huthelper.view.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseActivity;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.utils.DensityUtils;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.customView.CommonDialog;
import cn.nicolite.huthelper.view.iview.IUserInfoView;
import cn.nicolite.huthelper.view.presenter.UserInfoPresenter;


/**
 * 用户信息界面
 * Created by nicolite on 17-10-28.
 */

public class UserInfoActivity extends BaseActivity implements IUserInfoView {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.iv_user_headview)
    ImageView ivUserHeadview;
    @BindView(R.id.rl_user_headview)
    RelativeLayout rlUserHeadview;
    @BindView(R.id.tv_user_nickname)
    TextView tvUserNickname;
    @BindView(R.id.tv_user_password)
    TextView tvUserPassword;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.iv_user_gender)
    ImageView ivUserGender;
    @BindView(R.id.tv_user_num)
    TextView tvUserNum;
    @BindView(R.id.tv_user_school)
    TextView tvUserSchool;
    @BindView(R.id.tv_user_class)
    TextView tvUserClass;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.tv_user_bio)
    TextView tvUserBio;
    private UserInfoPresenter userInfoPresenter;
    private final int REQUEST_CODE_CHOOSE = 111;
    private final int REQUEST_CODE_CUT = 222;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar();
        setDeepColorStatusBar();
        setSlideExit();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("个人信息");
        userInfoPresenter = new UserInfoPresenter(this, this);
        userInfoPresenter.showUserData();
    }

    @OnClick({R.id.toolbar_back, R.id.rl_user_headview, R.id.rl_user_nickname,
            R.id.rl_user_password, R.id.user_logout, R.id.rl_user_bio})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.rl_user_headview:
                userInfoPresenter.changAvatar();
                break;
            case R.id.rl_user_nickname:
                final CommonDialog commonDialog = new CommonDialog(context);
                commonDialog
                        .setTitle("请输入新的昵称")
                        .setInput()
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String inputText = commonDialog.getInputText();
                                userInfoPresenter.changeUserName(inputText);
                                tvUserNickname.setText(inputText);
                                commonDialog.dismiss();
                            }
                        })
                        .setNegativeButton("不改了", null)
                        .show();
                break;
            case R.id.rl_user_password:
                showMessage("开源版暂不提供此功能");
                break;
            case R.id.user_logout:
                startActivity(LoginActivity.class);
                finish();
                break;

            case R.id.rl_user_bio:
                final CommonDialog commonDialog2 = new CommonDialog(context);
                commonDialog2
                        .setTitle("请输入新的签名")
                        .setInput()
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String inputText = commonDialog2.getInputText();
                                userInfoPresenter.changeBio(inputText);
                                tvUserBio.setText(inputText);
                                commonDialog2.dismiss();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
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
        SnackbarUtils.INSTANCE.showShortSnackbar(rootView, msg);
    }

    @Override
    public void showUserInfo(User user) {
        tvUserNickname.setText(user.getUsername());
        tvUserName.setText(user.getTrueName());
        ivUserGender.setImageResource(user.getSex().equals("男") ? R.drawable.male : R.drawable.female);
        tvUserSchool.setText(user.getDep_name());
        tvUserNum.setText(user.getStudentKH());
        tvUserClass.setText(user.getClass_name());
        tvUserBio.setText(TextUtils.isEmpty(user.getBio()) ? "没有签名" : user.getBio());
        if (!TextUtils.isEmpty(user.getHead_pic_thumb())) {
            int width = DensityUtils.dp2px(context, 40);
            Glide
                    .with(this)
                    .load(Constants.PICTURE_URL + user.getHead_pic_thumb())
                    .override(width, width)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .into(ivUserHeadview);
        }
    }

    @Override
    public void changeAvatarSuccess(Bitmap bitmap) {
        ivUserHeadview.setImageBitmap(bitmap);
        Intent intent = new Intent(Constants.MainBroadcast);
        Bundle bundle = new Bundle();
        bundle.putInt("type", Constants.BROADCAST_TYPE_REFRESH_AVATAR);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public void changeAvatar() {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .countable(true)
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> uriList = Matisse.obtainResult(data);
            if (!ListUtils.isEmpty(uriList)) {
                Uri uri = uriList.get(0); //TODO don't work on Android 5.0.2 MIUI9 ReadMi Note2
                Intent intent = new Intent();
                intent.setAction("com.android.camera.action.CROP");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);// 裁剪框比例
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 150);// 输出图片大小
                intent.putExtra("outputY", 150);
                intent.putExtra("return-data", true);

                startActivityForResult(intent, REQUEST_CODE_CUT);
            }
        } else if (requestCode == REQUEST_CODE_CUT && resultCode == RESULT_OK) {
            Bitmap bitmap = data.getParcelableExtra("data");
            userInfoPresenter.uploadAvatar(bitmap);
        } else {
            userInfoPresenter.showUserData();
        }
    }

}

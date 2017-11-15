package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.presenter.SearchPresenter;
import cn.nicolite.huthelper.presenter.UserInfoCardPresenter;
import cn.nicolite.huthelper.utils.ToastUtil;
import cn.nicolite.huthelper.view.iview.IUserInfoCardView;
import io.rong.imkit.RongIM;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by nicolite on 17-11-11.
 */

public class UserInfoCardActivity extends BaseActivity implements IUserInfoCardView {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_bio)
    TextView tvUserBio;
    @BindView(R.id.tv_user_class)
    TextView tvUserClass;
    @BindView(R.id.tv_user_department)
    TextView tvUserDepartment;
    private UserInfoCardPresenter userInfoCardPresenter;
    private String mUserId;
    private String username;


    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
        setSlideExit(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {
        if (bundle != null) {
            mUserId = bundle.getString("userId", null);
            username = bundle.getString("username", null);

            if (TextUtils.isEmpty(mUserId) || TextUtils.isEmpty(username)) {
                ToastUtil.showToastShort("获取信息失败！");
                finish();
            }
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_user_info_card;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText(username);
        tvUserName.setText(username);
        userInfoCardPresenter = new UserInfoCardPresenter(this, this);
        userInfoCardPresenter.showInfo(mUserId);
    }

    @OnClick({R.id.toolbar_back, R.id.bt_user_chat, R.id.iv_user_shiwu, R.id.iv_user_shuoshuo, R.id.iv_user_ershou})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.bt_user_chat:
                RongIM.getInstance().startPrivateChat(this, userId, username);
                break;
            case R.id.iv_user_shiwu:
                Bundle sBundle = new Bundle();
                sBundle.putInt("type", SearchPresenter.TYPE_MYLOSTANDFOUND);
                sBundle.putString("searchText", mUserId);
                sBundle.putString("extras", username);
                startActivity(SearchResultActivity.class, sBundle);
                break;
            case R.id.iv_user_shuoshuo:
                Bundle oBundle = new Bundle();
                oBundle.putInt("type", SearchPresenter.TYPE_MYSAY);
                oBundle.putString("searchText", mUserId);
                oBundle.putString("extras", username);
                startActivity(SearchResultActivity.class, oBundle);
                break;
            case R.id.iv_user_ershou:
                Bundle eBundle = new Bundle();
                eBundle.putInt("type", SearchPresenter.TYPE_MARKET_MYGOODS);
                eBundle.putString("searchText", mUserId);
                eBundle.putString("extras", username);
                startActivity(SearchResultActivity.class, eBundle);
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

    @Override
    public void showInfo(User user) {
        Glide
                .with(activity)
                .load(Constants.PICTURE_URL + user.getHead_pic_thumb())
                .bitmapTransform(new CropCircleTransformation(UserInfoCardActivity.this))
                .dontAnimate()
                .skipMemoryCache(true)
                .into(ivUserAvatar);

        tvUserName.setText(username);
        tvUserBio.setText("");
        tvUserClass.setText(user.getClass_name().replaceAll("\\d+", ""));
        tvUserDepartment.setText(user.getDep_name());
    }
}

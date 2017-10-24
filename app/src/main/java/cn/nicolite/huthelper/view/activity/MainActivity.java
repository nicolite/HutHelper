package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.android.tpush.XGPushManager;
import com.tencent.bugly.beta.Beta;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.model.bean.Menu;
import cn.nicolite.huthelper.model.bean.TimeAxis;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.model.bean.Weather;
import cn.nicolite.huthelper.presenter.MainPresenter;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.iview.IMainView;
import cn.nicolite.huthelper.view.widget.CommonDialog;
import cn.nicolite.huthelper.view.widget.DateLineView;
import cn.nicolite.huthelper.view.widget.DragLayout;
import cn.nicolite.huthelper.view.widget.RichTextView;
import io.rong.imkit.RongIM;

/**
 * 主页
 */
public class MainActivity extends BaseActivity implements IMainView {

    @BindView(R.id.unReadMessage)
    FrameLayout unReadMessage;
    @BindView(R.id.tv_wd_temp)
    TextView tvWdTemp;
    @BindView(R.id.tv_wd_location)
    TextView tvWdLocation;
    @BindView(R.id.tv_course_maincontent)
    TextView tvCourseMaincontent;
    @BindView(R.id.tv_date_maincontent)
    TextView tvDateMaincontent;
    @BindView(R.id.iv_dateline)
    ImageView ivDateline;
    @BindView(R.id.dateLineView)
    DateLineView dateLineView;
    @BindView(R.id.mu_tongzhi)
    ImageView muTongzhi;
    @BindView(R.id.tv_tongzhi_title)
    TextView tvTongzhiTitle;
    @BindView(R.id.tv_notice_maincontent)
    RichTextView tvNoticeMaincontent;
    @BindView(R.id.tv_tongzhi_contont)
    TextView tvTongzhiContont;
    @BindView(R.id.tv_tongzhi_time)
    TextView tvTongzhiTime;
    @BindView(R.id.rl_main_tongzhi)
    RelativeLayout rlMainTongzhi;
    @BindView(R.id.rv_main_menu)
    RecyclerView rvMainMenu;
    @BindView(R.id.rootView)
    DragLayout rootView;
    private MainPresenter mainPresenter;
    private User user;
    private long exitTime = 0;
    @Override
    protected void initConfig(Bundle savedInstanceState) {
        hideToolBar(true);
        setDeepColorStatusBar(true);
        setImmersiveStatusBar(true);
        setLayoutNoLimits(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void doBusiness() {
        user = boxHelper.getUserBox().get(1);
        rootView.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {

            }

            @Override
            public void onClose() {

            }

            @Override
            public void onDrag(float percent) {

            }
        });
        mainPresenter = new MainPresenter(this, this);
        mainPresenter.showMenu();
        mainPresenter.showDateLine();
        mainPresenter.showWeather();
        mainPresenter.initPush(user.getStudentKH());
    }

    @OnClick({R.id.iv_nav_avatar, R.id.tv_nav_name, R.id.tv_nav_private_message,
            R.id.tv_nav_update, R.id.tv_nav_share, R.id.tv_nav_logout, R.id.tv_nav_about,
            R.id.tv_nav_fback, R.id.imgbtn_menusetting, R.id.imgbtn_bell})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_nav_avatar:
                break;
            case R.id.tv_nav_name:
                break;
            case R.id.tv_nav_private_message:
                break;
            case R.id.tv_nav_update:
                Beta.checkUpgrade();
                break;
            case R.id.tv_nav_share:
                mainPresenter.share();
                break;
            case R.id.tv_nav_logout:
                final CommonDialog commonDialog = new CommonDialog(context);
                commonDialog
                        .setMessage("确定退出？")
                        .setPositiveButton("是的", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RongIM.getInstance().logout();
                                XGPushManager.deleteTag(context, user.getStudentKH());
                                XGPushManager.registerPush(context, "*");
                                XGPushManager.unregisterPush(context);
                                //TODO 删除登录用户信息
                                startActivity(LoginActivity.class);
                                finish();
                                commonDialog.dismiss();
                            }
                        })
                        .setNegativeButton("再想想", null)
                        .show();
                break;
            case R.id.tv_nav_about:
                startActivity(AboutActivity.class);
                break;
            case R.id.tv_nav_fback:
                startActivity(FeedBackActivity.class);
                break;
            case R.id.imgbtn_menusetting:
                rootView.open();
                break;
            case R.id.imgbtn_bell:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000){
            SnackbarUtils.showShortSnackbar(rootView, "再按一次返回键退出");
            exitTime = System.currentTimeMillis();
        }else {
            super.onBackPressed();
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
    public void showWeather(Weather weather) {
        tvWdLocation.setText(weather.getData().getCity() + "|" + weather.getData().getForecast().get(0).getType());
        tvWdTemp.setText(String.valueOf(weather.getData().getWendu() + "℃"));
    }

    @Override
    public void showDateLine(List<TimeAxis> timeAxisList) {
        dateLineView.setDateLineData(timeAxisList);
    }

    @Override
    public void showNotification() {

    }

    @Override
    public void showSyllabus() {

    }

    @Override
    public void showMenu(List<Menu> menuList) {

    }

}

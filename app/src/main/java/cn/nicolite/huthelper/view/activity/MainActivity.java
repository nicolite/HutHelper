package cn.nicolite.huthelper.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.bugly.beta.Beta;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.manager.ActivityStackManager;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.huthelper.model.bean.Menu;
import cn.nicolite.huthelper.model.bean.TimeAxis;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.presenter.MainPresenter;
import cn.nicolite.huthelper.utils.ButtonUtils;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.adapter.MenuAdapter;
import cn.nicolite.huthelper.view.iview.IMainView;
import cn.nicolite.huthelper.view.widget.CommonDialog;
import cn.nicolite.huthelper.view.widget.DateLineView;
import cn.nicolite.huthelper.view.widget.DragLayout;
import cn.nicolite.huthelper.view.widget.RichTextView;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

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
    @BindView(R.id.iv_nav_avatar)
    ImageView ivNavAvatar;
    @BindView(R.id.tv_nav_name)
    RichTextView tvNavName;
    private MainPresenter mainPresenter;
    private User user;
    private long exitTime = 0;
    private List<Menu> menuList = new ArrayList<>();
    private MenuAdapter menuAdapter;
    private boolean isOpen;
    private QBadgeView qBadgeView;
    private Configure configure;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        hideToolBar(true);
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
        String userId = getLoginUser();
        if (TextUtils.isEmpty(userId)) {
            startActivity(LoginActivity.class);
            finish();
        }

        List<Configure> configureList = getConfigureList();
        if (ListUtils.isEmpty(configureList)) {
            startActivity(LoginActivity.class);
            finish();
        }

        configure = configureList.get(0);
        user = configure.getUser();

        rootView.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {
                isOpen = true;
            }

            @Override
            public void onClose() {
                isOpen = false;
            }

            @Override
            public void onDrag(float percent) {

            }
        });
        menuAdapter = new MenuAdapter(context, menuList);
        rvMainMenu.setAdapter(menuAdapter);
        rvMainMenu.setLayoutManager(new GridLayoutManager(context, 4, OrientationHelper.VERTICAL, false));
        menuAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!ButtonUtils.isFastDoubleClick()) {
                    try {
                        Menu menu = menuList.get(position);
                        Bundle bundle = new Bundle();
                        if (menu.getType() == WebViewActivity.TYPE_LIBRARY) {
                            String url = Constants.LIBRARY;
                            if (!TextUtils.isEmpty(configure.getLibraryUrl())) {
                                url = configure.getLibraryUrl() + "/opac/m/index";
                            }
                            bundle.putString("url", url);
                        } else if (menu.getType() == WebViewActivity.TYPE_HOMEWORK) {
                            bundle.putString("url", Constants.HOMEWORK + user.getStudentKH() + "/" + configure.getAppRememberCode());
                        }
                        bundle.putString("title", menu.getTitle());
                        bundle.putInt("type", menu.getType());

                        startActivityForResult(Class.forName(menu.getPath()), bundle, Constants.REQUEST);
                    } catch (ClassNotFoundException e) {
                        showMessage("找不到该页面！");
                        e.printStackTrace();
                    }
                } else {
                    showMessage("你点的太快了！");
                }
            }
        });

        mainPresenter = new MainPresenter(this, this);
        mainPresenter.showMenu();
        mainPresenter.checkUpdate(user.getStudentKH());
        mainPresenter.initUser();
        mainPresenter.showTimeAxis();
        mainPresenter.showWeather();
        mainPresenter.checkPermission();
        mainPresenter.initPush(user.getStudentKH());
        mainPresenter.connectRongIM();
        mainPresenter.startLoginService();
        qBadgeView = new QBadgeView(context);
        qBadgeView.bindTarget(unReadMessage);
        qBadgeView.setBadgeGravity(Gravity.END | Gravity.TOP);
        qBadgeView.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
            @Override
            public void onDragStateChanged(int dragState, Badge badge, View targetView) {

            }
        });

        qBadgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qBadgeView.hide(false);
                mainPresenter.startChat();
            }
        });

        RongIM.getInstance().addUnReadMessageCountChangedObserver(new IUnReadMessageObserver() {
            @Override
            public void onCountChanged(int i) {
                if (i == 0) {
                    qBadgeView.hide(false);
                } else {
                    if (i > 99) {
                        qBadgeView.setBadgeText("99+");
                    } else {
                        qBadgeView.setBadgeText(String.valueOf(i));
                    }
                }
            }
        }, Conversation.ConversationType.PRIVATE);
    }

    @OnClick({R.id.iv_nav_avatar, R.id.tv_nav_name, R.id.tv_nav_private_message,
            R.id.tv_nav_update, R.id.tv_nav_share, R.id.tv_nav_logout, R.id.tv_nav_about,
            R.id.tv_nav_fback, R.id.imgbtn_menusetting, R.id.imgbtn_bell})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_nav_avatar:
                startActivity(UserInfoActivity.class);
                break;
            case R.id.tv_nav_name:
                startActivity(UserInfoActivity.class);
                break;
            case R.id.tv_nav_private_message:
                mainPresenter.startChat();
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
                                XGPushManager.deleteTag(getApplicationContext(), user.getStudentKH());
                                XGPushManager.registerPush(getApplicationContext(), "*");
                                XGPushManager.unregisterPush(getApplicationContext());
                                startActivity(LoginActivity.class);
                                commonDialog.dismiss();
                                finish();
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
                mainPresenter.startChat();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isOpen) {
            rootView.close(true);
        } else if (System.currentTimeMillis() - exitTime > 2000) {
            SnackbarUtils.showShortSnackbar(rootView, "再按一次返回键退出");
            exitTime = System.currentTimeMillis();
        } else {
            // super.onBackPressed();
            ActivityStackManager.getManager().exitApp(context);
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
    public void showWeather(String city, String tmp, String content) {
        tvWdLocation.setText(String.valueOf(city + "|" + content));
        tvWdTemp.setText(String.valueOf(tmp + "℃"));
    }

    @Override
    public void showTimeAxis(List<TimeAxis> timeAxisList) {
        dateLineView.setDateLineData(timeAxisList);
    }

    @Override
    public void showNotice() {

    }

    @Override
    public void showSyllabus() {

    }

    @Override
    public void showMenu(List<Menu> menuList) {
        this.menuList.clear();
        this.menuList.addAll(menuList);
        menuAdapter.notifyDataSetChanged();
    }

    @Override
    public void showUser(User user) {
        tvNavName.setText(user.getTrueName());
        if (!TextUtils.isEmpty(user.getHead_pic_thumb())) {
            Glide
                    .with(MainActivity.this)
                    .load(Constants.PICTURE_URL + user.getHead_pic_thumb())
                    .bitmapTransform(new CropCircleTransformation(MainActivity.this))
                    .skipMemoryCache(true)
                    .crossFade()
                    .into(ivNavAvatar);
        } else {
            if ("男".equals(user.getSex())) {
                Glide
                        .with(this)
                        .load(R.drawable.head_boy)
                        .bitmapTransform(new CropCircleTransformation(MainActivity.this))
                        .skipMemoryCache(true)
                        .crossFade()
                        .into(ivNavAvatar);
            } else {
                Glide
                        .with(this)
                        .load(R.drawable.head_girl)
                        .bitmapTransform(new CropCircleTransformation(MainActivity.this))
                        .skipMemoryCache(true)
                        .crossFade()
                        .into(ivNavAvatar);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST && resultCode == Constants.CHANGE) {
            mainPresenter.showMenu();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(new IUnReadMessageObserver() {
            @Override
            public void onCountChanged(int i) {

            }
        });
        if (RongIM.getInstance().getCurrentConnectionStatus()
                == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED) {
            RongIM.getInstance().disconnect();
        }
    }
}

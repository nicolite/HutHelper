package cn.nicolite.huthelper.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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
import cn.nicolite.huthelper.model.bean.Notice;
import cn.nicolite.huthelper.model.bean.TimeAxis;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.presenter.MainPresenter;
import cn.nicolite.huthelper.utils.ButtonUtils;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.utils.ToastUtil;
import cn.nicolite.huthelper.view.adapter.MenuAdapter;
import cn.nicolite.huthelper.view.customView.CommonDialog;
import cn.nicolite.huthelper.view.customView.DateLineView;
import cn.nicolite.huthelper.view.customView.DragLayout;
import cn.nicolite.huthelper.view.customView.RichTextView;
import cn.nicolite.huthelper.view.iview.IMainView;
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
    private long exitTime = 0;
    private List<Menu> menuList = new ArrayList<>();
    private MenuAdapter menuAdapter;
    private boolean isOpen;
    private QBadgeView qBadgeView;
    private Configure configure;
    private Notice notice;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private MainReceiver mainReceiver;

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
                        switch (menu.getType()) {
                            case WebViewActivity.TYPE_HOMEWORK:
                                showMessage("开原版暂不提供此功能");
                                return;
                            case WebViewActivity.TYPE_LIBRARY:
                                showMessage("开原版暂不提供此功能");
                                return;
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
        mainPresenter.checkUpdate();
        mainPresenter.initUser();
        mainPresenter.showTimeAxis();
        mainPresenter.showSyllabus();
        mainPresenter.showWeather();
        mainPresenter.checkPermission();
        // mainPresenter.registerPush(); //TODO 在openKey.properties填好key后，可以开启使用

        mainPresenter.showNotice(false);
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

        //注册本地广播监听消息
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        mainReceiver = new MainReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.MainBroadcast);
        localBroadcastManager.registerReceiver(mainReceiver, intentFilter);
    }

    //主界面监听器
    class MainReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int type = bundle.getInt("type");
                switch (type) {
                    case Constants.BROADCAST_TYPE_NOTICE:
                        mainPresenter.showNotice(true);
                        break;
                    case Constants.BROADCAST_TYPE_REFRESH_AVATAR:
                        showUser(configure.getUser());
                        break;
                }
            }
        }
    }


    @OnClick({R.id.iv_nav_avatar, R.id.tv_nav_name,
            R.id.tv_nav_update, R.id.tv_nav_share, R.id.tv_nav_logout, R.id.tv_nav_about,
            R.id.tv_nav_fback, R.id.imgbtn_menusetting, R.id.imgbtn_bell,
            R.id.tv_tongzhi_contont, R.id.tv_tongzhi_title, R.id.tv_notice_maincontent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_nav_avatar:
                startActivity(UserInfoActivity.class);
                break;
            case R.id.tv_nav_name:
                startActivity(UserInfoActivity.class);
                break;
            case R.id.tv_nav_update:
                showMessage("开源版暂不提供检查更新功能");
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
                                //TODO 反注册推送
//                                mainPresenter.unregisterPush();
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
            case R.id.tv_tongzhi_title:
            case R.id.tv_tongzhi_contont:
                if (notice != null) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("noticeId", notice.getId());
                    startActivity(NoticeItemActivity.class, bundle);
                }
                break;
            case R.id.tv_notice_maincontent:
                startActivity(NoticeActivity.class);
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
    public void showNotice(final Notice notice, boolean isReceiver) {
        this.notice = notice;
        tvTongzhiTitle.setText(notice.getTitle());
        tvTongzhiContont.setText(notice.getContent());

        if (isReceiver) {
            final CommonDialog commonDialog = new CommonDialog(context);
            commonDialog
                    .setTitle(notice.getTitle())
                    .setMessage(notice.getContent())
                    .setPositiveButton("查看", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            commonDialog.dismiss();
                            Bundle bundle = new Bundle();
                            bundle.putLong("noticeId", notice.getId());
                            startActivity(NoticeItemActivity.class, bundle);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
    }

    @Override
    public void showSyllabus(String date, String nextClass) {
        tvDateMaincontent.setText(date);
        tvCourseMaincontent.setText(nextClass);
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
                    .bitmapTransform(new CropCircleTransformation(context))
                    .skipMemoryCache(true)
                    .crossFade()
                    .into(ivNavAvatar);
        } else {
            if ("男".equals(user.getSex())) {
                Glide
                        .with(this)
                        .load(R.drawable.head_boy)
                        .bitmapTransform(new CropCircleTransformation(context))
                        .skipMemoryCache(true)
                        .crossFade()
                        .into(ivNavAvatar);
            } else {
                Glide
                        .with(this)
                        .load(R.drawable.head_girl)
                        .bitmapTransform(new CropCircleTransformation(context))
                        .skipMemoryCache(true)
                        .crossFade()
                        .into(ivNavAvatar);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST) {
            switch (resultCode) {
                case Constants.CHANGE:
                    mainPresenter.showMenu();
                    break;
                case Constants.REFRESH:
                    mainPresenter.showSyllabus();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (localBroadcastManager != null) {
            localBroadcastManager.unregisterReceiver(mainReceiver);
        }
    }
}

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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseActivity;
import cn.nicolite.huthelper.manager.ActivityStackManager;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.ConstantsValue;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.huthelper.model.bean.Menu;
import cn.nicolite.huthelper.model.bean.Notice;
import cn.nicolite.huthelper.model.bean.TimeAxis;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.utils.ButtonUtils;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.adapter.MenuAdapter;
import cn.nicolite.huthelper.view.customView.CommonDialog;
import cn.nicolite.huthelper.view.customView.DateLineView;
import cn.nicolite.huthelper.view.customView.DragLayout;
import cn.nicolite.huthelper.view.customView.RichTextView;
import cn.nicolite.huthelper.view.iview.IMainView;
import cn.nicolite.huthelper.view.presenter.MainPresenter;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 主页
 */
public class MainActivity extends BaseActivity implements IMainView {
    private TextView tvWdTemp;
    private TextView tvWdLocation;
    private TextView tvCourseMaincontent;
    private TextView tvDateMaincontent;
    private ImageView ivDateline;
    private DateLineView dateLineView;
    private ImageView muTongzhi;
    private TextView tvTongzhiTitle;
    private RichTextView tvNoticeMaincontent;
    private TextView tvTongzhiContont;
    private TextView tvTongzhiTime;
    private RelativeLayout rlMainTongzhi;
    private RecyclerView rvMainMenu;
    private DragLayout rootView;
    private ImageView ivNavAvatar;
    private RichTextView tvNavName;
    private MainPresenter mainPresenter;
    private long exitTime = 0;
    private List<Menu> menuList = new ArrayList<>();
    private MenuAdapter menuAdapter;
    private boolean isOpen;
    private Configure configure;
    private Notice notice;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private MainReceiver mainReceiver;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void doBusiness() {
        ViewGroup contentView = getWindow().getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
        contentView.getChildAt(0).setFitsSystemWindows(false);

        tvWdTemp = (TextView) findViewById(R.id.tv_wd_temp);
        tvWdLocation = (TextView) findViewById(R.id.tv_wd_location);
        tvCourseMaincontent = (TextView) findViewById(R.id.tv_course_maincontent);
        tvDateMaincontent = (TextView) findViewById(R.id.tv_date_maincontent);
        ivDateline = (ImageView) findViewById(R.id.iv_dateline);
        dateLineView = (DateLineView) findViewById(R.id.dateLineView);
        muTongzhi = (ImageView) findViewById(R.id.mu_tongzhi);
        tvTongzhiTitle = (TextView) findViewById(R.id.tv_tongzhi_title);
        tvNoticeMaincontent = (RichTextView) findViewById(R.id.tv_notice_maincontent);
        tvTongzhiContont = (TextView) findViewById(R.id.tv_tongzhi_contont);
        tvTongzhiTime = (TextView) findViewById(R.id.tv_tongzhi_time);
        rlMainTongzhi = (RelativeLayout) findViewById(R.id.rl_main_tongzhi);
        rvMainMenu = (RecyclerView) findViewById(R.id.rv_main_menu);
        rootView = (DragLayout) findViewById(R.id.rootView);
        ivNavAvatar = (ImageView) findViewById(R.id.iv_nav_avatar);
        tvNavName = (RichTextView) findViewById(R.id.tv_nav_name);

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
                        if (menu.getType() == WebViewActivity.TYPE_LIBRARY) {
                            bundle.putString("url", ConstantsValue.LIBRARY);
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
        mainPresenter.registerPush();

        mainPresenter.showNotice(false);
        mainPresenter.startLoginService();

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
            R.id.tv_nav_fback, R.id.imgbtn_menusetting,
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
                showMessage("已是最新版！");
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
                                mainPresenter.unregisterPush();
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
            SnackbarUtils.INSTANCE.showShortSnackbar(rootView, "再按一次返回键退出");
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
        SnackbarUtils.INSTANCE.showShortSnackbar(rootView, msg);
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

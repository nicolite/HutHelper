package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.db.dao.MenuDao;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.Configure;
import cn.nicolite.huthelper.model.bean.Menu;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.utils.ButtonUtils;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.adapter.MenuAdapter;
import cn.nicolite.huthelper.view.widget.CustomItemTouchHelper;

/**
 * 全部应用页面
 * Created by nicolite on 17-11-13.
 */

public class AllActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_left_text)
    TextView toolbarLeftText;
    @BindView(R.id.tv_al_msg)
    TextView tvAlMsg;
    @BindView(R.id.rv_all_main)
    RecyclerView rvAllMain;
    @BindView(R.id.rv_all_other)
    RecyclerView rvAllOther;
    @BindView(R.id.rootView)
    LinearLayout rootView;

    private List<Menu> mainMenuList = new ArrayList<>();
    private List<Menu> otherMenuList = new ArrayList<>();

    private MenuAdapter adapterMain;
    private MenuAdapter adapterOther;
    private boolean isEdit;
    private Configure configure;
    private User user;
    private MenuDao menuDao;

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
        return R.layout.activity_all;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("全部应用");
        toolbarLeftText.setText("编辑");

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

        menuDao = daoSession.getMenuDao();
        mainMenuList.addAll(menuDao.queryBuilder()
                .where(MenuDao.Properties.UserId.eq(userId), MenuDao.Properties.IsMain.eq(true))
                .list());
        mainMenuList.remove(mainMenuList.size() - 1); // 移除全部入口

        otherMenuList.addAll(menuDao.queryBuilder()
                .where(MenuDao.Properties.UserId.eq(userId), MenuDao.Properties.IsMain.eq(false))
                .list());

        rvAllMain.setLayoutManager(new GridLayoutManager(context, 4, OrientationHelper.VERTICAL, false));
        adapterMain = new MenuAdapter(context, mainMenuList);
        rvAllMain.setAdapter(adapterMain);

        rvAllOther.setLayoutManager(new GridLayoutManager(context, 4, OrientationHelper.VERTICAL, false));
        adapterOther = new MenuAdapter(context, otherMenuList);
        rvAllOther.setAdapter(adapterOther);

        adapterMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isEdit) {
                    return;
                }
                if (!ButtonUtils.isFastDoubleClick()) {
                    try {
                        Menu menu = mainMenuList.get(position);
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
                        startActivity(Class.forName(menu.getPath()), bundle);
                    } catch (ClassNotFoundException e) {
                        showMessage("找不到该页面！");
                        e.printStackTrace();
                    }
                } else {
                    showMessage("你点的太快了！");
                }
            }
        });
        ItemTouchHelper.Callback callback = new CustomItemTouchHelper(adapterMain);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvAllMain);

        adapterOther.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isEdit) {
                    return;
                }
                if (!ButtonUtils.isFastDoubleClick()) {
                    try {
                        Menu menu = otherMenuList.get(position);
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
                        startActivity(Class.forName(menu.getPath()), bundle);
                    } catch (ClassNotFoundException e) {
                        showMessage("找不到该页面！");
                        e.printStackTrace();
                    }
                } else {
                    showMessage("你点的太快了！");
                }
            }
        });

        adapterMain.setOnEditClickListener(new MenuAdapter.EditClickLister() {
            @Override
            public void onEditClick(View v, int position) {
                Menu item = mainMenuList.remove(position);
                item.setIsMain(false);
                item.setIndex(mainMenuList.size() - 1 + otherMenuList.size());
                otherMenuList.add(otherMenuList.size(), item);
                adapterMain.notifyItemRemoved(position);
                adapterOther.notifyItemInserted(otherMenuList.size());
            }
        });
        adapterOther.setOnEditClickListener(new MenuAdapter.EditClickLister() {
            @Override
            public void onEditClick(View v, int position) {
                if (mainMenuList.size() >= 11) {
                    showMessage("主页面最多放置11个应用");
                    return;
                }
                Menu item = otherMenuList.remove(position);
                item.setIsMain(true);
                item.setIndex(mainMenuList.size() - 1);
                mainMenuList.add(otherMenuList.size(), item);
                adapterOther.notifyItemRemoved(position);
                adapterMain.notifyItemInserted(mainMenuList.size());
            }
        });
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_left_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_left_text:
                isEdit = !isEdit;
                adapterMain.setEdit(isEdit);
                adapterOther.setEdit(isEdit);
                if (isEdit) {
                    toolbarLeftText.setText("完成");
                    tvAlMsg.setVisibility(View.VISIBLE);
                } else {
                    toolbarLeftText.setText("编辑");
                    tvAlMsg.setVisibility(View.GONE);
                    saveChange();
                }
                break;
        }
    }

    public void showMessage(String msg) {
        SnackbarUtils.showShortSnackbar(rootView, msg);
    }

    public void saveChange() {
        List<Menu> menuList = new ArrayList<>();
        menuList.addAll(mainMenuList);
        mainMenuList.addAll(otherMenuList);

        for (Menu menu : menuList) {
            menuDao.update(menu);
        }
    }
}

package cn.nicolite.huthelper.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseActivity;
import cn.nicolite.huthelper.db.dao.LessonDao;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.Lesson;
import cn.nicolite.huthelper.presenter.SyllabusPresenter;
import cn.nicolite.huthelper.utils.DateUtils;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.ScreenUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.adapter.ChooseListAdapter;
import cn.nicolite.huthelper.view.customView.LoadingDialog;
import cn.nicolite.huthelper.view.fragment.SyllabusFragment;
import cn.nicolite.huthelper.view.iview.ISyllabusView;

/**
 * Created by nicolite on 17-11-13.
 * 课程表页面
 */

public class SyllabusActivity extends BaseActivity implements ISyllabusView {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.rootView)
    FrameLayout rootView;
    private LoadingDialog loadingDialog;
    private SyllabusFragment syllabusFragment;
    int CurrWeek = 0;
    int chooseNum;
    private List<Lesson> lessonList = new ArrayList<>();
    private SyllabusPresenter syllabusPresenter;

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
        return R.layout.activity_syllabus;
    }

    @Override
    protected void doBusiness() {
        if (ListUtils.isEmpty(getConfigureList()) || TextUtils.isEmpty(userId) || userId.equals("*")) {
            startActivity(SplashActivity.class);
            finish();
        } else {
            CurrWeek = DateUtils.getNowWeek();
            toolbarTitle.setText(String.valueOf("第" + CurrWeek + "周"));
            chooseNum = CurrWeek - 1;

            SharedPreferences.Editor edit = getSharedPreferences("choose", MODE_PRIVATE).edit();
            edit.putInt("position", chooseNum);
            edit.apply();

            lessonList.addAll(daoSession.getLessonDao()
                    .queryBuilder()
                    .where(LessonDao.Properties.UserId.eq(userId))
                    .list());

            syllabusFragment = SyllabusFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.content, syllabusFragment).commit();

            syllabusPresenter = new SyllabusPresenter(this, this);
            syllabusPresenter.showSyllabus(false);
        }
    }

    @OnClick({R.id.toolbar_back, R.id.toolbar_title, R.id.toolbar_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_title:
                showWeekListWindows(toolbarTitle);
                break;
            case R.id.toolbar_refresh:
                syllabusPresenter.showSyllabus(true);
                break;
        }
    }

    /**
     * 选择周数弹出窗口
     */
    protected PopupWindow weekListWindow;
    /**
     * 显示周数的listview
     */
    protected ListView weekListView;

    private void showWeekListWindows(View parent) {

        if (ListUtils.isEmpty(lessonList)) {
            showMessage("暂未导入课表");
            return;
        }

        int width = ScreenUtils.getScreenWidth(context) / 2;
        if (weekListWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupWindowLayout = layoutInflater.inflate(R.layout.popupwindow_coursetable, null);
            weekListView = (ListView) popupWindowLayout.findViewById(R.id.lv_weekchoose_coursetable);
            final List<String> weekList = new ArrayList<>();
            for (int i = 1; i <= 25; i++) {
                if (i == CurrWeek) {
                    weekList.add("第" + i + "周(本周)");
                } else
                    weekList.add("第" + i + "周");
            }

            weekListView.setAdapter(new ChooseListAdapter(context, weekList));
            weekListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    weekListWindow.dismiss();
                    SharedPreferences.Editor edit = getSharedPreferences("choose", MODE_PRIVATE).edit();
                    edit.putInt("position", position);
                    edit.apply();
                    toolbarTitle.setText(weekList.get(position));
                    chooseNum = position;
                    syllabusFragment.changeWeek(position + 1, DateUtils.getNextSunday(DateUtils.addDate(new Date(), (position + 1 - CurrWeek) * 7)));
                }
            });
            weekListWindow = new PopupWindow(popupWindowLayout, width, width + 100);
        }

        weekListView.post(new Runnable() {
            @Override
            public void run() {
                weekListView.smoothScrollToPosition(chooseNum);
            }
        });

        rootView.setForeground(getResources().getDrawable(R.color.bg_black_shadow));

        weekListWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rootView.setForeground(getResources().getDrawable(R.color.transparent));
            }
        });

        weekListWindow.setFocusable(true);
        //设置点击外部可消失
        weekListWindow.setOutsideTouchable(true);
        weekListWindow.setBackgroundDrawable(new BitmapDrawable());
        weekListWindow.showAsDropDown(parent, -(width - parent.getWidth()) / 2, 20);
    }


    @Override
    public void showLoading() {
        loadingDialog = new LoadingDialog(context)
                .setLoadingText("加载中...");
        loadingDialog.show();
    }

    @Override
    public void closeLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg) {
        SnackbarUtils.showShortSnackbar(rootView, msg);
    }

    @Override
    public void showSyllabus(List<Lesson> lessonList) {
        this.lessonList.clear();
        this.lessonList.addAll(lessonList);

        CurrWeek = DateUtils.getNowWeek();
        toolbarTitle.setText(String.valueOf("第" + CurrWeek + "周"));
        chooseNum = CurrWeek - 1;

        SharedPreferences.Editor edit = getSharedPreferences("choose", MODE_PRIVATE).edit();
        edit.putInt("position", chooseNum);
        edit.apply();

        content.setVisibility(View.VISIBLE);
        syllabusFragment.updateData();
        setResult(Constants.REFRESH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST) {
            if (syllabusFragment != null) {
                syllabusFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

}

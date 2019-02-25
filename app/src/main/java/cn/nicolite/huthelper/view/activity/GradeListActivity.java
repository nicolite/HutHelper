package cn.nicolite.huthelper.view.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.model.bean.Grade;
import cn.nicolite.huthelper.model.bean.Term;
import cn.nicolite.huthelper.presenter.GradeListPresenter;
import cn.nicolite.huthelper.utils.ScreenUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.adapter.ChooseListAdapter;
import cn.nicolite.huthelper.view.adapter.GradeListAdapter;
import cn.nicolite.huthelper.view.iview.IGradeListView;
import cn.nicolite.huthelper.view.customView.LoadingDialog;

/**
 * 成绩列表页面
 * Created by nicolite on 17-11-12.
 */

public class GradeListActivity extends BaseActivity implements IGradeListView {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rootView)
    FrameLayout rootView;
    private GradeListPresenter gradeListPresenter;
    private List<Grade> gradeList = new ArrayList<>();
    private List<Grade> gradeListCache = new ArrayList<>();
    private GradeListAdapter adapter;
    private LoadingDialog loadingDialog;
    private List<Term> termList = new ArrayList<>();

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar();
        setDeepColorStatusBar();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_grade_list;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("所有成绩");
        recyclerView.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.VERTICAL, false));
        adapter = new GradeListAdapter(context, gradeList);
        recyclerView.setAdapter(adapter);
        gradeListPresenter = new GradeListPresenter(this, this);
        gradeListPresenter.showGradeList();
    }


    @OnClick({R.id.toolbar_back, R.id.toolbar_title, R.id.toolbar_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_title:
                showChooseListWindows(toolbarTitle);
                break;
            case R.id.toolbar_refresh:
                gradeListPresenter.showGradeList();
                break;
        }
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null){
            loadingDialog = new LoadingDialog(context)
                    .setLoadingText("加载中...");
        }
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
        SnackbarUtils.showShortSnackbar(recyclerView, msg);
    }

    @Override
    public void showGradeList(List<Grade> gradeList) {
        toolbarTitle.setText("所有成绩");
        this.gradeList.clear();
        this.gradeList.addAll(gradeList);
        adapter.notifyDataSetChanged();

        this.gradeListCache.clear();
        this.gradeListCache.addAll(gradeList);

        SharedPreferences.Editor edit = getSharedPreferences("choose", MODE_PRIVATE).edit();
        edit.putInt("position", 0);
        edit.apply();

        boolean isExist = false;
        termList.clear();

        //记录学年学期
        for (Grade grade : gradeList) {
            for (Term term : termList) {
                int temp1 = Integer.parseInt(grade.getXn().split("-")[0] + grade.getXq());
                int temp2 = Integer.parseInt(term.getXuenian().split("-")[0] + term.getXueqi());
                if (temp1 == temp2) {
                    isExist = true;
                    break;
                }
            }

            if (isExist) {
                isExist = false;
                continue;
            }
            Term term = new Term();
            term.setXuenian(grade.getXn());
            term.setXueqi(grade.getXq());
            term.setBanji(grade.getBj());
            term.setXueqnianXueqi(String.valueOf(grade.getXn() + "学年第" + grade.getXq() + "学期"));
            termList.add(term);
        }

        Collections.sort(termList, new Comparator<Term>() {
            @Override
            public int compare(Term term, Term t1) {
                int temp1 = Integer.parseInt(term.getXuenian().split("-")[0] + term.getXueqi());
                int temp2 = Integer.parseInt(t1.getXuenian().split("-")[0] + t1.getXueqi());
                return temp1 - temp2;
            }
        });
    }

    @Override
    public void changeGradeList(List<Grade> gradeList) {
        this.gradeList.clear();
        this.gradeList.addAll(gradeList);
        adapter.notifyDataSetChanged();
    }


    protected PopupWindow weekListWindow;
    private ListView weekListView;
    /**
     * 设置成绩选择列表PopupWindows
     *
     * @param parent 相对位置View
     */
    private void showChooseListWindows(View parent) {
        int width = ScreenUtils.getScreenWidth(context) / 2;
        if (weekListWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupWindowLayout = layoutInflater.inflate(R.layout.popupwindow_select, null);
            weekListView = (ListView) popupWindowLayout.findViewById(R.id.listView);
            List<String> titleList = new ArrayList<>();
            titleList.add("所有成绩");
            for (Term term : termList) {
                titleList.add(term.getXueqnianXueqi());
            }

            final List<String> title = titleList;
            weekListView.setAdapter(new ChooseListAdapter(context, titleList));
            weekListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    weekListWindow.dismiss();
                    toolbarTitle.setText(title.get(position));
                    //坑啊，直接在adapter中设置参数记录居然没用，只能暴力了
                    SharedPreferences.Editor edit = getSharedPreferences("choose", MODE_PRIVATE).edit();
                    edit.putInt("position", position);
                    edit.apply();
                    if (position > 0) {
                        gradeListPresenter.changeGradeList(gradeListCache,
                                termList.get(position - 1).getXuenian(),
                                termList.get(position - 1).getXueqi());
                    }else {
                        gradeListPresenter.changeGradeList(gradeListCache, "", "");
                    }
                }
            });
            weekListWindow = new PopupWindow(popupWindowLayout, width, width + 100);
        }

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
}

package cn.nicolite.huthelper.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseFragment;
import cn.nicolite.huthelper.model.bean.ExpLesson;
import cn.nicolite.huthelper.view.adapter.ExpLessonAdapter;

/**
 * 实验课表页面
 * Created by nicolite on 17-11-4.
 */

public class ExpLessonFragment extends BaseFragment {

    public static final int FINISHED = 0;
    public static final int UNFINISHED = 1;
    private int type = UNFINISHED;

    private List<ExpLesson> expLessonList = new ArrayList<>();
    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private ExpLessonAdapter adapter;

    public static ExpLessonFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        ExpLessonFragment fragment = new ExpLessonFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initConfig(Bundle savedInstanceState) {

    }

    @Override
    protected void initArguments(Bundle arguments) {
        if (arguments != null){
            type = arguments.getInt("type", UNFINISHED);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_explesson;
    }

    @Override
    protected void doBusiness() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.VERTICAL, false));
        adapter = new ExpLessonAdapter(context, expLessonList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void visibleToUser(boolean isVisible, boolean isFirstVisible) {

    }

    public void updateData(List<ExpLesson> expLessonList){

        this.expLessonList.clear();
        this.expLessonList.addAll(expLessonList);

        if (adapter != null){
            adapter.notifyDataSetChanged();
        }

    }
}

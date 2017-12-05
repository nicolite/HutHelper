package cn.nicolite.huthelper.view.fragment;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.fragment.BaseFragment;
import cn.nicolite.huthelper.model.bean.Lesson;

/**
 * Created by nicolite on 17-12-4.
 * 课程表
 */

public class SyllabusFragment extends BaseFragment {
    private List<Lesson> lessonList = new ArrayList<>();

    public static SyllabusFragment newInstance() {

        Bundle args = new Bundle();

        SyllabusFragment fragment = new SyllabusFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initConfig(Bundle savedInstanceState) {

    }

    @Override
    protected void initArguments(Bundle arguments) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_syllabus;
    }

    @Override
    protected void doBusiness() {

    }

    @Override
    protected void visibleToUser(boolean isVisible, boolean isFirstVisible) {

    }

    public void updateData(List<Lesson> lessons){
        lessonList.clear();
        lessonList.addAll(lessons);

        //TODO 更新课表数据
    }

    public void changeWeek(int weekNo){
        //TODO 更新数据
    }

}

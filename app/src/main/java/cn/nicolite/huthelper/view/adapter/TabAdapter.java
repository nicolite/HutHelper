package cn.nicolite.huthelper.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cn.nicolite.huthelper.utils.ListUtils;

/**
 * TabLayout with ViewPager 通用适配器
 * Created by nicolite on 17-7-30.
 */

public class TabAdapter extends FragmentPagerAdapter {
    private List<String> titleList;
    private List<Fragment> fragmentList;

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    public TabAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    public TabAdapter(FragmentManager fm, List<String> titleList, List<Fragment> fragmentList) {
        super(fm);
        this.titleList = titleList;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null? 0 : fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ListUtils.isEmpty(titleList) ? super.getPageTitle(position) : titleList.get(position);
    }
}

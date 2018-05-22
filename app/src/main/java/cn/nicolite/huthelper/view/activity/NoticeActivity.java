package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.db.dao.NoticeDao;
import cn.nicolite.huthelper.model.bean.Notice;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.view.adapter.NoticeAdapter;
import cn.nicolite.huthelper.view.customView.CommonDialog;

/**
 * Created by nicolite on 17-12-4.
 * 通知页面
 */

public class NoticeActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.lRecyclerView)
    LRecyclerView lRecyclerView;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private List<Notice> noticeList = new ArrayList<>();
    private NoticeAdapter noticeAdapter;

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
        return R.layout.activity_notice;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("通知公告");
        lRecyclerView.setPullRefreshEnabled(false);

        final NoticeDao noticeDao = daoSession.getNoticeDao();

        noticeList.addAll(noticeDao.queryBuilder()
                .where(NoticeDao.Properties.UserId.eq(userId))
                .orderDesc(NoticeDao.Properties.Id)
                .list());

        if (ListUtils.isEmpty(noticeList)) {
            lRecyclerView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
            return;
        }

        lRecyclerView.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.VERTICAL, false));
        noticeAdapter = new NoticeAdapter(context, noticeList);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(noticeAdapter);
        lRecyclerView.setAdapter(lRecyclerViewAdapter);
        lRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                final CommonDialog commonDialog = new CommonDialog(context);
                commonDialog
                        .setMessage("确认移除该通知？")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Notice notice = noticeList.remove(position);
                                noticeDao.delete(notice);
                                noticeAdapter.notifyItemRemoved(position);
                                commonDialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

    }

    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }
}

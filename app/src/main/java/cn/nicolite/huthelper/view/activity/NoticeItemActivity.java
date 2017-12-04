package cn.nicolite.huthelper.view.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.activity.BaseActivity;
import cn.nicolite.huthelper.db.dao.NoticeDao;
import cn.nicolite.huthelper.model.bean.Notice;
import cn.nicolite.huthelper.utils.ListUtils;
import cn.nicolite.huthelper.utils.ToastUtil;
import cn.nicolite.huthelper.view.widget.RichTextView;

/**
 * Created by nicolite on 17-12-4.
 * 通知详情
 */

public class NoticeItemActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_noticeitem_title)
    TextView tvNoticeitemTitle;
    @BindView(R.id.tv_noticeitem_time)
    RichTextView tvNoticeitemTime;
    @BindView(R.id.tv_noticeitem_content)
    TextView tvNoticeitemContent;
    @BindView(R.id.rootView)
    CoordinatorLayout rootView;
    private long noticeId = -2;

    @Override
    protected void initConfig(Bundle savedInstanceState) {
        setImmersiveStatusBar(true);
        setLayoutNoLimits(true);
        setSlideExit(true);
    }

    @Override
    protected void initBundleData(Bundle bundle) {
        if (bundle != null) {
            noticeId = bundle.getLong("noticeId", -2);
        }
        if (noticeId == -2) {
            ToastUtil.showToastShort("获取通知失败！");
            finish();
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_notice_item;
    }

    @Override
    protected void doBusiness() {
        toolbarTitle.setText("通知详情");
        NoticeDao noticeDao = daoSession.getNoticeDao();

        List<Notice> list = noticeDao.queryBuilder()
                .where(NoticeDao.Properties.UserId.eq(userId), NoticeDao.Properties.Id.eq(noticeId))
                .list();

        if (ListUtils.isEmpty(list)) {
            ToastUtil.showToastShort("未找到该通知的数据！");
            finish();
            return;
        }

        Notice notice = list.get(0);
        tvNoticeitemTitle.setText(notice.getTitle());
        tvNoticeitemTime.setText(notice.getTime());
        tvNoticeitemContent.setText(notice.getContent());

    }

    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }
}

package cn.nicolite.huthelper.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.fragment.BaseFragment;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.Say;
import cn.nicolite.huthelper.presenter.SayPresenter;
import cn.nicolite.huthelper.utils.ButtonUtils;
import cn.nicolite.huthelper.utils.CommUtil;
import cn.nicolite.huthelper.utils.KeyBoardUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.utils.ToastUtil;
import cn.nicolite.huthelper.view.activity.UserInfoCardActivity;
import cn.nicolite.huthelper.view.adapter.SayAdapter;
import cn.nicolite.huthelper.view.iview.ISayView;
import cn.nicolite.huthelper.view.customView.CommonDialog;

/**
 * Created by nicolite on 17-11-14.
 */

public class SayFragment extends BaseFragment implements ISayView {
    @BindView(R.id.lRecyclerView)
    LRecyclerView lRecyclerView;
    @BindView(R.id.rootView)
    LinearLayout rootView;

    private int type = ALLSAY;
    private String searchText = "";
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private List<Say> sayList = new ArrayList<>();
    public static final int ALLSAY = 0;
    public static final int MYSAY = 1;
    private SayPresenter sayPresenter;
    private boolean isNoMore = false;
    private int currentPage = 1;
    private SayAdapter sayAdapter;

    public static SayFragment newInstance(int type, String searchText) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        if (!TextUtils.isEmpty(searchText)) {
            args.putString("searchText", searchText);
        }
        SayFragment fragment = new SayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initConfig(Bundle savedInstanceState) {

    }

    @Override
    protected void initArguments(Bundle arguments) {
        if (arguments != null) {
            type = arguments.getInt("type", ALLSAY);
            if (type == MYSAY) {
                searchText = arguments.getString("searchText", "");
            }
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_say;
    }

    @Override
    protected void doBusiness() {
        lRecyclerView.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.VERTICAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });

        sayAdapter = new SayAdapter(context, sayList);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(sayAdapter);
        lRecyclerView.setAdapter(lRecyclerViewAdapter);
        sayPresenter = new SayPresenter(this, this);
        sayAdapter.setOnItemClickListener(new SayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.Adapter adapter, int position, long itemId) {

            }

            @Override
            public void onAddCommentClick(int position, String sayId) {
                showCommitView(position, sayId);
            }


            @Override
            public void onUserClick(String userId, String username) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putString("username", username);
                startActivity(UserInfoCardActivity.class, bundle);
            }

            @Override
            public void onLikeClick(String sayId) {
                sayPresenter.likeSay(sayId);
            }

            @Override
            public void onDeleteClick(final Say say, int position) {
                final CommonDialog commonDialog = new CommonDialog(context);
                commonDialog
                        .setMessage("确定删除这条说说？")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                commonDialog.dismiss();
                                sayPresenter.deleteSay(say);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

            }

        });

        lRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                switch (type) {
                    case SayFragment.ALLSAY:
                        sayPresenter.showSayList(type, true);
                        break;
                    case SayFragment.MYSAY:
                        sayPresenter.loadSayListByUserId(searchText, 1, true, false);
                        break;
                }
            }
        });

        lRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!isNoMore) {
                    switch (type) {
                        case SayFragment.ALLSAY:
                            sayPresenter.loadMore(type, ++currentPage);
                            break;
                        case SayFragment.MYSAY:
                            sayPresenter.loadSayListByUserId(searchText, ++currentPage, true, true);
                            break;
                    }
                }
            }
        });

        lRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
            @Override
            public void reload() {
                if (!isNoMore) {
                    switch (type) {
                        case SayFragment.ALLSAY:
                            sayPresenter.loadMore(type, currentPage);
                            break;
                        case SayFragment.MYSAY:
                            sayPresenter.loadSayListByUserId(searchText, currentPage, true, true);
                            break;
                    }
                }
            }
        });

        lRecyclerView.forceToRefresh();

    }

    @Override
    protected void visibleToUser(boolean isVisible, boolean isFirstVisible) {

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
    public void showSayList(List<Say> list) {
        lRecyclerView.setNoMore(false);
        sayList.clear();
        sayList.addAll(list);
        lRecyclerView.refreshComplete(list.size());
        lRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadMore(List<Say> list) {
        sayList.addAll(list);
        lRecyclerView.refreshComplete(list.size());
        lRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void noMoreData() {
        --currentPage;
        isNoMore = true;
        lRecyclerView.setNoMore(true);
    }

    @Override
    public void deleteSuccess(Say say) {
        sayList.remove(say);
        lRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void commentSuccess(String comment, int position, String userId, String username) {
        Say.CommentsBean commentsBean = new Say.CommentsBean();
        commentsBean.setComment(comment);
        commentsBean.setUser_id(userId);
        commentsBean.setUsername(username);
        sayList.get(position).getComments().add(commentsBean);
        sayAdapter.notifyItemChanged(position);
    }

    @Override
    public void loadFailure() {
        lRecyclerView.refreshComplete(0);
    }

    @Override
    public void loadMoreFailure() {
        --currentPage;
    }

    private PopupWindow addCommitWindow;
    private EditText editText;
    private Button button;

    private void showCommitView(final int position, final String sayId) {

        if (addCommitWindow == null || button == null || editText == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View popupWindowLayout = layoutInflater.inflate(R.layout.popwin_addcommit, null);
            button = (Button) popupWindowLayout.findViewById(R.id.btn_addcomment_submit);
            editText = (EditText) popupWindowLayout.findViewById(R.id.et_addcomment_content);
            addCommitWindow = new PopupWindow(popupWindowLayout, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            addCommitWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            addCommitWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        }
        if (editText != null) {
            CommUtil.showSoftInput(context, editText);
        }
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ButtonUtils.isFastDoubleClick()) {
                        KeyBoardUtils.hideSoftInput(context, activity.getWindow());

                        String comment = editText.getText().toString();

                        if (addCommitWindow.isShowing())
                            addCommitWindow.dismiss();

                        if (TextUtils.isEmpty(comment)) {
                            ToastUtil.showToastShort("请填写评论内容！");
                            return;
                        }
                        sayPresenter.addComment(comment, sayId, position);
                    }
                }
            });
        }
        addCommitWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                editText.setText("");
                KeyBoardUtils.hideSoftInput(context, activity.getWindow());
            }
        });

        addCommitWindow.setFocusable(true);
        //设置点击外部可消失
        addCommitWindow.setOutsideTouchable(true);
        addCommitWindow.setBackgroundDrawable(new BitmapDrawable());

        addCommitWindow.showAtLocation(rootView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST) {
            switch (resultCode) {
                case Constants.DELETE:
                    int position = data.getIntExtra("position", -1);
                    if (position != -1) {
                        deleteItem(position);
                    }
                    break;
                case Constants.PUBLISH:
                    refreshData();
                    break;
                case Constants.CHANGE:
                    break;
            }
        }
    }

    public void refreshData() {
        lRecyclerView.forceToRefresh();
    }

    public void deleteItem(int position) {
        sayList.remove(position);
        lRecyclerViewAdapter.notifyItemRemoved(position);
    }

    public void changetItem(int position, Say say) {
        sayList.remove(position);
        sayList.add(position, say);
        lRecyclerViewAdapter.notifyItemChanged(position);
    }
}

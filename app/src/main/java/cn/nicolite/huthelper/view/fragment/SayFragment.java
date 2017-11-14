package cn.nicolite.huthelper.view.fragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import cn.nicolite.huthelper.model.bean.Say;
import cn.nicolite.huthelper.presenter.SayPresenter;
import cn.nicolite.huthelper.utils.ButtonUtils;
import cn.nicolite.huthelper.utils.CommUtil;
import cn.nicolite.huthelper.utils.KeyBoardUtils;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.utils.ToastUtil;
import cn.nicolite.huthelper.view.adapter.SayAdapter;
import cn.nicolite.huthelper.view.iview.ISayView;

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
    private LRecyclerViewAdapter adapter;
    private List<Say> sayList = new ArrayList<>();
    public static final int ALLSAY = 0;
    public static final int MYSAY = 1;
    private SayPresenter sayPresenter;
    private boolean isNoMore = false;
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
        lRecyclerView.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.VERTICAL, false));
        final SayAdapter sayAdapter = new SayAdapter(context, sayList);
        adapter = new LRecyclerViewAdapter(sayAdapter);
        lRecyclerView.setAdapter(adapter);
        sayPresenter = new SayPresenter(this, this);

        lRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (type){
                    case SayFragment.ALLSAY:
                        break;
                    case SayFragment.MYSAY:
                        break;
                }
            }
        });

        lRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!isNoMore){
                    switch (type){
                        case SayFragment.ALLSAY:
                            break;
                        case SayFragment.MYSAY:
                            break;
                    }
                }
            }
        });

        lRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
            @Override
            public void reload() {
                if (!isNoMore){
                    switch (type){
                        case SayFragment.ALLSAY:
                            break;
                        case SayFragment.MYSAY:
                            break;
                    }
                }
            }
        });

        sayAdapter.setOnItemClickListener(new SayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.Adapter adapter, int position, long itemId) {

            }

            @Override
            public void onAddCommentClick(RecyclerView.Adapter adapter, List<Say.CommentsBean> commentsBeans, TextView commentNumView, int commentNum, int position, long itemId, String sayId) {
                showCommiteView(position, sayId);
            }


            @Override
            public void onLikeClick(RecyclerView.Adapter adapter, ImageView likeView, TextView likeNumView, int likeNum, int position, long itemId, String sayId) {
                likeView.setImageResource(R.drawable.ic_like);
                likeNumView.setText(String.valueOf(++likeNum));
            }

            @Override
            public void onDeleteClick(RecyclerView.Adapter adapter, int position, long itemId, String sayId) {

            }

            @Override
            public void onUserClick(RecyclerView.Adapter adapter, int position, long itemId, String userId, String username) {

            }
        });
        sayPresenter.showSayList(type, false);
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
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadMore(List<Say> list) {
        sayList.addAll(list);
        lRecyclerView.refreshComplete(list.size());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void noMoreData() {
        lRecyclerView.setNoMore(true);
    }

    private PopupWindow addCommitWindow;
    protected View popupWindowLayout;
    private EditText editText;
    private Button button;

    private void showCommiteView(int position, String sayId) {

        if (addCommitWindow == null || button == null || editText == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View popupWindowLayout = layoutInflater.inflate(R.layout.popwin_addcommit, null);
            button = (Button) popupWindowLayout.findViewById(R.id.btn_addcomment_submit);
            editText = (EditText) popupWindowLayout.findViewById(R.id.et_addcomment_content);
            addCommitWindow = new PopupWindow(popupWindowLayout, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        }
        if (editText != null) {
            CommUtil.toggleSoftInput(context, editText);
        }
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ButtonUtils.isFastDoubleClick()) {
                        String comment = editText.getText().toString();

                        if (addCommitWindow.isShowing())
                            addCommitWindow.dismiss();

                        if (TextUtils.isEmpty(comment)) {
                            ToastUtil.showToastShort("请填写评论内容！");
                            return;
                        }
                        sayPresenter.addComment();
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
}

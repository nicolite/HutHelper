package cn.nicolite.huthelper.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.base.BaseFragment;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.presenter.UserListPresenter;
import cn.nicolite.huthelper.utils.SnackbarUtils;
import cn.nicolite.huthelper.view.activity.UserInfoCardActivity;
import cn.nicolite.huthelper.view.adapter.UserListAdapter;
import cn.nicolite.huthelper.view.iview.IUserListView;

/**
 * Created by nicolite on 17-11-11.
 */

public class UserListFragment extends BaseFragment implements IUserListView {
    @BindView(R.id.lRecyclerView)
    LRecyclerView lRecyclerView;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    private UserListPresenter userListPresenter;
    private String searchText;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private List<User> userList = new ArrayList<>();

    public static UserListFragment newInstance(String searchText) {

        Bundle args = new Bundle();
        if (!TextUtils.isEmpty(searchText)) {
            args.putString("searchText", searchText);
        }
        UserListFragment fragment = new UserListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initConfig(Bundle savedInstanceState) {
    }

    @Override
    protected void initArguments(Bundle arguments) {
        if (arguments != null) {
            searchText = arguments.getString("searchText", "");
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_user_list;
    }

    @Override
    protected void doBusiness() {
        lRecyclerView.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.VERTICAL, false));
        lRecyclerViewAdapter = new LRecyclerViewAdapter(new UserListAdapter(context, userList));
        lRecyclerView.setAdapter(lRecyclerViewAdapter);

        userListPresenter = new UserListPresenter(this, this);

        lRecyclerView.setLoadMoreEnabled(false);

        lRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                userListPresenter.showUsers(searchText);
            }
        });

        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                User user = userList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("userId", user.getUser_id());
                bundle.putString("username", user.getTrueName());
                startActivity(UserInfoCardActivity.class, bundle);
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
    public void showUsers(List<User> users) {
        userList.clear();
        userList.addAll(users);
        lRecyclerView.refreshComplete(users.size());
        lRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadFailure() {
        lRecyclerView.refreshComplete(0);
    }

    public void refreshData() {
        lRecyclerView.forceToRefresh();
    }

    public void deleteItem(int position) {
        userList.remove(position);
        lRecyclerViewAdapter.notifyItemRemoved(position);
    }

    public void changetItem(int position, User user) {
        userList.remove(position);
        userList.add(position, user);
        lRecyclerViewAdapter.notifyItemChanged(position);
    }
}

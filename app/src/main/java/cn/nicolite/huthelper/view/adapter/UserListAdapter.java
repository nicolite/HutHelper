package cn.nicolite.huthelper.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nicolite.huthelper.R;
import cn.nicolite.huthelper.model.Constants;
import cn.nicolite.huthelper.model.bean.User;
import cn.nicolite.huthelper.utils.ListUtils;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by nicolite on 17-11-11.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {
    private Context context;
    private List<User> userList;

    public UserListAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_list, parent, false);
        return new UserListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserListViewHolder holder, final int position) {
        final User user = userList.get(position);

        Glide
                .with(context)
                .load(Constants.PICTURE_URL + user.getHead_pic_thumb())
                .error(R.drawable.img_error)
                .placeholder(R.drawable.img_loading)
                .bitmapTransform(new CropCircleTransformation(context))
                .skipMemoryCache(true)
                .crossFade()
                .into(holder.headIcon);

        holder.name.setText(user.getTrueName());
        holder.clazz.setText(user.getClass_name());
        holder.college.setText(user.getDep_name());

        /*holder.itemUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                    @Override
                    public UserInfo getUserInfo(String s) {
                        return new UserInfo(user.getUser_id(), user.getTrueName(),
                                Uri.parse(Constants.PICTURE_URL + user.getHead_pic()));
                    }
                }, true);

                RongIM.getInstance().refreshUserInfoCache(new UserInfo(user.getUser_id(),
                        user.getTrueName(),
                        Uri.parse(Constants.PICTURE_URL + user.getHead_pic())));

                //  RongIM.getInstance().startPrivateChat(context, user.getId(),
                //          user.getTrueName());

                Intent userInfoCard = new Intent(context, UserInfoCardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id", user.getUser_id());
                bundle.putString("username", user.getTrueName());
                userInfoCard.putExtras(bundle);
                context.startActivity(userInfoCard);

            }
        }); */
    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(userList) ? 0 : userList.size();
    }

    static class UserListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_user)
        LinearLayout itemUser;
        @BindView(R.id.head_icon)
        ImageView headIcon;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.clazz)
        TextView clazz;
        @BindView(R.id.college)
        TextView college;

        public UserListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

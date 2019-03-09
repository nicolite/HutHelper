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
    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(userList) ? 0 : userList.size();
    }

    static class UserListViewHolder extends RecyclerView.ViewHolder {

        LinearLayout itemUser;

        ImageView headIcon;

        TextView name;

        TextView clazz;

        TextView college;

        public UserListViewHolder(View itemView) {
            super(itemView);
            itemUser = (LinearLayout) itemView.findViewById(R.id.item_user);
            headIcon = (ImageView) itemView.findViewById(R.id.head_icon);
            name = (TextView) itemView.findViewById(R.id.name);
            clazz = (TextView) itemView.findViewById(R.id.clazz);
            college = (TextView) itemView.findViewById(R.id.college);
        }
    }
}

package cn.nicolite.huthelper.view.iview;

import android.graphics.Bitmap;

import cn.nicolite.huthelper.base.iview.IBaseView;
import cn.nicolite.huthelper.model.bean.User;

/**
 * Created by nicolite on 17-10-28.
 */

public interface IUserInfoView extends IBaseView {
    void showUserInfo(User user);
    void changeAvatarSuccess(Bitmap bitmap);
    void changeAvatar();
}

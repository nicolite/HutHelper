package cn.nicolite.huthelper.view.iview

import android.graphics.Bitmap

import cn.nicolite.huthelper.kBase.IBaseView
import cn.nicolite.huthelper.model.bean.User

/**
 * Created by nicolite on 17-10-28.
 */

interface IUserInfoView : IBaseView {
    fun showUserInfo(user: User)
    fun changeAvatarSuccess(bitmap: Bitmap)
    fun changeAvatar()
}

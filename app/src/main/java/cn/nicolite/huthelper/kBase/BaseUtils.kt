package cn.nicolite.huthelper.kBase

import cn.nicolite.huthelper.db.DaoUtils
import cn.nicolite.huthelper.model.bean.Configure

/**
 * Created by nicolite on 2018/5/20.
 * email nicolite@nicolite.cn
 *  kotlin Utils 基类
 */

abstract class BaseUtils {
    protected val TAG = javaClass.simpleName
    protected val loginUserId: String = DaoUtils.getLoginUser()
    protected val configureList: List<Configure> = DaoUtils.getConfigureList()

}

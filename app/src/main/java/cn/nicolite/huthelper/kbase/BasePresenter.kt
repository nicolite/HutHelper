package cn.nicolite.huthelper.kbase

import cn.nicolite.mvp.kBase.KBasePresenter

/**
 * Created by nicolite on 2019/3/6.
 * email nicolite@nicolite.cn
 */
abstract class BasePresenter<I, V>(iView: I, View: V) : KBasePresenter<I, V>(iView, View)
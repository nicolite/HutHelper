package cn.nicolite.huthelper.view.fragment

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import cn.nicolite.huthelper.R
import cn.nicolite.huthelper.kBase.BaseFragment
import cn.nicolite.huthelper.model.bean.FreshmanGuide
import cn.nicolite.huthelper.presenter.FreshmanGuidePresenter
import cn.nicolite.huthelper.utils.SnackbarUtils
import cn.nicolite.huthelper.view.adapter.FreshmanGuideAdapter
import cn.nicolite.huthelper.view.customView.LoadingDialog
import cn.nicolite.huthelper.view.iview.IFreshmanGuideView
import kotlinx.android.synthetic.main.fragment_freshman_guide.*

/**
 * Created by nicolite on 2018/5/30.
 * email nicolite@nicolite.cn
 */
class FreshmanGuideFragment : BaseFragment(), IFreshmanGuideView {
    private var dataList: MutableList<FreshmanGuide> = ArrayList()
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var guideAdapter: FreshmanGuideAdapter

    override fun setLayoutId(): Int {
        return R.layout.fragment_freshman_guide
    }

    override fun doBusiness() {
        super.doBusiness()
        guideAdapter = FreshmanGuideAdapter(context, dataList)
        guideList.apply {
            layoutManager = LinearLayoutManager(context, OrientationHelper.VERTICAL, false)
            adapter = guideAdapter
            addItemDecoration(DividerItemDecoration(context, OrientationHelper.VERTICAL))
        }
        loadingDialog = LoadingDialog(context).setLoadingText("加载中...")
        FreshmanGuidePresenter(this, this).showGuideList()
    }

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun closeLoading() {
        loadingDialog.dismiss()
    }

    override fun showGuideList(list: List<FreshmanGuide>) {
        dataList.clear()
        dataList.addAll(list)
        guideAdapter.notifyDataSetChanged()
    }

    override fun showMessage(msg: String) {
        SnackbarUtils.showShortSnackbar(guideList, msg)
    }
}
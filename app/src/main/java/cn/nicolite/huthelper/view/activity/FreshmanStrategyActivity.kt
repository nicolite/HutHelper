package cn.nicolite.huthelper.view.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import cn.nicolite.huthelper.R
import cn.nicolite.huthelper.kbase.DefaultConfigActivity
import cn.nicolite.huthelper.model.dao.FreshmanStrategy
import cn.nicolite.huthelper.utils.SnackbarUtils
import cn.nicolite.huthelper.view.IListView
import cn.nicolite.huthelper.view.adapter.FreshmanStrategyRecyclerViewAdapter
import cn.nicolite.huthelper.view.presenter.FreshmanStrategyPresenter
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_lrecyclerview.*
import kotlinx.android.synthetic.main.toolbar_nomenu.*

/**
 * Created by nicolite on 2019/3/6.
 * email nicolite@nicolite.cn
 */
class FreshmanStrategyActivity : DefaultConfigActivity(), IListView<FreshmanStrategy> {
    private lateinit var lRecyclerViewAdapter: LRecyclerViewAdapter
    private val dataList = ArrayList<FreshmanStrategy>()
    override fun setLayoutId(): Int {
        return R.layout.activity_freshman_strategy
    }

    override fun doBusiness() {
        super.doBusiness()
        toolbar_title.text = "新生攻略"
        toolbar_back.setOnClickListener { finish() }

        val freshmanStrategyPresenter = FreshmanStrategyPresenter(this, this)
        lRecyclerViewAdapter = FreshmanStrategyRecyclerViewAdapter(mContext, dataList).getLRecyclerViewAdapter()
        lRecyclerViewAdapter.setOnItemClickListener { view, position ->
            val freshmanStrategy = dataList[position]
            val bundle = Bundle()
            bundle.putInt("type", WebViewActivity.TYPE_FRESHMAN_STRATEGY)
            bundle.putString("title", freshmanStrategy.title.replaceFirst("新生攻略手册--", ""))
            bundle.putString("html", freshmanStrategy.content)
            startActivity(WebViewActivity::class.java, bundle)
        }

        item_lrecyclerview.apply {
            adapter = lRecyclerViewAdapter
            layoutManager = LinearLayoutManager(mContext, OrientationHelper.VERTICAL, false)
            setLoadMoreEnabled(false)
            setOnRefreshListener { freshmanStrategyPresenter.loadFreshmanStrategyList() }
        }

        item_lrecyclerview.forceToRefresh()
    }

    override fun showList(list: List<FreshmanStrategy>) {
        dataList.clear()
        dataList.addAll(list)
        lRecyclerViewAdapter.notifyDataSetChanged()
        item_lrecyclerview.refreshComplete(dataList.size)
    }

    override fun loadFailure(msg: String) {
        item_lrecyclerview.refreshComplete(0)
        SnackbarUtils.showSnackbar(this, msg)
    }
}
package cn.nicolite.huthelper.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.nicolite.huthelper.R
import cn.nicolite.huthelper.kbase.BaseLRecyclerViewAdapter
import cn.nicolite.huthelper.model.dao.FreshmanStrategy
import kotlinx.android.synthetic.main.item_freshman_strategy.view.*

/**
 * Created by nicolite on 2019/3/6.
 * email nicolite@nicolite.cn
 */
class FreshmanStrategyRecyclerViewAdapter(private val context: Context, private val dataList: List<FreshmanStrategy>) :
        BaseLRecyclerViewAdapter<FreshmanStrategyRecyclerViewAdapter.FSViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FSViewHolder {
        return FSViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_freshman_strategy, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: FSViewHolder, position: Int) {
        val freshmanStrategy = dataList[position]
        holder.apply { title.text = freshmanStrategy.title }
    }

    class FSViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.text
    }
}
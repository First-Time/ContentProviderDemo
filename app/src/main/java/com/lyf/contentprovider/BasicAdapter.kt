package com.lyf.contentprovider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView

class BasicAdapter(val mDatas: List<InfoModel>) : RecyclerView.Adapter<BasicAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameView: AppCompatTextView? = null
        var ageView: AppCompatTextView? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_basic, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.nameView = view.findViewById(R.id.tvName)
        viewHolder.ageView = view.findViewById(R.id.tvAge)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameView?.text = mDatas[position].name
        holder.ageView?.text = mDatas[position].age.toString()
    }
}
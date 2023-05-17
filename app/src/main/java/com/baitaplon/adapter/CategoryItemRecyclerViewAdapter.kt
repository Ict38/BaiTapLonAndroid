package com.baitaplon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.baitaplon.R
import com.baitaplon.adapter.CategoryItemRecyclerViewAdapter.CateViewHolder
import com.baitaplon.model.Category

class CategoryItemRecyclerViewAdapter : RecyclerView.Adapter<CateViewHolder>() {
    private var cateList : List<Category>


    init {
        cateList = ArrayList()
    }

    fun setCateList(list : List<Category>){
        cateList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cate_recyclerview_item,parent,false)
        return CateViewHolder(view)
    }

    override fun onBindViewHolder(holder: CateViewHolder, position: Int) {
        val category = cateList[position]
        holder.cate_name.text = category.name
    }
    override fun getItemCount(): Int {
        return cateList.size
    }

    inner class CateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cate_name: TextView

        init {
            cate_name = itemView.findViewById(R.id.cateItemName)
        }
    }
}
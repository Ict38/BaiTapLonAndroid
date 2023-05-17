package com.baitaplon.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.baitaplon.R
import com.baitaplon.model.Category

class CategoryCheckBoxRecyclerViewAdapter(private var cateList : List<Category>) :
    RecyclerView.Adapter<CategoryCheckBoxRecyclerViewAdapter.CateCheckBoxViewHolder>() {

    private var checkedList : ArrayList<Category> = ArrayList()
    fun setCateList(list : List<Category>){
        cateList = list
        notifyDataSetChanged()
    }
    fun getCateList() : List<Category>{
        return checkedList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CateCheckBoxViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cate_checkbox_recyclerview,parent,false)
        return CateCheckBoxViewHolder(view)
    }

    override fun onBindViewHolder(holder: CateCheckBoxViewHolder, position: Int) {
        val category = cateList[position]
        holder.cateName.text = category.name
        holder.checkbox.isChecked = false
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            toggleItemChecked(category)
        }
    }
    override fun getItemCount(): Int {
        return cateList.size
    }

    inner class CateCheckBoxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cateName: TextView
        val checkbox : CheckBox

        init {
            cateName = itemView.findViewById(R.id.cateItemName)
            checkbox = itemView.findViewById(R.id.cateCheckBox)
        }
    }
    fun toggleItemChecked(cate: Category) {
        if (checkedList.contains(cate)) {
            checkedList.remove(cate)
            Log.e("checked size", checkedList.size.toString())
        } else {
            checkedList.add(cate)
            Log.e("checked size", checkedList.size.toString())
        }
    }
}
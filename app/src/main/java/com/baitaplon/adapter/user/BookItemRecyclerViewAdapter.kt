package com.baitaplon.adapter.user

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.baitaplon.R
import com.baitaplon.model.Book

class BookItemRecyclerViewAdapter(
    private var bookList : List<Book>,
    private val listener : OnItemClickListener
) : RecyclerView.Adapter<BookItemRecyclerViewAdapter.BookViewHolder>() {

    init {
        bookList = ArrayList()
    }

    fun setBookList(list : List<Book>){
        bookList = list
        notifyDataSetChanged()
    }
    fun getBookByPosition(position: Int) : Book{
        return bookList[position]
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_item_recyclerview,parent,false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.bookname.text = book.name
    }
    override fun getItemCount(): Int {
        return bookList.size
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val bookname: TextView

        init {
            bookname = itemView.findViewById(R.id.bookName)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            Log.d("position", "$position")
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}
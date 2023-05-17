package com.baitaplon

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baitaplon.adapter.BookItemRecyclerViewAdapter
import com.baitaplon.databinding.ActivitySearchBinding
import com.baitaplon.model.Book
import java.util.*
import kotlin.collections.ArrayList

class SearchActivity : AppCompatActivity(), BookItemRecyclerViewAdapter.OnItemClickListener {
    lateinit var searchBinding: ActivitySearchBinding
    private lateinit var adapter : BookItemRecyclerViewAdapter
    private var bookList = ArrayList<Book>()
    private var tempBookList = ArrayList<Book>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(searchBinding.root)

        bookList = intent.getSerializableExtra("bookList") as ArrayList<Book>
        tempBookList.addAll(bookList)

        searchBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                tempBookList.clear()
                val searchText = query!!.lowercase(Locale.getDefault())
                if(searchText.isNotEmpty()){
                    bookList.forEach {
                        if(it.name?.lowercase(Locale.getDefault())?.contains(searchText) == true){
                            tempBookList.add(it)
                        }
                    }
                    tempBookList.forEach {
                        Log.e("book temp", it.toString())
                    }
                    adapter.notifyDataSetChanged()
                }
                else{
                    tempBookList.addAll(bookList)
                    adapter.notifyDataSetChanged()
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                tempBookList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if(searchText.isNotEmpty()){
                    bookList.forEach {
                        if(it.name?.lowercase(Locale.getDefault())?.contains(searchText) == true){
                            tempBookList.add(it)
                        }
                    }
                    tempBookList.forEach {
                        Log.e("book temp", it.toString())
                    }
                    adapter.notifyDataSetChanged()
                }
                else{
                    tempBookList.addAll(bookList)
                    adapter.notifyDataSetChanged()
                }
                return false
            }
        })

        adapter = BookItemRecyclerViewAdapter(tempBookList, this@SearchActivity)
        adapter.setBookList(tempBookList)
        val manager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        val itemDecoration = SpaceItemDecoration(25)
        searchBinding.rview.addItemDecoration(itemDecoration)
        searchBinding.rview.layoutManager = manager
        searchBinding.rview.adapter = adapter
    }
    class SpaceItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            // Đặt khoảng cách giữa các item bằng spaceHeight
            outRect.bottom = spaceHeight

            // Bỏ qua khoảng cách cho item cuối cùng
            if (parent.getChildAdapterPosition(view) == parent.adapter?.itemCount?.minus(1)) {
                outRect.bottom = 0
            }
        }
    }
    override fun onItemClick(position: Int) {
        val intent = Intent(this , ItemActivity::class.java)
        intent.putExtra("book", adapter.getBookByPosition(position))
        startActivity(intent)
    }
}
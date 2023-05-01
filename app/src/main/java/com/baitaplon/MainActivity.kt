package com.baitaplon

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baitaplon.adapter.user.BookItemRecyclerViewAdapter
import com.baitaplon.model.Book

class MainActivity : AppCompatActivity(), BookItemRecyclerViewAdapter.OnItemClickListener {

    lateinit var image : ImageView
    lateinit var drawer : DrawerLayout
    lateinit var recyclerview : RecyclerView
    lateinit var adapter : BookItemRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        image = findViewById(R.id.menuIcon)
        drawer = findViewById(R.id.drawer)

        image.setOnClickListener{
            drawer.openDrawer(GravityCompat.START)
        }

        recyclerview = findViewById(R.id.main_layout_recyclerview)

        var bookList = mutableListOf<Book>()
        var newBook1 = Book(0,"Dune","Frank Herbert",12000.0 , true)
        var newBook2 = Book(1,"HARRY PORTER","Frank Herbert",12000.0 , true)
        var newBook3 = Book(2,"MANCHESTER UNITED","Frank Herbert",12000.0 , true)
        var newBook4 = Book(2,"MANCHESTER UNITED","Frank Herbert",12000.0 , true)
        var newBook5 = Book(2,"MANCHESTER UNITED","Frank Herbert",12000.0 , true)
        bookList += newBook1
        bookList += newBook2
        bookList += newBook3
        bookList += newBook4
        bookList += newBook5
        adapter = BookItemRecyclerViewAdapter(bookList,this)
        adapter.setBookList(bookList)
        Toast.makeText(this, "${bookList.size}", Toast.LENGTH_SHORT)
        var manager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        val itemDecoration = SpaceItemDecoration(25)
        recyclerview.addItemDecoration(itemDecoration)
        recyclerview.layoutManager = manager
        recyclerview.adapter = adapter
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

    override fun OnItemClick(position: Int) {
        var intent = Intent(this , ItemActivity::class.java)
        Log.d("Something","HELLO THERE")
        intent.putExtra("book", adapter.getBookByPosition(position))
        startActivity(intent)
    }
}
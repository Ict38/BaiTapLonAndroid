package com.baitaplon

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baitaplon.adapter.user.BookItemRecyclerViewAdapter
import com.baitaplon.model.Book
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), BookItemRecyclerViewAdapter.OnItemClickListener, OnNavigationItemSelectedListener {

    private lateinit var image : ImageView
    private lateinit var drawer : DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView : NavigationView
    private lateinit var recyclerview : RecyclerView
    private lateinit var adapter : BookItemRecyclerViewAdapter

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        image = findViewById(R.id.menuIcon)
        drawer = findViewById(R.id.drawer)

        image.setOnClickListener{
            drawer.openDrawer(GravityCompat.START)
        }

        toggle = ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toggle.syncState()

        navView = findViewById(R.id.navigation_view)
        navView.setNavigationItemSelectedListener(this)

        recyclerview = findViewById(R.id.main_layout_recyclerview)

        val bookList = mutableListOf<Book>()
        val newBook1 = Book(0,"Dune","Frank Herbert",12000.0 , null)
        val newBook2 = Book(1,"HARRY PORTER","Frank Herbert",12000.0 )
        val newBook3 = Book(2,"MANCHESTER UNITED","Frank Herbert",12000.0  )
        val newBook4 = Book(2,"MANCHESTER UNITED","Frank Herbert",12000.0  )
        val newBook5 = Book(2,"MANCHESTER UNITED","Frank Herbert",12000.0 )
        bookList += newBook1
        bookList += newBook2
        bookList += newBook3
        bookList += newBook4
        bookList += newBook5
        adapter = BookItemRecyclerViewAdapter(bookList,this)
        adapter.setBookList(bookList)
        val manager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
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

    override fun onItemClick(position: Int) {
        val intent = Intent(this , ItemActivity::class.java)
        Log.d("Something","HELLO THERE")
        intent.putExtra("book", adapter.getBookByPosition(position))
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.nav_logout -> {
                    auth.signOut()
                    Toast.makeText(applicationContext, "SIGN OUT", Toast.LENGTH_SHORT).show()
                    val mainToLoginIntent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(mainToLoginIntent)
                    finish()
                }
                R.id.nav_search -> {

                }
                R.id.nav_about -> {}
                R.id.nav_feedback -> {}
            }
            drawer.closeDrawer(GravityCompat.START)
            return true
    }
}
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
import com.baitaplon.model.Category
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(), BookItemRecyclerViewAdapter.OnItemClickListener, OnNavigationItemSelectedListener {

    private lateinit var image : ImageView
    private lateinit var drawer : DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView : NavigationView
    private lateinit var recyclerview : RecyclerView
    private lateinit var adapter : BookItemRecyclerViewAdapter
    private var bookList = ArrayList<Book>()
    private var tempBookList = ArrayList<Book>()
    private var categoryList = ArrayList<Category>()
    private var tempCategoryList = ArrayList<Category>()
    private val auth = FirebaseAuth.getInstance()
    private val books = FirebaseDatabase.getInstance().getReference("books")
    private val categories = FirebaseDatabase.getInstance().getReference("categories")


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

        categories.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                tempCategoryList = ArrayList<Category>()
                for(ds in snapshot.children){
                    val newCate = Category(
                        ds.key,
                        ds.value.toString()
                    )
                    Log.e("new Cate", newCate.toString())
                    addNewCategory(newCate)
                }
                syncWithCategoriesList()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        books.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                tempBookList = ArrayList<Book>()

                for(ds in snapshot.children){
                    var tempCateList = ArrayList<Category>()
                    for(cate in ds.child("categories").children){
//                        Log.e("cate", cate.toString())
                        for(i in categoryList){
                            if(i.id.toString() == cate.value){
                                tempCateList.add(i)
                            }
                        }
                    }
                    val newBook = Book(
                        ds.key,
                        ds.child("name").value.toString(),
                        ds.child("author").value.toString(),
                        ds.child("price").value.toString().toInt(),
                        ds.child("description").value.toString(),
                        tempCateList
                    )
                    Log.e("book is", newBook.toString())
                    addnewBook(newBook)
                }
                syncWithBookAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        adapter = BookItemRecyclerViewAdapter(bookList,this@MainActivity)
        val manager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        val itemDecoration = SpaceItemDecoration(25)
        recyclerview.addItemDecoration(itemDecoration)
        recyclerview.layoutManager = manager
        recyclerview.adapter = adapter
    }

    private fun syncWithCategoriesList() {
        categoryList = tempCategoryList
    }


    private fun addNewCategory(newCate: Category) {
        tempCategoryList.add(newCate)
    }

    private fun addnewBook(newBook: Book) {
        tempBookList.add(newBook)
    }

    private fun syncWithBookAdapter() {
        bookList = tempBookList
        adapter.setBookList(bookList)
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
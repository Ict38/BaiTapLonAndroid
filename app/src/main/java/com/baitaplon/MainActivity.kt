package com.baitaplon

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baitaplon.adapter.BookItemRecyclerViewAdapter
import com.baitaplon.model.Book
import com.baitaplon.model.Category
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), BookItemRecyclerViewAdapter.OnItemClickListener, OnNavigationItemSelectedListener {

    private lateinit var image : ImageView
    private lateinit var drawer : DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView : NavigationView
    private lateinit var recyclerview : RecyclerView
    private lateinit var adapter : BookItemRecyclerViewAdapter
    private lateinit var fab : FloatingActionButton
    private var bookList = ArrayList<Book>()
    private var tempBookList = ArrayList<Book>()
    private var categoryList = ArrayList<Category>()
    private var tempCategoryList = ArrayList<Category>()
    private val auth = FirebaseAuth.getInstance()
    private val storageRef = FirebaseStorage.getInstance().reference
    private val ONE_MEGABYTE: Long = 1024 * 1024
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

        categories.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tempCategoryList.clear()
                for(ds in snapshot.children){
                    val newCate = Category(
                        ds.key,
                        ds.value.toString()
                    )
                    addNewCategory(newCate)
                }
                syncWithCategoriesList()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        books.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tempBookList.clear()

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
                        ds.child("price").value.toString().toIntOrNull(),
                        ds.child("description").value.toString(),
                        tempCateList)
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
        val itemDecoration = MainActivity.SpaceItemDecoration(25)
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
        Log.e("imagePath", newBook.id.toString() + ".jpg")
        val imageRef  = storageRef.child(newBook.id + ".jpg")
        imageRef.getBytes(ONE_MEGABYTE)
            .addOnSuccessListener { bytes ->
                // Chuyển đổi bytes thành bitmap
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                newBook.setBitmap(byteArray)
                onBookLoaded()
            }
            .addOnFailureListener { exception ->
                Log.e("UndoneActivity", "Lỗi khi tải xuống ảnh: ${newBook.name.toString()}")
            }
        tempBookList.add(newBook)
    }
    private fun onBookLoaded() {
        syncWithBookAdapter()
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
                val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                val searchBookList = bookList
                searchBookList.forEach {
                    it.bitmap = null
                }
                searchIntent.putExtra("bookList", searchBookList)
                startActivity(searchIntent)
            }
            R.id.nav_about -> {
                val searchIntent = Intent(this@MainActivity, UndoneActivity::class.java)
                startActivity(searchIntent)
            }
            R.id.nav_langue ->{
                showChangeLang()
            }
            R.id.nav_feedback -> {
                val searchIntent = Intent(this@MainActivity, UndoneActivity::class.java)
                startActivity(searchIntent)
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showChangeLang() {
        val listItems = arrayOf("Tiếng Việt", "English")

        val checkedItem = if (getCurrentLocale() == "vn") 0 else 1

        val dialogBuilder = AlertDialog.Builder(this@MainActivity)
        dialogBuilder.setTitle("Choose Language")
        dialogBuilder.setSingleChoiceItems(listItems, checkedItem) { dialog, which ->
            val selectedLang = if (which == 0) "vn" else "en"
            setLocale(this, selectedLang )
            recreate()
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun getCurrentLocale(): String {
        return Locale.getDefault().language
    }

    fun setLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = context.resources
        val configuration = Configuration(resources.configuration)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale)
        } else {
            configuration.locale = locale
        }

        resources.updateConfiguration(configuration, resources.displayMetrics)
    }




    override fun onItemClick(position: Int) {
        val intent = Intent(this , ItemActivity::class.java)
        val book = adapter.getBookByPosition(position)
        book.bitmap = null
        intent.putExtra("book",book)
        startActivity(intent)
    }
}
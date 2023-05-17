package com.baitaplon

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baitaplon.adapter.BookItemRecyclerViewAdapter
import com.baitaplon.adapter.CategoryCheckBoxRecyclerViewAdapter
import com.baitaplon.adapter.CategoryItemRecyclerViewAdapter
import com.baitaplon.databinding.ActivityAddBookBinding
import com.baitaplon.fragment.InfoFragment
import com.baitaplon.model.Category
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddBook : AppCompatActivity(){
    private lateinit var binding: ActivityAddBookBinding

    private var categoryList = ArrayList<Category>()
    private var tempCategoryList = ArrayList<Category>()
    private lateinit var adapter : CategoryCheckBoxRecyclerViewAdapter
    private val categories = FirebaseDatabase.getInstance().getReference("categories")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = CategoryCheckBoxRecyclerViewAdapter(categoryList)
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
        Log.e("Cate size", categoryList.size.toString())
        val manager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val itemDecoration = SpaceItemDecoration(25)
        binding.cateRview.addItemDecoration(itemDecoration)
        binding.cateRview.layoutManager = manager
        binding.cateRview.adapter = adapter

        binding.cancel.setOnClickListener{
            finish()
        }

        binding.addAction.setOnClickListener{
            val newBookRef = FirebaseDatabase.getInstance().getReference("books").push()
            newBookRef.child("name").setValue(binding.bookName.text.toString())
            newBookRef.child("author").setValue(binding.bookAuthor.text.toString())
            newBookRef.child("price").setValue(binding.bookPrice.text.toString().toInt())
            newBookRef.child("description").setValue(binding.bookDes.text.toString())
            val newBookCates = adapter.getCateList()
            newBookCates.forEach{
                val cateRef = newBookRef.child("categories").push()
                cateRef.setValue(it.id)
            }
        }
    }

    private fun syncWithCategoriesList() {
        categoryList = tempCategoryList
        adapter.setCateList(categoryList)
    }


    private fun addNewCategory(newCate: Category) {
        tempCategoryList.add(newCate)
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
}
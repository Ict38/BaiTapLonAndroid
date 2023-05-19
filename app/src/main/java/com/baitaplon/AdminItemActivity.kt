package com.baitaplon

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baitaplon.adapter.CategoryCheckBoxRecyclerViewAdapter
import com.baitaplon.databinding.ActivityAdminItemBinding
import com.baitaplon.model.Book
import com.baitaplon.model.Category
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AdminItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminItemBinding
    private lateinit var book: Book
    private val galleryPick = 1
    private lateinit var imageUri: Uri
    private var tempCategoryList = ArrayList<Category>()
    private lateinit var adapter: CategoryCheckBoxRecyclerViewAdapter
    private lateinit var cateList: ArrayList<Category>
    private val books = FirebaseDatabase.getInstance().getReference("books")

    private val storageRef = FirebaseStorage.getInstance().reference
    private val ONE_MEGABYTE: Long = 1024 * 1024

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        book = intent.getSerializableExtra("book") as Book
        cateList = intent.getSerializableExtra("categories") as ArrayList<Category>
        adapter = CategoryCheckBoxRecyclerViewAdapter(cateList, book.categories!!)
        adapter.setCheckedList(book.categories!!)
        val manager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val itemDecoration = AddBook.SpaceItemDecoration(25)
        binding.cateRview.addItemDecoration(itemDecoration)
        binding.cateRview.layoutManager = manager
        binding.cateRview.adapter = adapter
        adapter.setCateList(cateList)

        binding.img.setOnClickListener {
            openGallery()
        }
        val imageRef = storageRef.child(book?.id + ".jpg")
        imageRef.getBytes(ONE_MEGABYTE)
            .addOnSuccessListener { bytes ->
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                binding.img.setImageBitmap(bitmap)
            }
            .addOnFailureListener { exception ->
                Log.e("UndoneActivity", "Lỗi khi tải xuống ảnh: ${book?.name.toString()}")
            }
        binding.bookName.setText(book?.name)
        binding.bookPrice.setText(book?.price!!.toString())
        binding.bookAuthor.setText(book?.author)
        binding.bookDes.setText(book?.description)

        binding.cancel.setOnClickListener {
            finish()
        }

        binding.updateAction.setOnClickListener {
            if (binding.bookName.text.toString().trim().isEmpty() ||
                binding.bookAuthor.text.toString().trim().isEmpty() ||
                binding.bookPrice.text.toString().trim().isEmpty() ||
                binding.bookDes.text.toString().trim().isEmpty()
            ) {
                Toast.makeText(
                    applicationContext,
                    "Vui lòng điền đủ thông tin sách",
                    Toast.LENGTH_SHORT
                )
            } else if (adapter.getCateList().isEmpty()) {
                Toast.makeText(applicationContext, "Chọn ít nhất một thể loại", Toast.LENGTH_SHORT)
            } else {
                val bookRef =
                    FirebaseDatabase.getInstance().getReference("books").child(book.id.toString())
                bookRef.child("name").setValue(binding.bookName.text.toString())
                bookRef.child("author").setValue(binding.bookAuthor.text.toString())
                bookRef.child("price").setValue(binding.bookPrice.text.toString().toInt())
                bookRef.child("description").setValue(binding.bookDes.text.toString())

                val updates: HashMap<String, Any> = HashMap()

                bookRef.child("categories").removeValue()

                adapter.getCateList().forEach { category ->
                    val categoryKey = bookRef.child("categories").push().key
                    updates[categoryKey.toString()] = category.id.toString()
                }
                Log.e("updates", updates.toString())
                bookRef.child("categories").updateChildren(updates)
                val imgPath = bookRef.key
                Log.e("imgPath", imgPath.toString())
                if (imgPath.toString() != null) {
                    storageRef.child(imgPath.toString() + ".jpg").putFile(imageUri)
                        .addOnSuccessListener {
                            Log.e("Them anh thanh cong", "SUCCESS")
                        }.addOnFailureListener {
                        Log.e("Them anh that bai", "FAILURE")
                    }
                }
                finish()
            }
        }

        binding.deleteAction.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
            val bookRef = database.getReference("books").child(book.id.toString())
            bookRef.removeValue()
            finish()
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, galleryPick)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("DATA", data.toString())
        if (requestCode == galleryPick && resultCode == RESULT_OK && data != null) {
            Log.e("DATA", data.data.toString())
            imageUri = data.data!!
            binding.img.setImageURI(imageUri)
        }
    }
}
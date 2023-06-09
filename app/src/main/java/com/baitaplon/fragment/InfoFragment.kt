package com.baitaplon.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baitaplon.R
import com.baitaplon.adapter.CategoryItemRecyclerViewAdapter
import com.baitaplon.model.Book
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream


class InfoFragment(private val book: Book?) : Fragment() {

    private lateinit var recyclerview : RecyclerView
    private lateinit var adapter : CategoryItemRecyclerViewAdapter
    private val storageRef = FirebaseStorage.getInstance().reference
    private val ONE_MEGABYTE: Long = 1024 * 1024

    private lateinit var titleBookName : TextView
    private lateinit var titleBookAuthor : TextView
    private lateinit var bookPrice : TextView
    private lateinit var bookName : TextView
    private lateinit var bookAuthor : TextView
    private lateinit var bookDes : TextView
    private lateinit var bookImage : ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.info_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleBookName = view.findViewById(R.id.ItemBookName)
        titleBookAuthor = view.findViewById(R.id.ItemBookAuthor)
        bookPrice = view.findViewById(R.id.ItemBookPrice)
        bookName = view.findViewById(R.id.bookName)
        bookAuthor = view.findViewById(R.id.bookAuthor)
        bookDes = view.findViewById(R.id.bookDes)
        bookImage = view.findViewById(R.id.ItemImage)

        titleBookName.text = book?.name
        titleBookAuthor.text = book?.author
        bookPrice.text = book?.price.toString() + "đ"
        bookName.text = book?.name
        bookAuthor.text = book?.author
        bookDes.text = book?.description

        val imageRef  = storageRef.child(book?.id + ".jpg")
        imageRef.getBytes(ONE_MEGABYTE)
            .addOnSuccessListener { bytes ->
                // Chuyển đổi bytes thành bitmap
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                bookImage.setImageBitmap(bitmap)

            }
            .addOnFailureListener { exception ->
                Log.e("UndoneActivity", "Lỗi khi tải xuống ảnh: ${book?.name.toString()}")
            }

        recyclerview = view.findViewById(R.id.cateRecyclerView)
        adapter = CategoryItemRecyclerViewAdapter()
        var cateList = book?.categories


        if (cateList != null) {
            adapter.setCateList(cateList)
        }
        var manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)


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
}
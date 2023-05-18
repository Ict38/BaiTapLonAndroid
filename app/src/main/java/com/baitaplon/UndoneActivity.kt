package com.baitaplon

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.baitaplon.databinding.ActivityUndoneBinding
import com.google.firebase.storage.FirebaseStorage

class UndoneActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUndoneBinding
    val storageRef = FirebaseStorage.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUndoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageRef = storageRef.child("malfurion1.png")
        val ONE_MEGABYTE: Long = 1024 * 1024

        imageRef.getBytes(ONE_MEGABYTE)
            .addOnSuccessListener { bytes ->
                // Chuyển đổi bytes thành bitmap
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                // Lưu bitmap vào biến book
                Log.e("bitmap", bitmap.toString())
                binding.img.setImageBitmap(bitmap)

                // Tiếp tục với các hoạt động khác tại đây
            }
            .addOnFailureListener { exception ->
                Log.e("UndoneActivity", "Lỗi khi tải xuống ảnh: ${exception.message}")
            }
    }
}
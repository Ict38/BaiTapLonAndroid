package com.baitaplon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.baitaplon.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()
    private lateinit var resetPasswordBinding: ActivityResetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resetPasswordBinding = ActivityResetPasswordBinding.inflate(layoutInflater)
        val view = resetPasswordBinding.root
        setContentView(view)

        resetPasswordBinding.submit.setOnClickListener{
            val email = resetPasswordBinding.email.text.toString()
            auth.sendPasswordResetEmail(email).addOnCompleteListener{task ->
                if(task.isSuccessful){
                    Toast.makeText(applicationContext,
                    "Đã gửi email thay đổi mật khẩu đến tài khoản", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
package com.baitaplon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.baitaplon.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    lateinit var signupBinding: ActivitySignupBinding
    val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding = ActivitySignupBinding.inflate(layoutInflater)
        val view = signupBinding.root
        setContentView(view)

        signupBinding.register.setOnClickListener{
            val username = signupBinding.username.text.toString()
            val password = signupBinding.password.text.toString()
            signupWithFirebase(username, password)
        }

        signupBinding.cancel.setOnClickListener{
            finish()
        }
    }

    fun signupWithFirebase(username : String, password : String){
        auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener{task ->

            if(task.isSuccessful){

                Toast.makeText(applicationContext, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                finish()

            }else{
                Toast.makeText(applicationContext, task.exception?.toString(), Toast.LENGTH_SHORT).show()
            }

        }

    }
}
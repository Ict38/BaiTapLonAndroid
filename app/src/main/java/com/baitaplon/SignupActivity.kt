package com.baitaplon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.baitaplon.databinding.ActivitySignupBinding
import com.baitaplon.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {
    private lateinit var signupBinding: ActivitySignupBinding
    private val auth = FirebaseAuth.getInstance()
    private val users = FirebaseDatabase.getInstance().reference.child("users")
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

    private fun signupWithFirebase(username : String, password : String){
        auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener{task ->

            if(task.isSuccessful){
                val user: FirebaseUser? = auth.currentUser
                val newUser = User()
                newUser.setUID(user?.uid.toString())
                newUser.setRole("user")
                users.child(user?.uid.toString()).setValue(newUser)
                Log.e("USER : " , newUser.toString())
                Toast.makeText(applicationContext, R.string.signup_success_notify, Toast.LENGTH_SHORT).show()
                finish()

            }else{
                Toast.makeText(applicationContext, task.exception?.toString(), Toast.LENGTH_SHORT).show()
            }

        }

    }
}
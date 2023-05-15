package com.baitaplon

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.baitaplon.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.values
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding : ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val users = database.reference.child("users")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)
        loginBinding.loginButton.setOnClickListener{
            val username = loginBinding.username.text.toString()
            val password = loginBinding.password.text.toString()
            signingWithFirebase(username, password)
        }

        loginBinding.registerButton.setOnClickListener{
            val intent = Intent(this@LoginActivity , SignupActivity::class.java)
            startActivity(intent)
        }

        loginBinding.forgotpassword.setOnClickListener{
            val forgotPassword = Intent(this@LoginActivity, ResetPasswordActivity::class.java)
            startActivity(forgotPassword)
        }
    }

    private fun signingWithFirebase(email : String, password : String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    var role : String ? = null
                    var roleUser : Boolean = true
                    users?.child(user?.uid.toString())?.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for(ds in snapshot.children){
                                Log.e("DSS", ds.value.toString())
                                role = ds.value.toString()
                                if(role == "admin"){
                                    val adminIntent = Intent(this@LoginActivity, AdminMainActivity::class.java)
                                    startActivity(adminIntent)
                                    finish()
                                }
                                else{
                                    val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                                    startActivity(mainIntent)
                                    finish()
                                }
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        var role : String ? = null
        var roleUser : Boolean = true
        users?.child(user?.uid.toString())?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children){
                    Log.e("DSS", ds.value.toString())
                    role = ds.value.toString()
                    if(role == "admin"){
                        val adminIntent = Intent(this@LoginActivity, AdminMainActivity::class.java)
                        startActivity(adminIntent)
                        finish()
                    }
                    else{
                        val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(mainIntent)
                        finish()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}
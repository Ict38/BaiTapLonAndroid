package com.baitaplon

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.baitaplon.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding : ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()
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
                    Log.d(TAG, "signInWithEmail:success")
                    val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(mainIntent)
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
        if(user != null){
            val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(mainIntent)
        }
    }
}
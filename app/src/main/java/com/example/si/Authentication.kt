package com.example.si

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_authentication.*

class Authentication : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        // Init firebase app
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        // sign in button
        sign_in_button.setOnClickListener { _: View? ->
            startActivity(Intent(this, SignIn::class.java))
        }

        // sign_up_button
        sign_up_button.setOnClickListener { _: View? ->
            startActivity(Intent(this, SignUp::class.java))
        }
    }

    override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser != null) {
            Log.v(this.localClassName, "User already logged in.")
            startActivity(Intent(this, Home::class.java))
            finish()
        }
    }
}
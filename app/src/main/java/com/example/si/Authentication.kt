package com.example.si

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.si.`object`.Configs
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_authentication.*

class Authentication : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        // Init firebase auth
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        // sign in button
        sign_in_button.setOnClickListener { _: View? ->
            startActivityForResult(
                Intent(this, SignIn::class.java),
                Configs.SIGN_IN_SUCCESS_REQUEST_CODE
            )
        }

        // sign_up_button
        sign_up_button.setOnClickListener { _: View? ->
            startActivityForResult(
                Intent(this, SignUp::class.java),
                Configs.SIGN_UP_SUCCESS_REQUEST_CODE
            )
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === Configs.SIGN_IN_SUCCESS_REQUEST_CODE) {
            Log.d(this.localClassName, "Sign in activity returned successfully.")
        }

        if (requestCode === Configs.SIGN_UP_SUCCESS_REQUEST_CODE) {
            Log.d(this.localClassName, "Sign up activity returned successfully.")
        }
    }
}
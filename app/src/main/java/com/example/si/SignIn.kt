package com.example.si

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.si.`object`.Configs
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignIn : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Init firebase auth
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        // sing in button
        sign_in_button.setOnClickListener { _: View? ->
            val password = password_edit_text.text.toString()
            val email = email_edit_text.text.toString()
            signIn(email, password)
        }
    }

    private fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(this.localClassName, "signInWithEmail:success")
                    val user: FirebaseUser = firebaseAuth.getCurrentUser()!!
                    setResult(Configs.SIGN_IN_SUCCESS_REQUEST_CODE)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(this.localClassName, "signInWithEmail:failure", task.exception)
                    setResult(Configs.SIGN_IN_ERROR_REQUEST_CODE)
                    finish()
                }
            }
    }
}
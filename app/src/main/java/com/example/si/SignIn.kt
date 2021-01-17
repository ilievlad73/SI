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
import kotlinx.android.synthetic.main.activity_authentication.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_in.sign_in_button

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
            setResult(Configs.SIGN_IN_SUCCESS_REQUEST_CODE)
            finish()
        }
    }

    private fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(this.localClassName, "signInWithEmail:success")
                    val user: FirebaseUser = firebaseAuth.getCurrentUser()!!
                    startActivity(Intent(this, Home::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(this.localClassName, "signInWithEmail:failure", task.exception)
//                        updateUI(null)
                }
            }
    }
}
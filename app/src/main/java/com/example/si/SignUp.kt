package com.example.si

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUp : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Init firebase app
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun signUp(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(this.localClassName, "createUserWithEmail:success")
                    val user: FirebaseUser = firebaseAuth.getCurrentUser()!!
                    startActivity(Intent(this, Home::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(this.localClassName, "createUserWithEmail:failure", task.exception)
//                        updateUI(null)
                }
            }
    }
}
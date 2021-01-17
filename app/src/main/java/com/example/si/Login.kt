package com.example.si

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class Login : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Init firebase app
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser != null) {
            Log.v(this.localClassName, "User already logged in.")
            startActivity(Intent(this, Main::class.java))
            finish()
        }
    }

    fun signUp(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(this.localClassName, "createUserWithEmail:success")
                    val user: FirebaseUser = firebaseAuth.getCurrentUser()!!
//                        updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(this.localClassName, "createUserWithEmail:failure", task.exception)
//                        updateUI(null)
                }
            }
    }

    fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(this.localClassName, "signInWithEmail:success")
                    val user: FirebaseUser = firebaseAuth.getCurrentUser()!!
//                        updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(this.localClassName, "signInWithEmail:failure", task.exception)
//                        updateUI(null)
                }
            }
    }
}
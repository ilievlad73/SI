package com.example.si

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.example.si.`object`.Configs
import com.example.si.model.User
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUp : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Init firebase auth
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        // Init firebase firestore
        firebaseFirestore = FirebaseFirestore.getInstance()

        // sign up button
        sign_up_button.setOnClickListener { _: View? ->
            val password = password_edit_text.text.toString()
            val email = email_edit_text.text.toString()
            signUp(email, password)
        }
    }

    private fun signUp(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(this.localClassName, "createUserWithEmail:success")
                    // construct firestore user model
                    val user: FirebaseUser = firebaseAuth.getCurrentUser()!!
                    val name = user.displayName!!
                    val email = user.email!!
                    val photoUrl = user.photoUrl.toString()
                    val uid = user.uid
                    val userModel = User(uid, email, name, photoUrl)
                    
                    // add user model into firebase
                    firebaseFirestore.collection(Configs.USER_COLLECTION)
                        .add(userModel)
                        .addOnSuccessListener { docSnap ->
                            Log.d(this.localClassName, "Snapshot added with ID: " + docSnap.id)
                            setResult(Configs.SIGN_IN_SUCCESS_REQUEST_CODE)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Log.w(this.localClassName, "Error adding document", e)
                            setResult(Configs.SIGN_IN_ERROR_REQUEST_CODE)
                            finish()
                        }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(this.localClassName, "createUserWithEmail:failure", task.exception)
//                        updateUI(null)
                }
            }
    }
}
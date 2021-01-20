package com.example.si

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.si.`object`.Configs
import com.example.si.`object`.SavedPreferences
import com.example.si.admin.AdminHome
import com.example.si.model.Admin
import com.example.si.model.User
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_authentication.*

class Authentication : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        // Init firebase auth
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        // Init firebase firestore
        firebaseFirestore = FirebaseFirestore.getInstance()

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
            // Fetch user details
            if (SavedPreferences.getUId(this).isEmpty()) {
                // Details not cached
                Log.v(this.localClassName, "Fetching user details.")
                firebaseFirestore.collection(Configs.USER_COLLECTION).document(firebaseAuth.uid!!)
                    .get().addOnSuccessListener { doc ->
                        if (doc != null) {
                            Log.d(this.localClassName, "Doc data: ${doc.data}")
                            // store user details
                            val user = doc.toObject(User::class.java) as User;
                            if (SavedPreferences.isAdmin(user.role)) {
                                val admin = doc.toObject(Admin::class.java) as Admin
                                SavedPreferences.setAdmin(this, admin)
                                // home
                                startActivity(Intent(this, AdminHome::class.java))
                                finish()
                            } else {
                                SavedPreferences.set(this, user)
                                // maybe finish account configuration
                                if (SavedPreferences.isAccountConfigurationNeeded(this)) {
                                    startActivityForResult(
                                        Intent(this, AccountManagement::class.java),
                                        Configs.ACCOUNT_UPDATE_SUCCESS_REQUEST_CODE
                                    )
                                } else {
                                    // home
                                    startActivity(Intent(this, Home::class.java))
                                    finish()
                                }
                            }
                        } else {
                            Log.d(this.localClassName, "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(this.localClassName, "fetch failed with ", exception)
                    }
            } else {
                if (SavedPreferences.isAdmin(this)) {
                    startActivity(Intent(this, AdminHome::class.java))
                    finish()
                } else {
                    startActivity(Intent(this, Home::class.java))
                    finish()
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === Configs.SIGN_IN_SUCCESS_REQUEST_CODE) {
            Log.d(this.localClassName, "Sign in activity returned successfully.")
            authentication_progress_bar.visibility = View.VISIBLE
        }

        if (requestCode === Configs.SIGN_UP_SUCCESS_REQUEST_CODE) {
            Log.d(this.localClassName, "Sign up activity returned successfully.")
            authentication_progress_bar.visibility = View.VISIBLE
        }

        if (requestCode === Configs.ACCOUNT_UPDATE_SUCCESS_REQUEST_CODE) {
            Log.d(this.localClassName, "Account management activity returned successfully.")
            // this will start anyway due to on start behaviour
        }
    }
}
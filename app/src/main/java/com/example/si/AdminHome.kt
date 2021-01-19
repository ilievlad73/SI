package com.example.si

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.si.`object`.Configs
import com.example.si.`object`.SavedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_admin_home.*

class AdminHome : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        // LOGOUT BUTTON
        admin_logout_button.setOnClickListener {
            firebaseAuth.signOut();
            SavedPreferences.reset(this)
            Log.v(this.localClassName, "Sign out complete.")
            val intent = Intent(this, Authentication::class.java)
            startActivity(intent)
            finish()
        }

        // SETTINGS BUTTON
        admin_setting_button.setOnClickListener {
            startActivityForResult(
                Intent(this, AccountManagement::class.java),
                Configs.ACCOUNT_UPDATE_SUCCESS_REQUEST_CODE
            )
        }

        // Welcome text view
        admin_welcome_text_view.text =
            "Welcome ${SavedPreferences.getEmail(this)}, ${SavedPreferences.getUniversityName(this)}"
    }
}
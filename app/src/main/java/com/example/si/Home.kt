package com.example.si

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.si.`object`.Configs
import com.example.si.`object`.SavedPreferences
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_authentication.*
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Init firebase auth
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        // LOGOUT BUTTON
        logout_button.setOnClickListener {
            firebaseAuth.signOut();
            SavedPreferences.reset(this)
            Log.v(this.localClassName, "Sign out complete.")
            val intent = Intent(this, Authentication::class.java)
            startActivity(intent)
            finish()
        }

        // SETTINGS BUTTON
        setting_button.setOnClickListener {
            startActivityForResult(
                Intent(this, AccountManagement::class.java),
                Configs.ACCOUNT_UPDATE_SUCCESS_REQUEST_CODE
            )
        }

        // Welcome text view
        welcome_text_view.text = "Welcome ${SavedPreferences.getEmail(this)}"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode === Configs.ACCOUNT_UPDATE_SUCCESS_REQUEST_CODE) {
            Log.d(this.localClassName, "Account management activity returned successfully.")
            // this will start anyway due to on start behaviour
        }
    }
}
package com.example.si

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.si.`object`.Configs
import com.example.si.`object`.SavedPreferences
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_authentication.*
import kotlinx.android.synthetic.main.activity_finish_account_configuration.*

class AccountManagement : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish_account_configuration)

        // Init firebase auth
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        // Init firebase firestore
        firebaseFirestore = FirebaseFirestore.getInstance()

        // check if previous settings have been made
        first_name_edit_text.setText(SavedPreferences.getFirstName(this))
        last_name_edit_text.setText(SavedPreferences.getLastName(this))
        address_edit_text.setText(SavedPreferences.getAddress(this))
        cnp_edit_text.setText(SavedPreferences.getCNP(this))

        // select file button
        select_files_button.setOnClickListener { _: View? ->
            val getIntent = Intent()
            getIntent.type = "image/*";
            getIntent.action = Intent.ACTION_GET_CONTENT;
            val chooserIntent = Intent.createChooser(getIntent, "Select file")
            startActivityForResult(chooserIntent, Configs.SELECT_FILES_SUCCESS_REQUEST_CODE);
        }

        // sign_up_button
        finish_button.setOnClickListener { _: View? ->

            val currentUser = SavedPreferences.get(this)
            currentUser.address = address_edit_text.text.toString()
            currentUser.firstName = first_name_edit_text.text.toString()
            currentUser.lastName = last_name_edit_text.text.toString()
            currentUser.cnp = cnp_edit_text.text.toString()

            // add user model into firebase
            firebaseFirestore.collection(Configs.USER_COLLECTION).document(currentUser.uid)
                .set(currentUser)
                .addOnSuccessListener { docSnap ->
                    Log.d(this.localClassName, "Snapshot added with ID: $docSnap")
                    // update local cache
                    SavedPreferences.setAddress(this, address_edit_text.text.toString())
                    SavedPreferences.setFirstName(this, first_name_edit_text.text.toString())
                    SavedPreferences.setLastName(this, last_name_edit_text.text.toString())
                    SavedPreferences.setCNP(this, cnp_edit_text.text.toString())
                    // return
                    setResult(Configs.ACCOUNT_UPDATE_SUCCESS_REQUEST_CODE)
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.w(this.localClassName, "Error adding document", e)
                    setResult(Configs.ACCOUNT_UPDATE_ERROR_REQUEST_CODE)
                    finish()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === Configs.SELECT_FILES_SUCCESS_REQUEST_CODE) {
            Log.d(this.localClassName, "Select files activity returned successfully.")
        }
    }
}
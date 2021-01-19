package com.example.si

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.si.`object`.Configs
import com.example.si.`object`.SavedPreferences
import com.example.si.adapters.FilesAdapter
import com.example.si.uitl.toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_account_management.*
import kotlin.collections.ArrayList




class AccountManagement : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore;
    private lateinit var firebaseStorageReference: StorageReference;
    private lateinit var files: ArrayList<String>;
    private lateinit var filesAdapter: FilesAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_management)

        // Init firebase auth
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        // Init firebase firestore
        firebaseFirestore = FirebaseFirestore.getInstance()

        // Init storage
        firebaseStorageReference = FirebaseStorage.getInstance().reference

        // Init files
        files = SavedPreferences.getFiles(this)

        // check if previous settings have been made
        first_name_edit_text.setText(SavedPreferences.getFirstName(this))
        last_name_edit_text.setText(SavedPreferences.getLastName(this))
        address_edit_text.setText(SavedPreferences.getAddress(this))
        cnp_edit_text.setText(SavedPreferences.getCNP(this))

        // select file button
        select_files_button.setOnClickListener { _: View? ->
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(
                Intent.createChooser(intent, "Select a file"),
                Configs.SELECT_FILES_SUCCESS_REQUEST_CODE
            )
        }

        // sign_up_button
        finish_button.setOnClickListener { _: View? ->

            val currentUser = SavedPreferences.get(this)
            currentUser.address = address_edit_text.text.toString()
            currentUser.firstName = first_name_edit_text.text.toString()
            currentUser.lastName = last_name_edit_text.text.toString()
            currentUser.cnp = cnp_edit_text.text.toString()
            currentUser.files = files

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
                    SavedPreferences.setFiles(this, files.distinct())
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

        // Init friends list
        val layoutManager = LinearLayoutManager(this)
        filesAdapter = FilesAdapter(files, this)
        files_recycler_view.layoutManager = layoutManager
        files_recycler_view.itemAnimator = DefaultItemAnimator()
        files_recycler_view.adapter = filesAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === Configs.SELECT_FILES_SUCCESS_REQUEST_CODE) {
            Log.d(this.localClassName, "Select files activity returned successfully.")

            if (data != null && data.data != null) {
                val uri: Uri? = data.data
                if (uri != null) {
                    Log.d(this.localClassName, "Uri found ${uri.toString()}")
//                    https://stackoverflow.com/questions/36329379/file-not-found-exception-path-from-uri
//                    https://stackoverflow.com/questions/20067508/get-real-path-from-uri-android-kitkat-new-storage-access-framework/20413475#20413475
//                    https://stackoverflow.com/questions/59123162/android-kotlin-getting-a-filenotfoundexception-with-filename-chosen-from-file-p
                    val stream = contentResolver.openInputStream(uri)
                    if (stream != null) {
                        val fileName = getFileName(uri)!!
                        Log.d(this.localClassName, "File found $fileName")
                        val ref = firebaseStorageReference.child(SavedPreferences.getUId(this))
                            .child(fileName)
                        val uploadTask = ref.putStream(stream)
                        uploadTask.addOnFailureListener {
                            // Handle unsuccessful uploads
                            Log.d(this.localClassName, "File upload failed.")
                            this.toast("File upload failed.")
                        }.addOnSuccessListener { taskSnapshot ->
                            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                            Log.d(this.localClassName, "File uploaded successfully.")
                            this.toast("File uploaded successfully.")
                            // sync file names
                            files.add(fileName)
                        }
                    } else {
                        Log.d(this.localClassName, "File not found")
                    }
                }
            }
        }
    }

    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }
}
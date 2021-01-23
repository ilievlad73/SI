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
import com.example.si.model.UserCustomToken
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.HttpsCallableResult
import kotlinx.android.synthetic.main.activity_authentication.*


class Authentication : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore;
    private lateinit var firebaseFunctions: FirebaseFunctions;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        // Init firebase auth
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        // Init firebase firestore
        firebaseFirestore = FirebaseFirestore.getInstance()

        // Init firebase functions
        firebaseFunctions = FirebaseFunctions.getInstance()

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
                                signInAdminWithCustomToken(SavedPreferences.getUId(this))
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
                    signInAdminWithCustomToken(SavedPreferences.getUId(this))
                } else {
                    startActivity(Intent(this, Home::class.java))
                    finish()
                }

            }
        }


    }

    private fun signInAdminWithCustomToken(token: String) {
        customToken(token)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    val e = task.exception
                    if (e is FirebaseFunctionsException) {
                        val code = e.code
                        val details = e.details
                    }
                    Log.d("Cloud functions", "Error")
                    // ...
                } else {
                    Log.d("Cloud functions", "Success")
                    var userCustomToken = task.result!!
                    Log.d("Cloud functions", userCustomToken.customToken)

                    firebaseAuth.signInWithCustomToken(userCustomToken.customToken)
                        .addOnCompleteListener(this) { task ->
                            if (!task.isSuccessful) {
                                Log.d("SignIn custom token", "Failed!")
                            } else {
                                Log.d("SignIn custom token", "Success!")
                                startActivity(Intent(this, AdminHome::class.java))
                                finish()
                            }
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

    private fun customToken(uid: String): Task<UserCustomToken> {
        Log.d("Cloud functions", "started")
        val data = hashMapOf("uid" to uid)
        return firebaseFunctions
            .getHttpsCallable("customToken")
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
//                val fieldOfStudy = document.toObject(FieldOfStudy::class.java) as FieldOfStudy;

                val result = task.result?.data as HashMap<String, String>
                val userCustomToken = UserCustomToken(result["customToken"]!!)
                userCustomToken
            }
    }
}
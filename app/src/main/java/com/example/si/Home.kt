package com.example.si

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.si.`object`.Configs
import com.example.si.`object`.SavedPreferences
import com.example.si.adapters.FilesAdapter
import com.example.si.adapters.ProgramAdapter
import com.example.si.model.FieldOfStudy
import com.example.si.model.Program
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore;
    private var fieldsOfStudy: ArrayList<FieldOfStudy> = ArrayList();
    private lateinit var fieldsOfStudySpinner: Spinner;
    private var selectedFieldOfStudy: FieldOfStudy = FieldOfStudy();

    private var programs: ArrayList<Program> = ArrayList();
    private lateinit var programsAdapter: ProgramAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Init firebase auth
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        // Init firebase firestore
        firebaseFirestore = FirebaseFirestore.getInstance()

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

        // fetch fields of study
        fetchFieldsOfStudy()

        // init programs list
        val layoutManager = LinearLayoutManager(this)
        programsAdapter = ProgramAdapter(programs, this)
        programs_recycler_view.layoutManager = layoutManager
        programs_recycler_view.itemAnimator = DefaultItemAnimator()
        programs_recycler_view.adapter = programsAdapter

        // fetch programs
        fetchPrograms()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === Configs.ACCOUNT_UPDATE_SUCCESS_REQUEST_CODE) {
            Log.d(this.localClassName, "Account management activity returned successfully.")
            // this will start anyway due to on start behaviour
        }
    }

    private fun initFieldsOfStudySpinner(fieldsOfStudyName: List<String>) {
        fieldsOfStudySpinner = findViewById(R.id.user_home_field_of_study_spinner)
        val dataAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, fieldsOfStudyName)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldsOfStudySpinner.adapter = dataAdapter;
        fieldsOfStudySpinner.onItemSelectedListener =
            SpinnerItemSelectedListener(fieldsOfStudy, selectedFieldOfStudy)
    }

    private fun fetchFieldsOfStudy() {
        firebaseFirestore.collection(Configs.FIELD_OF_STUDY_COLLECTION).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(this.localClassName, "${document.id} => ${document.data}")
                    var fieldOfStudy = document.toObject(FieldOfStudy::class.java) as FieldOfStudy;
                    fieldsOfStudy.add(fieldOfStudy)
                }
                selectedFieldOfStudy.name = fieldsOfStudy[0].name
                selectedFieldOfStudy.uid = fieldsOfStudy[0].uid
                initFieldsOfStudySpinner(fieldsOfStudy.map { it -> it.name })
            }
            .addOnFailureListener { exception ->
                Log.d(this.localClassName, "Error getting documents: ", exception)
            }
    }

    private fun fetchPrograms() {
        firebaseFirestore.collection(Configs.PROGRAMS_COLLECTION).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(this.localClassName, "${document.id} => ${document.data}")
                    var program = document.toObject(Program::class.java) as Program;
                    programs.add(program)
                }
                programsAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(this.localClassName, "Error getting documents: ", exception)
            }
    }

    class SpinnerItemSelectedListener(
        private val fieldsOfStudy: List<FieldOfStudy>,
        private val fieldOfStudy: FieldOfStudy
    ) : AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
            fieldOfStudy.name = fieldsOfStudy[pos].name
            fieldOfStudy.uid = fieldsOfStudy[pos].uid
        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            // Another interface callback
        }
    }
}
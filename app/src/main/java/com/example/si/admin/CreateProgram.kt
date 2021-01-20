package com.example.si.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.si.R
import com.example.si.`object`.Configs
import com.example.si.`object`.SavedPreferences
import com.example.si.model.FieldOfStudy
import com.example.si.model.Program
import com.example.si.uitl.toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_program.*

class CreateProgram : AppCompatActivity() {

    private lateinit var firebaseFirestore: FirebaseFirestore;
    private var fieldsOfStudy: ArrayList<FieldOfStudy> = ArrayList<FieldOfStudy>(64);
    private lateinit var fieldsOfStudySpinner: Spinner;
    private var selectedFieldOfStudy: FieldOfStudy = FieldOfStudy();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_program)

        // Init firebase firestore
        firebaseFirestore = FirebaseFirestore.getInstance()

        // get all
        fetchFieldsOfStudy()

        // finish button
        finish_button.setOnClickListener { _: View? ->

            val currentProgram = Program()
            currentProgram.city = program_city_edit_text.text.toString()
            currentProgram.faculty_name = program_faculty_name_edit_text.text.toString()
            currentProgram.tuition_fee = program_tuition_fee_edit_text.text.toString()
            currentProgram.cycle = program_cycle_edit_text.text.toString()
            currentProgram.description = program_description_edit_text.text.toString()
            currentProgram.name = program_name_edit_text.text.toString()
            currentProgram.uid = ""
            currentProgram.university = SavedPreferences.getUniversity(this)
            currentProgram.fieldOfStudy = selectedFieldOfStudy

            firebaseFirestore.collection(Configs.PROGRAMS_COLLECTION).add(currentProgram)
                .addOnSuccessListener { documentReference ->
                    Log.d(
                        this.localClassName,
                        "DocumentSnapshot written with ID: ${documentReference.id}"
                    )
                    firebaseFirestore.collection(Configs.PROGRAMS_COLLECTION)
                        .document(documentReference.id).update("uid", documentReference.id)
                        .addOnSuccessListener {
                            Log.d(
                                this.localClassName,
                                "Doc successfully updated!"
                            )
                            this.toast("Program added!")
                            startActivity(Intent(this, AdminHome::class.java))
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Log.w(
                                this.localClassName,
                                "Error updating document",
                                e
                            )
                        }
                }
                .addOnFailureListener { e ->
                    Log.w(this.localClassName, "Error adding document", e)
                }
        }
    }

    private fun initFieldsOfStudySpinner(fieldsOfStudyName: List<String>) {
        fieldsOfStudySpinner = findViewById(R.id.field_of_study_spinner)
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


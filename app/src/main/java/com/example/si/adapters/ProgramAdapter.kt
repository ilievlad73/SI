package com.example.si.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.si.AccountManagement
import com.example.si.R
import com.example.si.`object`.Configs
import com.example.si.`object`.SavedPreferences
import com.example.si.admin.AdminHome
import com.example.si.model.Application
import com.example.si.model.Program
import com.example.si.uitl.toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*

class ProgramAdapter(
    private var programs: List<Program>,
    private var context: Context,
    private var firestore: FirebaseFirestore
) :
    RecyclerView.Adapter<ProgramAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_program_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val program = programs[position]
        holder.bindFileName(program)
    }

    override fun getItemCount(): Int {
        return programs.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var TAG = "Program View Holder"
        var programName: TextView = view.findViewById(R.id.program_name_text_view)
        var universityName: TextView = view.findViewById(R.id.program_university_name_text_view)
        var cityName: TextView = view.findViewById(R.id.program_city_text_view)
        var facultyName: TextView = view.findViewById(R.id.program_faculty_name_text_view)
        var applyButton: Button = view.findViewById(R.id.program_apply_button)

        fun bindFileName(program: Program) {
            programName.text = "${programName.text}${program.name}"
            universityName.text = "${universityName.text}${program.university.name}"
            cityName.text = "${cityName.text}${program.city}"
            facultyName.text = "${facultyName.text}${program.facultyName}"

            applyButton.setOnClickListener {
                var application = Application(SavedPreferences.get(context), program, "", "pending")
                firestore.collection(Configs.APPLICATION_COLLECTION).add(application)
                    .addOnSuccessListener { doc ->
                        Log.d(TAG, "DocumentSnapshot written with ID: ${doc.id}")
                        firestore.collection(Configs.APPLICATION_COLLECTION)
                            .document(doc.id).update("uid", doc.id)
                            .addOnSuccessListener {
                                Log.d(TAG, "Doc successfully updated!")
                                context.toast("Request send!")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error updating document", e)
                            }
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
            }
        }
    }
}
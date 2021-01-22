package com.example.si.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.si.R
import com.example.si.`object`.Configs
import com.example.si.`object`.SavedPreferences
import com.example.si.model.Application
import com.google.firebase.firestore.FirebaseFirestore

class ApplicationsAdapter(
    private var applications: List<Application>,
    private var context: Context,
    private var firestore: FirebaseFirestore
) :
    RecyclerView.Adapter<ApplicationsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_application_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val program = applications[position]
        holder.bindApplication(program)
    }

    override fun getItemCount(): Int {
        return applications.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var TAG = "Application View Holder"
        var userName: TextView = view.findViewById(R.id.application_user_name_text_view)
        var programName: TextView = view.findViewById(R.id.application_program_name_text_view)
        var facultyName: TextView =
            view.findViewById(R.id.application_program_faculty_name_text_view)
        var acceptButton: Button = view.findViewById(R.id.application_accept_button)
        var rejectButton: Button = view.findViewById(R.id.application_reject_button)

        fun bindApplication(application: Application) {
            programName.text = "${programName.text}${application.program.name}"
            facultyName.text = "${facultyName.text}${application.program.facultyName}"
            userName.text =
                "${userName.text}${application.user.lastName} ${application.user.firstName}"

//            applyButton.setOnClickListener {
//                var application = Application(SavedPreferences.get(context), program, "")
//                firestore.collection(Configs.APPLICATION_COLLECTION).add(application)
//                    .addOnSuccessListener { doc ->
//                        Log.d(TAG, "DocumentSnapshot written with ID: ${doc.id}")
//                        firestore.collection(Configs.APPLICATION_COLLECTION)
//                            .document(doc.id).update("uid", doc.id)
//                            .addOnSuccessListener {
//                                Log.d(TAG,  "Doc successfully updated!")
//                                context.toast("Request send!")
//                            }
//                            .addOnFailureListener { e ->
//                                Log.w(TAG, "Error updating document", e)
//                            }
//                    }
//                    .addOnFailureListener { e ->
//                        Log.w(TAG, "Error adding document", e)
//                    }
//            }
        }
    }
}
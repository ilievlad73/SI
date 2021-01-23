package com.example.si.adapters

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.si.R
import com.example.si.`object`.Configs
import com.example.si.`object`.SavedPreferences
import com.example.si.admin.AdminHome
import com.example.si.model.Application
import com.example.si.uitl.toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_account_management.*

class ApplicationsAdapter(
    private var applications: List<Application>,
    private var context: Context,
    private var firestore: FirebaseFirestore,
    private var firebaseStorageReference: StorageReference
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
        var filesRecyclerView: RecyclerView =
            view.findViewById(R.id.application_files_recycler_view)
        var status: TextView = view.findViewById(R.id.application_status_text_view)


        fun bindApplication(application: Application) {
            programName.text = "${programName.text}${application.program.name}"
            facultyName.text = "${facultyName.text}${application.program.facultyName}"
            userName.text =
                "${userName.text}${application.user.lastName} ${application.user.firstName}"

            if (application.status.compareTo("pending") != 0) {
                // remove buttons and add status text view
                acceptButton.visibility = View.GONE
                rejectButton.visibility = View.GONE
                status.text = application.status.capitalize()
                status.visibility = View.VISIBLE
            } else {
                acceptButton.setOnClickListener {
                    firestore.collection(Configs.APPLICATION_COLLECTION)
                        .document(application.uid).update("status", "accepted")
                        .addOnSuccessListener {
                            Log.d(TAG, "Doc successfully updated!")
                            context.toast("Request response send!")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error updating document", e)
                        }
                }

                rejectButton.setOnClickListener {
                    firestore.collection(Configs.APPLICATION_COLLECTION)
                        .document(application.uid).update("status", "rejected")
                        .addOnSuccessListener {
                            Log.d(TAG, "Doc successfully updated!")
                            context.toast("Request response send!")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error updating document", e)
                        }
                }

            }


            if (application.user.files?.isNotEmpty() == true) {
                // Init files list
                val layoutManager = LinearLayoutManager(context)
                val filesAdapter =
                    ApplicationFileAdapter(
                        application.user.files!!,
                        context,
                        application,
                        firebaseStorageReference
                    )
                filesRecyclerView.layoutManager = layoutManager
                filesRecyclerView.itemAnimator = DefaultItemAnimator()
                filesRecyclerView.adapter = filesAdapter
            }
        }
    }
}
package com.example.si.adapters

import android.content.Context
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.si.R
import com.example.si.model.Application
import com.example.si.uitl.toast
import com.google.firebase.storage.StorageReference
import java.io.File

class ApplicationFileAdapter(
    private var files: List<String>,
    private var context: Context,
    private var application: Application,
    private var firebaseStorageReference: StorageReference
) :
    RecyclerView.Adapter<ApplicationFileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_application_file_name, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fileName = files[position]
        holder.bindFileName(fileName)
    }

    override fun getItemCount(): Int {
        return files.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var TAG = "Application Files View Holder"
        var fileName: TextView = view.findViewById(R.id.application_file_name_text_view)
        var downloadFile: Button = view.findViewById(R.id.application_file_download_button)

        fun bindFileName(name: String) {
            fileName.text = name
            downloadFile.setOnClickListener {
                // get applicatnt name for folder path
                var applicantName = application.user.firstName + application.user.firstName
                val rootPath = File(context.getExternalFilesDir(null), applicantName)
                if (!rootPath.exists()) rootPath.mkdirs()
                // create file
                val file = File(rootPath, name)
                firebaseStorageReference.child(application.user.uid).child(name).getFile(file)
                    .addOnSuccessListener {
                        // Local temp file has been created
                        Log.d(TAG, "File downloaded ${file.toString()}")
                        context.toast("Downloaded!")
                    }.addOnFailureListener {
                        // Handle any errors
                        Log.d(TAG, "File download failed")
                        context.toast("Download failed!")
                    }
            }
        }
    }
}
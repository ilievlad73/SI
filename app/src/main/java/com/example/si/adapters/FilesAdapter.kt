package com.example.si.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.si.R

class FilesAdapter(private var files: List<String>, var context: Context) :
    RecyclerView.Adapter<FilesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_file_name, parent, false)
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
        var friendName: TextView = view.findViewById(R.id.file_name_text_view)

        fun bindFileName(fileName: String) {
            friendName.text = fileName
        }
    }
}
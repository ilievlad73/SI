package com.example.si.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.si.R
import com.example.si.model.Program

class ProgramAdapter(private var programs: List<Program>, var context: Context) :
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
        var programName: TextView = view.findViewById(R.id.program_name_text_view)
        var universityName: TextView = view.findViewById(R.id.program_university_name_text_view)
        var cityName: TextView = view.findViewById(R.id.program_city_text_view)
        var facultyName: TextView = view.findViewById(R.id.program_faculty_name_text_view)

        fun bindFileName(program: Program) {
            programName.text = "${programName.text}${program.name}"
            universityName.text = "${universityName.text}${program.university.name}"
            cityName.text = "${cityName.text}${program.city}"
            facultyName.text = "${facultyName.text}${program.facultyName}"
        }
    }
}
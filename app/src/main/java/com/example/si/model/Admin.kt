package com.example.si.model

data class Admin(
    val uid: String = "",
    val email: String = "",
    val role: String = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var cnp: String? = "",
    var address: String? = "",
    var files: List<String>? = ArrayList<String>(0),
    var university: University? = University("","")
)

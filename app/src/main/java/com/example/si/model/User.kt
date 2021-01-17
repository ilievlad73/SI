package com.example.si.model

data class User(
    val uid: String = "",
    val email: String = "",
    val role: String = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var cnp: String? = "",
    var address: String? = "",
    var files: ArrayList<String>? = ArrayList<String>(0)
)

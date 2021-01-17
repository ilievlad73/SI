package com.example.si.model

data class User(
    val uid:String = "",
    val email: String = "",
    val firstName: String? = "",
    val lastName: String? = "",
    val cnp: String? = "",
    val address: String? = "",
    val files: ArrayList<String>? = ArrayList<String>(0)
)

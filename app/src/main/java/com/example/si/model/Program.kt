package com.example.si.model

data class Program(
    var university: University = University(),
    var faculty_name: String = "",
    var city: String = "",
    var tuition_fee: String = "",
    var fieldOfStudy: FieldOfStudy = FieldOfStudy(),
    var cycle: String = "",
    var description:String="",
    var uid: String ="",
    var name: String = ""
)

package com.example.si.model

data class Program(
    var university: University = University(),
    var facultyName: String = "",
    var city: String = "",
    var tuitionFee: String = "",
    var fieldOfStudy: FieldOfStudy = FieldOfStudy(),
    var cycle: String = "",
    var description:String="",
    var uid: String ="",
    var name: String = ""
)

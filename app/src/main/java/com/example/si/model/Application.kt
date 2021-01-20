package com.example.si.model

data class Application(
    val user: User = User(),
    val program: Program = Program(),
    val university: University = University(),
    val uid: String = "",
    val createdAt: String = ""
)

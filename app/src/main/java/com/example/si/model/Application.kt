package com.example.si.model

data class Application(
    val user: User = User(),
    val program: Program = Program(),
    val uid: String = "",
    val status: String = "",
)

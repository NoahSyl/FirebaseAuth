package com.noahsyl.firebasenotes.model

data class NotesState(
    val emailUser: String = "",
    val title: String = "",
    val note: String = "",
    val date: String = "",
    val idDoc: String = "",
    val imagePath : String = ""
)

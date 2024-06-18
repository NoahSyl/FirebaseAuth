package com.noahsyl.firebasenotes.model


data class UserModel( //Datos para loggeo
    val userId: String,
    val email: String,
    val userName: String
) {
    fun toMap(): MutableMap<String, Any>{ //funcion para mapear los datos para Firebase
        return mutableMapOf(
            "userId" to this.userId,
            "email" to this.email,
            "userName" to this.userName
        )
    }
}
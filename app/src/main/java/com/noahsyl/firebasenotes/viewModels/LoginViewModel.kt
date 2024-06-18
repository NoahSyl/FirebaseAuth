package com.noahsyl.firebasenotes.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import com.noahsyl.firebasenotes.model.UserModel

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth //Servicio de autenticacion de Firebase
    var showAlert by mutableStateOf(false)
        private set


    fun login(email: String, password: String, onSuccess: () -> Unit) {

        viewModelScope.launch { //lo lanzamos por corutina
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task -> //cuando acaba de hacer la autenticacion
                        if (task.isSuccessful) { //si ha funcionado
                            onSuccess()
                        } else {
                            showAlert = true
                            Log.d("FIREAPP", "Problema de autenticacion")
                        }
                    }
            } catch (e: Exception) {
                Log.d("FIREAPP", "Error en el uso de Firebase")

            }
        }

    }

    private fun saveUser(username: String) {
        val email = auth.currentUser?.email
        val id = auth.currentUser?.uid

        val user = UserModel(id.toString(), email.toString(), username)
        viewModelScope.launch(Dispatchers.IO) {
            FirebaseFirestore.getInstance().collection("Users")
                .add(user)
                .addOnSuccessListener {
                    Log.i("FIREAPP", "Usuario guardado")
                }
                .addOnFailureListener() {
                    Log.i("FIREAPP", "Usuarion NO guardado")
                }
        }
    }

    fun closeAlert() {
        showAlert = false
    }

    fun createUser(email: String, password: String, username: String, onSuccess: () -> Unit) {

        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password)
                saveUser(username)
                onSuccess()
            } catch (e: Exception) {
                Log.d("FIREAPP", "Error al crear usuario")
            }
        }

    }


}
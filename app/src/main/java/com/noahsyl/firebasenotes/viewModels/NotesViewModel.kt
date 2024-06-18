package com.noahsyl.firebasenotes.viewModels

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.noahsyl.firebasenotes.model.NotesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class NotesViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val firestore = Firebase.firestore
    private val storageRef = FirebaseStorage.getInstance().reference

    private var _notesData = MutableStateFlow<List<NotesState>>(emptyList())
    val notesData: StateFlow<List<NotesState>> = _notesData

    var state by mutableStateOf(NotesState())
        private set

    fun fetchNotes() {
        val email = auth.currentUser?.email
        firestore.collection("Notes")
            .whereEqualTo("emailUser", email.toString())
            .addSnapshotListener { data, error ->
                if (error != null) {
                    return@addSnapshotListener //si hay error salgo
                }
                val documents = mutableListOf<NotesState>()
                if (data != null) {
                    for (doc in data) {
                        val myDocument = doc.toObject(NotesState::class.java).copy(idDoc = doc.id)
                        documents.add(myDocument)
                    }
                }
                _notesData.value = documents
            }
    }

     fun saveNote(title: String, note: String, image: Uri, onSucces: () -> Boolean) {




        val email = auth.currentUser?.email

        viewModelScope.launch {

            val imagePath = uploadImage(image)

            try {

                val newNote = hashMapOf(
                    "title" to title,
                    "note" to note,
                    "date" to formatDate(),
                    "emailUser" to email,
                    "imagePath" to imagePath
                )

                firestore.collection("Notes").add(newNote)
                    .addOnSuccessListener {
                        onSucces
                    }

            } catch (e: Exception) {

                Log.d("FIREAPP", "Problema almacenamiento")

            }
        }

    }

    private suspend fun uploadImage(image: Uri): String? {

       return try {

            val imageRef = storageRef.child("images/${UUID.randomUUID()}")
            val taskUpload = imageRef.putFile(image).await()
            val downloadUri = taskUpload.metadata?.reference?.downloadUrl?.await()
            downloadUri.toString()
        } catch (e: Exception) {

            ""

        }

    }

    private fun formatDate(): String? {

        val currentDate: Date = Calendar.getInstance().time
        val res = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        return res.format(currentDate)

    }
}
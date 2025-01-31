package com.noahsyl.firebasenotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.noahsyl.firebasenotes.navigation.NavManager
import com.noahsyl.firebasenotes.ui.theme.FirebaseNotesTheme
import com.noahsyl.firebasenotes.viewModels.LoginViewModel
import com.noahsyl.firebasenotes.viewModels.NotesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginVM : LoginViewModel by viewModels()
        val notesVM : NotesViewModel by viewModels()
        setContent {
            FirebaseNotesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavManager(loginVM, notesVM)
                }
            }
        }
    }
}

package com.noahsyl.firebasenotes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.noahsyl.firebasenotes.viewModels.LoginViewModel
import com.noahsyl.firebasenotes.viewModels.NotesViewModel
import com.noahsyl.firebasenotes.views.login.TabsView
import com.noahsyl.firebasenotes.views.notes.AddNoteView
import com.noahsyl.firebasenotes.views.notes.HomeView

@Composable
fun NavManager(loginVM: LoginViewModel, notesVM: NotesViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Login" ){
        composable("Login"){
            TabsView(navController, loginVM)
        }
        composable("Home"){
            HomeView(navController, notesVM)
        }

        composable("AddNoteView"){
            AddNoteView(navController = navController, notesVM = notesVM)
        }
    }
}
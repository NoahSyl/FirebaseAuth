package com.noahsyl.firebasenotes.views.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.noahsyl.firebasenotes.viewModels.LoginViewModel

@Composable
fun TabsView(navController: NavController, loginVM: LoginViewModel) {

    var selectedTab by remember {
        mutableStateOf(0)
    }
    val tabs = listOf("Login", "Registro")

    Column {
        TabRow(
            selectedTabIndex = selectedTab,
            contentColor = MaterialTheme.colorScheme.secondary,
            containerColor = MaterialTheme.colorScheme.onSecondary
        ) {

            tabs.forEachIndexed { index, tittle ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(text = tittle) }
                )


            }

        }

        when (selectedTab) {
            0 -> LoginView(navController = navController, loginVM = loginVM)
            1 -> RegisterView(navController = navController, loginVM = loginVM)
        }
    }

}
















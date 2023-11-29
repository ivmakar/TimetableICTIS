package ru.ivmak.raspisanie_iktib.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.ivmak.search.ui.SearchScene

@Composable
fun NavigationScene() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Timetable.route
    ) {
        composable(Screen.Timetable.route) {
            MainScene(navController)
        }
        composable(Screen.Search.route) {
            SearchScene(navController)
        }
    }
}

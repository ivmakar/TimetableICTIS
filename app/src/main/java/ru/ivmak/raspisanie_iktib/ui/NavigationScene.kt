package ru.ivmak.raspisanie_iktib.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.ivmak.search.ui.SearchScene

@Composable
fun NavigationScene(startGroupId: String) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "${Screen.Timetable.route}/{group}"
    ) {
        composable(
            "${Screen.Timetable.route}/{group}",
            arguments = listOf(navArgument("group") { type = NavType.StringType; defaultValue = startGroupId })
        ) {
            MainScene(navController)
        }
        composable(Screen.Search.route) {
            SearchScene(navController) {
                val options = NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .build()
                navController.navigate("${Screen.Timetable.route}/$it", options)
            }
        }
    }
}

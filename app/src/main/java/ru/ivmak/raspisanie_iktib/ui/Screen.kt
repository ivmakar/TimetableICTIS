package ru.ivmak.raspisanie_iktib.ui

open class Screen(val route : String) {
    object Timetable : Screen("timetable_route")
    object Search : Screen("search_route")
    object Favorite : Screen("favorite_route")
}

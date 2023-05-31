package com.example.composesubmissionreal.ui.navigation

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object About: Screen("about")
    object Detail: Screen("home/{id}") {
        fun createRoute(id:Long) = "home/$id"
    }
}
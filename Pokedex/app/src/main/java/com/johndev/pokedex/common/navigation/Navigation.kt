package com.johndev.pokedex.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.johndev.pokedex.ui.screens.DetailScreen
import com.johndev.pokedex.ui.screens.PokedexScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.PokedexScreen.route
    ) {
        composable(Routes.PokedexScreen.route) {
            PokedexScreen()
        }
        composable(Routes.DetailScreen.route) {
            DetailScreen()
        }
    }
}
package com.johndev.pokedex.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
            PokedexScreen() { id ->
                navController.navigate(Routes.DetailScreen.createRoute(id))
            }
        }
        composable(
            Routes.DetailScreen.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) {
            DetailScreen(it.arguments?.getInt("id") ?: 0) {
                navController.popBackStack()
            }
        }
    }
}
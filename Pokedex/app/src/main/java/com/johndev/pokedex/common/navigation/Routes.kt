package com.johndev.pokedex.common.navigation

sealed class Routes(val route: String) {

    data object PokedexScreen : Routes("pokedex")
    data object DetailScreen : Routes("detail")

}

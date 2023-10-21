package com.johndev.pokedex.common.entities

import com.google.gson.annotations.SerializedName

data class PokemonEntity(
    var id: Int,
    var name: String,
    var height: Int,
    var weight: Double,
    var types: List<Types>,
    var sprites: Sprites,
    var stats: Stats
)

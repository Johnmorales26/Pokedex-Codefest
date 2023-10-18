package com.johndev.pokedex.common.entities

import com.google.gson.annotations.SerializedName

data class PokemonEntity(
    var id: Int,
    var name: String,
    var height: Int,
    @SerializedName("weight")
    var peso: Double,
    var types: List<Type>,
    var sprites: Sprites
)

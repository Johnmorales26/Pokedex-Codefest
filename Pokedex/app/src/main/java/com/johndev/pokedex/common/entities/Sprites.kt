package com.johndev.pokedex.common.entities

import com.google.gson.annotations.SerializedName

data class Sprites(
    @SerializedName("front_default")
    var image: String,
)

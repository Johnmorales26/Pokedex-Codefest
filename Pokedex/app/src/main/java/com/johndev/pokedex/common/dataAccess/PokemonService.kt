package com.johndev.pokedex.common.dataAccess

import com.johndev.pokedex.common.entities.PokemonEntity
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonService {

    @GET("pokemon/{id}")
    suspend fun getPokemonInfo(@Path("id") id: String): PokemonEntity

}
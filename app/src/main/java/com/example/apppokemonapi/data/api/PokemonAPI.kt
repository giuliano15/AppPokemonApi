package com.example.apppokemonapi.data.api

import com.example.apppokemon.PokemonResponse
import com.example.apppokemonapi.data.dto.PokemonDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {
    @GET("pokemon")
    suspend fun getPokemons(@Query("limit") limit: Int): Response<PokemonResponse>

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(@Path("id") id: Int): Response<PokemonDetail>
}

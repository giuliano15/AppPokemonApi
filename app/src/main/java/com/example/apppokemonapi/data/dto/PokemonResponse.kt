package com.example.apppokemon

data class PokemonResponse(
    val results: List<Result>
) {
    data class Result(
        val name: String,
        val url: String
    )
}

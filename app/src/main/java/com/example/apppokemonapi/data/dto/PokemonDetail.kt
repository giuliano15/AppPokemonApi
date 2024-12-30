package com.example.apppokemonapi.data.dto

// PokemonDetail.kt
data class PokemonDetail(
    val id: Int,
    val name: String,
    val weight: Int,
    val height: Int,
    val sprites: Sprites,
    val types: List<Type>,
    val stats: List<Stat>
)

data class Sprites(
    val other: Other
)

data class Other(
    val home: Home
)

data class Home(
    val front_default: String
)

data class Type(
    val type: TypeDetail
)

data class TypeDetail(
    val name: String
)

data class Stat(
    val base_stat: Int
)


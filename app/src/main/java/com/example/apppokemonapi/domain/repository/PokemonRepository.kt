package com.example.apppokemonapi.domain.repository

import com.example.apppokemon.Pokemon
import com.example.apppokemon.PokemonM

interface PokemonRepository {
   suspend fun recuperarPokemons(): List<Pokemon>
   suspend fun recuperarDetalhesPokemon(id: Int): PokemonM?
}
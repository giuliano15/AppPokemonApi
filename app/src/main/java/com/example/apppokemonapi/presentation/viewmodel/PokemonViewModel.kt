package com.example.apppokemonapi.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apppokemon.PokemonM
import com.example.apppokemon.toPokemonM
import com.example.apppokemonapi.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _pokemons = MutableLiveData<List<PokemonM>>()
    val pokemons: LiveData<List<PokemonM>> get() = _pokemons

    private val _pokemonDetail = MutableLiveData<PokemonM?>()
    val pokemonDetail: LiveData<PokemonM?> get() = _pokemonDetail

    fun fetchPokemons() {
        viewModelScope.launch {
            val pokemonList = pokemonRepository.recuperarPokemons().map { it.toPokemonM() }
            _pokemons.value = pokemonList
        }
    }

    fun fetchPokemonDetail(id: Int) {
        viewModelScope.launch {
            val pokemonDetail = pokemonRepository.recuperarDetalhesPokemon(id)
            _pokemonDetail.value = pokemonDetail
        }
    }
}


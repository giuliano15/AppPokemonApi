package com.example.apppokemonapi.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import com.example.apppokemon.Pokemon
import com.example.apppokemon.PokemonM
import com.example.apppokemonapi.data.api.PokemonApi
import com.example.apppokemonapi.domain.repository.PokemonRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import javax.inject.Inject


class PokemonRepositoryImpl @Inject constructor(
    private val pokemonAPI: PokemonApi,
    private val context: Context // Injetando o contexto no construtor
) : PokemonRepository {

    override suspend fun recuperarPokemons(): List<Pokemon> {
        return try {
            // Verifica a conectividade antes de prosseguir
            if (!isNetworkAvailable()) {
                Toast.makeText(
                    context,
                    "Falha ao carregar dados do Pokémon. Por favor, verifique sua conexão de internet.",
                    Toast.LENGTH_LONG
                ).show()
                return emptyList()
            }

            val respostaPokemonAPI = pokemonAPI.getPokemons(151)

            if (respostaPokemonAPI.isSuccessful && respostaPokemonAPI.body() != null) {
                // Lista para armazenar os Deferreds de cada Pokémon
                val pokemonsDeferred = respostaPokemonAPI.body()!!.results.map { result ->
                    // Usar async para iniciar cada chamada de rede em paralelo
                    GlobalScope.async {
                        try {
                            val respostaPokemonDetail =
                                pokemonAPI.getPokemonDetail(extractIdFromUrl(result.url))
                            val pokemonDetail = respostaPokemonDetail.body()!!

                            Pokemon(
                                id = extractIdFromUrl(result.url),
                                name = result.name,
                                imageUrl = pokemonDetail.sprites.other.home.front_default,
                                types = pokemonDetail.types.map { it.type.name },
                                weight = pokemonDetail.weight.toString(),
                                height = pokemonDetail.height.toString(),
                                stat1 = pokemonDetail.stats[0].base_stat.toString(),
                                stat2 = pokemonDetail.stats[1].base_stat.toString(),
                                stat3 = pokemonDetail.stats[2].base_stat.toString(),
                                stat4 = pokemonDetail.stats[3].base_stat.toString(),
                                stat5 = pokemonDetail.stats[4].base_stat.toString(),
                                stat6 = pokemonDetail.stats[5].base_stat.toString()
                            )
                        } catch (e: Exception) {
                            // Lidar com exceções individualmente se necessário
                            e.printStackTrace()
                            null // Ou outro valor padrão em caso de erro
                        }
                   }
                }

                // Aguardar até que todas as tarefas assíncronas tenham terminado
                pokemonsDeferred.mapNotNull { it.await() }
            } else {
                emptyList()
            }
        } catch (erroRecuperarPokemons: Exception) {
            erroRecuperarPokemons.printStackTrace()
            emptyList()
        }
    }

    override suspend fun recuperarDetalhesPokemon(id: Int): PokemonM? {
        return try {
            // Verifica a conectividade antes de prosseguir
            if (!isNetworkAvailable()) {
                Toast.makeText(
                    context,
                    "Falha ao carregar detalhes do Pokémon. Por favor, verifique sua conexão de internet.",
                    Toast.LENGTH_LONG
                ).show()
                return null
            }

            val respostaPokemonDetail = pokemonAPI.getPokemonDetail(id)

            if (respostaPokemonDetail.isSuccessful && respostaPokemonDetail.body() != null) {
                val pokemonDetail = respostaPokemonDetail.body()!!

                PokemonM(
                    id = pokemonDetail.id,
                    name = pokemonDetail.name,
                    imageUrl = pokemonDetail.sprites.other.home.front_default,
                    types = pokemonDetail.types.map { it.type.name },
                    weight = pokemonDetail.weight.toString(),
                    height = pokemonDetail.height.toString(),
                    stat1 = pokemonDetail.stats[0].base_stat.toString(),
                    stat2 = pokemonDetail.stats[1].base_stat.toString(),
                    stat3 = pokemonDetail.stats[2].base_stat.toString(),
                    stat4 = pokemonDetail.stats[3].base_stat.toString(),
                    stat5 = pokemonDetail.stats[4].base_stat.toString(),
                    stat6 = pokemonDetail.stats[5].base_stat.toString()
                )
            } else {
                null
            }
        } catch (erroRecuperarDetalhesPokemon: Exception) {
            erroRecuperarDetalhesPokemon.printStackTrace()
            null
        }
    }

    private fun extractIdFromUrl(url: String): Int {
        return url.split("/").filter { it.isNotEmpty() }.last().toInt()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}


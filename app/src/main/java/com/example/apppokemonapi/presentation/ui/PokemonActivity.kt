package com.example.apppokemonapi.presentation.ui

import PokemonAdapter
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.apppokemon.PokemonM
import com.example.apppokemonapi.R
import com.example.apppokemonapi.databinding.ActivityPokemonBinding
import com.example.apppokemonapi.presentation.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonActivity : AppCompatActivity() {

    private val pokemonList = mutableListOf<PokemonM>()
    private lateinit var binding: ActivityPokemonBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var viewModel: PokemonViewModel
    private lateinit var adapter: PokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicialize o ProgressDialog
        progressDialog = ProgressDialog(this).apply {
            setMessage("Loading Pokémon...")
            setCancelable(false)
        }

        // Inicialize a RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicialize o Adapter
        adapter = PokemonAdapter(emptyList(), object : OnItemClickListener {
            override fun onItemClick(pokemon: PokemonM) {
                val intent = Intent(this@PokemonActivity, PokemonDetailActivity::class.java)
                intent.putExtra("pokemon_id", pokemon.id)
                startActivity(intent)
                binding.searchView.setQuery("", false)

            }
        })
        binding.recyclerView.adapter = adapter

        // Obtenha a instância do ViewModel
        viewModel = ViewModelProvider(this).get(PokemonViewModel::class.java)

        // Observe as mudanças na lista de Pokémons
        viewModel.pokemons.observe(this, Observer { pokemonList ->
            // Ocultar o ProgressDialog quando os dados são carregados
            progressDialog.dismiss()
            this.pokemonList.clear()
            this.pokemonList.addAll(pokemonList)
            adapter.updateList(this.pokemonList) // Atualiza o adaptador com a lista completa
        })

        // Carregue a lista de Pokémons
        viewModel.fetchPokemons()
        progressDialog.show()

        setupSearchView()
    }

    private fun setupSearchView() {
        with(binding.searchView) {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val query = newText.orEmpty().lowercase()
                    filterPokemonList(query)
                    return true
                }
            })
        }
    }

    private fun filterPokemonList(query: String) {
        // Converte a consulta em letras minúsculas para garantir uma comparação case-insensitive.
        val trimmedQuery = query.lowercase()

        // Filtra a lista de Pokémons com base na consulta.
        val filteredList = if (trimmedQuery.isEmpty()) {
            // Se a consulta estiver vazia, retorna a lista completa de Pokémons.
            pokemonList.toList() // Retorna uma cópia da lista completa
        } else {
            // Caso contrário, filtra a lista de Pokémons com base na consulta.
            pokemonList.filter {
                // Verifica se o nome ou o id do Pokémon corresponde à consulta.
                it.name.lowercase().contains(trimmedQuery) ||
                        it.id.toString().contains(trimmedQuery)
            }.sortedWith(
                // Ordena a lista filtrada com base em dois critérios:
                compareBy(
                    // Primeiro, Pokémons cujo nome começa com a consulta aparecem antes.
                    { !it.name.lowercase().startsWith(trimmedQuery) },
                    // Segundo, Pokémons cujo nome ou id contém a consulta aparecem mais cedo.
                    {
                        if (it.name.lowercase().contains(trimmedQuery)) {
                            it.name.lowercase().indexOf(trimmedQuery)
                        } else {
                            it.id.toString().indexOf(trimmedQuery)
                        }
                    }
                )
            )
        }

        // Atualiza o adaptador com a lista filtrada
        adapter.updateList(filteredList)
    }
}

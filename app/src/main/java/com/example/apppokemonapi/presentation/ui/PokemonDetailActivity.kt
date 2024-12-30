package com.example.apppokemonapi.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.apppokemon.Pokemon
import com.example.apppokemon.PokemonM
import com.example.apppokemonapi.R
import com.example.apppokemonapi.databinding.ActivityPokemonDetailBinding
import com.example.apppokemonapi.presentation.viewmodel.PokemonViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonDetailBinding

    private val viewModel: PokemonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pokemonId = intent.getIntExtra("pokemon_id", -1)

        if (pokemonId != -1) {
            viewModel.fetchPokemonDetail(pokemonId)
        }

        viewModel.pokemonDetail.observe(this, Observer { pokemon ->
            pokemon?.let { updateUI(it) }
        })
    }

    private fun updateUI(pokemon: PokemonM) {
        with(binding) {
            Glide.with(this@PokemonDetailActivity).load(pokemon.imageUrl).into(imageView)
            nameTextView.text = pokemon.name
            weightTextnumber.text = pokemon.weight
            heightTextnumber.text = pokemon.height
            typeChipGroup.removeAllViews()
            //aqui passo no chipGroup ao inves de lista
            pokemon.types.forEach { type ->
                val chip = Chip(this@PokemonDetailActivity).apply { text = type }
                typeChipGroup.addView(chip)
            }
            determinateBar1.progress = pokemon.stat1.toIntOrNull() ?: 0
            determinateBar2.progress = pokemon.stat2.toIntOrNull() ?: 0
            determinateBar3.progress = pokemon.stat3.toIntOrNull() ?: 0
            determinateBar4.progress = pokemon.stat4.toIntOrNull() ?: 0
            determinateBar5.progress = pokemon.stat5.toIntOrNull() ?: 0
            determinateBar6.progress = pokemon.stat6.toIntOrNull() ?: 0
        }
    }
}


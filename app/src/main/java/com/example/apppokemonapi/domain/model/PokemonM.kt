package com.example.apppokemon

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonM(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
    val weight: String,
    val height: String,
    val stat1: String,
    val stat2: String,
    val stat3: String,
    val stat4: String,
    val stat5: String,
    val stat6: String

) : Parcelable
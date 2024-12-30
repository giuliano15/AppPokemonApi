package com.example.apppokemon

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pokemon(
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

fun Pokemon.toPokemonM() : PokemonM {
    return PokemonM(
        name = this.name,
        id = this.id,
        imageUrl = this.imageUrl,
        types = this.types,
        weight = this.weight,
        height = this.height,
        stat1 = this.stat1.ifEmpty { "0" },
        stat2 = this.stat2.ifEmpty { "0" },
        stat3 = this.stat3.ifEmpty { "0" },
        stat4 = this.stat4.ifEmpty { "0" },
        stat5 = this.stat5.ifEmpty { "0" },
        stat6 = this.stat6.ifEmpty { "0" }
    )
}
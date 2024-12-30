package com.example.apppokemonapi.presentation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.apppokemonapi.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btnCall).setOnClickListener {
            GoToPokemonActivity(it)
        }
    }

    fun GoToPokemonActivity(view: View?) {
        val openPokemonActivity = Intent(this, PokemonActivity::class.java)
        startActivity(openPokemonActivity)
    }
}
package com.example.pokedex.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pokedex.model.Pokemon
import com.example.pokedex.ui.card.PokemonCard
import com.example.pokedex.ui.empty.EmptyStateScreen
import com.example.pokedex.ui.filter.TypeFilterRow

@Composable
fun PokemonMainScreen(
    pokemonList: List<Pokemon>,
    modifier: Modifier = Modifier
) {
    var selectedType by rememberSaveable { mutableStateOf("All") }

    val availableTypes = listOf(
        "All", "Grass", "Fire", "Water", "Electric", "Poison", "Ice", "Ghost", "Normal","Fairy"
    )

    val filteredList = if (selectedType == "All") {
        pokemonList
    } else {
        pokemonList.filter { pokemon -> pokemon.types.contains(selectedType) }
    }

    Column(modifier = modifier.fillMaxSize()) {
        TypeFilterRow(
            types = availableTypes,
            selectedType = selectedType,
            onTypeSelected = { selectedType = it }
        )

        if (filteredList.isEmpty()) {
            EmptyStateScreen(onResetFilter = { selectedType = "All" })
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredList) { pokemon ->
                    PokemonCard(pokemon = pokemon)
                }
            }
        }
    }
}

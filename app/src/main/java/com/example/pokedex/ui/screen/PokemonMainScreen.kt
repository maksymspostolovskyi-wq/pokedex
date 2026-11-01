package com.example.pokedex.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokedex.ui.card.PokemonCard
import com.example.pokedex.ui.empty.EmptyStateScreen
import com.example.pokedex.ui.filter.TypeFilterRow

@Composable
fun PokemonMainScreen(
    onPokemonClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PokemonViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        TypeFilterRow(
            types = uiState.availableTypes,
            selectedType = uiState.selectedType,
            onTypeSelected = { type -> viewModel.selectType(type) }
        )

        if (uiState.filteredList.isEmpty()) {
            EmptyStateScreen(onResetFilter = { viewModel.resetFilter() })
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.filteredList) { pokemon ->
                    PokemonCard(
                        pokemon = pokemon,
                        onPokemonClick = onPokemonClick
                    )
                }
            }
        }
    }
}
package com.example.pokedex.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pokedex.ui.card.PokemonCard
import com.example.pokedex.ui.empty.EmptyStateScreen
import com.example.pokedex.ui.filter.TypeFilterRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonMainScreen(
    onPokemonClick: (Int) -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: PokemonViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pokédex",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFDC0A2D)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TypeFilterRow(
                types = uiState.availableTypes,
                selectedType = uiState.selectedType,
                onTypeSelected = { type ->
                    if (type.isEmpty()) {
                        viewModel.resetFilter()
                    } else {
                        viewModel.selectType(type)
                    }
                }
            )

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFDC0A2D))
                }
            } else if (uiState.filteredList.isEmpty()) {
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
}
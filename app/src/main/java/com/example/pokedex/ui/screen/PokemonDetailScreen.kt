package com.example.pokedex.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.samplePokemonList
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    isImperial: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pokemon: Pokemon? = samplePokemonList.find { it.id == pokemonId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = pokemon?.name ?: "Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (pokemon == null) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Pokemon with ID $pokemonId not found",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            val heightText = if (isImperial) {
                val feet = pokemon.height * 3.28084
                String.format(Locale.US, "%.2f ft", feet)
            } else {
                "${pokemon.height} m"
            }

            val weightText = if (isImperial) {
                val lbs = pokemon.weight * 2.20462
                String.format(Locale.US, "%.2f lbs", lbs)
            } else {
                "${pokemon.weight} kg"
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = pokemon.name,
                    modifier = Modifier.size(100.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = pokemon.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Types: ${pokemon.types.joinToString(", ")}",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Characteristics",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "ID: #${pokemon.id}", fontSize = 16.sp)
                        Text(text = "Height: $heightText", fontSize = 16.sp)
                        Text(text = "Weight: $weightText", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
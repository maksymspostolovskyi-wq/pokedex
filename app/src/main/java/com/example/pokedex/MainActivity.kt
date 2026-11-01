package com.example.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.model.samplePokemonList
import com.example.pokedex.ui.screen.PokemonMainScreen
import com.example.pokedex.ui.theme.PokedexTheme

//Коротко, фільтри додані, користувач натискає на тип і фільтр оновляє колонку, додано ще тип без покемона, емптістейт, для переіврки відсутності покемона
//ШІ допоміг оформити папки правильно, без помилок(виникало багато помилок бо забував щось додати чи імпортувати)
// також поміг відфільтрувати розташування ід покемона та коло навкого самого покемона
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokedexTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(Color.White),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF28AAFD))
                                        )
                                    }
                                    Text(
                                        text = "PokéDex",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 22.sp,
                                        color = Color.White
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(0xFFDC0A2D)
                            )
                        )
                    },
                    containerColor = Color(0xFFF8F8F8),
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    PokemonMainScreen(
                        pokemonList = samplePokemonList,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
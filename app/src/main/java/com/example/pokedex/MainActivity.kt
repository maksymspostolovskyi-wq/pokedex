package com.example.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.ui.theme.PokedexTheme

// 1. Модель даних
data class Pokemon(
    val id: Int,
    val name: String,
    val types: List<String>,
    val height: Int,
    val weight: Int,
    @DrawableRes val imageRes: Int
)

// 2. Список покемонів
val samplePokemonList = listOf(
    Pokemon(
        id = 25,
        name = "Pikachu",
        types = listOf("Electric"),
        height = 4,
        weight = 60,
        imageRes = R.drawable.pikachu
    ),
    Pokemon(
        id = 1,
        name = "Bulbasaur",
        types = listOf("Grass", "Poison"),
        height = 7,
        weight = 69,
        imageRes = R.drawable.bulbasaur
    ),
    Pokemon(
        id = 4,
        name = "Charmander",
        types = listOf("Fire"),
        height = 6,
        weight = 85,
        imageRes = R.drawable.charmander
    )
)

// 3. Кольори типів
fun getTypeColor(type: String): Color {
    return when (type.lowercase()) {
        "grass" -> Color(0xFF78C850)
        "poison" -> Color(0xFFA040A0)
        "fire" -> Color(0xFFF08030)
        "flying" -> Color(0xFFA890F0)
        "water" -> Color(0xFF6890F0)
        "bug" -> Color(0xFFA8B820)
        "normal" -> Color(0xFFA8A878)
        "electric" -> Color(0xFFF8D030)
        "ground" -> Color(0xFFE0C068)
        "fairy" -> Color(0xFFEE99AC)
        "fighting" -> Color(0xFFC03028)
        "psychic" -> Color(0xFFF85888)
        "rock" -> Color(0xFFB8A038)
        "steel" -> Color(0xFFB8B8D0)
        "ice" -> Color(0xFF98D8D8)
        "ghost" -> Color(0xFF70559B)
        "dragon" -> Color(0xFF7038F8)
        else -> Color.Gray
    }
}

// 4. Бейдж типу
@Composable
fun TypeBadge(type: String) {
    Text(
        text = type.replaceFirstChar { it.uppercase() },
        color = Color.White,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(getTypeColor(type))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

// 5. MainActivity
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
                                    // Індикатор-лінза Покедекса
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF28AAFD))
                                    )
                                    Text(
                                        text = "POKÉDEX",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 22.sp,
                                        letterSpacing = 2.sp,
                                        color = Color.White
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(0xFFDC0A2D) // Класичний червоний
                            )
                        )
                    },
                    containerColor = Color(0xFFEFEFEF),
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    PokemonListScreen(
                        pokemonList = samplePokemonList,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// 6. Список
@Composable
fun PokemonListScreen(pokemonList: List<Pokemon>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(pokemonList) { pokemon ->
            PokemonCard(pokemon = pokemon)
        }
    }
}

// 7. Картка
@Composable
fun PokemonCard(pokemon: Pokemon) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0F0F0))
            ) {
                Image(
                    painter = painterResource(id = pokemon.imageRes),
                    contentDescription = pokemon.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    text = "#${pokemon.id}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .background(
                            Color.Black.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(bottomEnd = 8.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pokemon.name.replaceFirstChar { it.uppercase() },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D1D1D)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    pokemon.types.forEach { type ->
                        TypeBadge(type = type)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Зріст: ${pokemon.height / 10.0} м",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Вага: ${pokemon.weight / 10.0} кг",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
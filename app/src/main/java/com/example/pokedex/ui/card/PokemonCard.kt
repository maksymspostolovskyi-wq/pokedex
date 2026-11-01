package com.example.pokedex.ui.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.getTypeBackgroundColor
import com.example.pokedex.model.getTypeTextColor

@Composable
fun PokemonCard(
    pokemon: Pokemon,
    modifier: Modifier = Modifier
) {
    val primaryType = pokemon.types.firstOrNull() ?: "Normal"
    val circleBgColor = getTypeBackgroundColor(primaryType)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "#${pokemon.id.toString().padStart(3, '0')}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.TopEnd)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ось цей круг переробляв ШІ, ну і він трохи поміг вирівняти мені всю інформацію, в мене накладалась вона
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(circleBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png",
                        contentDescription = pokemon.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = pokemon.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        pokemon.types.forEach { type ->
                            Surface(
                                color = getTypeBackgroundColor(type),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = type,
                                    color = getTypeTextColor(type),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Height: ${pokemon.height} m  Weight: ${pokemon.weight} kg",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
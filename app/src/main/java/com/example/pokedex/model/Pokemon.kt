package com.example.pokedex.model

import androidx.compose.ui.graphics.Color

data class Pokemon(
    val id: Int,
    val name: String,
    val types: List<String>,
    val height: Double,
    val weight: Double
)

val samplePokemonList = listOf(
    Pokemon(1, "Bulbasaur", listOf("Grass", "Poison"), 0.7, 6.9),
    Pokemon(4, "Charmander", listOf("Fire"), 0.6, 8.5),
    Pokemon(7, "Squirtle", listOf("Water"), 0.5, 9.0),
    Pokemon(25, "Pikachu", listOf("Electric"), 0.4, 6.3),
    Pokemon(94, "Gengar", listOf("Ghost", "Poison"), 1.5, 40.5),
    Pokemon(131, "Lapras", listOf("Water", "Ice"), 2.5, 220.0),
    Pokemon(133, "Eevee", listOf("Normal"), 0.3, 6.5)
)

fun getTypeBackgroundColor(type: String): Color {
    return when (type) {
        "Grass" -> Color(0xFFE8F5E9)
        "Fire" -> Color(0xFFFFE0B2)
        "Water" -> Color(0xFFE1F5FE)
        "Electric" -> Color(0xFFFFF9C4)
        "Poison" -> Color(0xFFF3E5F5)
        "Ice" -> Color(0xFFE0F7FA)
        "Ghost" -> Color(0xFFEDE7F6)
        "Fairy" -> Color(0xFFFCE4EC)
        else -> Color(0xFFF5F5F5)
    }
}

fun getTypeTextColor(type: String): Color {
    return when (type) {
        "Grass" -> Color(0xFF2E7D32)
        "Fire" -> Color(0xFFE65100)
        "Water" -> Color(0xFF0277BD)
        "Electric" -> Color(0xFFF57F17)
        "Poison" -> Color(0xFF6A1B9A)
        "Ice" -> Color(0xFF00838F)
        "Ghost" -> Color(0xFF4527A0)
        "Fairy" -> Color(0xFFFCE4EC)
        else -> Color(0xFF616161)
    }
}
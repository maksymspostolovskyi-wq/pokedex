package com.example.pokedex.model

import androidx.compose.ui.graphics.Color
import com.example.pokedex.data.remote.PokemonDetailDto
import java.util.Locale

data class Pokemon(
    val id: Int,
    val name: String,
    val types: List<String>,
    val height: Double,
    val weight: Double
)

fun PokemonDetailDto.toDomain(): Pokemon {
    return Pokemon(
        id = this.id ?: 0,
        name = this.name?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } ?: "Unknown",
        types = this.types?.mapNotNull { it.type?.name?.replaceFirstChar { c -> c.titlecase(Locale.ROOT) } } ?: listOf("Normal"),
        height = (this.height ?: 0.0) / 10.0,
        weight = (this.weight ?: 0.0) / 10.0
    )
}

fun getTypeBackgroundColor(type: String): Color {
    return when (type.lowercase()) {
        "grass" -> Color(0xFFE2F9E1)
        "fire" -> Color(0xFFFDEACD)
        "water" -> Color(0xFFE0F4FE)
        "electric" -> Color(0xFFFFF9C4)
        "poison" -> Color(0xFFF3E5F5)
        "ghost" -> Color(0xFFEDE7F6)
        "ice" -> Color(0xFFE0F7FA)
        "bug" -> Color(0xFFF1F8E9)
        "normal" -> Color(0xFFF5F5F5)
        else -> Color(0xFFEFEFEF)
    }
}

fun getTypeTextColor(type: String): Color {
    return when (type.lowercase()) {
        "grass" -> Color(0xFF2E7D32)
        "fire" -> Color(0xFFE65100)
        "water" -> Color(0xFF0277BD)
        "electric" -> Color(0xFFF57F17)
        "poison" -> Color(0xFF7B1FA2)
        "ghost" -> Color(0xFF512DA8)
        "ice" -> Color(0xFF00838F)
        "bug" -> Color(0xFF558B2F)
        "normal" -> Color(0xFF616161)
        else -> Color(0xFF424242)
    }
}
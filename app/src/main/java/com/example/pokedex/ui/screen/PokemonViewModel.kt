package com.example.pokedex.ui.screen

import androidx.lifecycle.ViewModel
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.samplePokemonList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PokemonUiState(
    val selectedType: String = "All",
    val availableTypes: List<String> = listOf(
        "All", "Grass", "Fire", "Water", "Electric", "Poison", "Ice", "Ghost", "Normal", "Fairy"
    ),
    val pokemonList: List<Pokemon> = samplePokemonList
) {
    val filteredList: List<Pokemon>
        get() = if (selectedType == "All") {
            pokemonList
        } else {
            pokemonList.filter { it.types.contains(selectedType) }
        }
}

class PokemonViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonUiState())
    val uiState: StateFlow<PokemonUiState> = _uiState.asStateFlow()

    fun selectType(type: String) {
        _uiState.update { currentState ->
            currentState.copy(selectedType = type)
        }
    }

    fun resetFilter() {
        selectType("All")
    }
}
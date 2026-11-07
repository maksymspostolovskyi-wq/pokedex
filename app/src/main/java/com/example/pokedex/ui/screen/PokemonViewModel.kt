package com.example.pokedex.ui.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.UserPreferencesRepository
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.samplePokemonList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PokemonUiState(
    val filteredList: List<Pokemon> = samplePokemonList,
    val selectedType: String? = null,
    val availableTypes: List<String> = emptyList()
)

class PokemonViewModel(application: Application) : AndroidViewModel(application) {

    private val userPreferencesRepository = UserPreferencesRepository(application)

    private val _uiState = MutableStateFlow(PokemonUiState())
    val uiState: StateFlow<PokemonUiState> = _uiState.asStateFlow()

    val isImperial: StateFlow<Boolean> = userPreferencesRepository.isImperialFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    init {
        val types = samplePokemonList.flatMap { it.types }.distinct().sorted()
        _uiState.update { it.copy(availableTypes = types) }
    }

    fun selectType(type: String) {
        val newSelectedType = if (_uiState.value.selectedType == type) null else type
        val filtered = if (newSelectedType == null) {
            samplePokemonList
        } else {
            samplePokemonList.filter { it.types.contains(newSelectedType) }
        }
        _uiState.update {
            it.copy(
                selectedType = newSelectedType,
                filteredList = filtered
            )
        }
    }

    fun resetFilter() {
        _uiState.update {
            it.copy(
                selectedType = null,
                filteredList = samplePokemonList
            )
        }
    }

    fun setImperialUnit(isImperial: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveImperialSetting(isImperial)
        }
    }
}
package com.example.pokedex.ui.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.UserPreferencesRepository
import com.example.pokedex.data.remote.RetrofitInstance
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

data class PokemonUiState(
    val pokemonList: List<Pokemon> = emptyList(),
    val filteredList: List<Pokemon> = emptyList(),
    val selectedType: String? = null,
    val availableTypes: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedPokemonDetail: Pokemon? = null,
    val isDetailLoading: Boolean = false
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

    private val allStandardTypes = listOf(
        "Normal", "Fire", "Water", "Grass", "Electric", "Ice",
        "Fighting", "Poison", "Ground", "Flying", "Psychic",
        "Bug", "Rock", "Ghost", "Dragon", "Steel", "Fairy"
    )

    init {
        loadPokemonList()
    }

    fun loadPokemonList() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, error = null, availableTypes = allStandardTypes) }
            try {
                val response = RetrofitInstance.api.getPokemonList(limit = 20)
                val results = response.results ?: emptyList()

                val fetchedPokemon = coroutineScope {
                    results.map { item ->
                        async {
                            item.name?.let { name ->
                                try {
                                    RetrofitInstance.api.getPokemonDetailByName(name).toDomain()
                                } catch (e: Exception) {
                                    null
                                }
                            }
                        }
                    }.awaitAll().filterNotNull()
                }

                _uiState.update {
                    it.copy(
                        pokemonList = fetchedPokemon,
                        filteredList = applyFilter(fetchedPokemon, it.selectedType),
                        availableTypes = allStandardTypes,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                val userFriendlyMessage = when (e) {
                    is UnknownHostException, is IOException ->
                        "Відсутнє підключення до Інтернету. Перевірте авіарежим або Wi-Fi."
                    is HttpException ->
                        "Помилка сервера (Код ${e.code()}). Спробуйте пізніше."
                    is SerializationException, is IllegalArgumentException ->
                        "Отримано дані у невідповідному форматі."
                    else ->
                        "Невідома помилка мережі: ${e.localizedMessage}"
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = userFriendlyMessage,
                        availableTypes = allStandardTypes
                    )
                }
            }
        }
    }

    fun loadPokemonDetail(pokemonId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isDetailLoading = true, selectedPokemonDetail = null) }
            try {
                val detailDto = RetrofitInstance.api.getPokemonDetail(pokemonId)
                val domainPokemon = detailDto.toDomain()
                _uiState.update {
                    it.copy(
                        selectedPokemonDetail = domainPokemon,
                        isDetailLoading = false
                    )
                }
            } catch (e: Exception) {
                val fallback = _uiState.value.pokemonList.find { it.id == pokemonId }
                _uiState.update {
                    it.copy(
                        selectedPokemonDetail = fallback,
                        isDetailLoading = false
                    )
                }
            }
        }
    }

    fun selectType(type: String) {
        val newSelectedType = if (_uiState.value.selectedType.equals(type, ignoreCase = true) || type.equals("All", ignoreCase = true)) null else type
        _uiState.update { state ->
            state.copy(
                selectedType = newSelectedType,
                filteredList = applyFilter(state.pokemonList, newSelectedType)
            )
        }
    }

    private fun applyFilter(list: List<Pokemon>, selectedType: String?): List<Pokemon> {
        return if (selectedType.isNullOrEmpty() || selectedType.equals("All", ignoreCase = true)) {
            list
        } else {
            list.filter { pokemon ->
                pokemon.types.any { it.equals(selectedType, ignoreCase = true) }
            }
        }
    }

    fun resetFilter() {
        _uiState.update { state ->
            state.copy(
                selectedType = null,
                filteredList = state.pokemonList
            )
        }
    }

    fun setImperialUnit(isImperial: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveImperialSetting(isImperial)
        }
    }
}
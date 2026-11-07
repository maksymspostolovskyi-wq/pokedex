package com.example.pokedex.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.pokedex.ui.card.PokemonCard
import com.example.pokedex.ui.empty.EmptyStateScreen
import com.example.pokedex.ui.error.ErrorStateScreen
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
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Сповіщення Pokédex увімкнено!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Функція сповіщень недоступна (відхилено дозвіл)", Toast.LENGTH_LONG).show()
        }
    }

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
                    IconButton(onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            when (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)) {
                                PackageManager.PERMISSION_GRANTED -> {
                                    Toast.makeText(context, "Сповіщення вже активовані!", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                            }
                        } else {
                            Toast.makeText(context, "Сповіщення підтримуються автоматично", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Notification Permission",
                            tint = Color.White
                        )
                    }

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

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFFDC0A2D))
                    }
                }

                uiState.error != null -> {
                    ErrorStateScreen(
                        errorMessage = uiState.error!!,
                        onRetry = { viewModel.loadPokemonList() }
                    )
                }

                uiState.filteredList.isEmpty() -> {
                    EmptyStateScreen(onResetFilter = { viewModel.resetFilter() })
                }

                else -> {
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
}
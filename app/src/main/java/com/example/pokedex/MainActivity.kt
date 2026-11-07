package com.example.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokedex.ui.screen.PokemonDetailScreen
import com.example.pokedex.ui.screen.PokemonMainScreen
import com.example.pokedex.ui.screen.PokemonViewModel
import com.example.pokedex.ui.screen.SettingsScreen
import com.example.pokedex.ui.theme.PokedexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexTheme {
                val navController = rememberNavController()
                val viewModel: PokemonViewModel = viewModel()
                val isImperial by viewModel.isImperial.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "main",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("main") {
                            PokemonMainScreen(
                                onPokemonClick = { pokemonId ->
                                    navController.navigate("detail/$pokemonId")
                                },
                                onSettingsClick = {
                                    navController.navigate("settings")
                                },
                                viewModel = viewModel
                            )
                        }
                        composable("settings") {
                            SettingsScreen(
                                isImperial = isImperial,
                                onImperialChanged = { newValue ->
                                    viewModel.setImperialUnit(newValue)
                                },
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                        composable(
                            route = "detail/{pokemonId}",
                            arguments = listOf(
                                navArgument("pokemonId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: -1
                            PokemonDetailScreen(
                                pokemonId = pokemonId,
                                isImperial = isImperial,
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
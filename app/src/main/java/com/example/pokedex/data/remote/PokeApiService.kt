package com.example.pokedex.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@Serializable
data class PokemonListResponseDto(
    @SerialName("results") val results: List<NamedApiResourceDto>? = null
)

@Serializable
data class NamedApiResourceDto(
    @SerialName("name") val name: String? = null,
    @SerialName("url") val url: String? = null
)

@Serializable
data class PokemonDetailDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("height") val height: Double? = null,
    @SerialName("weight") val weight: Double? = null,
    @SerialName("types") val types: List<TypeEntryDto>? = null
)

@Serializable
data class TypeEntryDto(
    @SerialName("type") val type: NamedApiResourceDto? = null
)

interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonListResponseDto

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): PokemonDetailDto

    @GET("pokemon/{name}")
    suspend fun getPokemonDetailByName(
        @Path("name") name: String
    ): PokemonDetailDto
}

object RetrofitInstance {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    val api: PokeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PokeApiService::class.java)
    }
}
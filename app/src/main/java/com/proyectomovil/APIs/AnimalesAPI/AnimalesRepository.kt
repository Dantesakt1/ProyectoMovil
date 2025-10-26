package com.proyectomovil.APIs.AnimalesAPI

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AnimalesRepository {

    // Obtener especies (página 0 = primeras)
    suspend fun fetchSpecies(page: Int = 0): Result<SpeciesResponse> =
        withContext(Dispatchers.IO) {
            try {
                Result.success(ApiAnimalesClient.service.getSpeciesByPage(page))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    // Buscar especie por nombre
    suspend fun fetchSpeciesByName(name: String): Result<SpeciesDetailResponse> =
        withContext(Dispatchers.IO) {
            try {
                Result.success(ApiAnimalesClient.service.getSpeciesByName(name))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    // Obtener especies por país
    suspend fun fetchSpeciesByCountry(countryCode: String): Result<CountrySpeciesResponse> =
        withContext(Dispatchers.IO) {
            try {
                Result.success(ApiAnimalesClient.service.getSpeciesByCountry(countryCode))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
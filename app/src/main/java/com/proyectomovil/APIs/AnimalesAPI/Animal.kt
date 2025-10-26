package com.proyectomovil.APIs.AnimalesAPI

import com.squareup.moshi.Json

// Respuesta principal de especies
data class SpeciesResponse(
    val count: Int,
    val result: List<Species>
)

// Especie individual
data class Species(
    @Json(name = "taxonid") val taxonId: Int,
    @Json(name = "scientific_name") val scientificName: String,
    @Json(name = "kingdom") val kingdom: String?,
    @Json(name = "phylum") val phylum: String?,
    @Json(name = "class") val className: String?,
    @Json(name = "order") val order: String?,
    @Json(name = "family") val family: String?,
    @Json(name = "genus") val genus: String?,
    @Json(name = "main_common_name") val commonName: String?,
    @Json(name = "category") val category: String? // EX, EW, CR, EN, VU, NT, LC, DD
)

// Detalle de una especie específica
data class SpeciesDetailResponse(
    val result: List<SpeciesDetail>
)

data class SpeciesDetail(
    @Json(name = "taxonid") val taxonId: Int,
    @Json(name = "scientific_name") val scientificName: String,
    @Json(name = "main_common_name") val commonName: String?,
    @Json(name = "category") val category: String?,
    @Json(name = "population_trend") val populationTrend: String?,
    @Json(name = "habitat") val habitat: String?,
    @Json(name = "threats") val threats: String?
)

// Para búsqueda por país
data class CountrySpeciesResponse(
    val count: Int,
    val country: String,
    val result: List<Species>
)
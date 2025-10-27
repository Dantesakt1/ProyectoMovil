package com.proyectomovil.APIs.AnimalesAPI

import com.squareup.moshi.Json

data class SpeciesResponse(
    val count: Int,
    val result: List<Animal>
)

data class Animal(
    @Json(name = "taxonid") val taxonId: Int,
    @Json(name = "scientific_name") val scientificName: String,
    @Json(name = "main_common_name") val commonName: String?,
    @Json(name = "category") val category: String?
) {
    fun toAnimalApadrinado() = com.proyectomovil.BDLocal.AnimalApadrinado(
        id = taxonId,
        nombre = commonName ?: scientificName,
        especie = scientificName,
        imagen = null,
        estadoConservacion = category,
        estaApadrinado = false,
        usuarioId = null
    )
}
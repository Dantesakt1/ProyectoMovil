package com.proyectomovil.APIs.AnimalesAPI

import retrofit2.http.*

interface ApiAnimalesService {

    // Obtener especies por página (página 0 = primeras especies)
    // https://apiv3.iucnredlist.org/api/v3/species/page/0
    @GET("species/page/{page}")
    suspend fun getSpeciesByPage(@Path("page") page: Int): SpeciesResponse

    // Buscar una especie por nombre científico
    // https://apiv3.iucnredlist.org/api/v3/species/panthera%20leo
    @GET("species/{name}")
    suspend fun getSpeciesByName(@Path("name") name: String): SpeciesDetailResponse

    // Obtener especies de un país específico
    // Códigos de país: US, BR, CL, AR, MX, etc.
    // https://apiv3.iucnredlist.org/api/v3/country/getspecies/CL
    @GET("country/getspecies/{country_code}")
    suspend fun getSpeciesByCountry(@Path("country_code") countryCode: String): CountrySpeciesResponse

    // Obtener especies por región
    // https://apiv3.iucnredlist.org/api/v3/region/mediterranean/species/page/0
    @GET("region/{region}/species/page/{page}")
    suspend fun getSpeciesByRegion(
        @Path("region") region: String,
        @Path("page") page: Int
    ): SpeciesResponse
}